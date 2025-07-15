package com.Brew_Track.Cafe.Brew_Track.dao;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.Brew_Track.Cafe.Brew_Track.POJO.User;
import com.Brew_Track.Cafe.Brew_Track.wrapper.UserWrapper; // ✅ needed for UserWrapper

public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmail(@Param("email") String email);

    List<UserWrapper> getAllUser();
}
