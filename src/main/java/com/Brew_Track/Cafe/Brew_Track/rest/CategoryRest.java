package com.Brew_Track.Cafe.Brew_Track.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Brew_Track.Cafe.Brew_Track.POJO.Category;

@RequestMapping("/category")
public interface CategoryRest {

    @PostMapping("/add")
    ResponseEntity<String> addNewCategory(@RequestBody (required = true) Map<String, String> requestMap);

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required=false) String filterValue);
   
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    ResponseEntity<String> updateCategory(@RequestBody (required = true) Map<String, String> requestMap);




}
