package hu.bme.hit.vihima06.caffshop.backend.repository;

import hu.bme.hit.vihima06.caffshop.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
