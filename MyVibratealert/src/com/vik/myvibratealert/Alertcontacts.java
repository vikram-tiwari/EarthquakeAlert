package com.vik.myvibratealert;

import java.util.ArrayList;
import java.util.List;




import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Alertcontacts extends Activity{
	EditText e1,e2;
	Button b,b1;
	ListView lv;
	ArrayAdapter<String>aa;
	List<String> list;
	 SQLiteDatabase sdb;
	int i=0,sup=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alertcontacts);
		e1=(EditText)findViewById(R.id.name);
		e2=(EditText)findViewById(R.id.number);
		b=(Button)findViewById(R.id.save);
		b1=(Button)findViewById(R.id.but1);
		lv=(ListView)findViewById(R.id.alertconlist);
		list=new ArrayList<String>();
		
		sdb=openOrCreateDatabase("alertcontact3",MODE_WORLD_WRITEABLE,null);
		String str="create table specialcontacts3(name varchar(30) Primary Key,num varchar(15))";
		aa=new ArrayAdapter<String>(Alertcontacts.this, android.R.layout.simple_list_item_1,list);
		Cursor dbc=sdb.rawQuery("select * from sqlite_master where type='table'", null);
		while(dbc.moveToNext()){
			if(dbc.getString(1).equals("specialcontacts3")){
				sup=1;
				break;
			}
		}
		if(sup==0){
			sdb.execSQL(str);
		}
		Cursor c=sdb.rawQuery("select name from specialcontacts3",null);
		while(c.moveToNext()){
			if(!c.isNull(0))
			list.add(c.getString(0));
		}
		
		b.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String q1=e1.getText().toString();
				String q2=e2.getText().toString();
				 
				if(!q1.equals(null)&&!q1.equals("")&&!q2.equals(null)&&!q2.equals("")){
					
					sdb.execSQL("insert into specialcontacts3 values('"+q1+"','"+q2+"')");
					e1.setText("");
					e2.setText("");
					i=1;
				Cursor c=sdb.rawQuery("select name from specialcontacts3",null);
					while(c.moveToNext()){
						if(c.isLast())
						list.add(c.getString(0));
					}
					lv.setAdapter(aa);
				}
				
			}
		});
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				sdb.execSQL("delete from specialcontacts3");}
				catch(Exception e){
					
				}
               	list.clear();
              lv.setAdapter(aa);
            	   
               
			}
		});
		
		
		lv.setAdapter(aa);
		
	}

}
