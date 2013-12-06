package com.spm.common.parser.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.json.JSONException;
import android.util.Log;
import com.google.inject.internal.Lists;
import com.spm.common.domain.Application.ApplicationProvider;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.parser.WebServiceParser;

/**
 * JSON input streams parser
 * 
 * @author Luciano Rey
 */
public abstract class JsonParser implements WebServiceParser {
	
	private static final String TAG = JsonParser.class.getSimpleName();
	
	/**
	 * @see com.spm.common.parser.WebServiceParser#parse(java.io.InputStream)
	 */
	@Override
	public Object parse(InputStream inputStream) {
		if (ApplicationProvider.LOGGING_SERVER_RESPONSE) {
			Log.d(TAG, "Parsing started.");
		}
		try {
			// Read the JSON response
			String result = convertStreamToString(inputStream);
			
			if (ApplicationProvider.LOGGING_SERVER_RESPONSE) {
				Log.d(TAG, result);
			}
			
			// Create a wrapped JSONObject
			JsonObjectWrapper json = null;
			if (result.equals("[]\n")) {
				json = new JsonObjectWrapper();
			} else {
				json = new JsonObjectWrapper(result);
			}
			
			// Parse the JSONObject
			return parse(json);
		} catch (JSONException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} finally {
			if (ApplicationProvider.LOGGING_SERVER_RESPONSE) {
				Log.d(TAG, "Parsing finished.");
			}
		}
	}
	
	/**
	 * @param json
	 * @return The parsed object
	 * @throws JSONException
	 */
	public abstract Object parse(JsonObjectWrapper json) throws JSONException;
	
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Parses a list of items.
	 * 
	 * @param <ITEM> The item's type.
	 * 
	 * @param json The {@link JsonObjectWrapper} to parse.
	 * @param jsonKey The key for the Json array.
	 * @param parser The {@link JsonParser} to parse each list item.
	 * @return The parsed list.
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	protected <ITEM> List<ITEM> parseList(JsonObjectWrapper json, String jsonKey, JsonParser parser)
			throws JSONException {
		List<ITEM> list = Lists.newArrayList();
		JsonArrayWrapper jsonArray = json.getJSONArray(jsonKey);
		int length = jsonArray.length();
		
		for (int i = 0; i < length; i++) {
			list.add((ITEM)parser.parse(jsonArray.getJSONObject(i)));
		}
		return list;
	}
}
