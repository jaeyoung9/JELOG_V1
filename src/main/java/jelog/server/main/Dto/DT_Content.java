package jelog.server.main.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Model.DN_Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Description :
 * PackageName : jelog.server.main.Dto
 * FileName : DT_Content
 * Author : User
 * TimeDate : 2023-08-03
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-08-03               User                최초생성
 * ------------------------------------------------------------
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DT_Content {
    private int contentId;
    private OsEnum contentCategories;
    private String contentThumbnail;
    private String contentTitle;
    private String contentBody;
    private int views;

    private List<DT_Files> files;

    private List<DT_Comment> comments;

    /* Table -> DTO **/
    public DT_Content(DN_Content content) {
        this.contentId = content.getContentId();
        this.contentCategories = content.getContentCategories();
        this.contentThumbnail = content.getContentThumbnail();
        this.contentTitle = content.getContentTitle();
        this.contentBody = content.getContentBody();
        this.views = content.getViews();
        this.files = content.getFiles().stream()
                .map(DT_Files::new)
                .collect(Collectors.toList());
        this.comments = content.getComments().stream()
                .map(DT_Comment::new)
                .collect(Collectors.toList());
    }

    // Getters and setters
    public static DN_Content toEntity(DT_Content dto) {
        DN_Content content = new DN_Content();
        content.setContentId(dto.getContentId());
        content.setContentCategories(dto.getContentCategories());
        content.setContentThumbnail(dto.getContentThumbnail());
        content.setContentTitle(dto.getContentTitle());
        content.setContentBody(dto.getContentBody());
        content.setViews(dto.getViews());
        // Map files and comments back to entities if needed
        return content;
    }
}
