package com.example.cloudtaskproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class MainService {
    public String findMaxValue(MultipartFile file) throws IOException {
        String tmp;
        int max = 0;
        int lineNumber = 1;
        int currentLine = 1;
        try {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".txt")) {
                return "1 - Wrong input format";
            }
            InputStream stream = file.getInputStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                while ((tmp = reader.readLine()) != null) {
                    if (Integer.parseInt(tmp) > 1023 || Integer.parseInt(tmp) < 0) {
                        return "2 - Error in calculation.";
                    }
                    if (Integer.parseInt(tmp) > max) {
                        max = Integer.parseInt(tmp);
                        lineNumber = currentLine;
                    }
                    currentLine++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
        return "Status code: OK \n The maximum value in the file is: " + max + " and the location (line number) is: " + lineNumber;
    }
}
