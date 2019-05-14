package self.frota.guilherme.principal;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import self.frota.guilherme.game.GameState;

public class GameThread extends Thread {

    public static int TOTAL_TICK = 0;
    public static int TOTAL_TICK_PAUSADO = 0;
    public static int FPS_NOW = 0;
    public final static long FPS = 60;
    private static final long DELAY = 1000/FPS;
    private boolean running;
    private Game game;
    private SurfaceHolder surfaceHolder;

    public GameThread(Game game)  {
        this.game = game;
        this.surfaceHolder = Game.getTela().getHolder();
        running = true;
        start();
    }

    @Override
    public void run()  {
        long startTime = System.nanoTime();
        long startTimeCounter = System.currentTimeMillis();
        long elapsedFPS = 0;

        while(running)  {
            Canvas canvas= null;
            try {
                // Get Canvas from Holder and lock it.
                canvas = this.surfaceHolder.lockCanvas();

                // Synchronized
                synchronized (canvas)  {
                    Game.getTela().update();
                    Game.getTela().draw(canvas);
                }
            }catch(Exception e)  {
                e.printStackTrace();
            } finally {
                if(canvas!= null)  {
                    // Unlock Canvas.
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.nanoTime() ;


            // Interval to redraw game
            // (Change nanoseconds to milliseconds)
            long waitTime = (now - startTime)/1000000;
            if(waitTime > DELAY)  {
                waitTime = DELAY; // Millisecond.

            }

            elapsedFPS++;
            if (!GameState.isPausado()) {
                TOTAL_TICK++;
            } else {
                TOTAL_TICK_PAUSADO++;
            }

            if (System.currentTimeMillis() - startTimeCounter >= 1000) {
                FPS_NOW = (int)elapsedFPS;
                elapsedFPS = 0;
                startTimeCounter = System.currentTimeMillis();

            }

            try {
                // Sleep.
                this.sleep(DELAY - waitTime);
            } catch(InterruptedException e)  {

            }
            startTime = System.nanoTime();

        }
    }

    public void pause() {
        running = false;
    }
}