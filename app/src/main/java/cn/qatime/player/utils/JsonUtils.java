package cn.qatime.player.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/11 15:38
 * @Description
 */
public class JsonUtils {

    private static Gson sGson = new GsonBuilder().serializeNulls().create();

    public static String jsonFromObject(Object object) {
        try {
            return sGson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectFromJson(String json, Class<T> clz) {
        try {
            return sGson.fromJson(json, clz);
        } catch (Exception e) {
            LogUtils.e("JSON", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectFromJson(InputStream in, Class<T> clz) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            T object = sGson.fromJson(reader, clz);
            reader.close();
            return object;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> listFromJsonWithSubKey(String json, Class<T> clz, String key) {
        List<T> retList = new ArrayList<T>();
        try {
            JsonParser parser = new JsonParser();
            JsonElement el = parser.parse(json);
            JsonObject obj = null;
            if (el.isJsonObject()) {
                obj = el.getAsJsonObject();
            }
            JsonArray jsonArray = obj.getAsJsonArray(key);
            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JsonElement temp = iterator.next();
                T entity = sGson.fromJson(temp, clz);
                retList.add(entity);
            }
        } catch (Exception e) {
            LogUtils.e("异常",e.toString());
            e.printStackTrace();
        }

        return retList;
    }

    public static <T> List<T> listFromJson(String json, Class<T> clz) {
        List<T> retList = new ArrayList<T>();
        try {
            JsonParser parser = new JsonParser();
            JsonElement el = parser.parse(json);
            JsonArray jsonArray = null;
            if (el.isJsonArray()) {
                jsonArray = el.getAsJsonArray();
            }

            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JsonElement temp = iterator.next();
                T entity = sGson.fromJson(temp, clz);
                retList.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retList;
    }
}
