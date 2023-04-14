package org.example.dividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FinanceController {
    @GetMapping("/finance/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        return null;
    }

    @GetMapping("/finance/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword){
        return null;
    }
}
