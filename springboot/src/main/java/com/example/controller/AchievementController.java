package com.example.controller;

import com.example.common.Result;
import com.example.entity.Achievement;
import com.example.service.AchievementService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 科研项目前端请求接口
 */
@RestController
@RequestMapping("/achievement")
public class AchievementController {

    @Resource
    private AchievementService achievementService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Achievement achievement) throws ParseException {
        achievementService.add(achievement);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Achievement achievement) {
        achievementService.updateById(achievement);
        return Result.success();
    }

    @PutMapping("/check")
    public Result check(@RequestBody Achievement achievement) {
        achievementService.check(achievement);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        achievementService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        achievementService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Achievement achievement = achievementService.selectById(id);
        return Result.success(achievement);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Achievement achievement) {
        List<Achievement> list = achievementService.selectAll(achievement);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Achievement achievement,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Achievement> pageInfo = achievementService.selectPage(achievement, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
