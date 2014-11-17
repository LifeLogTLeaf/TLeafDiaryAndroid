package com.tleaf.tiary.dialog;

import java.util.ArrayList;

/** 커스텀 다이어로그와 프래그먼트간의 통신을 위한 인터페이스 **/
public interface DialogResultListener {

	public void setResult(int result);
	public void setResult(ArrayList<String> result, int dataType);
	public void setResult(String result);

	public void setCancel();
	
	
}
