package com.app.vruddy.Models.AsyncTask;

import android.os.AsyncTask;

import com.app.vruddy.Models.TestCallBack;
import com.app.vruddy.Models.Objects.VideoObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrendingDataRequest extends AsyncTask<Void, Void, Void> {
    private static List<VideoObject> videoObjectList = new ArrayList<>();
    private static String video_videoId;
    private static String video_videoTitle;
    private static String video_time;
    private static String video_date;
    private static String video_views;
    private static String video_by;
    private static String video_url;
    private static String video_channel_image;
    private static boolean video_badge;
    private static  TestCallBack testCallBack;

    public TrendingDataRequest(TestCallBack localTestCallBack) {
        testCallBack = localTestCallBack;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        request();
        return null;
    }

    private static void request() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("x-youtube-client-name","1")
                .header("x-youtube-client-version","2.20210304.08.01")
                .header("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36")
//                .header("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36")
                .url("https://www.youtube.com/feed/trending?pbj=1")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Bad Request!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    jsonData(response.body().string());
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
    }

    private static void jsonData(String json) {
        System.out.println("----------> TREND JSON: "+json);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(json);
            JSONObject get_response = (JSONObject) jsonArray.get(1);
            JSONObject response = (JSONObject) get_response.get("response");
            JSONObject contents = (JSONObject) response.get("contents");
            JSONObject twoColumnBrowseResultsRenderer = (JSONObject) contents.get("twoColumnBrowseResultsRenderer");
            JSONArray get_tabs = (JSONArray) twoColumnBrowseResultsRenderer.get("tabs");
            JSONObject tabs = (JSONObject) get_tabs.get(0);
            JSONObject tabRenderer = (JSONObject) tabs.get("tabRenderer");
            JSONObject content = (JSONObject) tabRenderer.get("content");
            JSONObject sectionListRenderer = (JSONObject) content.get("sectionListRenderer");
            JSONArray get_contents = (JSONArray) sectionListRenderer.get("contents");
            JSONObject contents_b = (JSONObject) get_contents.get(0);
            JSONObject itemSectionRenderer = (JSONObject) contents_b.get("itemSectionRenderer");
            JSONArray get_itemSectionRenderer_contents = (JSONArray) itemSectionRenderer.get("contents");
            JSONObject itemSectionRenderer_contents = (JSONObject) get_itemSectionRenderer_contents.get(0);
            JSONObject shelfRenderer = (JSONObject) itemSectionRenderer_contents.get("shelfRenderer");
            JSONObject shelfRenderer_content = (JSONObject) shelfRenderer.get("content");
            JSONObject expandedShelfContentsRenderer = (JSONObject) shelfRenderer_content.get("expandedShelfContentsRenderer");
            JSONArray items = (JSONArray) expandedShelfContentsRenderer.get("items");

            //part one
            for(int index = 0; index < items.size(); index++){
                System.out.println("----------------+++--------------------");
                System.out.println("--------------------- item size: "+items.size());
                JSONObject get_videoRenderer = (JSONObject) items.get(index);
                JSONObject videoRenderer = (JSONObject) get_videoRenderer.get("videoRenderer");

                if(videoRenderer.get("videoId") != null && videoRenderer.get("shortViewCountText") != null ){
                    //VideoID
                    video_videoId = (String) videoRenderer.get("videoId");
                    System.out.println("Id: "+video_videoId);

                    //thumbnail
                    JSONObject thumbnail = (JSONObject) videoRenderer.get("thumbnail");
                    JSONArray thumbnails = (JSONArray) thumbnail.get("thumbnails");
                    JSONObject get_url = (JSONObject) thumbnails.get(2);
                    video_url = (String) get_url.get("url");
                    System.out.println("url: "+video_url);

                    //Title
                    JSONObject title = (JSONObject) videoRenderer.get("title");
                    JSONArray get_runs = (JSONArray) title.get("runs");
                    JSONObject runs = (JSONObject) get_runs.get(0);
                    video_videoTitle = (String) runs.get("text");
                    System.out.println("Title: "+video_videoTitle);

                    //Date
                    JSONObject publishedTimeText = (JSONObject) videoRenderer.get("publishedTimeText");
                    video_date = (String) publishedTimeText.get("simpleText");
                    System.out.println("Date: "+video_date);

                    //Time
                    JSONObject lengthText = (JSONObject) videoRenderer.get("lengthText");
                    video_time = (String) lengthText.get("simpleText");
                    System.out.println("Time: "+video_time);

                    //Views
                    JSONObject shortViewCountText = (JSONObject) videoRenderer.get("shortViewCountText");
                    video_views = (String) shortViewCountText.get("simpleText");
                    System.out.println("Views: "+video_views);

                    //Badge
                    if(videoRenderer.get("ownerBadges") != null){
                        System.out.println("Badge: Yes");
                        video_badge = true;
                    }else {
                        System.out.println("Badge: No");
                        video_badge = false;
                    }

                    //By
                    JSONObject ownerText = (JSONObject) videoRenderer.get("ownerText");
                    JSONArray get_ownerText_runs = (JSONArray) ownerText.get("runs");
                    JSONObject ownerText_runs = (JSONObject) get_ownerText_runs.get(0);
                    video_by = (String) ownerText_runs.get("text");
                    System.out.println("By: "+video_by);

                    //channel Image
                    JSONObject channelThumbnailSupportedRenderers = (JSONObject) videoRenderer.get("channelThumbnailSupportedRenderers");
                    JSONObject channelThumbnailWithLinkRenderer = (JSONObject) channelThumbnailSupportedRenderers.get("channelThumbnailWithLinkRenderer");
                    JSONObject channel_thumbnail = (JSONObject) channelThumbnailWithLinkRenderer.get("thumbnail");
                    JSONArray get_channel_thumbnails = (JSONArray) channel_thumbnail.get("thumbnails");
                    JSONObject channel_thumbnails = (JSONObject) get_channel_thumbnails.get(0);
                    video_channel_image = (String) channel_thumbnails.get("url");
                    System.out.println("Channel Image: "+video_channel_image);

                    videoObjectList.add(new VideoObject(video_videoId, video_videoTitle,video_by,video_time,video_date,video_views,video_badge,video_url,video_channel_image));
                }
            }

            if(items.size() < 10) {
                //part two
                JSONObject contents_c = (JSONObject) get_contents.get(2);
                JSONObject get_itemSectionRenderer = (JSONObject) contents_c.get("itemSectionRenderer");
                JSONArray get_contents_ = (JSONArray) get_itemSectionRenderer.get("contents");
                JSONObject contents_ = (JSONObject) get_contents_.get(0);
                JSONObject shelfRenderer_ = (JSONObject) contents_.get("shelfRenderer");
                JSONObject shelfRender_content = (JSONObject) shelfRenderer_.get("content");
                JSONObject content_expandedShelfContentsRenderer = (JSONObject) shelfRender_content.get("expandedShelfContentsRenderer");
                JSONArray expandedShelfContentsRenderer_items = (JSONArray) content_expandedShelfContentsRenderer.get("items");

                for (int index = 0; index < expandedShelfContentsRenderer_items.size(); index++) {
                    JSONObject get_videoRenderer = (JSONObject) expandedShelfContentsRenderer_items.get(index);
                    JSONObject videoRenderer = (JSONObject) get_videoRenderer.get("videoRenderer");

                    if (videoRenderer.get("videoId") != null && videoRenderer.get("shortViewCountText") != null) {
                        //VideoID
                        video_videoId = (String) videoRenderer.get("videoId");
                        System.out.println("Id: " + video_videoId);

                        //thumbnail
                        JSONObject thumbnail = (JSONObject) videoRenderer.get("thumbnail");
                        JSONArray thumbnails = (JSONArray) thumbnail.get("thumbnails");
                        JSONObject get_url = (JSONObject) thumbnails.get(2);
                        video_url = (String) get_url.get("url");
                        System.out.println("url: " + video_url);

                        //Title
                        JSONObject title = (JSONObject) videoRenderer.get("title");
                        JSONArray get_runs = (JSONArray) title.get("runs");
                        JSONObject runs = (JSONObject) get_runs.get(0);
                        video_videoTitle = (String) runs.get("text");
                        System.out.println("Title: " + video_videoTitle);

                        //Date
                        JSONObject publishedTimeText = (JSONObject) videoRenderer.get("publishedTimeText");
                        video_date = (String) publishedTimeText.get("simpleText");
                        System.out.println("Date: " + video_date);

                        //Time
                        JSONObject lengthText = (JSONObject) videoRenderer.get("lengthText");
                        video_time = (String) lengthText.get("simpleText");
                        System.out.println("Time: " + video_time);

                        //Views
                        JSONObject shortViewCountText = (JSONObject) videoRenderer.get("shortViewCountText");
                        video_views = (String) shortViewCountText.get("simpleText");
                        System.out.println("Views: " + video_views);

                        //Badge
                        if (videoRenderer.get("ownerBadges") != null) {
                            System.out.println("Badge: Yes");
                            video_badge = true;
                        } else {
                            System.out.println("Badge: No");
                            video_badge = false;
                        }

                        //By
                        JSONObject ownerText = (JSONObject) videoRenderer.get("ownerText");
                        JSONArray get_ownerText_runs = (JSONArray) ownerText.get("runs");
                        JSONObject ownerText_runs = (JSONObject) get_ownerText_runs.get(0);
                        video_by = (String) ownerText_runs.get("text");
                        System.out.println("By: " + video_by);

                        //channel Image
                        JSONObject channelThumbnailSupportedRenderers = (JSONObject) videoRenderer.get("channelThumbnailSupportedRenderers");
                        JSONObject channelThumbnailWithLinkRenderer = (JSONObject) channelThumbnailSupportedRenderers.get("channelThumbnailWithLinkRenderer");
                        JSONObject channel_thumbnail = (JSONObject) channelThumbnailWithLinkRenderer.get("thumbnail");
                        JSONArray get_channel_thumbnails = (JSONArray) channel_thumbnail.get("thumbnails");
                        JSONObject channel_thumbnails = (JSONObject) get_channel_thumbnails.get(0);
                        video_channel_image = (String) channel_thumbnails.get("url");
                        System.out.println("Channel Image: " + video_channel_image);

                        videoObjectList.add(new VideoObject(video_videoId, video_videoTitle, video_by, video_time, video_date, video_views, video_badge, video_url, video_channel_image));
                    }
                }
            }
//            System.out.println("--- Test Call Back ---");
//            testCallBack = new HomeFragment();
//            testCallBack.trend(videoObjectList);
            System.out.println("------------- 0606 --------------");


        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("------------- 0606 --------------");
        testCallBack.trend(videoObjectList);
    }
}
