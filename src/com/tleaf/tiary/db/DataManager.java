package com.tleaf.tiary.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.Util;

public class DataManager {

	private Context mContext = null;
	private DbHelper dbHelper = null;

	public DataManager(Context context) {
		mContext = context;
		dbHelper = new DbHelper(mContext);
	}

	public boolean insertDiary(Diary diary) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "insert into Diary values (" +
//				"null, " +
//				"'" + diary.getIsbn() + "', " +
//				"'" + diary.getTitle() + "', " +
//				"'" + diary.getAuthor() + "', " +
//				"'" + diary.getPublisher() + "', " +
//				"'" + diary.getRePrice() + "', " +
//				"'" + diary.getImage() + "', " +
//				"'" + diary.getRegDate() + "', " +
//				"'" + diary.getPrice() + "', " +
//				"'" + diary.getRating() + "', " +
//				"'" + diary.getUniv() + "', " +
//				"'" + diary.getMajor() + "', " +
//				"'" + diary.getLecture() + "', " +
//				"'" + diary.getProfessor() + "', " +
//				"'" + diary.getUsedYear() + "', " +
//				"'" + diary.getUsedTerm() + "', " +
//				"'" + diary.getDealLocation() +
				"')";		
		db.execSQL(sql);
		dbHelper.close();
		return true;
	}


	public boolean updateDiary(int diaryNo, Diary diary) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "UPDATE diary SET " +
//				"isbn = '" + diary.getIsbn() + "', " +
//				"title = '" + diary.getTitle() + "', " +
//				"author = '" + diary.getAuthor() + "', " +
//				"publisher = '" + diary.getPublisher() + "', " +
//				"reprice = '" + diary.getRePrice() + "', " +
//				"image = '" + diary.getImage() + "', " +
//				"regDate = '" + diary.getRegDate() + "', " +
//				"price = '" + diary.getPrice() + "', " +
//				"rating = '" + diary.getRating() + "', " +
//				"univ = '" + diary.getUniv() + "', " +
//				"major = '" + diary.getMajor() + "', " +
//				"lecture = '" + diary.getLecture() + "', " +
//				"professor = '" + diary.getProfessor() + "', " +
//				"usedYear = '" + diary.getUsedYear() + "', " +
//				"usedTerm = '" + diary.getUsedTerm() + "', " +
//				"daelLocation = '" + diary.getDealLocation() + "' " +			
				"where diaryno = " + diaryNo;
		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public boolean deleteDiary(int diaryNo) {
		Util.ll("diaryNo", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from diary where diaryNo='" + diaryNo + "'";
		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public ArrayList<Diary> getDiaryList() {
		Util.ll("getDiaryList", "");

		ArrayList<Diary> arItem = new ArrayList<Diary>();
		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
		String sql = "select * from diary";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			Diary diary = new Diary();
//			int diaryno = cursor.getInt(0);
//			String isbn = cursor.getString(1);
//			String title = cursor.getString(2);
//			String author = cursor.getString(3);
//			String publisher = cursor.getString(4);
//			String rePrice = cursor.getString(5);
//			String image = cursor.getString(6);
//			String regDate = cursor.getString(7);
//			String price = cursor.getString(8);
//			String rating = cursor.getString(9);
//			String univ = cursor.getString(10);
//			String major = cursor.getString(11);
//			String lecture = cursor.getString(12);
//			String professor = cursor.getString(13);
//			String usedYear = cursor.getString(14);
//			String usedTerm = cursor.getString(15);
//			String dealLocation = cursor.getString(16);
//
//			Log.e("cursor.getInt(0)", ""+cursor.getInt(0));
//			diary.setDiaryNo(diaryno);
//			diary.setIsbn(isbn);
//			diary.setTitle(title);
//			diary.setAuthor(author);
//			diary.setPublisher(publisher);
//			diary.setRePrice(rePrice);
//			diary.setImage(image);
//			diary.setRegDate(regDate);
//			diary.setPrice(price);
//			diary.setRating(rating);
//			diary.setUniv(univ);
//			diary.setMajor(major);
//			diary.setLecture(lecture);
//			diary.setProfessor(professor);
//			diary.setUsedYear(usedYear);
//			diary.setUsedTerm(usedTerm);
//			diary.setDealLocation(dealLocation);
//			
			arItem.add(diary);
		}

		return arItem;
	}	

//	public ArrayList<Diary> getDiaryListBySearch(String search) {
//		Log.e("getDiaryListBySearch", search);
//
//		ArrayList<Diary> arItem = new ArrayList<Diary>();
//		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
//		String sql = "select * from diary where " +
//				"isbn like '%" + search + "%' or " +
//				"title like '%" + search + "%' or " +
//				"author like '%" + search + "%' or " +
//				"publisher like '%" + search + "%' or " +
//				"univ like '%" + search + "%' or " +
//				"lecture like '%" + search + "%' or " +
//				"professor like '%" + search + "%' or " +
//				"usedYear like '%" + search + "%' or " +
//				"daelLocation like '%" + search + "%'";
//		Log.e("sql", sql);
//
//		Cursor cursor = db.rawQuery(sql, null);
//
//		 while(cursor.moveToNext()) {
//			 Diary diary = new Diary();
//			 int diaryno = cursor.getInt(0);
//			 String isbn = cursor.getString(1);
//			 String title = cursor.getString(2);
//			 String author = cursor.getString(3);
//			 String publisher = cursor.getString(4);
//			 String rePrice = cursor.getString(5);
//			 String image = cursor.getString(6);
//			 String regDate = cursor.getString(7);
//			 String price = cursor.getString(8);
//			 String rating = cursor.getString(9);
//			 String univ = cursor.getString(10);
//			 String major = cursor.getString(11);
//			 String lecture = cursor.getString(12);
//			 String professor = cursor.getString(13);
//			 String usedYear = cursor.getString(14);
//			 String usedTerm = cursor.getString(15);
//			 String dealLocation = cursor.getString(16);
//
//			 Log.e("cursor.getInt(0)", ""+cursor.getInt(0));
//			 diary.setDiaryNo(diaryno);
//			 diary.setIsbn(isbn);
//			 diary.setTitle(title);
//			 diary.setAuthor(author);
//			 diary.setPublisher(publisher);
//			 diary.setRePrice(rePrice);
//			 diary.setImage(image);
//			 diary.setRegDate(regDate);
//			 diary.setPrice(price);
//			 diary.setRating(rating);
//			 diary.setUniv(univ);
//			 diary.setMajor(major);
//			 diary.setLecture(lecture);
//			 diary.setProfessor(professor);
//			 diary.setUsedYear(usedYear);
//			 diary.setUsedTerm(usedTerm);
//			 diary.setDealLocation(dealLocation);
//			 arItem.add(diary);
//		 }
//
//		 return arItem;
//	}	

	public Diary getDiary(int diaryNo) {
		Util.ll("diaryNo", diaryNo);
		Diary diary = new Diary();
	
		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
		String sql = "select * from diary where isbn='" + diaryNo + "'";
		
		Cursor cursor = db.rawQuery(sql, null);
		Log.e("cursor null", ""+cursor);
		while(cursor.moveToNext()) {
//			int diaryno = cursor.getInt(0);
//			String isbn = cursor.getString(1);
//			String title = cursor.getString(2);
//			String author = cursor.getString(3);
//			String publisher = cursor.getString(4);
//			String rePrice = cursor.getString(5);
//			String image = cursor.getString(6);
//			String regDate = cursor.getString(7);
//			String price = cursor.getString(8);
//			String rating = cursor.getString(9);
//			String univ = cursor.getString(10);
//			String major = cursor.getString(11);
//			String lecture = cursor.getString(12);
//			String professor = cursor.getString(13);
//			String usedYear = cursor.getString(14);
//			String usedTerm = cursor.getString(15);
//			String dealLocation = cursor.getString(16);
//
//			diary.setDiaryNo(diaryno);
//			diary.setIsbn(isbn);
//			diary.setTitle(title);
//			diary.setAuthor(author);
//			diary.setPublisher(publisher);
//			diary.setRePrice(rePrice);
//			diary.setImage(image);
//			diary.setRegDate(regDate);
//			diary.setPrice(price);
//			diary.setRating(rating);
//			diary.setUniv(univ);
//			diary.setMajor(major);
//			diary.setLecture(lecture);
//			diary.setProfessor(professor);
//			diary.setUsedYear(usedYear);
//			diary.setUsedTerm(usedTerm);
//			diary.setDealLocation(dealLocation);
		}
		return diary;
	}	

//	public Diary getDiaryByNo(int diaryNo) {
//		Log.e("diaryNo", ""+diaryNo);
//		Diary diary = new Diary();
//
//		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
//		String sql = "select * from diary where diaryno=" + diaryNo;
//
//		Cursor cursor = db.rawQuery(sql, null);
//		Log.e("cursor null", ""+cursor);
//		while(cursor.moveToNext()) {
//			int diaryno = cursor.getInt(0);
//			String isbn = cursor.getString(1);
//			String title = cursor.getString(2);
//			String author = cursor.getString(3);
//			String publisher = cursor.getString(4);
//			String rePrice = cursor.getString(5);
//			String image = cursor.getString(6);
//			String regDate = cursor.getString(7);
//			String price = cursor.getString(8);
//			String rating = cursor.getString(9);
//			String univ = cursor.getString(10);
//			String major = cursor.getString(11);
//			String lecture = cursor.getString(12);
//			String professor = cursor.getString(13);
//			String usedYear = cursor.getString(14);
//			String usedTerm = cursor.getString(15);
//			String dealLocation = cursor.getString(16);
//
//			diary.setDiaryNo(diaryno);
//			diary.setIsbn(isbn);
//			diary.setTitle(title);
//			diary.setAuthor(author);
//			diary.setPublisher(publisher);
//			diary.setRePrice(rePrice);
//			diary.setImage(image);
//			diary.setRegDate(regDate);
//			diary.setPrice(price);
//			diary.setRating(rating);
//			diary.setUniv(univ);
//			diary.setMajor(major);
//			diary.setLecture(lecture);
//			diary.setProfessor(professor);
//			diary.setUsedYear(usedYear);
//			diary.setUsedTerm(usedTerm);
//			diary.setDealLocation(dealLocation);
//		}
//		return diary;
//	}	
}
