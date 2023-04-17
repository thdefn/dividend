package org.example.dividend.config;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    // 트라이는 서비스 내에서 하나만 유지되고 코드의 일관성 유지를 위해 빈으로 관리한다
    @Bean
    public Trie<String, String> trie(){
        return new PatriciaTrie<>();
    }
}
