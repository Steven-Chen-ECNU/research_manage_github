package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${fileBaseUrl:}")
    private String fileBaseUrl;

    public FileController() {
        // 确保文件目录存在
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
            log.info("Created files directory at: {}", filePath);
        }
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            fileName = System.currentTimeMillis() + "-" + fileName;
            String realFilePath = filePath + fileName;
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), realFilePath);
            log.info("File uploaded successfully: {}", realFilePath);
        } catch (Exception e) {
            log.error(fileName + "--文件上传失败", e);
            return Result.error("文件上传失败");
        }
        String url = fileBaseUrl + "/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 获取文件
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                String filePath = FileController.filePath + fileName;
                if (!FileUtil.exist(filePath)) {
                    log.error("File not found: {}", filePath);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                log.info("File downloaded successfully: {}", filePath);
            }
        } catch (Exception e) {
            log.error("文件下载失败：" + fileName, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
