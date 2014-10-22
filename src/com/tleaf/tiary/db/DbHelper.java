package com.tleaf.tiary.db;

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

	@Override
	public void onCreate(SQLiteDatabase db) {
//		String sql = "create table diary (diaryno integer primary key autoincrement, " +
//				"isbn text, " +
//				"title text, " +
//				"author text, " +
//				"publisher text, " +
//				"reprice text, " +
//				"image text, " +
//				"regDate text, " +
//				"price text, " +
//				"rating text, " +
//				"univ text, " +
//				"major text, " +
//				"lecture text, " +
//				"professor text, " +
//				"usedYear text, " +
//				"usedTerm text, " +
//				"daelLocation text)";

//		String insertSql1 = "insert into salebook values (" +
//				"null, " +
//				"'123456789', " +
//				"'이산수학', " +
//				"'김승현', " +
//				"'프리렉', " +
//				"'30000', " +
//				"null, " +
//				"2013/12/5, " +
//				"'3000', " +
//				"'중', " +
//				"'동덕여대', " +
//				"'컴퓨터학과', " +
//				"'이산수학', " +
//				"'진혜진', " +
//				"'2011', " +
//				"'1', " +
//				"'잠실')";
//
//		String insertSql2 = "insert into salebook values (" +
//				"null, " +
//				"'123456789101112', " +
//				"'안드로이드 정복하기', " +
//				"'김승현', " +
//				"'프리렉', " +
//				"'30000', " +
//				"null, " +
//				"2013/12/5, " +
//				"'3000', " +
//				"'중', " +
//				"'동덕여대', " +
//				"'컴퓨터학과', " +
//				"'이산수학', " +
//				"'진혜진', " +
//				"'2011', " +
//				"'1', " +
//				"'잠실')";
//
//		db.execSQL(sql);
//		db.execSQL(insertSql1);
//		db.execSQL(insertSql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
