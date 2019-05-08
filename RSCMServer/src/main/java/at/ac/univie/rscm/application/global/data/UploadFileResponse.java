package at.ac.univie.rscm.application.global.data;

import lombok.Getter;
import lombok.Setter;

//https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
@Getter
@Setter
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

	// Getters and Setters (Omitted for brevity)
}