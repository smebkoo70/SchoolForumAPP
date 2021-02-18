package com.schoolforum.www.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static List<String> getFilePaths(String filePath){
		List<String> fileList = new ArrayList<String>();
		File file = new File(filePath);
		File[] files = file.listFiles();
		if(files!=null && files.length!=0){
			for(File f:files){
				if(f.isFile()){
					fileList.add(f.getAbsolutePath());
				}
			}			
		}		
		return fileList;
	}
	
	public static List<File> getFiles(String fileDir){
		List<File> fileList = new ArrayList<File>();
		File file = new File(fileDir);
		if(!file.exists()){
			return fileList;
		}
		File[] files = file.listFiles();
		for(File f:files){
			if(f.isFile()){
				fileList.add(f);
			}
		}		
		return fileList;
	}

	public static String getFileName(String fileDir){
		StringBuffer sb = new StringBuffer();
		File file = new File(fileDir);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(files!=null && files.length>0){
				for(File f:files){
					if(f.isFile()){
						sb.append(f.getName()).append(",");
					}
				}
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	
	public static void createFile(String _path, Bitmap mSignBitmap) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(_path)).write(photoBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	public static boolean isExist(String filePath){
		boolean flag = false;
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		if(file.isDirectory()){
			File[] list = file.listFiles();
			if(list!=null && list.length>0){
				flag = true;
			}
		}		
		return flag;
	}
	
	
	public static void deleteFile(String fpath, String fname){
		File file = new File(fpath,fname);
		if(file.exists()){
			file.delete();
		}		
	}
	
	public static void deleteFile(String fpath){
		File file = new File(fpath);
		if(file!=null){
			if(file.isDirectory()){
				File[] files = file.listFiles();
				if(files!=null && files.length>0){
					for(File f : files){
						if(f.isFile() && f.exists()){
							f.delete();
						}
					}
				}
			}else{
				if(file.exists()){
					file.delete();
				}
			}			
		}
	}
	
	
	
}
