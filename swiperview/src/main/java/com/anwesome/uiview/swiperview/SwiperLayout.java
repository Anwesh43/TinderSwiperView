package com.anwesome.uiview.swiperview;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anweshmishra on 18/11/16.
 */
public class SwiperLayout extends ViewGroup {
    private boolean scroll = true;
    private class ViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        private SwiperView swiperView;

        public void setSwiperView(SwiperView swiperView) {
            this.swiperView = swiperView;
        }
        public ViewGestureListener(SwiperView swiperView) {
            this.swiperView  = swiperView;
        }
        public boolean onDown(MotionEvent event) {
            if(swiperView!=null &&  swiperView.containsTouch(event.getX(),event.getY()) && !isDown) {
                isDown = true;
            }
            return  true;
        }
        public boolean onSingleTapUp(MotionEvent event) {
            if(swiperView!=null) {
                swiperView.handleDown(event.getX(), event.getY());
                requestLayout();
            }
            return true;
        }
        public boolean onFling(MotionEvent e1,MotionEvent e2,float velx,float vely) {
            if(swiperView!=null) {

                if (e1.getX() > e2.getX()) {
                        swipeLeft(false);
                } else {
                        swipeRight(false);
                    }
                scroll = false;
                requestLayout();
            }
            return true;
        }
        public boolean onScroll(MotionEvent e1,MotionEvent e2,float velx,float vely) {
            if(swiperView!=null && isDown) {
                Log.d("velx:", "" + velx);
                Log.d("vely:", "" + vely);
                if (Math.abs(velx) < 10) {
                    scroll = true;
                    swiperView.setLeftOfView(e2.getX() - swiperView.getMeasuredWidth() / 2);
                    swiperView.setInMotion(true);
                    swiperView.setTopOfView(e2.getY() - swiperView.getMeasuredHeight() / 2);
                    if (swiperView.getLeftOfView() < swiperView.getInitLeft()) {
                        swiperView.setRot(-30);
                    } else {
                        swiperView.setRot(30);
                    }
                }
                requestLayout();
            }
            return true;
        }
    }
    private List<SwiperObject> swiperObjects = new ArrayList<>();
    private List<SwiperView> swiperViews = new ArrayList<>();
    private SwiperView currentView;
    private int width,height;
    private boolean loaded = false;
    private boolean isDown = false;
    private GestureDetector gestureDetector;
    private ViewGestureListener viewGestureListener;
    public SwiperLayout(Context context,List<SwiperObject> swiperObjects) {
        super(context);
        this.swiperObjects = swiperObjects;

    }

    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        if(!loaded) {
            Point size = new Point();
            getDisplay().getRealSize(size);
            width = size.x;
            height = size.y;

            for (SwiperObject swiperObject : swiperObjects) {
                SwiperView swiperView = new SwiperView(getContext());
                swiperView.setSwiperObject(swiperObject);
                swiperView.setTopOfView(height / 10);
                swiperView.setInitLeft(width / 10);
                swiperView.setLeftOfView(width / 10);
                swiperView.setInitTop(height / 10);
                swiperView.setLayoutParams(new LayoutParams(3 * width / 4, 3 * height / 4));
                addView(swiperView);
                measureChild(swiperView, widthMeasureSpec, heightMeasureSpec);
                currentView = swiperView;
                swiperViews.add(swiperView);

            }
            viewGestureListener = new ViewGestureListener(currentView);
            gestureDetector = new GestureDetector(getContext(),viewGestureListener);
            loaded = true;
        }
        setMeasuredDimension(width,height);
    }
    public void onLayout(boolean changed,int a,int b,int w,int h) {
        int viewWidth = 3*width/4,viewHeight = height/2;
        int i =0,n = getChildCount();
        for(SwiperView swiperView:swiperViews) {
            swiperView.layout((int)swiperView.getLeftOfView(),(int)swiperView.getTopOfView(),(int)swiperView.getLeftOfView()+viewWidth,(int)swiperView.getTopOfView()+viewHeight);
            swiperView.setRotation(swiperView.getRot());
            swiperView.setScaleX(1+0.05f*(n-i));
            swiperView.setScaleY(1+0.05f*(n-i));
            i++;
        }

        if(currentView!=null && currentView.outOfBounds(width)) {
            removeView(currentView);
            swiperViews.remove(currentView);
            if(swiperViews.size()>0) {
                currentView = swiperViews.get(swiperViews.size()-1);
            }
            else {
                currentView = null;
            }
            viewGestureListener.setSwiperView(currentView);
            requestLayout();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
//        if(currentView!=null) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    if(!isDown) {
//                        isDown = true;
//                        currentView.handleDown(event.getX(),event.getY());
//                    }
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    if(isDown) {
//                        currentView.setInMotion(true);
//                        currentView.setLeftOfView(event.getX()-currentView.getMeasuredWidth()/2);
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    if(isDown) {
//                        isDown = true;
//                        if(!(currentView.isAfterScreen(width) || currentView.isBeforeScreen())) {
//                            currentView.setRot(0);
//                            currentView.setLeftOfView(currentView.getInitLeft());
//                            currentView.setTopOfView(currentView.getInitTop());
//                        }
//                        else {
//                            currentView.handleUp();
//                        }
//                    }
//                    break;
//            }
//            requestLayout();
//        }
        if(currentView!=null && scroll && event.getAction() == MotionEvent.ACTION_UP) {
            if(currentView.isAfterScreen(width)) {
                currentView.startMovingOut(this,1);
                currentView.doSwipeLikeAction();
            }
            else if(currentView.isBeforeScreen()) {
                currentView.startMovingOut(this,-1);
                currentView.doSwipeDislikeAction();
            }
            else {
                currentView.setLeftOfView(currentView.getInitLeft());
                currentView.setTopOfView(currentView.getInitTop());
                currentView.setInMotion(false);
                currentView.setRot(0);
            }
            scroll = false;
            isDown = false;
            requestLayout();
            return true;
        }
        return gestureDetector.onTouchEvent(event);
    }
    public void swipeRight() {

    }

    public void swipeLeft(boolean fromExternalSource) {
        if(currentView!=null) {
            currentView.setRot(-AppConstants.ROTATION_CLEAR);
            currentView.setInMotion(true);
            currentView.startMovingOut(this, -1);
            currentView.doSwipeDislikeAction();
            if(fromExternalSource) {
                requestLayout();
            }
        }
    }
    public void swipeRight(boolean fromExternalSource) {
        if(currentView!=null) {
            currentView.setRot(AppConstants.ROTATION_CLEAR);
            currentView.setInMotion(true);
            currentView.startMovingOut(this, 1);
            currentView.doSwipeLikeAction();
            if(fromExternalSource) {
                requestLayout();
            }
        }
    }

}
