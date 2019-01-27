package com.pax.tk.annapp.FlappyBird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {

    public ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleWidth;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleWidth, int color){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleWidth = obstacleWidth;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();
        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player){
        for (Obstacle ob:
             obstacles) {
            if(ob.playerCollide(player)){
                return true;
            }
        }
        return false;
    }

    //TODO: change variable i
    private void populateObstacles(){
        int currX = 5*Constants.SCREEN_WIDTH/4;
        int i = 0;
        while(currX > Constants.SCREEN_WIDTH && i < 15){
            int yStart = (int)(Math.random()*(Constants.SCREEN_HEIGHT - playerGap));
            obstacles.add(new Obstacle(obstacleWidth, color, yStart, currX, playerGap));
            currX += obstacleWidth + obstacleGap;
            i++;
        }
    }

    public void update(){
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/10000.0))*Constants.SCREEN_WIDTH/5000.0f;
        for (Obstacle ob:
             obstacles) {
            ob.incrementX(speed * elapsedTime);
        }
        if (obstacles.get(obstacles.size() - 1).getRectangle().right < 0){
            int yStart = (int)(Math.random()*(Constants.SCREEN_HEIGHT - playerGap));
            obstacles.add(new Obstacle(obstacleWidth, color, yStart, obstacles.get(0).getRectangle().right + obstacleWidth + obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score++;
        }
    }

    public void draw(Canvas canvas){
        for (Obstacle ob:
             obstacles) {
            ob.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.BLUE);
        canvas.drawText(String.valueOf(score), 50, 50 + paint.descent() - paint.ascent(), paint);
    }
}
