package com.vigneet.macgray_v010;

import android.*;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentTransaction;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements RoutingListener,NavigationView.OnNavigationItemSelectedListener,
        TaskListFragment.OnTaskSelectedListener, TaskDetailFragment.TaskDetailInteractions,
        ScheduleFragment.DateChangeInteractions,OnMapReadyCallback, StartTask.TaskCompleted {

    TaskListFragment taskListFragment;
    android.app.FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private GoogleApiClient client;
    private GoogleMap gMap;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
    public LatLng start;
    public LatLng end = new LatLng(23.022505,72.5713621);
    private ArrayList<Polyline> polylines;
    private int[] colors = new int[]{R.color.primary};
    String employeeId, employeeFirstName, employeeLastName,empId;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        employeeId = extras.getString("employeeId");
        employeeFirstName = extras.getString("employeeFirstName");
        employeeLastName = extras.getString("employeeLastName");
        empId = extras.getString("empId");
        getSupportActionBar().setTitle("Tasks");
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (findViewById(R.id.main_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            taskListFragment = new TaskListFragment();
            Bundle args = new Bundle();
            args.putInt("empId",Integer.parseInt(empId));
            taskListFragment.setArguments(args);
//            taskListFragment.setArguments(getIntent().getExtras());

            transaction.add(R.id.main_container, taskListFragment).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView employeeName = (TextView) header.findViewById(R.id.employeeName);
        employeeName.setText(employeeFirstName+" "+employeeLastName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tasks");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.vigneet.macgray_v010/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.vigneet.macgray_v010/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_refresh) {
//            taskListFragment.onActivityCreated(null);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_task) {
            TaskListFragment taskListFragment;
            taskListFragment = (TaskListFragment) getFragmentManager().findFragmentById(R.id.tasklistcontainer);

            if (taskListFragment != null) {
                taskListFragment.updateTaskList(null, null);
            } else {
                TaskListFragment newFragment = new TaskListFragment();
                Bundle args = new Bundle();
                args.putInt("empId",Integer.parseInt(empId));
                newFragment.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else if (id == R.id.nav_completed_tasks) {
            TaskListFragment taskListFragment;
            taskListFragment = (TaskListFragment) getFragmentManager().findFragmentById(R.id.tasklistcontainer);

            if (taskListFragment != null) {
                taskListFragment.updateTaskList("Status", "Complete");
            } else {
                TaskListFragment newFragment = new TaskListFragment();
                Bundle args = new Bundle();
                args.putString("condition", "Status");
                args.putString("value", "Complete");
                args.putInt("empId",Integer.parseInt(empId));
                newFragment.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        } else if (id == R.id.nav_cancelled_tasks) {
            TaskListFragment taskListFragment;
            taskListFragment = (TaskListFragment) getFragmentManager().findFragmentById(R.id.tasklistcontainer);

            if (taskListFragment != null) {
                taskListFragment.updateTaskList("Status", "Cancelled");
            } else {
                TaskListFragment newFragment = new TaskListFragment();
                Bundle args = new Bundle();
                args.putString("condition", "Status");
                args.putString("value", "Cancelled");
                args.putInt("empId",Integer.parseInt(empId));
                newFragment.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }  else if (id == R.id.nav_schedule) {
            ScheduleFragment newFragment = new ScheduleFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_food_bills) {
            FoodBillsFragment newFragment = new FoodBillsFragment();
            Bundle args = new Bundle();
            args.putString("EmployeeId", empId);
            newFragment.setArguments(args);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_Travel_bills) {
            TravelBillsFragment newFragment = new TravelBillsFragment();
            Bundle args = new Bundle();
            args.putString("EmployeeId", empId);
            newFragment.setArguments(args);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTaskSelected(String id) {
        TaskDetailFragment taskDetailFragment;
        taskDetailFragment = (TaskDetailFragment) getFragmentManager().findFragmentById(R.id.taskdetailscontainer);

        if (taskDetailFragment != null) {
            taskDetailFragment.updateTaskDetailsView(id);
        } else {
            TaskDetailFragment newFragment = new TaskDetailFragment();
            Bundle args = new Bundle();
            args.putString("TaskId", id);
            newFragment.setArguments(args);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void call(String contact) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contact));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    @Override
    public void text(String contact) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", contact);
        startActivity(smsIntent);
    }

    @Override
    public void mail(String[] emailid) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, emailid);
        intent.putExtra(Intent.EXTRA_SUBJECT, "MacGray Solutions Pvt Ltd.");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void map(String Lat, String Lng) {
        this.end = new LatLng(Double.parseDouble(Lat),Double.parseDouble(Lng));
        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, mMapFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void startTask(TaskDetails taskDetails) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE,30);
        Intent locationIntent = new Intent(getApplicationContext(),TaskNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 4449, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        WebServiceConnector webServiceConnector = new WebServiceConnector(getApplicationContext());
        webServiceConnector.startTaskUpdates(taskDetails.getTaskId());
        webServiceConnector.execute();

        StartTask newFragment = new StartTask();
        Bundle args = new Bundle();
        args.putString("CompanyName", taskDetails.getCompanyName());
        args.putString("TaskType",taskDetails.getTaskType());
        args.putString("TaskId",taskDetails.getTaskId());
        newFragment.setArguments(args);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void cancelTask() {
        Toast.makeText(getApplicationContext(),"Task Cancelled Successfully!",Toast.LENGTH_SHORT).show();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        gMap.setMyLocationEnabled(true);
        gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                start = ll;
                addPointToViewPort(ll);
                route();
                // we only want to grab the location once, to allow the user to pan and zoom freely.
                gMap.setOnMyLocationChangeListener(null);
            }
        });
    }

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);
        mBounds.include(end);
        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(), 5));
    }

    public void route(){
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(start, end)
                .build();
        routing.execute();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start);
        builder.include(end);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 10);
        gMap.animateCamera(cu);
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        gMap.moveCamera(center);
        if(polylines!=null) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % colors.length;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(colors[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = gMap.addPolyline(polyOptions);
            polylines.add(polyline);
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();

        options.position(end);
        gMap.addMarker(options);
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void dateChanged(String year, String month, String date) {
        TaskListFragment taskListFragment;
        taskListFragment = (TaskListFragment) getFragmentManager().findFragmentById(R.id.tasklistcontainer);

        if (taskListFragment != null) {

            taskListFragment.updateTaskList("scheduled_date", "'"+year+"-"+month+"-"+date+"'");
        } else {
            TaskListFragment newFragment = new TaskListFragment();
            Bundle args = new Bundle();
            args.putString("condition", "scheduled_date");
            args.putString("value", year+"-"+month+"-"+date);
            args.putInt("empId",Integer.parseInt(empId));
            newFragment.setArguments(args);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void taskComplete() {
        Toast.makeText(getApplicationContext(),"Task Details updated successfully!",Toast.LENGTH_SHORT).show();
//        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
//            fragmentManager.popBackStack();
//        }
        Intent locationIntent = new Intent(getApplicationContext(),TaskNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 4449, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
}
