package com.find.doongji.search.repository;

import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.search.payload.request.SearchQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchRepository {
    List<AptInfo> filterBySearchQuery(@Param("searchQuery") SearchQuery searchQuery);

    int selectCountBySearchQuery(@Param("searchQuery") SearchQuery searchQuery);
}
