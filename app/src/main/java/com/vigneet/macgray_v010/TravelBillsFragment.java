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
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
 * Created by Vigneet on 13-05-2016.
 */
public class TravelBillsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String employeeId,pictureImagePath,ImagePath,uploadedfileName,selectedMOT;
    FragmentActivity activity;
    View fragment;
    Spinner MOT;
    Button Date;
    EditText Src,Dest,Cost;
    ImageButton Capture;
    ImageView capturedImage;
    FloatingActionButton floatingActionButton;
    int CAMERA_REQUEST = 1345;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM, yyyy");
    Calendar date = Calendar.getInstance();
    Bitmap scaledImage;
    Boolean imgFlag;
    RelativeLayout relativeLayout;
    Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.employeeId = this.getArguments().getString("EmployeeId");
        activity = (FragmentActivity) this.getActivity();
        fragment =  inflater.inflate(R.layout.travel_bill,container,false);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Upload Travel Bill");
        actionBar.setSubtitle("");

        MOT = (Spinner) fragment.findViewById(R.id.ModeOfTransport);
        Date = (Button) fragment.findViewById(R.id.Date);
        Src =(EditText) fragment.findViewById(R.id.Src);
        Dest =(EditText) fragment.findViewById(R.id.Dest);
        Cost =(EditText) fragment.findViewById(R.id.Cost);
        Capture = (ImageButton) fragment.findViewById(R.id.Capture);
        capturedImage = (ImageView) fragment.findViewById(R.id.capturedImage);
        floatingActionButton = (FloatingActionButton) fragment.findViewById(R.id.fab);
        relativeLayout = (RelativeLayout) fragment.findViewById(R.id.travel_bill_layout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.Mode_of_Transport, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MOT.setAdapter(adapter);
        MOT.setOnItemSelectedListener(this);


        Date.setText(dateFormat.format(new Date()));
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        Date.setText(dateFormat.format(newDate.getTime()));
                        date = newDate;
                    }
                },date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStorageDirectory();
                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(pictureImagePath);
                Uri outputFileUri = Uri.fromFile(file);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Cost.getText().toString()!= null && imgFlag){

                    ProgressDialog progressDialog = new ProgressDialog(activity,ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    snackbar= Snackbar.make(relativeLayout,"Uploading...",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    String empId= employeeId;
                    String Date = new SimpleDateFormat("yyyy-M-d").format(date.getTime());
                    String billamount = Cost.getText().toString();

                    Map<String,String> postData = new HashMap<String, String>();
                    postData.put("employee_id",empId);
                    postData.put("date",Date);
                    postData.put("mode_of_transport",selectedMOT);
                    postData.put("from",Src.getText().toString());
                    postData.put("to",Dest.getText().toString());
                    postData.put("cost",billamount);
                    postData.put("URL",uploadedfileName);
                    File file = new File(ImagePath);
                    UploadwithImage uploadImage = new UploadwithImage(activity,"uploadTravelBillImage");
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
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if (photo.getWidth() > 800) {
                    scaledImage = Bitmap.createScaledBitmap(photo, 1600, (1600 * photo.getHeight() / photo.getWidth()), true);
                } else {
                    scaledImage = photo;
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String iFileName = timeStamp + "Compressed.jpg";
                File storageDir = Environment.getExternalStorageDirectory();
                ImagePath = storageDir.getAbsolutePath() + "/" + iFileName;
                uploadedfileName = iFileName;
                File file = new File(ImagePath);
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    scaledImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                } catch (Exception e) {
                }
                capturedImage.setImageBitmap(scaledImage);
                imgFlag = true;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectedMOT = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
