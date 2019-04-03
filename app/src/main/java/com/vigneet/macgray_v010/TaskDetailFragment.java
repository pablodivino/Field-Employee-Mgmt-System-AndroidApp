package com.vigneet.macgray_v010;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by Vigneet on 09-04-2016.
 */
public class TaskDetailFragment extends Fragment {

    View fragment;
    String TaskId;
    String response;
    TaskDetails task;

    TaskDetailInteractions taskDetailInteractions;

    public interface TaskDetailInteractions {
        public void call(String contact);
        public void text(String contact);
        public void mail(String[] emailid);
        public void map(String Lat,String Lng);
        public void startTask(TaskDetails taskDetails);
        public void cancelTask();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.TaskId = this.getArguments().getString("TaskId");
        fragment = inflater.inflate(R.layout.task_detail_fragment,container,false);
       // setHasOptionsMenu(true);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TaskDetails taskDetails = updateTaskDetailsView(TaskId);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(taskDetails.getCompanyName());
        actionBar.setSubtitle(taskDetails.getArea()+","+taskDetails.getCity());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            taskDetailInteractions  = (TaskDetailInteractions) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.task,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start_task) {
            taskDetailInteractions.startTask(task);
            return true;
        }
        if (id == R.id.action_cancel_task) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Cancel Task");
            dialog.setTitle("Please specify reason:");

            final EditText comments= new EditText(getActivity());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            lp.setMargins(8,4,8,4);
            comments.setLayoutParams(lp);
            dialog.setView(comments);

            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WebServiceConnector webServiceConnector = new WebServiceConnector(getActivity());
                    webServiceConnector.CancelTask(TaskId,comments.getText().toString());
                    webServiceConnector.execute();
                    taskDetailInteractions.cancelTask();
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public TaskDetails updateTaskDetailsView(String id){
        TaskDetails taskDetails = null;
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                response = new WebServiceConnector(getActivity(),"androidTaskDetailsProvider",TaskId).execute().get();
                JSONStringParser js = new JSONStringParser(response);
                 taskDetails = js.getTaskDetails();
                task = taskDetails;

                if(!(task.getStatus().matches("Complete") || task.getStatus().matches("Cancelled"))){
                    setHasOptionsMenu(true);
                }
                ((TextView)fragment.findViewById(R.id.companyName)).setText(taskDetails.getCompanyName());
                ((TextView)fragment.findViewById(R.id.customerName)).setText(taskDetails.getCompanyName());
                ((TextView)fragment.findViewById(R.id.address)).setText(taskDetails.getAddress());
                ((TextView)fragment.findViewById(R.id.areaCity)).setText(taskDetails.getArea()+","+taskDetails.getCity());
                ((TextView)fragment.findViewById(R.id.contact)).setText(taskDetails.getContact());
                ((TextView)fragment.findViewById(R.id.email)).setText(taskDetails.getEmail());
                ((TextView)fragment.findViewById(R.id.taskType)).setText(taskDetails.getTaskType());
                ((TextView)fragment.findViewById(R.id.subType)).setText(taskDetails.getSubType());
                ((TextView)fragment.findViewById(R.id.scheduledDate)).setText(taskDetails.getScheduledDate());
                ((TextView)fragment.findViewById(R.id.scheduledTime)).setText(taskDetails.getScheduledTime());
                ((TextView)fragment.findViewById(R.id.description)).setText(taskDetails.getDescription());
                ((TextView)fragment.findViewById(R.id.startTime)).setText(taskDetails.getStartTime());
                ((TextView)fragment.findViewById(R.id.endTime)).setText(taskDetails.getEndTime());
                ((TextView)fragment.findViewById(R.id.comments)).setText(taskDetails.getComments());
                ((TextView)fragment.findViewById(R.id.status)).setText(taskDetails.getStatus());

                ImageButton callButton = (ImageButton) fragment.findViewById(R.id.callButton);
                ImageButton textButton = (ImageButton) fragment.findViewById(R.id.textButton);
                ImageButton mailButton = (ImageButton) fragment.findViewById(R.id.mailButton);
                ImageButton mapButton = (ImageButton) fragment.findViewById(R.id.mapButton);

                final TaskDetails finalTaskDetails = taskDetails;
                callButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Do something in response to button click
                        taskDetailInteractions.call(finalTaskDetails.getContact());
//
                    }
                });
                textButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Do something in response to button click
                        taskDetailInteractions.text(finalTaskDetails.getContact());
//
                    }
                });
                mailButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String[] emailArray = new String[1];
                        emailArray[0]= finalTaskDetails.getEmail();
                        taskDetailInteractions.mail(emailArray);
//                        // Do something in response to button click
//

                    }
                });
                mapButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Do something in response to button click
                    taskDetailInteractions.map(finalTaskDetails.getLat(),finalTaskDetails.getLng());
                    }
                });


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(),"No Internet Connection!!",Toast.LENGTH_SHORT).show();
        }
        return taskDetails;
    }
}
