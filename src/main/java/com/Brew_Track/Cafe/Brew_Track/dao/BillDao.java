package com.Brew_Track.Cafe.Brew_Track.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.Brew_Track.Cafe.Brew_Track.POJO.Bill;

public interface BillDao extends JpaRepository <Bill, Integer> {

    List<Bill> getAllBills();

    List<Bill> getBillByUserName(@Param("username") String username);
   
}
