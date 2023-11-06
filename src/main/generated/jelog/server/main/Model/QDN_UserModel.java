package jelog.server.main.Model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDN_UserModel is a Querydsl query type for DN_UserModel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDN_UserModel extends EntityPathBase<DN_UserModel> {

    private static final long serialVersionUID = 1620943453L;

    public static final QDN_UserModel dN_UserModel = new QDN_UserModel("dN_UserModel");

    public final StringPath daSignID = createString("daSignID");

    public final BooleanPath dnGuest = createBoolean("dnGuest");

    public final StringPath dnGuestAnswer = createString("dnGuestAnswer");

    public final StringPath dnGuestQuestion = createString("dnGuestQuestion");

    public final StringPath dnName = createString("dnName");

    public final StringPath dnPasswd = createString("dnPasswd");

    public final StringPath dnSalt = createString("dnSalt");

    public final NumberPath<Integer> dnUid = createNumber("dnUid", Integer.class);

    public final EnumPath<jelog.server.main.Enum.OsEnum> dnUserAuthEnum = createEnum("dnUserAuthEnum", jelog.server.main.Enum.OsEnum.class);

    public final DateTimePath<java.time.LocalDateTime> inDate = createDateTime("inDate", java.time.LocalDateTime.class);

    public final ListPath<jelog.server.main.Model.Jwt.Authority, jelog.server.main.Model.Jwt.QAuthority> roles = this.<jelog.server.main.Model.Jwt.Authority, jelog.server.main.Model.Jwt.QAuthority>createList("roles", jelog.server.main.Model.Jwt.Authority.class, jelog.server.main.Model.Jwt.QAuthority.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> upDate = createDateTime("upDate", java.time.LocalDateTime.class);

    public QDN_UserModel(String variable) {
        super(DN_UserModel.class, forVariable(variable));
    }

    public QDN_UserModel(Path<? extends DN_UserModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDN_UserModel(PathMetadata metadata) {
        super(DN_UserModel.class, metadata);
    }

}

