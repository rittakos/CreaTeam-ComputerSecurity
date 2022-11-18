package hu.bme.hit.vihima06.caffshop.backend.service.dto;

import org.springframework.core.io.Resource;

public class FileDataDTO {
    private String fileName;
    private Resource fileResource;
    private long length;

    public FileDataDTO() {
    }

    public FileDataDTO(String fileName, Resource fileResource, long length) {
        this.fileName = fileName;
        this.fileResource = fileResource;
        this.length = length;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Resource getFileResource() {
        return fileResource;
    }

    public void setFileResource(Resource fileResource) {
        this.fileResource = fileResource;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
