package com.czx.big.common.utils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

public class ZipUtil {
	// 要压缩文件的最大值   
    public final static long maxSize = 100000000;// 这个可以配置到配置文件中 100MB   
    public final static int BUFFERSIZE = 2048 ;   //   
    private static final ThreadLocal currentZipName = new ThreadLocal(); 
    
    /** 
     * 根据输出路径对文件流进行压缩输出 
     * @param fileOutPath 
     * @param mapInputStreams 
     * @throws Exception 
     */  
    public void zip(String fileOutPath, Map mapInputStreams,OutputStream out) throws Exception {  
        
        if (fileOutPath == null || "".equals(fileOutPath)) {  
            return;  
        }  
          
        if (mapInputStreams != null && mapInputStreams.size() > 0) {  
            Set entrys = mapInputStreams.entrySet();  
           // FileOutputStream out = new FileOutputStream(fileOutPath);  
            //压缩专用类   
            ZipOutputStream zip = new ZipOutputStream(out);  
            String fileName = null;  
            InputStream inputStream = null;  
            Iterator it = entrys.iterator();  
            while (it.hasNext()) {  
                Map.Entry entry = (Map.Entry) it.next();  
                fileName = (String) entry.getKey();  
                inputStream = (InputStream) entry.getValue();  
                zip(zip, inputStream, fileName);  
            }  
            zip.close();  
        }
    }  
    /** 
     * 根据输出路径对文件流进行压缩输出 
     * @param mapInputStreams 
     * @throws Exception 
     */  
    public void zip(Map mapInputStreams,OutputStream out) throws Exception {  
          
        if (mapInputStreams != null && mapInputStreams.size() > 0) {  
            Set entrys = mapInputStreams.entrySet();  
           // FileOutputStream out = new FileOutputStream(fileOutPath);  
            //压缩专用类   
            ZipOutputStream zip = new ZipOutputStream(out);  
            String fileName = null;  
            InputStream inputStream = null;  
            Iterator it = entrys.iterator();  
            while (it.hasNext()) {  
                Map.Entry entry = (Map.Entry) it.next();  
                fileName = (String) entry.getKey();  
                inputStream = (InputStream) entry.getValue();  
                zip(zip, inputStream, fileName);  
            }  
            zip.close();  
        }
    }  
    /** 
     * 压缩文件 
     * @param out 
     * @param inputStream 
     * @param fileName 
     * @throws Exception 
     */  
    private void zip(ZipOutputStream out, InputStream inputStream,  
            String fileName) throws Exception {  
          
        //for test   
        /*File file1 = new File("D://Workspace//DocumentUpload//","test.txt"); 
        BufferedOutputStream out1 = new BufferedOutputStream(new FileOutputStream(file1)); 
         
        int b; 
        byte[] buf = new byte[2048]; 
        while((b = inputStream.read(buf)) != -1) { 
            out1.write(buf); 
        } 
 
        //注意，这边三点最好全部都写完整，要不会出问题 
        //将缓冲区中的数据全部写出。 
        out1.flush(); 
        inputStream.close(); 
        out1.close();*/  
        //for test   
          
        //开始写入新的 ZIP 文件条目并将流定位到条目数据的开始处   
        //**********************************************   
        //这边请注意，ZipOutputStream和ZipEntry都请用org.apache.tools.zip包下的类，   
        //这样就可以解决压缩文件名乱码的问题了。   
        out.putNextEntry(new org.apache.tools.zip.ZipEntry(fileName));  
        int b1;  
        byte[] buf1 = new byte[BUFFERSIZE];  
        while ((b1 = inputStream.read(buf1)) != -1) {  
            out.write(buf1, 0, b1);  
        }  
        if (inputStream != null) {  
            inputStream.close();  
            inputStream = null;  
        }  
    }
	
}
