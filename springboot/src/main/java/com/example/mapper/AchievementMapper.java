package com.example.mapper;

import com.example.entity.Achievement;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AchievementMapper {

    int insert(Achievement achievement);

    void updateById(Achievement achievement);

    void deleteById(Integer id);

    @Select("select * from `achievement` where id = #{id}")
    Achievement selectById(Integer id);

    List<Achievement> selectAll(Achievement achievement);

}
