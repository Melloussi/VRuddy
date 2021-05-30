package com.app.vruddy;

import com.app.vruddy.Objects.VideoObject;

import java.util.List;

public interface TestCallBack {
    void trend(List<VideoObject> videoObjectList);
    void recommended(List<VideoObject> videoObjectList);
}
