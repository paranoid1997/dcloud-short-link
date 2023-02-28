package net.xdclass.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.OSSConfig;
import net.xdclass.service.FileService;
import net.xdclass.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private OSSConfig ossConfig;

    @Override
    public String uploadUserImg(MultipartFile file) {
        String bucketname = ossConfig.getBucketname();
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //xxx.jpg
        String originalFilename = file.getOriginalFilename();

        //jdk8语法获取日期格式
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        String folder = pattern.format(ldt);
        String fileName = CommonUtil.generateUUID();

        //拿到后缀
        String extendsion = originalFilename.substring(originalFilename.lastIndexOf("."));

        //在oss的bucket上创建文件夹
        String newFilename = "user/" + folder + "/" + fileName + extendsion;

        //推送到阿里云上
        try {
            PutObjectResult putObjectResult = ossClient.putObject(bucketname, newFilename, file.getInputStream());
            //拼装返回路径
            if (putObjectResult != null) {
                String imgUrl = "https://" + bucketname + "." + endpoint + "/" + newFilename;
                return imgUrl;
            }
        } catch (IOException e) {
            log.error("文件上传失败:{}",e.getMessage());
        }finally {
            ossClient.shutdown();
        }

        return null;
    }
}
