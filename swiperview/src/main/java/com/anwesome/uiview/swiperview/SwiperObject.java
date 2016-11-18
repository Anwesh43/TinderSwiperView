package com.anwesome.uiview.swiperview;

import android.graphics.Bitmap;

/**
 * Created by anweshmishra on 18/11/16.
 */
public class SwiperObject {
    private Bitmap bitmap;
    private SwiperObjectClickListener listener;

    private SwipeAction likeAction,disLikeAction;
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
