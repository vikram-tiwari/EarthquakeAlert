package com.vik.myvibratealert;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class Conprovider extends ContentProvider {
	Context c;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Context c=getContext();
		Cursor cu;
		
		SQLiteDatabase sb=c.openOrCreateDatabase("alertcontact3",c.MODE_WORLD_WRITEABLE,null);
		 cu=sb.rawQuery("select * from specialcontacts3",null);
		 return cu;
		 
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
