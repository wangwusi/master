package com.ws.vpn_server.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import com.ws.vpn_server.domain.dto.UserAddDTO;
import com.ws.vpn_server.service.WsUserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("ws/user")
public class WsUserController {

    @Autowired
    private WsUserService wsUserService;

    @PostMapping
    public void addUser(@RequestBody UserAddDTO dto){
        wsUserService.addUser(dto);
    }

    @GetMapping
    public void start(){
        wsUserService.start();
    }

    @GetMapping("/upload")
    public void upload(@RequestParam("size") Integer size) throws Exception {
        mosaic(size);

    }

    public boolean mosaic(int size) throws Exception {
        File file = new File("C:\\Users\\gj\\Pictures\\Camera Roll\\42a98226cffc1e170d8dcd544c90f603738de9a1.jpg");
        if (!file.isFile()) {
            throw new Exception("ImageDeal>>>" + file + " 不是一个图片文件!");
        }
        BufferedImage bi = ImageIO.read(file); // 读取该图片
        BufferedImage spinImage = new BufferedImage(bi.getWidth(),
                bi.getHeight(), bi.TYPE_INT_RGB);
        if (bi.getWidth() < size || bi.getHeight() < size || size <= 0) { // 马赛克格尺寸太大或太小
            return false;
        }

        int xcount = 0; // 方向绘制个数
        int ycount = 0; // y方向绘制个数
        if (bi.getWidth() % size == 0) {
            xcount = bi.getWidth() / size;
        } else {
            xcount = bi.getWidth() / size + 1;
        }
        if (bi.getHeight() % size == 0) {
            ycount = bi.getHeight() / size;
        } else {
            ycount = bi.getHeight() / size + 1;
        }
        int x = 0; //坐标
        int y = 0;
        // 绘制马赛克(绘制矩形并填充颜色)
        Graphics2D gs = spinImage.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < xcount; i++) {
            for (int j = 0; j < ycount; j++) {
            //马赛克矩形格大小
            int mwidth = size;
            int mheight = size;
            if(i==xcount-1){ //横向最后一个比较特殊，可能不够一个size
                mwidth = bi.getWidth()-x;
            }
            if(j == ycount-1){ //同理
            mheight =bi.getHeight()-y;
            }
            // 矩形颜色取中心像素点RGB值
            int centerX = x;
            int centerY = y;
            if (mwidth % 2 == 0) {
            centerX += mwidth / 2;
            } else {
            centerX += (mwidth - 1) / 2;
            }
            if (mheight % 2 == 0) {
            centerY += mheight / 2;
            } else {
            centerY += (mheight - 1) / 2;
            }
            Color color = new Color(bi.getRGB(centerX, centerY));
            gs.setColor(color);
            gs.fillRect(x, y, mwidth, mheight);
            y = y + size;// 计算下一个矩形的y坐标
            }
            y = 0;// 还原y坐标
            x = x + size;// 计算x坐标
        }
        gs.dispose();

        File sf = new File("D:\\test", "a1" + "." + "jpg");
        ImageIO.write(spinImage, "jpg", sf); // 保存图片
//        binaryImage(spinImage);
//        grayImage(spinImage);

//        BufferedImage grayImage =
//                new BufferedImage(spinImage.getWidth(),
//                        spinImage.getHeight(),
//                        spinImage.getType());
//
//
//        for (int i = 0; i < spinImage.getWidth(); i++) {
//            for (int j = 0; j < spinImage.getHeight(); j++) {
//                final int color = spinImage.getRGB(i, j);
//                final int r = (color >> 16) & 0xff;
//                final int g = (color >> 8) & 0xff;
//                final int b = color & 0xff;
//                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);;
//                System.out.println(i + " : " + j + " " + gray);
//                int newPixel = colorToRGB(255, gray, gray, gray);
//                grayImage.setRGB(i, j, newPixel);
//            }
//        }
//        ImageIO.write(grayImage, "jpg", sf);

        return true;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }
}
