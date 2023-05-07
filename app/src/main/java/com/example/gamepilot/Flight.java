package com.example.gamepilot;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Flight {

    // поля
    private int x = 0, y = 0;
    private int width, height;
    private int wingCounter = 0;
    private Bitmap flight1, flight2;
    private boolean isGoingUp = false;

    // конструктор
    public Flight(int screenX, int screenY, Resources resources) {
        // считывания изображений
        flight1 = BitmapFactory.decodeResource(resources, R.drawable.plane_fly1);
        flight2 = BitmapFactory.decodeResource(resources, R.drawable.plane_fly2);

        // инициализация размеров самолета с масштабированием
        width = (int) (flight1.getWidth() / 1.5);
        height = (int) (flight1.getHeight() / 1.5);

        // приведение размера самолёта совместимым с другими экранами
        width = (int)(width * 1920f / screenX);
        height = (int)(height * 1080f / screenY);

        // изменение размера изображения самолёта
        flight1 = Bitmap.createScaledBitmap(flight1, width, height, true);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, true);

        // начальное местоположение полёта
        x = screenX / 21;
        y = screenY / 2;
    }

    // метод задания очерёдности переключения изображений полёта
    public Bitmap getFlight() {
        if (wingCounter == 0) {
            wingCounter ++;
            return flight1;
        } else if(wingCounter > 0) {
            wingCounter --;
            return flight2;
        }

        return null;
    }

    // геттеры и сеттеры

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isGoingUp() {
        return isGoingUp;
    }

    public void setGoingUp(boolean goingUp) {
        isGoingUp = goingUp;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
