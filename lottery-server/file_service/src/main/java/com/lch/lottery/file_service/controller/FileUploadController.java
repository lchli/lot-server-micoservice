package com.lch.lottery.file_service.controller;


import com.lch.lottery.file_service.service.FileService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
    @Autowired
    private FileService fileService;

    @Value("${spring.application.name}")
    String servicename;


    @PostMapping(path = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public String uploadFile(@RequestParam("file") MultipartFile fileMetaData) {

        ObjectId id = fileService.saveFile(fileMetaData);
        if (id == null) {
            return null;
        }

        return buildFileDownloadUrl(id.toString());
    }

    private String buildFileDownloadUrl(String fileId) {

        return String.format("http://localhost:50209/api/file/%s", fileId);

    }
}