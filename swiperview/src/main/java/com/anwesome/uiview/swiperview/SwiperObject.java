package com.anwesome.uiview.swiperview;

import android.graphics.Bitmap;

/**
 * Created by anweshmishra on 18/11/16.
 */
public class SwiperObject {
    private Bitmap bitmap;
    private SwiperObjectClickListener listener;
    private String leftText = null;
    private Bitmap rightBitmap = null;
    private SwipeAction likeAction,disLikeAction;
    private SwiperObject(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public static SwiperObject newInstance(Bitmap bitmap) {
        return new SwiperObject(bitmap);
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public SwiperObjectClickListener getListener() {
        return listener;
    }
    public void setListener(SwiperObjectClickListener listener) {
        this.listener = listener;
    }
    public void setSwipeLikeAction(SwipeAction likeAction) {
        this.likeAction = likeAction;
    }
    public void setSwipeDisLikeAction(SwipeAction disLikeAction) {
        this.disLikeAction = disLikeAction;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public Bitmap getRightBitmap() {
        return rightBitmap;
    }

    public void setRightBitmap(Bitmap rightBitmap) {
        this.rightBitmap = rightBitmap;
    }

    public SwipeAction getLikeAction() {
        return likeAction;
    }
    public SwipeAction getDisLikeAction() {
        return disLikeAction;
    }
    public int hashCode() {
        return bitmap.hashCode();
    }
}
