package com.lch.lottery.file_service.service;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoDbFactory mongoDbFactory;


    public ObjectId saveFile(MultipartFile fileMetaData) {
        InputStream ins = null;

        try {
            String fname = fileMetaData.getOriginalFilename();
            ins = fileMetaData.getInputStream();

            GridFSBucket bucket = getGridFs();
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .metadata(new Document("_contentType", fileMetaData.getContentType()));

            return bucket.uploadFromStream(fname, ins, options);

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }

    public GridFsResource getFile(String fileId) {
        return find(fileId);
    }

    private GridFsResource find(String fileId) {
        GridFSDownloadStream ds = getGridFs().openDownloadStream(new ObjectId(fileId));

        return new GridFsResource(ds.getGridFSFile(), ds);
    }

    private GridFSBucket getGridFs() {

        MongoDatabase db = mongoDbFactory.getDb("fileDB");
        return GridFSBuckets.create(db);
    }


}