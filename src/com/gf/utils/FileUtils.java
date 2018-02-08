package com.gf.utils;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * Title:文件工具类
 * Packet:com.gh.utils
 * Description:
 * Author:郭富
 * Create Date: 2015/4/27.
 * Modify User:
 * Modify Date:
 * Modify Description:
 */


public class FileUtils {
    /**
     * 创建目录 已做判断处理，不存在则创建
     * @param dir 要创建的路径【递归创建】
     */
    public static void mkdir(String dir) {
        String dirTemp = dir;
        File dirPath = new File(dirTemp);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }
    /**
     * 获取文件MD5值
     *
     * @param file
     * @return
     */
    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value.toUpperCase();
    }
    /**
     * 新建文件
     * @param fileName String 包含路径的文件名 如:E:\phsftp\src\123.txt
     * @param content  String 文件内容
     */
    public static void createNewFile(String fileName, String content)
            throws IOException {
        String fileNameTemp = fileName;
        File filePath = new File(fileNameTemp);
        if (!filePath.exists() ) {
            filePath.createNewFile();
        }
        FileWriter fw = new FileWriter(filePath);
        PrintWriter pw = new PrintWriter(fw);
        String strContent = content;
        pw.println(strContent);
        pw.flush();
        pw.close();
        fw.close();

    }

    /**
     * 删除文件夹
     * @param folderPath 文件夹路径
     */
    public static void delFolder(String folderPath) {
        // 删除文件夹里面所有内容
        delAllFile(folderPath);
        String filePath = folderPath;
        File myFilePath = new File(filePath);
        // 删除空文件夹
        myFilePath.delete();
    }

    /**
     * 删除文件夹里面的所有文件
     * @param path 文件夹路径
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] childFiles = file.list();
        File temp = null;
        for (int i = 0; i < childFiles.length; i++) {
            // File.separator与系统有关的默认名称分隔符
            // 在UNIX系统上，此字段的值为'/'；在Microsoft Windows系统上，它为 '\'。
            if (path.endsWith(File.separator)) {
                temp = new File(path + childFiles[i]);
            } else {
                temp = new File(path + File.separator + childFiles[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separatorChar + childFiles[i]);// 先删除文件夹里面的文件
                delFolder(path + File.separatorChar + childFiles[i]);// 再删除空文件夹
            }
        }
    }


    /**
     * 复制单个文件，传统方式
     * @param srcFile 包含路径的源文件 如：E:/phsftp/src/abc.txt
     * @param dirDest 目标文件目录；若文件目录不存在则自动创建 如：E:/phsftp/dest
     * @throws IOException
     */
    public static void copyFile(String srcFile, String dirDest)
            throws IOException {
        FileInputStream in = new FileInputStream(srcFile);
        mkdir(dirDest);
        FileOutputStream out = new FileOutputStream(dirDest + "/" + new File(srcFile).getName());
        int len;
        byte buffer[] = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();
    }

    /**
     * 复制文件夹
     * @param oldPath String 源文件夹路径 如：E:/phsftp/src
     * @param newPath String 目标文件夹路径 如：E:/phsftp/dest
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath)
            throws IOException {
        // 如果文件夹不存在 则新建文件夹
        mkdir(newPath);
        File file = new File(oldPath);
        String[] files = file.list();
        File temp = null;
        for (int i = 0; i < files.length; i++) {
            if (oldPath.endsWith(File.separator)) {
                temp = new File(oldPath + files[i]);
            } else {
                temp = new File(oldPath + File.separator + files[i]);
            }

            if (temp.isFile()) {
                FileInputStream input = new FileInputStream(temp);
                FileOutputStream output = new FileOutputStream(newPath + "/"
                        + (temp.getName()).toString());
                byte[] buffer = new byte[1024 * 2];
                int len;
                while ((len = input.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
                output.flush();
                output.close();
                input.close();
            }
            if (temp.isDirectory()) {// 如果是子文件夹
                copyFolder(oldPath + "/" + files[i], newPath + "/" + files[i]);
            }
        }
    }



    /**
     * 移动文件到指定目录，不会删除文件夹
     * @param oldPath 源文件目录 如：E:/phsftp/src
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFiles(String oldPath, String newPath)
            throws IOException {
        copyFolder(oldPath, newPath);
        delAllFile(oldPath);
    }

    /**
     * 移动文件到指定目录，会删除文件夹
     * @param oldPath 源文件目录 如：E:/phsftp/src
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFolder(String oldPath, String newPath)
            throws IOException {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }
    /**
     * 检查文件是否存在
     * @param fileName 文件名（包含路径）
     * @return 存在：true   不存在：false
     * @throws IOException
     */
    public static boolean existFile(String fileName)
            throws IOException {
        File file = new File(fileName);
        return file.exists();
    }
    /**
     * A方法追加文件：使用RandomAccessFile
     *
     * @param fileName
     *            文件名（包含路径）
     * @param content
     *            追加的内容
     */
    public static void appendToFile_A(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            content = ParseUtils.parseGBKToISO8859_1(content);//处理，防止中文乱码
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content + "\r\n");
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     *
     * @param fileName 文件名（包含路径）
     * @param content 要追加的内容
     */
    public static void appendToFile_B(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            content = ParseUtils.parseGBKToISO8859_1(content);//处理，防止中文乱码
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content+"\r\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendToFile_C(String fileName, String content) {
        try {
            content = ParseUtils.parseGBKToISO8859_1(content);//处理，防止中文乱码
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content+"\r\n");
            writer.close();

//           FileOutputStream outputStream=new FileOutputStream(new File(fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delFile(String fileName){
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            return;
        }

        file.delete();
    }
}
