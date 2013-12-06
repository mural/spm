package com.spm.common.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import android.app.Activity;
import android.widget.EditText;
import com.spm.common.utils.ReflectionUtils;

/**
 * 
 * @author Luciano Rey
 */
public class FieldModelMapper {
	
	public static void mapFieldsModel(Activity activity) {
		for (Field field : activity.getClass().getDeclaredFields()) {
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof FieldModel) {
					FieldModel validationField = (FieldModel)annotation;
					
					Object model = ReflectionUtils.get(activity, validationField.model());
					
					Object value = null;
					if (field.getType().equals(EditText.class)) {
						EditText editText = (EditText)ReflectionUtils.get(field, activity);
						value = editText.getText().toString();
					}
					
					ReflectionUtils.set(model, validationField.field(), value);
				}
			}
		}
	}
}
