
package com.sjcnh.commons.jsonserial;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sjcnh.commons.constants.DateConstants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author w
 * @description:
 * @title: CorsConstants
 * @projectName sjcnh-common
 * @date 2021/4/19
 * @company  sjcnh-ctu
 */
public class CustomTimeSerializer extends JsonSerializer<Date> {

	/**
	 * 日期格式化
	 */
	private final SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DEFAULT_DATE_TIME_FORMAT);


	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {

		String strDate = "";
		if (date != null) {
			strDate = sdf.format(date);
		}
		gen.writeString(strDate);

	}

}
