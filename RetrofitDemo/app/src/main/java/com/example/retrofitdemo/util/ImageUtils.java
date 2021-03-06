package com.example.retrofitdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * 图片处理工具类
 * Created by kelvin on 16/4/20.
 */
public class ImageUtils {


    /**
     * 图片的二次采样
     * @param bgimage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,double newHeight) {

        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 删除一条图片Uri
     */
    public static void deleteImageUri(Context context, Uri uri){
        context.getContentResolver().delete(uri, null, null);
    }

    /**
     * 裁剪图片返回
     */
    public static void startPhotoZoom(Context context, Uri uri) {
        int  dp = 500;

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", dp);//输出X方向的像素
        intent.putExtra("outputY", dp);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);//设置为不返回数据
        ((Activity) context).startActivityForResult(intent, 300);
    }

    /**
     * 将uri转成bitmap
     * @param uri
     * @param mContext
     * @return
     */

    public static Bitmap getBitmapFromUri(Uri uri,Context mContext){
        try{
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  将uri 转成 file
     * @param uri
     * @param mContext
     * @return
     */
    public static File getFileUri(Uri uri,Context mContext){
        //将uri路径转成绝对路径
        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor actualimagecursor = ((Activity)mContext).managedQuery(uri,proj,null,null,null);

        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor.getString(actual_image_column_index);

        File file = new File(img_path);
        return file;
    }


    // 根据intent来获取所拍摄的照片
    public static File getPhotos(Intent data) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Dog.i("wc","SD card is not avaiable/writeable right now.");
//            setToast("没有检测到sd卡！！");
            return null;
        }

        Bundle bundle = data.getExtras();
        Bitmap bitmap = bundle.getParcelable("data");

        FileOutputStream b = null;
        String photoName = getPhotoName();  // 获取刚拍照片的地址
        try {
            b = new FileOutputStream(photoName);
            /**
             *   mBitmap.compress 压缩图片
             *
             *  Bitmap.CompressFormat.PNG   图片的格式
             *   100  图片的质量（0-100）
             *   out  文件输出流
             */
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File(photoName);
        return file;
    }

    // 将相册中获取到的照片重新选择路径存储
    public static File getPhotos(File url) {
        Bitmap bitmap= BitmapFactory.decodeFile(url.toString(),getBitmapOption(2)); //将图片的长和宽缩小为原来的1/2
        FileOutputStream b = null;
        String photoName = getPhotoName();  // 获取刚拍照片的地址
        try {
            b = new FileOutputStream(photoName);
            /**
             *   mBitmap.compress 压缩图片
             *
             *  Bitmap.CompressFormat.PNG   图片的格式
             *   100  图片的质量（0-100）
             *   out  文件输出流
             */
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File(photoName);
        return file;

    }

    // 将bitmap选择路径存储
    public static File getPhotos(Bitmap bitmap) {
        FileOutputStream b = null;
        String photoName = getPhotoName();  // 获取刚拍照片的地址
        try {
            b = new FileOutputStream(photoName);
            /**
             *   mBitmap.compress 压缩图片
             *
             *  Bitmap.CompressFormat.PNG   图片的格式
             *   100  图片的质量（0-100）
             *   out  文件输出流
             */
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File(photoName);
        return file;

    }


    /**
     * 根据 路径 得到 file 得到 bitmap
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Bitmap decodeFile(String filePath) {
        Bitmap b = null;
        int IMAGE_MAX_SIZE = 600;

        File f = new File(filePath);
        if (f == null){
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    /**
     * 如果图片过大，可能导致Bitmap对象装不下图片
     * 将图片的长和宽缩小味原来的1/2
     * @param inSampleSize
     * @return
     */
    private static BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    // 封装照片路径
    public static String getPhotoName() {
        File file = new File("/sdcard/Image/");
        if(!file.exists()){
            file.mkdirs();// 创建文件夹
        }
        String  fileName = "/sdcard/Image/"+getPhotoFileName();
        return fileName;
    }

    // 为刚拍出的照片起名
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

}
