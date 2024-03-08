package com.sqy.urms.persistence.repository;

import com.sqy.urms.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByLoginAndPassword(String login, String password);
    User findByLogin(String login);
}
