package com.deanshek.uploadFile.service;

import com.deanshek.uploadFile.entity.UploadFile;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @ClassName UploadFileServiceImpl
 * @Author deanshek
 * @Date 2020-04-05 14:17
 * @Wechat s71t28
 * @Version 1.0
 **/
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public int insert(UploadFile uploadFile) {
        //自行将数据插入数据库，在此不写serviceImpl->dao
        return 1;
    }

    @Override
    public String upload(File file) {
        String path = "";
        //这里一般就是调用外系统的接口，去将文件上传并且返回一个服务器地址。
//        path = ossUtils.upload(new File(path,filename));
        path="http://www.baidu.com";
        return path;
    }
}
