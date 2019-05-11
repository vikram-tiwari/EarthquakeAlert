package com.vik.myvibratealert;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.R.color;
import android.R.string;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Myvibratealert extends Activity implements SensorEventListener {
int a,b,c;
TextView tv;
EditText et1;
Button send;
Switch sw;
Intent i;
SensorManager sm;
Sensor s;
GraphView gv;
LineGraphSeries<DataPoint> series,series1,series2;
int lastX=0,lastY=0,lastZ=0;
RelativeLayout rl;
ContentResolver resolver;
Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myvibratealert);
        rl=(RelativeLayout)findViewById(R.id.rl1);
        i=new Intent(Myvibratealert.this,VibrateService.class);
        sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        s=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        tv=(TextView)findViewById(R.id.textView1);
        et1=(EditText)findViewById(R.id.smsalert);
        send=(Button)findViewById(R.id.send);
        gv=(GraphView)findViewById(R.id.graph);
        series=new LineGraphSeries<DataPoint>();
        series1=new LineGraphSeries<DataPoint>();
        series2=new LineGraphSeries<DataPoint>();
        series1.setColor(Color.GRAY);
        series2.setColor(Color.GREEN);
        gv.addSeries(series);
        gv.addSeries(series1);
        gv.addSeries(series2);
        Viewport viewport=gv.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-10);
        viewport.setMaxY(10);
        viewport.setScrollable(true);
        sw=(Switch)findViewById(R.id.switch1);
        String url="content://com.vik.vibrationealert.earth/specialcontacts3";
	    uri=Uri.parse(url);
		 resolver=getContentResolver();
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 		
 		@Override
 		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
 			// TODO Auto-generated method stub
 			if(isChecked){
 				startService(i);
 				
 			}
 			else{
 				stopService(i);
 		 	rl.setBackgroundColor(Color.WHITE);}
 		}
 	});
        
    }
    @Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		// TODO Auto-generated method stub
    	getMenuInflater().inflate(R.menu.main, menu);
    		return true;
    		
    	}
    @Override
    	public boolean onOptionsItemSelected(MenuItem item) {
    		// TODO Auto-generated method stub
    	int id1=item.getItemId();
    	switch(id1){
    	case R.id.i1:{
    		Intent in1=new Intent(Myvibratealert.this,Alarmtone.class);
    		startActivity(in1);
    		break;
    		
    	}
    	case R.id.i2:{
    		Intent i=new Intent(Myvibratealert.this,Alertcontacts.class);
    		startActivity(i);
    		break;
    	}
    	default: break;
    	}
    		return false;
    	}

@Override
public void onSensorChanged(SensorEvent event) {
	// TODO Auto-generated method stub
	 a=(int)event.values[0];
	 b=(int)event.values[1];
	 c=(int)event.values[2];
	if(VibrateService.con==4){
		rl.setBackgroundColor(Color.RED);
	}
	
}
@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	
}
@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sm.registerListener(Myvibratealert.this, s, sm.SENSOR_DELAY_NORMAL);
		if(VibrateService.con>3){
			sw.setChecked(true);
			rl.setBackgroundColor(Color.RED);
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(long i=0;i<1000000000;i++){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							series.appendData(new DataPoint(lastX++,a),true,50);
							series1.appendData(new DataPoint(lastY++,b),true,50);
							series2.appendData(new DataPoint(lastZ++,c),true,50);
						
						}
					});
					try{
						Thread.sleep(200);
					}
					catch(Exception e){
						
					}
				}
				
			}
		}).start();
			
		
		
	}
@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sm.unregisterListener(Myvibratealert.this, s);
		
	}

public void sendalertinfo(View v){
	Cursor cu=resolver.query(uri, null,null,null,null);
	String sub1=et1.getText().toString();
	while(cu.moveToNext()){
	String mob1=cu.getString(1);
		if(mob1.length()>0 && sub1.length()>0)
			mysms(mob1,sub1);
		
	}
}
public void mysms(String mob1,String sub1) {
	String SENT="SMS_SENT";
	String DEL="SMS_SENT";
PendingIntent sendpi=PendingIntent.getActivity(getApplicationContext(),0, new Intent(SENT), 0);
PendingIntent delpi=PendingIntent.getActivity(getApplicationContext(), 0,new Intent(DEL), 0);
registerReceiver(new BroadcastReceiver() {

@Override
public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	switch(getResultCode()){
	case Activity.RESULT_OK:{
		Toast.makeText(getApplicationContext(), "Message Sent", 3000).show();
		break;
	}
	case SmsManager.RESULT_ERROR_RADIO_OFF:{
		Toast.makeText(getApplicationContext(), "Radio Off", 3000).show();
		break;
	}
	case SmsManager.RESULT_ERROR_GENERIC_FAILURE:{
		Toast.makeText(getApplicationContext(), "No Balance", 3000).show();
		break;
	}
	case SmsManager.RESULT_ERROR_NO_SERVICE:{
		Toast.makeText(getApplicationContext(), "No Network", 3000).show();
		break;
	}
	default:break;
	}
	
}
}, new IntentFilter(SENT));
registerReceiver(new BroadcastReceiver() {

@Override
public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	switch(getResultCode()){
	case Activity.RESULT_OK:{
		Toast.makeText(getApplicationContext(), "Message Delivered", 3000).show();
		break;
	}
	case Activity.RESULT_CANCELED:{
		Toast.makeText(getApplicationContext(), "Delivery Failed", 3000).show();
		break;
	}
	default: break;
	}
	
}
}, new IntentFilter(DEL));
android.telephony.gsm.SmsManager sms=android.telephony.gsm.SmsManager.getDefault();
sms.sendTextMessage(mob1, null, sub1, sendpi, delpi);
}


}
