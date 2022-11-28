package hu.bme.hit.vihima06.caffshop.backend.service;

import hu.bme.hit.vihima06.caffshop.backend.config.Constants;
import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.BadRequestException;
import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.ForbiddenException;
import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.NotFoundException;
import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.UnauthorizedException;
import hu.bme.hit.vihima06.caffshop.backend.mapper.CaffFileDataMapper;
import hu.bme.hit.vihima06.caffshop.backend.mapper.CommentMapper;
import hu.bme.hit.vihima06.caffshop.backend.model.CaffFileData;
import hu.bme.hit.vihima06.caffshop.backend.model.Comment;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.models.*;
import hu.bme.hit.vihima06.caffshop.backend.repository.CaffFileDataRepository;
import hu.bme.hit.vihima06.caffshop.backend.repository.CommentRepository;
import hu.bme.hit.vihima06.caffshop.backend.repository.UserRepository;
import hu.bme.hit.vihima06.caffshop.backend.security.service.LoggedInUserService;
import hu.bme.hit.vihima06.caffshop.backend.service.dto.FileDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Value("${caffshop.upload-folder}")
    private String uploadFolder;

    @Autowired
    private CaffFileDataRepository caffFileDataRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LoggedInUserService loggedInUserService;

    public void deleteById(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        if (!loggedInUser.getId().equals(caffFileData.getCreator().getId()) && !loggedInUserService.isAdmin()) {
            throw new ForbiddenException("File access denied");
        }

        // TODO delete caff file and preview as well

        caffFileDataRepository.delete(caffFileData);
    }

    public CaffDetailsResponse getFileDetailsById(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        CaffDetailsResponse caffDetailsResponse = CaffFileDataMapper.INSTANCE.fileToCaffDetailsResponse(caffFileData);

        caffDetailsResponse.setPurchased(caffFileData.getUsersPurchased().stream().anyMatch(u -> u.getId().equals(loggedInUser.getId())));

        return caffDetailsResponse;
    }

    public List<CaffResponse> searchCaffFiles(String query) {
        List<CaffFileData> results = caffFileDataRepository.findAllByNameContains(query);

        return results.stream().map(r -> CaffFileDataMapper.INSTANCE.fileToCaffResponse(r)).toList();
    }

    public CaffDetailsResponse modifyFile(Integer id, ModifyCaffRequest modifyCaffRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        if (!loggedInUser.getId().equals(caffFileData.getCreator().getId()) && !loggedInUserService.isAdmin()) {
            throw new ForbiddenException("File access denied");
        }

        caffFileData.setName(modifyCaffRequest.getName());

        caffFileDataRepository.save(caffFileData);

        return CaffFileDataMapper.INSTANCE.fileToCaffDetailsResponse(caffFileData);
    }

    public CommentResponse comment(Integer fileId, CommentRequest commentRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData file = caffFileDataRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found"));

        Comment comment = new Comment(commentRequest.getComment(), loggedInUser, file);

        commentRepository.save(comment);

        return CommentMapper.INSTANCE.commentToCommentResponse(comment);
    }

    public FileDataDTO getCaffFileForDownloadById(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        // TODO check if file is bought
        // new ForbiddenException("File is not bought")

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        return getFileDataDTOByCaff(caffFileData, Constants.CAFF_FOLDER, Constants.CAFF_EXTENSION);
    }

    public FileDataDTO getPreviewFileDataById(Integer id) {
        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        return getFileDataDTOByCaff(caffFileData, Constants.PREVIEW_FOLDER, Constants.BMP_EXTENSION);
    }

    public FileUploadResponse uploadFile(String name, Double price, MultipartFile file) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        if (price < 0) {
            throw new BadRequestException("Price should not be less than 0");
        }

        if (name.length() < 3) {
            throw new BadRequestException("Name should be at list 3 character long");
        }

        String fileName = UUID.randomUUID().toString();

        try {
            File uploadfile = new File(
                    uploadFolder
                            + File.separator
                            + Constants.CAFF_FOLDER
                            + File.separator
                            + fileName
                            + Constants.CAFF_EXTENSION
            );
            file.transferTo(uploadfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO parse
        Runtime rt = Runtime.getRuntime();
        String[] commands = { "/bin/bash", "-c", "cd /usr/local/app/parser/ && ./parser /usr/local/app/uploads/caffs/" + fileName + ".caff preview /usr/local/app/uploads/previews/" + fileName +".bmp" };
        Process proc = null;
        try {
            proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            System.out.println("Here is the standard output of the command:");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            System.out.println("Here is the standard error of the command (if any):");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CaffFileData caffFileData = new CaffFileData(
                name,
                fileName,
                loggedInUser,
                List.of("tags"),
                1,
                1,
                price,
                1.0
        );

        caffFileDataRepository.save(caffFileData);

        return CaffFileDataMapper.INSTANCE.fileToFileUploadResponse(caffFileData);
    }

    private FileDataDTO getFileDataDTOByCaff(CaffFileData caffFileData, String folder, String extension) {
        File file = new File(
                uploadFolder
                        + File.separator
                        + folder
                        + File.separator
                        + caffFileData.getStoredFileName()
                        + extension
        );

        if (!file.exists()) {
            throw new NotFoundException("File not found");
        }

        return new FileDataDTO(caffFileData.getName(), new FileSystemResource(file), file.length());
    }

    public void buyFileById(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData file = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        file.getUsersPurchased().add(loggedInUser);

        caffFileDataRepository.save(file);
    }
}
