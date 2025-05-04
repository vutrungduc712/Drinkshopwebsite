package com.trungduc.drinkshop.repository;

import com.trungduc.drinkshop.entity.User;
import com.trungduc.drinkshop.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByFullNameContaining(String fullName, Pageable pageable);

    Page<User> findByCreatedAtAfter(Date date, Pageable pageable);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findAllByOrderByCreatedAtAsc(Pageable pageable);

    User findByEmail(String email);

    List<User> findAllByStatus(Status status);
}
