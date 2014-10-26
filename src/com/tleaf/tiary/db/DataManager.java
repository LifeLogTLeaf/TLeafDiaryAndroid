package com.tleaf.tiary.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.Util;

public class DataManager {

	private Context mContext = null;
	private DbHelper dbHelper = null;

	private final String DIARY = "diary";
	private final String IMAGE = "image";
	private final String TAG = "tag";
	private final String FOLDER = "folder";
	
	private SQLiteDatabase db;
	private ContentValues row;
	
	public DataManager(Context context) {
		mContext = context;
		dbHelper = new DbHelper(mContext);
	}

	//	String table_diary = "create table diary (no integer primary key autoincrement, " +
	//			"date integer, " +
	//			"title text, " +
	//			"content text, " +
	//			"emotion text, " +
	//			"images text, " +
	//			"tags text, " +
	//			"folders text, " +
	//			"location text, " +
	//			"todayWeather text, " +
	//			"temperature real, " +
	//			"humidity real)";
	//
	//	String table_image = "create table imgae (no integer primary key autoincrement, " +
	//			"image text, " +
	//			"foreign key(diaryno) references diary(diaryno)";
	//	
	//	String table_tag = "create table tag (no integer primary key autoincrement, " +
	//			"tag text, " +
	//			"foreign key(diaryno) references diary(diaryno)";
	//	
	//	String table_folder = "create table folder (no integer primary key autoincrement, " +
	//			"folder text, " +
	//			"foreign key(diaryno) references diary(diaryno)";

//	String sql = "insert into Diary values (" +
//			"null, " +
//			"'" + diary.getDate() + "', " +
//			"'" + diary.getTitle() + "', " +
//			"'" + diary.getContent() + "', " +
//			"'" + diary.getEmotion() + "', " +
//			"'" + diary.getLocation() + "', " +
//			"'" + diary.getWeather().getTodayWeather() + "', " +
//			"'" + diary.getWeather().getTemperature() + "', " +
//			"'" + diary.getWeather().getHumidity() + "')";
	
	public boolean insertDiary(Diary diary) {
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("date", diary.getDate());
		row.put("title", diary.getTitle());
		row.put("content", diary.getContent());
		row.put("emotion", diary.getEmotion());
		row.put("location", diary.getLocation());
//		row.put("todayWeather", diary.getWeather().getTodayWeather());
//		row.put("temperature", diary.getWeather().getTemperature());
//		row.put("humidity", diary.getWeather().getHumidity());
		long diaryno = db.insert(DIARY, null, row);

		row = new ContentValues();
		if (diary.getImages() != null && diary.getImages().size() != 0) {
			for(int i=0; i<diary.getImages().size(); i++) {
				row.put("image", diary.getImages().get(i));
				row.put("diaryno", diaryno);
			}
		}
		row = new ContentValues();
		if (diary.getTags() != null && diary.getTags().size() != 0) {
			for(int i=0; i<diary.getTags().size(); i++) {
				row.put("tag", diary.getTags().get(i));
				row.put("diaryno", diaryno);
			}
		}
		row = new ContentValues();
		if (diary.getFolders() != null && diary.getFolders().size() != 0) {
			for(int i=0; i<diary.getFolders().size(); i++) {
				row.put("folder", diary.getFolders().get(i));
				row.put("diaryno", diaryno);
			}
		}
	
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


	public ArrayList<String> getFolders() {

		//DataManager dm = new DataManager(mContext); 
		//dm.getFolderList();

		ArrayList<String> arrFolder = new ArrayList<String>();
		//
		arrFolder.add("daily");
		arrFolder.add("다이어");
		arrFolder.add("가계부");

		return arrFolder;
	}

	public ArrayList<Diary> getDiaryListByFolderName(String FolderName) {
		return null;
	}





}



//private String convertedStr;

//private String imgStr;
//private String tagsStr;
//private String folderStr;

//private String covertArrayToString(ArrayList<String> arr) {
//	String str = "";
//	for (int i = 0; i < arr.size(); i++) {
//		str += arr.get(i);
//		if (i != arr.size() - 1)
//			str += ", ";
//	}
//	return str; 
//}

