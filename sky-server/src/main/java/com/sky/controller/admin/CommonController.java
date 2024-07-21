package com.sky.controller.admin;

import com.sky.config.OssConfiguration;
import com.sky.constant.MessageConstant;
import com.sky.properties.AliOssProperties;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    OssConfiguration ossConfiguration;
    AliOssProperties aliOssProperties;

    @PostMapping("upload")
    public Result<String> fileupload(MultipartFile file) throws IOException {
        log.info("文件上传：{}",file);
        try {
            String orginalname = file.getOriginalFilename();
            String back = orginalname.substring(orginalname.lastIndexOf("."));
            String name = UUID.randomUUID().toString() + back;

            AliOssUtil aliOssUtil = ossConfiguration.aliOssUtil(aliOssProperties);

            String URL = aliOssUtil.upload(file.getBytes(),name);

            return Result.success(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);

    }
}
