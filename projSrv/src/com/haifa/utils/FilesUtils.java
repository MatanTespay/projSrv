package com.haifa.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.xml.bind.DatatypeConverter;

public class FilesUtils {

	 private static SimpleDateFormat longformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 private static SimpleDateFormat sortformatter = new SimpleDateFormat("yyyy-MM-dd");
	 
	public static String appDirName = "";

	public static boolean writeLocalCopy(String fileName, byte[] bytes, boolean append) {
		boolean res = false;
		FileOutputStream output = null;
		try {

			File root = new File(appDirName);
			if(!root.exists()){
				root.mkdirs();
			}
			
			File file = new File(root,fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			output = new FileOutputStream(file, append);
			output.write(bytes);
			res = true;
			System.out.println("file created ==>" + file.getAbsolutePath());
		} catch (Throwable e) {
			e.printStackTrace();
			res = false;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;

	}



	public static byte[] getLocalCopy(File file) {

		if (!file.exists()) {
			return new byte[0];
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream(4096 * 2);

		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[4096 * 2];
			int n = -1;
			while ((n = in.read(buffer)) != -1) {
				if (n > 0) {
					stream.write(buffer, 0, n);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return stream.toByteArray();
	}

	public static String blobToBase64String(byte[] imgByte){
		String base64 = null;
		
		base64 = DatatypeConverter.printBase64Binary(imgByte);
		
		return base64;
	}

    /**
     * Converts the given java.util.Date to java.sql.Date.
     * @param date The java.util.Date to be converted to java.sql.Date.
     * @return The converted java.sql.Date.
     */
    public static Date toSqlDate(java.util.Date date) {
     return (date != null) ? new Date(date.getTime()) : null;
    }
    
    public static Date getTimeStemp(Date date){
    	
    	return null;
    	
    }
   
    
    public static java.util.Date getDateTimeFromString(String dateString){
    	java.util.Date  date = null;
       // Timestamp ts;
        try {
                if(dateString != null && !dateString.equals(""))
                {
            		 //ts = Timestamp.valueOf(dateString);
                	//date =  java.sql.Date.valueOf(dateString);
                	date = longformatter.parse(dateString);
                }
                
        }catch(Throwable t) {
            t.printStackTrace();
        }
        return  date;
    }
    
    public static java.util.Date getDateFromString(String dateString){
    	java.util.Date  date = null;
       // Timestamp ts;
        try {
                if(dateString != null && !dateString.equals(""))
                {
            		 //ts = Timestamp.valueOf(dateString);
                	//date =  java.sql.Date.valueOf(dateString);
                	date = sortformatter.parse(dateString);
                }
                
        }catch(Throwable t) {
            t.printStackTrace();
        }
        return  date;
    }
    public static String getShortDateString(java.util.Date date){
    	
    	String dateFormatted = sortformatter.format(date);
    	return dateFormatted;
    }
    
    public static String getLongDateString(java.util.Date date){
    	String stringFormatted = longformatter.format(date);
    	return stringFormatted;
    }
    
}
