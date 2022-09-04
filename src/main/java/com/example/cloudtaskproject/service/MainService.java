package com.example.cloudtaskproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.base.Utf8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.*;

@Service
public class MainService {
    public String findMaxValue(MultipartFile file) throws IOException {
        System.out.println("");
        String filePath=file.getOriginalFilename();
        String tmp;
        int max = 0;
        int lineNumber = 1;
        int currentLine = 1;
        StringBuilder stringBuilder=new StringBuilder();
        String ls = System.getProperty("line.separator");
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
                    stringBuilder.append(tmp+"\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
        int last = stringBuilder.lastIndexOf("\n");
        if (last >= 0) { stringBuilder.delete(last, stringBuilder.length()); }
        uploadObject(filePath,stringBuilder);
        return "Status code: OK \n The maximum value in the file is: " + max + " and the location (line number) is: " + lineNumber;
    }
    public static void uploadObject(
            String filePath,StringBuilder stringBuilder) throws IOException {
        // The ID of your GCP project
        String projectId = "pvo-p1";

        // The ID of your GCS bucket
        String bucketName = "pvo-p1.appspot.com";

        // The ID of your GCS object
        String objectName = filePath;

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        //Blob blob = bucket.create(filePath, "Hello, World!".getBytes(), "text/plain");

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, stringBuilder.toString().getBytes());

        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }
}
