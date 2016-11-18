package com.anwesome.uiview.swiperview;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anweshmishra on 18/11/16.
 */
public class SwiperLayout extends ViewGroup {
    private List<SwiperObject> swiperObjects = new ArrayList<>();
    private List<SwiperView> swiperViews = new ArrayList<>();
    private SwiperView currentView;
    private int width,height;
    private boolean isDown = false;
    public SwiperLayout(Context context,List<SwiperObject> swiperObjects) {
        super(context);
        this.swiperObjects = swiperObjects;
    }

    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        Point size = new Point();
        getDisplay().getRealSize(size);
        width = size.x;
        height = size.y;
        int i = 0;
        int n = swiperObjects.size()-1;
        for(SwiperObject swiperObject:swiperObjects) {
            SwiperView swiperView = new SwiperView(getContext());
            swiperView.setSwiperObject(swiperObject);
            swiperView.setTopOfView(height/10);
            swiperView.setInitLeft(width/10);
            swiperView.setLeftOfView(width/10);

            swiperView.setScaleX(1+0.01f*(n-i));
            swiperView.setScaleY(1+0.01f*(n-i));
            swiperView.setLayoutParams(new LayoutParams(3*width/4,3*height/4));
            addView(swiperView);
            measureChild(swiperView,widthMeasureSpec,heightMeasureSpec);
            currentView = swiperView;
            i++;
        }
        setMeasuredDimension(width,3*height/4);
    }
    public void onLayout(boolean changed,int a,int b,int w,int h) {
        int viewWidth = 3*width/4,viewHeight = 3*height/4;
        for(SwiperView swiperView:swiperViews) {
            swiperView.layout((int)swiperView.getLeftOfView(),(int)swiperView.getTopOfView(),(int)swiperView.getLeftOfView()+viewWidth,(int)swiperView.getTopOfView()+viewHeight);
            swiperView.setRotation(swiperView.getRot());
        }
        if(currentView.isAfterScreen(width)) {
            currentView.doSwipeLikeAction();
            currentView.startMovingOut(this,1);
        }
        if(currentView.isBeforeScreen()) {
            currentView.doSwipeDislikeAction();
            currentView.startMovingOut(this,-1);
        }
        if(currentView.outOfBounds(width)) {
            removeView(currentView);
            swiperViews.remove(currentView);
            if(swiperViews.size()>0) {
                currentView = swiperViews.get(swiperViews.size()-1);
            }
            else {
                currentView = null;
            }
            requestLayout();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(currentView!=null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(!isDown) {
                        isDown = true;
                        currentView.handleDown(event.getX(),event.getY());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(isDown) {

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(isDown) {
                        isDown = true;
                        if(!(currentView.isAfterScreen(width) || currentView.isBeforeScreen())) {
                            currentView.setRot(0);
                            currentView.setLeftOfView(currentView.getInitLeft());
                        }
                        else {
                            currentView.handleUp();
                        }
                    }
                    break;
            }
            requestLayout();
        }
        return true;
    }
}
