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


		long diaryNo = db.insert(DIARY, null, row);	
		diary.setNo(diaryNo);

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
				row.put("diaryNo", diary.getNo());
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
				long tagNo = db.insert(TAG, null, row);
				setDiaryTagRelation(diary.getNo(), tagNo); //관계디비 데이터 삽입
			}
			dbHelper.close();
		}
		return true;
	}


	public boolean setDiaryTagRelation(long diaryNo, long tagNo){ //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("diaryNo", diaryNo);
		row.put("tagNo", tagNo);
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
				long folderNo;
				if(!isContainedFolder(diary.getFolders().get(i))) {
					row = new ContentValues();
					Util.ll("row.put diary.getFolders().get" + i, diary.getFolders().get(i));
					row.put("folder", diary.getFolders().get(i));
					folderNo = db.insert(FOLDER, null, row);
				} else {
					folderNo = getFolderNo(diary.getFolders().get(i));
				}
				setDiaryFolderRelation(diary.getNo(), folderNo); //관계디비 데이터 삽입
			}
			dbHelper.close();
		}
		return true;
	}

	//빈 폴더 생성의 경우
	public boolean insertFolder(String folder) {  //완료
		if(isContainedFolder(folder)) 
			return false;
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("folder", folder);
		db.insert(FOLDER, null, row);
		dbHelper.close();
		return true;
	}

	private boolean isContainedFolder(String folder) {
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + FOLDER + " where folder = '" + folder + "'";
		Cursor cursor = db.rawQuery(sql, null);

		boolean result = (cursor.getCount() != 0) ? true : false;
		cursor.close();
		dbHelper.close();
		
		return result;
		
	}

	private long getFolderNo(String folder) {
		db = dbHelper.getReadableDatabase(); 
		String sql = "select no from " + FOLDER + " where folder = '" + folder + "'";
		Cursor cursor = db.rawQuery(sql, null);

		cursor.moveToFirst();
		Long folderNo = cursor.getLong(0);
		
		cursor.close();
		dbHelper.close();
		return folderNo;
	}

	public boolean setDiaryFolderRelation(long diaryNo, long folderNo){ //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("diaryNo", diaryNo);
		row.put("folderNo", folderNo);
		db.insert(DIARY_FOLDER, null, row);
		dbHelper.close();
		return true;

	}


	public boolean deleteImageByDiary(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + IMAGE + " where diaryNo ='" + diaryNo + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public boolean deleteDiaryTag(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + DIARY_TAG + " where diaryNo='" + diaryNo + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public boolean deleteDiaryFolder(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + DIARY_FOLDER + " where diaryNo='" + diaryNo + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	public boolean deleteDiary(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String delete_diary = "delete from " + DIARY + " where no='" + diaryNo + "'";

		db.execSQL(delete_diary);

		deleteImageByDiary(diaryNo);
		deleteDiaryTag(diaryNo);
		deleteDiaryFolder(diaryNo);
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
			long diaryNo = cursor.getInt(0);
			long date = cursor.getLong(1);
			String title = cursor.getString(2);
			String content = cursor.getString(3);
			String emotion = cursor.getString(4);
			String location = cursor.getString(5);
			String todayWeather = cursor.getString(6);
			float temperature = cursor.getFloat(7);
			float humidity = cursor.getFloat(8);

			ArrayList<String> images = getImages(diaryNo);
			ArrayList<String> tags = getTagsByDiaryNo(diaryNo);
			ArrayList<String> folders = getFoldersByDiaryNo(diaryNo);

			Log.e("cursor.getInt(0)", ""+cursor.getInt(0));

			diary.setNo(diaryNo);
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
		cursor.close();
		dbHelper.close();
		return arItem;
	}	

	public ArrayList<String> getImages(long diaryNo) { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + IMAGE + " where diaryNo = '" + diaryNo + "'";
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(1);
			arr.add(item);
		}
		
		cursor.close();
		dbHelper.close();
		return arr;
	}

	public ArrayList<String> getTagsByDiaryNo(long diaryNo) { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 

		ArrayList<Long> tagNo = new ArrayList<Long>();
		tagNo = getTagNoByDiaryNo(diaryNo);
		String sql;
		Cursor cursor = null;
		String item;

		for (int i=0; i<tagNo.size(); i++) {
			sql = "select * from " + TAG + " where no = '" + tagNo.get(i) + "'";
			cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext()) {
				item = cursor.getString(1);
				arr.add(item);
			}
			cursor.close();
		}
		dbHelper.close();
		return arr;
	}

	public ArrayList<Long> getTagNoByDiaryNo(long diaryNo) { //완료
		ArrayList<Long> arr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + DIARY_TAG + " where diaryNo = '" + diaryNo + "'";
		Cursor cursor = db.rawQuery(sql, null);

		Long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(2);
			arr.add(item);
		}
		cursor.close();
		dbHelper.close();
		return arr;
	}


	//해당 다이어리가 담긴 폴더들을 다이어리번호로 가져온다
	public ArrayList<String> getFoldersByDiaryNo(long diaryNo) { //완료
		ArrayList<String> arr = new ArrayList<String>();
		Util.ll("db.isOpen()", ""+db.isOpen());
		ArrayList<Long> folderNo = new ArrayList<Long>();
		folderNo = getFolderNoByDiaryNo(diaryNo);

		String sql;
		String item;
		
		db = dbHelper.getReadableDatabase(); 
		for (int i=0; i<folderNo.size(); i++) {
			sql = "select * from " + FOLDER +" where no = '" + folderNo.get(i) + "'";
			Util.ll("sql", sql + ", " + db.isOpen());
			Cursor cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext()) {
				item = cursor.getString(1);
				arr.add(item);
			}
			cursor.close();
		}
		dbHelper.close();
		return arr;
	}

	//다이어리와 폴더의 다대다 관계를 정의해주는 테이블에서 다이어리번호에 해당하는 폴더번호를 가져온다
	public ArrayList<Long> getFolderNoByDiaryNo(long diaryNo) { //완료
		ArrayList<Long> arr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + DIARY_FOLDER + " where diaryNo = '" + diaryNo + "'";
		Cursor cursor = db.rawQuery(sql, null);

		Long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(2);
			arr.add(item);
		}		
		cursor.close();
		dbHelper.close();
		return arr;
	}


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
		cursor.close();
		dbHelper.close();
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
		cursor.close();
		dbHelper.close();
		return arr;
	}


	public Diary getDiaryByNo(Long diaryNo) {
		Util.ll("diaryNo", diaryNo);
		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
		String sql = "select * from diary where no='" + diaryNo + "'";

		Diary diary = null;
		Cursor cursor = db.rawQuery(sql, null);
//		Log.e("cursor null", ""+cursor);
		while(cursor.moveToNext()) {
			diary = new Diary();
			long date = cursor.getLong(1);
			String title = cursor.getString(2);
			String content = cursor.getString(3);
			String emotion = cursor.getString(4);
			String location = cursor.getString(5);
			String todayWeather = cursor.getString(6);
			float temperature = cursor.getFloat(7);
			float humidity = cursor.getFloat(8);

			ArrayList<String> images = getImages(diaryNo);
			ArrayList<String> tags = getTagsByDiaryNo(diaryNo);
			ArrayList<String> folders = getFoldersByDiaryNo(diaryNo);

			Log.e("cursor.getInt(0)", ""+cursor.getInt(0));

			diary.setNo(diaryNo);
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

		}
		cursor.close();
		dbHelper.close();
		return diary;
	}	
	
	private long getFolderNoByFolderName(String folderName) {
		
		ArrayList<Long> arr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select no from " + FOLDER + "where folder = '" + folderName + "'";
		Cursor cursor = db.rawQuery(sql, null);

		long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(0);
			arr.add(item);
		}
		cursor.close();
		dbHelper.close();

		long folderNo = arr.get(0);
		return folderNo;
	}

	private ArrayList<Long> getDiaryNoByFolderNo(long folderNo) {
		ArrayList<Long> arrDiaryNo = new ArrayList<Long>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select diaryno from " + DIARY_FOLDER + "where folderno = '" + folderNo + "'"; //순서지정 order by date desc
		
		Cursor cursor = db.rawQuery(sql, null);
		Long diaryNo;
		while(cursor.moveToNext()) {
			diaryNo = cursor.getLong(1);
			arrDiaryNo.add(diaryNo);
		}
		cursor.close();
		dbHelper.close();
		return arrDiaryNo;
	}


	public ArrayList<Diary> getDiaryListByFolderName(String folderName) {
		db = dbHelper.getReadableDatabase(); 
	
		long folderNo = getFolderNoByFolderName(folderName);
		ArrayList<Long> arrDiaryNo = getDiaryNoByFolderNo(folderNo);
		
		ArrayList<Diary> arItem = new ArrayList<Diary>();
		for(int i=0; i<arrDiaryNo.size(); i++) {
			Diary diary = getDiaryByNo(arrDiaryNo.get(i));
			arItem.add(diary);
		}
		return arItem;

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


	//임
		//	private ArrayList<String> getArrayList(long diaryNo, String table) {
		//		ArrayList<String> arr = new ArrayList<String>();
		//
		//		db = dbHelper.getReadableDatabase(); 
		//		String sql;
		//
		//		if (table.equals(IMAGE)) {
		//			sql = "select * from " + table + " where diaryNo =" + diaryNo;
		//		} else {
		//			sql = "select distinct * from " + table + " where diaryNo =" + diaryNo;
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
	//			 int diaryNo = cursor.getInt(0);
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
	//			 diary.setdiaryNo(diaryNo);
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


//		public Diary getDiaryByNo(long diaryNo) {
	//		Log.e("diaryNo", ""+diaryNo);
	//		Diary diary = new Diary();
	//
	//		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
	//		String sql = "select * from diary where diaryNo=" + diaryNo;
	//
	//		Cursor cursor = db.rawQuery(sql, null);
	//		Log.e("cursor null", ""+cursor);
	//		while(cursor.moveToNext()) {

//			return diary;
//		}	

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

