package com.tleaf.tiary.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.drive.internal.w;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.Weather;
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
	
	
//	row = new ContentValues();
//	if (diary.getImages() != null && diary.getImages().size() != 0) {
//		for(int i=0; i<diary.getImages().size(); i++) {
//			Util.ll("row.put diary.getImages().get" + i, diary.getImages().get(i));
//			row.put("image", diary.getImages().get(i));
//			row.put("diaryno", diaryno);
//		}
//	}
//	db.insert(IMAGE, null, row);
//
//	row = new ContentValues();
//	if (diary.getTags() != null && diary.getTags().size() != 0) {
//		for(int i=0; i<diary.getTags().size(); i++) {
//			Util.ll("row.put diary.getTags().get" + i, diary.getTags().get(i));
//			row.put("tag", diary.getTags().get(i));
//			row.put("diaryno", diaryno);
//		}
//	}
//	db.insert(TAG, null, row);
//	row = new ContentValues();
//	if (diary.getFolders() != null && diary.getFolders().size() != 0) {
//		for(int i=0; i<diary.getFolders().size(); i++) {
//			Util.ll("row.put diary.getFolders().get" + i, diary.getFolders().get(i));
//			row.put("folder", diary.getFolders().get(i));
//			row.put("diaryno", diaryno);
//		}
//	}
//	db.insert(FOLDER, null, row);
	
	
	public boolean insertDiary(Diary diary) {
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("date", diary.getDate());
		row.put("title", diary.getTitle());
		row.put("content", diary.getContent());
		row.put("emotion", diary.getEmotion());
		row.put("location", diary.getLocation());

		if(diary.getWeather() != null) {
			row.put("todayWeather", diary.getWeather().getTodayWeather());
			row.put("temperature", diary.getWeather().getTemperature());
			row.put("humidity", diary.getWeather().getHumidity());
		}

		
		long diaryno = db.insert(DIARY, null, row);	
		diary.setNo(diaryno);
		
		boolean result_image = insertImages(diary);
		boolean result_tag = insertTags(diary);
		boolean result_folder = insertFolders(diary);
		
		if (!result_image || !result_tag || !result_folder)
			return false;

		dbHelper.close();
		return true;

	}


	public boolean updateDiary(Diary diary) {
		db = dbHelper.getWritableDatabase();

		String todayWth = null;
		float temperature = 0;
		float humidity = 0;

		if(diary.getWeather() != null) {
			todayWth = diary.getWeather().getTodayWeather();
			temperature = diary.getWeather().getTemperature();
			humidity = diary.getWeather().getHumidity();
		}

		String update_diary = "update diary set " +
				"date = '" + diary.getDate() + "', " +
				"title = '" + diary.getTitle() + "', " +
				"content = '" + diary.getContent() + "', " +
				"emotion = '" + diary.getEmotion() + "', " +
				"location = '" + diary.getLocation() + "', " +
				"todayWeather = '" + todayWth + "', " +
				"temperature = '" + temperature + "', " +
				"humidity = '" + humidity + "' " +
				"where no = " + diary.getNo();

		db.execSQL(update_diary);
		
		if (diary.getImages() != null && diary.getImages().size() != 0) 
			updateSubTable(diary, IMAGE);
		if (diary.getTags() != null && diary.getTags().size() != 0) 
			updateSubTable(diary, TAG);
		if (diary.getFolders() != null && diary.getFolders().size() != 0) 
			updateSubTable(diary, FOLDER);
		
		dbHelper.close();
		return true;
	}

	private boolean updateSubTable(Diary diary, String table) {
		boolean resuslt_delete = deleteSubTable(diary.getNo(), table);
		boolean resuslt_insert = true;
		if (table.equals(IMAGE))
			resuslt_insert = insertImages(diary);
		else if(table.equals(TAG)) 
			resuslt_insert = insertTags(diary);
		else if(table.equals(FOLDER)) 
			resuslt_insert = insertFolders(diary);

		if (resuslt_delete && resuslt_insert)
			return true;
		else 
			return false;
	}


	private boolean insertImages(Diary diary) {
		if (diary.getNo() == -1) 
			return false;
		if (diary.getImages() != null && diary.getImages().size() != 0) {
			db = dbHelper.getWritableDatabase();
			for(int i=0; i<diary.getImages().size(); i++) {
				row = new ContentValues();
				Util.ll("row.put diary.getImages().get" + i, diary.getImages().get(i));
				row.put("image", diary.getImages().get(i));
				row.put("diaryno", diary.getNo());
				db.insert(IMAGE, null, row);
			}
			dbHelper.close();
		}
		return true;
	}

	private boolean insertTags(Diary diary) {
		if (diary.getNo() == -1) 
			return false;

		if (diary.getTags() != null && diary.getTags().size() != 0) {
			db = dbHelper.getWritableDatabase();
			for(int i=0; i<diary.getTags().size(); i++) {
				row = new ContentValues();
				Util.ll("row.put diary.getTags().get" + i, diary.getTags().get(i));
				row.put("tag", diary.getTags().get(i));
				row.put("diaryno", diary.getNo());
				db.insert(TAG, null, row);
			}
			dbHelper.close();
		}
		return true;
	}

	private boolean insertFolders(Diary diary) { //밖으로 뺴고, return만한다면
		if (diary.getNo() == -1) 
			return false;

		if (diary.getFolders() != null && diary.getFolders().size() != 0) {
			db = dbHelper.getWritableDatabase();
			for(int i=0; i<diary.getFolders().size(); i++) {
				row = new ContentValues();
				Util.ll("row.put diary.getFolders().get" + i, diary.getFolders().get(i));
				row.put("folder", diary.getFolders().get(i));
				row.put("diaryno", diary.getNo());
				db.insert(FOLDER, null, row);
			}
			dbHelper.close();
		}
		return true;
	}


	private boolean deleteSubTable(long diaryno, String table) {
		Util.ll("no", diaryno);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + table + " where diaryNo ='" + diaryno + "'";
		db.execSQL(sql);
		dbHelper.close();
		return true;
	}


	public boolean deleteDiary(long no) {
		Util.ll("no", no);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from diary where no='" + no + "'";
		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	//			"images text, " +
	//			"tags text, " +
	//			"folders text, " +

	//			"todayWeather text, " +
	//			"temperature real, " +
	//			"humidity real)";

	public ArrayList<Diary> getDiaryList() {
		//		Util.tst(mContext, "getDiaryList()");
		ArrayList<Diary> arItem = new ArrayList<Diary>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from diary";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			Diary diary = new Diary();
			long diaryno = cursor.getInt(0);
			long date = cursor.getInt(1);
			String title = cursor.getString(2);
			String content = cursor.getString(3);
			String emotion = cursor.getString(4);
			String location = cursor.getString(5);
			String todayWeather = cursor.getString(6);
			float temperature = cursor.getFloat(7);
			float humidity = cursor.getFloat(8);

			ArrayList<String> images = getArrayList(diaryno, IMAGE);
			ArrayList<String> tags = getArrayList(diaryno, TAG);
			ArrayList<String> folders = getArrayList(diaryno, FOLDER);

			Log.e("cursor.getInt(0)", ""+cursor.getInt(0));

			diary.setNo(diaryno);
			diary.setDate(date);
			diary.setTitle(title);
			diary.setContent(content);
			diary.setEmotion(emotion);
			diary.setLocation(location);

			Weather weather = new Weather();
			weather.setTodayWeather(todayWeather);
			weather.setTemperature(temperature);
			weather.setHumidity(humidity);

			diary.setWeather(weather);
			diary.setImages(images);
			diary.setTags(tags);
			diary.setFolders(folders);

			arItem.add(diary);
		}

		//		Util.tst(mContext, "getDiaryList arItem" + arItem.size());

		return arItem;
	}	

	private ArrayList<String> getArrayList(long diaryno, String table) {
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql;

		if (table.equals(IMAGE)) {
			sql = "select * from " + table + " where diaryno =" + diaryno;
		} else {
			sql = "select distinct * from " + table + " where diaryno =" + diaryno;
		}
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(1);
			arr.add(item);
		}
		return arr;
	}

	public ArrayList<String> getFolders() {
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select distinct * from " + FOLDER;
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(1);
			arr.add(item);
		}
		return arr;
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

