package com.chiefdata.myUtil.controller;

import com.chiefdata.myUtil.service.PdfService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author MODA-Master
 * @Title: zjy
 * @ProjectName myPdfUtil
 * @Description:
 * @date 2022/8/5 16:00
 */

@Controller
@RequestMapping("/pdf")
@AllArgsConstructor
public class PdfController {

    private final PdfService pdfService;

    @PostMapping("/pdfToImages")
    public void pdfToImages(@RequestParam("file")MultipartFile file,@RequestParam("dpi") int dpi, HttpServletResponse response) {
        try {
            pdfService.pdfToImages(file,dpi,response);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @PostMapping("/imagesToPdf")
    public void imagesToPdf(@RequestParam("file")MultipartFile file,HttpServletResponse response)  {
        try {
            pdfService.imagesToPdf(file,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
