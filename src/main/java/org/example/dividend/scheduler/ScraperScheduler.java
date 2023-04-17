package org.example.dividend.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dividend.model.Company;
import org.example.dividend.model.ScrapedResult;
import org.example.dividend.model.constants.CacheKey;
import org.example.dividend.persist.CompanyRepository;
import org.example.dividend.persist.DividendRepository;
import org.example.dividend.persist.entity.CompanyEntity;
import org.example.dividend.persist.entity.DividendEntity;
import org.example.dividend.scraper.Scraper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 코카콜라의 배당금 정보를 저장하고 언젠가 또 배당이 이뤄질텐데
 * 추가된 배당 정보를 스케쥴러를 통해 가져와서 데이터베이스에 저장해준다
 */
@Component
@Slf4j
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;

    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;

    // 일정 주기마다 수행
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true) // value에 해당하는 부분이 key의 프리픽스 / 레디스의 finance에 해당하는 값들은 다 비운다
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("scraping scheduler is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companyEntities = this.companyRepository.findAll();
        // 회사마다 배당금 정보를 새로스크래핑
        for (var company : companyEntities) {
            log.info("scraping for " + company.getName()); // 로그를 잘 남기는 것도 중요한 스킬이 될 수 있다
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
                    new Company(company.getTicker(), company.getName()));

            // 스크래핑한 배당금 정보 중 데이베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    // 디비든 모델을 디비든 엔티티로 맵핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    // 엘리먼트를 하나씩 디비든 레파지토리에 삽입 (존재하지 않는 경우)
                    .forEach(
                            e -> {
                                boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                                if(!exists){
                                    this.dividendRepository.save(e);
                                }
                            }
                    );

            // 연속해서 스크래핑 대상 사이트 서버에 부하가 가는 요청을 날리지 않도록 일시 정지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트가 되지 않았다면 현재 스레드에 인터럽트를 검
            }

        }
    }

//    @Scheduled(fixedDelay = 1000)
//    public void test1() throws InterruptedException {
//        Thread.sleep(10000); // 10초간 일시 정지
//        System.out.println("테스트1 "+ Thread.currentThread().getName());
//    }
//
//    // test1이 1번 수행되는동안 test2는 10번 수행되어야 함
//    @Scheduled(fixedDelay = 1000)
//    public void test2() throws InterruptedException {
//        System.out.println("테스트2 "+ Thread.currentThread().getName());
//    }
}
