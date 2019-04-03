package com.vigneet.macgray_v010;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * Created by Vigneet on 12-05-2016.
 */
public class FoodBillsFragment extends Fragment {

    ImageView imageView;
    String imageFileName;
    EditText cost;
    Bitmap scaledImage;
    private static final int CAMERA_REQUEST = 1829;
    View fragment;
    FragmentActivity activity;
    Button dateButton;
    ImageButton uploadButton;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM, yyyy");
    String pictureImagePath="" ;
    Calendar date = Calendar.getInstance();
    Boolean imgFlag = false;
    String employeeId;
    String ImagePath = "";
    String uploadedfileName = "";
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.employeeId = this.getArguments().getString("EmployeeId");
        activity = (FragmentActivity) this.getActivity();
        fragment =  inflater.inflate(R.layout.food_bill,container,false);

        return fragment;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Upload Food Bill");
        actionBar.setSubtitle("");

        dateButton= (Button) fragment.findViewById(R.id.DateButton);
        imageView = (ImageView) fragment.findViewById(R.id.imageView);
        uploadButton = (ImageButton) fragment.findViewById(R.id.imageUpload);
        cost = (EditText) fragment.findViewById(R.id.Cost);
        relativeLayout  = (RelativeLayout) fragment.findViewById(R.id.food_bill_layout);

        dateButton.setText(dateFormat.format(new Date()));
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        dateButton.setText(dateFormat.format(newDate.getTime()));
                        date = newDate;
                    }
                },date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStorageDirectory();
                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(pictureImagePath);
                Uri outputFileUri = Uri.fromFile(file);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) fragment.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cost.getText().toString()!= null && imgFlag){

                    progressDialog = ProgressDialog.show(activity,"Uploading...",null,true);
                    snackbar = Snackbar.make(relativeLayout,"Upload...",Snackbar.LENGTH_LONG);
                    snackbar.show();

                    String empId= employeeId;
                    String Date = new SimpleDateFormat("yyyy-M-d").format(date.getTime());
                    String billamount = cost.getText().toString();

                    Map<String,String> postData = new HashMap<String, String>();
                    postData.put("employee_id",empId);
                    postData.put("date",Date);
                    postData.put("cost",billamount);
                    postData.put("URL",uploadedfileName);
                    File file = new File(ImagePath);
                    UploadwithImage uploadImage = new UploadwithImage(activity,"uploadFoodBillImage");
                    uploadImage.singleFileEntityBuilder(postData,file);
                    try {
                        String response = uploadImage.execute().get();
                        String trimResponse = response.trim().replaceAll("(\\r|\\n)", "").trim();
                        if(trimResponse.matches("Upload Successful")){

                            progressDialog.dismiss();
                            snackbar.dismiss();
                            Toast.makeText(activity,"Upload Successful",Toast.LENGTH_LONG).show();
                            activity.onBackPressed();
                        }
                        else {
                            progressDialog.dismiss();
                            snackbar.dismiss();
                            Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();


                        }
                    } catch (InterruptedException e) {
                        progressDialog.dismiss();
                        snackbar.dismiss();
                        Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                    } catch (ExecutionException e) {
                        progressDialog.dismiss();
                        snackbar.dismiss();
                        Toast.makeText(activity,"Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(getActivity(),"Please enter all the details.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }





    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Toast.makeText(activity,photo.getWidth()+"X"+photo.getHeight(),Toast.LENGTH_SHORT).show();
//            imageView.setImageBitmap(photo);
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                Bitmap photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //Toast.makeText(activity,imgFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
//                try {
//                    MediaStore.Images.Media.insertImage(activity.getContentResolver(),imgFile.getAbsolutePath(),imgFile.getName(),imgFile.getName());
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                if(photo.getWidth()>800) {
                    scaledImage = Bitmap.createScaledBitmap(photo, 1600, (1600 * photo.getHeight() / photo.getWidth()), true);
                }else {
                    scaledImage = photo;
                }


                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String iFileName = timeStamp + "Compressed.jpg";
                File storageDir = Environment.getExternalStorageDirectory();
                ImagePath = storageDir.getAbsolutePath() + "/" + iFileName;
                uploadedfileName = iFileName;
                File file = new File(ImagePath);
                try{
                    FileOutputStream out = new FileOutputStream(file);

                    scaledImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

                }catch (Exception e){

                }
//                Toast.makeText(activity,photo.getWidth()+"X"+photo.getHeight()+"Size:"+photo.getAllocationByteCount()+"New Size:"+scaledImage.getAllocationByteCount(),Toast.LENGTH_LONG).show();
                imageView.setImageBitmap(scaledImage);
                imgFlag = true;
            }
        }
    }
}
