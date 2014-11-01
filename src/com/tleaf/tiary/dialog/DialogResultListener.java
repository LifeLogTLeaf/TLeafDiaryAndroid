package com.tleaf.tiary.dialog;

import java.util.ArrayList;

public interface DialogResultListener {

	public void setResult(int result);
	public void setResult(ArrayList<String> result, int dataType);
	public void setResult(String result);

	public void setCancel();
	
	
}
