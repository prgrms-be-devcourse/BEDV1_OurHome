package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.community.post.entity.ResidentialType;
import com.armand.ourhome.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByResidentialType(ResidentialType residentialType, Pageable pageable);
    Page<Post> findAllByUser(User user, Pageable pageable);
    Long countAllByUser(User user);


    Page<Post> findAllByPlaceType(PlaceType placeType, Pageable pageable);

}
