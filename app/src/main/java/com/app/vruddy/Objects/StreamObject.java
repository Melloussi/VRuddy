package com.app.vruddy.Objects;

import java.util.List;

public class StreamObject {
    private List<VideoStreamObject> streamObjectList;

    public StreamObject(List<VideoStreamObject> streamObjectList) {
        streamObjectList.clear();
        this.streamObjectList = streamObjectList;
    }

    public List<VideoStreamObject> getStreamObjectList() {
        return streamObjectList;
    }
}
