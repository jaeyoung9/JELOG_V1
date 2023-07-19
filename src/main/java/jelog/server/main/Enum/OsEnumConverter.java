package jelog.server.main.Enum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Description :
 * PackageName : jelog.server.main.Enum
 * FileName : OsEnumConverter
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-15               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * 2023-07-18               MinJaeYoung                Enum 직열화 오류
 * 2023-07-19               MinJaeYoung                Enum Index 변경가능 여부 확인중 상수값이라 불가능 할것으로 보이나 테스트 중
 */
@Converter
public class OsEnumConverter implements AttributeConverter<OsEnum,Integer> {

    /**
     * Enum to DataBase
     * */
    @Override
    public Integer convertToDatabaseColumn(OsEnum osEnum){
        return osEnum.getTitleCode();
    }

    /**
     * DataBase to Enum
     * */
    @Override
    public OsEnum convertToEntityAttribute(Integer data){
        return OsEnum.LegacyCode(data);
    }
}
