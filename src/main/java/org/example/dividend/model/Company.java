package org.example.dividend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Company 모델 클래스를 따로 정의해준 이유
 * -> 엔티티는 DB 테이블과 직접적으로 맵핑된 클래스
 * -> 엔티티를 서비스 내부에서 데이터를 주고받기 위한 용도로 쓴다면, 이 클래스의 원래 역할 범위를 벗어남
 * -> 중요한 건 코드가 자신의 역할에만 충실한 것
 * -> 사이드 이펙트, 유지보수 어려워짐
 * @Data 는 남용하지 말자, 예를 들어 외부에서 변수값을 임의로 변경하면 안되는 객체
 * 객체간 비교 연산이 중요한 로직에서는 equals 와 hashcode 메서드를 직접 정의하거나 자동 생성된 equals 가 내가 의도한대로 동작하는지 확인
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private String ticker;
    private String name;
}
