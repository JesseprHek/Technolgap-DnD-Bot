package org.dndbot.fifthapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DnDAPI {

    public static void main(String[] args) {
        System.out.println(getClass("WIZARD"));

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
}
