package com.pax.tk.annapp.FlappyBird;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject {

    private Rect rectangle;
    private Rect rectangle2;
    private int color;

    public Obstacle(int rectWidth, int color, int startY, int startX, int playerGap){
        this.color = color;
        rectangle = new Rect(startX, 0, startX + rectWidth, startY);
        rectangle2 = new Rect(startX, startY + playerGap, startX + rectWidth, Constants.SCREEN_HEIGHT);
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public void incrementX(float x){
        rectangle.right -= x;
        rectangle.left -= x;
        rectangle2.right -= x;
        rectangle2.left -= x;
    }

    public boolean playerCollide(RectPlayer player){
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {

    }
}
