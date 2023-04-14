package org.example.dividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 실제 서비스 구현을 하다보면 몇십개 이상의 api를 뚫어줘야 하는 경우 있음
 * --> 네이밍 규칙, 클래스 구분, 함수 구분은 내 코드를 처음 보는 사람들도 이해할 수 있게 단순하게 하자
 * 한 클래스에 코드를 몰아넣기 보다는 기준을 가지고 코드를 찢어놓자
 */
@RestController
public class CompanyController {

    @GetMapping("/company/autocomplete")
    public ResponseEntity<?> autocomplete(){
        return null;
    }

    @GetMapping("/company")
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    @PostMapping("/company")
    public ResponseEntity<?> addCompany(){
        return null;
    }

    @DeleteMapping("/company")
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
