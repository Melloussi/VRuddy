package com.app.vruddy.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vruddy.Adapter.InProgressAdapter;
import com.app.vruddy.R;
import com.app.vruddy.database.InProgress.FileViewModel;
import com.app.vruddy.database.InProgress.InProgressFile;
import com.app.vruddy.database.InProgress.Updaters.FileStatue;
import com.app.vruddy.testService;

import java.util.List;
import java.util.Timer;


public class inProgressFragment extends Fragment{
    private static FileViewModel fileViewModel;
    private RecyclerView recyclerView;
    private InProgressAdapter inProgressAdapter;
    private Timer timer;
    private com.app.vruddy.testService testService;
    private boolean isFirstTime = true;
    private static List<InProgressFile> filesList;
    private static String s;

    public static String getS() {
        return s;
    }

    public static void setS(String s) {
        inProgressFragment.s = s;
    }

    public inProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(FileViewModel.class);
        //timer.doInBackground();
        testService = new testService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.in_progress_fragment, container, false);

        recyclerView = view.findViewById(R.id.in_progress_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        inProgressAdapter = new InProgressAdapter();
        recyclerView.setAdapter(inProgressAdapter);

//        if(isFirstTime){
//            data();
//            isFirstTime = false;
//        }

        fileViewModel.getAllFiles().observe(this, new Observer<List<InProgressFile>>() {
            @Override
            public void onChanged(List<InProgressFile> inProgressFiles) {

                System.out.println("----------------------------------- Live Data -----------------------------------");

                //timer.setSeconds(0);
                filesList = inProgressFiles;
                //showData();
                //fileViewModel.deleteAll();
                inProgressAdapter.updateRecycler(inProgressFiles);

                if(testService.isIsServiceCreated() == false){
                    //Start Service
                    Intent i = new Intent(getActivity(), testService.class);
                    getActivity().startService(i);
//                    for(int index = 0; index > inProgressFiles.size(); index++) {
//                            if (inProgressFiles.get(index).getFile_type().equals("video")) {
//                                if(inProgressFiles.get(index).isFileCompleted() && !inProgressFiles.get(index).isAudioFail() && !inProgressFiles.get(index).isConverted()){
//                                    //
//                                    testService.resume(inProgressFiles.get(index).getAudioFileId(), null);
//                                }
//                            }
//                        }
                }
            }
        });
        inProgressAdapter.setOnItemClickListener(new InProgressAdapter.OnItemClickListener() {
            @Override
            public void onCancelClick(int position, String video_id, int downloadId) {
                testService.cancel(downloadId);
                fileViewModel.deleteFileById(video_id);
            }

            @Override
            public void onPauseClick(int position, String video_id, int downloadId) {
                if(!filesList.get(position).isFileFail() && !filesList.get(position).isAudioFail()) {
                    testService.pause(filesList.get(position));
                    FileStatue fileStatue = new FileStatue(video_id, true);
                    fileViewModel.updateFileStatueById(fileStatue);
                }

            }

            @Override
            public void onResumeClick(int position, String video_id, int downloadId) {
                if(!filesList.get(position).isFileFail() && !filesList.get(position).isAudioFail()) {
                    testService.resume(filesList.get(position));
                    FileStatue fileStatue = new FileStatue(video_id, false);
                    fileViewModel.updateFileStatueById(fileStatue);
                }
            }
        });
        //inProgressAdapter.updateRecycler(file);
        return view;
    }
    public void data(){
        fileViewModel.getAllFiles().observe(this, new Observer<List<InProgressFile>>() {
            @Override
            public void onChanged(List<InProgressFile> inProgressFiles) {

                String img = "https://image.freepik.com/free-vector/modern-youtube-thumbnail-with-comic-art-background_1361-2738.jpg";
                //fileViewModel.insertFile(inProgressFilee);
                //fileViewModel.deleteAll();
                //inProgressAdapter.setInProgressFiles(inProgressFiles);
//                List<InProgressFile>elements = new ArrayList<>();
//                elements.add(inProgressFilee);
//                elements.add(inProgressFilee2);
                //System.out.println("My File: " + inProgressFiles.get(0).getTitle());
                System.out.println("----------------------------------- Live Data -----------------------------------");

                inProgressAdapter.updateRecycler(inProgressFiles);
                //timer.setSeconds(0);


            }
        });
    }
}