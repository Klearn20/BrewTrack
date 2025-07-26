package com.Brew_Track.Cafe.Brew_Track.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Brew_Track.Cafe.Brew_Track.POJO.Bill;

public interface BillDao extends JpaRepository <Bill, Integer> {
   
}
