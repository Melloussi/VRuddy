package com.app.vruddy.Views.Fragment;

import static com.app.vruddy.Data.Global.GlobalConstantVariables.*;

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

import com.app.vruddy.Data.DataClasses.QualityAvailability;
import com.app.vruddy.Data.DataClasses.QualityDownloadLink;
import com.app.vruddy.Views.Adapter.HomeVideoAdapter;
import com.app.vruddy.Models.AsyncTask.TrendingDataRequest;
import com.app.vruddy.Views.Adapter.MyRecycle;
import com.app.vruddy.Models.AsyncTask.getVideoStreamLink;
import com.app.vruddy.Models.DecryptCipher.DecryptCipher;
import com.app.vruddy.Models.DecryptCipher.ResultCallBack;
import com.app.vruddy.Models.Interfaces.FetchDownloadingLinksCallback;
import com.app.vruddy.Models.Objects.VideoStreamObject;
import com.app.vruddy.R;
import com.app.vruddy.Models.TestCallBack;
import com.app.vruddy.Views.Activities.HomeActivity;
import com.app.vruddy.Models.Objects.VideoObject;
import com.app.vruddy.Data.database.Cipher.CipherViewModel;
import com.app.vruddy.Data.database.Favorite.Favorite;
import com.app.vruddy.Data.database.Favorite.FavoriteIndex;
import com.app.vruddy.Data.database.Favorite.FavoriteViewModel;
import com.app.vruddy.Models.AsyncTask.getHomeVideo;
import com.app.vruddy.Data.Background.Services.testService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TestCallBack {

    private BottomNavigationView bottomNavigationView;
//    private boolean screenState = false;
    static boolean isThereData = false;
    private static boolean isMoreVideoLoaded = false;
//    private Context context;
    private static boolean isFirstTime = true;
    private WatchFragment watchFragment;
    private static FragmentActivity activity;

    private static QualityAvailability qualityAvailability;
    private static QualityDownloadLink qualityDownloadLink;

    private static boolean isPermissionGranted = false;
    private static boolean isPermissionResponse = false;

    private static Dialog downloadWindow;
    private TextView
            p240MbSize
            ,p360MbSize
            ,p480MbSize
            ,p720MbSize
            ,p1080MbSize
            ,p1440MbSize
            ,k4MbSize

            ,tinyMbSize
            ,smallMbSize
            ,mediumMbSize
            ,largeMbSize
            ,tinyBitrateSize
            ,smallBitrateSize
            ,mediumBitrateSize
            ,largeBitrateSize;


    private LinearLayout download_links_layout;
    private RelativeLayout
            waiting_download_links_spinner_layout,
            p240,
            p360,
            p480,
            p720,
            p1080,
            p1440,
            k4;

    private static CardView
            k4Card,
            p1440Card,
            p1080Card,
            p720Card,
            p480Card,
            p360Card,
            p240Card,

            largeCard,
            mediumCard,
            smallCard,
            tinyCard;

    private static View view;

    private static boolean refresh = true;
    private static ShimmerFrameLayout shimmer;
    private static ShimmerFrameLayout home_vide_shimmer;
    private static LinearLayoutManager linearLayoutManager;
    private static RecyclerView recyclerView;
    private MyRecycle myRecycle;
    private static HomeVideoAdapter homeVideoAdapter;
    private static FavoriteViewModel favoriteViewModel;
    private static CipherViewModel cipherViewModel;
    private static DecryptCipher decryptCipher;

    private static List<VideoObject> trendVideoList = new ArrayList<>();
    private static List<VideoObject> recommendedVideoList = new ArrayList<>();

    private static FavoriteIndex favoriteIndex;

    private HomeActivity homeActivity;



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

        activity = requireActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        //doLast()

        homeActivity = (HomeActivity) getActivity();
        qualityDownloadLink = new QualityDownloadLink();



        shimmer = view.findViewById(R.id.shimmerTrend);
        home_vide_shimmer = view.findViewById(R.id.homeVideoShimmer);

        ImageView backImg = view.findViewById(R.id.backImg);
        backImg.setVisibility(View.GONE);

        //make my costume tool bar, will be the main bar
        //Variables
        Toolbar homeBar = view.findViewById(R.id.home_bar);
        ((AppCompatActivity) activity).setSupportActionBar(homeBar);
        watchFragment = new WatchFragment();
        downloadWindow = new Dialog(activity);

        SearchView mEdit = (SearchView) view.findViewById(R.id.home_search);
        mEdit.clearFocus();

        FrameLayout frameLayout = view.findViewById(R.id.search_frame_for_click);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.manager.fragmentManager(SEARCH_FRAGMENT);
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

        return view;
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
        //
        TrendingDataRequest trendingDataRequest = new TrendingDataRequest(new TestCallBack() {
            @Override
            public void trend(List<VideoObject> localVideoObjectList) {
                //
                System.out.println("---+--- Trend list: " + localVideoObjectList.size());
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
        getHomeVideo get_HomeVideo = new getHomeVideo();
        get_HomeVideo.execute();
    }

    private void feedBundle(Fragment fragment, VideoObject videoObject) {
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
        if (dataInput == 0) {
            feedBundle(watchFragment, trendVideoList.get(position));
        } else {
            feedBundle(watchFragment, recommendedVideoList.get(position));
        }
        openFragment(isFirstTime, watchFragment);

    }

    private void openFragment(boolean IsFirstTime, WatchFragment watchFragment) {
        if (IsFirstTime) {
            homeActivity.manager.fragmentManager(WATCH_FRAGMENT, watchFragment);
            isFirstTime = false;
        } else {
            homeActivity.manager.fragmentManager(WATCH_FRAGMENT, watchFragment);
            homeActivity.frameLayoutVisibility();
        }
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
                recyclerView = view.findViewById(R.id.recycler_view);
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
                            getVideoStreamLink downloadUrls = new getVideoStreamLink(requireContext(), new FetchDownloadingLinksCallback() {
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
                RecyclerView HomerecyclerView = view.findViewById(R.id.homeVideoRecycle);
                NestedScrollView nestedScrollView = view.findViewById(R.id.home_nested);
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
                            getVideoStreamLink downloadUrls = new getVideoStreamLink(requireContext(), new FetchDownloadingLinksCallback() {
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
        //    private static boolean sleep = true;
        boolean sleep = false;
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
        //    private static TextView downloadWindowTime;
        ImageView downloadWindowThumbnail = downloadWindow.findViewById(R.id.thumbnailRoundedImageView);
        TextView downloadWindowTitle = downloadWindow.findViewById(R.id.titleOfDownloadFile);
        TextView downloadWindowTime = downloadWindow.findViewById(R.id.downloadTime);

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

        qualityAvailability = new QualityAvailability();


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
        if (!qualityAvailability.isIs4KFound()) {
            qualityDownloadLink.setK4DownloadLink(downloadLink);
            if (quality.equals("hd2160")) {
                k4.setVisibility(View.VISIBLE);
//                is4KFound = true;
                qualityAvailability.setIs4KFound(true);
                k4MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                k4.setVisibility(View.GONE);
            }
        }
        //hd1440
        if (!qualityAvailability.isIs1440Found()) {
            if (quality.equals("hd1440")) {
                qualityDownloadLink.setP1440DownloadLink(downloadLink);
                p1440.setVisibility(View.VISIBLE);
                qualityAvailability.setIs1440Found(true);
                p1440MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p1440.setVisibility(View.GONE);
            }
        }
        //hd1080
        if (!qualityAvailability.isIs1080Found()) {
            if (quality.equals("hd1080")) {
                qualityDownloadLink.setP1080DownloadLink(downloadLink);
                p1080.setVisibility(View.VISIBLE);
                qualityAvailability.setIs1080Found(true);
                p1080MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p1080.setVisibility(View.GONE);
            }
        }
        //hd720
        if (!qualityAvailability.isIs720Found()) {
            if (quality.equals("hd720")) {
                qualityDownloadLink.setP720DownloadLink(downloadLink);
                p720.setVisibility(View.VISIBLE);
                qualityAvailability.setIs720Found(true);
                p720MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p720.setVisibility(View.GONE);
            }
        }
        //480p
        if (!qualityAvailability.isIs480Found()) {
            if (quality.equals("large")) {
                qualityDownloadLink.setP480DownloadLink(downloadLink);
                p480.setVisibility(View.VISIBLE);
                qualityAvailability.setIs480Found(true);
                p480MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p480.setVisibility(View.GONE);
            }
        }
        //360p
        if (!qualityAvailability.isIs360Found()) {
            if (quality.equals("medium")) {
                qualityDownloadLink.setP360DownloadLink(downloadLink);
                p360.setVisibility(View.VISIBLE);
                qualityAvailability.setIs360Found(true);
                p360MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p360.setVisibility(View.GONE);
            }
        }
        //240p
        if (!qualityAvailability.isIs240Found()) {
            if (quality.equals("small")) {
                System.out.println("-------> p240 download link: "+downloadLink);
                qualityDownloadLink.setP240DownloadLink(downloadLink);
                p240.setVisibility(View.VISIBLE);
                qualityAvailability.setIs240Found(true);
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
            qualityDownloadLink.setAacDownloadLink(downloadLink);
        }
        //Large
        if (!qualityAvailability.isLargeFound() & audioType.contains("audio")) {
            qualityDownloadLink.setLargeDownloadLink(downloadLink);
            qualityAvailability.setLargeFound(true);
            largeMbSize.setText(String.format("%.2f", fileSize) + "MB");
            largeBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
        } else {
            //Medium
            if (!qualityAvailability.isMediumFound() & audioType.contains("audio")) {
                qualityDownloadLink.setMediumDownloadLink(downloadLink);
                qualityAvailability.setMediumFound(true);
                mediumMbSize.setText(String.format("%.2f", fileSize) + "MB");
                mediumBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
            } else {
                //Small
                if (!qualityAvailability.isSmallFound() & audioType.contains("audio")) {
                    qualityDownloadLink.setSmallDownloadLink(downloadLink);
                    qualityAvailability.setSmallFound(true);
                    smallMbSize.setText(String.format("%.2f", fileSize) + "MB");
                    smallBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                } else {
                    //Tiny
                    if (!qualityAvailability.isTinyFound() & audioType.contains("audio")) {
                        qualityDownloadLink.setTinyDownloadLink(downloadLink);
                        qualityAvailability.setTinyFound(true);
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
                if (qualityDownloadLink.getK4DownloadLink() != null) {
                    if (qualityDownloadLink.getK4DownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "video", qualityDownloadLink.getK4DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setK4DownloadLink(stream);
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                qualityDownloadLink.setAacDownloadLink(stream);
                                startDownload(position, localVideoList, "video"
                                        , qualityDownloadLink.getK4DownloadLink()
                                        , qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getK4DownloadLink(), "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p1080Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //Check links if need to DecryptCipher

                    if (qualityDownloadLink.getP1080DownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "video", qualityDownloadLink.getP1080DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setP1080DownloadLink(stream);
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                qualityDownloadLink.setAacDownloadLink(stream);
                                startDownload(position, localVideoList, "video", qualityDownloadLink.getP1080DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getP1080DownloadLink(), "F");
                    }
                    downloadWindow.cancel();
            }
        });
        //
        p1440Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qualityDownloadLink.getP1440DownloadLink() != null) {
                    if (qualityDownloadLink.getP1440DownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "video", qualityDownloadLink.getP1440DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setP1440DownloadLink(stream);

                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                qualityDownloadLink.setAacDownloadLink(stream);
                                startDownload(position, localVideoList, "video", qualityDownloadLink.getP1440DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getP1440DownloadLink(), "F");
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
                if (qualityDownloadLink.getP720DownloadLink() != null) {
                    if (qualityDownloadLink.getP720DownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "video", qualityDownloadLink.getP720DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setP720DownloadLink(stream);
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                qualityDownloadLink.setAacDownloadLink(stream);
                                startDownload(position, localVideoList, "video", qualityDownloadLink.getP720DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getP720DownloadLink(), "F");
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
                if (qualityDownloadLink.getP480DownloadLink() != null) {
                    if (qualityDownloadLink.getP480DownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "video", qualityDownloadLink.getP480DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setP480DownloadLink(stream);
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                qualityDownloadLink.setAacDownloadLink(stream);
                                startDownload(position, localVideoList, "video", qualityDownloadLink.getP480DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getP480DownloadLink(), "F");
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
                if (qualityDownloadLink.getP360DownloadLink() != null) {
                    if (qualityDownloadLink.getP360DownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "video", qualityDownloadLink.getP360DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setP360DownloadLink(stream);
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                qualityDownloadLink.setAacDownloadLink(stream);
                                startDownload(position, localVideoList, "video", qualityDownloadLink.getP360DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getP360DownloadLink(), "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p240Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (qualityDownloadLink.getP240DownloadLink() != null) {//                                                                              MyIntentService myIntentService = new MyIntentService();
//                                                                              myIntentService.setIsConverted(false);
//
//
//                                                                              //send data to service to start download file
                    //try this part of code
                    //DownloadService ds = new DownloadService();
//                    System.out.println("download_video_link: " + p240DownloadLink);
//                    System.out.println("download_audio_link: " + aacDownloadLink);

                    //Check links if need to DecryptCipher
                    if (qualityDownloadLink.getP240DownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "video", qualityDownloadLink.getP240DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setP240DownloadLink(stream);
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                qualityDownloadLink.setAacDownloadLink(stream);
                                startDownload(position, localVideoList, "video", qualityDownloadLink.getP240DownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getP240DownloadLink(), "F");
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
                if (qualityDownloadLink.getLargeDownloadLink() != null) {
                    if (qualityDownloadLink.getLargeDownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", qualityDownloadLink.getLargeDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");


                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setLargeDownloadLink(stream);
                                startDownload(position, localVideoList, "audio", qualityDownloadLink.getLargeDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getLargeDownloadLink(), "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        mediumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qualityDownloadLink.getMediumDownloadLink() != null) {
                    if (qualityDownloadLink.getMediumDownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", qualityDownloadLink.getMediumDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setMediumDownloadLink(stream);
                                startDownload(position, localVideoList, "audio", qualityDownloadLink.getMediumDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getMediumDownloadLink(), "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        smallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qualityDownloadLink.getSmallDownloadLink() != null) {
                    if (qualityDownloadLink.getSmallDownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", qualityDownloadLink.getSmallDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setSmallDownloadLink(stream);
                                startDownload(position, localVideoList, "audio", qualityDownloadLink.getSmallDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getSmallDownloadLink(), "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        tinyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qualityDownloadLink.getTinyDownloadLink() != null) {
                    if (qualityDownloadLink.getTinyDownloadLink().startsWith("https://")) {
                        startDownload(position, localVideoList, "audio", qualityDownloadLink.getTinyDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(localVideoList.get(position).getVideoId(), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                qualityDownloadLink.setTinyDownloadLink(stream);
                                startDownload(position, localVideoList, "audio", qualityDownloadLink.getTinyDownloadLink(), qualityDownloadLink.getAacDownloadLink());
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(qualityDownloadLink.getTinyDownloadLink(), "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });

    }

    private void decryptAudio() {
        decryptCipher.decrypt(qualityDownloadLink.getAacDownloadLink(), "S");
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