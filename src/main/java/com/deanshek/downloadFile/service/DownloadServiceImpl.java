package com.deanshek.downloadFile.service;

import com.deanshek.uploadFile.entity.UploadFile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DownloadServiceImpl
 * @Author deanshek
 * @Date 2020-04-04 22:06
 * @Wechat s71t28
 * @Version 1.0
 **/
@Service
public class DownloadServiceImpl implements DownloadService{
    @Override
    public List<String> queryAll() {
        //模拟去数据库查询需要下载的数据
        List<String> list = new ArrayList<String>();
        list.add("张三,男");
        list.add("李四,女");
        return list;
    }
}
