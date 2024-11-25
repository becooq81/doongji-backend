package com.find.doongji.review.repository;

import com.find.doongji.review.payload.request.ReviewEntity;
import com.find.doongji.review.payload.response.ReviewResponse;
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
     * @param reviewEntities
     */
    void bulkInsertReview(@Param("list") List<ReviewEntity> reviewEntities);

    /**
     * review 추가
     * @param reviewEntity
     */
    void insertReview(ReviewEntity reviewEntity);

    /**
     * @param aptSeq 아파트 시퀀스
     * @return 아파트 시퀀스에 해당하는 리뷰 조회
     */
    List<String> selectReviewsByAptSeq(@Param("aptSeq") String aptSeq);
}
