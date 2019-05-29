package self.frota.guilherme.principal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Frota on 01/08/2017.
 *
 */

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private Game game;

    public GameSurface(Game game)  {
        super(game);
        this.game = game;

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);

        setDrawingCacheEnabled(true);
    }

    public Bitmap getBitmap(){
        return this.getDrawingCache();
    }


    public void update()  {
        game.updateGame();
    }


    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);
        game.drawGame(canvas);
    }


    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        game.init();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


}
