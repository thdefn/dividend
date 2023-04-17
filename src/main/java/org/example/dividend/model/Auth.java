package org.example.dividend.model;

import lombok.Data;
import org.example.dividend.persist.entity.MemberEntity;

import java.util.List;

public class Auth {
    @Data
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        private String role;

        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .role(this.role)
                    .build();
        }
    }
}
