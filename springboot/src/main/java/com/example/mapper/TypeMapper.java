package com.example.mapper;

import com.example.entity.Type;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TypeMapper {

    int insert(Type type);

    void updateById(Type type);

    void deleteById(Integer id);

    @Select("select * from `type` where id = #{id}")
    Type selectById(Integer id);

    List<Type> selectAll(Type type);

}
