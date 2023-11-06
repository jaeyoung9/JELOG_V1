package jelog.server.main.Model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDN_Files is a Querydsl query type for DN_Files
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDN_Files extends EntityPathBase<DN_Files> {

    private static final long serialVersionUID = 441290294L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDN_Files dN_Files = new QDN_Files("dN_Files");

    public final QDN_Content content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final NumberPath<Integer> fileId = createNumber("fileId", Integer.class);

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final StringPath mediaType = createString("mediaType");

    public final ArrayPath<byte[], Byte> resultFile = createArray("resultFile", byte[].class);

    public QDN_Files(String variable) {
        this(DN_Files.class, forVariable(variable), INITS);
    }

    public QDN_Files(Path<? extends DN_Files> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDN_Files(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDN_Files(PathMetadata metadata, PathInits inits) {
        this(DN_Files.class, metadata, inits);
    }

    public QDN_Files(Class<? extends DN_Files> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QDN_Content(forProperty("content")) : null;
    }

}

