package com.Brew_Track.Cafe.Brew_Track.restImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.Brew_Track.Cafe.Brew_Track.rest.DashboardRest;
import com.Brew_Track.Cafe.Brew_Track.service.DashboardService;

@RestController
public class DashboardRestImpl implements DashboardRest	 {
    
    @Autowired 
    DashboardService dashboardService;


    @Override
    public ResponseEntity getCount() {
     return dashboardService.getCount();        
    }
      
}
