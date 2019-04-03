
package com.vigneet.macgray_v010;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class AuthActivity extends AppCompatActivity {

    String pin = null;
    String IMEI=null;
    String phoneNum = null;
    Employee employee= null;
    AuthActivity authActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        Intent setAlarmIntent = new Intent(getApplicationContext(),SetAlarm.class);
        PendingIntent pendingSetAlarmIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 4441, setAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY ,pendingSetAlarmIntent);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY, 18);
        Intent cancelAlarmIntent = new Intent(getApplicationContext(),CancelAlarm.class);
        PendingIntent pendingCancelAlarmIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 4442, cancelAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),AlarmManager.INTERVAL_DAY ,pendingCancelAlarmIntent);
        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = mngr.getDeviceId();
        authActivity = this;
        String response = null;
        try {
            response = new WebServiceConnector(this,"employeeAuth",IMEI).execute().get();
            if(response != null) {
                JSONStringParser js = new JSONStringParser(response);
                employee = js.getEmployee();
                if(employee!=null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("employeeId", employee.getId());
                    intent.putExtra("employeeFirstName", employee.getFirst_name());
                    intent.putExtra("employeeLastName", employee.getLast_name());
                    intent.putExtra("empId",employee.getEmpId());

                    startActivity(intent);
                    this.finish();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MacGray");

        final TextView contactNumber = (TextView) findViewById(R.id.contactNumber);
        final TextView verificationNumber = (TextView) findViewById(R.id.verificationNumber);
        final Button contactButton = (Button) findViewById(R.id.contactButton);
        final Button verifyButton = (Button) findViewById(R.id.verifyButton);

        if (contactButton != null) {
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneNum = contactNumber.getText().toString();
                    verificationNumber.setEnabled(true);
                    verifyButton.setEnabled(true);
                    if(phoneNum.length() == 10){
                        try {
                            String r= new WebServiceConnector(authActivity,"setEmployeeVerification",phoneNum).execute().get();
                            if(r != null) {
                                JSONStringParser js = new JSONStringParser(r);
                                pin = js.getString("pin");
                                if(pin!=null) {
                                    if(!pin.matches("404")) {

                                        //new SendSMS(phoneNum,"Your Employee Verification Pin Number is:" + pin).execute();
                                        //SmsManager smsManager = SmsManager.getDefault();
                                        //smsManager.sendTextMessage(phoneNum, null, "Your Employee Verification Pin Number is:" + pin, null, null);

                                    }else{
                                        Toast.makeText(getApplicationContext(),"Contact number does not exist!!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        if (verifyButton != null) {
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String verificationCode = verificationNumber.getText().toString();
                    if(verificationCode.matches(pin)){
                        WebServiceConnector webServiceConnector = new WebServiceConnector(authActivity);
                        webServiceConnector.registerIMEI(phoneNum,IMEI);
                        try {
                           String resp =  webServiceConnector.execute().get();
                            if(resp != null) {
                                JSONStringParser js = new JSONStringParser(resp);
                                employee = js.getEmployee();
                                if(employee!=null) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("employeeId", employee.getId());
                                    intent.putExtra("employeeFirstName", employee.getFirst_name());
                                    intent.putExtra("employeeLastName", employee.getLast_name());
                                    intent.putExtra("empId",employee.getEmpId());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Pin does not match!!",Toast.LENGTH_SHORT);
                    }
                }
            });
        }

    }

}
