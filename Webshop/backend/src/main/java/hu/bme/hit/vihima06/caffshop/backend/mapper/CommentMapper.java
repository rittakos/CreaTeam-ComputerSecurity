package hu.bme.hit.vihima06.caffshop.backend.mapper;

import hu.bme.hit.vihima06.caffshop.backend.model.Comment;
import hu.bme.hit.vihima06.caffshop.backend.models.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentResponse commentToCommentResponse(Comment comment);
}
