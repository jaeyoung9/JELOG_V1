package jelog.server.main.Model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDN_LogModel is a Querydsl query type for DN_LogModel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDN_LogModel extends EntityPathBase<DN_LogModel> {

    private static final long serialVersionUID = -1979059674L;

    public static final QDN_LogModel dN_LogModel = new QDN_LogModel("dN_LogModel");

    public final NumberPath<Integer> dnLid = createNumber("dnLid", Integer.class);

    public final EnumPath<jelog.server.main.Enum.OsEnum> dnOs = createEnum("dnOs", jelog.server.main.Enum.OsEnum.class);

    public final StringPath dseSignID = createString("dseSignID");

    public final DateTimePath<java.time.LocalDateTime> inDate = createDateTime("inDate", java.time.LocalDateTime.class);

    public final StringPath queryString = createString("queryString");

    public QDN_LogModel(String variable) {
        super(DN_LogModel.class, forVariable(variable));
    }

    public QDN_LogModel(Path<? extends DN_LogModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDN_LogModel(PathMetadata metadata) {
        super(DN_LogModel.class, metadata);
    }

}

