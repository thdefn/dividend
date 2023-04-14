package org.example.dividend.scraper;

import org.example.dividend.model.Company;
import org.example.dividend.model.Dividend;
import org.example.dividend.model.ScrapedResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceScraper {
    // 멤버 변수로 빼주면 1. 코드 유지 보수 하기 좋음 2. 스택 낭비 x
    // String.format을 이용해 유동적으로 값을 바꿔주기 위해 포맷 인수 사용
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";

    public ScrapedResult scrap(Company company){
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);
        try {
            long start = 0;
            long end = 0;
            String url = String.format(STATISTICS_URL, company.getTicker(), start, end);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();
            Elements parsingDivs = document.getElementsByAttributeValue("data-test","historical-prices"); // 이 속성을 가진 element가 한 개가 아닐 수 있음
            Element tableEle = parsingDivs.get(0); // 테이블 전체

            Element tbody = tableEle.children().get(1);
            List<Dividend> dividends = new ArrayList<>();
            for(Element e : tbody.children()){
                String txt = e.text();
                if(!txt.endsWith("Dividend")){
                    continue;
                }
                String[] splits = txt.split(" ");
                String month = splits[0];
                int day = Integer.valueOf(splits[1].replace(",",""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                //System.out.println(year + "/" + month + "/" + day + "/" + " -> " + dividend);
            }
            scrapResult.setDividends(dividends);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
