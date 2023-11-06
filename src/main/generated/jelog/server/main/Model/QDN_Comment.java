package jelog.server.main.Model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDN_Comment is a Querydsl query type for DN_Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDN_Comment extends EntityPathBase<DN_Comment> {

    private static final long serialVersionUID = 683593310L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDN_Comment dN_Comment = new QDN_Comment("dN_Comment");

    public final StringPath commentBody = createString("commentBody");

    public final NumberPath<Integer> commentId = createNumber("commentId", Integer.class);

    public final StringPath commentName = createString("commentName");

    public final StringPath commentPwd = createString("commentPwd");

    public final QDN_Content content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final StringPath dnUid = createString("dnUid");

    public final NumberPath<Integer> replyCommentId = createNumber("replyCommentId", Integer.class);

    public QDN_Comment(String variable) {
        this(DN_Comment.class, forVariable(variable), INITS);
    }

    public QDN_Comment(Path<? extends DN_Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDN_Comment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDN_Comment(PathMetadata metadata, PathInits inits) {
        this(DN_Comment.class, metadata, inits);
    }

    public QDN_Comment(Class<? extends DN_Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QDN_Content(forProperty("content")) : null;
    }

}

