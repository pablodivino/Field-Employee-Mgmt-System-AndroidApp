package com.vigneet.macgray_v010;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vigneet on 21-04-2016.
 */
public class SendSMS extends AsyncTask<String, Void, String>{

    String message=null;
    String contact = null;

    public SendSMS(String contact, String message){
        this.contact = contact;
        this.message = message;
    }

    @Override
    protected String doInBackground(String... params) {
        sendSms();
        return null;
    }


    public String sendSms() {
        try {
            // Construct data
            String user = "username=" + "maildisplayscreen@gmail.com";
            String hash = "&apikey=" + "X2OIY+mFznQ-tkLxEpPztLemKTAV5R1u8uvfDASVcy";
            String message = "&message=" + this.message;
            String sender = "&sender=" + "TXTLCL";
            String numbers = "&numbers=" + "91"+this.contact;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
            String data = user + hash + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }
}
