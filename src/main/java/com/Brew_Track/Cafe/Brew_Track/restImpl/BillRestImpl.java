package com.Brew_Track.Cafe.Brew_Track.restImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.Brew_Track.Cafe.Brew_Track.POJO.Bill;
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

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<List<Bill>> getBills() {
        try {
         return billService.getBills();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return billService.getPdf(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
           return billService.deleteBill(id); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
   
   
  
}
