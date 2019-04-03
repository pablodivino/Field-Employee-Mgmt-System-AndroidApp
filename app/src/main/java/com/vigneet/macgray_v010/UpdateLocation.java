package com.vigneet.macgray_v010;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;


/**
 * Created by Vigneet on 16-05-2016.
 */
public class UpdateLocation extends BroadcastReceiver {

    String lng;
    String lat;
    LocationManager locationManager;
    Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {

        this.context = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null) {
            lat = Double.toString(location.getLatitude());
            lng = Double.toString(location.getLongitude());

        }else {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
                lat = Double.toString(location.getLatitude());
                lng = Double.toString(location.getLongitude());
            }
        }

        if(location!=null) {

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMEI = telephonyManager.getDeviceId();

            WebServiceConnector webServiceConnector = new WebServiceConnector(context);
            webServiceConnector.UpdateLocation(IMEI, lng, lat);
            webServiceConnector.execute();
        }

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling\
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });
    }
}
