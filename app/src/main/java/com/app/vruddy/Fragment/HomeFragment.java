package com.app.vruddy.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vruddy.Adapter.HomeVideoAdapter;
import com.app.vruddy.AsyncTask.TrendingDataRequest;
import com.app.vruddy.Adapter.MyRecycle;
import com.app.vruddy.AsyncTask.getVideoStreamLink;
import com.app.vruddy.DecryptCipher.DecryptCipher;
import com.app.vruddy.DecryptCipher.ResultCallBack;
import com.app.vruddy.Interfaces.FetchDownloadingLinksCallback;
import com.app.vruddy.Objects.VideoStreamObject;
import com.app.vruddy.R;
import com.app.vruddy.TestCallBack;
import com.app.vruddy.HomeActivity;
import com.app.vruddy.Objects.VideoObject;
import com.app.vruddy.database.Cipher.CipherViewModel;
import com.app.vruddy.database.Favorite.Favorite;
import com.app.vruddy.database.Favorite.FavoriteIndex;
import com.app.vruddy.database.Favorite.FavoriteViewModel;
import com.app.vruddy.AsyncTask.getHomeVideo;
import com.app.vruddy.testService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TestCallBack {

    //Variables
    private Toolbar homeBar;
    private BottomNavigationView bottomNavigationView;
    private ImageView backImg;
    private boolean screenState = false;
    static boolean isThereData = false;
    private static boolean isMoreVideoLoaded = false;
    private static Context context;
    private static boolean isFirstTime = true;
    private static WatchFragment watchFragment;
    private static FragmentActivity activity;

    private static boolean is4KFound = false;
    private static boolean is1440Found = false;
    private static boolean is1080Found = false;
    private static boolean is720Found = false;
    private static boolean is480Found = false;
    private static boolean is360Found = false;
    private static boolean is240Found = false;
    private static boolean isTinyFound = false;
    private static boolean isSmallFound = false;
    private static boolean isMediumFound = false;
    private static boolean isLargeFound = false;
    private static boolean isPermissionGranted = false;
    private static boolean sleep = true;
    private static boolean isPermissionResponse = false;


    private static String p240DownloadLink;
    private static String p360DownloadLink;
    private static String p480DownloadLink;
    private static String p720DownloadLink;
    private static String p1080DownloadLink;
    private static String p1440DownloadLink;
    private static String k4DownloadLink;
    private static String aacDownloadLink;
    private static String tinyDownloadLink;
    private static String smallDownloadLink;
    private static String mediumDownloadLink;
    private static String largeDownloadLink;

    private static Dialog downloadWindow;
    private static TextView downloadWindowTitle;
    private static TextView downloadWindowTime;
    private static ImageView downloadWindowThumbnail;
    private static LinearLayout download_links_layout;
    private static RelativeLayout waiting_download_links_spinner_layout;
    private static RelativeLayout p240;
    private static RelativeLayout p360;
    private static RelativeLayout p480;
    private static RelativeLayout p720;
    private static RelativeLayout p1080;
    private static RelativeLayout p1440;
    private static RelativeLayout k4;
    private static TextView p240MbSize;
    private static TextView p360MbSize;
    private static TextView p480MbSize;
    private static TextView p720MbSize;
    private static TextView p1080MbSize;
    private static TextView p1440MbSize;
    private static TextView k4MbSize;

    //    private static RelativeLayout tiny;
//    private static RelativeLayout small;
//    private static RelativeLayout medium;
//    private static RelativeLayout large;
    private static TextView tinyMbSize;
    private static TextView smallMbSize;
    private static TextView mediumMbSize;
    private static TextView largeMbSize;
    private static TextView tinyBitrateSize;
    private static TextView smallBitrateSize;
    private static TextView mediumBitrateSize;
    private static TextView largeBitrateSize;

    private static CardView k4Card;
    private static CardView p1440Card;
    private static CardView p1080Card;
    private static CardView p720Card;
    private static CardView p480Card;
    private static CardView p360Card;
    private static CardView p240Card;

    private static CardView largeCard;
    private static CardView mediumCard;
    private static CardView smallCard;
    private static CardView tinyCard;

    private static View v;
    private static TrendingDataRequest trendingDataRequest;
    private static getHomeVideo get_HomeVideo;
    private static int TRENDING = 0;
    private static int RECOMMENDED = 1;

    private static boolean refresh = true;
    private static ShimmerFrameLayout shimmer;
    private static ShimmerFrameLayout home_vide_shimmer;
    private static LinearLayoutManager linearLayoutManager;
    private static RecyclerView recyclerView;
    private static MyRecycle myRecycle;
    private static HomeVideoAdapter homeVideoAdapter;
    private static FavoriteViewModel favoriteViewModel;
    private static CipherViewModel cipherViewModel;
    private static DecryptCipher decryptCipher;

    private static List<VideoObject> trendVideoList = new ArrayList<>();
    private static List<VideoObject> recommendedVideoList = new ArrayList<>();

    private static FavoriteIndex favoriteIndex;
    private static String TREND_TAG = "TREND";
    private static String RECOMMEND_TAG = "RECOMMEND";



    public static boolean isFirstTime() {
        return isFirstTime;
    }

    public static void setIsFirstTime(boolean isFirstTime) {
        HomeFragment.isFirstTime = isFirstTime;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteViewModel = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(FavoriteViewModel.class);

        favoriteIndex = FavoriteIndex.getInstance();

        cipherViewModel = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity()
                        .getApplication()))
                .get(CipherViewModel.class);

        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_fragment, container, false);

        //doLast()

        shimmer = v.findViewById(R.id.shimmerTrend);
        home_vide_shimmer = v.findViewById(R.id.homeVideoShimmer);

        backImg = v.findViewById(R.id.backImg);
        backImg.setVisibility(View.GONE);

        //make my costume tool bar, will be the main bar
        homeBar = v.findViewById(R.id.home_bar);
        ((AppCompatActivity) activity).setSupportActionBar(homeBar);
        watchFragment = new WatchFragment();
        downloadWindow = new Dialog(activity);

        SearchView mEdit = (SearchView) v.findViewById(R.id.home_search);
        mEdit.clearFocus();

        FrameLayout frameLayout = v.findViewById(R.id.search_frame_for_click);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("-------- ************************ ---------------");

                SearchFragment searchFragment = new SearchFragment();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.searchFragment, searchFragment);
                fragmentTransaction.commit();
                isFirstTime = false;
            }
        });

        if (refresh) {
            getData();
            refresh = false;
            favoriteData();
        } else {
            displayTrendVideos(activity);
            displayHomeVideos();
            favoriteData();
            for (int position = 0; position < trendVideoList.size(); position++) {
            }
        }

        return v;
    }

    private void favoriteData() {
        favoriteViewModel.getAllFavorites().observe(activity, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                favoriteIndex.passIds(favorites);
                if (myRecycle != null) {
                    myRecycle.notifyDataSetChanged();
                }
                if (homeVideoAdapter != null) {
                    homeVideoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addToFavorite(VideoObject videoObject) {
        if (favoriteIndex.Id(videoObject.getVideoId())) {
            deleteFromFavorite(videoObject);
        } else {
            favoriteViewModel.insert(new Favorite(videoObject));
        }

        Toast toast = Toast.makeText(activity, "Item Added to Favor List", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void deleteFromFavorite(VideoObject videoObject) {
//        Favorite favorite = new Favorite(youtubeData.getVideoTitle().get(position), youtubeData.getChannelName().get(position)
//                , youtubeData.getVideoViews().get(position), youtubeData.getVideoDate().get(position)
//                , youtubeData.getVideoTime().get(position), false, youtubeData.getVideoThumbnail().get(position)
//                , youtubeData.getChannelPicture().get(position), youtubeData.getVideoId().get(position));
//
//        favoriteViewModel.delete(favorite);
        favoriteIndex.removeId(videoObject.getVideoId());
        favoriteViewModel.deleteById(videoObject.getVideoId());
        //remove by value
        //TrendPositions.remove(Integer.valueOf(position));

        Toast toast = Toast.makeText(activity, "Item deleted from Favorite List", Toast.LENGTH_SHORT);
        toast.show();
        //youtubeData.getHeartIcon().set(position, R.drawable.ic_save_for_later);
        myRecycle.notifyDataSetChanged();
    }

    public void getData() {
        //!youtubeData.isDataFetched()
        shimmer.startShimmer();
        trendingDataRequest = new TrendingDataRequest(new TestCallBack() {
            @Override
            public void trend(List<VideoObject> localVideoObjectList) {
                //
                System.out.println("---+--- Trend list: "+localVideoObjectList.size());
                trendVideoList = localVideoObjectList;
                isThereData = true;
                displayTrendVideos(activity);
            }

            @Override
            public void recommended(List<VideoObject> videoObjectList) {

            }
        });
        trendingDataRequest.execute();

        home_vide_shimmer.startShimmer();
        get_HomeVideo = new getHomeVideo();
        get_HomeVideo.execute();
    }

    private void sendData(Fragment fragment, VideoObject videoObject) {
        Bundle bundle = new Bundle();
        bundle.putString("from", "HomeFeed");
        bundle.putString("video_id", videoObject.getVideoId());
        bundle.putString("video_thumbnail", videoObject.getThumbnail());
        bundle.putString("video_title", videoObject.getTitle());
        bundle.putString("video_views", videoObject.getViews());
        bundle.putString("video_date", videoObject.getDate());
        bundle.putString("video_time", videoObject.getTime());
        bundle.putString("channel_name", videoObject.getBy());
        bundle.putString("channel_pic", videoObject.getChannelThumbnail());
        fragment.setArguments(bundle);
    }

    private void passFragment(int position, int dataInput) {
        WatchFragment watchFragment = new WatchFragment();
        /**Check from where data come to pass it in the right way.
         * there are two source of data the first one is Trending videos and the second one is the Recommended videos**/
        if (dataInput == 0) {
            sendData(watchFragment, trendVideoList.get(position));
        } else {
            sendData(watchFragment, recommendedVideoList.get(position));
        }
        //Open Watch Fragment
        openFragment(isFirstTime, watchFragment);

    }

    private void openFragment(boolean IsFirstTime, WatchFragment watchFragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (IsFirstTime) {
            fragmentTransaction.add(R.id.watchFragment, watchFragment);
            isFirstTime = false;
        } else {
            fragmentTransaction.replace(R.id.watchFragment, watchFragment);
            ((HomeActivity) activity).frameLayoutSwitch();
        }
        fragmentTransaction.commit();
    }

    public void displayTrendVideos(FragmentActivity fragmentActivity) {
        //= activity;
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //youtubeData.setDataFetched(true);
                //shimmer stop
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);


                // Stuff that updates the UI
                linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                recyclerView = v.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(linearLayoutManager);
                myRecycle = new MyRecycle(activity, trendVideoList);
                recyclerView.setAdapter(myRecycle);

                myRecycle.setOnHeartClickListener(new MyRecycle.OnItemClickListener() {
                    @Override
                    public void onHeartClick(int position) {
                        addToFavorite(trendVideoList.get(position));
                    }

                    @Override
                    public void onThumbnailClick(int position) {
                        passFragment(position, TRENDING);
                    }

                    @Override
                    public void onDownloadClick(int position) {
                        if(HomeActivity.isPermission){
                            getVideoStreamLink downloadUrls = new getVideoStreamLink(context, new FetchDownloadingLinksCallback() {
                                @Override
                                public void callback(List<VideoStreamObject> videoStreamObjectList) {
                                    //
                                    System.out.println("------------ stream size: " + videoStreamObjectList.size() + " ------------");
                                    generateDownloadingOption(position, videoStreamObjectList, TREND_TAG);
                                }
                            });
                            downloadUrls.execute("https://www.youtube.com/watch?v=" + trendVideoList.get(position).getVideoId());
                            createDownloadDialog(position, trendVideoList);
                        }else {
                            Toast.makeText(getContext(), R.string.StorageMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    private void displayHomeVideos() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //code
                home_vide_shimmer.stopShimmer();
                home_vide_shimmer.setVisibility(View.GONE);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                RecyclerView HomerecyclerView = v.findViewById(R.id.homeVideoRecycle);
                NestedScrollView nestedScrollView = v.findViewById(R.id.home_nested);
                HomerecyclerView.setLayoutManager(linearLayoutManager);


                homeVideoAdapter = new HomeVideoAdapter(recommendedVideoList);
                HomerecyclerView.setAdapter(homeVideoAdapter);

                homeVideoAdapter.setOnItemClickListener(new HomeVideoAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
//                                        //Open Watch Fragment
                        passFragment(position, RECOMMENDED);
                    }

                    @Override
                    public void onDownloadClick(int position) {
                        if(HomeActivity.isPermission){
                            getVideoStreamLink downloadUrls = new getVideoStreamLink(context, new FetchDownloadingLinksCallback() {
                                @Override
                                public void callback(List<VideoStreamObject> videoStreamObjectList) {
                                    //
                                    System.out.println("------------ stream size: " + videoStreamObjectList.size() + " ------------");
                                    generateDownloadingOption(position, videoStreamObjectList, RECOMMEND_TAG);
                                }
                            });
                            downloadUrls.execute("https://www.youtube.com/watch?v=" + recommendedVideoList.get(position).getVideoId());
                            createDownloadDialog(position, recommendedVideoList);
                        }else { Toast.makeText(getContext(), R.string.StorageMsg, Toast.LENGTH_LONG).show(); }
                    }

                    @Override
                    public void onHeartClick(int position) {
                        System.out.println("Heart Clicked");
                        addToFavorite(recommendedVideoList.get(position));
                    }
                });
                //This part for loading more videos make sure to improve it!
                //Detect lest item
//                nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                    @Override
//                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                        if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() & isMoreVideoLoaded != true) {
//
//                            System.out.println("77777777777777777777777777777777777");
//                            System.out.println("I'm in the last index");
//                            System.out.println("77777777777777777777777777777777777");
//                            progressBar = v.findViewById(R.id.homeFeedSpinner);
//                            progressBar.setVisibility(View.VISIBLE);
//                            isMoreVideoLoaded = true;
//
//                            int listSize = get_HomeVideo.getChannelImgUrl().size();
//                            get_HomeVideo.loadMore();
//
//                            java.util.Timer loadMoreDataTimer = new Timer();
//                            loadMoreDataTimer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    System.out.println("note yet");
//                                    if (listSize < get_HomeVideo.getBadgeIcon().size()) {
//
//                                        activity.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                // Stuff that updates the UI
//                                                System.out.println("I get data");
//                                                progressBar.setVisibility(View.GONE);
//                                                //System.out.println("orignal list size: "+listSize);
//                                                //System.out.println("current list size: "+getData.getChannelImgUrl().size());
//                                                int rr = get_HomeVideo.getChannelImgUrl().size() - listSize;
//                                                //System.out.println("how many data added: "+ rr);
//
//
//                                                //relatedVideoAdapter.notifyDataSetChanged();
//                                                //notifyItemRangeInserted(insertIndex, items.size());
//
//
//                                                homeVideoAdapter.notifyItemRangeInserted(get_HomeVideo.getVideoId().size(), get_HomeVideo.getVideoId().size() - listSize);
//                                                isMoreVideoLoaded = false;
//                                            }
//                                        });
//                                        loadMoreDataTimer.cancel();
//                                    }
//                                }
//                            }, 0, 1000);
//                        }
//                    }
//
//
//                });
            }
        });
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askForPermission();
        } else {
            isPermissionGranted = true;
            //sleep = false;
            isPermissionResponse = true;
            //permissionChecker.run(execute);
        }
    }

    public void askForPermission() {
        // You can directly ask for the permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("-------------------------");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            System.out.println("------------- Value: Allow");
            isPermissionGranted = true;
        } else {
            System.out.println("------------- Value: Deny");
            isPermissionGranted = false;
        }
        sleep = false;
        //permissionChecker.run();
        isPermissionResponse = true;
    }

    @Override
    public void trend(List<VideoObject> localVideoObjectList) {
        System.out.println("Trend list: "+localVideoObjectList.size());
        trendVideoList = localVideoObjectList;
        isThereData = true;
        displayTrendVideos(activity);
    }

    @Override
    public void recommended(List<VideoObject> localRecommendedVideoList) {
        recommendedVideoList = localRecommendedVideoList;
        displayHomeVideos();
    }

    private void createDownloadDialog(int position, List<VideoObject> videoList) {
        downloadWindow.setContentView(R.layout.my_dialog);
        downloadWindowThumbnail = downloadWindow.findViewById(R.id.thumbnailRoundedImageView);
        downloadWindowTitle = downloadWindow.findViewById(R.id.titleOfDownloadFile);
        downloadWindowTime = downloadWindow.findViewById(R.id.downloadTime);

        waiting_download_links_spinner_layout = downloadWindow.findViewById(R.id.waiting_download_links_spinner_layout);
        download_links_layout = downloadWindow.findViewById(R.id.download_links_layout);

        p240Card = downloadWindow.findViewById(R.id.p240Card);
        p360Card = downloadWindow.findViewById(R.id.p360Card);
        p480Card = downloadWindow.findViewById(R.id.p480Card);
        p720Card = downloadWindow.findViewById(R.id.p720Card);
        p1080Card = downloadWindow.findViewById(R.id.p1080Card);
        p1440Card = downloadWindow.findViewById(R.id.p1440Card);
        k4Card = downloadWindow.findViewById(R.id.k4Card);

        p240 = downloadWindow.findViewById(R.id.p240);
        p360 = downloadWindow.findViewById(R.id.p360);
        p480 = downloadWindow.findViewById(R.id.p480);
        p720 = downloadWindow.findViewById(R.id.p720);
        p1080 = downloadWindow.findViewById(R.id.p1080);
        p1440 = downloadWindow.findViewById(R.id.p1440);
        k4 = downloadWindow.findViewById(R.id.k4);

        p240MbSize = downloadWindow.findViewById(R.id.p240MbSize);
        p360MbSize = downloadWindow.findViewById(R.id.p360MbSize);
        p480MbSize = downloadWindow.findViewById(R.id.p480MbSize);
        p720MbSize = downloadWindow.findViewById(R.id.p720MSize);
        p1080MbSize = downloadWindow.findViewById(R.id.p1080MbSize);
        p1440MbSize = downloadWindow.findViewById(R.id.p1440MbSize);
        k4MbSize = downloadWindow.findViewById(R.id.k4MbSize);

        tinyCard = downloadWindow.findViewById(R.id.tinyCard);
        smallCard = downloadWindow.findViewById(R.id.smallCard);
        mediumCard = downloadWindow.findViewById(R.id.mediumCard);
        largeCard = downloadWindow.findViewById(R.id.largCard);

//        tiny = downloadWindow.findViewById(R.id.tiny);
//        small = downloadWindow.findViewById(R.id.small);
//        medium = downloadWindow.findViewById(R.id.medium);
//        large = downloadWindow.findViewById(R.id.large);

        tinyMbSize = downloadWindow.findViewById(R.id.tinyMbSize);
        smallMbSize = downloadWindow.findViewById(R.id.smallMbSize);
        mediumMbSize = downloadWindow.findViewById(R.id.mediumMbSize);
        largeMbSize = downloadWindow.findViewById(R.id.largeMbSize);

        tinyBitrateSize = downloadWindow.findViewById(R.id.tinyBiterateSize);
        smallBitrateSize = downloadWindow.findViewById(R.id.smallBiterateSize);
        mediumBitrateSize = downloadWindow.findViewById(R.id.mediumBiterateSize);
        largeBitrateSize = downloadWindow.findViewById(R.id.largeBitrateSize);


        Picasso.get()
                .load(videoList.get(position).getThumbnail())
                .centerCrop()
                .fit()
                .into(downloadWindowThumbnail);
        downloadWindowTitle.setText(videoList.get(position).getTitle());
        downloadWindowTime.setText(videoList.get(position).getTime());

        downloadWindow.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        downloadWindow.show();
    }

    public void generateDownloadingOption(int position, List<VideoStreamObject> videoStreamObjectList, String location) {
        //downloadUrls.isStreamLinksReady()

        is4KFound = false;
        is1080Found = false;
        is720Found = false;
        is480Found = false;
        is360Found = false;
        is240Found = false;
        isLargeFound = false;
        isMediumFound = false;
        isSmallFound = false;
        isTinyFound = false;
        //downloadUrls.setIsStreamLinksReady(false);

        //
        for (int c = 0; c < videoStreamObjectList.size(); c++) {
            //Calculate InProgressFile Size
            //1048576
            try {
                double mbUnite = 14857;
                double kbUnite = 1000;
                DecimalFormat df = new DecimalFormat("#.##");

                double result = videoStreamObjectList.get(c).getContentLength() / (1024 * 1024);
                //downloadUrls.getContentLength().get(c) / mbUnite;
                double bitRate = videoStreamObjectList.get(c).getBitrate() / kbUnite;
                //downloadUrls.getBitrate().get(c) / kbUnite;

                //falter data
                falterQuality(videoStreamObjectList.get(c).getQuality(), /*Double.valueOf(df.format(result))*/result, videoStreamObjectList.get(c).getMimeType(), bitRate, videoStreamObjectList.get(c).getDownloadLink());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("At index: [" + c + "]");
            }

        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                optionButtonsListener(position, location);
            }
        });
    }

    public void falterQuality(String quality, double fileSize, String audioType, double bitRate, String downloadLink) {

        System.out.println("Faltering .... " + "Quality: " + quality);
        //hd2160
        if (is4KFound != true) {
            k4DownloadLink = downloadLink;
            if (quality.equals("hd2160")) {
                k4.setVisibility(View.VISIBLE);
                is4KFound = true;
                k4MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                k4.setVisibility(View.GONE);
            }
        }
        //hd1440
        if (is1440Found != true) {
            if (quality.equals("hd1440")) {
                p1440DownloadLink = downloadLink;
                p1440.setVisibility(View.VISIBLE);
                is1440Found = true;
                p1440MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p1440.setVisibility(View.GONE);
            }
        }
        //hd1080
        if (is1080Found != true) {
            if (quality.equals("hd1080")) {
                p1080DownloadLink = downloadLink;
                p1080.setVisibility(View.VISIBLE);
                is1080Found = true;
                p1080MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p1080.setVisibility(View.GONE);
            }
        }
        //hd720
        if (is720Found != true) {
            if (quality.equals("hd720")) {
                p720DownloadLink = downloadLink;
                p720.setVisibility(View.VISIBLE);
                is720Found = true;
                p720MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p720.setVisibility(View.GONE);
            }
        }
        //480p
        if (is480Found != true) {
            if (quality.equals("large")) {
                p480DownloadLink = downloadLink;
                p480.setVisibility(View.VISIBLE);
                is480Found = true;
                p480MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p480.setVisibility(View.GONE);
            }
        }
        //360p
        if (is360Found != true) {
            if (quality.equals("medium")) {
                p360DownloadLink = downloadLink;
                p360.setVisibility(View.VISIBLE);
                is360Found = true;
                p360MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p360.setVisibility(View.GONE);
            }
        }
        //240p
        if (is240Found != true) {
            if (quality.equals("small")) {
                p240DownloadLink = downloadLink;
                p240.setVisibility(View.VISIBLE);
                is240Found = true;
                p240MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p240.setVisibility(View.GONE);
            }
        }

        //Audio part
        //find mp4a audio
        //we will need this audio format if we want to download almost any video
        if (audioType.equals("audio/mp4; codecs=\"mp4a.40.2\"")) {
            System.out.println("I found mp4 audio");
            aacDownloadLink = downloadLink;
        }
        //Large
        if (isLargeFound != true & audioType.contains("audio")) {
            largeDownloadLink = downloadLink;
            isLargeFound = true;
            largeMbSize.setText(String.format("%.2f", fileSize) + "MB");
            largeBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
        } else {
            //Medium
            if (isMediumFound != true & audioType.contains("audio")) {
                mediumDownloadLink = downloadLink;
                isMediumFound = true;
                mediumMbSize.setText(String.format("%.2f", fileSize) + "MB");
                mediumBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
            } else {
                //Small
                if (isSmallFound != true & audioType.contains("audio")) {
                    smallDownloadLink = downloadLink;
                    isSmallFound = true;
                    smallMbSize.setText(String.format("%.2f", fileSize) + "MB");
                    smallBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                } else {
                    //Tiny
                    if (isTinyFound != true & audioType.contains("audio")) {
                        tinyDownloadLink = downloadLink;
                        isTinyFound = true;
                        tinyMbSize.setText(String.format("%.2f", fileSize) + "MB");
                        tinyBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                    }
                }
            }
        }


    }

    private void optionButtonsListener(int position, String location) {
        waiting_download_links_spinner_layout.setVisibility(View.GONE);
        download_links_layout.setVisibility(View.VISIBLE);

        List<VideoObject> localVideoList;
        if (location.equals("TREND")) {
            localVideoList = trendVideoList;
        } else {
            localVideoList = recommendedVideoList;
        }

        //Video part
        k4Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (k4DownloadLink != null) {
                    if (k4DownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "video", k4DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                k4DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(position, localVideoList, "video", k4DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(k4DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p1080Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p1080DownloadLink != null) {//                                                                              MyIntentService myIntentService = new MyIntentService();
//                                                                              myIntentService.setIsConverted(false);
//
//
//                                                                              //send data to service to start download file
                    //try this part of code
                    //DownloadService ds = new DownloadService();
//                    System.out.println("download_video_link: " + p240DownloadLink);
//                    System.out.println("download_audio_link: " + aacDownloadLink);

                    //Check links if need to DecryptCipher
                    if (p1080DownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "video", p1080DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p1080DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(position, localVideoList, "video", p1080DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p1080DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p1440Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p1440DownloadLink != null) {
                    if (p1440DownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "video", p1440DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p1440DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(position, localVideoList, "video", p1440DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p1440DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p720Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p720DownloadLink != null) {
                    if (p720DownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "video", p720DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p720DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(position, localVideoList, "video", p720DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p720DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p480Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p480DownloadLink != null) {
                    if (p480DownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "video", p480DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p480DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(position, localVideoList, "video", p480DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p480DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p360Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p360DownloadLink != null) {
                    if (p360DownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "video", p360DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p360DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(position, localVideoList, "video", p360DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p360DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p240Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p240DownloadLink != null) {//                                                                              MyIntentService myIntentService = new MyIntentService();
//                                                                              myIntentService.setIsConverted(false);
//
//
//                                                                              //send data to service to start download file
                    //try this part of code
                    //DownloadService ds = new DownloadService();
                    System.out.println("download_video_link: " + p240DownloadLink);
                    System.out.println("download_audio_link: " + aacDownloadLink);

                    //Check links if need to DecryptCipher
                    if (p240DownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "video", p240DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p240DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(position, localVideoList, "video", p240DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p240DownloadLink, "F");
                    }
                    downloadWindow.cancel();

                    Toast.makeText(activity
                            , "Start Downloading " + trendVideoList.get(position).getTitle()
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Audio Part
        largeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (largeDownloadLink != null) {
                    if (largeDownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", largeDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");


                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                largeDownloadLink = stream;
                                startDownload(position, localVideoList, "audio", largeDownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(largeDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        mediumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediumDownloadLink != null) {
                    if (mediumDownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", mediumDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                mediumDownloadLink = stream;
                                startDownload(position, localVideoList, "audio", mediumDownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(mediumDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        smallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smallDownloadLink != null) {
                    if (smallDownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", smallDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                smallDownloadLink = stream;
                                startDownload(position, localVideoList, "audio", smallDownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(smallDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        tinyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDownloadLink != null) {
                    if (tinyDownloadLink.startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", tinyDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                tinyDownloadLink = stream;
                                startDownload(position, localVideoList, "audio", tinyDownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(tinyDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });

    }

    private void decryptAudio() {
        decryptCipher.decrypt(aacDownloadLink, "S");
    }

    private void startDownload(int position, List<VideoObject> videoList, String type, String videoLink, String audioLink) {
        Intent i = new Intent(activity, testService.class);
        i.putExtra("video_id", videoList.get(position).getVideoId());

        if (type.equals("video")) {
            i.putExtra("download_type", "video");
        } else {
            i.putExtra("download_type", "audio");
        }

        i.putExtra("download_video_link", videoLink);
        i.putExtra("download_audio_link", audioLink);
        i.putExtra("file_name", videoList.get(position).getTitle());
        i.putExtra("thumbnail", videoList.get(position).getThumbnail());
        i.putExtra("by", videoList.get(position).getBy());
        i.putExtra("time_line", videoList.get(position).getTime());
        Toast.makeText(getContext(), "it'll take a bit to start downloading",Toast.LENGTH_SHORT);
        activity.startService(i);
    }
}