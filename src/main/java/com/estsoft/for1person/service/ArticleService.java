package com.estsoft.for1person.service;

import com.estsoft.for1person.dto.*;
import com.estsoft.for1person.entity.*;
import com.estsoft.for1person.exception.NotFoundException;
import com.estsoft.for1person.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;
    private ReviewRepository reviewRepository;
    private VipRepository vipRepository;
    private UserRepository userRepository;
    private ArticleLikeRepository articleLikeRepository;
    private ReviewLikeRepository reviewLikeRepository;
    private VipLikeRepository vipLikeRepository;

    public ArticleService(ArticleRepository articleRepository, ReviewRepository reviewRepository, VipRepository vipRepository, UserRepository userRepository, ArticleLikeRepository articleLikeRepository, ReviewLikeRepository reviewLikeRepository, VipLikeRepository vipLikeRepository) {
        this.articleRepository = articleRepository;
        this.reviewRepository = reviewRepository;
        this.vipRepository = vipRepository;
        this.userRepository = userRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.reviewLikeRepository = reviewLikeRepository;
        this.vipLikeRepository = vipLikeRepository;
    }

    //============================================================================================================
    // 게시판 모든 목록 리스트로 반환(일반, 리뷰, VIP)
    public List<Article> getAllArticle() {
        return articleRepository.findAll();
    }
    public List<Review> getAllReview() {
        return reviewRepository.findAll();
    }
    public List<Vip> getAllVip() {
        return vipRepository.findAll();
    }

    //============================================================================================================
    // 게시판 모든 목록 페이지로 반환(일반, 리뷰, VIP)
    public Page<Article> getAllArticlesPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAll(pageable);
    }
    public Page<Review> getAllReviewPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findAll(pageable);
    }
    public Page<Vip> getAllVipPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vipRepository.findAll(pageable);
    }

    //============================================================================================================
    // 게시글 1개 반환(일반, 리뷰, VIP)
    public Article getArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow();
    }
    public Review getReview(Long articleId) {
        return reviewRepository.findById(articleId).orElseThrow();
    }
    public Vip getVip(Long articleId) {
        return vipRepository.findById(articleId).orElseThrow();
    }

    //============================================================================================================
    // 게시글 생성(일반, 리뷰, VIP)
    public void createArticle(String userId, AddArticleRequest request) {
        User user = userRepository.findByUserId(userId).get();
        Article article = request.toEntity(user);
        articleRepository.save(article);
    }
    public void createReview(String userId, AddReviewRequest request) {
        User user = userRepository.findByUserId(userId).get();
        Review article = request.toEntity(user);
        reviewRepository.save(article);
    }
    public void createVip(String userId, AddVipRequest request) {
        User user = userRepository.findByUserId(userId).get();
        Vip article = request.toEntity(user);
        vipRepository.save(article);
    }
    //============================================================================================================
    // 게시글 수정(일반, 리뷰, VIP)
    @Transactional
    public Article updateArticle(String userId, Long articleId, AddArticleRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundException::new);
        //userId 맞는지 확인
        //맞으면 dto 내용을 article에 넣어서 변경

        if(userId.equals(article.getUser().getUserId()))
        {
            article.update(request.getTitle(),request.getContent(),request.getAnonymous());
        }
        // 후 저장

        //articleRepository.save(article);
        return article;
    }

    @Transactional
    public Review updateReview(String userId, Long reviewId, AddReviewRequest request) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        //userId 맞는지 확인
        //맞으면 dto 내용을 review에 넣어서 변경

        if(userId.equals(review.getUser().getUserId()))
        {
            review.update(request.getTitle(),request.getContent(),request.getAnonymous(), request.getScore());
        }

        // 후 저장
        //reviewRepository.save(review);

        return review;
    }

    @Transactional
    public Vip updateVip(String userId, Long vipId, AddArticleRequest request) { // Changed from Article to Vip, and parameter name from articleId to vipId
        Vip vip = vipRepository.findById(vipId).orElseThrow(NotFoundException::new); // Changed from Article to Vip
        //userId 맞는지 확인
        //맞으면 dto 내용을 vip에 넣어서 변경


        if(userId.equals(vip.getUser().getUserId()))
        {
            vip.update(request.getTitle(),request.getContent(),request.getAnonymous());
        }

        // 후 저장
        //vipRepository.save(vip); // Changed from article to vip
        return vip;
    }

    //============================================================================================================
    // 게시글 삭제(일반, 리뷰, VIP)
    public void deleteArticle(String userId, Long articleId) {
        //계정이 있는지 확인
        Article article = articleRepository.findById(articleId).get();
        //유저 아이디랑 일치하는지 확인

        if(article.getUser().getUserId().equals(userId))
        {
            articleRepository.deleteById(articleId);
        }

        //있으면 삭제 없으면 에러 반환
    }

    public void deleteReview(String userId, Long reviewId) {
        //계정이 있는지 확인
        Review review = reviewRepository.findById(reviewId).get();
        //유저 아이디랑 일치하는지 확인
        if(review.getUser().getUserId().equals(userId))
        {
            reviewRepository.deleteById(reviewId);
        }
        //있으면 삭제 없으면 에러 반환
    }

    public void deleteVip(String userId, Long vipId) { // Changed from Article to Vip, and parameter name from articleId to vipId
        //계정이 있는지 확인
        Vip vip = vipRepository.findById(vipId).get();
        //유저 아이디랑 일치하는지 확인
        if(vip.getUser().getUserId().equals(userId))
        {
            vipRepository.deleteById(vipId);
        }
        //있으면 삭제 없으면 에러 반환
    }



    //============================================================================================================
    // 게시글 1개 가져오기(일반, 리뷰, VIP) (중복 코드)
//    public Article findArticleId(Long articleId) {
//        return articleRepository.findById(articleId).orElseThrow(() ->
//                new RuntimeException("Article not found with id: " + articleId));
//    }
//
//    public Review findReviewId(Long reviewId) {
//        return reviewRepository.findById(reviewId).orElseThrow(() ->
//                new RuntimeException("Review not found with id: " + reviewId));
//    }
//
//    public Vip findVipId(Long vipId) {
//        return vipRepository.findById(vipId).orElseThrow(() ->
//                new RuntimeException("Article not found with id: " + vipId));
//    }
    
    
    //============================================================================================================
    // 게시글 추천 기능
    // (추천은 게시글당 동일 유저가 최대 1개까지만 가능)
    // (추천을 안 눌렀을 때 누르면 추천이 되고 추천이 눌러져있을 때 누르면 추천이 해제됨)
    
    @Transactional
    public void likeArticle(String userId, Long articleId) {
        // 만약 articleLike 정보가 있으면 좋아요 한 상태임
        User user = userRepository.findByUserId(userId).orElseThrow(NotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundException::new);

        if(articleLikeRepository.findByArticleAndUser(article, user).isEmpty()){

            ArticleLike articleLike = ArticleLike.builder()
                    .user(user)
                    .article(article)
                    .build();

            articleLikeRepository.save(articleLike);

        }
        // 좋아요 한 정보가 있다면
        else {
            ArticleLike tmp = articleLikeRepository.findByArticleAndUser(article, user).get();
            articleLikeRepository.delete(tmp);
        }
    }

    @Transactional
    public void likeReview(String userId, Long reviewId) {
        // 만약 articleLike 정보가 있으면 좋아요 한 상태임
        User user = userRepository.findByUserId(userId).orElseThrow(NotFoundException::new);
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);

        if(reviewLikeRepository.findByReviewAndUser(review, user).isEmpty()){

            ReviewLike reviewLike = ReviewLike.builder()
                    .user(user)
                    .review(review)
                    .build();

            reviewLikeRepository.save(reviewLike);


        }
        // 좋아요 한 정보가 있다면
        else {
            ReviewLike tmp = reviewLikeRepository.findByReviewAndUser(review, user).get();
            reviewLikeRepository.delete(tmp);

        }
    }

    @Transactional
    public void likeVip(String userId, Long vipId) {
        // 만약 articleLike 정보가 있으면 좋아요 한 상태임
        User user = userRepository.findByUserId(userId).orElseThrow(NotFoundException::new);
        Vip vip = vipRepository.findById(vipId).orElseThrow(NotFoundException::new);

        if(vipLikeRepository.findByVipAndUser(vip, user).isEmpty()){

            VipLike vipLike = VipLike.builder()
                    .user(user)
                    .vip(vip)
                    .build();

            vipLikeRepository.save(vipLike);


        }
        // 좋아요 한 정보가 있다면
        else {
            VipLike tmp = vipLikeRepository.findByVipAndUser(vip, user).get();
            vipLikeRepository.delete(tmp);

        }
    }

    //============================================================================================================
    // 특정 게시글 추천 수 가져오는 기능 (일반, 리뷰, Vip)

    @Transactional
    public Optional<Integer> getLikeArticle(Long articleId)
    {
        Article article = articleRepository.findById(articleId).get();

        return articleLikeRepository.countArticleLikeByArticle(article);
    }

    @Transactional
    public Optional<Integer> getLikeReview(Long reviewId)
    {
        Review review = reviewRepository.findById(reviewId).get();

        return reviewLikeRepository.countArticleLikeByReview(review);
    }


    @Transactional
    public Optional<Integer> getLikeVip(Long vipId)
    {
        Vip vip = vipRepository.findById(vipId).get();

        return vipLikeRepository.countArticleLikeByVip(vip);
    }


    //============================================================================================================
    // 모든 게시글 추천 수를 리스트로 가져오는 기능 (일반, 리뷰, VIP)

    @Transactional
    public List<CommonViewResponse> getAllLikeArticle(List<CommonViewResponse> articles)
    {

        for(CommonViewResponse article : articles)
        {
            Article tmp = articleRepository.findById(article.getArticleId()).get();
            Integer like = articleLikeRepository.countArticleLikeByArticle(tmp).get();
            article.setLike(like);
        }

       return articles;
    }


    @Transactional
    public List<ReviewViewResponse> getAllLikeReview(List<ReviewViewResponse> articles)
    {

        for(ReviewViewResponse article : articles)
        {
            Review tmp = reviewRepository.findById(article.getReviewId()).get();
            Integer like = reviewLikeRepository.countArticleLikeByReview(tmp).get();
            article.setLike(like);
        }

        return articles;
    }


    @Transactional
    public List<VipViewResponse> getAllLikeVip(List<VipViewResponse> articles)
    {

        for(VipViewResponse article : articles)
        {
            Vip tmp = vipRepository.findById(article.getVipId()).get();
            Integer like = vipLikeRepository.countArticleLikeByVip(tmp).get();
            article.setLike(like);
        }

        return articles;
    }

    //============================================================================================================
    // 게시글에 있는 추천을 모두 지우는 기능 (일반, 리뷰, VIP)

    public void deleteLikeAllArticle(Long articleId)
    {
        Article article = articleRepository.findById(articleId).get();
        articleLikeRepository.deleteAllByArticle(article);
    }

    public void deleteLikeAllReview(Long reviewId)
    {
        Review review = reviewRepository.findById(reviewId).get();
        reviewLikeRepository.deleteAllByReview(review);
    }

    public void deleteLikeAllVip(Long vipId)
    {
        Vip vip = vipRepository.findById(vipId).get();
        vipLikeRepository.deleteAllByVip(vip);
    }


    //============================================================================================================
    // 조회수 자동으로 증가시키는 기능 (일반, 리뷰, VIP)

    @Transactional
    public void increaseCommonView(Long articleId)
    {
        Article article = articleRepository.findById(articleId).get();

        article.setViews(article.getViews()+1);
    }

    @Transactional
    public void increaseReviewView(Long reviewId)
    {
        Review review = reviewRepository.findById(reviewId).get();

        review.setViews(review.getViews()+1);
    }


    @Transactional
    public void increaseVipView(Long vipId)
    {
        Vip vip = vipRepository.findById(vipId).get();

        vip.setViews(vip.getViews()+1);
    }


    //============================================================================================================
    // 유저가 작성한 모든 게시물 수를 반환하는 기능

    public List<UserViewResponse> getAllArticleByUserId(List<UserViewResponse> users) {

        for(UserViewResponse user : users)
        {
            User tmp = userRepository.findByUserId(user.getUserId()).get();
            Integer count = articleRepository.countArticleByUser(tmp) + reviewRepository.countReviewByUser(tmp) + vipRepository.countVipByUser(tmp);
            user.setArticleCount(count);
        }

        return users;
    }

    //============================================================================================================
    // 리스트를 페이지로 변환하는 함수
    public <T> Page<T> toPageFromList(List<T> result, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()), result.size());

        Page<T> pageResult = new PageImpl<>(result.subList(start,end), pageRequest, result.size());

        return pageResult;
    }
    //============================================================================================================

}