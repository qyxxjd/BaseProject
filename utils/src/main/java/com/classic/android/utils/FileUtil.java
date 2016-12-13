package com.classic.android.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: 文件操作工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public final class FileUtil {

    private static final String CHARSET_NAME = "UTF-8";

    private FileUtil() { }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     */
    public static long getFolderSize(@NonNull File file) throws Exception {
        //Logger.d("获取文件大小 - path:" + file.getPath());
        long           size     = 0;
        File[] fileList = file.listFiles();
        if (!DataUtil.isEmpty(fileList)) {
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath       文件路径
     * @param isDeleteFolder 是否删除目录
     */
    @SuppressWarnings("ResultOfMethodCallIgnored") public static void deleteFolderFile(
            @NonNull String filePath, boolean isDeleteFolder) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) { // 处理目录
                File[] files = file.listFiles();
                if (DataUtil.isEmpty(files)) { return; }
                for (File item : files) {
                    deleteFolderFile(item.getAbsolutePath(), isDeleteFolder);
                }
                if (isDeleteFolder && DataUtil.isEmpty(file.listFiles())) {
                    // 目录下没有文件或者目录，删除
                    file.delete();
                }
            } else {
                file.delete();
            }
        }
    }

    /**
     * 格式化文件大小，以B、K、M、G的格式显示
     */
    public static String formatFileSize(long size) {
        //Logger.d("格式化文件大小前 - size:" + size);
        DecimalFormat df = new DecimalFormat("#.00");
        String        fileSizeString;
        if (size < 1) {
            fileSizeString = "0B";
        } else if (size < SizeUtil.KB_2_BYTE) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < SizeUtil.MB_2_BYTE) {
            fileSizeString = df.format((double) size / SizeUtil.KB_2_BYTE) + "K";
        } else if (size < SizeUtil.GB_2_BYTE) {
            fileSizeString = df.format((double) size / SizeUtil.MB_2_BYTE) + "M";
        } else {
            fileSizeString = df.format((double) size / SizeUtil.GB_2_BYTE) + "G";
        }
        //Logger.d("格式化文件大小后 - size:" + fileSizeString);
        return fileSizeString;
    }

    /**
     * 读文件
     */
    public static byte[] readSDFile(@NonNull String path) {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            fis = new FileInputStream(file);
            int    length       = fis.available();
            byte[] buffer       = new byte[length];
            int    bufferLength = fis.read(buffer);
            return buffer;
        } catch (Exception e) {
            return null;
        } finally {
            CloseUtil.close(fis);
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(@NonNull String path) {
        File file = new File(path);
        return !file.exists() || file.delete();
    }

    /**
     * 扫描指定路径的文件
     */
    public static void fileScan(@NonNull Context context, @NonNull String file) {
        fileScan(context, Uri.fromFile(new File(file)));
    }

    /**
     * 扫描指定uri
     */
    public static void fileScan(@NonNull Context context, @NonNull Uri uri) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    /**
     * 读取二进制数据
     *
     * @param filePath 文件路径
     */
    public static byte[] read(@NonNull String filePath) {
        byte[]              data = null;
        BufferedInputStream in   = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            data = new byte[in.available()];
            int length = in.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(in);
        }
        return data;
    }

    /**
     * 文件是否存在
     */
    public static boolean exists(@NonNull String fileName) {
        return new File(fileName).exists();
    }

    /**
     * 文件是否存在
     */
    public static boolean exists(@NonNull Context context, @NonNull String fileName) {
        return new File(context.getFilesDir(), fileName).exists();
    }

    /**
     * 存储文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名，要在系统内保持唯一
     * @param content  文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(
            @NonNull Context context, @NonNull String fileName, @NonNull String content) {
        return writeFile(context, fileName, content, Context.MODE_PRIVATE);
    }

    /**
     * 存储文本
     *
     * @param mode 读写模式
     *             <pre>
     *             @see Context#MODE_APPEND
     *             @see Context#MODE_PRIVATE
     *             @see Context#MODE_WORLD_READABLE
     *             @see Context#MODE_WORLD_WRITEABLE
     *             </pre>
     */
    public static boolean writeFile(
            @NonNull Context context, @NonNull String fileName, @NonNull String content, int mode) {
        boolean          success = false;
        FileOutputStream fos     = null;
        try {
            fos = context.openFileOutput(fileName, mode);
            byte[] byteContent = content.getBytes(CHARSET_NAME);
            fos.write(byteContent);

            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
        }

        return success;
    }

    /**
     * 存储文本数据
     *
     * @param filePath 文件名，要在系统内保持唯一
     * @param content  文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(@NonNull String filePath, @NonNull String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * 存储文本
     *
     * @param append 是否追加内容
     */
    public static boolean writeFile(
            @NonNull String filePath, @NonNull String content, boolean append) {
        boolean          success = false;
        FileOutputStream fos     = null;
        try {
            fos = new FileOutputStream(filePath, append);
            byte[] byteContent = content.getBytes(CHARSET_NAME);
            fos.write(byteContent);

            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
        }
        return success;
    }

    /**
     * 读取文本数据
     *
     * @param filePath 文件名
     */
    public static String readFile(@NonNull String filePath) {
        return readFile(filePath, CHARSET_NAME);
    }

    /**
     * 读取文本数据
     *
     * @param filePath    文件名
     * @param charsetName 编码格式
     */
    public static String readFile(@NonNull String filePath, @NonNull String charsetName) {
        if (!new File(filePath).exists()) {
            return null;
        }
        FileInputStream fis     = null;
        String          content = null;
        try {
            fis = new FileInputStream(filePath);
            byte[]                buffer            = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            int                   readLength;
            while ((readLength = fis.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, readLength);
            }
            fis.close();
            arrayOutputStream.close();
            content = new String(arrayOutputStream.toByteArray(), charsetName);
        } catch (Exception e) {
            e.printStackTrace();
            content = null;
        } finally {
            CloseUtil.close(fis);
        }
        return content;
    }

    /**
     * 将bitmap存储到SD卡(如果文件已存在，会被替换)
     */
    public static void writeBitmapToSD(@NonNull String path, @NonNull Bitmap bitmap) {

        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                boolean result = file.delete();
            }
            File    parent       = file.getParentFile();
            boolean isDirCreated = parent.exists();
            if (!isDirCreated) {
                isDirCreated = parent.mkdirs();
            }
            if (isDirCreated && file.createNewFile()) {
                fos = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
        }
    }

    /**
     * 复制文件
     *
     * @param srcFile 源文件
     * @param dstFile 目标文件
     */
    public static boolean copy(@NonNull String srcFile, @NonNull String dstFile) {
        FileInputStream  fis = null;
        FileOutputStream fos = null;
        try {

            File    dst          = new File(dstFile);
            boolean isDirCreated = !dst.getParentFile().exists();
            if (!isDirCreated) {
                isDirCreated = dst.getParentFile().mkdirs();
            }

            if (isDirCreated) {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(dstFile);

                byte[] buffer = new byte[1024];
                int    len;
                while ((len = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtil.close(fis);
            CloseUtil.close(fos);
        }
        return true;
    }

    /**
     * 获取文件的md5
     */
    public static String getFileMD5(@NonNull File file) {
        if (!file.isFile()) {
            return null;
        }

        MessageDigest   digest = null;
        FileInputStream in     = null;
        byte[]          buffer = new byte[1024];
        int             len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtil.close(in);
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
