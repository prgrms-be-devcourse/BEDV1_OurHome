package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.community.post.entity.ResidentialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByResidentialType(ResidentialType residentialType);


}
