package com.example.cloudtaskproject.controller;

import com.example.cloudtaskproject.service.MainService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Controller
public class MainController {
    private final MainService service;

    @Autowired
    public MainController(MainService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getPage() {
        return "mainForm";
    }

    @PostMapping("/")
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                 HttpServletResponse response) throws IOException {

        long startTime = System.nanoTime();
        String result = service.findMaxValue(file);
        long elapsedTime = System.nanoTime() - startTime;
        result = result + "\n The processing time was: " + elapsedTime / 1000000 + " ms.";
        InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
        IOUtils.copy(is, response.getOutputStream());
        response.setContentType("application/txt");
        response.setHeader("Content-Disposition", "attachment; filename=\"output.txt\"");
        response.flushBuffer();
    }
}
