package com.example.mapper;

import com.example.entity.Activity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ActivityMapper {

    int insert(Activity activity);

    void updateById(Activity activity);

    void deleteById(Integer id);

    @Select("select * from `activity` where id = #{id}")
    Activity selectById(Integer id);

    List<Activity> selectAll(Activity activity);

}
