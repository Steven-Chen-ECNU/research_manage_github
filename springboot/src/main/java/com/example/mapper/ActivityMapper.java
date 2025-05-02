package com.example.mapper;

import com.example.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper {

    int insert(Activity activity);

    void updateById(Activity activity);

    void deleteById(Integer id);

    @Select("select * from `activity` where id = #{id}")
    Activity selectById(Integer id);

    List<Activity> selectAll(Activity activity);

    List<Map<String, Object>> getResearchVitality();
}
