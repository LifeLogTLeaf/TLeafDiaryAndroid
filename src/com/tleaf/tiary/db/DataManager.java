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
	private final String DIARY_TAG = "diary_tag";
	private final String DIARY_FOLDER = "diary_folder";

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


	public boolean insertDiary(Diary diary) { //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("date", diary.getDate());

		Util.ll("insertDiary getDate", diary.getDate());

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

		boolean result_image = insertImageByDiary(diary);
		boolean result_tag = insertTagByDiary(diary);
		boolean result_folder = insertFolderByDiary(diary);

		if (!result_image || !result_tag || !result_folder)
			return false;

		dbHelper.close();
		return true;

	}


	public boolean updateDiary(Diary diary) { //완료
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
			updateImageByDiary(diary);
		if (diary.getTags() != null && diary.getTags().size() != 0) 
			updateTagByDiary(diary);
		if (diary.getFolders() != null && diary.getFolders().size() != 0) 
			updateFolderByDiary(diary);

		dbHelper.close();
		return true;
	}

	public boolean updateImageByDiary(Diary diary) { //완료
		boolean resuslt_delete = deleteImageByDiary(diary.getNo());
		boolean resuslt_insert = insertImageByDiary(diary);
		if (resuslt_delete && resuslt_insert)
			return true;
		else 
			return false;
	}

	public boolean updateTagByDiary(Diary diary) { //완료
		boolean resuslt_delete = deleteDiaryTag(diary.getNo());
		boolean resuslt_insert = insertTagByDiary(diary);
		if (resuslt_delete && resuslt_insert)
			return true;
		else 
			return false;
	}


	public boolean updateFolderByDiary(Diary diary) { //완료
		boolean resuslt_delete = deleteDiaryFolder(diary.getNo());
		boolean resuslt_insert = insertFolderByDiary(diary);
		if (resuslt_delete && resuslt_insert)
			return true;
		else 
			return false;
	}

	public boolean updateFolder() { //완료
		return true;
	}

	public boolean insertImageByDiary(Diary diary) { //완료
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

	public boolean insertTagByDiary(Diary diary) { //완료
		if (diary.getNo() == -1) 
			return false;

		if (diary.getTags() != null && diary.getTags().size() != 0) {
			db = dbHelper.getWritableDatabase();
			for(int i=0; i<diary.getTags().size(); i++) {
				row = new ContentValues();
				Util.ll("row.put diary.getTags().get" + i, diary.getTags().get(i));
				row.put("tag", diary.getTags().get(i));
				long tagno = db.insert(TAG, null, row);
				setDiaryTagRelation(diary.getNo(), tagno); //관계디비 데이터 삽입
			}
			dbHelper.close();
		}
		return true;
	}



	public boolean setDiaryTagRelation(long diaryno, long tagno){ //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("diaryno", diaryno);
		row.put("tagno", tagno);
		db.insert(DIARY_TAG, null, row);
		dbHelper.close();
		return true;

	}
	public boolean insertFolderByDiary(Diary diary) { //밖으로 빼고, return만한다면 //완료
		if (diary.getNo() == -1) 
			return false;

		if (diary.getFolders() != null && diary.getFolders().size() != 0) {
			db = dbHelper.getWritableDatabase();
			for(int i=0; i<diary.getFolders().size(); i++) {
				row = new ContentValues();
				Util.ll("row.put diary.getFolders().get" + i, diary.getFolders().get(i));
				row.put("folder", diary.getFolders().get(i));
				long folderno = db.insert(FOLDER, null, row);
				setDiaryFolderRelation(diary.getNo(), folderno); //관계디비 데이터 삽입
			}
			dbHelper.close();
		}
		return true;
	}

	//빈 폴더 생성의 경우
	public boolean insertFolder(String folder) {  //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("folder", folder);
		db.insert(FOLDER, null, row);
		dbHelper.close();
		return true;
	}

	public boolean setDiaryFolderRelation(long diaryno, long folderno){ //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("diaryno", diaryno);
		row.put("folderno", folderno);
		db.insert(DIARY_FOLDER, null, row);
		dbHelper.close();
		return true;

	}


	public boolean deleteImageByDiary(long diaryno) { //완료
		Util.ll("no", diaryno);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + IMAGE + " where diaryNo ='" + diaryno + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public boolean deleteDiaryTag(long diaryno) { //완료
		Util.ll("no", diaryno);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + DIARY_TAG + "where diaryno='" + diaryno + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public boolean deleteDiaryFolder(long diaryno) { //완료
		Util.ll("no", diaryno);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + DIARY_FOLDER + "where diaryno='" + diaryno + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public boolean deleteDiary(long diaryno) { //완료
		Util.ll("no", diaryno);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String delete_diary = "delete from " + DIARY + "where no='" + diaryno + "'";

		db.execSQL(delete_diary);

		deleteImageByDiary(diaryno);
		deleteDiaryTag(diaryno);
		deleteDiaryFolder(diaryno);
		dbHelper.close();
		return true;
	}

	public ArrayList<Diary> getDiaryList() { //완료
		//		Util.tst(mContext, "getDiaryList()");
		ArrayList<Diary> arItem = new ArrayList<Diary>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from diary order by date desc";
		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			Diary diary = new Diary();
			long diaryno = cursor.getInt(0);
			long date = cursor.getLong(1);
			String title = cursor.getString(2);
			String content = cursor.getString(3);
			String emotion = cursor.getString(4);
			String location = cursor.getString(5);
			String todayWeather = cursor.getString(6);
			float temperature = cursor.getFloat(7);
			float humidity = cursor.getFloat(8);

			ArrayList<String> images = getImages(diaryno);
			ArrayList<String> tags = getTagsByDiaryNo(diaryno);
			ArrayList<String> folders = getFoldersByDiaryNo(diaryno);

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


	public ArrayList<String> getImages(long diaryno) { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + IMAGE + " where diaryno =" + diaryno;
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(1);
			arr.add(item);
		}
		return arr;
	}


	public ArrayList<String> getTagsByDiaryNo(long diaryno) { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 

		ArrayList<Long> tagno = new ArrayList<Long>();
		tagno = getTagNoByDiaryNo(diaryno);
		String sql;
		Cursor cursor;
		String item;

		for (int i=0; i<tagno.size(); i++) {
			sql = "select * from " + TAG + " where tagno =" + tagno.get(i);
			cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext()) {
				item = cursor.getString(1);
				arr.add(item);
			}
		}
		return arr;
	}

	public ArrayList<Long> getTagNoByDiaryNo(long diaryno) { //완료
		ArrayList<Long> arr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + DIARY_TAG + " where diaryno =" + diaryno;
		Cursor cursor = db.rawQuery(sql, null);

		Long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(2);
			arr.add(item);
		}
		return arr;
	}


	public ArrayList<String> getFoldersByDiaryNo(long diaryno) { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 

		ArrayList<Long> tagno = new ArrayList<Long>();
		tagno = getFolderNoByDiaryNo(diaryno);
		String sql;
		Cursor cursor;
		String item;

		for (int i=0; i<tagno.size(); i++) {
			sql = "select * from " + FOLDER + " where tagno =" + tagno.get(i);
			cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext()) {
				item = cursor.getString(1);
				arr.add(item);
			}
		}
		return arr;
	}

	public ArrayList<Long> getFolderNoByDiaryNo(long diaryno) { //완료
		ArrayList<Long> arr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + DIARY_FOLDER + " where diaryno =" + diaryno;
		Cursor cursor = db.rawQuery(sql, null);

		Long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(2);
			arr.add(item);
		}
		return arr;
	}

	//미
	//	private ArrayList<String> getArrayList(long diaryno, String table) {
	//		ArrayList<String> arr = new ArrayList<String>();
	//
	//		db = dbHelper.getReadableDatabase(); 
	//		String sql;
	//
	//		if (table.equals(IMAGE)) {
	//			sql = "select * from " + table + " where diaryno =" + diaryno;
	//		} else {
	//			sql = "select distinct * from " + table + " where diaryno =" + diaryno;
	//		}
	//		Cursor cursor = db.rawQuery(sql, null);
	//
	//		String item;
	//		while(cursor.moveToNext()) {
	//			item = cursor.getString(1);
	//			arr.add(item);
	//		}
	//		return arr;
	//	}

	public ArrayList<String> getDistinctTagList() { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select distinct * from " + TAG;
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(1);
			arr.add(item);
		}
		return arr;
	}

	public ArrayList<String> getDistinctFolderList() { //완료
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




	public int getEmotionCount(String emotion) {

		//		DB = DBHELPER.GETREADABLEDATABASE(); 
		//		STRING SQL = "SELECT * FROM " + FOLDER;
		//		CURSOR CURSOR = DB.RAWQUERY(SQL, NULL);
		//
		//		STRING ITEM;
		//		WHILE(CURSOR.MOVETONEXT()) {
		//			ITEM = CURSOR.GETSTRING(1);
		//			ARR.ADD(ITEM);
		//		}
		return 0;
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



//publicString convertedStr;

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

