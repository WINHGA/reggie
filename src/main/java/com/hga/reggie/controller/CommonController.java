package com.hga.reggie.controller;

import com.hga.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @Date 2023/6/11 16:27
 * @Author HGA
 * @Class CommonController
 * @Package com.hga.reggie.controller
 * Description:  公共控制类
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file  文件
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file)  {
        // 形参名必须时前端传过来的(file)，不能随意
        log.info("上传的文件：",file.toString());

        // 获取原始的文件名
        String originalFileName = file.getOriginalFilename();

        // 截取文件的后缀
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 因为文件是用户上传的，不知到文件名是否有重复的，有可能导致相同名，会把原来的文件给覆盖掉。
        // 使用UUID重新生成文件名
        String fileName = UUID.randomUUID().toString();

        // 拼接后缀
        fileName += suffix;

        // 创建一个目录对象
        File dir = new File(basePath);

        // 判断当前目录是否存在
        if(!dir.exists()){
            // 不存在就创建这个目录
            dir.mkdirs();
        }

        // 指定文件上传保存的位置
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 将上传的文件名返回给前端
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name 文件名
     * @param response 返回响应体
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response)  {

        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;

        // 设置响应类型
        response.setContentType("image/jpeg");

        try {
            // 输入流，通过输入流读取文件内容
            fileInputStream = new FileInputStream(new File(basePath + name));

            // 输出流，通过输出流将文件写回到浏览器，在浏览器展示
            outputStream = response.getOutputStream();

            byte[] bytes = new byte[1024];
            int len = 0;
            // 写操作
            while((len = fileInputStream.read(bytes)) != -1){
                // 写操作，从哪里开始，读到哪里。
                outputStream.write(bytes,0,len);
                // 输出流刷新
                outputStream.flush();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
