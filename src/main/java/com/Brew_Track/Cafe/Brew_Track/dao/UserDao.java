package com.Brew_Track.Cafe.Brew_Track.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Brew_Track.Cafe.Brew_Track.POJO.User;
import com.Brew_Track.Cafe.Brew_Track.wrapper.UserWrapper;

import jakarta.transaction.Transactional;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmail(@Param("email") String email);

    @Query("SELECT new com.Brew_Track.Cafe.Brew_Track.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) FROM User u WHERE u.role = 'user'")
    List<UserWrapper> getAllUser();

    List<String> getAllAdmin();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    Integer updateUser(@Param("status") String status, @Param("id") Integer id);}
