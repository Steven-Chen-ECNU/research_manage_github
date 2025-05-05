package com.example.controller;

import com.example.common.Result;
import com.example.service.AIService;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
// PDF依赖
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
@RequestMapping("/project")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analyze")
    public Result analyze(@RequestBody AnalyzeRequest request) {
        try {
            String result = aiService.analyzeFile(request.getFileUrl());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/downloadResult")
    public void downloadResult(@RequestBody DownloadRequest req, HttpServletResponse response) throws Exception {
        String content = req.getContent();
        String format = req.getFormat(); // "word" "txt" "pdf"
        String fileName = "AI分析结果." + format;

        if ("txt".equalsIgnoreCase(format)) {
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.getWriter().write(content);
        } else if ("word".equalsIgnoreCase(format)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            XWPFDocument doc = new XWPFDocument();
            XWPFParagraph para = doc.createParagraph();
            para.createRun().setText(content);
            doc.write(response.getOutputStream());
            doc.close();
        } else if ("pdf".equalsIgnoreCase(format)) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            Document pdfDoc = new Document();
            PdfWriter.getInstance(pdfDoc, response.getOutputStream());
            pdfDoc.open();
            pdfDoc.add(new Paragraph(content));
            pdfDoc.close();
        }
    }

    public static class AnalyzeRequest {
        private String fileUrl;

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }

    public static class DownloadRequest {
        private String content;
        private String format;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }
} 