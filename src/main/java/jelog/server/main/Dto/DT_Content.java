package jelog.server.main.Dto;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Model.DN_Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    /* Table -> DTO **/
    public DT_Content(final DN_Content dataTo){
        this.contentId = dataTo.getContentId();
        this.contentCategories = dataTo.getContentCategories();
        this.contentThumbnail = dataTo.getContentThumbnail();
        this.contentTitle = dataTo.getContentTitle();
        this.contentBody = dataTo.getContentBody();
        this.views = dataTo.getViews();
    }

    /* DTO ->  Table **/
    public static DN_Content dnContent(final DT_Content dto){
        return DN_Content.builder()
                .contentId(dto.getContentId())
                .contentCategories(dto.getContentCategories())
                .contentThumbnail(dto.getContentThumbnail())
                .contentTitle(dto.getContentTitle())
                .contentBody(dto.getContentBody())
                .views(dto.getViews())
                .build();
    }


}
