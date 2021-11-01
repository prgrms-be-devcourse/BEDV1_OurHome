package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yunyun on 2021/10/29.
 */

public interface TagRepository extends JpaRepository<Tag, Long> {
}
