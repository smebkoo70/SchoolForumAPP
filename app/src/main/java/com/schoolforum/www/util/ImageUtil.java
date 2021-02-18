package com.schoolforum.www.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtil {

	public static Bitmap compressImage(Bitmap image) {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	        int options = 100;
	        while ( baos.toByteArray().length / 1024>100) {
	            baos.reset();
	            options -= 10;
	            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
	            if(options<=0){
	            	break;
	            }
	        }
	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
	        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
	        return bitmap;
	}
	
	public static Bitmap getimage(String srcPath) {
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();
	        newOpts.inJustDecodeBounds = true;
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);
	        newOpts.inJustDecodeBounds = false;
	        int w = newOpts.outWidth;
	        int h = newOpts.outHeight;
	        float hh = 800f;
	        float ww = 480f;
	        int be = 1;
	        if (w > h && w > ww) {
	            be = (int) (newOpts.outWidth / ww);
	        } else if (w < h && h > hh) {
	            be = (int) (newOpts.outHeight / hh);
	        }
	        if (be <= 0)
	            be = 1;
	        newOpts.inSampleSize = be;
	        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	        return compressImage(bitmap);
	}
	
	
	public static void saveFile(Bitmap bitmap, String takePath){
		File file = new File(takePath);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();  
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	public static Bitmap returnBitMap(String url, String filePath, String fileName) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			try {
				byte[] data=readStream(is);
				if(data!=null){
					bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}						
			//bitmap = BitmapFactory.decodeStream(is);
			if(bitmap!=null){
				saveBitmap(bitmap,filePath,fileName);
			}			
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}	
	

	private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];      
        int len = 0;      
        while( (len=inStream.read(buffer)) != -1){      
            outStream.write(buffer, 0, len);      
        }      
        outStream.close();      
        inStream.close();      
        return outStream.toByteArray();      
    }	
	
	
    public static Bitmap returnBitMap(String url){
    	URL myFileUrl = null;
    	Bitmap bitmap = null;
    	try {  
    		myFileUrl = new URL(url);
    	} catch (MalformedURLException e) {
    		e.printStackTrace();  
    	}  
    	try {  
    		HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
    		conn.setDoInput(true);  
    		conn.connect();  
    		InputStream is = conn.getInputStream();
    		bitmap = BitmapFactory.decodeStream(is);
    		is.close();  
    	} catch (IOException e) {
    		  e.printStackTrace();  
    	}  
    	return bitmap;  
    }	
	
	
 	private static void saveBitmap(Bitmap bitmap, String filePath, String fileName) {
 		  File file = new File(filePath);
 		  if(!file.exists()){
 			 file.mkdirs();  
 		  }
		  File f = new File(filePath, fileName);
		  if (f.exists()) {
			  f.delete();
		  }
		  try {
		   FileOutputStream out = new FileOutputStream(f);
		   bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		   out.flush();
		   out.close();
		  } catch (FileNotFoundException e) {
			  e.printStackTrace();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }

	}	
	
	public static Bitmap getPropThumnail(String filePath){
		Bitmap b = getimage(filePath);
		int w = 240;
		int h = 240;
		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		return thumBitmap;
	}

	/***
	 * 根据文件路径获取Bitmap
	 *
	 * @param context
	 * @param
	 * @return
	 */
	public static Bitmap ReadBitmapByPath(Context context, String filePath,
                                          int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inPurgeable = true;
		options.inInputShareable = true;
		//options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}


}
