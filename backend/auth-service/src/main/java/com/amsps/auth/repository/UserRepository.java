package com.amsps.auth.repository;

import com.amsps.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByUname(String uname);


    boolean existsByUname(String uname);
}
