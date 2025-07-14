package com.Brew_Track.Cafe.Brew_Track.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.Brew_Track.Cafe.Brew_Track.POJO.User;

public interface UserDao extends JpaRepository<User, Integer>{
  
	User findByEmail(@Param("email") String email);
	
}
