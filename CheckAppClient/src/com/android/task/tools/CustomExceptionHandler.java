package com.android.task.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.os.Environment;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

	
	private UncaughtExceptionHandler defaultUEH;
    private String localPath;
    public CustomExceptionHandler ()
    {
    	localPath      = Environment.getExternalStorageDirectory() +"/";
    	this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    
	public void uncaughtException(Thread thread, Throwable e) {
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + ".stacktrace";

        if (localPath != null) {
            writeToFile(stacktrace, filename);
        }
        defaultUEH.uncaughtException(thread, e);
	}
	private void writeToFile(String stacktrace, String filename) {
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(
                    localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
