package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.entity.Comment;
import com.trungduc.drinkshop.repository.BlogRepository;
import com.trungduc.drinkshop.repository.CommentRepository;
import com.trungduc.drinkshop.service.CommentService;
import com.trungduc.drinkshop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    UserRepository userRepository;
    BlogRepository blogRepository;

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Comment saveComment(String content, Long blogId, Long userId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(userRepository.findById(userId).orElse(null));
        comment.setBlog(blogRepository.findById(blogId).orElse(null));
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findByBlogIdOrderByCreatedAtDesc(Long blogId) {
        return commentRepository.findByBlogIdOrderByCreatedAtDesc(blogId);
    }
}
