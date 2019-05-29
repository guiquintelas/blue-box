package self.frota.guilherme.game.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import self.frota.guilherme.GUI.Botao;
import self.frota.guilherme.R;
import self.frota.guilherme.game.FallingObj;
import self.frota.guilherme.game.GameState;
import self.frota.guilherme.game.objs.ObjMenu;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.principal.GameThread;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 14/08/2017.
 *
 */

public class MenuState extends GameState {

    private Botao botaoNovoJogo;
    private Botao botaoScore;
    private Botao botaoInfo;
    private Botao botaoVoltar;


    private static boolean isHighScore = false;
    private static boolean isInfo = false;

    private int tickInit;

    private Bitmap infoImg = null;
    @Override
    protected void initState() {
        tickInit = GameThread.TOTAL_TICK;

        isHighScore = false;
        isInfo = false;


        botaoNovoJogo = new Botao(this, 0, Game.getTela().getHeight()/2, Util.inDP(140), Util.inDP(38), R.string.novo_jogo_botao) {
            @Override
            public void acao() {
                game.setGameState(new JogoState());
            }
        };
        botaoNovoJogo.centerX();

        botaoScore = new Botao(this, 0, Game.getTela().getHeight()/2 + Util.inDP(66), Util.inDP(140), Util.inDP(38), R.string.high_score_botao) {
            @Override
            public void acao() {
                if (isHighScore) return;

                botaoInfo.setAtivo(false);
                botaoScore.setAtivo(false);
                botaoNovoJogo.setAtivo(false);

                botaoVoltar.setAtivo(true);

                MenuState.setHighScore();
            }
        };
        botaoScore.centerX();

        botaoInfo = new Botao(this, 0, Game.getTela().getHeight()/2 + Util.inDP(133), Util.inDP(140), Util.inDP(38), R.string.info_botao) {
            @Override
            public void acao() {
                if (isInfo) return;

                botaoInfo.setAtivo(false);
                botaoScore.setAtivo(false);
                botaoNovoJogo.setAtivo(false);

                botaoVoltar.setAtivo(true);

                MenuState.setInfo();
            }
        };
        botaoInfo.centerX();

        botaoVoltar = new Botao(this, 0, Game.getTela().getHeight()/2 + Util.inDP(133), Util.inDP(140), Util.inDP(38), R.string.voltar_botao) {
            @Override
            public void acao() {
                botaoInfo.setAtivo(true);
                botaoScore.setAtivo(true);
                botaoNovoJogo.setAtivo(true);

                if (isInfo) {
                    isInfo = false;
                }

                if (isHighScore) {
                    isHighScore = false;
                }

                botaoVoltar.setAtivo(false);
            }
        };
        botaoVoltar.centerX();
        botaoVoltar.setAtivo(false);
    }


    @Override
    protected void closeState() {
        FallingObj.todosFallingObj.clear();

        botaoScore.clear();
        botaoVoltar.clear();
        botaoNovoJogo.clear();
        botaoInfo.clear();
    }





    /*
     =========================================
                     UPDATE
     =========================================
     */



    @Override
    protected void updateState() {
        if (!isInfo && !isHighScore)  {
            criaPedra();
            FallingObj.updateTodos();
        }
    }


    private void criaPedra() {
        if (GameThread.TOTAL_TICK - tickInit > 25 && GameThread.TOTAL_TICK % 5 == 1 && FallingObj.todosFallingObj.size() < 30) {
            new ObjMenu(this, Util.randomFloat(0, Game.getTela().getWidth() - 50), Util.randomDouble(4, 8)/1.5);
        }

    }


    private void setFilter(Paint tintaPai) {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        tintaPai.setColorFilter(f);
    }



    /*
     =========================================
                     DRAW
     =========================================
     */




    @Override
    protected void drawState(Canvas canvas, Paint tintaPai) {
        setFilter(tintaPai);

        FallingObj.drawTodos(canvas, tintaPai);
        if (!isHighScore && !isInfo) drawTitulo(canvas);
        if (isInfo) drawInfo(canvas);
        if (isHighScore) drawScore(canvas, tintaPai);

        botaoVoltar.draw(canvas, tintaPai);
        botaoInfo.draw(canvas, tintaPai);
        botaoNovoJogo.draw(canvas, tintaPai);
        botaoScore.draw(canvas, tintaPai);

        //botaoTeste.draw(canvas, tintaPai);
    }

    private void drawTitulo(Canvas canvas) {
        Paint tintaPreta = new Paint();
        tintaPreta.setColor(Color.BLACK);
        tintaPreta.setAlpha(155);
        canvas.drawRect(0, Util.inDP(93), canvas.getWidth(), Util.inDP(206), tintaPreta);

        Paint tinta = new Paint();
        tinta.setAntiAlias(true);


        tinta.setTypeface(Util.FONT_PADRAO);
        tinta.setTextSize(Util.inDP(100));
        tinta.setTextAlign(Paint.Align.CENTER);

        tinta.setColor(Color.BLUE);
        tinta.setShadowLayer(0.1f, 1.5f, 1.5f, Color.WHITE);
        canvas.drawText("Blue", canvas.getWidth()/2, Util.inDP(167), tinta);


        tinta.setTypeface(Util.FONT_BOTAO);
        tinta.setTextSize(Util.inDP(25));
        tinta.setShadowLayer(0, 0, 0, 0);
        tinta.setColor(Color.WHITE);
        canvas.drawText("BOX", canvas.getWidth()/2, Util.inDP(194), tinta);

    private void createInfoImg() {
        Bitmap bitmap = Game.getTela().getBitmap();

        Canvas canvas = new Canvas(bitmap);

        Paint tintaPreta = new Paint();
        tintaPreta.setColor(Color.BLACK);
        tintaPreta.setAlpha(155);

        canvas.drawRect(0, Util.inDP(60), canvas.getWidth(), canvas.getHeight() - Util.inDP(50), tintaPreta);

        Paint tinta = new Paint();
        tinta.setAntiAlias(true);
        tinta.setTextAlign(Paint.Align.CENTER);
        tinta.setColor(Color.WHITE);
        tinta.setTypeface(Util.FONT_PADRAO);

        float fontSize = 27;

        tinta.setTextSize(Util.inDP(fontSize + 6));
        canvas.drawText("INFO", canvas.getWidth()/2f, Util.inDP(105), tinta);

        tinta.setTextSize(Util.inDP(fontSize - 8));
        int off = 150;
        int espaco = 23;

        canvas.drawText(game.getString(R.string.info_1), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco + 10;
        canvas.drawText(game.getString(R.string.info_2), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco;
        canvas.drawText(game.getString(R.string.info_3), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco;

        canvas.drawText(game.getString(R.string.info_4), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco;
        canvas.drawText(game.getString(R.string.info_5), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco + 10;

        canvas.drawText(game.getString(R.string.info_6), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco;
        canvas.drawText(game.getString(R.string.info_7), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco;
        canvas.drawText(game.getString(R.string.info_8), canvas.getWidth()/2f, Util.inDP(off), tinta);
        off += espaco + 20;


        canvas.drawText(game.getString(R.string.versao) + " " + Game.VERSAO, canvas.getWidth()/2f, Util.inDP(off), tinta);

        infoImg = bitmap;
    }

    private void drawInfo(Canvas canvas) {
        if (infoImg != null) {
            canvas.drawBitmap(infoImg, 0, 0, null);

        } else {
            createInfoImg();
        }
    }

    private void drawScore(Canvas canvas, Paint tintaPai) {
        Paint tintaPreta = new Paint();
        tintaPreta.setColor(Color.BLACK);
        tintaPreta.setAlpha(155);

        canvas.drawRect(0, canvas.getHeight()/2 - Util.inDP(80), canvas.getWidth(), canvas.getHeight()/2 + Util.inDP(80), tintaPreta);


        Paint tinta = new Paint(tintaPai);
        tinta.setColorFilter(null);
        tinta.setColor(Color.WHITE);
        tinta.setTextSize(Util.inDP(32));
        tinta.setTextAlign(Paint.Align.CENTER);

        int off = -50;
        canvas.drawText(game.getString(R.string.high_score) + " " + game.getDados().getInt("maiorScore", 0), canvas.getWidth()/2, canvas.getHeight()/2 + Util.inDP(off), tinta);


        off += 50;
        tinta.setTextSize(Util.inDP(24));
        canvas.drawText(game.getString(R.string.max_vidas) + " " + game.getDados().getInt("maxNumVida", 0), canvas.getWidth()/2, canvas.getHeight()/2 + Util.inDP(off), tinta);

        off += 50;
        tinta.setTextSize(Util.inDP(24));

        int minutosMax = game.getDados().getInt("maxTick", 0)/60;
        int segundosMax = game.getDados().getInt("maxTick", 0);
        while (segundosMax >= 60) segundosMax -= 60;
        String segundosMaxStr;
        if (segundosMax < 10) {
            segundosMaxStr = "0" + segundosMax;
        } else {
            segundosMaxStr = segundosMax + "";
        }

        canvas.drawText(game.getString(R.string.max_tick) + " " + minutosMax + ":" + segundosMaxStr, canvas.getWidth()/2,canvas.getHeight()/2 + Util.inDP(off), tinta);

    }



     /*
     =========================================
               GETTERS & SETTERS
     =========================================
     */


    private static void setInfo() {
        isInfo = true;
        isHighScore = false;
    }


    private static void setHighScore() {
        isInfo = false;
        isHighScore = true;
    }
}
