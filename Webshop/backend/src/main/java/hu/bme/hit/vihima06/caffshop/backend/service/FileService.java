package hu.bme.hit.vihima06.caffshop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.hit.vihima06.caffshop.backend.config.Constants.*;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private String uploadFolder;

    private String parserPath;

    private final CaffFileDataRepository caffFileDataRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final LoggedInUserService loggedInUserService;

    public FileService(@Value("${caffshop.upload-folder}") String uploadFolder, @Value("${caffshop.parser-path}") String parserPath, CaffFileDataRepository caffFileDataRepository, CommentRepository commentRepository, UserRepository userRepository, LoggedInUserService loggedInUserService) {
        this.uploadFolder = uploadFolder;
        this.parserPath = parserPath;
        this.caffFileDataRepository = caffFileDataRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.loggedInUserService = loggedInUserService;

        File previewDirectory = new File(uploadFolder + File.separator + PREVIEW_FOLDER);
        if (!previewDirectory.exists()) {
            previewDirectory.mkdirs();
            logger.info("Preview folder created");
        }

        logger.info("Initialized");
    }

    public void deleteById(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        if (!loggedInUser.getId().equals(caffFileData.getCreator().getId()) && !loggedInUserService.isAdmin()) {
            logger.error("Access denied for deleting file by id {} by user {}", id, loggedInUser.getUsername());
            throw new ForbiddenException("File access denied");
        }

        File caffFile = new File(
                uploadFolder
                        + File.separator
                        + CAFF_FOLDER
                        + File.separator
                        + caffFileData.getStoredFileName()
                        + CAFF_EXTENSION
        );

        if (caffFile.exists()) {
            caffFile.delete();
        }

        File previewFile = new File(
                uploadFolder
                        + File.separator
                        + PREVIEW_FOLDER
                        + File.separator
                        + caffFileData.getStoredFileName()
                        + BMP_EXTENSION
        );

        if (previewFile.exists()) {
            previewFile.delete();
        }

        caffFileDataRepository.delete(caffFileData);

        logger.info("File {} deleted by user {}", id, loggedInUser.getUsername());
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
            logger.error("Access denied for editing file by id {} by user {}", id, loggedInUser.getUsername());
            throw new ForbiddenException("File access denied");
        }

        caffFileData.setName(modifyCaffRequest.getName());

        caffFileDataRepository.save(caffFileData);

        logger.info("File modified successfully by id {} by user {}", id, loggedInUser.getUsername());

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

        User user = userRepository.findById(loggedInUser.getId()).orElseThrow(() -> new ForbiddenException("User not found"));

        if (!user.getPurchasedCaffFiles().stream().anyMatch(f -> f.getId().equals(id)) && !user.getCaffFiles().stream().anyMatch(f -> f.getId().equals(id)) && !loggedInUserService.isAdmin()) {
            logger.error("File download forbidden for user {} by file id {}", loggedInUser.getUsername(), id);
            throw new ForbiddenException("File not purchased");
        }

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        logger.info("User {} downloaded file {}", loggedInUser.getUsername(), id);

        return getFileDataDTOByCaff(caffFileData, CAFF_FOLDER, CAFF_EXTENSION);
    }

    public FileDataDTO getPreviewFileDataById(Integer id) {
        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        return getFileDataDTOByCaff(caffFileData, PREVIEW_FOLDER, BMP_EXTENSION);
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

        logger.info("File uploaded by user {} saving with UUID {}", loggedInUser.getUsername(), fileName);

        File uploadedfile;
        try {
            uploadedfile = new File(
                    uploadFolder
                            + File.separator
                            + CAFF_FOLDER
                            + File.separator
                            + fileName
                            + CAFF_EXTENSION
            );
            file.transferTo(uploadedfile);
        } catch (IOException e) {
            logger.error("Error while saving uploaded file: {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }

        Runtime rt = Runtime.getRuntime();
        String[] dataCommand = { "/bin/bash", "-c", "cd " + parserPath + " && ./parser " + uploadFolder + File.separator + CAFF_FOLDER + File.separator + fileName + CAFF_EXTENSION + " " + PARSER_DATA };
        String[] previewCommand = { "/bin/bash", "-c", "cd " + parserPath + " && ./parser " + uploadFolder + File.separator + CAFF_FOLDER + File.separator + fileName + CAFF_EXTENSION + " " + PARSER_PREVIEW + " " + uploadFolder + File.separator + PREVIEW_FOLDER + File.separator + fileName + BMP_EXTENSION };
        String input;
        try {
            Process proc = rt.exec(dataCommand);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String error = stdError.lines().collect(Collectors.joining());
            input = stdInput.lines().collect(Collectors.joining());

            if (!error.isEmpty()) {
                uploadedfile.delete();
                logger.error("Failed parsing file data {} uploaded by {}", fileName, loggedInUser.getUsername());
                throw new BadRequestException("Invalid caff file");
            }

            proc = rt.exec(previewCommand);
            stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            error = stdError.lines().collect(Collectors.joining());

            if (!error.isEmpty()) {
                uploadedfile.delete();
                logger.error("Failed parsing file preview {} uploaded by {}", fileName, loggedInUser.getUsername());
                throw new BadRequestException("Invalid caff file");
            }

        } catch (IOException e) {
            uploadedfile.delete();
            logger.error("Error while parsing file {} uploaded by {}: {}", fileName, loggedInUser.getUsername(), e.getMessage());
            throw new InternalServerErrorException("Error while parsing");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(input);
        } catch (JsonProcessingException e) {
            uploadedfile.delete();
            logger.error("Error while parsing json file data for {} uploaded by {}: {}", fileName, loggedInUser.getUsername(), e.getMessage());
            throw new BadRequestException("Invalid caff file");
        }

        CaffFileData caffFileData = new CaffFileData(
                name,
                fileName,
                loggedInUser,
                jsonArrayToList(node.get("Tags")),
                node.get("Width").asInt(),
                node.get("Height").asInt(),
                price,
                node.get("Duration").asDouble() / 1000
        );

        caffFileDataRepository.save(caffFileData);

        logger.info("File uploaded by user {} saved as {}", loggedInUser.getUsername(), fileName);

        return CaffFileDataMapper.INSTANCE.fileToFileUploadResponse(caffFileData);
    }

    private List<String> jsonArrayToList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node.isArray()) {
            for (final JsonNode objNode : node) {
                list.add(objNode.asText());
            }
        }
        return list;
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
            logger.error("File {} not found", file);
            throw new NotFoundException("File not found");
        }

        return new FileDataDTO(caffFileData.getName(), new FileSystemResource(file), file.length());
    }

    public void buyFileById(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData file = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        file.getUsersPurchased().add(loggedInUser);

        logger.info("File {} bought by user {}", file.getId(), loggedInUser.getUsername());

        caffFileDataRepository.save(file);
    }
}
