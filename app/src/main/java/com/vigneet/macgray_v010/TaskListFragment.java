package com.vigneet.macgray_v010;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Vigneet on 09-04-2016.
 */
public class TaskListFragment extends ListFragment {

    int empId;
    TaskListAdapter taskListAdapter;
    private List<TaskRowItem> rowItems;
    String response;
    OnTaskSelectedListener onTaskSelectedListener;

    public interface OnTaskSelectedListener {
        public void onTaskSelected(String id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        empId = this.getArguments().getInt("empId");
        return inflater.inflate(R.layout.tasks_list_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Tasks");
        actionBar.setSubtitle(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.getArguments()!= null) {
            updateTaskList(this.getArguments().getString("condition"), this.getArguments().getString("value"));
        }else {
            updateTaskList(null,null);
        }
    }

    public void updateTaskList(String condition, String value){
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                if(condition!= null && value!=null){
                    response = new WebServiceConnector(getActivity(),"androidTaskListProvider",empId,condition,value).execute().get();
                }
                else{
                    response = new WebServiceConnector(getActivity(),"androidTaskListProvider",empId).execute().get();
                }
                JSONStringParser js = new JSONStringParser(response);
                rowItems = js.getTaskRowItemsList();
                if (rowItems!=null) {
                    taskListAdapter = new TaskListAdapter(getActivity(), rowItems);
                    setListAdapter(taskListAdapter);
                }else{
                    Toast.makeText(getActivity(),"No Records Found!!",Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(),"No Internet Connection!!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onTaskSelectedListener  = (OnTaskSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        onTaskSelectedListener.onTaskSelected(rowItems.get(position).getId());
    }
}
