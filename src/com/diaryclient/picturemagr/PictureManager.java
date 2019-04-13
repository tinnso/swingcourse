package com.diaryclient.picturemagr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.diaryclient.datamgr.DBManager;

public class PictureManager {
	
	 // 读取本地图片获取输入流
    public static FileInputStream readImage(String path) throws IOException {
        return new FileInputStream(new File(path));
    }

    // 读取表中图片获取输出流
    public static void readBin2Image(InputStream in, String targetPath) {
        File file = new File(targetPath);
        String path = targetPath.substring(0, targetPath.lastIndexOf("/"));
        if (!file.exists()) {
            new File(path).mkdir();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
    
    public static void readImage2DB(String path, int diaryid) {
        Connection conn = null;
        PreparedStatement ps = null;
        FileInputStream in = null;
        try {
            in = readImage(path);
            conn = DBManager.getconn();
            String sql = "insert into photo (diaryid,name,photo)values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, diaryid);
            
            String fileName = path.substring(path.lastIndexOf("\\")+1);  
            ps.setString(2, fileName);
            ps.setBinaryStream(3, in, in.available());
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("插入成功！");
            } else {
                System.out.println("插入失败！");
            }
            
            ps.close();
            conn.close();
           
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }

    }

    /*
     * @param	targetPath must end with "/"
     */
    public static void readDB2Image(String targetPath, int diaryid) {
        //String targetPath = "D:/image/1.png";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getconn();
            String sql = "select * from photo where diaryid=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, diaryid);
            rs = ps.executeQuery();
            while (rs.next()) {
                InputStream in = rs.getBinaryStream("photo");
                readBin2Image(in, targetPath + rs.getString("name"));
            }
            
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
