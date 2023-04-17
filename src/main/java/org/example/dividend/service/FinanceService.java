package org.example.dividend.service;

import lombok.AllArgsConstructor;
import org.example.dividend.model.Company;
import org.example.dividend.model.Dividend;
import org.example.dividend.model.ScrapedResult;
import org.example.dividend.persist.CompanyRepository;
import org.example.dividend.persist.DividendRepository;
import org.example.dividend.persist.entity.CompanyEntity;
import org.example.dividend.persist.entity.DividendEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;

    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {
        // 1. 회사명을 기준으로 회사 정보를 조회

        // 래핑타입 옵셔널은 1. nullpointexception을 방지 2. 값이 없는 경우에 대한 처리가 더 깔끔함
        CompanyEntity companyEntity = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다.")); // 값이 없으면 익셉션 값이 있다면 옵셔널을 벗겨 알맹이를 뱉어냄

        // 2. 조회된 회사의 아이디로 배당금을 조회
        List<DividendEntity> dividendEntities =
                this.dividendRepository.findAllByCompanyId(companyEntity.getId());

        // 3. 조회된 회사 정보와 배당금 정보를 조합해서 결과로 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(e ->
                        Dividend.builder().date(e.getDate())
                                .dividend(e.getDividend())
                                .build())
                .collect(Collectors.toList());

        return new ScrapedResult(
                Company.builder()
                        .ticker(companyEntity.getTicker())
                        .name(companyEntity.getName())
                        .build(),
                dividends
        );
    }
}
