package com.Brew_Track.Cafe.Brew_Track.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;


public interface BillService {
   
   ResponseEntity<String> generateReport(Map<String, Object> requestMap);
   
}
