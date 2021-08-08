package com.zqh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @Author：zhengqh
 * @date 2021/8/5 17:14
 **/
public class CustomizeLoadClass extends ClassLoader {

    private String path;

    public CustomizeLoadClass(String path) {
        super(null);
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //读取字节
        byte[] data = new byte[0];
        try {
            data = readBytes(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, data, 0, data.length);
    }

    private byte[] readBytes(String name) throws IOException {
        String sourceFilePath = path + name + ".xlass";
        File xlassFile = new File(sourceFilePath);
        InputStream fis = null;
        ByteArrayOutputStream bos = null;
        byte[] bytes = new byte[1024];
        byte[] readBytes = null;
        try {
            bos = new ByteArrayOutputStream();
            fis = new FileInputStream(xlassFile);
            int length = 0;
            while ((length = fis.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }
            byte[] oldBytes = bos.toByteArray();
            readBytes = new byte[oldBytes.length];
            for (int i = 0; i < oldBytes.length; i++) {
                readBytes[i] = (byte) (255 - oldBytes[i]);
            }
            return readBytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return readBytes;
    }

    public static void main(String[] args) {
        String path = "src\\main\\java\\com\\zqh\\";
        CustomizeLoadClass customizeLoadClass = new CustomizeLoadClass(path);
        try {
            Class<?> helloClass = customizeLoadClass.findClass("Hello");
            Method helloMethod = helloClass.getDeclaredMethod("hello");
            Object obj = helloClass.newInstance();
            helloMethod.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
