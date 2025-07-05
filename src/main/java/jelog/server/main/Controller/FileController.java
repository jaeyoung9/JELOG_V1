package jelog.server.main.Controller;

import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Global.ResponseResult;
import jelog.server.main.Global.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Description : 파일 업로드 컨트롤러
 * PackageName : jelog.server.main.Controller
 * FileName : FileController
 * Author : MinJaeYoung
 * TimeDate : 2024-01-01
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-01-01               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    
    // 허용되는 이미지 확장자
    private static final String[] ALLOWED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "webp"};
    
    // 허용되는 문서 확장자
    private static final String[] ALLOWED_DOCUMENT_EXTENSIONS = {"pdf", "doc", "docx", "txt", "md"};
    
    // 최대 파일 크기 (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Value("${file.upload.path:/uploads}")
    private String uploadPath;

    @Value("${file.upload.url:/api/files}")
    private String fileUrlPrefix;

    /**
     * 이미지 파일 업로드 (블로그 포스트용)
     */
    @PostMapping("/images")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 파일 유효성 검증
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .result(ResponseResult.FAIL)
                    .message("파일이 선택되지 않았습니다.")
                    .build());
            }

            // 파일 크기 검증
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .result(ResponseResult.FAIL)
                    .message("파일 크기는 10MB를 초과할 수 없습니다.")
                    .build());
            }

            // 파일 확장자 검증
            if (!ValidationUtils.isValidFileExtension(file.getOriginalFilename(), ALLOWED_IMAGE_EXTENSIONS)) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .result(ResponseResult.FAIL)
                    .message("지원하지 않는 파일 형식입니다. (jpg, jpeg, png, gif, webp만 허용)")
                    .build());
            }

            // 파일 저장
            String savedFileName = saveFile(file, "images");
            String fileUrl = fileUrlPrefix + "/images/" + savedFileName;

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("fileName", savedFileName);
            responseData.put("originalName", file.getOriginalFilename());
            responseData.put("fileUrl", fileUrl);
            responseData.put("fileSize", file.getSize());

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("이미지가 성공적으로 업로드되었습니다.")
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error uploading image", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("이미지 업로드 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 문서 파일 업로드
     */
    @PostMapping("/documents")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            // 파일 유효성 검증
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .result(ResponseResult.FAIL)
                    .message("파일이 선택되지 않았습니다.")
                    .build());
            }

            // 파일 크기 검증
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .result(ResponseResult.FAIL)
                    .message("파일 크기는 10MB를 초과할 수 없습니다.")
                    .build());
            }

            // 파일 확장자 검증
            if (!ValidationUtils.isValidFileExtension(file.getOriginalFilename(), ALLOWED_DOCUMENT_EXTENSIONS)) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .result(ResponseResult.FAIL)
                    .message("지원하지 않는 파일 형식입니다. (pdf, doc, docx, txt, md만 허용)")
                    .build());
            }

            // 파일 저장
            String savedFileName = saveFile(file, "documents");
            String fileUrl = fileUrlPrefix + "/documents/" + savedFileName;

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("fileName", savedFileName);
            responseData.put("originalName", file.getOriginalFilename());
            responseData.put("fileUrl", fileUrl);
            responseData.put("fileSize", file.getSize());

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("문서가 성공적으로 업로드되었습니다.")
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error uploading document", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("문서 업로드 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 파일 다운로드/조회
     */
    @GetMapping("/{type}/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String type, @PathVariable String fileName) {
        try {
            // type 검증 (images 또는 documents만 허용)
            if (!type.equals("images") && !type.equals("documents")) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(uploadPath, type, fileName);
            
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(fileContent);

        } catch (Exception e) {
            log.error("Error retrieving file: " + fileName, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 파일 삭제 (관리자 또는 파일 소유자만)
     */
    @DeleteMapping("/{type}/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteFile(@PathVariable String type, @PathVariable String fileName) {
        try {
            // type 검증
            if (!type.equals("images") && !type.equals("documents")) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(uploadPath, type, fileName);
            
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Files.delete(filePath);

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("파일이 성공적으로 삭제되었습니다.")
                .build());

        } catch (Exception e) {
            log.error("Error deleting file: " + fileName, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("파일 삭제 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 파일 저장 로직
     */
    private String saveFile(MultipartFile file, String type) throws IOException {
        // 업로드 디렉토리 생성
        Path uploadDir = Paths.get(uploadPath, type);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 날짜별 서브 디렉토리 생성
        String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path dateDir = uploadDir.resolve(dateFolder);
        if (!Files.exists(dateDir)) {
            Files.createDirectories(dateDir);
        }

        // 파일명 생성 (UUID + 원본 확장자)
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        String savedFileName = dateFolder + "/" + UUID.randomUUID().toString() + extension;
        Path filePath = uploadDir.resolve(savedFileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return savedFileName;
    }
}