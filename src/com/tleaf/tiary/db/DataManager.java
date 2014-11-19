package com.tleaf.tiary.db;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

import com.google.gson.JsonParser;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.core.AppContext;
import com.tleaf.tiary.model.BookMark;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.Card;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.model.MyTemplate;
import com.tleaf.tiary.model.TemplateContent;
import com.tleaf.tiary.model.Weather;
import com.tleaf.tiary.network.HttpMethod;
import com.tleaf.tiary.network.Request;
import com.tleaf.tiary.network.Response;
import com.tleaf.tiary.util.MyPreference;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class DataManager implements Request.Callback{

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
	
	private long todayTime;
	
	private boolean isUpdate, isDelete;
	private ArrayList<String> updateFile;

	public DataManager(Context context) {
		mContext = context;
		dbHelper = new DbHelper(mContext);
		pref = new MyPreference(mContext);
		Time time = new Time();
		time.setToNow();
		time.hour = 0;
		time.minute = 0;
		time.second = 0;
		todayTime = time.toMillis(false);
	}

	/** 다이어리를 다이어리, 이미지, 태그, 폴더 테이블에 추가한다 **/
	public boolean insertDiary(Diary diary) { //완료
		//TODO (Young) Insert Dairy into server
		// 임베디드 데이터베이스에서 데이터를 삭제할때 해당 Column에는 서버에 삭제할 도큐먼트 아이디를 저장하고 있어야한다. 그래야
		// 거기서 도큐먼트 아이디를 읽어와서 서버의 데이터도 삭제한다.
		new Request(AppContext.getTlSession(), "api/user/app/log", HttpMethod.POST, this, diary).execute();		
		
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
	
	/** 수정된 다이어리를 다이어리, 이미지, 태그, 폴더 테이블에 반영한다 **/
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

	/** 수정된 다이어리에 있는 수정된 이미지를 반영한다 **/
	public boolean updateImageByDiary(Diary diary) { //완료
		boolean resuslt_delete = deleteImageByDiary(diary.getNo());
		boolean resuslt_insert = insertImageByDiary(diary);
		if (resuslt_delete && resuslt_insert)
			return true;
		else 
			return false;
	}

	/** 수정된 다이어리에 있는 수정된 태그를 반영한다 **/
	public boolean updateTagByDiary(Diary diary) { //완료
		boolean resuslt_delete = deleteDiaryTag(diary.getNo());
		boolean resuslt_insert = insertTagByDiary(diary);
		if (resuslt_delete && resuslt_insert)
			return true;
		else 
			return false;
	}

	/** 수정된 다이어리에 있는 수정된 폴더를 반영한다 **/
	public boolean updateFolderByDiary(Diary diary) { //완료
		boolean resuslt_delete = deleteDiaryFolder(diary.getNo());
		boolean resuslt_insert = insertFolderByDiary(diary);
		if (resuslt_delete && resuslt_insert)
			return true;
		else 
			return false;
	}

	/* 새로추가하는 다이어리의 새로운 이미지를 이미지 테이블에 넣는다 */
	public boolean insertImageByDiary(Diary diary) { //완료
		if (diary.getNo() == -1) 
			return false;
		if (diary.getImages() != null && diary.getImages().size() != 0) {
			isUpdate = true;
			updateFile = diary.getImages();
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

	/* 새로추가하는 다이어리의 새로운 태그를 태그 테이블에 넣는다 */
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

	/** 다이어리를 새로 추가할 때 다이어리 테이블과 태그 테이블를 연계하는 중간 테이블에 관계를 삽입한다 **/
	public boolean setDiaryTagRelation(long diaryNo, long tagNo){ //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("diaryNo", diaryNo);
		row.put("tagNo", tagNo);
		db.insert(DIARY_TAG, null, row);
		dbHelper.close();
		return true;
	}

	/* 새로추가하는 다이어리의 새로운 폴더를 폴더 테이블에 넣는다 */
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

	/** 새로 폴더 테이블에 폴더를 추가한다 **/
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

	/** 새로 태그 테이블에 태그를 추가한다 **/
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

	/** 사용자가 새로 추가한 폴더가 이미 있는 폴더인지 확인한다 **/
	private boolean isContainedFolder(String folder) {
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + FOLDER + " where folder = '" + folder + "'";
		Cursor cursor = db.rawQuery(sql, null);

		boolean result = (cursor.getCount() != 0) ? true : false;
		cursor.close();
		dbHelper.close();

		return result;
	}

	/** 사용자가 새로 추가한 태그가 이미 있는 태그인지 확인한다 **/
	public boolean isContainedTag(String tag) {
		db = dbHelper.getReadableDatabase(); 
		String sql = "select * from " + TAG + " where tag = '" + tag + "'";
		Cursor cursor = db.rawQuery(sql, null);

		boolean result = (cursor.getCount() != 0) ? true : false;
		cursor.close();
		dbHelper.close();

		return result;
	}

	/* 태그 테이블에서 태그명으로 태그 pk인 no를 찾는다 */
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

	/* 폴더 테이블에서 폴더명으로 폴더 pk인 no를 찾는다 */
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

	/* 다이어리를 새로 추가할 때 다이어리 테이블과 폴더 테이블를 연계하는 중간 테이블에 관계를 삽입한다 */
	public boolean setDiaryFolderRelation(long diaryNo, long folderNo){ //완료
		db = dbHelper.getWritableDatabase();
		row = new ContentValues();
		row.put("diaryNo", diaryNo);
		row.put("folderNo", folderNo);
		db.insert(DIARY_FOLDER, null, row);
		dbHelper.close();
		return true;

	}

	/* 다이어리 pk에 해당하는 이미지를 이미지 테이블에서 삭제한다 */
	public boolean deleteImageByDiary(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + IMAGE + " where diaryNo ='" + diaryNo + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	/* 다이어리를 삭제할 때 다이어리테이블과 태그 테이블을 연계하는 중간 테이블에 관계를 삭제한다 */
	public boolean deleteDiaryTag(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + DIARY_TAG + " where diaryNo='" + diaryNo + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	/* 다이어리를 삭제할 때 다이어리테이블과 폴더 테이블을 연계하는 중간 테이블에 관계를 삭제한다 */
	public boolean deleteDiaryFolder(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + DIARY_FOLDER + " where diaryNo='" + diaryNo + "'";

		db.execSQL(sql);
		dbHelper.close();
		return true;
	}

	/** 다이어리를 삭제하기위해 다이어리, 이미지, 태그, 폴더 테이블 및 관계 테이블에서 해당 정보를 삭제한다 */
	public boolean deleteDiary(long diaryNo) { //완료
		Util.ll("no", diaryNo);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String delete_diary = "delete from " + DIARY + " where no='" + diaryNo + "'";

		db.execSQL(delete_diary);

		deleteImageByDiary(diaryNo);
		deleteDiaryTag(diaryNo);
		deleteDiaryFolder(diaryNo);
		dbHelper.close();
		
		// TODO Young's Code
		isDelete = true;
		
		return true;
	}

	/** 다이어리 테이블에서 현재 있는 다이어리를 가져온다 **/
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

	/** 이미지 테이블에서 다이어리 pk에 해당하는 이미지를 가져온다 **/
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

	/** 태그 테이블에서 다이어리 pk에 해당하는 태그를 가져온다 **/
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

	/** 해당 다이어리 pk에 해당하는 태그 pk를 가져온다 **/
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

	/** 해당 다이어리 pk에 해당하는 폴더 pk를 가져온다 **/
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

	/** 다이어리와 폴더의 다대다 관계를 정의해주는 테이블에서 다이어리번호에 해당하는 폴더번호를 가져온다 **/
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

	/** 태그 테이블에 중복되지 않은 태그 목록을 가져온다 **/
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

	/** 폴더 테이블에 중복되지 않은 폴더 목록을 가져온다 **/
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

	/** 다이어리 테이블에 중복되지 않은 장소 목록을 가져온다 **/
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


	/** 다이어리 pk에 해당하는 다이어리를 얻는다 **/
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

	/** 폴더 테이블에서 폴더 이름에 해당하는 폴더 번호를 가져온다 **/
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

	/** 태그 테이블에서 태그 이름에 해당하는 태그 번호를 가져온다 **/
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
	/** 폴더 pk에 해당하는 다이어리 pk를 가져온다 **/
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

	/** 태그 pk에 해당하는 다이어리 pk를 가져온다 **/
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

	/** 폴더명에 해당되는 다이어리들을 가져온다 **/
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

	/** 태그명에 해당하는 다이어리들을 가져온다 **/
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

//	public boolean insertMyLogList(ArrayList<MyLog> logArr) {
//		if (logArr != null && logArr.size() != 0) {
//			db = dbHelper.getWritableDatabase();
//			for(int i=0; i< logArr.size(); i++) {
//				row = new ContentValues();
//				row.put("id", logArr.get(i).getId());
//				row.put("rev", logArr.get(i).getRev());
//				row.put("latitude", logArr.get(i).getLatitude());
//				row.put("longitude", logArr.get(i).getLongitude());
//				row.put("date", logArr.get(i).getDate());
//				row.put("type", logArr.get(i).getShackLogType());
//				db.insert(CALL, null, row);
//			}
//			dbHelper.close();
//		}
//		return true;
//	}


	/** 카드로그를 카드 테이블에 넣는다 **/
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

	/** 문자로그를 문자 테이블에 넣는다 **/
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

	/** 전화기록을 전화 테이블에 넣는다 **/
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

	/** 전화 테이블에 있는 전화로그를 가져온다 **/
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
	
	
	/** 각 로그테이블에서 오늘의 로그 개수를 세온다 **/
	@Deprecated
	public int getLogCountByType(String type) { //완료
		//		Util.tst(mContext, "getDiaryList()");
		db = dbHelper.getReadableDatabase(); 
		
		String dateType;
		if (type.equals(CARD))
			dateType = "cardDate";
		else 
			dateType = "date";
		
		String sql = "select count(*) from " + type + " where "+  dateType + " >= '" + todayTime + "'";

		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		
		cursor.close();
		dbHelper.close();
		return count;	
	}	
	
	/** 전화 테이블에서 전화 타입(수신, 발신, 부재중)에 따른 전화기록을 가져온다 **/
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

	/** 문자 테이블에서 문자 타입(수신, 발신)에 따른 문자기록을 가져온다
	  * 또한 card정보를 파싱하기 위해 기준 타임 이후 수신메시지만 가져온다 **/
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

	/** 문자테이블에서 문자 로그를 가져온다 **/
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

	/** 카드 테이블에서 카드 로그를 가져온다
	 *  카드 테이블에 있는 sms pk를 가져와서 sms에 해당하는 정보도 넣어준다 **/
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

	/** sms 테이블에서 sms pk에 해당하는 sms 정보를 card 객체로 반환한다 **/
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

	/** 템플릿 테이블에서 카테고리 타입에 해당하는 템플릿 객체를 반환한다 **/
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

	/** 템플릿 테이블에서 중복되지 않는 카테고리 목록을 반환한다 **/ //re
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

	/** 템플릿 컨텐츠 테이블에서 템플릿 pk에 해당하는 텀플릿 컨텐츠 객체를 반환한다 **/
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

	/* (non-Javadoc)
	 * @see com.tleaf.tiary.network.Request.Callback#onRecieve(com.tleaf.tiary.network.Response)
	 */
	@Override
	public void onRecieve(Response response) {
		// TODO Auto-generated method stub
		Log.i("DBHelper",""+response.getStatus());
		Log.i("DBHelper",response.getJsonStringData());
		
		// TODO if need to update
		if(isUpdate){
			isUpdate = false;
			String id = null, rev = null;
			try {
				JSONObject jsonObject = new JSONObject(response.getJsonStringData());
				id = jsonObject.getString("_id");
				rev = jsonObject.getString("_rev");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Request(AppContext.getTlSession(), "api/user/file", HttpMethod.FILEPOST, this, id, rev, updateFile).execute();		
		}
	}

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

