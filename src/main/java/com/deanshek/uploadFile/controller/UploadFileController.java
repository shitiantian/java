package com.deanshek.uploadFile.controller;

import com.deanshek.uploadFile.entity.UploadFile;
import com.deanshek.uploadFile.service.UploadFileService;
import com.deanshek.uploadFile.service.UploadFileServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
/**
 * @ClassName UploadFileController
 * @Author deanshek
 * @Date 2020-04-04 22:06
 * @Wechat s71t28
 * @Version 1.0
 **/
@Controller
public class UploadFileController {

    @Autowired
    private UploadFileServiceImpl uploadFileService;

    @RequestMapping("/upload/page")
    public String  page() {
        return "uploadFile";
    }
    @RequestMapping("/upload")
    @ResponseBody
     public String upload(MultipartFile upload, HttpServletRequest request) throws IOException {

        // 获取上传文件的名称
        String filename = upload.getOriginalFilename();

        //校验文件后缀是否符合业务场景要求PNG/jpg/JPEG/TXT/XLS/XLSX/DOC等等后缀根据条件
        boolean b = FilenameUtils.isExtension(filename,"jpg");
        if(!b){
            return "文件名非法";
        }

        // 上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads/");
        // 判断，该路径是否存在
        File file = new File(path);
        if(!file.exists()){
            // 创建该文件夹
            file.mkdirs();
        }

        // 把文件的名称设置唯一值，uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid+"_"+filename;
        // 讲文件上传到本地
        upload.transferTo(new File(path,filename));

        //将本地临时文件上传到缓存服务器/对象存储服务器，比如阿里云oss里
        String ossPath = "";
        try {
          ossPath = uploadFileService.upload(new File(path,filename));
        } catch (Exception e) {
            return "上传oss失败.具体原因:"+e;
        }

        //将临时文件夹中的内容删除掉
        if(file.exists()){
            file.delete();
        }

        //将文件服务器的地址存到数据库里
        UploadFile uploadFile = new UploadFile();
        uploadFile.setPath(ossPath);
        uploadFileService.insert(uploadFile);
        return "success";
    }
}
