package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.domain.ReviewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final EntityManager em;

    @Override
    public List<Review> findAllByUser(Pageable pageable, User user) {
        String query = "select r from Review r " +
                "where r.user = :user and r.status = :status " +
                "order by r.id desc";

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        return em.createQuery(query, Review.class)
                .setParameter("user", user)
                .setParameter("status", ReviewStatus.POSTED)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<Review> findAllByUserOrderByHelpDesc(Pageable pageable, User user) {
        String query = "select r from Review r " +
                "where r.user = :user and r.status = :status " +
                "order by r.help desc";

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        return em.createQuery(query, Review.class)
                .setParameter("user", user)
                .setParameter("status", ReviewStatus.POSTED)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<Review> findAllByItem(Pageable pageable, Item item) {
        String query = "select r from Review r " +
                "where r.item = :item and r.status = :status " +
                "order by r.id desc";

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        return em.createQuery(query, Review.class)
                .setParameter("item", item)
                .setParameter("status", ReviewStatus.POSTED)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<Review> findAllByItemOrderByHelpDesc(Pageable pageable, Item item) {
        String query = "select r from Review r " +
                "where r.item = :item and r.status = :status " +
                "order by r.help desc";

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        return em.createQuery(query, Review.class)
                .setParameter("item", item)
                .setParameter("status", ReviewStatus.POSTED)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public boolean existsByUserAndAndItem(User user, Item item) {
        String query = "select r from Review r " +
                "where r.user = :user and r.item = :item and r.status = :status";

        try {
            em.createQuery(query, Review.class)
                    .setParameter("user", user)
                    .setParameter("item", item)
                    .setParameter("status", ReviewStatus.POSTED)
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
