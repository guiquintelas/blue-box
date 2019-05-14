package self.frota.guilherme.principal;

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

        /*Display display = game.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y - (int)Util.inDP(50);

        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        //layout.topMargin = (int)Util.inDP(50);
        this.setLayoutParams(layout);*/



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
