package com.armand.ourhome.community.post.repository;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.community.post.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by yunyun on 2021/11/03.
 */

@Repository
@Transactional
public class PostCustomRepositoryImpl implements PostCustomRepository{

    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Post> findAllByPlaceType(PlaceType placeType, Pageable pageable) {
        var tagTypedQuery = entityManager
                .createQuery("SELECT t FROM Tag AS t WHERE t.name= :placeType", Tag.class)
                .setParameter("placeType", placeType)
                .setFirstResult((pageable.getPageNumber() -1) * pageable.getPageSize() );


        List<Long> postIds = tagTypedQuery
                .getResultList()
                .stream()
                .map(v ->
                        v.getContent()
                                .getPost()
                                .getPostId())
                .distinct()
                .toList();
        entityManager
                .createQuery("SELECT p FROM Post As p WHERE p.id in(:postIds)")
                .setParameter("postIds", postIds)
                .getResultList();


        return null;
    }
}
