package com.example.mapper;

import com.example.entity.Apply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ApplyMapper {

    int insert(Apply apply);

    void updateById(Apply apply);

    void deleteById(Integer id);

    @Select("select * from `apply` where id = #{id}")
    Apply selectById(Integer id);

    List<Apply> selectAll(Apply apply);

    @Select("select * from apply where teacher_id = #{teacherId} and activity_id = #{activityId} and status != '不通过'")
    List<Apply> selectByTeacherIdAndActivityId(@Param("teacherId") Integer teacherId, @Param("activityId") Integer activityId);
}
