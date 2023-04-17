package org.example.dividend.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.example.dividend.exception.impl.NoCompanyException;
import org.example.dividend.model.Company;
import org.example.dividend.model.ScrapedResult;
import org.example.dividend.persist.CompanyRepository;
import org.example.dividend.persist.DividendRepository;
import org.example.dividend.persist.entity.CompanyEntity;
import org.example.dividend.persist.entity.DividendEntity;
import org.example.dividend.scraper.Scraper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CompanyService는 스프링 부트 빈이기 때문에 싱글톤으로 관리됨
 * 프로그램 전체에서 하나의 인스턴스만 생성되어야 할 때
 */
@Service
@AllArgsConstructor
public class CompanyService {

    private final Trie trie; // 각각 다른 인스턴스가 생성되고 그래서 값이 다른 것을 염두하지 않아도 된다

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

    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable); // fe에서 한번에 보여줄 수 있는 데이터의 개수는 한계가 있다
    }

    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null); // 우리는 value 의 해당하는 값을 넣을 필요가 없다
    }

    public List<String> autocomplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream().collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    /**
     * 장점 : 구현이 더 간단하다
     * 단점 : 데이터를 찾는 연산이 전부 디비에서 이뤄지기 때문에 디비에 더 많은 부하가 감
     */
    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities =
                this.companyRepository.findByNameStartsWithIgnoreCase(keyword, limit);

        return companyEntities.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    public String deleteCompany(String ticker) {
        var company = this.companyRepository.findByTicker(ticker)
                .orElseThrow(() -> new NoCompanyException());

        this.dividendRepository.deleteAllByCompanyId(company.getId());
        this.companyRepository.delete(company);

        this.deleteAutocompleteKeyword(company.getName());

        return company.getName();
    }
}

