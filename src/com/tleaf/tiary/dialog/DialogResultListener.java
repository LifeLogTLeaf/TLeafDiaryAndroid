package com.tleaf.tiary.dialog;

public interface DialogResultListener {

	public void setResult(int result);
	public void setResult(String result, int dataType);
	public void setCancel();
}
