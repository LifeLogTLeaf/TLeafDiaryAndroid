/**
 * 
 */
package com.tleaf.tiary.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with Eclipse IDE
 * Author : RichardJ 
 * Date   : Oct 25, 2014 12:24:38 AM
 * Description : 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeclareView {
	int id() default 0;
	String name() default "";
	String tag() default "";
	String click() default "";
}
