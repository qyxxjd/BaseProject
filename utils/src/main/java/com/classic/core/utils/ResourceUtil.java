package com.classic.core.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.utils
 *
 * 文件描述: Assets、Raw资源文件操作工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/3 17:26
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public final class ResourceUtil {

    private static final String CHARSET_NAME = "UTF-8";

    private ResourceUtil() { }

    /**
     * 读取Assets下的字符串列表
     */
    public static List<String> getListFromAssets(
            @NonNull Context context, @NonNull String fileName) {
        List<String> fileContent = new ArrayList<>();
        try {
            InputStreamReader in = new InputStreamReader(
                    context.getResources().getAssets().open(fileName), CHARSET_NAME);
            BufferedReader br = new BufferedReader(in);
            String         line;
            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
            br.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Raw下的字符串列表
     */
    public static List<String> getListFromRaw(@NonNull Context context, int resId) {
        List<String>   fileContent = new ArrayList<>();
        BufferedReader reader;
        try {
            InputStreamReader in = new InputStreamReader(
                    context.getResources().openRawResource(resId), CHARSET_NAME);
            reader = new BufferedReader(in);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述：读取Assets目录的文件内容.(默认UTF-8编码)
     *
     * @param context the context
     * @param name    the name
     * @return string
     */
    public static String readAssetsByName(@NonNull Context context, @NonNull String name) {
        return readAssetsByName(context, name, CHARSET_NAME);
    }

    /**
     * 描述：读取Assets目录的文件内容.
     *
     * @param context  the context
     * @param name     the name
     * @param encoding the encoding
     * @return the string
     */
    public static String readAssetsByName(
            @NonNull Context context, @NonNull String name, @NonNull String encoding) {
        String            text        = null;
        InputStreamReader inputReader = null;
        BufferedReader    bufReader   = null;
        try {
            inputReader = new InputStreamReader(context.getAssets().open(name), encoding);
            bufReader = new BufferedReader(inputReader);
            String       line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(encoding), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(bufReader, inputReader);
        }
        return text;
    }

    /**
     * 描述：读取Raw目录的文件内容.(默认UTF-8编码)
     *
     * @param context the context
     * @param id      the id
     * @return string
     */
    public static String readRawByName(@NonNull Context context, int id) {
        return readRawByName(context, id, CHARSET_NAME);
    }

    /**
     * 描述：读取Raw目录的文件内容.
     *
     * @param context  the context
     * @param id       the id
     * @param encoding the encoding
     * @return the string
     */
    public static String readRawByName(@NonNull Context context, int id, @NonNull String encoding) {
        String            text        = null;
        InputStreamReader inputReader = null;
        BufferedReader    bufReader   = null;
        try {
            inputReader = new InputStreamReader(context.getResources().openRawResource(id),
                                                encoding);
            bufReader = new BufferedReader(inputReader);
            String line;
            //noinspection StringBufferMayBeStringBuilder
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(encoding), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(bufReader, inputReader);
        }
        return text;
    }

    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param context  the context
     * @param fileName the file name
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromAsset(@NonNull Context context, @NonNull String fileName) {
        Bitmap bit = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream  is           = assetManager.open(fileName);
            bit = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bit;
    }

    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param context  the context
     * @param fileName the file name
     * @return Drawable 图片
     */
    public static Drawable getDrawableFromAsset(
            @NonNull Context context, @NonNull String fileName) {
        Drawable drawable = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream  is           = assetManager.open(fileName);
            drawable = Drawable.createFromStream(is, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 拷贝Assets目录内容到sd卡目录
     *
     * @param outDir 完整sd卡路径
     */
    public static void copyAssets2SD(
            @NonNull Context context, @NonNull String assetDir, @NonNull String outDir) {
        String[]     files;
        InputStream  is = null;
        OutputStream os = null;
        try {
            files = context.getAssets().list(assetDir);
            File    outDirFile   = new File(outDir);
            boolean isDirCreated = false;
            if (!outDirFile.exists()) {
                isDirCreated = outDirFile.mkdirs();
            }
            if (!isDirCreated) {
                return;
            }

            for (String fileName : files) {
                String[] filesChild = context.getAssets().list(fileName);
                if (filesChild != null && filesChild.length > 0) {
                    copyAssets2SD(context, fileName, outDir + "/" + fileName);
                } else {
                    if (!TextUtils.isEmpty(assetDir)) {
                        is = context.getAssets().open(assetDir + "/" + fileName);
                    } else {
                        is = context.getAssets().open(fileName);
                    }
                    File outFile = new File(outDir + "/" + fileName);
                    if (outFile.exists()) {
                        boolean result = outFile.delete();
                    }
                    if (outFile.createNewFile()) {
                        os = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];
                        int    len;
                        while ((len = is.read(buf)) > 0) {
                            os.write(buf, 0, len);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(is);
            CloseUtil.close(os);
        }
    }
}
