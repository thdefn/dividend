package org.example.dividend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DividendApplication {

    public static void main(String[] args) {
        //SpringApplication.run(DividendApplication.class, args);
        Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/O/history?period1=99100800&period2=1681430400&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
        try {
            Document document = connection.get();
            Elements elements = document.getElementsByAttributeValue("data-test","historical-prices"); // 이 속성을 가진 element가 한 개가 아닐 수 있음
            Element element = elements.get(0); // 테이블 전체

            Element tbody = element.children().get(1);
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

                System.out.println(year + "/" + month + "/" + day + "/" + " -> " + dividend);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
