package jelog.server.main.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Description :
 * PackageName : jelog.server.main.Model
 * FileName : DN_Enums
 * Author : MinJaeYoung
 * TimeDate : 2025-07-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2025-07-05               MinJaeYoung                최초생성
 * 2025-07-05               MinJaeYoung                해당 모델링 테이블은 enum을 DB로 관리하기 위해 추가 된 테이블이다.
 * ------------------------------------------------------------
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SequenceGenerator(name = "enums", sequenceName = "enumSequence", initialValue = 8766559)
public class DN_Enums {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enumSequence")
    private int enumId;

    // enumParentId is left with the parent enumId, if at its highest, enumParentId is null or empty.
    private int enumParentId;

    private String enumName; // 이름
    private String enumType;   // “어떤 그룹(도메인)인가?”를 나타내는 상위 구분자 예: CATEGORY, ROLE, STATUS …
    private String enumValue; // 해당 그룹 안에서 구체적으로 무엇인가?를 식별하는 코드 값 ( 백단 로직에선 해당 값을 사용 ) JAVA, ADMIN, PUBLISHED, ARCHIVED …
    private String description; // 설명
    private Boolean status; // 상태 활성, 비활성
    private Integer sortOrder; // 정렬 순서

    @CreationTimestamp
    private LocalDateTime inDate; // 생성일

}
