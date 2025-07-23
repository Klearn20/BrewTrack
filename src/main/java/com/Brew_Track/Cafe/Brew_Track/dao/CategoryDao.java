package com.Brew_Track.Cafe.Brew_Track.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Brew_Track.Cafe.Brew_Track.POJO.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

    List<Category> getAllCategory();

}
