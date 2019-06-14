package com.xinhua.kanban.util;


import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 将图片或文字转换成二维码
 */
public class QRCodeUtil {

    /**
     * 将文字信息添加到二维码当中
     * @param qrData
     * @return
     */
    public static String strQrcode(String qrData){
        try {
            Qrcode qrcode = new Qrcode();
            qrcode.setQrcodeErrorCorrect('M');  //纠错等级（分为L、M、H三个等级）
            qrcode.setQrcodeEncodeMode('B');    //N代表数字，A代表a-Z，B代表其它字符
            qrcode.setQrcodeVersion(7);         //版本

            //设置一下二维码的像素
            int width = 67 + 12 * (7 - 1);
            int height = 67 + 12 * (7 - 1);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //绘图
            Graphics2D gs = bufferedImage.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.setColor(Color.BLACK);
            gs.clearRect(0, 0, width, height);//清除下画板内容

            //设置下偏移量,如果不加偏移量，有时会导致出错。
            int pixoff = 2;

            byte[] d = qrData.getBytes("utf-8");
            if (d.length > 0 && d.length < 120) {
                boolean[][] s = qrcode.calQrcode(d);
                for (int i = 0; i < s.length; i++) {
                    for (int j = 0; j < s.length; j++) {
                        if (s[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            }

            QRCodeDecoder decoder = new QRCodeDecoder();
            String content = new String(decoder.decode(new TwoDimensionCodeImage(bufferedImage)), "utf-8");


            // Image->bufferreImage
            BufferedImage bimg = new BufferedImage(bufferedImage.getWidth(null),
                    bufferedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics g = bimg.createGraphics();
            g.drawImage(bufferedImage, 0, 0, null);
            g.dispose();

            // bufferImage->base64
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bimg, "jpg", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64Img = encoder.encode(outputStream.toByteArray());

            gs.dispose();
            bufferedImage.flush();
            return  "data:image/png;base64," + base64Img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}


class TwoDimensionCodeImage implements QRCodeImage {
    //BufferedImage作用将一幅图片加载到内存中
    BufferedImage bufImg;

    public TwoDimensionCodeImage(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }

    @Override
    public int getWidth() {
        return bufImg.getWidth();   //返回像素宽度
    }

    @Override
    public int getHeight() {
        return bufImg.getHeight();  //返回像素高度
    }

    @Override
    public int getPixel(int i, int i1) {
        return bufImg.getRGB(i, i1);    //得到长宽值，即像素值，i,i1代表像素值
    }
}