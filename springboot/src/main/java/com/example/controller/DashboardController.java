package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.*;
import com.example.service.ActivityService;
import com.example.service.FeedbackService;
import com.example.service.AchievementService;
import com.example.service.ApplyService;
import com.example.service.ProjectService;
import com.example.service.TeacherService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据统计前端调用的接口入口
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Resource
    private ProjectService projectService;
    @Resource
    private FeedbackService feedbackService;
    @Resource
    private ActivityService activityService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private ApplyService applyService;
    @Resource
    private AchievementService achievementService;

    @GetMapping("/base")
    public Result base() {
        Map<String, Integer> resultMap = new HashMap<>();

        Project project = new Project();
        project.setStatus("审核通过");
        List<Project> projects = projectService.selectAll(project);

        List<Feedback> feedbacks = feedbackService.selectAll(new Feedback());
        List<Activity> activities = activityService.selectAll(new Activity());
        List<Teacher> teachers = teacherService.selectAll(new Teacher());

        resultMap.put("project", projects.size());
        resultMap.put("feedback", feedbacks.size());
        resultMap.put("activity", activities.size());
        resultMap.put("teacher", teachers.size());

        return Result.success(resultMap);
    }

    @GetMapping("/pie")
    public Result pie() {
        List<Map<String, Object>> result = new ArrayList<>();
        Project project = new Project();
        project.setStatus("审核通过");
        List<Project> projects = projectService.selectAll(project);
        Map<String, Long> collect = projects.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getLevel()))
                .collect(Collectors.groupingBy(Project::getLevel, Collectors.counting()));
        for (String key : collect.keySet()) {
            Map<String, Object> temMap = new HashMap<>();
            temMap.put("name", key);
            temMap.put("value", collect.get(key));
            result.add(temMap);
        }
        return Result.success(result);
    }

    @GetMapping("/bar1")
    public Result bar1() {
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> xList = new ArrayList<>();
        List<Object> yList = new ArrayList<>();

        // 封装xList和yList里面的值
        List<Apply> applies = applyService.selectAll(new Apply());
        Map<String, Long> collect = applies.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getActivityName()))
                .collect(Collectors.groupingBy(Apply::getActivityName, Collectors.counting()));
        for (String key : collect.keySet()) {
            xList.add(key);
            yList.add(collect.get(key));
        }

        resultMap.put("xAxis", xList);
        resultMap.put("yAxis", yList);
        return Result.success(resultMap);
    }

    @GetMapping("/bar2")
    public Result bar2() {
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> xList = new ArrayList<>();
        List<Object> yList = new ArrayList<>();

        // 封装xList和yList里面的值
        Achievement achievement = new Achievement();
        achievement.setStatus("审核通过");
        List<Achievement> achievements = achievementService.selectAll(achievement);
        Map<String, Long> collect = achievements.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getTypeName()))
                .collect(Collectors.groupingBy(Achievement::getTypeName, Collectors.counting()));

        for (String key : collect.keySet()) {
            xList.add(key);
            yList.add(collect.get(key));
        }

        resultMap.put("xAxis", xList);
        resultMap.put("yAxis", yList);
        return Result.success(resultMap);
    }

    @GetMapping("/line")
    public Result line() {
        Map<String, List<String>> resultMap = new HashMap<>();
        List<String> yList = new ArrayList<>();

        // 封装xList和yList里面的值
        List<Feedback> feedbacks = feedbackService.selectAll(new Feedback());
        Date today = new Date();
        DateTime start = DateUtil.offsetDay(today, -7);
        List<String> xList = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).toList();
        for (Object xTime : xList) {
            int size = feedbacks.stream().filter(x -> ObjectUtil.isNotEmpty(x.getTime()) && x.getTime().contains(xTime + "")).toList().size();
            yList.add(Integer.toString(size));
        }

        resultMap.put("xAxis", xList);
        resultMap.put("yAxis", yList);
        return Result.success(resultMap);
    }

    @GetMapping("/activeTeacherCount")
    public Result activeTeacherCount() {
        Integer count = projectService.countActiveTeacher();
        return Result.success(count);
    }

    @GetMapping("/researchVitality")
    public Result researchVitality() {
        try {
            logger.warn("开始获取科研活力数据");
            List<Map<String, Object>> result = activityService.getResearchVitality();
            logger.info("获取到的数据: {}", result);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取科研活力数据失败", e);
            return Result.error("-1", "获取科研活力数据失败：" + e.getMessage());
        }
    }
}
