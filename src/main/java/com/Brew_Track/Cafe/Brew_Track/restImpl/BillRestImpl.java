package com.Brew_Track.Cafe.Brew_Track.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.Brew_Track.Cafe.Brew_Track.constents.CafeConstants;
import com.Brew_Track.Cafe.Brew_Track.rest.BillRest;
import com.Brew_Track.Cafe.Brew_Track.service.BillService;
import com.Brew_Track.Cafe.Brew_Track.utils.CafeUtils;

@RestController
public class BillRestImpl implements BillRest {
    
    @Autowired
    private BillService billService;
    
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity generateReport(Map requestMap) {
        try {
            return billService.generateReport(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
   
   
  
}
