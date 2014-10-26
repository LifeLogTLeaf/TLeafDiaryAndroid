package com.tleaf.tiary.db;

import java.util.ArrayList;

import com.tleaf.tiary.model.Weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * 작성자: 최슬기
 * 기능: 앱의 db를 초기화 하는 클래스이다. 
 * 
 * SQLiteOpenHelper를 상속받은 클래스이다.
 * 기본 테이블을 생성하고 초기 더미데이터를 넣는다.
 */

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context) {
		super(context, "DiaryDb.db", null, 1);
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
				"images text, " +
				"tags text, " +
				"folders text, " +
				"location text, " +
				"todayWeather text, " +
				"temperature real, " +
				"humidity real)";

		String table_image = "create table image (no integer primary key autoincrement, " +
				"image text, " +
				"diaryno integer, " +
				"foreign key(diaryno) references diary(no))";
		
		String table_tag = "create table tag (no integer primary key autoincrement, " +
				"tag text, " +
				"diaryno integer, " +
				"foreign key(diaryno) references diary(no))";
		
		String table_folder = "create table folder (no integer primary key autoincrement, " +
				"folder text, " +
				"diaryno integer, " +
				"foreign key(diaryno) references diary(no))";

		db.execSQL(table_diary);
		db.execSQL(table_image);
		db.execSQL(table_tag);
		db.execSQL(table_folder);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	
	
	
//	db.execSQL(insertSql1);
//	db.execSQL(insertSql2);
	
	
//	String insertSql1 = "insert into salebook values (" +
//	"null, " +
//	"'123456789', " +
//	"'이산수학', " +
//	"'김승현', " +
//	"'프리렉', " +
//	"'30000', " +
//	"null, " +
//	"2013/12/5, " +
//	"'3000', " +
//	"'중', " +
//	"'동덕여대', " +
//	"'컴퓨터학과', " +
//	"'이산수학', " +
//	"'진혜진', " +
//	"'2011', " +
//	"'1', " +
//	"'잠실')";
//
//String insertSql2 = "insert into salebook values (" +
//	"null, " +
//	"'123456789101112', " +
//	"'안드로이드 정복하기', " +
//	"'김승현', " +
//	"'프리렉', " +
//	"'30000', " +
//	"null, " +
//	"2013/12/5, " +
//	"'3000', " +
//	"'중', " +
//	"'동덕여대', " +
//	"'컴퓨터학과', " +
//	"'이산수학', " +
//	"'진혜진', " +
//	"'2011', " +
//	"'1', " +
//	"'잠실')";
//
}
