package org.example.dividend.persist;

import org.example.dividend.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * companyId 와 date 가 복합 유니크키이기 때문에
 * 해당 유니크키로 데이터를 조회하면 일반 select보다 훨씬 더 빠르게 조회 가능함
 *
 * -> Dividend 테이블에 저장된 데이터가 많이졌을 때 인덱스를 걸지 않고 데이터를 조회하면 db 성능에 지장을 줄 수 있음
 * -> 인덱스를 걸면 성능을 향상시켜줄 수 있다
 */
@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
    List<DividendEntity> findAllByCompanyId(Long companyId);

    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);

    @Transactional
    void deleteAllByCompanyId(Long companyId);
}
