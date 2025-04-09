package com.example.controller;

import com.example.common.Result;
import com.example.entity.Log;
import com.example.service.LogService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Log log) {
        logService.add(log);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        logService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        logService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Log log,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Log> pageInfo = logService.selectPage(log, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
