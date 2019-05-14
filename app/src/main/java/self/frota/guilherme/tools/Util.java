package self.frota.guilherme.tools;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import self.frota.guilherme.game.GameState;
import self.frota.guilherme.principal.Game;

/**
 * Created by Frota on 01/08/2017.
 */

public class Util {
    public static Typeface FONT_BOTAO;
    public static Typeface FONT_PADRAO;

    public static void init(Game game) {
        FONT_BOTAO = getFont("font-botao", game);
        FONT_PADRAO = getFont("font-padrao", game);
    }

    public static double randomDouble(double min, double max) {
        return min + (Math.random() * (max - min));
    }

    public static float randomFloat(float min, float max) {
        return min + (float)(Math.random() * (max - min));
    }

    public static int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min) + 0.5);
    }

    public static int limita(int num,int min, int max) {
        if (num > max) num = max;
        if (num < min) num = min;

        return num;
    }

    public static float limita(float num,float min, float max) {
        if (num > max) num = max;
        if (num < min) num = min;

        return num;
    }

    public static double limita(double num,double min, double max) {
        if (num > max) num = max;
        if (num < min) num = min;

        return num;
    }

    public static int getFontHeight(Paint tinta, String texto) {
        Rect result = new Rect();
        tinta.getTextBounds(texto, 0, texto.length(), result);

        return result.height();
    }

    private static Typeface getFont(String nome, Game game) {
        return Typeface.createFromAsset(game.getAssets(), "fonts/" + nome +  ".ttf");
    }

    public static float inDP(float pixel) {
        return (pixel * Game.SCALE + 0.5f);
    }

    public static float inDP(int pixel) {
        return (pixel * Game.SCALE + 0.5f);
    }

    public static double inDP(double pixel) {
        return (pixel * Game.SCALE + 0.5f);
    }


    public static float inPX(float dp) {
        return (dp / Game.SCALE + 0.5f);
    }

    public static float inPX(int dp) {
        return (dp / Game.SCALE + 0.5f);
    }

    public static double inPX(double dp) {
        return (dp / Game.SCALE + 0.5f);
    }

}
