package org.stormdev.authlib.json;

import lib.stormauthlib.com.google.gson.Gson;
import lib.stormauthlib.com.google.gson.GsonBuilder;
import lib.stormauthlib.com.google.gson.JsonParser;

/**
 * Internally used instances of Gson objects
 *
 */
public class GsonUtil {
	public static Gson gson = new GsonBuilder().create();
	public static JsonParser parser = new JsonParser();
}
