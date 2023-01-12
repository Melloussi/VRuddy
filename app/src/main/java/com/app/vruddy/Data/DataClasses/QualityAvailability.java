package com.app.vruddy.Data.DataClasses;

public class QualityAvailability {
    private boolean is4KFound = false;
    private boolean is1440Found = false;
    private boolean is1080Found = false;
    private boolean is720Found = false;
    private boolean is480Found = false;
    private boolean is360Found = false;
    private boolean is240Found = false;
    private boolean isTinyFound = false;
    private boolean isSmallFound = false;
    private boolean isMediumFound = false;
    private boolean isLargeFound = false;

    public QualityAvailability(boolean is4KFound, boolean is1440Found, boolean is1080Found, boolean is720Found, boolean is480Found, boolean is360Found, boolean is240Found, boolean isTinyFound, boolean isSmallFound, boolean isMediumFound, boolean isLargeFound) {
        this.is4KFound = is4KFound;
        this.is1440Found = is1440Found;
        this.is1080Found = is1080Found;
        this.is720Found = is720Found;
        this.is480Found = is480Found;
        this.is360Found = is360Found;
        this.is240Found = is240Found;
        this.isTinyFound = isTinyFound;
        this.isSmallFound = isSmallFound;
        this.isMediumFound = isMediumFound;
        this.isLargeFound = isLargeFound;
    }

    public QualityAvailability() {}

    public boolean isIs4KFound() {
        return is4KFound;
    }

    public void setIs4KFound(boolean is4KFound) {
        this.is4KFound = is4KFound;
    }

    public boolean isIs1440Found() {
        return is1440Found;
    }

    public void setIs1440Found(boolean is1440Found) {
        this.is1440Found = is1440Found;
    }

    public boolean isIs1080Found() {
        return is1080Found;
    }

    public void setIs1080Found(boolean is1080Found) {
        this.is1080Found = is1080Found;
    }

    public boolean isIs720Found() {
        return is720Found;
    }

    public void setIs720Found(boolean is720Found) {
        this.is720Found = is720Found;
    }

    public boolean isIs480Found() {
        return is480Found;
    }

    public void setIs480Found(boolean is480Found) {
        this.is480Found = is480Found;
    }

    public boolean isIs360Found() {
        return is360Found;
    }

    public void setIs360Found(boolean is360Found) {
        this.is360Found = is360Found;
    }

    public boolean isIs240Found() {
        return is240Found;
    }

    public void setIs240Found(boolean is240Found) {
        this.is240Found = is240Found;
    }

    public boolean isTinyFound() {
        return isTinyFound;
    }

    public void setTinyFound(boolean tinyFound) {
        isTinyFound = tinyFound;
    }

    public boolean isSmallFound() {
        return isSmallFound;
    }

    public void setSmallFound(boolean smallFound) {
        isSmallFound = smallFound;
    }

    public boolean isMediumFound() {
        return isMediumFound;
    }

    public void setMediumFound(boolean mediumFound) {
        isMediumFound = mediumFound;
    }

    public boolean isLargeFound() {
        return isLargeFound;
    }

    public void setLargeFound(boolean largeFound) {
        isLargeFound = largeFound;
    }
}
