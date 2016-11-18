package com.anwesome.uiview.tinderswiperview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.anwesome.uiview.swiperview.SwipeAction;
import com.anwesome.uiview.swiperview.SwiperLayout;
import com.anwesome.uiview.swiperview.SwiperObject;
import com.anwesome.uiview.swiperview.SwiperObjectClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anweshmishra on 18/11/16.
 */
public class DemoActivity extends AppCompatActivity {
    private List<SwiperObject> swiperObjects = new ArrayList<>();
    int like = 0,dislike=0;
    private Bitmap rightIconBitmap;
    private final Map<String,Integer> personalitiesMap = new LinkedHashMap<String, Integer>(){{
        put("Barack",R.drawable.barack);
        put("Conte",R.drawable.conte);
        put("Klopp",R.drawable.klopp);
        put("Putin",R.drawable.putin);
        put("Wenger",R.drawable.wenger);
    }};
    //private final int res[] = {R.drawable.barack,R.drawable.conte,R.drawable.klopp,R.drawable.putin,R.drawable.wenger};
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rightIconBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.book);
        for(Map.Entry<String,Integer> entry:personalitiesMap.entrySet()) {
            SwiperObject swiperObject = SwiperObject.newInstance(BitmapFactory.decodeResource(getResources(),entry.getValue()));
            final String title = entry.getKey();
            swiperObject.setLeftText(title);
            swiperObject.setRightBitmap(rightIconBitmap);
            swiperObject.setListener(new SwiperObjectClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(DemoActivity.this,title,Toast.LENGTH_SHORT).show();
                }
            });
            swiperObject.setSwipeLikeAction(new SwipeAction() {
                @Override
                public void doAction() {
                    like++;
                    Toast.makeText(DemoActivity.this,"You have liked "+like+" things",Toast.LENGTH_SHORT).show();
                }
            });
            swiperObject.setSwipeDisLikeAction(new SwipeAction() {
                @Override
                public void doAction() {
                    dislike++;
                    Toast.makeText(DemoActivity.this,"You have disliked "+dislike+" things",Toast.LENGTH_SHORT).show();
                }
            });
            swiperObjects.add(swiperObject);
        }
        SwiperLayout swiperLayout = new SwiperLayout(this,swiperObjects);
        setContentView(swiperLayout);
    }
}
