package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.Content;
import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findAllByPlaceType(PlaceType placeType, Pageable pageable);

}
