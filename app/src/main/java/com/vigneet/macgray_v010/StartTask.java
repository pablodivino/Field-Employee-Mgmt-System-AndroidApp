package com.vigneet.macgray_v010;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Vigneet on 16-05-2016.
 */
public class StartTask extends Fragment {

    String TaskId,CompanyName,TaskType,ocPictureImagePath,ocScaledImagePath,ocUploadedfileName,mrPictureImagePath,mrScaledImagePath,mrUploadedfileName;
    FragmentActivity activity;
    View fragment;
    int MR_REQUEST = 4231;
    int OC_REQUEST = 6342;
    ImageView imageView;
    Bitmap scaledImage;
    Boolean mrImgFlag;
    EditText amount,comments,reading;

    RelativeLayout relativeLayout;
    Snackbar snackbar;
    TaskCompleted taskCompleted;

    public interface TaskCompleted{
        public void taskComplete();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.TaskId = this.getArguments().getString("TaskId");
        this.CompanyName = this.getArguments().getString("CompanyName");
        this.TaskType = this.getArguments().getString("TaskType");
        this.activity = (FragmentActivity) this.getActivity();
        if(TaskType.matches("Meter Reading")){
            fragment = inflater.inflate(R.layout.start_task_rcp,container,false);
        }else if(TaskType.matches("Payment Collection") || TaskType.matches("Money Withdrawal")){
            fragment = inflater.inflate(R.layout.start_task_ac,container,false);
        }else{
            fragment = inflater.inflate(R.layout.start_task_c,container,false);
        }
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(CompanyName);
        actionBar.setSubtitle(TaskType);
        relativeLayout = (RelativeLayout) fragment.findViewById(R.id.start_task_layout);
        snackbar = Snackbar.make(relativeLayout,"Uploading...",Snackbar.LENGTH_LONG);

        if(TaskType.matches("Meter Reading")){
            reading = (EditText) fragment.findViewById(R.id.reading);
            ImageButton capture = (ImageButton) fragment.findViewById(R.id.cameraButton);
            imageView = (ImageView) fragment.findViewById(R.id.image);
            comments  = (EditText) fragment.findViewById(R.id.comments);
            FloatingActionButton floatingActionButton = (FloatingActionButton) fragment.findViewById(R.id.fab);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reading.getText().toString()!=null && mrImgFlag){
                        snackbar.show();
                        Map<String,String> postData = new HashMap<String, String>();
                        postData.put("task_id",TaskId);
                        postData.put("reading",reading.getText().toString());
                        if(comments.getText().toString()!=null) {
                            postData.put("comments", comments.getText().toString());
                        }
                        postData.put("URL",mrUploadedfileName);

                        File file = new File(mrScaledImagePath);
                        UploadwithImage uploadImage = new UploadwithImage(activity,"uploadTaskDetails");
                        uploadImage.singleFileEntityBuilder(postData,file);
                        try {
                            String response = uploadImage.execute().get();
                            String trimResponse = response.trim().replaceAll("(\\r|\\n)", "").trim();
                            if(trimResponse.matches("Upload Successful")){
                                snackbar.dismiss();
                                taskCompleted.taskComplete();
                            }
                            else {
                                snackbar.dismiss();
                                Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException e) {
                            snackbar.dismiss();
                            Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                        } catch (ExecutionException e) {
                            snackbar.dismiss();
                            Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),"Please enter all the details.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String MeterReadingName = "MR"+timeStamp + ".jpg";
                    File storageDir = Environment.getExternalStorageDirectory();
                    mrPictureImagePath = storageDir.getAbsolutePath() + "/" + MeterReadingName;
                    File file = new File(mrPictureImagePath);
                    Uri outputFileUri = Uri.fromFile(file);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(cameraIntent, MR_REQUEST);
                }
            });

        }else if(TaskType.matches("Payment Collection") || TaskType.matches("Money Withdrawal")){
            amount = (EditText) fragment.findViewById(R.id.amount);
            comments = (EditText) fragment.findViewById(R.id.comments);
            FloatingActionButton floatingActionButton = (FloatingActionButton) fragment.findViewById(R.id.fab);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.show();
                    String collectedAmount = amount.getText().toString();
                    String comment = comments.getText().toString();
                    WebServiceConnector webServiceConnector = new WebServiceConnector(activity);
                    webServiceConnector.UpdatePaymentCollection(TaskId,collectedAmount,comment);
                    webServiceConnector.execute();
                    snackbar.dismiss();
                    taskCompleted.taskComplete();
                }
            });


        }else{
            ImageButton capture = (ImageButton) fragment.findViewById(R.id.capture);
            imageView = (ImageView) fragment.findViewById(R.id.image);
            comments  = (EditText) fragment.findViewById(R.id.comments);
            FloatingActionButton floatingActionButton = (FloatingActionButton) fragment.findViewById(R.id.fab);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(comments.getText().toString()!=null){
                        snackbar.show();
                        Map<String,String> postData = new HashMap<String, String>();
                        postData.put("task_id",TaskId);
                        if(comments.getText().toString()!=null) {
                            postData.put("comments", comments.getText().toString());
                        }
                        postData.put("URL",ocUploadedfileName);

                        File file = new File(ocScaledImagePath);
                        UploadwithImage uploadImage = new UploadwithImage(activity,"uploadTaskDetails");
                        uploadImage.singleFileEntityBuilder(postData,file);
                        try {
                            String response = uploadImage.execute().get();
                            String trimResponse = response.trim().replaceAll("(\\r|\\n)", "").trim();
                            if(trimResponse.matches("Upload Successful")){
                                snackbar.dismiss();
                                taskCompleted.taskComplete();
                            }
                            else {
                                snackbar.dismiss();
                                Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException e) {
                            snackbar.dismiss();
                            Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                        } catch (ExecutionException e) {
                            snackbar.dismiss();
                            Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),"Please enter all the details.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName = timeStamp + ".jpg";
                    File storageDir = Environment.getExternalStorageDirectory();
                    ocPictureImagePath = storageDir.getAbsolutePath() + "/" + fileName;
                    File file = new File(ocPictureImagePath);
                    Uri outputFileUri = Uri.fromFile(file);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(cameraIntent, OC_REQUEST);
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MR_REQUEST && resultCode == Activity.RESULT_OK){
            File imgFile = new  File(mrPictureImagePath);
            if(imgFile.exists()){
                Bitmap photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if(photo.getWidth()>800) {
                    scaledImage = Bitmap.createScaledBitmap(photo, 1600, (1600 * photo.getHeight() / photo.getWidth()), true);
                }else {
                    scaledImage = photo;
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String iFileName = "MR"+timeStamp + "Compressed.jpg";
                File storageDir = Environment.getExternalStorageDirectory();
                mrScaledImagePath = storageDir.getAbsolutePath() + "/" + iFileName;
                mrUploadedfileName = iFileName;
                File file = new File(mrScaledImagePath);
                try{
                    FileOutputStream out = new FileOutputStream(file);
                    scaledImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                }catch (Exception e){
                }
                imageView.setImageBitmap(scaledImage);
                mrImgFlag = true;
            }
        }else if(requestCode==OC_REQUEST && resultCode == Activity.RESULT_OK){
            File imgFile = new  File(ocPictureImagePath);
            if(imgFile.exists()){
                Bitmap photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if(photo.getWidth()>800) {
                    scaledImage = Bitmap.createScaledBitmap(photo, 1600, (1600 * photo.getHeight() / photo.getWidth()), true);
                }else {
                    scaledImage = photo;
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String iFileName = timeStamp + "Compressed.jpg";
                File storageDir = Environment.getExternalStorageDirectory();
                ocScaledImagePath = storageDir.getAbsolutePath() + "/" + iFileName;
                ocUploadedfileName = iFileName;
                File file = new File(ocScaledImagePath);
                try{
                    FileOutputStream out = new FileOutputStream(file);
                    scaledImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                }catch (Exception e){
                }
                imageView.setImageBitmap(scaledImage);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            taskCompleted = (TaskCompleted) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TaskCompleted");
        }
    }
}

