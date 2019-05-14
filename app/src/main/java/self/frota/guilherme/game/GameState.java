package self.frota.guilherme.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import self.frota.guilherme.input.ListenerManager;
import self.frota.guilherme.particulas.Particula;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.principal.GameSurface;
import self.frota.guilherme.principal.GameThread;
import self.frota.guilherme.tools.Timer;
import self.frota.guilherme.tools.Util;
import self.frota.guilherme.tools.Variator;

/**
 * Created by Frota on 06/08/2017.
 */

public abstract class GameState {
    private int tickPausado;
    private ListenerManager listenerManager;
    protected Game game;
    private static boolean isPausado = false;

    public void init(Game game) {
        this.game = game;
        Util.init(game);
        listenerManager = new ListenerManager();
        initState();
        tickPausado = 0;
    }

    protected abstract void initState();

    public final void update() {
        if (!isPausado) {
            updateParticulas();
            updateState();
        } else {
            updatePausado();
        }

        updateTimers();
        updateVariator();
    }

    protected void updatePausado() {

    }

    protected void pausar() {
        isPausado = true;
    }

    protected void despausar() {
        isPausado = false;
    }

    protected abstract void updateState();

    private void updateVariator() {
        for (int i = Variator.todosVariator.size()-1; i >= 0; i--) {
            Variator.todosVariator.get(i).update();
        }
    }

    private void updateTimers() {
        for (int i = Timer.todosTimers.size()-1; i >= 0 ; i--) {
            Timer.todosTimers.get(i).update();
        }
    }

    private void updateParticulas() {
        for (int i = Particula.todasParticulas.size()-1; i >= 0 ; i--) {
            Particula.todasParticulas.get(i).update();
        }
    }

    public void draw(Canvas canvas, Paint tintaPai) {
        drawState(canvas, tintaPai);
        drawParticulas(canvas, tintaPai);
    }

    protected abstract void drawState(Canvas canvas, Paint tintaPai);

    private void drawParticulas(Canvas canvas, Paint tintaPai) {
        for (int i = Particula.todasParticulas.size()-1; i >= 0 ; i--) {
            Particula.todasParticulas.get(i).draw(canvas, tintaPai);
        }
    }

    public void close() {
        listenerManager.close();
        Particula.todasParticulas.clear();
        Timer.todosTimers.clear();
        closeState();
    }

    protected abstract void closeState();


    public final ListenerManager getListenerManager() {
        return listenerManager;
    }

    public static final boolean isPausado() {
        return isPausado;
    }

    public Game getGame() {
        return game;
    }


}
