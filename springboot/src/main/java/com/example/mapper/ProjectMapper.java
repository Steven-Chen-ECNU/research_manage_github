package com.example.mapper;

import com.example.entity.Project;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProjectMapper {

    int insert(Project project);

    void updateById(Project project);

    void deleteById(Integer id);

    @Select("select * from `project` where id = #{id}")
    Project selectById(Integer id);

    List<Project> selectAll(Project project);

}
