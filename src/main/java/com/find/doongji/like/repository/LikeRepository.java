package com.find.doongji.like.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeRepository {

    void toggleLike(@Param("username") String username, @Param("aptSeq") String aptSeq);

    int selectLike(@Param("username")String username, @Param("aptSeq")String aptSeq);

}
