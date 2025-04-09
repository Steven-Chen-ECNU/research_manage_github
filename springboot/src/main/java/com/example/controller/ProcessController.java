package com.example.controller;

import com.example.common.Result;
import com.example.common.config.AutoLog;
import com.example.entity.Process;
import com.example.service.ProcessService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/process")
public class ProcessController {

    @Resource
    private ProcessService processService;

    /**
     * 新增
     */
    @AutoLog("新增科研过程")
    @PostMapping("/add")
    public Result add(@RequestBody Process process) {
        processService.add(process);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Process process) {
        processService.updateById(process);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @AutoLog("删除科研过程")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        processService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @AutoLog("批量删除科研过程")
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        processService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Process process = processService.selectById(id);
        return Result.success(process);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Process process) {
        List<Process> list = processService.selectAll(process);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Process process,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Process> pageInfo = processService.selectPage(process, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
