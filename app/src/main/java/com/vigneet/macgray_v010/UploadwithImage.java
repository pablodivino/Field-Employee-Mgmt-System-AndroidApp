package com.vigneet.macgray_v010;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Vigneet on 13-05-2016.
 */
public class UploadwithImage extends AsyncTask<String , Void , String> {

    Context context;
    String ServiceName;
    HttpEntity entity;

    public UploadwithImage(Context context, String ServiceName){
        this.context=context;
        this.ServiceName = ServiceName;
    }
    @Override
    protected String doInBackground(String... params) {
        return uploadtoServer(ServiceName);
    }

    public void singleFileEntityBuilder(Map<String,String> kvpairs,File file){
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addBinaryBody("file", file);
        for (Map.Entry<String, String> entry : kvpairs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            entityBuilder.addTextBody(key,value);

        }
        entity = entityBuilder.build();
    }


    private String uploadtoServer(String ServiceName) {
        try
        {
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://"+context.getResources().getString(R.string.serverIp)+":4444/MacGray/v0.3.4/AndroidWebServices/"+ServiceName+".jsp");
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            //Toast.makeText(context, EntityUtils.toString(httpEntity),Toast.LENGTH_LONG);
           // Log.v("result", EntityUtils.toString(httpEntity));
            return EntityUtils.toString(httpEntity);
        }
        catch(Exception e)
        {
          //  Toast.makeText(context, e.getMessage(),Toast.LENGTH_LONG);
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
