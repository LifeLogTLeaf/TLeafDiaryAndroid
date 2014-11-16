package com.tleaf.tiary.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.drive.internal.ar;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.model.BookMark;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.Card;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.model.MyTemplate;
import com.tleaf.tiary.model.TemplateContent;
import com.tleaf.tiary.model.Weather;
import com.tleaf.tiary.util.MyPreference;
import com.tleaf.tiary.util.MyTime;
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

	private final String MYLOG = "mylog";
	private final String CALL = "call";
	private final String SMS = "sms";
	private final String CARD = "card";

	private final String TEMPLATE = "template";
	private final String TEMPLATE_CONTENT = "template_content";

	private SQLiteDatabase db;
	private ContentValues row;

	private MyPreference pref;

	public DataManager(Context context) {
		mContext = context;
		dbHelper = new DbHelper(mContext);
		pref = new MyPreference(mContext);

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
				long tagNo;
				if(!isContainedTag(diary.getTags().get(i))) {
					row = new ContentValues();
					Util.ll("row.put diary.getTags().get" + i, diary.getTags().get(i));
					row.put("folder", diary.getTags().get(i));
					tagNo = db.insert(TAG, null, row);
				} else {
					tagNo = getTagNo(diary.getTags().get(i));
				}
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

	//빈 태그 생성의 경우
	public boolean insertTag(String tag) {  //완료
		if(isContainedTag(tag)) 
			return false;
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("tag", tag);
		db.insert(TAG, null, row);
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

	public boolean isContainedTag(String tag) {
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + TAG + " where tag = '" + tag + "'";
		Cursor cursor = db.rawQuery(sql, null);

		boolean result = (cursor.getCount() != 0) ? true : false;
		cursor.close();
		dbHelper.close();

		return result;
	}

	private long getTagNo(String tag) {
		db = dbHelper.getReadableDatabase(); 
		String sql = "select no from " + TAG + " where tag = '" + tag + "'";
		Cursor cursor = db.rawQuery(sql, null);

		cursor.moveToFirst();
		Long tagNo = cursor.getLong(0);

		cursor.close();
		dbHelper.close();
		return tagNo;
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
			arItem.add(diary);
		}

		//		Util.tst(mContext, "getDiaryList arItem" + arItem.size());
		cursor.close();
		dbHelper.close();

		for (int i = 0; i < arItem.size(); i++) {
			ArrayList<String> images = getImages(arItem.get(i).getNo());
			ArrayList<String> tags = getTagsByDiaryNo(arItem.get(i).getNo());
			ArrayList<String> folders = getFoldersByDiaryNo(arItem.get(i).getNo());
			arItem.get(i).setImages(images);
			arItem.get(i).setTags(tags);
			arItem.get(i).setFolders(folders);
		}
		return arItem;
	}	

	public ArrayList<String> getImages(long diaryNo) { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select image from " + IMAGE + " where diaryNo = '" + diaryNo + "'";
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(0);
			arr.add(item);
		}

		cursor.close();
		dbHelper.close();
		return arr;
	}

	public ArrayList<String> getTagsByDiaryNo(long diaryNo) { //완료
		ArrayList<String> tagArr = new ArrayList<String>();
		ArrayList<Long> tagNo = new ArrayList<Long>();
		tagNo = getTagNoByDiaryNo(diaryNo);
		String sql;
		String item;

		db = dbHelper.getReadableDatabase(); 
		for (int i=0; i<tagNo.size(); i++) {
			sql = "select tag from " + TAG + " where no = '" + tagNo.get(i) + "'";
			Cursor cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext()) {
				item = cursor.getString(0);
				tagArr.add(item);
			}
			cursor.close();
		}
		dbHelper.close();
		return tagArr;
	}

	public ArrayList<Long> getTagNoByDiaryNo(long diaryNo) { //완료
		ArrayList<Long> tagNoArr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select tagno from " + DIARY_TAG + " where diaryNo = '" + diaryNo + "'";
		Cursor cursor = db.rawQuery(sql, null);

		Long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(0);
			tagNoArr.add(item);
		}
		cursor.close();
		dbHelper.close();
		return tagNoArr;
	}


	//해당 다이어리가 담긴 폴더들을 다이어리번호로 가져온다
	public ArrayList<String> getFoldersByDiaryNo(long diaryNo) { //완료
		ArrayList<String> folderArr = new ArrayList<String>();
		Util.ll("db.isOpen()", ""+db.isOpen());

		ArrayList<Long> folderNo = new ArrayList<Long>();
		folderNo = getFolderNoByDiaryNo(diaryNo);

		String sql;
		String item;

		db = dbHelper.getReadableDatabase(); 
		for (int i=0; i<folderNo.size(); i++) {
			sql = "select folder from " + FOLDER +" where no = '" + folderNo.get(i) + "'";
			Util.ll("sql", sql + ", " + db.isOpen());
			Cursor cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext()) {
				item = cursor.getString(0);
				folderArr.add(item);
			}
			cursor.close();
		}
		dbHelper.close();
		return folderArr;
	}

	//다이어리와 폴더의 다대다 관계를 정의해주는 테이블에서 다이어리번호에 해당하는 폴더번호를 가져온다
	public ArrayList<Long> getFolderNoByDiaryNo(long diaryNo) { //완료
		ArrayList<Long> folderNoArr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select folderno from " + DIARY_FOLDER + " where diaryNo = '" + diaryNo + "'";
		Cursor cursor = db.rawQuery(sql, null);

		Long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(0);
			folderNoArr.add(item);
		}		
		cursor.close();
		dbHelper.close();
		return folderNoArr;
	}


	public ArrayList<String> getDistinctTagList() { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select distinct tag from " + TAG;
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(0);
			arr.add(item);
		}
		cursor.close();
		dbHelper.close();
		return arr;
	}

	public ArrayList<String> getDistinctFolderList() { //완료
		ArrayList<String> arr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select distinct folder from " + FOLDER;
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(0);
			arr.add(item);
		}
		cursor.close();
		dbHelper.close();
		return arr;
	}


	public ArrayList<String> getDistinctLocationList() { //완료
		ArrayList<String> locationArr = new ArrayList<String>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select distinct location from " + DIARY;
		Cursor cursor = db.rawQuery(sql, null);

		String item;
		while(cursor.moveToNext()) {
			item = cursor.getString(0);
			if(item != null && !item.equals("null"))
				locationArr.add(item);
		}
		cursor.close();
		dbHelper.close();
		return locationArr;
	}


	public Diary getDiaryByNo(Long diaryNo) {
		Util.ll("diaryNo", diaryNo);
		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
		String sql = "select * from diary where no = '" + diaryNo + "'";

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

			//			Log.e("cursor.getInt(0)", ""+cursor.getInt(0));

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

		}
		cursor.close();
		dbHelper.close();


		ArrayList<String> images = getImages(diaryNo);
		ArrayList<String> tags = getTagsByDiaryNo(diaryNo);
		ArrayList<String> folders = getFoldersByDiaryNo(diaryNo);

		diary.setImages(images);
		diary.setTags(tags);
		diary.setFolders(folders);
		return diary;
	}	

	private long getFolderNoByFolderName(String folderName) {

		ArrayList<Long> arr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select no from " + FOLDER + " where folder = '" + folderName + "'";
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

	private long getTagNoByTag(String tag) {
		ArrayList<Long> arr = new ArrayList<Long>();

		db = dbHelper.getReadableDatabase(); 
		String sql = "select no from " + TAG + " where tag = '" + tag + "'";
		Cursor cursor = db.rawQuery(sql, null);

		long item;
		while(cursor.moveToNext()) {
			item = cursor.getLong(0);
			arr.add(item);
		}
		cursor.close();
		dbHelper.close();

		long tagno = arr.get(0);
		Util.ll("tagno", tagno);
		return tagno;
	}


	//리스트뷰의 0번지가 가장 최신 
	//no 내림차순으로 가져와서 0번지부터 저장시
	private ArrayList<Long> getDiaryNoByFolderNo(long folderNo) {
		ArrayList<Long> arrDiaryNo = new ArrayList<Long>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select diaryno from " + DIARY_FOLDER + " where folderno = '" + folderNo + "' order by no desc";

		Cursor cursor = db.rawQuery(sql, null);
		Long diaryNo;
		while(cursor.moveToNext()) {
			diaryNo = cursor.getLong(0);
			arrDiaryNo.add(diaryNo);
		}
		cursor.close();
		dbHelper.close();
		return arrDiaryNo;
	}

	private ArrayList<Long> getDiaryNoByTagNo(long tagNo) {
		ArrayList<Long> arrDiaryNo = new ArrayList<Long>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select diaryno from " + DIARY_TAG + " where tagno = '" + tagNo + "' order by no desc";

		Cursor cursor = db.rawQuery(sql, null);
		Long diaryNo;
		while(cursor.moveToNext()) {
			diaryNo = cursor.getLong(0);
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

	public ArrayList<Diary> getDiaryListByTag(String tag) {
		if (!isContainedTag(tag))
			return null;

		Util.ll("getDiaryListByTag", tag);
		db = dbHelper.getReadableDatabase(); 

		long tagNo = getTagNoByTag(tag);
		ArrayList<Long> arrDiaryNo = getDiaryNoByTagNo(tagNo);

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



	//	public ArrayList<Photo> getGalleryList() { 
	//		return null;
	//		
	//	}

	public ArrayList<MyLog> getMyLogList() { 
		return null;

	}

	public ArrayList<BookMark> getBookMarkList() {
		return null;

	}


	public boolean insertMyLogList(ArrayList<MyLog> logArr) {
		if (logArr != null && logArr.size() != 0) {
			db = dbHelper.getWritableDatabase();
			for(int i=0; i< logArr.size(); i++) {
				row = new ContentValues();
				row.put("id", logArr.get(i).getId());
				row.put("rev", logArr.get(i).getRev());
				row.put("latitude", logArr.get(i).getLatitude());
				row.put("longitude", logArr.get(i).getLongitude());
				row.put("date", logArr.get(i).getDate());
				row.put("type", logArr.get(i).getMyLogType());
				db.insert(CALL, null, row);
			}
			dbHelper.close();
		}
		return true;
	}


	public boolean insertCardList(ArrayList<Card> cardArr) { 
		if (cardArr != null && cardArr.size() != 0) {
			Util.ll("insertCardList  cardArr.size()",  cardArr.size());
			db = dbHelper.getWritableDatabase();
			long max = -1;
			for(int i=0; i< cardArr.size(); i++) {
				row = new ContentValues();
				row.put("smsno", cardArr.get(i).getNo());
				row.put("cardType", cardArr.get(i).getCardType());
				row.put("cardDate", cardArr.get(i).getCardDate());
				row.put("spendedMoney", cardArr.get(i).getSpendedMoney());
				row.put("spendedPlace", cardArr.get(i).getSpendedPlace());
				row.put("leftMoney", cardArr.get(i).getLeftMoney());
				db.insert(CARD, null, row);

				if(cardArr.get(i).getDate() > max){
					max  = cardArr.get(i).getDate();
				}
			}
			if (max > 0) {
				pref.setLongPref(Common.KEY_CARD_BASETIME, max);
			}
			dbHelper.close();
		}
		return true;
	}

	public boolean insertSmsList(ArrayList<MySms> smsArr, String timetype) {  //완료
		if (smsArr != null && smsArr.size() != 0) {
			db = dbHelper.getWritableDatabase();
			long max = -1;
			for(int i=0; i< smsArr.size(); i++) {
				row = new ContentValues();
				row.put("name", smsArr.get(i).getName());
				row.put("number", smsArr.get(i).getNumber());
				row.put("type", smsArr.get(i).getType());
				row.put("date", smsArr.get(i).getDate());
				row.put("message", smsArr.get(i).getMessage());
				db.insert(SMS, null, row);

				if(smsArr.get(i).getDate() > max){
					max  = smsArr.get(i).getDate();
				}
			}
			if (max > 0) {
				if (timetype.equals(Common.KEY_SMSINBOX_BASETIME))
					pref.setLongPref(Common.KEY_SMSINBOX_BASETIME, max);
				else if (timetype.equals(Common.KEY_SMSSENT_BASETIME))
					pref.setLongPref(Common.KEY_SMSSENT_BASETIME, max);
			}
			dbHelper.close();
		}
		return true;
	}


	public boolean insertCallList(ArrayList<Call> callArr) {  //완료
		if (callArr != null && callArr.size() != 0) {
			db = dbHelper.getWritableDatabase();
			long max = -1;
			for(int i=0; i< callArr.size(); i++) {
				row = new ContentValues();
				row.put("name", callArr.get(i).getName());
				row.put("number", callArr.get(i).getNumber());
				row.put("type", callArr.get(i).getType());
				row.put("date", callArr.get(i).getDate());
				row.put("duration", callArr.get(i).getDuration());
				db.insert(CALL, null, row);

				if(callArr.get(i).getDate() > max){
					max  = callArr.get(i).getDate();
				}
			}
			dbHelper.close();
			if (max > 0)
				pref.setLongPref(Common.KEY_CALL_BASETIME, max);
		}
		return true;
	}




	public ArrayList<MyLog> getCallList() { //완료
		//		Util.tst(mContext, "getDiaryList()");
		ArrayList<MyLog> arItem = new ArrayList<MyLog>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + CALL + " order by date desc";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			Call call = new Call();
			long callNo = cursor.getInt(0);
			String name = cursor.getString(1);
			String number = cursor.getString(2);
			String type = cursor.getString(3);
			long date = cursor.getLong(4);
			int duration = cursor.getInt(5);

			call.setNo(callNo);
			call.setName(name);
			call.setNumber(number);
			call.setType(type);
			call.setDate(date);
			call.setDuration(duration);

			arItem.add(call);
		}
		cursor.close();
		dbHelper.close();
		return arItem;
	}	

	public ArrayList<MyLog> getCallListByType(String subType) { //완료
		ArrayList<MyLog> arItem = new ArrayList<MyLog>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + CALL + " where type = '" +  subType + "' order by date desc";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			Call call = new Call();
			long callNo = cursor.getInt(0);
			String name = cursor.getString(1);
			String number = cursor.getString(2);
			String type = cursor.getString(3);
			long date = cursor.getLong(4);
			int duration = cursor.getInt(5);

			call.setNo(callNo);
			call.setName(name);
			call.setNumber(number);
			call.setType(type);
			call.setDate(date);
			call.setDuration(duration);

			arItem.add(call);
		}
		cursor.close();
		dbHelper.close();
		return arItem;
	}

	public ArrayList<MyLog> getSmsListByType(String subType, long baseTime) { //완료
		ArrayList<MyLog> arItem = new ArrayList<MyLog>();
		db = dbHelper.getReadableDatabase(); 
	
		String sql = "select * from " + SMS + " where type = '" +  subType + "' ";
		
		if(baseTime != -1) 
			sql += "and date > '" + baseTime + "' ";
		sql += "order by date desc";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			MySms sms = new MySms();
			long smsNo = cursor.getInt(0);
			String name = cursor.getString(1);
			String number = cursor.getString(2);
			String type = cursor.getString(3);
			long date = cursor.getLong(4);
			String message = cursor.getString(5);

			sms.setNo(smsNo);
			sms.setName(name);
			sms.setNumber(number);
			sms.setType(type);
			sms.setDate(date);
			sms.setMessage(message);

			arItem.add(sms);
		}
		cursor.close();
		dbHelper.close();
		return arItem;
	}

	
	public ArrayList<MyLog> getSmsList() { //완료
		ArrayList<MyLog> arItem = new ArrayList<MyLog>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + SMS + " order by date desc";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			MySms sms = new MySms();
			long smsNo = cursor.getInt(0);
			String name = cursor.getString(1);
			String number = cursor.getString(2);
			String type = cursor.getString(3);
			long date = cursor.getLong(4);
			String message = cursor.getString(5);

			sms.setNo(smsNo);
			sms.setName(name);
			sms.setNumber(number);
			sms.setType(type);
			sms.setDate(date);
			sms.setMessage(message);

			arItem.add(sms);
		}
		
		Util.ll("getSmsList", arItem.size());
		cursor.close();
		dbHelper.close();
		return arItem;
	}	



	public ArrayList<MyLog> getCardList() { 
		ArrayList<MyLog> arItem = new ArrayList<MyLog>();
		ArrayList<Card> cardArr = new ArrayList<Card>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + CARD + " order by cardDate desc";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			Card card = new Card();
			long cardNo = cursor.getInt(0);
			long smsNo = cursor.getInt(1);
			String cardType = cursor.getString(2);
			long cardDate = cursor.getLong(3);
			int spendedMoney = cursor.getInt(4);
			String spendedPlace = cursor.getString(5);
			int leftMoney = cursor.getInt(6);

			card.setCardNo(cardNo);
			card.setNo(smsNo);
			card.setCardType(cardType);
			card.setCardDate(cardDate);
			card.setSpendedMoney(spendedMoney);
			card.setSpendedPlace(spendedPlace);
			card.setLeftMoney(leftMoney);

			cardArr.add(card);
		}
		cursor.close();
		dbHelper.close();

		ArrayList<Card> fullCardArr = new ArrayList<Card>();
		for (int i=0; i < cardArr.size(); i++) {
			fullCardArr.add(getSmsBySmsNo(cardArr.get(i))); //패턴 
		}
		arItem.addAll(fullCardArr);
	
		Util.ll("getCardList  cardArr.size()",  cardArr.size());
		Util.ll("getCardList  arItem.size()",  arItem.size());
		return arItem;
	}	
	

//	String table_sms = "create table sms (no integer primary key autoincrement, " +
//			"name text, " +
//			"number text, " +
//			"type text, " +
//			"date integer, " +
//			"message text)";//re

	public Card getSmsBySmsNo(Card card) { //완료
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + SMS + " where no = '" +  card.getNo() + "'";

		Cursor cursor = db.rawQuery(sql, null);

		cursor.moveToFirst();
		long smsNo = cursor.getInt(0);
		String name = cursor.getString(1);
		String number = cursor.getString(2);
		String type = cursor.getString(3);
		long date = cursor.getLong(4);
		String message = cursor.getString(5);

		card.setNo(smsNo);
		card.setName(name);
		card.setNumber(number);
		card.setType(type);
		card.setDate(date);
		card.setMessage(message);

		Util.ll("getSmsBySmsNo date", MyTime.getLongToString(date));
//		card = (Card) sms;//확인

		cursor.close();
		dbHelper.close();
		return card;
	}


	public ArrayList<MyTemplate> getTemplateList(String categoryType) { 
		ArrayList<MyTemplate> arItem = new ArrayList<MyTemplate>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + TEMPLATE;

		if (categoryType != Common.ALL) {
			sql += " where category = '" + categoryType + "'";
		}

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			MyTemplate template = new MyTemplate();
			long templateNo = cursor.getInt(0);
			String name = cursor.getString(1);
			String category = cursor.getString(2);
			String information = cursor.getString(3);
			String author = cursor.getString(4);

			template.setNo(templateNo);
			template.setName(name);
			template.setCategory(category);
			template.setInformation(information);
			template.setAuthor(author);

			arItem.add(template);
		}
		cursor.close();
		dbHelper.close();
		return arItem;
	}	

	public ArrayList<String> getDistinctTemplateCategory() {
		ArrayList<String> arItem = new ArrayList<String>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select distinct category from " + TEMPLATE;

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			arItem.add(cursor.getString(0));
		}
		cursor.close();
		dbHelper.close();
		return arItem;
	}

	public ArrayList<TemplateContent> getTemplateContentByNo(long rcvTemplateNo) { 
		ArrayList<TemplateContent> arItem = new ArrayList<TemplateContent>();
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + TEMPLATE_CONTENT + " where templateno = '" + rcvTemplateNo + "' order by no asc";

		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext()) {
			TemplateContent tempContent = new TemplateContent();
			long contentNo = cursor.getInt(0);
			long templateNo = cursor.getInt(1);

			String question = cursor.getString(2);
			String content = cursor.getString(3);
			String front = cursor.getString(4);
			String end = cursor.getString(5);
			String type = cursor.getString(6);

			tempContent.setNo(contentNo);
			tempContent.setTemplateNo(templateNo);
			tempContent.setQuestion(question);
			tempContent.setContent(content);
			tempContent.setFront(front);
			tempContent.setEnd(end);
			tempContent.setType(type);

			arItem.add(tempContent);
		}
		cursor.close();
		dbHelper.close();
		return arItem;
	}	
	//	String table_template_content = "create table template_content (no integer primary key autoincrement, " +
	//			"templateno integer, " +
	//			"question text, " +
	//			"content text, " +
	//			"front text, " +
	//			"end text, " +
	//			"foreign key(templateno) references template(no))";		
	//
	//	String insert_template1 = "insert into template values (0, 'my life log', 'daily', '하루동안 발생한 로그로 일기를 작성해보세요', 'tiary')";
	//	String insert_template2 = "insert into template values (1, '오늘의 지출', 'money', '가계부, 이제 일일이 적지 않아요', 'tiary')";
	//	String insert_template3 = "insert into template values (2, '여긴 누구, 난 어디?', 'location', '내가 오늘 이동한 장소로 하루를 확인하세요', 'tiary')";
	//	String insert_template4 = "insert into template values (3, '나의 다이어트 일기', 'diet', '나의 다이어트 정보를 기록하는 똑똑한 일', 'tiary')";
	//
	//	String insert_template2_1 = "insert into table_template_content values (0, 1, '안녕하세요:)\n오늘은 " + MyTime.getLongToString(MyTime.getCurrentTime()) + "입니다, " +
	//			"null, null, null)";
	//	String insert_template2_2 = "insert into table_template_content values (1, 1, '오늘도 현명하게 소비하셨나요? 아니시라면 오늘부터 Tiary를 통해 가계부 습관들 들여보아요\n질문에 키워드로 답변해주세요', " +
	//			"null, null, null)";
	//	String insert_template2_3 = "insert into table_template_content values (2, 1, '오늘 식비는 얼마나 쓰셨나요?\n하나씩 선택해서 입력 가능 or 금액만 써주세요', " +
	//			"'', '식비: ', '원')";
	//	String insert_template2_4 = "insert into table_template_content values (3, 1, '오늘 교통비는 얼마나 쓰셨나요?\n', " +
	//			"'', '교통비: ', '원')";
	//	String insert_template2_5 = "insert into table_template_content values (4, 1, '오늘 갑작스런 지출이 있으셨나요?\n항목:금액 형식으로 입력해주세요\n(예)가족 식사: 100,000 ', " +
	//			"'', 'cust', '원')";



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

