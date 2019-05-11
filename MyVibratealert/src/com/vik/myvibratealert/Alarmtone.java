package com.vik.myvibratealert;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Alarmtone extends Activity {
	ListView lv;
	List<Ringtone> ringtone;
	RingtoneManager rm;
	ArrayAdapter<Ringtone> aa;
	SharedPreferences pref;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.alarmtone);
	lv=(ListView)findViewById(R.id.listView1);
	ringtone=new ArrayList<Ringtone>();
    rm=new RingtoneManager(this);
    rm.setType(RingtoneManager.TYPE_RINGTONE);
    aa=new ArrayAdapter<Ringtone>(this, android.R.layout.simple_list_item_1,ringtone);
    int n=rm.getCursor().getCount();
    for(int i=0;i<n;i++){
    ringtone.add(rm.getRingtone(i));
    }
    lv.setAdapter(aa);
    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			pref=getSharedPreferences("play", MODE_PRIVATE);
			Editor ed=pref.edit();
			ed.putString("id",rm.getRingtoneUri(position).toString());
			ed.commit();
			Intent i1=new Intent(Alarmtone.this,Myvibratealert.class);
			startActivity(i1);
		}
	});
    }
    
	
}


