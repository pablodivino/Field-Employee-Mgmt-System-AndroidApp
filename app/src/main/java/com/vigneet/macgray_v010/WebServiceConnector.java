package com.vigneet.macgray_v010;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vigneet on 09-04-2016.
 */
public class WebServiceConnector extends AsyncTask<String, Void, String>{

    Context context;
    InputStream is = null;
    StringBuilder sb;
    String url;
    String BaseUrl;

    public WebServiceConnector(Context context){
        this.context=context;
        BaseUrl = "http://"+context.getResources().getString(R.string.serverIp)+":4444/MacGray/v0.3.4/AndroidWebServices/";
    }

    public WebServiceConnector(Context context,String serviceName,int empId){
        this.context=context;
        BaseUrl = "http://"+context.getResources().getString(R.string.serverIp)+":4444/MacGray/v0.3.4/AndroidWebServices/";
        this.url = BaseUrl+serviceName+".jsp?filterBy=1&&value=1&&empId="+empId;
    }
    public WebServiceConnector(Context context,String serviceName,String id){
        this.context=context;
        BaseUrl = "http://"+context.getResources().getString(R.string.serverIp)+":4444/MacGray/v0.3.4/AndroidWebServices/";
        this.url = BaseUrl+serviceName+".jsp?id="+id;
    }

    public WebServiceConnector(Context context,String serviceName,int empId,String condition, String value){
        this.context=context;
        BaseUrl = "http://"+context.getResources().getString(R.string.serverIp)+":4444/MacGray/v0.3.4/AndroidWebServices/";
        this.url = BaseUrl+serviceName+".jsp?empId="+empId+"&&filterBy="+condition+"&&value="+value;
    }

    public void registerIMEI(String Contact,String IMEI){
        this.url = BaseUrl+"registerIMEI.jsp?contact="+Contact+"&&IMEI="+IMEI;
    }

    public void UpdateLocation(String empId,String lng,String lat){
        this.url = BaseUrl+"updateEmployeeLocation.jsp?employeeId="+empId+"&&lng="+lng+"&&lat="+lat;
    }

    public void startTaskUpdates(String taskId){
        this.url = BaseUrl+"startTask.jsp?taskId="+taskId;
    }

    public void UpdatePaymentCollection(String taskId,String amount,String comments){
        this.url = BaseUrl+"updatePaymentInfo.jsp?taskId="+taskId+"&&amount="+amount+"&&comments="+comments;
    }

    public void CancelTask(String taskId,String comments){
        this.url = BaseUrl+"cancelTask.jsp?taskId="+taskId+"&&comments="+comments;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(url);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    @Override
    protected void onPostExecute(String result) {

    }

    private String downloadUrl(String myurl) throws IOException{
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
        sb = new StringBuilder();
        sb.append(reader.readLine()+"\n");
        String line="0";
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }
}
