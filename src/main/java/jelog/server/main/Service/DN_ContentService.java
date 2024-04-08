package jelog.server.main.Service;

import jelog.server.main.Dto.DT_Content;
import jelog.server.main.Dto.DT_Files;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Model.DN_Files;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Repositories.DN_CommentRepositories;
import jelog.server.main.Repositories.DN_ContentRepositories;
import jelog.server.main.Repositories.DN_FilesRepositories;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.experimental.categories.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Description :
 * PackageName : jelog.server.main.Service
 * FileName : DN_ContentService
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * 2024-04-08               jyMin                @Autowired 수정
 */

@Slf4j
@Service
public class DN_ContentService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------

    private final DN_FilesRepositories dn_filesRepositories;
    private final DN_CommentRepositories dn_commentRepositories;
    private final DN_ContentRepositories dn_contentRepositories;

    @Autowired
    public DN_ContentService(DN_FilesRepositories dn_filesRepositories, DN_CommentRepositories dn_commentRepositories, DN_ContentRepositories dn_contentRepositories){
        this.dn_filesRepositories = dn_filesRepositories;
        this.dn_commentRepositories = dn_commentRepositories;
        this.dn_contentRepositories = dn_contentRepositories;
    }

    /**
     * [Content]
     * Validate
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    public void validateExistContentId(int contentId){
        boolean data = dn_contentRepositories.existsById(contentId);
        if(true != data){
            log.warn("not exist contentId");
            throw  new RuntimeException("not exist contentId");
        }
    }

    public void validateContent(final DT_Content _model){
        if(null == _model){
            log.warn("data is check for cannot be null.");
            throw new RuntimeException("data is check for cannot be null.");
        }
    }

    /**
     * [Content]
     * Content Services
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------

    /**
     * [Content] Create Content
     * */
    public DN_Content createContent(DT_Content entity){

        validateContent(entity);

        // DT -> DN conversion
        DN_Content content = new DN_Content();
        content.setContentCategories(entity.getContentCategories());
        content.setContentTitle(entity.getContentTitle());
        content.setContentBody(entity.getContentBody());
        content.setContentThumbnail(entity.getContentThumbnail());
        content = dn_contentRepositories.save(content);

        // Create and save DT_Files for each file in the DTO
        List<DT_Files> dtFilesList = entity.getFiles();
        if(null != dtFilesList){
            for(DT_Files dtFile :dtFilesList){
                DN_Files saveFile = new DN_Files();
                saveFile.setFilePath(dtFile.getFilePath());
                saveFile.setFileName(dtFile.getFileName());
                saveFile.setMediaType(dtFile.getMediaType());
                saveFile.setResultFile(dtFile.getResultFile());
                saveFile.setContentId(content.getContentId());
                saveFile.setContent(content);
                dn_filesRepositories.save(saveFile);
            }
        }
        return content;
    }

    /**
     * [Content] Main Content Page
     * */
    public Page<?> findPage(Pageable pageable, String Title, OsEnum Categories){

        Set<OsEnum> categories = new HashSet<>();

        if(null != Categories){
            categories.add(Categories);
        }else {
            categories.add(OsEnum.LegacyCode(49847));
            categories.add(OsEnum.LegacyCode(42958));
            categories.add(OsEnum.LegacyCode(42959));
            categories.add(OsEnum.LegacyCode(47685));
            categories.add(OsEnum.LegacyCode(45656));
            categories.add(OsEnum.LegacyCode(48765));
            categories.add(OsEnum.LegacyCode(41232));
            categories.add(OsEnum.LegacyCode(49999));
        }

        return dn_contentRepositories.findDN_ContentByContentTitleContainsAndContentCategoriesInOrContentCategoriesIsNullOrderByContentIdDesc(Title, categories, pageable);
    }

    /**
     * [Content] Main Content Detail
     * */
    @Transactional
    public DN_Content findContent(int mainNumber){

        validateExistContentId(mainNumber);

        return dn_contentRepositories.findByContentId(mainNumber);
    }

    /**
     * [Content] View Count
     * */
    @Transactional
    public String contentViewCount(int contentNumber){

        // 데이터 존재 여부 체크.
        validateExistContentId(contentNumber);

        DN_Content entity = dn_contentRepositories.findByContentId(contentNumber);
        entity.setViews(entity.getViews() + 1);
        dn_contentRepositories.save(entity);
        return "";
    }
}
