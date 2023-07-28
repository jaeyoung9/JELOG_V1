package jelog.server.main.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Description :
 * PackageName : jelog.server.main.Model
 * FileName : DN_Files
 * Author : MinJaeYoung
 * TimeDate : 2023-07-28
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-28               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SequenceGenerator(name = "fileSequence", sequenceName = "fileSequence", initialValue = 766559)
public class DN_Files {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fileSequence")
    private int fileId;
    private int contentId;

    private String filePath;
    private String fileName;
    private String mediaType;
    private String resultFile;
}
