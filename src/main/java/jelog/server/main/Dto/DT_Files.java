package jelog.server.main.Dto;

import jelog.server.main.Model.DN_Files;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description :
 * PackageName : jelog.server.main.Dto
 * FileName : DT_Files
 * Author : User
 * TimeDate : 2023-10-25
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-10-25               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DT_Files {

    private int fileId;
    private String filePath;
    private String fileName;
    private String mediaType;
    private byte[] resultFile;
    private DT_Content content; // Dependency reference

    public DT_Files(DN_Files dnFiles) {
        this.fileId = dnFiles.getFileId();
        this.filePath = dnFiles.getFilePath();
        this.fileName = dnFiles.getFileName();
        this.mediaType = dnFiles.getMediaType();
        this.resultFile = dnFiles.getResultFile();
    }
}
