package weifen.com.common.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhurencong on 2017/9/25.
 * 生成二维码工具类
 */
public class CreateQRCodeUtil {

    /***
     *
     * @param content 生成二维码的内容
     * @param width   二维码的宽
     * @param height  二维码的长度
     * @return
     */

    private static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            //BitMatrix相当于一个二维数组
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    //encode.get(j, i)返回一个布尔值，如果为true，则设置背景为黑色，否则为白色
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            //生成图片
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     *         为生成的二维码添加logo
     * @param qrBitmap  上面生成的二维码
     * @param logoBitmap  传过来logo图片
     * @return
     */
    private static Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {

        //获取两个图片的长宽
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        //根据之前生成的二维码的长宽生成一个空白的Bitmap对象，
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        //把blankBitmap当成一个背景
        Canvas canvas = new Canvas(blankBitmap);
        //将二维码图片放上去，从坐标（0,0）开始，最后一参数设置为null，设置重复模式等等效果
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        //当前的绘制状态保存下来
        canvas.save(Canvas.ALL_SAVE_FLAG);
        //下面对logo图片进行缩放
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        //调用scale进行缩放
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        //把logo放上去
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        //画布恢复原来状态
        canvas.restore();
        return blankBitmap;
    }
}
