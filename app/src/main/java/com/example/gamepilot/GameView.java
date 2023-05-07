package com.example.gamepilot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    // поля
    private Thread thread; // поле нового потока
    private boolean isPlaying;
    private Background background1, background2;
    private int screenX, screenY;
    private Paint paint;
    private float screenRatioX, screenRatioY;
    private Flight flight;


    // конструктор на основе SurfaceView
    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        // сщздание объектов фонов
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        // присваивание поля x класса Background переменной ширины screenX
        background2.setX(screenX);

        paint = new Paint();

        // создание объекта самолёта
        flight = new Flight(screenX, screenY, getResources());
    }

    // реализация метода run() дополнительного потока
    @Override
    public void run() {
        // операции  в потоке
        while (isPlaying) {
            // методы запускаемые в потоке
            update();
            draw();
            sleep();
        }
    }
    // метод обновления потока
    private void update() {
        // сдвиг экрана по оси Х на 10 пикселей и преобразование для совместимости разных экранов
        background1.setX(background1.getX() - (int)(10 * screenRatioX));
        background2.setX(background2.getX() - (int)(10 * screenRatioX));

        if ((background1.getX() + background1.getBackground().getWidth()) <= 0) {
            background1.setX(screenX);
        }
        if ((background2.getX() + background2.getBackground().getWidth()) <= 0) {
            background2.setX(screenX);
        }

        // задание скорости подъёма и снижения самолёта
        if (flight.isGoingUp()) {
            flight.setY(flight.getY() - (int)(30 * screenRatioY));
        } else {
            flight.setY(flight.getY() + (int)(30 * screenRatioY));
        }
        // задание порога значений местоположения самолёта
        if (flight.getY() < 0) {
            flight.setY(0);
        } else if (flight.getY() >= screenY - flight.getHeight()) {
            flight.setY(screenY - flight.getHeight());
        }
    }

    // метод рисования в потоке
    private void draw() {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.getBackground(), background1.getX(), background1.getY(), paint);
            canvas.drawBitmap(background2.getBackground(), background2.getX(), background2.getY(), paint);

            // отрисовка изображения самолёта
            canvas.drawBitmap(flight.getFlight(), flight.getX(), flight.getY(), paint);

            // вывод нарисованных изображений на экран
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    // метод засыпания потока
    private void sleep() {
        try {
            // засыпания потока на 16 милисекунд
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // метод запуска потока
    public void resumeThread() {
        // установление флага запуска игры
        isPlaying = true;
        // создание объекта потока
        thread = new Thread(this);
        // запуск потока
        thread.start();
    }

    // метод паузы потока
    public void pauseThread(){
        try {
            // установление флага приостановления игры
            isPlaying = false;
            // приостановление потока
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // метод обработки касания экрана
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // обработка событий касания экрана
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // если пользователь нажал на левую сторону экрана
                if (event.getX() < (screenX / 2) && event.getY() < (screenY / 2)) {
                    // то движение вверх
                    flight.setGoingUp(true);
                }  else if (event.getX() < (screenX / 2) && event.getY() >= (screenY / 2)) {
                    flight.setGoingUp(false);
                } else if  (event.getX() >= (screenX / 2)) {

                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() < (screenX / 2) && event.getY() < (screenY / 2)) {
                    flight.setGoingUp(false);
                    flight.setY(screenY / 2);
                }  else if (event.getX() < (screenX / 2) && event.getY() >= (screenY / 2)) {
                    flight.setGoingUp(true);
                    flight.setY(screenY / 2);
                }
                break;
        }
        return true;
    }
}
