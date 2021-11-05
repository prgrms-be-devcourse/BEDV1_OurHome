package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.community.post.entity.ResidentialType;
import com.armand.ourhome.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByResidentialType(ResidentialType residentialType, Pageable pageable);

    Page<Post> findAllByUser(User user, Pageable pageable);

    @Query("select DISTINCT p from Content c inner join c.post p on p.id = c.post.id WHERE c.placeType = :placeType")
    Page<Post> findAllByPlaceType(@Param("placeType") PlaceType placeType, Pageable pageable);

    @Query("select DISTINCT p from Post p WHERE p.id in (select c.post.id FROM Tag t INNER JOIN t.content c ON t.content.contentId = c.contentId where t.name = :tag)")
    Page<Post> findAllByTag(@Param("tag") String tag , Pageable pageable);

    Long countAllByUser(User user);

    // 다수의 user id에 맞는 게시글을 페이징으로 찾는다
    @Query("SELECT p FROM Post p WHERE p.user.id IN(:userIdList) and p.id < :lastId order by p.id desc")
    Page<Post> findByUserIdListOrderByIdDesc(
            @Param("userIdList") List<Long> userIdList,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

}
