package self.frota.guilherme.input;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import self.frota.guilherme.principal.Game;

/**
 * Created by Frota on 06/08/2017.
 *
 */

public class ListenerManager {

    private ArrayList<GameListener> onTouchlisteners = new ArrayList<GameListener>();

    public ListenerManager() {
       restart();
    }

    public void addOnTouchListener(GameListener gl) {
        onTouchlisteners.add(gl);
    }

    public void removeOnTouchListener(GameListener gl) {
        onTouchlisteners.remove(gl);
    }

    public void close() {
        Game.getTela().setOnTouchListener(null);
        onTouchlisteners.clear();
    }

    public void restart() {
        Game.getTela().setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                for (int i = onTouchlisteners.size() - 1 ; i >= 0 ; i--) {
                    if (!onTouchlisteners.get(i).action(event)) {
                        return false;
                    }

                }

                return true;
            }
        });

    }

}
