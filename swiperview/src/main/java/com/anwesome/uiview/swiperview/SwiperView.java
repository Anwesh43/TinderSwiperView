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
    private float leftOfView,topOfView,initLeft=0,initTop = 0;
    private long prevTime = 0;
    public SwiperView(Context context) {
        super(context);
    }

    public float getInitTop() {
        return initTop;
    }

    public void setInitTop(float initTop) {
        this.initTop = initTop;
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
                rot = -5;
            }
            else if(leftOfView<initLeft) {
                rot = 5;
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
        canvas.drawColor(Color.WHITE);
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
        }
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float resolution = (swiperObject.getBitmap().getHeight()*1.0f)/(swiperObject.getBitmap().getHeight()*1.0f);
        if(swiperObject!=null && swiperObject.getBitmap()!=null) {
            canvas.drawBitmap(swiperObject.getBitmap(), new Rect(0, 0, swiperObject.getBitmap().getWidth(), swiperObject.getBitmap().getHeight()), new RectF(canvas.getWidth()/10, canvas.getHeight()/30, canvas.getWidth()/10+3*canvas.getWidth()/4, canvas.getHeight()/30+(3*canvas.getWidth()/4) * resolution), paint);
        }
        if(swiperObject!=null && swiperObject.getRightBitmap()!=null) {
            int rb_width = swiperObject.getRightBitmap().getWidth(),rb_height = swiperObject.getRightBitmap().getHeight();
            float rb_res = (rb_height*1.0f)/(rb_width*1.0f);
            float rb_src_width = (w*1.0f)/10,rb_src_height = (w/10)*rb_res;
            float rb_x = 3*w*0.25f,rb_y = 0.8f*h;
            canvas.drawBitmap(swiperObject.getRightBitmap(),new Rect(0,0,rb_width,rb_height),new RectF(rb_x,rb_y,rb_x+rb_src_width,rb_y+rb_src_width),paint);
        }
        if(swiperObject!=null && swiperObject.getLeftText()!=null) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(w/40);
            float tx = w/10,ty = 0.8f*h;
            canvas.drawText(swiperObject.getLeftText(),tx,ty,paint);
        }
        paint.setColor(Color.parseColor("#E0E0E0"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawRect(new RectF(0,0,canvas.getWidth(),canvas.getHeight()),paint);
        time++;
        if(shouldUpdate) {
            leftOfView+=100*outDir;
            topOfView-=30*outDir;
            swiperLayout.requestLayout();
            try {
                Thread.sleep(10);
                invalidate();
            }
            catch (Exception ex) {

            }
        }
    }
    public boolean isAfterScreen(float width) {
        return (inMotion)  && (leftOfView>initLeft && leftOfView+7*w/10>width);
    }
    public boolean isBeforeScreen() {
        return (inMotion) && (leftOfView<initLeft && leftOfView+3*w/10<0);
    }
    public void handleDown(float x,float y) {
        if(containsTouch(x,y)) {
            if(swiperObject!=null && swiperObject.getListener()!=null) {
                swiperObject.getListener().onClick(this);
            }
        }

    }
    public boolean containsTouch(float x,float y) {
        return !inMotion && x>leftOfView && x<leftOfView+w && y>topOfView && y<topOfView+h && prevTime == 0;
    }
    public boolean outOfBounds(float width) {
        return (leftOfView+w<0) || (leftOfView>width);
    }
    public void startMovingOut(SwiperLayout swiperLayout,int outDir) {
        this.swiperLayout = swiperLayout;
        shouldUpdate = true;
        this.outDir = outDir;
        invalidate();
    }
    public void handleUp() {
        if(prevTime!=0 && System.currentTimeMillis()-prevTime<=2000) {

        }
        prevTime = 0;
    }
}
