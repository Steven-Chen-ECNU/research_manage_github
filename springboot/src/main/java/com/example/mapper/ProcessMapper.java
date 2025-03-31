package com.example.mapper;

import com.example.entity.Process;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProcessMapper {

    int insert(Process process);

    void updateById(Process process);

    void deleteById(Integer id);

    @Select("select * from `process` where id = #{id}")
    Process selectById(Integer id);

    List<Process> selectAll(Process process);

}
