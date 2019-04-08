package com.example.lenovo.parallax;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class ParallaxActivity extends AppCompatActivity {

    private ParallaxView parallaxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point resolution = new Point();
        display.getSize(resolution);

        parallaxView = new ParallaxView(this, resolution.x, resolution.y);
        setContentView(ParallaxView);
    }

    @Override
    protected void onPause(){
        super.onPause();
        parallaxView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parallaxView.resume();
    }
}
