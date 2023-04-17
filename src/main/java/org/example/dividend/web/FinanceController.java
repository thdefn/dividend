package org.example.dividend.web;

import lombok.AllArgsConstructor;
import org.example.dividend.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FinanceController {

    private final FinanceService financeService;
    @GetMapping("/finance/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        var result = this.financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/finance/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword){
        return null;
    }
}
