package com.app.vruddy.Models.AsyncTask;

import android.os.AsyncTask;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SuggestionsRequest extends AsyncTask<String, String, String> {
    private String REQUEST_URL = "http://suggestqueries.google.com/complete/search?client=firefox&ds=yt&q=";
    private ArrayList<String> keyword = new ArrayList<>();

    //Getter

    public ArrayList<String> getKeyword() {
        return keyword;
    }

    @Override
    public String doInBackground(String... strings) {

        suggestionGetRequest(strings[0]);
        return null;
    }
    public void suggestionGetRequest(String keyword){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(REQUEST_URL+keyword)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{
                    json(response.body().string());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void json(String json) {

        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(json);

            JSONArray keywords = (JSONArray) jsonArray.get(1);
            for (Object resultKeyword : keywords){
                keyword.add(resultKeyword.toString());
                //System.out.println("Keyword: "+resultKeyword.toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
