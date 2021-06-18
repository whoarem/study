package com.example.study.repository;

import com.example.study.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  // select * from user where account = ?
  Optional<User> findByAccount(String account);

  Optional<User> findByEmail(String email);

  Optional<User> findByAccountAndEmail(String account, String email);
}