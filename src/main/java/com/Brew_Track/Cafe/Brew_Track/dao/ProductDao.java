package com.Brew_Track.Cafe.Brew_Track.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.Brew_Track.Cafe.Brew_Track.POJO.Product;
import com.Brew_Track.Cafe.Brew_Track.wrapper.ProductWrapper;

import jakarta.transaction.Transactional;


public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();
    
    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("status") String status,@Param("id") Integer id);

    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    ProductWrapper getProductById(@Param("id") Integer id);

}
