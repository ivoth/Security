package cn.hzu.mobile.security.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class StreamUtil {
    public static String streamToString(InputStream is) {
        //1,在读取的过程中,将读取的内容存储值缓存中,然后一次性的转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2,读流操作,读到没有为止(循环)
        byte[] buffer = new byte[1024];
        int temp;
        try {
            while ((temp = is.read(buffer)) != -1) {
                bos.write(buffer, 0, temp);
            }
            //返回读取数据
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
