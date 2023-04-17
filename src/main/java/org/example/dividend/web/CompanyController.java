package org.example.dividend.web;

import lombok.AllArgsConstructor;
import org.example.dividend.model.Company;
import org.example.dividend.persist.entity.CompanyEntity;
import org.example.dividend.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 실제 서비스 구현을 하다보면 몇십개 이상의 api를 뚫어줘야 하는 경우 있음
 * --> 네이밍 규칙, 클래스 구분, 함수 구분은 내 코드를 처음 보는 사람들도 이해할 수 있게 단순하게 하자
 * 한 클래스에 코드를 몰아넣기 보다는 기준을 가지고 코드를 찢어놓자
 */
@RestController
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/company/autocomplete")
    public ResponseEntity<?> autocomplete(){
        return null;
    }

    @GetMapping("/company")
    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<CompanyEntity> companyEntities = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyEntities);
    }

    @PostMapping("/company")
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        String ticker = request.getTicker().trim();

        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.save(ticker);

        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/company")
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
