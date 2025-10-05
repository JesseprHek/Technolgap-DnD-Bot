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
        String jsonData = getInfo("classes");
        // You can now use jsonData as needed
        // This will return a JSON string containing all classes
        return new String[]{jsonData};
    }

    public static String getClass(String className) {
        return getInfo("classes/" + className.toLowerCase());
    }

    public static String getInfo(String path){
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
            // You can now use jsonData as needed
            return jsonData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("Item not found at path: " + path + ". Please check the spelling and try again.");
    }



    public static String[] getClassNames() {
        String jsonData = getInfo("classes");
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray results = jsonObject.getJSONArray("results");
        String[] names = new String[results.length()];
        for (int i = 0; i < results.length(); i++) {
            names[i] = results.getJSONObject(i).getString("name");
        }
        return names;
    }
}
