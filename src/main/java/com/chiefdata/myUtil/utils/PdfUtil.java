package com.chiefdata.myUtil.utils;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author MODA-Master
 * @Title: zjy
 * @ProjectName myPdfUtil
 * @Description:
 * @date 2022/8/5 16:07
 */
public class PdfUtil {

    /**
     *
     * @param imageFolderPath  图片文件夹
     * @param pdfFilePath 生成的pdf路径
     */
    public static void imagesToPdf(String imageFolderPath,String pdfFilePath) throws IOException {

        Document doc = null;
        FileOutputStream fos = null;
        try {
            // 图片地址
            String imagePath = null;
            // 输入流
            fos = new FileOutputStream(pdfFilePath);
            // 创建文档
            doc = new Document(null, 0, 0, 0, 0);
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;
            // 获取图片文件夹对象
            File file = new File(imageFolderPath);
            File[] files = file.listFiles();
            // 循环获取图片文件夹内的图片
            for (File file1 : files) {
                if (file1.getName().endsWith(".png") || file1.getName().endsWith(".jpg") || file1.getName().endsWith(".gif")
                        || file1.getName().endsWith(".jpeg") || file1.getName().endsWith(".tif")) {
                    imagePath = imageFolderPath + file1.getName();
                    System.out.println(imagePath);
                    // 读取图片流
                    img = ImageIO.read(new File(imagePath));
                    doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                    // 根据图片大小设置文档大小
                    doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                    // 实例化图片
                    image = Image.getInstance(imagePath);
                    // 添加图片到文档
                    doc.open();
                    doc.add(image);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭文档
            doc.close();
            fos.close();
        }

    }


    public static void pdfToImages(String pdfFilePath,String imgDir,int dpi) {
        File file = new File(pdfFilePath);
        PDDocument pdDocument;
        try {

            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            int pages = pdDocument.getNumberOfPages();
            StringBuffer imgFilePath;
            for (int i = 0; i < pages; i++) {
                String imgFilePathPrefix = imgDir+File.separator;
                imgFilePath = new StringBuffer();
                imgFilePath.append(imgFilePathPrefix);
                imgFilePath.append(String.format("%05d", i));
                imgFilePath.append(".png");
                File dstFile = new File(imgFilePath.toString());
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                ImageIO.write(image, "png", dstFile);
            }
            pdDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
