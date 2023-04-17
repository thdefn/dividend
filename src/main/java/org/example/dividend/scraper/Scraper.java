package org.example.dividend.scraper;

import org.example.dividend.model.Company;
import org.example.dividend.model.ScrapedResult;

/**
 * 코드의 확장성과 재사용성을 위해 인터페이스 생성
 */
public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
