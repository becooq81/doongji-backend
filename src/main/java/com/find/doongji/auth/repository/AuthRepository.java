package com.find.doongji.auth.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthRepository {

    String getPasswordByUsername(@Param("username") String username);
}
