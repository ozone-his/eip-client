package com.ozonehis.eip.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public final class JsonUtils {

    private JsonUtils() {}

    /**
     * Utility method to extract and concatenate json array key values
     * @param jsonArray json array string
     * @param key key parameter whose values will be concatenated
     * @return concatenated values for key of json array elements
     */
    public static String convertToValuesArrayForKey(final String jsonArray, final String key) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        JSONArray array = new JSONArray(jsonArray);
        array.forEach((item) -> {
            builder.append(",\"").append(((JSONObject) item).getString(key)).append("\"");
        });
        return builder.substring(1);
    }

    /**
     * Utility method to determine if jsonArray contains item with matching key value pair
     * @param jsonArray json array string
     * @param key key parameter whose value will used for comparison
     * @param value value to be compared with
     * @return true/false
     */
    public static boolean containsItemWithKeyEqualsValue(final String jsonArray, final String key, final String value) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return false;
        }
        JSONArray array = new JSONArray(jsonArray);
        for (Object o : array) {
            if (value.equalsIgnoreCase(((JSONObject) o).getString(key))) return true;
        }
        return false;
    }

    /**
     * Utility method to return item's property's value given the provided array contains the item
     * @param jsonArray json array string
     * @param propertyName property name whose value will be returned
     * @param key key parameter whose value will be compared
     * @param value value to be compared with
     * @return property value
     */
    public static String getPropertyValueWhereKeyMatchesValue(
            final String jsonArray, final String propertyName, final String key, final String value) {
        String propertyValue = "";
        JSONArray array = new JSONArray(jsonArray);
        for (Object o : array) {
            JSONObject obj = ((JSONObject) o);
            if (value.equalsIgnoreCase(obj.getString(key))) {
                propertyValue = obj.getString(propertyName);
                break;
            }
        }
        return propertyValue;
    }
}
