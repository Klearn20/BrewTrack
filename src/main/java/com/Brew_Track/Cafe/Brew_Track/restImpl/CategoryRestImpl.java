package com.Brew_Track.Cafe.Brew_Track.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Brew_Track.Cafe.Brew_Track.POJO.Category;
import com.Brew_Track.Cafe.Brew_Track.constents.CafeConstants;
import com.Brew_Track.Cafe.Brew_Track.rest.CategoryRest;
import com.Brew_Track.Cafe.Brew_Track.service.CategoryService;
import com.Brew_Track.Cafe.Brew_Track.utils.CafeUtils;

 @RestController
public class CategoryRestImpl implements CategoryRest {

   @Autowired
   CategoryService categoryService;

    // Implement the method from CategoryRest interface
    @Override

   @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<String> addNewCategory(@RequestBody Map<String, String> requestMap) {
        try {
            // Call the service method to add a new category
            return categoryService.addNewCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
   @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
   @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            return categoryService.updateCategory(requestMap);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

   

}
