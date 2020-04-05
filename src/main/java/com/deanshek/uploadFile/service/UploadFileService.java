package com.deanshek.uploadFile.service;

import com.deanshek.uploadFile.entity.UploadFile;

import java.io.File;

/**
 * @ClassName UploadFileService
 * @Author deanshek
 * @Date 2020-04-05 14:17
 * @Wechat s71t28
 * @Version 1.0
 **/
public interface UploadFileService {
    /**
     * 将上传完的文件存到数据库
     * @param uploadFile
     * @return
     */
    public int insert(UploadFile uploadFile);

    /**
     * 将文件上传到缓存服务器上并且返回缓存服务器的地址
     * @param file
     * @return
     */
    public  String upload(File file);
}
