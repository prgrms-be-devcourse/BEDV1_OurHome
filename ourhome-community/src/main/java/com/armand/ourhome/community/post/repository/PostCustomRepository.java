package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yunyun on 2021/11/03.
 */

@Repository
@Transactional
public interface PostCustomRepository {

    Page<Post> findAllByPlaceType(PlaceType placeType, Pageable pageable);

}
