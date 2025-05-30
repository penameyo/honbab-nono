package com.estsoft.for1person.repository;

import com.estsoft.for1person.entity.CommentReview;
import com.estsoft.for1person.entity.Review;
import com.estsoft.for1person.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Integer countReviewByUser(User user);

    List<Review> findAllByTitle(String searchKey);

    List<Review> findAllByUser(User findUser);
}