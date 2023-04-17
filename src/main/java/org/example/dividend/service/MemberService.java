package org.example.dividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dividend.model.Auth;
import org.example.dividend.persist.MemberRepository;
import org.example.dividend.persist.entity.MemberEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override // 스프링시큐리티에서 지원하는 기능을 사용하기 위해 이 메서드가 구현되어 잇어야 한다
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // MemberEntity는 UserDetails를 상속한 코드이기 때문에 바로 리턴이 가능함
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("could not find user -> " + username));
    }

    public MemberEntity register(Auth.SignUp member) {
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());

        return result;
    }

    // 로그인 시 검증
    public MemberEntity authenticate(Auth.SignIn member) {
        return null;
    }
}
