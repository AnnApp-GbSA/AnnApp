package com.pax.tk.annapp.FlappyBird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene {

    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;
    private boolean gameOver = false;
    private long gameOverTime;
    private ObstacleManager obstacleManager;
    private int velocity = 0;
    private int gravity = 50;
    boolean gameState = false;

    public GameplayScene(){
        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(450, 500, 250, Color.BLACK);
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(450, 500, 250, Color.BLACK);

    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            velocity = -200;
            gameState = true;
            //playerPoint.set((int)event.getX(), (int)event.getY());
            if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000){
                reset();
                gameOver = false;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.GREEN);
        obstacleManager.draw(canvas);
        if(gameState) {
            if (player.getRectangle().bottom < Constants.SCREEN_HEIGHT || velocity < 0) {
                velocity += gravity;
                player.getRectangle().set(player.getRectangle().left, player.getRectangle().top + velocity, player.getRectangle().right, player.getRectangle().bottom + velocity);
            }
        }
        player.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.BLUE);
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    @Override
    public void update() {
        if(!gameOver) {
            //playerPoint.set((player.getRectangle().right - player.getRectangle().left)/2, (player.getRectangle().bottom - player.getRectangle().top)/2);
            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2F + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
