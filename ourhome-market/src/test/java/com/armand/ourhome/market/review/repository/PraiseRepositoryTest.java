package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.market.review.domain.Praise;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(OurHomeDomainConfig.class)
class PraiseRepositoryTest {

    @Autowired
    private PraiseRepository praiseRepository;
    
    @Test 
    @DisplayName("Praise가 저장된다")
    public void testSave() throws Exception {
        //given 
        Praise praise = new Praise(1L, 1L);

        //when 
        praiseRepository.save(praise);

        //then
        long count = praiseRepository.count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Praise를 Review.id로 카운트 할 수 있다.")
    public void testCountByReviewId() throws Exception {
        //given
        Praise praise = new Praise(1L, 1L);
        Praise praise1 = new Praise(2L, 1L);
        Praise praise2 = new Praise(3L, 1L);
        Praise praise3 = new Praise(4L, 1L);

        //when
        praiseRepository.saveAll(List.of(praise1, praise2, praise3, praise));

        //then
        long count = praiseRepository.countByReviewId(praise.getReviewId());
        assertThat(count).isEqualTo(4);
    }

    @Test
    @DisplayName("Praise를 삭제할 수 있다.")
    public void testDelete() throws Exception {
        //given 
        Praise praise = new Praise(1L, 1L);
        praiseRepository.save(praise);

        //when 
        praiseRepository.deleteById(praise.getId());
        //then
        long count = praiseRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("User.id와 Review.id로 Praise를 제거할 수 있다.")
    public void testDeleteByUserIdAndReviewId() throws Exception {
        //given
        Praise praise1 = new Praise(1L, 2L);
        Praise praise2 = new Praise(1L, 3L);

        praiseRepository.saveAll(List.of(praise1, praise2));

        //when
        praiseRepository.deletePraiseByUserIdAndReviewId(praise1.getUserId(), praise2.getReviewId());

        //then
        long count = praiseRepository.count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("User.id와 Review.id로 Praise가 존재하는 지 확인할 수 있다.")
    public void testExistByUserIdAndReviewId() throws Exception {
        //given
        Praise praise1 = new Praise(1L, 2L);
        Praise praise2 = new Praise(1L, 3L);

        praiseRepository.saveAll(List.of(praise1, praise2));

        //when
        boolean exist = praiseRepository.existsByUserIdAndReviewId(praise1.getUserId(), praise1.getReviewId());

        //then
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("User.id와 Review.id로 Praise가 존재하는 지 확인할 수 있다.")
    public void testExistByUserIdAndReviewIdNotExist() throws Exception {
        //given
        Praise praise1 = new Praise(1L, 2L);
        Praise praise2 = new Praise(1L, 3L);

        praiseRepository.saveAll(List.of(praise1, praise2));

        //when
        boolean exist = praiseRepository.existsByUserIdAndReviewId(praise2.getUserId(), praise2.getReviewId() + 100);

        //then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("User.id와 Review.id로 Praise.id로 조회할 수 있다.")
    public void testFindByIdAndUserIdAndReviewId() throws Exception {
        //given
        Praise praise1 = new Praise(1L, 2L);
        Praise praise2 = new Praise(1L, 3L);

        praiseRepository.saveAll(List.of(praise1, praise2));

        //when
        Optional<Praise> actual1 = praiseRepository.findByIdAndUserIdAndReviewId(praise1.getId(), praise1.getUserId(), praise1.getReviewId());
        Optional<Praise> actual2 = praiseRepository.findByIdAndUserIdAndReviewId(praise1.getId(), praise1.getUserId(), praise1.getReviewId() + 1000);

        //then
        assertThat(actual1).isPresent();
        assertThat(actual2).isEmpty();
    }
}