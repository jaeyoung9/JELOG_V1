package jelog.server.main.Model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDN_Content is a Querydsl query type for DN_Content
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDN_Content extends EntityPathBase<DN_Content> {

    private static final long serialVersionUID = 684725368L;

    public static final QDN_Content dN_Content = new QDN_Content("dN_Content");

    public final ListPath<DN_Comment, QDN_Comment> comments = this.<DN_Comment, QDN_Comment>createList("comments", DN_Comment.class, QDN_Comment.class, PathInits.DIRECT2);

    public final StringPath contentBody = createString("contentBody");

    public final EnumPath<jelog.server.main.Enum.OsEnum> contentCategories = createEnum("contentCategories", jelog.server.main.Enum.OsEnum.class);

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final StringPath contentThumbnail = createString("contentThumbnail");

    public final StringPath contentTitle = createString("contentTitle");

    public final ListPath<DN_Files, QDN_Files> files = this.<DN_Files, QDN_Files>createList("files", DN_Files.class, QDN_Files.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> inDate = createDateTime("inDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> upDate = createDateTime("upDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QDN_Content(String variable) {
        super(DN_Content.class, forVariable(variable));
    }

    public QDN_Content(Path<? extends DN_Content> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDN_Content(PathMetadata metadata) {
        super(DN_Content.class, metadata);
    }

}

