package com.app.vruddy.Models.Muxer;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import com.app.vruddy.Views.Activities.HomeActivity;
import com.app.vruddy.Data.database.InProgress.FileHandler;
import com.app.vruddy.Data.database.InProgress.Updaters.FileConverted;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Mux {
    private static boolean isComplete = false;

    public static boolean isComplete() {
        return isComplete;
    }

    public void doSomething(String video_id, String video, String audio, String output) throws IOException {
        con(video_id, audio, video, output);
    }

    private void con(String video_id, String AudioPath, String VideoPath, String OutputPath) throws IOException {
        System.out.println("Audio Path: "+AudioPath);
        System.out.println("Video Path: "+VideoPath);

        //Init extractors which will get encoded frames

        isComplete = false;

        //Video
        MediaExtractor VideoExtractor = new MediaExtractor();
        VideoExtractor.setDataSource(VideoPath);
        VideoExtractor.selectTrack(0);
        MediaFormat videoFormat = VideoExtractor.getTrackFormat(0);

        //Audio
        MediaExtractor AudioExtractor = new MediaExtractor();
        AudioExtractor.setDataSource(AudioPath);
        AudioExtractor.selectTrack(0);
        MediaFormat audioFormat = AudioExtractor.getTrackFormat(0);

        // Init muxer
        MediaMuxer muxer = new MediaMuxer(OutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        int audioTrackIndex = muxer.addTrack(audioFormat);
        int videoTrackIndex = muxer.addTrack(videoFormat);
        muxer.start();

        // Prepare buffer for copying
        int maxChunkSize = 1024 * 1024;
        ByteBuffer buffer = ByteBuffer.allocate(maxChunkSize);
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        // Copy Video
        while (true) {
            int chunkSize = VideoExtractor.readSampleData(buffer, 0);
            if(chunkSize > 0){
                bufferInfo.presentationTimeUs = VideoExtractor.getSampleTime();
                bufferInfo.flags = VideoExtractor.getSampleFlags();
                bufferInfo.size = chunkSize;

                muxer.writeSampleData(videoTrackIndex, buffer, bufferInfo);
                VideoExtractor.advance();
            }else {
                break;
            }
        }
        // Copy Audio
        while (true) {
            int chunkSize = AudioExtractor.readSampleData(buffer, 0);
            if(chunkSize > 0){
                bufferInfo.presentationTimeUs = AudioExtractor.getSampleTime();
                bufferInfo.flags = AudioExtractor.getSampleFlags();
                bufferInfo.size = chunkSize;

                muxer.writeSampleData(audioTrackIndex, buffer, bufferInfo);
                AudioExtractor.advance();
            }else {
                break;
            }
        }

        // Cleanup
        muxer.stop();
        muxer.release();

        VideoExtractor.release();
        AudioExtractor.release();

        System.out.println("Converting Complete");
        FileHandler fileHandler = new HomeActivity();
        fileHandler.updateConvertById(new FileConverted(video_id, true));
        fileHandler.deleteFileById(video_id);
        isComplete = true;



    }
}
