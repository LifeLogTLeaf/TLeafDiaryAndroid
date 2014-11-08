package com.tleaf.tiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tleaf.tiary.R;

/*
 * 작성자: 최슬기
 * 기능: 앱의 db를 초기화 하는 클래스이다. 
 * 
 * SQLiteOpenHelper를 상속받은 클래스이다.
 * 기본 테이블을 생성하고 초기 더미데이터를 넣는다.
 */

public class DbHelper extends SQLiteOpenHelper {

	private Context mContext;
	public DbHelper(Context context) {
		super(context, "DiaryDb.db", null, 1);
		mContext = context;
	}

	//	private long date;
	//	private String title;
	//	private String content;
	//	private String emotion;
	//	private ArrayList<String> images;
	//	private ArrayList<String> tags;
	//	private ArrayList<String> folders;
	//	private String location;
	//	private Weather weather;
	//	
	//	private String todayWeather;
	//	private float temperature;
	//	private float humidity;


	@Override
	public void onCreate(SQLiteDatabase db) {
		String table_diary = "create table diary (no integer primary key autoincrement, " +
				"date integer, " +
				"title text, " +
				"content text, " +
				"emotion text, " +
				"location text, " +
				"todayWeather text, " +
				"temperature real, " +
				"humidity real)";

		String table_image = "create table image (no integer primary key autoincrement, " +
				"image text, " +
				"diaryno integer, " +
				"foreign key(diaryno) references diary(no))";

		//		String table_tag = "create table tag (no integer primary key autoincrement, " +
		//				"tag text, " +
		//				"diaryno integer, " +
		//				"foreign key(diaryno) references diary(no))";

		String table_tag = "create table tag (no integer primary key autoincrement, " +
				"tag text)";

		//		String table_folder = "create table folder (no integer primary key autoincrement, " +
		//				"folder text, " +
		//				"diaryno integer, " +
		//				"foreign key(diaryno) references diary(no))";

		String table_folder = "create table folder (no integer primary key autoincrement, " +
				"folder text)";

		String table_diaryTag = 
				"create table diary_tag (no integer primary key autoincrement, " +
						"diaryno integer, " +
						"tagno integer, " +
						"foreign key(diaryno) references diary(no), " +
						"foreign key(tagno) references tag(no))";

		String table_diaryFolder = 
				"create table diary_folder (no integer primary key autoincrement, " +
						"diaryno integer, " +
						"folderno integer, " +
						"foreign key(diaryno) references diary(no), " +
						"foreign key(folderno) references folder(no))";
		String insert_myfolder = "insert into folder values (0, '" + mContext.getResources().getString(R.string.mydiary) + "')";

		String table_call = "create table call (no integer primary key autoincrement, " +
				"name text, " +
				"number text, " +
				"type text, " +
				"date integer, " +
				"duration integer)";//다시

		
		String table_mylog = "create table mylog (id text primary key autoincrement, " +
				"id text primary key, " +
				"rev text, " +
				"latitude real, " +
				"longitude real, " +
				"date integer, " +
				"type text)";
		
		db.execSQL(table_diary);
		db.execSQL(table_image);
		db.execSQL(table_tag);
		db.execSQL(table_folder);
		db.execSQL(table_diaryTag);

		db.execSQL(table_diaryFolder);
		db.execSQL(insert_myfolder);

		db.execSQL(table_call);
		db.execSQL(table_mylog);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
