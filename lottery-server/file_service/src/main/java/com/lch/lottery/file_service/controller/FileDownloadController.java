package com.lch.lottery.file_service.controller;

import com.lch.lottery.file_service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileDownloadController {
    @Autowired
    private FileService fileService;

    @GetMapping(path = "/file/{fileId}")
    public ResponseEntity<Resource> downloadApkFile(@PathVariable("fileId") final String fileId) {
        System.err.println("downloadApkFile:"+fileId);

        GridFsResource gridFsResource = fileService.getFile(fileId);
        if (gridFsResource == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AbstractResource(){
                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream("not found file".getBytes());
                }
            });
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(gridFsResource.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + gridFsResource.getFilename() + "\"").body(gridFsResource);
    }
}