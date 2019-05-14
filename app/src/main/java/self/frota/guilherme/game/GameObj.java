package self.frota.guilherme.game;

import android.graphics.Canvas;
import android.graphics.Paint;

import self.frota.guilherme.game.states.JogoState;

/**
 * Created by Frota on 06/08/2017.
 *
 */

public abstract class GameObj {
    protected float x;
    protected float y;

    protected float width;
    protected float height;

    protected self.frota.guilherme.game.GameState gameState;
    protected JogoState jogoState;

    //public static

    public GameObj(self.frota.guilherme.game.GameState gameState) {
        this.gameState = gameState;
    }

    public GameObj(JogoState jogoState) {
        this.jogoState = jogoState;
    }

    public GameObj(JogoState jogoState, float x, float y, float width, float height) {
        this.jogoState = jogoState;
        this.gameState = jogoState;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GameObj(self.frota.guilherme.game.GameState gameState, float x, float y, float width, float height) {
        this.gameState = gameState;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getXCentro() {
        return x + width/2;
    }

    public float getYCentro() {
        return y + height/2;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    public abstract void update();
    public abstract void draw(Canvas canvas, Paint tintaPai);
}
