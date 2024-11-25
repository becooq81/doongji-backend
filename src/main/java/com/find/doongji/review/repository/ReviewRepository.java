package com.find.doongji.review.repository;

import com.find.doongji.review.payload.request.ReviewCreateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewRepository {

    /**
     * @return review 테이블 존재 여부 확인
     */
    int checkIfTableExists();

    /**
     * review 레코드 한 번에 여러 개씩 추가
     * @param requests
     */
    void bulkInsertReview(@Param("list") List<ReviewCreateRequest> requests);

    /**
     * review 추가
     * @param request
     */
    void insertReview(ReviewCreateRequest request);

    /**
     * @param aptSeq 아파트 시퀀스
     * @return 아파트 시퀀스에 해당하는 리뷰 조회
     */
    List<String> selectReviewsByAptSeq(@Param("aptSeq") String aptSeq);
}
