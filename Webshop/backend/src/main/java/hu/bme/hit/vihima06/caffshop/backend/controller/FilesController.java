package hu.bme.hit.vihima06.caffshop.backend.controller;

import hu.bme.hit.vihima06.caffshop.backend.api.FilesApi;
import hu.bme.hit.vihima06.caffshop.backend.models.*;
import hu.bme.hit.vihima06.caffshop.backend.config.Constants;
import hu.bme.hit.vihima06.caffshop.backend.service.FileService;
import hu.bme.hit.vihima06.caffshop.backend.service.dto.FileDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
public class FilesController implements FilesApi {

    @Autowired
    private FileService fileService;

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFilesDelete(Integer id) {
        fileService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CaffDetailsResponse> getFileDetails(Integer id) {
        CaffDetailsResponse fileDetails = fileService.getFileDetailsById(id);

        return new ResponseEntity<>(fileDetails, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Resource> getFilesDownload(Integer id) {
        FileDataDTO fileDataDTO = fileService.getCaffFileForDownloadById(id);

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(fileDataDTO.getLength());
        respHeaders.setContentDispositionFormData(
                "attachment",
                fileDataDTO.getFileName() + Constants.CAFF_EXTENSION
        );

        return new ResponseEntity<>(fileDataDTO.getFileResource(), respHeaders, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Resource> getFilesPreview(Integer id) {
        FileDataDTO fileDataDTO = fileService.getPreviewFileDataById(id);

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(fileDataDTO.getLength());
        respHeaders.setContentDispositionFormData(
                "attachment",
                fileDataDTO.getFileName() + Constants.BMP_EXTENSION
        );

        return new ResponseEntity<>(fileDataDTO.getFileResource(), respHeaders, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CaffResponse>> getFilesSearch(String query) {
        List<CaffResponse> files = fileService.searchCaffFiles(query);

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CommentResponse> postFilesCommentFileId(Integer fileId, CommentRequest body) {
        CommentResponse comment = fileService.comment(fileId, body);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CaffDetailsResponse> putFilesModifyId(Integer id, ModifyCaffRequest body) {
        CaffDetailsResponse file = fileService.modifyFile(id, body);

        return new ResponseEntity<>(file, HttpStatus.ACCEPTED);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FileUploadResponse> postFilesUpload(String name, Double price, MultipartFile file) {
        FileUploadResponse fileUploadResponse = fileService.uploadFile(name, price, file);

        return new ResponseEntity<>(fileUploadResponse, HttpStatus.CREATED);
    }

}
