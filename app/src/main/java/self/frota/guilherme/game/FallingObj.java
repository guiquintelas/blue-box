package self.frota.guilherme.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import self.frota.guilherme.game.objs.Player;
import self.frota.guilherme.game.states.JogoState;
import self.frota.guilherme.game.states.MenuState;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 25/09/2017.
 *
 */

public abstract class FallingObj extends GameObj {

    protected Player player;

    private double speed;
    private float size;
    private int cor;

    public static ArrayList<FallingObj> todosFallingObj = new ArrayList<>();

    public FallingObj(JogoState jogoState, float x, float y, float size, double speed, int cor) {
        super(jogoState, x, (y==0) ? -size : y, size, size);
        this.speed = speed;
        this.player = jogoState.getPlayer();
        this.cor = cor;
        this.size = size;

        todosFallingObj.add(this);
    }

    public FallingObj(MenuState menuState, float x, float y, float size, double speed, int cor) {
        super(menuState, x, y, size, size);
        this.speed = speed;
        this.player = null;
        this.cor = cor;
        this.size = size;

        todosFallingObj.add(this);
    }


    @Override
    public void update() {
        move();
        checaColisaoPlayer();
        checaFimTela();

        updateObj();
    }

    protected void checaColisaoPlayer() {
        if (y + height >= player.getY()) {
            Rect pedraR = new Rect((int)x, (int)y, (int)(x + size), (int)(y + size));
            Rect playerR = new Rect((int)player.getX(), (int)player.getY(), (int)(player.getX() + player.getWidth()), (int)(player.getY() + player.getHeight()));

            if (pedraR.intersect(playerR)) {
                colisaoAcao();
                todosFallingObj.remove(this);
            }
        }
    }

    protected void move() {
        y += Util.inDP(speed);
    }

    protected void checaFimTela() {
        if (y > Game.getTela().getHeight()) {
            fimTelaAcao();
            todosFallingObj.remove(this);
        }
    }

    protected abstract void fimTelaAcao();
    protected abstract void colisaoAcao();
    protected abstract void updateObj();

    @Override
    public void draw(Canvas canvas, Paint tintaPai) {
        Paint tinta = new Paint(tintaPai);
        tinta.setColor(cor);
        canvas.drawRect(x, y, x + size, y + size, tinta);
    }


    public static final void drawTodos(Canvas canvas, Paint tintaPai) {
        for (int i = FallingObj.todosFallingObj.size()-1; i >= 0; i--) {
            FallingObj.todosFallingObj.get(i).draw(canvas, tintaPai);
        }
    }

    public static final void updateTodos() {
        for (int i = FallingObj.todosFallingObj.size()-1; i >= 0; i--) {
            FallingObj.todosFallingObj.get(i).update();
        }
    }
}
