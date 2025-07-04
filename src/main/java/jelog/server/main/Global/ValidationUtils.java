package jelog.server.main.Global;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Description : 공통 유효성 검증 유틸리티
 * PackageName : jelog.server.main.Global
 * FileName : ValidationUtils
 * Author : MinJaeYoung
 * TimeDate : 2024-01-01
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-01-01               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\d{2,3}-\\d{3,4}-\\d{4}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
    );

    /**
     * 이메일 형식 유효성 검증
     * @param email 검증할 이메일
     * @return 유효성 검증 결과
     */
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 전화번호 형식 유효성 검증
     * @param phone 검증할 전화번호
     * @return 유효성 검증 결과
     */
    public static boolean isValidPhone(String phone) {
        return StringUtils.hasText(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 강력한 비밀번호 유효성 검증
     * 최소 8자, 영문자, 숫자, 특수문자 포함
     * @param password 검증할 비밀번호
     * @return 유효성 검증 결과
     */
    public static boolean isValidPassword(String password) {
        return StringUtils.hasText(password) && PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * 문자열 길이 유효성 검증
     * @param text 검증할 문자열
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @return 유효성 검증 결과
     */
    public static boolean isValidLength(String text, int minLength, int maxLength) {
        if (!StringUtils.hasText(text)) {
            return minLength == 0;
        }
        int length = text.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * 숫자 범위 유효성 검증
     * @param number 검증할 숫자
     * @param min 최소값
     * @param max 최대값
     * @return 유효성 검증 결과
     */
    public static boolean isValidRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

    /**
     * 페이지 번호 유효성 검증
     * @param page 페이지 번호
     * @return 유효성 검증 결과
     */
    public static boolean isValidPageNumber(int page) {
        return page >= 0;
    }

    /**
     * 페이지 크기 유효성 검증
     * @param size 페이지 크기
     * @return 유효성 검증 결과
     */
    public static boolean isValidPageSize(int size) {
        return size > 0 && size <= 100; // 최대 100개까지 허용
    }

    /**
     * 파일 확장자 유효성 검증
     * @param fileName 파일명
     * @param allowedExtensions 허용된 확장자 배열
     * @return 유효성 검증 결과
     */
    public static boolean isValidFileExtension(String fileName, String[] allowedExtensions) {
        if (!StringUtils.hasText(fileName)) {
            return false;
        }
        
        String extension = getFileExtension(fileName);
        if (!StringUtils.hasText(extension)) {
            return false;
        }
        
        for (String allowedExt : allowedExtensions) {
            if (extension.equalsIgnoreCase(allowedExt)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 파일 확장자 추출
     * @param fileName 파일명
     * @return 파일 확장자
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    /**
     * HTML 태그 제거
     * @param input 입력 문자열
     * @return HTML 태그가 제거된 문자열
     */
    public static String stripHtmlTags(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        return input.replaceAll("<[^>]*>", "");
    }

    /**
     * XSS 방지를 위한 특수문자 이스케이프
     * @param input 입력 문자열
     * @return 이스케이프된 문자열
     */
    public static String escapeHtml(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}