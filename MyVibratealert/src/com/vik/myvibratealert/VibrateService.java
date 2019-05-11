package com.vik.myvibratealert;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class VibrateService extends Service implements SensorEventListener,OnCompletionListener {
SensorManager sm;
Sensor sensor;
 int a,b,c;
 NotificationManager nm;
 Notification notification;
static int con=0;
 int minx=0,miny=0,minz=0,maxx=0,maxy=0,maxz=0;
MediaPlayer mp;
SharedPreferences pref;
RingtoneManager rm;
Uri uri;
Ringtone r;
int n,flag=0;
String s1,s2;
boolean statement=false;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sensor=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		con=0;
		flag=0;
		minx=0;
		miny=0;
		minz=0;
		maxx=0;
		maxy=0;
		maxz=0;
		mp=MediaPlayer.create(VibrateService.this, R.raw.earthquake);
		nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notification=new Notification(android.R.drawable.stat_notify_error,"Alert",System.currentTimeMillis());
		rm=new RingtoneManager(VibrateService.this);
		rm.setType(RingtoneManager.TYPE_RINGTONE);
		 n=rm.getCursor().getCount();
		 pref=getSharedPreferences("play", MODE_PRIVATE);
			 s1=pref.getString("id","");
			 for(int i=0;i<n;i++){
					uri=rm.getRingtoneUri(i);
					s2=uri.toString();
					if(s2.equals(s1)){
						statement=true;
					break;}
					}
			 r=rm.getRingtone(VibrateService.this,uri);
		
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		sm.registerListener(this, sensor,sm.SENSOR_DELAY_NORMAL);
		Toast.makeText(VibrateService.this, "start", Toast.LENGTH_LONG).show();
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		
		// TODO Auto-generated method stub
		super.onDestroy();
		sm.unregisterListener(this);
		mp.stop();
		con=0;
		minx=0;
		miny=0;
		minz=0;
		maxx=0;
		maxy=0;
		maxz=0;
		Toast.makeText(VibrateService.this, "stop", Toast.LENGTH_LONG).show();
		flag=0;
		r.stop();
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		 a=(int)event.values[0];
		 b=(int)event.values[1];
		 c=(int)event.values[2];
	    if(minx>a){
	    	minx=a;
	    	 }
	    else if(maxx<a){
	    	maxx=a;
	    }
	    if(miny>b){
	    	miny=b;
	    	 }
	    else if(maxy<b){
	    	maxy=b;
	    }
	    if(minz>c){
	    	minz=c;
	    	 }
	    else if(maxz<c){
	    	maxz=c;
	    }
	     
	    if(maxx+minx==0 && (a==maxx||a==minx)){
	    	if(maxx!=0 && minx!=0){
		    	con++;}
	    }
	    if(maxy+miny==0 && (b==maxy||b==miny)){
	    	if(maxy!=0 && miny!=0){
		    	con++;}	
	    }
	    
	   if (maxz+minz==0 && (c==maxz||c==minz)){
	    	if(maxz!=0 && minz!=0){
		    	con++;}
	    }
	    
		if(con>3){
			if(con==4 && flag==0){
				notification.defaults=Notification.DEFAULT_ALL;
				Intent intent1=new Intent(VibrateService.this,Myvibratealert.class);
				PendingIntent pi=PendingIntent.getActivity(VibrateService.this,0, intent1, 0);
	            CharSequence msg1="Unreasonable vibration";
	            CharSequence msg2="It may be earthquake";
	            notification.setLatestEventInfo(VibrateService.this, msg1, msg2, pi);
				nm.notify(0, notification);
				flag=1;
				
			}
			startAlertOnVibration();
		}
	    
		 
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	public void startAlertOnVibration(){
		
		
		if(statement)
		{
			if(!r.isPlaying())
			r.play();
		}
		
		else if(!mp.isLooping()){
			mp.start();
		}

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.release();
		if(!mp.isLooping()||!mp.isPlaying()){
			mp.start();
		}
	}
	

}
