package at.ac.univie.rscm.application.global.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadFileInfo {
    private String filename;
    private String creationDate;
    private long size;
    private String path;
    
	public DownloadFileInfo(String filename, String creationDate, String path, long size) {
		super();
		this.size = size;
		this.filename = filename;
		this.creationDate = creationDate;
		this.path = path;
	}

   
}