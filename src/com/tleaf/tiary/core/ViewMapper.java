/**
 * 
 */
package com.tleaf.tiary.core;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * Created with Eclipse IDE
 * Author : RichardJ
 * Date : Oct 25, 2014 1:29:30 PM
 * Description :
 * 뷰 매퍼라는 UI자동화 코드를 생성해서 UI 셋팅에 관해서 자동화를 진행한다. 코드의 가독성과 관리과 쉬워지지만,
 * 어플리케이션 실행시 느려질 가능성이 크다.
 */
public class ViewMapper {
	/**
	 * Author : RichardJ
	 * Date : Oct 26, 2014 5:31:27 PM
	 * Description :
	 * 
	 * @param context : 액티비티의 컴포넌트 컨텍스트 [ 전체 컨텍스트보다 컴포넌트 컨텍스트가 메모리 관리상 더 좋다. ]
	 * @param container : 액티비티 객체
	 * @param layoutId : 액티비티가 담을 레이아웃아이디.
	 * @return
	 */
	public static View inflateLayout(Context context, Object container, int layoutId) {
		return mapLayout(container, LayoutInflater.from(context).inflate(layoutId, null));
	}

	/**
	 * Author : RichardJ
	 * Date : Oct 25, 2014 1:49:23 PM
	 * Description :
	 * 어노테이션 된 뷰들을 모두 읽어들여서 인스턴스화시키고 클릭이벤트를 가지고 있을경우에는 클릭이벤트를
	 * 등록시켜준다.
	 * 
	 */
	public static View mapLayout(Object container, View layoutView) {
		View childView = null;
		Field[] fields = container.getClass().getDeclaredFields();
		for (Field field : fields) {
			DeclareView param = field.getAnnotation(DeclareView.class);
			if (param != null) {
				childView = layoutView.findViewById(param.id());
				String viewName = childView.getClass().getSimpleName();
				try {
					field.setAccessible(true);
					field.set(container, childView);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (param.click().equals("this")) {
					switch (viewName) {
					case "RadioGroup":
						((RadioGroup) childView).setOnCheckedChangeListener((OnCheckedChangeListener) container);
						break;
					default:
						childView.setOnClickListener((OnClickListener) container);
						break;
					}
				}
			}
		}
		return layoutView;
	}
}
