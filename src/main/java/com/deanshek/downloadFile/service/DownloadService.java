package com.deanshek.downloadFile.service;

import com.deanshek.uploadFile.entity.UploadFile;

import java.util.List;

/**
 * @ClassName DownloadService
 * @Author deanshek
 * @Date 2020-04-04 22:06
 * @Wechat s71t28
 * @Version 1.0
 **/
public interface DownloadService {
    /**
     * 查询全部需要下载的数据
     * @return
     */
    public List<String> queryAll();
}
