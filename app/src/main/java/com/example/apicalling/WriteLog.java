package com.example.apicalling;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class WriteLog {
    private WriteLog() {

    }
    private static WriteLog writeLog;
    private final String filename = "LogFile.txt";

    public static WriteLog getInstance() {
        if (writeLog != null) {
            return writeLog;
        } else {
            return new WriteLog();
        }
    }

    void saveButtonClick(String from, String message) {
        String data = "Button Click ---->  " + getCurrentTime() + ",Activity:" + from + ","+message+ " is Clicked \n\n";
        writeFile(data);
    }

    void saveApiData(String from, String message) {
        String data = "Api ---->  " + getCurrentTime() + ",Activity:" + from + ","+message+"\n\n";
        writeFile(data);
    }

    private String getCurrentTime() {
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private void writeFile(String message) {

        new Asycmanager().execute(message);

    }

    class  Asycmanager extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            File logFile = new File(Environment.getExternalStorageDirectory(), filename);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(strings[0]);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
