package com.example.common.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Log;
import com.example.service.LogService;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * 处理切面的“监控”
 */
@Component
@Aspect
public class LogAspect {

    @Resource
    private LogService logService;

    @Around("@annotation(autoLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, AutoLog autoLog) throws Throwable {
        System.out.println("我来切了！");

        // 操作内容，我们在注解里已经定义了value()，然后再需要切入的接口上面去写上对应的操作内容即可
        String name = autoLog.value();
        // 操作时间（当前时间）
        String time = DateUtil.now();
        // 操作人
        String username = "";
        Account user = TokenUtils.getCurrentUser();
        if (ObjectUtil.isNotNull(user)) {
            username = user.getName();
        }
        // 操作人IP
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String location = getLocation();

        // 执行具体的接口
        Result result = (Result) joinPoint.proceed();

        Object data = result.getData();
        if (data instanceof Account) {
            String userName = ((Account) data).getName();
            if (ObjectUtil.isNotEmpty(userName)) {
                username = userName;
            } else {
                username = ((Account) data).getUsername();
            }
        }

        // 再去往日志表里写一条日志记录
        Log log = new Log();
        log.setTime(time);
        log.setName(name);
        log.setIp("0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip);
        log.setLocation(location);
        log.setUsername(username);

        logService.add(log);

        // 你可以走了，去返回前台报到吧~
        return result;
    }

    public String getLocation() throws IOException {
        String url = "https://api.map.baidu.com/location/ip?ip=" + getIP() + "&ak=bmvg8yeOopwOB4aHl5uvx52rgIa3VrPO";
        String res = HttpUtil.createRequest(Method.GET, url).execute().body();
        String json = UnicodeUtil.toString(res);
        String originalString = JSONUtil.parseObj(json).getStr("address");
        String[] parts = originalString.split("\\|");
        String result="";
        if (parts.length > 1 && parts.length > 2) {
            // 获取"安徽省"和"合肥市"并拼接
            String province = parts[1];
            String city = parts[2];
            result = province + "-" + city;
            // 输出结果
        }
        return result;
    }

    private String getIP() throws IOException {
        Document document = Jsoup.connect("https://ip.900cha.com/").get();
        // /html/body/div/div/div/div[1]/div[1]/h3
        return document.selectXpath("/html/body/div/div/div/div[1]/div[1]/h3").get(0).text().trim();
    }
}