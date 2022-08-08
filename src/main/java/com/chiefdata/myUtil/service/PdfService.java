package com.chiefdata.myUtil.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MODA-Master
 * @Title: zjy
 * @ProjectName myPdfUtil
 * @Description: TODO
 * @date 2022/8/5 16:11
 */
public interface PdfService {

    void pdfToImages(MultipartFile file,int dpi, HttpServletResponse response) throws IOException;

    void imagesToPdf(MultipartFile file, HttpServletResponse response) throws IOException;
}
