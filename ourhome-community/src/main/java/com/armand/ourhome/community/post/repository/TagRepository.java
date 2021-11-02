package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findAllByName(String tagName, Pageable pageable);
}
