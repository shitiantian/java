package com.deanshek.downloadFile.controller;

import com.deanshek.downloadFile.service.DownloadService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName DownloadFileController
 * @Author deanshek
 * @Date 2020-04-04 22:06
 * @Wechat s71t28
 * @Version 1.0
 **/
@Controller
public class DownloadFileController {

    @Autowired
    private DownloadService downloadService;

    @RequestMapping("/download/page")
    public String  page() {
        return "downloadFile";
    }

    @RequestMapping("/download")
     public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //校验查询参数是否合法

        //从数据库中查询出来需要的数据
        List<String> dataList = downloadService.queryAll();
        //将模板头跟查询的数据组成新list
        List<String> list = new ArrayList<String>();
        list.add("姓名,性别");
        list.addAll(dataList);

        //将数据放到临时文件中
        SimpleDateFormat sb = new SimpleDateFormat("yyyyMMdd");
        String date = sb.format(new Date());
        String filename = "下载的文件-"+date;
        String suffix = ".txt";
        File txtFile = File.createTempFile(filename, suffix);

        //将模板标题头+查询到的数据追加到临时文件里
        FileUtils.writeLines(txtFile, list, false);
//        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(txtFile), "utf-8"));
//        for (int i = 0; i < list.size(); i++) {
//            pw.println(list.get(i));
//        }
//        pw.flush();

        //将文本文件以流的形式去下载
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(txtFile));
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode(filename,"UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename+suffix);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data; charset=utf-8");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
        //程序退出删除临时文件
        txtFile.deleteOnExit();
    }
}
