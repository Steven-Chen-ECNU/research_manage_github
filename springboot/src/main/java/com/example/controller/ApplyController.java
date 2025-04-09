package com.example.controller;

import com.example.common.Result;
import com.example.common.config.AutoLog;
import com.example.entity.Apply;
import com.example.service.ApplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Resource
    private ApplyService applyService;

    /**
     * 新增
     */
    @AutoLog("新增学术活动申请")
    @PostMapping("/add")
    public Result add(@RequestBody Apply apply) {
        applyService.add(apply);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Apply apply) {
        applyService.updateById(apply);
        return Result.success();
    }

    @AutoLog("审核学术活动申请")
    @PutMapping("/check")
    public Result check(@RequestBody Apply apply) {
        applyService.check(apply);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @AutoLog("删除学术活动申请")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        applyService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @AutoLog("批量删除学术活动申请")
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        applyService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Apply apply = applyService.selectById(id);
        return Result.success(apply);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Apply apply) {
        List<Apply> list = applyService.selectAll(apply);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Apply apply,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Apply> pageInfo = applyService.selectPage(apply, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}