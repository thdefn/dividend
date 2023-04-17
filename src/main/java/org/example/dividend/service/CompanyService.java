package org.example.dividend.service;

import lombok.AllArgsConstructor;
import org.example.dividend.model.Company;
import org.example.dividend.model.ScrapedResult;
import org.example.dividend.persist.CompanyRepository;
import org.example.dividend.persist.DividendRepository;
import org.example.dividend.persist.entity.CompanyEntity;
import org.example.dividend.persist.entity.DividendEntity;
import org.example.dividend.scraper.Scraper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scraper yahooFinanceScraper;

    private final CompanyRepository companyRepository;

    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        boolean exist = this.companyRepository.existsByTicker(ticker);
        if (exist) {
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker) {
        // ticker를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);

        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }
        // 해당 회사가 존재할 경우 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);
        // 스크래핑 결과

        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));

        List<DividendEntity> dividendEntities =
                scrapedResult.getDividends().stream()
                        .map(e -> new DividendEntity(companyEntity.getId(), e)) // 컬렉션의 엘리먼트들을 다른 값으로 맵핑해야 할때 사용
                        .collect(Collectors.toList()); // 결과값을 리스트 타입으로 반환

        this.dividendRepository.saveAll(dividendEntities);
        return company;
    }
}

