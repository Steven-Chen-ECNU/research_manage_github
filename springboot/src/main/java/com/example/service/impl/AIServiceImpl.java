package com.example.service.impl;

import com.example.service.AIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIServiceImpl implements AIService {

    private static final Logger log = LoggerFactory.getLogger(AIServiceImpl.class);

    @Value("${tencent.secretId}")
    private String secretId;

    @Value("${tencent.secretKey}")
    private String secretKey;

    @Value("${tencent.region}")
    private String region;

    private static final String filePath = System.getProperty("user.dir") + "/files/";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String analyzeFile(String fileUrl) throws Exception {
        // 提取文件名
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        File file = new File(System.getProperty("user.dir") + "/files/" + fileName);
        String content = readDocxContent(file);
        if (content.length() > 6000) content = content.substring(0, 6000);
        return analyzeTextWithTencent(content);
    }

    private String readDocxContent(File file) throws Exception {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph para : document.getParagraphs()) {
                content.append(para.getText()).append("\n");
            }
        }
        return content.toString();
    }

    private String analyzeTextWithTencent(String text) throws Exception {
        String action = "ChatCompletions";
        String version = "2023-09-01";
        String host = "hunyuan.tencentcloudapi.com";

        Map<String, Object> body = new HashMap<>();
        body.put("Model", "hunyuan-turbo");
        body.put("TopP", 0.8);
        body.put("Temperature", 0.7);
        body.put("Stream", false);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("Role", "user");
        message.put("Content", "请对以下内容做一个500字左右的总结：\n\n" + text);
        messages.add(message);
        body.put("Messages", messages);

        String response = sendTencentApiRequest(action, version, host, body);
        Map<String, Object> respMap = objectMapper.readValue(response, Map.class);
        Map<String, Object> responseObj = (Map<String, Object>) respMap.get("Response");
        if (responseObj != null && responseObj.containsKey("Choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseObj.get("Choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> msg = (Map<String, Object>) choices.get(0).get("Message");
                if (msg != null) {
                    return (String) msg.get("Content");
                }
            }
        }
        throw new RuntimeException("AI分析失败，未获取到总结内容");
    }

    private String sendTencentApiRequest(String action, String version, String host, Map<String, Object> body) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = String.valueOf(new Random().nextInt(1000000));
        String algorithm = "TC3-HMAC-SHA256";
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

        // 构建请求体
        String payload = objectMapper.writeValueAsString(body);

        // 构建规范请求串
        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json\nhost:" + host + "\n";
        String signedHeaders = "content-type;host";
        String hashedRequestPayload = sha256Hex(payload);

        String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

        // 构建待签名字符串
        String credentialScope = date + "/hunyuan/tc3_request";
        String hashedCanonicalRequest = sha256Hex(canonicalRequest);
        String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

        // 计算签名
        byte[] secretDate = hmacSha256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmacSha256(secretDate, "hunyuan");
        byte[] secretSigning = hmacSha256(secretService, "tc3_request");
        String signature = bytesToHex(hmacSha256(secretSigning, stringToSign));

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Host", host);
        headers.set("X-TC-Action", action);
        headers.set("X-TC-Version", version);
        headers.set("X-TC-Timestamp", timestamp);
        headers.set("X-TC-Region", region);
        headers.set("X-TC-Nonce", nonce);
        headers.set("X-TC-RequestClient", "SDK_JAVA_3.0.0");
        headers.set("Authorization", algorithm + " " + "Credential=" + secretId + "/" + credentialScope + ", "
                + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature);

        // 发送请求
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        String response = restTemplate.postForObject("https://" + host, request, String.class);
        log.debug("腾讯云API响应: {}", response);

        return response;
    }

    private String sha256Hex(String s) throws Exception {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(d);
    }

    private byte[] hmacSha256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
} 