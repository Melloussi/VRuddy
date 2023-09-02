package com.app.vruddy.Views.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vruddy.Data.Background.AsyncTask.StreamUrl;
import com.app.vruddy.Models.DecryptCipher.DecryptCipher;
import com.app.vruddy.Models.DecryptCipher.ResultCallBack;
import com.app.vruddy.Models.DecryptCipher.StreamResultCallBack;
import com.app.vruddy.Models.Interfaces.FetchDownloadingLinksCallback;
import com.app.vruddy.Models.Interfaces.RelatedResultCallBack;
import com.app.vruddy.Models.Objects.VideoObject;
import com.app.vruddy.Models.Objects.VideoStreamObject;
import com.app.vruddy.R;
import com.app.vruddy.ViewModels.WatchVM;
import com.app.vruddy.Views.Adapter.RelatedVideoAdapter;
import com.app.vruddy.Views.Activities.HomeActivity;
import com.app.vruddy.Data.Background.AsyncTask.getRelatedVideoData;
import com.app.vruddy.Data.Background.AsyncTask.getVideoStreamLink;
import com.app.vruddy.Data.database.Cipher.CipherViewModel;
import com.app.vruddy.Data.database.Favorite.Favorite;
import com.app.vruddy.Data.database.Favorite.FavoriteIndex;
import com.app.vruddy.Data.database.Favorite.FavoriteViewModel;
import com.app.vruddy.Data.Background.Services.testService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class WatchFragment extends Fragment {


    private static class UIComponents {
        private TextView textTitle, textViews, textDate, titleSmallVersion, textChannelName,
                downloadWindowTitle, downloadWindowTime, p240MbSize, p360MbSize,
                p480MbSize, p720MbSize, p1080MbSize, p1440MbSize, k4MbSize, tinyMbSize,
                smallMbSize, mediumMbSize, largeMbSize, tinyBitrateSize, smallBitrateSize,
                mediumBitrateSize, largeBitrateSize;


        private ImageView channelPic, heartPic, downloadPic, sharePic,
                closeImg, pauseImg, fullScreeen, downloadWindowThumbnail;

        private CardView k4Card, p1440Card, p1080Card, p720Card, p480Card,
                p360Card, p240Card, largeCard, mediumCard, smallCard, tinyCard;

        private LinearLayout download_links_layout;

        private RelativeLayout p240, p360, p480, p720, p1080, p1440, k4,
                waiting_download_links_spinner_layout, tiny, small,
                medium, large;

        private ProgressBar progressBar;
        private MotionLayout motionLayout;

        private YouTubePlayerView youTubePlayerView;
        private PlayerView playerView;

        private ShimmerFrameLayout containerB;

    }

    private final UIComponents uiComponents = new UIComponents();
    private SimpleExoPlayer player;
    private boolean isThereData = false;
    private boolean screenState = false;
    private static boolean isVideoReady = false;
    private static boolean isRelatedVideoLoaded = false;

    private static GestureDetectorCompat mGestureDetectorCompat;
    private static boolean isNeedToMaximize = false;
    private Context context;
    private static Dialog downloadWindow;

    private static getRelatedVideoData getData;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String From;
    private String videoId;
    private String VideoThumbnail;
    private String VideoTitle;
    private String VideoViews;
    private String VideoDate;
    private String VideoTime;
    private String ChannelName;
    private String ChannelPic;


    private HomeActivity homeActivity;
    private static CipherViewModel cipherViewModel;


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
    private static final boolean isPermissionGranted = false;
    private static final boolean sleep = true;
    private static final boolean isPermissionResponse = false;

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

    private static Activity activity;
    private static DecryptCipher decryptCipher;

    private static FavoriteIndex favoriteIndex;
    private static RelatedVideoAdapter relatedVideoAdapter;
    private static FavoriteViewModel favoriteViewModel;
    private static WatchVM watchVM;
    private View view;


    public WatchFragment() {
    }

    public static WatchFragment newInstance(String param1, String param2) {
        WatchFragment fragment = new WatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containerA, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_watch, containerA, false);

//        uiComponents = new UIComponents();
        initializeUIComponents(view);


        homeActivity = (HomeActivity) getActivity();

        watchVM = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(WatchVM.class);


        context = getActivity();


        //--------------------------------

        if (getArguments() != null) {
            From = getArguments().getString("from");
            videoId = getArguments().getString("video_id");
            VideoThumbnail = getArguments().getString("video_thumbnail");
            VideoTitle = getArguments().getString("video_title");
            VideoViews = getArguments().getString("video_views");
            VideoDate = getArguments().getString("video_date");
            VideoTime = getArguments().getString("video_time");
            ChannelName = getArguments().getString("channel_name");
            ChannelPic = getArguments().getString("channel_pic");
        }

        //I Set Value true cause when back button pressed
        //I check Which Fragment is opened to close it
        homeActivity.setWatchFragmentOpen(true);

        cipherViewModel = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity()
                        .getApplication()))
                .get(CipherViewModel.class);

        favoriteViewModel = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(FavoriteViewModel.class);

        favoriteIndex = FavoriteIndex.getInstance();

        activity = getActivity();

        //-------------------------------


        //
        try {
            motionLayoutTransitionListener(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGestureDetectorCompat = new GestureDetectorCompat(getActivity(), new MyGestureListener());
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetectorCompat.onTouchEvent(motionEvent);
                return false;
            }
        });


        //---------- I need to know why I added this part of code below
//        //check if data come from Home feed
//        if(From.equals("HomeFeed")){
//
//            //main video part--------------------------------------------------------------------
//            //get data from "SplachScreen" class for the main video
//            videoId = getIntent().getStringExtra("video_id");
//            videoThumbnail = getIntent().getStringExtra("video_thumbnail");
//            videoTitle = getIntent().getStringExtra("video_title");
//            videoViews = getIntent().getStringExtra("video_views");
//            videoDate = getIntent().getStringExtra("video_date");
//            videoTime = getIntent().getStringExtra("video_time");
//            channelName = getIntent().getStringExtra("channel_name");
//            channelPicUrl = getIntent().getStringExtra("channel_pic");
//        } else if(getIntent().getStringExtra("from").equals("Watch")){
//
//            //main video part--------------------------------------------------------------------
//            //get data from "SplachScreen" class for the main video
//            videoId = getIntent().getStringExtra("video_id");
//            videoThumbnail = getIntent().getStringExtra("video_thumbnail");
//            videoTitle = getIntent().getStringExtra("video_title");
//            videoViews = getIntent().getStringExtra("video_views");
//            videoDate = getIntent().getStringExtra("video_date");
//            videoTime = getIntent().getStringExtra("video_time");
//            channelName = getIntent().getStringExtra("channel_name");
//            channelPicUrl = getIntent().getStringExtra("channel_pic");
//        }

        if (uiComponents.textTitle != null) {
            //pass data to the main video
            uiComponents.titleSmallVersion.setText(VideoTitle);
            uiComponents.textTitle.setText(VideoTitle);
            uiComponents.textViews.setText(VideoViews);
            uiComponents.textDate.setText(VideoDate);
            uiComponents.textChannelName.setText(ChannelName);
            Picasso.get()
                    .load(ChannelName)
                    .transform(new CropCircleTransformation())
                    .into(uiComponents.channelPic);
        }


        //----------------------------------------------------------------------------------------
        //ExoPlayer part -------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------


//        player = new SimpleExoPlayer.Builder(activity).build();
        player = watchVM.getExoPlayer();
        int test = watchVM.getTest();
        System.out.println("--------------- WatchVM Test: " + test);
        System.out.println("--------------- WatchVM Player: " + player);
        if (player == null) {
            player = new SimpleExoPlayer.Builder(activity).build();
            watchVM.setExoPlayer(player);
            watchVM.setTest(1);
        }

        //get watch Stream link
        try {
            uiComponents.downloadPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Download button clicked");

                    if (HomeActivity.isPermission) {
                        VideoObject mVideoObject = new VideoObject(videoId, VideoTitle, ChannelName, VideoTime, VideoDate, VideoViews, false, VideoThumbnail, ChannelPic);
                        getVideoStreamLink downloadUrls = new getVideoStreamLink(activity, new FetchDownloadingLinksCallback() {
                            @Override
                            public void callback(List<VideoStreamObject> videoStreamObjectList) {
                                //
                                generateDownloadingOption(mVideoObject, videoStreamObjectList);
                                System.out.println("------- Top Called");
                            }
                        });
                        downloadUrls.execute("https://www.youtube.com/watch?v=" + mVideoObject.getVideoId());
                        createDownloadDialog(mVideoObject);
                    } else {
                        Toast.makeText(getContext(), R.string.StorageMsg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Error in getting stream link part!");
        }
        try {
            //get Watch Stream
            StreamUrl streamUrl = new StreamUrl(getActivity(), cipherViewModel, new StreamResultCallBack() {
                @Override
                public void result(String url) {
                    //
                    activity.runOnUiThread(new Runnable() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void run() {
                            //code

                            //video url
                            Uri videoUrl = Uri.parse(url);

                            uiComponents.playerView.setPlayer(player);
                            //make video fit frame size
                            uiComponents.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                            //keep screen on
                            uiComponents.playerView.setKeepScreenOn(true);

                            if (test == 0) {
                                // Build the media item.
                                MediaItem mediaItem = MediaItem.fromUri(videoUrl);
                                // Set the media item to be played.
                                player.setMediaItem(mediaItem);
                                // Prepare the player.
                                player.prepare();
                            }

                            if(watchVM.getWatchedTime() != 0){
                                player.seekTo(watchVM.getWatchedTime());
                                player.setPlayWhenReady(watchVM.isPlayState());
                            }

                            try {
                                // Start the playback.
//                                player.play();
                            } catch (Exception e) {
                                System.out.println("Error in play video!");
                            }

                            //listener
                            player.addListener(new Player.EventListener() {
                                @Override
                                public void onPlaybackStateChanged(int state) {

                                    //check if video Still loading
                                    //to take action about progressbar if you should hide it or show it
                                    if (state == Player.STATE_BUFFERING) {
                                        uiComponents.progressBar.setVisibility(View.VISIBLE);
                                    } else if (state == Player.STATE_READY) {
                                        uiComponents.progressBar.setVisibility(View.INVISIBLE);
                                        isVideoReady = true;
                                    }
                                    uiComponents.progressBar.setAlpha(0);
                                }
                            });


                            //full screen
                            uiComponents.fullScreeen.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("UseCompatLoadingForDrawables")
                                @Override
                                public void onClick(View view) {
                                    //check if screen if full or not to take action
//                if(screenState == true){
//                    //change full screen icon to small
//                    //fullScreeen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
//                    //setImageResource(R.drawable.my_image);
//                    fullScreeen.setImageResource(R.drawable.ic_fullscreen_exit);
//
//                    //set PORTRAIT orientation
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//                    View vieww = findViewById(R.id.videoRelativeLayout);
//                    ViewGroup.LayoutParams layoutParams = vieww.getLayoutParams();
//                    layoutParams.height = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    view.setLayoutParams(layoutParams);
//
//                    screenState = false;
//                }else {
//                    //change small screen icon to full
//                    //fullScreeen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
//                    fullScreeen.setImageResource(R.drawable.ic_fullscreen);
//
//                    //set landscape orientation
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    screenState = true;
//                }
                                    //-------------------------------------------------------

                                    ViewGroup.LayoutParams params;
                                    if (screenState) {
                                        uiComponents.fullScreeen.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen));
                                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                                        }
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
//                                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                                        params = uiComponents.playerView.getLayoutParams();

                                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                        params.height = (int) (200 * getActivity().getResources().getDisplayMetrics().density);
                                        uiComponents.playerView.setLayoutParams(params);
                                        screenState = false;


                                        homeActivity.setBottomNaveVisibility(false);
                                        watchVM.setLoadNewData(false);
                                        watchVM.setWatchedTime(player.getCurrentPosition());
                                        watchVM.setPlayState(player.getPlayWhenReady());
                                    } else {
                                        uiComponents.fullScreeen.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_exit));
                                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                                        }
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                                        params = uiComponents.playerView.getLayoutParams();

                                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                        uiComponents.playerView.setLayoutParams(params);
                                        screenState = true;
                                        homeActivity.setBottomNaveVisibility(true);
                                        watchVM.setLoadNewData(false);
                                        watchVM.setWatchedTime(player.getCurrentPosition());
                                        watchVM.setPlayState(player.getPlayWhenReady());
                                    }
                                }
                            });
                        }
                    });
                }
            });
            streamUrl.execute(videoId);
        } catch (Exception e) {
            e.printStackTrace();
        }

//-----------------------------------------------------------------------------------------------------
//        Related Video part --------------------------------------------------------------------------
//
//        addData();

        //Clear previous data
        if (getData != null) {
            getData.getVideoId().clear();
            getData.getThumbnailUrl().clear();
            getData.getVideoTitle().clear();
            getData.getViewsInfo().clear();
            getData.getShowTime().clear();
            getData.getDateInfo().clear();
            getData.getChannelName().clear();
            getData.getChannelImgUrl().clear();
            getData.getHeartIcon().clear();
            getData.getDownloadIcon().clear();
            getData.getBadgeIcon().clear();
            getData.getIsLive().clear();
        }


        //Shimmer

        if (uiComponents.containerB != null) {
            uiComponents.containerB.startShimmer();

            if (watchVM.getLoadNewData()) {
                getData = new getRelatedVideoData(new RelatedResultCallBack() {
                    @Override
                    public void result(List<VideoObject> videoData) {

                        System.out.println("Data arrived");

                        //start test ---------------------------------------------

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                watchVM.setVideoData(videoData);
                                watchVM.setRelatedVideoData(getData);
                                System.out.println("-----------\nI passed Data: " + getData.getVideoTitle().size());
                                System.out.println("-----------\nWatchVM Data: " + watchVM.getRelatedVideoData().getVideoTitle().size());
                                relatedVideos(videoData);
                            }
                        });

                        //end of test --------------------------------------------
                        isThereData = true;

                    }
                });
                getData.execute("https://www.youtube.com/watch?v=" + videoId);
            } else {
                getData = watchVM.getRelatedVideoData();
                relatedVideos(watchVM.getVideoData());
            }


            isThereData = false;

            uiComponents.pauseImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (player.isPlaying()) {
                        player.pause();
                        uiComponents.pauseImg.setImageResource(R.drawable.ic_play_small_version);
                    } else {
                        player.play();
                        uiComponents.pauseImg.setImageResource(R.drawable.ic_pause_small_version);
                    }
                }
            });
            uiComponents.closeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Check if video playing make it pause before hide the layout
                    if (player.isPlaying()) {
                        player.pause();
                    }
                    //motionLayout.setAlpha(0);
                    homeActivity.manager.resizeWatchFragment(0);
                    uiComponents.motionLayout.setProgress((float) 0.0);
                    isNeedToMaximize = false;
                    homeActivity.frameLayoutVisibility();

                    System.out.println("------ Close Button Clicked");
                }
            });

            favoriteData();
        }


        return view;
    }

    private void relatedVideos(List<VideoObject> videoObjectList) {
        uiComponents.containerB.stopShimmer();
        uiComponents.containerB.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.relatedVideoRecycle);
        NestedScrollView nestedScrollView = view.findViewById(R.id.nested);
        //
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        System.out.println("----------- Size of watch videoObjectList: " + videoObjectList.size());

        relatedVideoAdapter = new RelatedVideoAdapter(videoObjectList, getData.getIsLive());

        recyclerView.setAdapter(relatedVideoAdapter);

        //Scrolling for new Related Videos Suggestions is not working I'll stop using it Until see where's the problem.
        //if you want to activate it, change [stopWorking] to false
        boolean stopWorking = true;
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() & isRelatedVideoLoaded != true && !stopWorking) {

                    System.out.println("77777777777777777777777777777777777");
                    System.out.println("I'm in the last index");
                    System.out.println("77777777777777777777777777777777777");
                    isRelatedVideoLoaded = true;
                    uiComponents.containerB.startShimmer();
                    uiComponents.containerB.setVisibility(View.VISIBLE);

                    int listSize = getData.getChannelImgUrl().size();
                    getRelatedVideoData.moreData();

                    Timer timer1 = new Timer();
                    timer1.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("note yet");
                            if (listSize < getData.getBadgeIcon().size()) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Stuff that updates the UI
                                        System.out.println("I get data");
                                        System.out.println("orignal list size: " + listSize);
                                        System.out.println("current list size: " + getData.getChannelImgUrl().size());
                                        int rr = getData.getChannelImgUrl().size() - listSize;
                                        System.out.println("how many data added: " + rr);


                                        //relatedVideoAdapter.notifyDataSetChanged();
                                        //notifyItemRangeInserted(insertIndex, items.size());
                                        uiComponents.containerB.stopShimmer();
                                        uiComponents.containerB.setVisibility(View.GONE);

                                        relatedVideoAdapter.notifyItemRangeInserted(getData.getVideoId().size(), getData.getVideoId().size() - listSize);
                                        isRelatedVideoLoaded = false;
                                    }
                                });
                                timer1.cancel();
                            }
                        }
                    }, 0, 1000);
                    //relatedVideoAdapter.notifyDataSetChanged();

                }
            }
        });

        relatedVideoAdapter.setOnItemClickListener(new RelatedVideoAdapter.onItemClickListener() {
            @Override
            public void onHeartClick(int position) {
                //
                addToFavorite(videoObjectList.get(position));
            }

            @Override
            public void onItemClick(int position) {
                //System.out.println(getData.getVideoTitle().get(position));
                //videoObjectList
                //getData.getVideoTitle().size() != 0
                if (videoObjectList.size() != 0) {
                    //System.out.println("You clicked on me: "+ getData.getVideoId().get(position));
                    List<VideoObject> data = new ArrayList<>(videoObjectList);
                    player.release();


                    watchVM.setLoadNewData(true);
                    watchVM.getVideoData().clear();


                    if (isVideoReady) {
                        //playerView.onPause();
                        player.stop(true);
                        player.clearMediaItems();
                    }

                    //refresh activity with new data by starting new intent
//                                        Intent intent = new Intent(getActivity(), watch.class);
//                                        intent.putExtra("from", "Watch");
//                                        intent.putExtra("video_id", getData.getVideoId().get(position));
//                                        intent.putExtra("video_thumbnail", getData.getThumbnailUrl().get(position));
//                                        intent.putExtra("video_title", getData.getVideoTitle().get(position));
//                                        intent.putExtra("video_views", getData.getViewsInfo().get(position));
//                                        intent.putExtra("video_date", getData.getDateInfo().get(position));
//                                        intent.putExtra("video_time", getData.getShowTime().get(position));
//                                        intent.putExtra("channel_name", getData.getChannelName().get(position));
//                                        intent.putExtra("channel_pic",  getData.getChannelImgUrl().get(position));

                    //clear data
//                                        getData.getVideoId().clear();
//                                        getData.getThumbnailUrl().clear();
//                                        getData.getVideoTitle().clear();
//                                        getData.getViewsInfo().clear();
//                                        getData.getShowTime().clear();
//                                        getData.getDateInfo().clear();
//                                        getData.getChannelName().clear();
//                                        getData.getChannelImgUrl().clear();
//                                        getData.getHeartIcon().clear();
//                                        getData.getDownloadIcon().clear();
//                                        getData.getBadgeIcon().clear();
//                                        getData.getIsLive().clear();
//                                        container.startShimmer();

//                                        startActivity(intent);
//                                        getActivity().finish();



                    openWatchFragment(position, data);

                } else {
                    System.out.println("-------------------------\nThere's No Data In [getData]\nTitle Size: " + getData.getVideoTitle().size());
                    System.out.println("-------------------------\nWatchVM Title Size: " + watchVM.getRelatedVideoData().getVideoTitle().size());
                }

            }

            @Override
            public void onDownloadClick(int position) {
                if (HomeActivity.isPermission) {
                    getVideoStreamLink downloadUrls = new getVideoStreamLink(activity, new FetchDownloadingLinksCallback() {
                        @Override
                        public void callback(List<VideoStreamObject> videoStreamObjectList) {
                            //
                            System.out.println("------- Down Called");
                            generateDownloadingOption(videoObjectList.get(position), videoStreamObjectList);
                        }
                    });
                    downloadUrls.execute("https://www.youtube.com/watch?v=" + videoObjectList.get(position).getVideoId());
                    createDownloadDialog(new VideoObject(getData.getVideoId().get(position), getData.getVideoTitle().get(position), "", getData.getShowTime().get(position), "", "", false, getData.getThumbnailUrl().get(position), ""));
                } else {
                    Toast.makeText(getContext(), R.string.StorageMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initializeUIComponents(View view) {
        downloadWindow = new Dialog(getActivity());

        uiComponents.containerB = view.findViewById(R.id.shimmer);

        uiComponents.textTitle = view.findViewById(R.id.mainVideoTitle);
        uiComponents.textViews = view.findViewById(R.id.viewsText);
        uiComponents.textDate = view.findViewById(R.id.dateText);
        uiComponents.titleSmallVersion = view.findViewById(R.id.title_small_version);

        uiComponents.textChannelName = view.findViewById(R.id.channelName);
        uiComponents.channelPic = view.findViewById(R.id.chennelPic);
        uiComponents.heartPic = view.findViewById(R.id.addTofavo);
        uiComponents.downloadPic = view.findViewById(R.id.download);
        uiComponents.sharePic = view.findViewById(R.id.share);

        uiComponents.pauseImg = view.findViewById(R.id.pause_small);
        uiComponents.closeImg = view.findViewById(R.id.close_small);
        uiComponents.motionLayout = view.findViewById(R.id.watchVideoMotionLayout);

        uiComponents.playerView = view.findViewById(R.id.video_view);
        uiComponents.progressBar = view.findViewById(R.id.pB);
        uiComponents.fullScreeen = view.findViewById(R.id.btn_fullscreen);

        uiComponents.downloadWindowThumbnail = downloadWindow.findViewById(R.id.thumbnailRoundedImageView);
        uiComponents.downloadWindowTitle = downloadWindow.findViewById(R.id.titleOfDownloadFile);
        uiComponents.downloadWindowTime = downloadWindow.findViewById(R.id.downloadTime);

        uiComponents.waiting_download_links_spinner_layout = downloadWindow.findViewById(R.id.waiting_download_links_spinner_layout);
        uiComponents.download_links_layout = downloadWindow.findViewById(R.id.download_links_layout);

        uiComponents.p240Card = downloadWindow.findViewById(R.id.p240Card);
        uiComponents.p360Card = downloadWindow.findViewById(R.id.p360Card);
        uiComponents.p480Card = downloadWindow.findViewById(R.id.p480Card);
        uiComponents.p720Card = downloadWindow.findViewById(R.id.p720Card);
        uiComponents.p1080Card = downloadWindow.findViewById(R.id.p1080Card);
        uiComponents.p1440Card = downloadWindow.findViewById(R.id.p1440Card);
        uiComponents.k4Card = downloadWindow.findViewById(R.id.k4Card);

        uiComponents.p240 = downloadWindow.findViewById(R.id.p240);
        uiComponents.p360 = downloadWindow.findViewById(R.id.p360);
        uiComponents.p480 = downloadWindow.findViewById(R.id.p480);
        uiComponents.p720 = downloadWindow.findViewById(R.id.p720);
        uiComponents.p1080 = downloadWindow.findViewById(R.id.p1080);
        uiComponents.p1440 = downloadWindow.findViewById(R.id.p1440);
        uiComponents.k4 = downloadWindow.findViewById(R.id.k4);

        uiComponents.p240MbSize = downloadWindow.findViewById(R.id.p240MbSize);
        uiComponents.p360MbSize = downloadWindow.findViewById(R.id.p360MbSize);
        uiComponents.p480MbSize = downloadWindow.findViewById(R.id.p480MbSize);
        uiComponents.p720MbSize = downloadWindow.findViewById(R.id.p720MSize);
        uiComponents.p1080MbSize = downloadWindow.findViewById(R.id.p1080MbSize);
        uiComponents.p1440MbSize = downloadWindow.findViewById(R.id.p1440MbSize);
        uiComponents.k4MbSize = downloadWindow.findViewById(R.id.k4MbSize);

        uiComponents.tinyCard = downloadWindow.findViewById(R.id.tinyCard);
        uiComponents.smallCard = downloadWindow.findViewById(R.id.smallCard);
        uiComponents.mediumCard = downloadWindow.findViewById(R.id.mediumCard);
        uiComponents.largeCard = downloadWindow.findViewById(R.id.largCard);

        uiComponents.tiny = downloadWindow.findViewById(R.id.tiny);
        uiComponents.small = downloadWindow.findViewById(R.id.small);
        uiComponents.medium = downloadWindow.findViewById(R.id.medium);
        uiComponents.large = downloadWindow.findViewById(R.id.large);

        uiComponents.tinyMbSize = downloadWindow.findViewById(R.id.tinyMbSize);
        uiComponents.smallMbSize = downloadWindow.findViewById(R.id.smallMbSize);
        uiComponents.mediumMbSize = downloadWindow.findViewById(R.id.mediumMbSize);
        uiComponents.largeMbSize = downloadWindow.findViewById(R.id.largeMbSize);

        uiComponents.tinyBitrateSize = downloadWindow.findViewById(R.id.tinyBiterateSize);
        uiComponents.smallBitrateSize = downloadWindow.findViewById(R.id.smallBiterateSize);
        uiComponents.mediumBitrateSize = downloadWindow.findViewById(R.id.mediumBiterateSize);
        uiComponents.largeBitrateSize = downloadWindow.findViewById(R.id.largeBitrateSize);
    }

    private void favoriteData() {
        favoriteViewModel.getAllFavorites().observe(getActivity(), new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                FavoriteIndex.passIds(favorites);
                if (relatedVideoAdapter != null) {
                    relatedVideoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addToFavorite(VideoObject videoObject) {
        if (FavoriteIndex.Id(videoObject.getVideoId())) {
            deleteFromFavorite(videoObject);
        } else {
            favoriteViewModel.insert(new Favorite(videoObject));
        }

        Toast toast = Toast.makeText(activity, "Item Added to Favor List", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void deleteFromFavorite(VideoObject videoObject) {
        FavoriteIndex.removeId(videoObject.getVideoId());
        favoriteViewModel.deleteById(videoObject.getVideoId());

        Toast toast = Toast.makeText(activity, "Item deleted from Favorite List", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * this method change the play and pause image depend on the statue of the video
     */
    private void checkVideoStatue() {
        if (player.isPlaying()) {
            uiComponents.pauseImg.setImageResource(R.drawable.ic_pause_small_version);
        } else {
            uiComponents.pauseImg.setImageResource(R.drawable.ic_play_small_version);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop when ready
        player.setPlayWhenReady(false);
        //get playback state
        player.getPlaybackState();
    }

    @Override
    public void onResume() {
        super.onResume();

        //play video when ready
        player.setPlayWhenReady(true);
        //get playback state
        player.getPlaybackState();
    }

    /**
     * This Method For Listening on MotionLayout changes
     * the purpose of this method is to send signal to (resizeWatchFragment) method that exist in (HomeActivity) activity
     * to minimize and maximize this fragment layout
     */
    public void motionLayoutTransitionListener(View vieww) {
        MotionLayout motionLayout = view.findViewById(R.id.watchVideoMotionLayout);
        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                if (motionLayout.getProgress() == 1.0) {
                    homeActivity.manager.resizeWatchFragment(1);
                    uiComponents.playerView.hideController();
                    uiComponents.playerView.setUseController(false);
                    checkVideoStatue();
                    isNeedToMaximize = true;

                    homeActivity.setWatchFragmentOpen(false);
                } else {
                    uiComponents.playerView.setUseController(true);
                    if (isNeedToMaximize) {
                        homeActivity.manager.resizeWatchFragment(0);
                    }
                    homeActivity.setWatchFragmentOpen(true);
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            if (isNeedToMaximize) {
                ((HomeActivity) getActivity()).manager.resizeWatchFragment(0);
                isNeedToMaximize = false;
            }
            return super.onDown(e);
        }
    }

    private void openWatchFragment(int position, List<VideoObject> videoObjectList) {
        //Open Watch Fragment

        WatchFragment watchFragment = new WatchFragment();
        //Send Data to watch Fragment before added it
        /**Check from where data come to pass it in the right way.
         * there are two source of data the first one is Trending videos and the second one is the Recommended videos**/

//        sendData(watchFragment, getData.getVideoId().get(position), getData.getThumbnailUrl().get(position)
//                , getData.getVideoTitle().get(position), getData.getViewsInfo().get(position)
//                , getData.getDateInfo().get(position), getData.getShowTime().get(position)
//                , getData.getChannelName().get(position), getData.getChannelImgUrl().get(position));

        sendData(watchFragment, videoObjectList.get(position).getVideoId(), videoObjectList.get(position).getThumbnail()
                , videoObjectList.get(position).getTitle(), videoObjectList.get(position).getViews()
                , videoObjectList.get(position).getDate(), videoObjectList.get(position).getTime()
                , videoObjectList.get(position).getBy(), videoObjectList.get(position).getChannelThumbnail());


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.watchFragment, watchFragment);
        fragmentTransaction.commit();

    }

    private void sendData(Fragment fragment, String id, String thumbnail, String title, String views, String date, String time, String channelName, String channelPicture) {
        Bundle bundle = new Bundle();
        bundle.putString("from", "");
        bundle.putString("video_id", id);
        bundle.putString("video_thumbnail", thumbnail);
        bundle.putString("video_title", title);
        bundle.putString("video_views", views);
        bundle.putString("video_date", date);
        bundle.putString("video_time", time);
        bundle.putString("channel_name", channelName);
        bundle.putString("channel_pic", channelPicture);
        fragment.setArguments(bundle);
    }

    public void minimize() {
        uiComponents.motionLayout.transitionToEnd();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                //Landscape
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            } else {
                //Portrait
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //downloading part
    private void createDownloadDialog(VideoObject videoList) {
        downloadWindow.setContentView(R.layout.my_dialog);


        Picasso.get()
                .load(videoList.getThumbnail())
                .centerCrop()
                .fit()
                .into(uiComponents.downloadWindowThumbnail);
        uiComponents.downloadWindowTitle.setText(videoList.getTitle());
        uiComponents.downloadWindowTime.setText(videoList.getTime());

        downloadWindow.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        downloadWindow.show();
    }

    public void generateDownloadingOption(VideoObject videoObject, List<VideoStreamObject> videoStreamObjectList) {
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
                optionButtonsListener(videoObject);
            }
        });
    }

    public void falterQuality(String quality, double fileSize, String audioType, double bitRate, String downloadLink) {

        System.out.println("Faltering .... " + "Quality: " + quality);
        //hd2160
        if (!is4KFound) {
            k4DownloadLink = downloadLink;
            if (quality.equals("hd2160")) {
                uiComponents.k4.setVisibility(View.VISIBLE);
                is4KFound = true;
                uiComponents.k4MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                uiComponents.k4.setVisibility(View.GONE);
            }
        }
        //hd1440
        if (!is1440Found) {
            if (quality.equals("hd1440")) {
                p1440DownloadLink = downloadLink;
                uiComponents.p1440.setVisibility(View.VISIBLE);
                is1440Found = true;
                uiComponents.p1440MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                uiComponents.p1440.setVisibility(View.GONE);
            }
        }
        //hd1080
        if (!is1080Found) {
            if (quality.equals("hd1080")) {
                p1080DownloadLink = downloadLink;
                uiComponents.p1080.setVisibility(View.VISIBLE);
                is1080Found = true;
                uiComponents.p1080MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                uiComponents.p1080.setVisibility(View.GONE);
            }
        }
        //hd720
        if (!is720Found) {
            if (quality.equals("hd720")) {
                p720DownloadLink = downloadLink;
                uiComponents.p720.setVisibility(View.VISIBLE);
                is720Found = true;
                uiComponents.p720MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                uiComponents.p720.setVisibility(View.GONE);
            }
        }
        //480p
        if (!is480Found) {
            if (quality.equals("large")) {
                p480DownloadLink = downloadLink;
                uiComponents.p480.setVisibility(View.VISIBLE);
                is480Found = true;
                uiComponents.p480MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                uiComponents.p480.setVisibility(View.GONE);
            }
        }
        //360p
        if (!is360Found) {
            if (quality.equals("medium")) {
                p360DownloadLink = downloadLink;
                uiComponents.p360.setVisibility(View.VISIBLE);
                is360Found = true;
                uiComponents.p360MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                uiComponents.p360.setVisibility(View.GONE);
            }
        }
        //240p
        if (!is240Found) {
            if (quality.equals("small")) {
                p240DownloadLink = downloadLink;
                uiComponents.p240.setVisibility(View.VISIBLE);
                is240Found = true;
                uiComponents.p240MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                uiComponents.p240.setVisibility(View.GONE);
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
        if (!isLargeFound & audioType.contains("audio")) {
            largeDownloadLink = downloadLink;
            isLargeFound = true;
            uiComponents.largeMbSize.setText(String.format("%.2f", fileSize) + "MB");
            uiComponents.largeBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
        } else {
            //Medium
            if (!isMediumFound & audioType.contains("audio")) {
                mediumDownloadLink = downloadLink;
                isMediumFound = true;
                uiComponents.mediumMbSize.setText(String.format("%.2f", fileSize) + "MB");
                uiComponents.mediumBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
            } else {
                //Small
                if (!isSmallFound & audioType.contains("audio")) {
                    smallDownloadLink = downloadLink;
                    isSmallFound = true;
                    uiComponents.smallMbSize.setText(String.format("%.2f", fileSize) + "MB");
                    uiComponents.smallBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                } else {
                    //Tiny
                    if (!isTinyFound & audioType.contains("audio")) {
                        tinyDownloadLink = downloadLink;
                        isTinyFound = true;
                        uiComponents.tinyMbSize.setText(String.format("%.2f", fileSize) + "MB");
                        uiComponents.tinyBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                    }
                }
            }
        }


    }

    private void optionButtonsListener(VideoObject videoObject) {
        uiComponents.waiting_download_links_spinner_layout.setVisibility(View.GONE);
        uiComponents.download_links_layout.setVisibility(View.VISIBLE);


        //Video part
        uiComponents.k4Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (k4DownloadLink != null) {
                    if (k4DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", k4DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(videoObject, "video", k4DownloadLink, aacDownloadLink);
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
        uiComponents.p1080Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p1080DownloadLink != null) {
                    if (p1080DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p1080DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(videoObject, "video", p1080DownloadLink, aacDownloadLink);
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
        uiComponents.p1440Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p1440DownloadLink != null) {
                    if (p1440DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p1440DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(videoObject, "video", p1440DownloadLink, aacDownloadLink);
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
        uiComponents.p720Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p720DownloadLink != null) {
                    if (p720DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p720DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(videoObject, "video", p720DownloadLink, aacDownloadLink);
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
        uiComponents.p480Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p480DownloadLink != null) {
                    if (p480DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p480DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(videoObject, "video", p480DownloadLink, aacDownloadLink);
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
        uiComponents.p360Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p360DownloadLink != null) {
                    if (p360DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p360DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(videoObject, "video", p360DownloadLink, aacDownloadLink);
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
        uiComponents.p240Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p240DownloadLink != null) {
                    if (p240DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p240DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(videoObject, "video", p240DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p240DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });

        //Audio Part
        uiComponents.largeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (largeDownloadLink != null) {
                    if (largeDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", largeDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                largeDownloadLink = stream;
                                startDownload(videoObject, "audio", largeDownloadLink, aacDownloadLink);

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
        uiComponents.mediumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediumDownloadLink != null) {
                    if (mediumDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", mediumDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                mediumDownloadLink = stream;
                                startDownload(videoObject, "audio", mediumDownloadLink, aacDownloadLink);

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
        uiComponents.smallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smallDownloadLink != null) {
                    if (smallDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", smallDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                smallDownloadLink = stream;
                                startDownload(videoObject, "audio", smallDownloadLink, aacDownloadLink);

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
        uiComponents.tinyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDownloadLink != null) {
                    if (tinyDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", tinyDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                tinyDownloadLink = stream;
                                startDownload(videoObject, "audio", tinyDownloadLink, aacDownloadLink);

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

    private void startDownload(VideoObject videoObject, String type, String videoLink, String audioLink) {
        Intent i = new Intent(activity, testService.class);
        i.putExtra("video_id", videoObject.getVideoId());

        if (type.equals("video")) {
            i.putExtra("download_type", "video");
        } else {
            i.putExtra("download_type", "audio");
        }

        i.putExtra("download_video_link", videoLink);
        i.putExtra("download_audio_link", audioLink);
        i.putExtra("file_name", videoObject.getTitle());
        i.putExtra("thumbnail", videoObject.getThumbnail());
        i.putExtra("by", videoObject.getBy());
        i.putExtra("time_line", videoObject.getTime());
        activity.startService(i);
    }

    private void decryptAudio() {
        decryptCipher.decrypt(aacDownloadLink, "S");
    }
}