package self.frota.guilherme.tools;

import java.util.ArrayList;

import self.frota.guilherme.game.GameState;
import self.frota.guilherme.principal.GameThread;

/**
 * Created by Frota on 11/08/2017.
 */

public abstract class Timer {
    private int tickCriado;
    private int delay;

    private boolean loop = false;
    private boolean ativoQuandoPausado = false;

    private boolean isInit = false;

    public static ArrayList<Timer> todosTimers = new ArrayList<Timer>();

    public Timer(int delay) {
        this.delay = delay;
        tickCriado = GameThread.TOTAL_TICK;
        isInit = true;
        todosTimers.add(this);

    }

    public Timer() {

    }

    public void init(int delay) {
        if (isInit) todosTimers.remove(this);

        this.delay = delay;
        tickCriado = GameThread.TOTAL_TICK;
        isInit = true;
        todosTimers.add(this);
    }

    public final void update() {
        if (GameState.isPausado() && !ativoQuandoPausado) return;

        acaoTick();
        if (GameThread.TOTAL_TICK >= tickCriado + delay) {
            acao();
            if (loop) {
                reset();
            } else {
                todosTimers.remove(this);
            }
        }
    }

    protected void acaoTick() {}

    public abstract void acao();

    public final void reset() {
        tickCriado = GameThread.TOTAL_TICK;

        if (!todosTimers.contains(this)) {
            todosTimers.add(this);
        }
    }

    public final void reset(int novoDelay) {
        delay = novoDelay;
        reset();
    }

    public final void delete() {
        loop = false;
        todosTimers.remove(this);
    }

    public final boolean isRodando() {
        return todosTimers.contains(this);
    }

    public final void setLoop(boolean loop) {
        this.loop = loop;
    }

    public final void setAtivoQuandoPausado(boolean ativo) {
        this.ativoQuandoPausado = ativo;
    }

}
