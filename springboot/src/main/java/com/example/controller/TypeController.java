package com.example.controller;

import com.example.common.Result;
import com.example.common.config.AutoLog;
import com.example.entity.Type;
import com.example.service.TypeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/type")
public class TypeController {

    @Resource
    private TypeService typeService;

    /**
     * 新增
     */
    @AutoLog("新增成果类型")
    @PostMapping("/add")
    public Result add(@RequestBody Type type) {
        typeService.add(type);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Type type) {
        typeService.updateById(type);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @AutoLog("删除成果类型")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        typeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @AutoLog("批量删除成果类型")
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        typeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Type type = typeService.selectById(id);
        return Result.success(type);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Type type) {
        List<Type> list = typeService.selectAll(type);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Type type,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Type> pageInfo = typeService.selectPage(type, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
