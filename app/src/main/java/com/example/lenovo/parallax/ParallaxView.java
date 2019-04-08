package com.example.lenovo.parallax;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

public class ParallaxView extends SurfaceView implements Runnable {

    ArrayList<Background> backgrounds;

    private volatile boolean running;
    private Thread gameThread = null;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    Context context;

    long fps =60;

    int screenWidth;
    int screenHeight;

    ParallaxView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        ourHolder = getHolder();
        paint = new Paint();

        backgrounds = new ArrayList<>();

        backgrounds.add(new Background (
                this.context,
                screenWidth,
                screenHeight,
                "skyline", 0, 80, 50));

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "grass", 70, 110, 200));
    }

    @Override
    public void run(){
        while (running) {
            long startFrameTime = System.currentTimeMillis();

            update();

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        for (Background bg : backgrounds) {
            bg.update(fps);
        }
    }

    private void drawBackground(int position) {
        Background bg = backgrounds.get(position);

        Rect fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
        Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width, bg.endY);

        Rect fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
        Rect toRect2 = new Rect(0, bg.startY, bg.xClip, bg.endY);

        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReserved, fromRect2, toRect2, paint);
        }else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReserved, fromRect1, toRect1, paint);
        }
    }

    private void draw() {
        if(ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 0, 3, 70));

            drawBackground(0);

            paint.setTextSize(60);
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawText("Icha Terbang", 350, screenHeight/ 100 * 5, paint);
            paint.setTextSize(220);
            canvas.drawText("Icha Jalan", 50, screenHeight/100*80, paint);

            drawBackground(1);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}