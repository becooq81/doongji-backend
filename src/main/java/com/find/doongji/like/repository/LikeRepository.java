package com.find.doongji.like.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LikeRepository {

    void toggleLike(@Param("username") String username, @Param("aptSeq") String aptSeq);

    int selectLike(@Param("username")String username, @Param("aptSeq")String aptSeq);

    List<String> selectAllLikes(@Param("username") String username);
}
