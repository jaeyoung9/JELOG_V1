package jelog.server.main.Model;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Description :
 * PackageName : jelog.server.main.Model
 * FileName : DN_Content
 * Author : MinJaeYoung
 * TimeDate : 2023-07-28
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-28               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 *     contentCategories Enum
 *      - Other("Other_Categories", 49847),
 *      - Java("Java_Categories",42958),
 *      - JavaScript("JavaScript_Categories",42959),
 *      - C("C_Categories",47685),
 *      - Python("Python",45656),
 *      - Shell("ShellScript_Categories",  48765),
 *      - Security("Security_Categories", 41232),
 *      - DeveloperCareerSkills("DeveloperCareerSkills_Categories",49999),
 *
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SequenceGenerator(name = "contentSequence", sequenceName = "contentSequence", initialValue = 987685)
public class DN_Content {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contentSequence")
    private int contentId;

    @Convert(converter = OsEnumConverter.class)
    private OsEnum contentCategories;

    private String contentThumbnail;
    private String contentTitle;
    private String contentBody;
    private int views;


}