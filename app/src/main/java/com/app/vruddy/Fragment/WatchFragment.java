package com.app.vruddy.Fragment;

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
import androidx.constraintlayout.widget.ConstraintLayout;
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

import com.app.vruddy.AsyncTask.StreamUrl;
import com.app.vruddy.DecryptCipher.DecryptCipher;
import com.app.vruddy.DecryptCipher.ResultCallBack;
import com.app.vruddy.DecryptCipher.StreamResultCallBack;
import com.app.vruddy.Interfaces.FetchDownloadingLinksCallback;
import com.app.vruddy.Interfaces.RelatedResultCallBack;
import com.app.vruddy.Objects.VideoObject;
import com.app.vruddy.Objects.VideoStreamObject;
import com.app.vruddy.R;
import com.app.vruddy.Adapter.RelatedVideoAdapter;
import com.app.vruddy.HomeActivity;
import com.app.vruddy.AsyncTask.getRelatedVideoData;
import com.app.vruddy.AsyncTask.getVideoStreamLink;
import com.app.vruddy.database.Cipher.CipherViewModel;
import com.app.vruddy.database.Favorite.Favorite;
import com.app.vruddy.database.Favorite.FavoriteIndex;
import com.app.vruddy.database.Favorite.FavoriteViewModel;
import com.app.vruddy.testService;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchFragment extends Fragment {

    //Variables
    private TextView textTitle;
    private TextView textViews;
    private TextView textDate;
    private ImageView channelPic;
    private ImageView heartPic;
    private ImageView downloadPic;
    private ImageView sharePic;
    private YouTubePlayerView youTubePlayerView;
    private boolean isThereData = false;
    private boolean screenState = false;
    private ImageView closeImg;
    private ImageView pauseImg;
    private TextView titleSmallVersion;

    //just for test -----------------
    private ArrayList<String> thumbnailUrl = new ArrayList<>();
    private ArrayList<String> videoTitleList = new ArrayList<>();
    private ArrayList<String> viewsInfo = new ArrayList<>();
    private ArrayList<String> showTime = new ArrayList<>();
    private ArrayList<String> dateInfo = new ArrayList<>();
    private ArrayList<String> channelNameList = new ArrayList<>();
    private ArrayList<String> channelImgUrl = new ArrayList<>();
    private ArrayList<Integer> heartIcon = new ArrayList<>();
    private ArrayList<Integer> DownloadIcon = new ArrayList<>();
    private ArrayList<Integer> badgeIcon = new ArrayList<>();

    private PlayerView playerView;
    private static ProgressBar progressBar;
    private ImageView fullScreeen;
    private SimpleExoPlayer player;
    private static boolean isVideoReady = false;
    private static boolean isRelatedVideoLoaded = false;

    private static GestureDetectorCompat mGestureDetectorCompat;
    private static boolean isNeedToMaximize = false;

    private static Context context;
    private static Dialog downloadWindow;
    private static TextView downloadWindowTitle;
    private static TextView downloadWindowTime;
    private static ImageView downloadWindowThumbnail;

    private static getRelatedVideoData getData;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    private static MotionLayout motionLayout;
    private static HomeActivity homeActivity;
    private static CipherViewModel cipherViewModel;

    private static RelativeLayout waiting_download_links_spinner_layout;
    private static LinearLayout download_links_layout;
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

    private static RelativeLayout tiny;
    private static RelativeLayout small;
    private static RelativeLayout medium;
    private static RelativeLayout large;
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

    private static Activity activity;
    private static DecryptCipher decryptCipher;

    private static FavoriteIndex favoriteIndex;
    private static RelatedVideoAdapter relatedVideoAdapter;
    private static FavoriteViewModel favoriteViewModel;

    public WatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchFragment newInstance(String param1, String param2) {
        WatchFragment fragment = new WatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        homeActivity = (HomeActivity) getActivity();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containerA, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_watch, containerA, false);


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


        context = getActivity();
        downloadWindow = new Dialog(context);

        textTitle = view.findViewById(R.id.mainVideoTitle);
        textViews = view.findViewById(R.id.viewsText);
        textDate = view.findViewById(R.id.dateText);
        TextView textChannelName = view.findViewById(R.id.channelName);
        channelPic = view.findViewById(R.id.chennelPic);
        heartPic = view.findViewById(R.id.addTofavo);
        downloadPic = view.findViewById(R.id.download);
        sharePic = view.findViewById(R.id.share);
        titleSmallVersion = view.findViewById(R.id.title_small_version);
        pauseImg = view.findViewById(R.id.pause_small);
        closeImg = view.findViewById(R.id.close_small);
        motionLayout = view.findViewById(R.id.watchVideoMotionLayout);


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

        //pass data to the main video
        titleSmallVersion.setText(VideoTitle);
        textTitle.setText(VideoTitle);
        textViews.setText(VideoViews);
        textDate.setText(VideoDate);
        textChannelName.setText(ChannelName);
        Picasso.get()
                .load(ChannelName)
                .transform(new CropCircleTransformation())
                .into(channelPic);


        //----------------------------------------------------------------------------------------
        //ExoPlayer part -------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------
        playerView = view.findViewById(R.id.video_view);
        progressBar = view.findViewById(R.id.pB);
        fullScreeen = view.findViewById(R.id.btn_fullscreen);
        player = new SimpleExoPlayer.Builder(activity).build();

        //get watch Stream link
        try {
            downloadPic.setOnClickListener(new View.OnClickListener() {
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

                            playerView.setPlayer(player);

                            //make video fit frame size
                            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                            //keep screen on
                            playerView.setKeepScreenOn(true);

                            // Build the media item.
                            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
                            // Set the media item to be played.
                            player.setMediaItem(mediaItem);
                            // Prepare the player.
                            player.prepare();

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
                                        progressBar.setVisibility(View.VISIBLE);
                                    } else if (state == Player.STATE_READY) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        isVideoReady = true;
                                    }
                                    progressBar.setAlpha(0);
                                }
                            });


                            //full screen
                            fullScreeen.setOnClickListener(new View.OnClickListener() {
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

                                    if (screenState) {
                                        fullScreeen.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen));
                                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                                        }
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();

                                        params.width = params.MATCH_PARENT;
                                        params.height = (int) (200 * getActivity().getResources().getDisplayMetrics().density);
                                        playerView.setLayoutParams(params);
                                        screenState = false;
                                    } else {
                                        fullScreeen.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_exit));
                                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                                        }
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                                        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                                        params.width = params.MATCH_PARENT;
                                        params.height = params.MATCH_PARENT;
                                        playerView.setLayoutParams(params);
                                        screenState = true;
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
        ShimmerFrameLayout containerB =
                (ShimmerFrameLayout) view.findViewById(R.id.shimmer);
        containerB.startShimmer();

        getData = new getRelatedVideoData(new RelatedResultCallBack() {
            @Override
            public void result(List<VideoObject> videoObjectList) {

                System.out.println("Data arrived");

                //start test ---------------------------------------------

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        containerB.stopShimmer();
                        containerB.setVisibility(View.GONE);


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

                        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() & isRelatedVideoLoaded != true) {

                                    System.out.println("77777777777777777777777777777777777");
                                    System.out.println("I'm in the last index");
                                    System.out.println("77777777777777777777777777777777777");
                                    isRelatedVideoLoaded = true;
                                    containerB.startShimmer();
                                    containerB.setVisibility(View.VISIBLE);

                                    int listSize = getData.getChannelImgUrl().size();
                                    getData.moreData();

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
                                                        containerB.stopShimmer();
                                                        containerB.setVisibility(View.GONE);

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
                                if (getData.getVideoTitle().size() != 0) {
                                    //System.out.println("You clicked on me: "+ getData.getVideoId().get(position));


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


                                    openWatchFragment(position);

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
                });

                //end of test --------------------------------------------
                isThereData = true;

            }
        });
        getData.execute("https://www.youtube.com/watch?v=" + videoId);


        isThereData = false;

        pauseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying()) {
                    player.pause();
                    pauseImg.setImageResource(R.drawable.ic_play_small_version);
                } else {
                    player.play();
                    pauseImg.setImageResource(R.drawable.ic_pause_small_version);
                }
            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if video playing make it pause before hide the layout
                if (player.isPlaying()) {
                    player.pause();
                }
                //motionLayout.setAlpha(0);
                ((HomeActivity) getActivity()).resizeWatchFragment(0);
                motionLayout.setProgress((float) 0.0);
                isNeedToMaximize = false;
                ((HomeActivity) getActivity()).frameLayoutSwitch();

                System.out.println("------ Close Button Clicked");
            }
        });

        favoriteData();

        return view;
    }

    private void favoriteData() {
        favoriteViewModel.getAllFavorites().observe(getActivity(), new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                favoriteIndex.passIds(favorites);
                if (relatedVideoAdapter != null) {
                    relatedVideoAdapter.notifyDataSetChanged();
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
        favoriteIndex.removeId(videoObject.getVideoId());
        favoriteViewModel.deleteById(videoObject.getVideoId());

        Toast toast = Toast.makeText(activity, "Item deleted from Favorite List", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * this method change the play and pause image depend on the statue of the video
     */
    private void checkVideoStatue() {
        if (player.isPlaying()) {
            pauseImg.setImageResource(R.drawable.ic_pause_small_version);
        } else {
            pauseImg.setImageResource(R.drawable.ic_play_small_version);
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
     *
     * @param view
     */
    public void motionLayoutTransitionListener(View view) {
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
                    ((HomeActivity) getActivity()).resizeWatchFragment(1);
                    playerView.hideController();
                    playerView.setUseController(false);
                    checkVideoStatue();
                    isNeedToMaximize = true;

                    homeActivity.setWatchFragmentOpen(false);
                } else {
                    playerView.setUseController(true);
                    if (isNeedToMaximize) {
                        ((HomeActivity) getActivity()).resizeWatchFragment(0);
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
                ((HomeActivity) getActivity()).resizeWatchFragment(0);
                isNeedToMaximize = false;
            }
            return super.onDown(e);
        }
    }

    private void openWatchFragment(int position) {
        //Open Watch Fragment

        WatchFragment watchFragment = new WatchFragment();
        //Send Data to watch Fragment before added it
        /**Check from where data come to pass it in the right way.
         * there are two source of data the first one is Trending videos and the second one is the Recommended videos**/

        sendData(watchFragment, getData.getVideoId().get(position), getData.getThumbnailUrl().get(position)
                , getData.getVideoTitle().get(position), getData.getViewsInfo().get(position)
                , getData.getDateInfo().get(position), getData.getShowTime().get(position)
                , getData.getChannelName().get(position), getData.getChannelImgUrl().get(position));


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
        motionLayout.transitionToEnd();
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

        tiny = downloadWindow.findViewById(R.id.tiny);
        small = downloadWindow.findViewById(R.id.small);
        medium = downloadWindow.findViewById(R.id.medium);
        large = downloadWindow.findViewById(R.id.large);

        tinyMbSize = downloadWindow.findViewById(R.id.tinyMbSize);
        smallMbSize = downloadWindow.findViewById(R.id.smallMbSize);
        mediumMbSize = downloadWindow.findViewById(R.id.mediumMbSize);
        largeMbSize = downloadWindow.findViewById(R.id.largeMbSize);

        tinyBitrateSize = downloadWindow.findViewById(R.id.tinyBiterateSize);
        smallBitrateSize = downloadWindow.findViewById(R.id.smallBiterateSize);
        mediumBitrateSize = downloadWindow.findViewById(R.id.mediumBiterateSize);
        largeBitrateSize = downloadWindow.findViewById(R.id.largeBitrateSize);


        Picasso.get()
                .load(videoList.getThumbnail())
                .centerCrop()
                .fit()
                .into(downloadWindowThumbnail);
        downloadWindowTitle.setText(videoList.getTitle());
        downloadWindowTime.setText(videoList.getTime());

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

    private void optionButtonsListener(VideoObject videoObject) {
        waiting_download_links_spinner_layout.setVisibility(View.GONE);
        download_links_layout.setVisibility(View.VISIBLE);


        //Video part
        k4Card.setOnClickListener(new View.OnClickListener() {
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
        p1080Card.setOnClickListener(new View.OnClickListener() {
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
        p1440Card.setOnClickListener(new View.OnClickListener() {
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
        p720Card.setOnClickListener(new View.OnClickListener() {
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
        p480Card.setOnClickListener(new View.OnClickListener() {
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
        p360Card.setOnClickListener(new View.OnClickListener() {
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
        p240Card.setOnClickListener(new View.OnClickListener() {
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
        largeCard.setOnClickListener(new View.OnClickListener() {
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
        mediumCard.setOnClickListener(new View.OnClickListener() {
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
        smallCard.setOnClickListener(new View.OnClickListener() {
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
        tinyCard.setOnClickListener(new View.OnClickListener() {
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