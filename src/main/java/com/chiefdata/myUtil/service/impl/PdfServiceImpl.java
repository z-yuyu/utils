package com.chiefdata.myUtil.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.chiefdata.myUtil.service.PdfService;
import com.chiefdata.myUtil.utils.PdfUtil;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author MODA-Master
 * @Title: zjy
 * @ProjectName myPdfUtil
 * @Description:
 * @date 2022/8/5 16:12
 */

@Service
public class PdfServiceImpl implements PdfService {


    @Override
    public void pdfToImages(MultipartFile multiFile,int dpi, HttpServletResponse response) throws IOException {

        File zip = null;
        long time = System.currentTimeMillis();
        String dirPath = dirPath();
        FileInputStream input = null;
        try {
            // 获取文件名
            String fileName = multiFile.getOriginalFilename();
            // 获取文件后缀
            String prefix = fileName.substring(fileName.lastIndexOf("."));

            File zipDir = new File(dirPath+File.separator+"zip");
            if (!zipDir.exists()){
                zipDir.mkdirs();
            }

            File tempFile = new File(dirPath+File.separator+"pdf"+File.separator+ time);
            if (!tempFile.exists()){
                tempFile.mkdirs();
            }

            File file = File.createTempFile(fileName, prefix,tempFile);
            multiFile.transferTo(file);

           //创建生成图片目录
            File imgDir = new File(dirPath+File.separator+"img"+File.separator+ time);
            imgDir.mkdirs();

            //转化成图片
            PdfUtil.pdfToImages(file.getPath(),imgDir.getPath(),dpi);

            //打包图片文件夹，生成的压缩文件路径
            String zipFile = zipDir+File.separator+time+".zip";
            ZipUtil.zip(imgDir.getPath(),zipFile);
            zip = new File(zipFile);

            input = new FileInputStream(zip);
            byte[] data = new byte[input.available()];
            input.read(data);
            response.getOutputStream().write(data);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            input.close();
            //删除所有文件
            FileUtil.del(zip);
            FileUtil.del(dirPath+File.separator+"pdf"+File.separator+ time);
            FileUtil.del(dirPath+File.separator+"img"+File.separator+ time);
        }
    }

    @Override
    public void imagesToPdf(MultipartFile multipartFile, HttpServletResponse response) throws IOException {
        long time = System.currentTimeMillis();
        String dirPath = dirPath();
        String zipPath = dirPath+File.separator+"zip"+File.separator+time;
        String imgPath = dirPath+File.separator+"img"+File.separator+time;
        String pdfPath = dirPath+File.separator+"pdf"+File.separator+time;
        FileInputStream input = null;
        try{
            //存放上传的文件
            File zipDir = new File(zipPath);
            if (!zipDir.exists()){
                zipDir.mkdirs();
            }
            File zipFile = File.createTempFile("tmp",".zip",zipDir);
            multipartFile.transferTo(zipFile);

            //解压
            File imgDir = new File(imgPath);
            if (!imgDir.exists()){
                imgDir.mkdirs();
            }
            ZipUtil.unzip(zipFile,imgDir);

            //转化
            File pdfDir = new File(pdfPath);
            if (!pdfDir.exists()){
                pdfDir.mkdirs();
            }
            PdfUtil.imagesToPdf(imgPath+File.separator,pdfPath+File.separator+"create.pdf");

            input = new FileInputStream(pdfPath+File.separator+"create.pdf");
            byte[] data = new byte[input.available()];
            input.read(data);
            response.getOutputStream().write(data);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            input.close();
            FileUtil.del(zipPath);
            FileUtil.del(imgPath);
            FileUtil.del(pdfPath);
        }
    }

    private String dirPath(){
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        return jarF.getParentFile().toString();
    }
}
