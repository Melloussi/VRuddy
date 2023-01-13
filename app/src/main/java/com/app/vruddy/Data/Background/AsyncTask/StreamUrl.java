package com.app.vruddy.Data.Background.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.app.vruddy.Models.DecryptCipher.DecryptCipher;
import com.app.vruddy.Models.DecryptCipher.ResultCallBack;
import com.app.vruddy.Models.DecryptCipher.StreamResultCallBack;
import com.app.vruddy.Data.database.Cipher.CipherViewModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StreamUrl extends AsyncTask<String, Void, Void> {
    private static String cipherLink;
    private static FragmentActivity fragmentActivity;
    private static CipherViewModel cipherViewModel;
    private static StreamResultCallBack streamResultCallBack;
    private static String id;

    public StreamUrl(FragmentActivity localFragmentActivity, CipherViewModel localCipherViewModel, StreamResultCallBack localStreamResultCallBack) {
        fragmentActivity      = localFragmentActivity;
        cipherViewModel       = localCipherViewModel;
        streamResultCallBack  = localStreamResultCallBack;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String url = "https://www.youtube.com/watch?v="+strings[0].trim()+"&pbj=1";
        id = strings[0].trim();
        request(url);
        return null;
    }
    private void request(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .addHeader("host", "www.youtube.com")
                .addHeader("x-youtube-client-name", "1")
                .addHeader("x-youtube-client-version", "2.20210304.08.01")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36")
                .url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("StreamUrl", "Bad Connection");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    handelJsonData(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void handelJsonData(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();

        jsonArray = (JSONArray) jsonParser.parse(json);
        JSONObject get_playerResponse = (JSONObject) jsonArray.get(2);
        JSONObject playerResponse = (JSONObject) get_playerResponse.get("playerResponse");

        JSONObject streamingData = (JSONObject) playerResponse.get("streamingData");
        JSONArray get_formats = (JSONArray) streamingData.get("formats");
        JSONObject formats = (JSONObject) get_formats.get(0);
        

        /*I check if this JSON data have direct stream link or have a "signatureCipher"
         * if it have "signatureCipher" I call a method that Decrypt "signatureCipher" and return
         *  stream link in a string as a value */
        if (formats.get("signatureCipher") != null) {
            //get signatureCipher
            cipherLink = (String) formats.get("signatureCipher");

            fragmentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DecryptCipher cipher = new DecryptCipher(id, fragmentActivity, cipherViewModel, new ResultCallBack() {
                        @Override
                        public void firstStream(String stream) {

                        }

                        @Override
                        public void secStream(String stream) {

                        }

                        @Override
                        public void watchUrl(String url) {
                            streamResultCallBack.result(url);
                        }
                    });
                    cipher.decrypt(cipherLink, "W");
                }
            });

        } else {
            System.out.println(formats.get("url"));
            cipherLink = (String) formats.get("url");
            streamResultCallBack.result(cipherLink);
        }


    }
}
