package com.armand.ourhome.community.bookmark.repository;

import com.armand.ourhome.community.bookmark.entity.Bookmark;
import com.armand.ourhome.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Long countByUser(User user);
}
