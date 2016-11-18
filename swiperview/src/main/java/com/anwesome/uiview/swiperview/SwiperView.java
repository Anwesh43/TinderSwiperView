package com.anwesome.uiview.swiperview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.*;
import android.view.View;

/**
 * Created by anweshmishra on 18/11/16.
 */
public class SwiperView extends View {
    private SwiperObject swiperObject;
    private int w,h,outDir = 1,time = 0;
    private float rot = 0;
    private boolean shouldUpdate=false;
    private SwiperLayout swiperLayout;
    public void setRot(float rot) {
        this.rot = rot;
    }
    public float getRot() {
        return rot;
    }
    private boolean inMotion = false;
    private float leftOfView,topOfView,initLeft=0;
    private long prevTime = 0;
    public SwiperView(Context context) {
        super(context);
    }
    public void setSwiperObject(SwiperObject swiperObject) {
        this.swiperObject = swiperObject;
    }
    public void setInMotion(boolean inMotion) {
        this.inMotion = inMotion;
    }
    public void setInitLeft(float left) {
        this.initLeft = left;
    }
    public void setLeftOfView(float left) {
        this.leftOfView = left;
        if(initLeft!=0) {
            if(leftOfView>initLeft) {
                rot = -30;
            }
            else if(leftOfView<initLeft) {
                rot = 30;
            }
            else {
                rot = 0;
            }
        }
    }
    public float getLeftOfView() {
        return leftOfView;
    }
    public float getTopOfView() {
        return topOfView;
    }
    public void setTopOfView(float top) {
        this.topOfView = top;
    }
    public float getInitLeft() {
        return initLeft;
    }
    public void doSwipeLikeAction() {
        if(swiperObject!=null && swiperObject.getLikeAction()!=null) {
            swiperObject.getLikeAction().doAction();
        }
    }
    public void doSwipeDislikeAction() {
        if(swiperObject!=null && swiperObject.getDisLikeAction()!=null) {
            swiperObject.getDisLikeAction().doAction();
        }
    }
    public void onDraw(Canvas canvas) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
        }
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float resolution = swiperObject.getBitmap().getHeight()/swiperObject.getBitmap().getHeight();
        if(swiperObject!=null && swiperObject.getBitmap()!=null) {
            canvas.drawBitmap(swiperObject.getBitmap(), new Rect(0, 0, swiperObject.getBitmap().getWidth(), swiperObject.getBitmap().getHeight()), new RectF(0, 0, canvas.getWidth(), canvas.getWidth() * resolution), paint);
        }
        time++;
        if(shouldUpdate) {
            leftOfView+=20*outDir;
            swiperLayout.requestLayout();
            try {
                Thread.sleep(30);
                invalidate();
            }
            catch (Exception ex) {

            }
        }
    }
    public boolean isAfterScreen(float width) {
        return (inMotion)  &&(leftOfView>initLeft && leftOfView+this.w/2>width);
    }
    public boolean isBeforeScreen() {
        return (inMotion) && (leftOfView<initLeft && leftOfView+w/2<0);
    }
    public void handleDown(float x,float y) {
        if(!inMotion && x>leftOfView && x<leftOfView+w && y>topOfView && y<topOfView+h && prevTime == 0) {
            prevTime = System.currentTimeMillis();
        }

    }
    public boolean outOfBounds(float width) {
        return (leftOfView+w<0) || (leftOfView+w>width);
    }
    public void startMovingOut(SwiperLayout swiperLayout,int outDir) {
        this.swiperLayout = swiperLayout;
        shouldUpdate = true;
        this.outDir = outDir;
        invalidate();
    }
    public void handleUp() {
        if(prevTime!=0 && System.currentTimeMillis()-prevTime<=2000) {
            if(swiperObject!=null && swiperObject.getListener()!=null) {
                swiperObject.getListener().onClick(this);
            }
        }
        prevTime = 0;
    }
}
