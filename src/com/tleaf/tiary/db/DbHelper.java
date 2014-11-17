package com.tleaf.tiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.util.MyTime;

/*
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
				"duration integer)";//re

		String table_mylog = "create table mylog (no integer primary key autoincrement, " +
				"id text, " +
				"rev text, " +
				"latitude real, " +
				"longitude real, " +
				"date integer, " +
				"type text)";

		String table_sms = "create table sms (no integer primary key autoincrement, " +
				"name text, " +
				"number text, " +
				"type text, " +
				"date integer, " +
				"message text)";//re

		
		String table_card = "create table card (no integer primary key autoincrement, " +
				"smsno integer, " +
				"cardType text, " +
				"cardDate integer, " +
				"spendedMoney integer, " +
				"spendedPlace text, " +
				"leftMoney integer, " +
				"foreign key(smsno) references sms(no))";		

		String table_template = "create table template (no integer primary key autoincrement, " +
				"name text, " +
				"category text, " +
				"information text, " +
				"author text)"; //작가개념

		String table_template_content = "create table template_content (no integer primary key autoincrement, " +
				"templateno integer, " +
				"question text, " +
				"content text, " +
				"front text, " +
				"end text, " +
				"type text, " +
				"foreign key(templateno) references template(no))";		

		String insert_template1 = "insert into template values (0, 'My life log', 'Daily', '하루동안 발생한 로그로 일기를 작성해보세요', 'tiary')";
		String insert_template2 = "insert into template values (1, '오늘의 지출', 'Money', '가계부, 이제 일일이 적지 않아요', 'tiary')";
		String insert_template3 = "insert into template values (2, '여긴 누구, 난 어디?', 'Location', '내가 오늘 이동한 장소로 하루를 확인하세요', 'tiary')";
		String insert_template4 = "insert into template values (3, '나의 다이어트 일기', 'Diet', '음식, 운동 단 하나의 기록도 놓치지 마세요', 'tiary')";
//		MyTime.getLongToString(MyTime.getCurrentTime())
		
		String insert_template2_1 = "insert into template_content values (0, 1, '안녕하세요 오늘은 2014년 11월 14일 입니다', " +
				"null, null, null, null)";
		String insert_template2_2 = "insert into template_content values (1, 1, '오늘도 현명하게 소비하셨나요? 아니시라면 오늘부터 Tiary를 통해 가계부 습관들 들여보아요! 질문에 키워드로 답변해주세요', " +
				"null, null, null, '" + Common.STRING_CARD +"')";
		String insert_template2_3 = "insert into template_content values (2, 1, '오늘 식비는 얼마나 쓰셨나요? 하나씩 선택해서 입력 가능 or 금액만 써주세요', " +
				"'', '식비: ', '원', '" + Common.STRING_CARD +"')";
		String insert_template2_4 = "insert into template_content values (3, 1, '오늘 교통비는 얼마나 쓰셨나요?', " +
				"'', '교통비: ', '원', '" + Common.STRING_CARD +"')";
		String insert_template2_5 = "insert into template_content values (4, 1, '오늘 갑작스런 지출이 있으셨나요?항목:금액 형식으로 입력해주세요 (예)가족 식사: 100,000 ', " +
				"'', 'cust', '원', '" + Common.STRING_CARD +"')";

		
		
		
		db.execSQL(table_diary);
		db.execSQL(table_image);
		db.execSQL(table_tag);
		db.execSQL(table_folder);
		db.execSQL(table_diaryTag);
		db.execSQL(table_diaryFolder);

		db.execSQL(insert_myfolder);

		db.execSQL(table_call);
		db.execSQL(table_mylog);
		db.execSQL(table_sms);
		db.execSQL(table_card);

		db.execSQL(table_template);
		db.execSQL(table_template_content);

		db.execSQL(insert_template1);
		db.execSQL(insert_template2);
		db.execSQL(insert_template3);
		db.execSQL(insert_template4);
		
		db.execSQL(insert_template2_1);
		db.execSQL(insert_template2_2);
		db.execSQL(insert_template2_3);
		db.execSQL(insert_template2_4);
		db.execSQL(insert_template2_5);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
