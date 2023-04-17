package org.example.dividend.persist.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.dividend.model.Dividend;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 유니크 키는 일종의 인덱스이자 제약조건
 * 중복 데이터 저장을 방지하는 제약 조건
 * 단일 컬럼 뿐 아니라 복합 컬럼을 지정할 수도 있음
 *
 * -> 유니크 키를 설정하면 중복된 유니크 키를 가진 데이터를 저장하면 익셉션 발생함
 * -> 디비든 데이터 저장할 때 중복된 데이터 키가 있다면 어떻게 할건지 설정 가능 ex> insert ignore, on duplicate key update
 *
 */
@Entity(name = "DIVIDEND")
@Getter
@ToString
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"companyId", "date"}
                )
        }
)
public class DividendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;

    public DividendEntity(Long companyId, Dividend dividend) {
        this.companyId = companyId;
        this.date = dividend.getDate();
        this.dividend = dividend.getDividend();
    }
}
