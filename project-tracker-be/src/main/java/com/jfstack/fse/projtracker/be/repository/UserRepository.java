package com.jfstack.fse.projtracker.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jfstack.fse.projtracker.be.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmployeeId(Integer i);

}
