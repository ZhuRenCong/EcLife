package weifen.com.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片转二进制
 * Created by zhurencong on 2017/10/16.
 */
public class PitureToBinary {

    public static String pitureToBinary(String imagePath){
        String path = imagePath;
        File file = new File(path);
        FileInputStream fis = null;
        StringBuilder str=null;
        try
        {
            fis = new FileInputStream(file);
            byte[] b;
            b = new byte[fis.available()];
            str = new StringBuilder();// 不建议用String
            fis.read(b);
            for (byte bs : b)
            {
                str.append(Integer.toBinaryString(bs));// 转换为二进制
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str.toString();
    }
}
