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
