package org.dndbot.fifthapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class DnDAPI {

    public static void main(String[] args) {
        System.out.println(getClass("WIZARD"));

    }
    public static String[] getAllClasses() {
        JSONObject jsonData = getInfo("classes");
        // You can now use jsonData as needed
        // This will return a JSON object containing all classes
        return new String[]{jsonData.toString()};
    }

    public static String getClass(String className) {
        return getInfo("classes/" + className.toLowerCase()).toString();
    }

    public static JSONObject getInfo(String path){
        String apiUrl = "https://www.dnd5eapi.co/api/2014/" + path;
        StringBuilder jsonResult = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }
            reader.close();

            // Store the JSON result as a String
            String jsonData = jsonResult.toString();
            // Return as JSONObject
            return new JSONObject(jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return an empty object with an error message
        JSONObject error = new JSONObject();
        error.put("error", "Item not found at path: " + path + ". Please check the spelling and try again.");
        return error;
    }



    public static String[] getClassNames() {
        JSONObject jsonObject = getInfo("classes");
        JSONArray results = jsonObject.getJSONArray("results");
        String[] names = new String[results.length()];
        for (int i = 0; i < results.length(); i++) {
            names[i] = results.getJSONObject(i).getString("name");
        }
        return names;
    }

    public static JSONObject getItem(String itemName) {
        JSONObject result = getInfo("equipment/" + itemName.toLowerCase().replace(" ", "-"));
        if (result.has("error")) {
            // Try magic-items if not found in equipment
            result = getInfo("magic-items/" + itemName.toLowerCase().replace(" ", "-"));
        }
        return result;
    }
}
