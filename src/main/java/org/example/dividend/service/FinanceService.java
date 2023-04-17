package org.example.dividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dividend.exception.impl.NoCompanyException;
import org.example.dividend.model.Company;
import org.example.dividend.model.Dividend;
import org.example.dividend.model.ScrapedResult;
import org.example.dividend.model.constants.CacheKey;
import org.example.dividend.persist.CompanyRepository;
import org.example.dividend.persist.DividendRepository;
import org.example.dividend.persist.entity.CompanyEntity;
import org.example.dividend.persist.entity.DividendEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;

    private final DividendRepository dividendRepository;

    // 1. 요청이 자주 들어오는가 ? 주식 정보 검색은 특정 회사에 대한 요청이 몰린다 -> 특정 회사의 배당금 데이터를 캐싱해두면 한번 요청이 온 데이터는 이후 데이터베이스에서 서칭하지 않아도됨
    // 2. 자주 변경되는 데이터인가 ? 데이터 변경이 잦은 데이터라면 캐시에 있는 데이터도 업데이트하거나 삭제해줘야함 -> but 배당금 데이터는 과거 금액 정보가 바뀌지 않음, 배당 정보 추가되었을 때 업데이트만 하면 됨
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE) // 메소드 파라미터 / 레디스 서버의 key value와는 의미가 다르다
    public ScrapedResult getDividendByCompanyName(String companyName) {
        // 1. 회사명을 기준으로 회사 정보를 조회
        log.info("searchCompany : "+companyName);
        // 래핑타입 옵셔널은 1. nullpointexception을 방지 2. 값이 없는 경우에 대한 처리가 더 깔끔함
        CompanyEntity companyEntity = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException()); // 값이 없으면 익셉션 값이 있다면 옵셔널을 벗겨 알맹이를 뱉어냄

        // 2. 조회된 회사의 아이디로 배당금을 조회
        List<DividendEntity> dividendEntities =
                this.dividendRepository.findAllByCompanyId(companyEntity.getId());

        // 3. 조회된 회사 정보와 배당금 정보를 조합해서 결과로 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(
                new Company(companyEntity.getTicker(), companyEntity.getName()),
                dividends
        );
    }
}
