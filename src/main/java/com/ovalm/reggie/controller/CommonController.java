package com.ovalm.reggie.controller;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ovalm.reggie.common.R;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        Date date = new Date();
        SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss ");
        String fileName =  fileFormat.format(date) + UUID.randomUUID() + suffix;
        String dirPath = basePath;
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            file.transferTo(new File(dirPath + fileName));
        } catch (IOException e){
            e.printStackTrace();
            return R.error("上传文件失败");
        }
        return R.ok().put("data", fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[4096];
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();

            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            ;
        }
    }
}
