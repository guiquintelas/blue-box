package self.frota.guilherme.game.states;


import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import self.frota.guilherme.GUI.Botao;
import self.frota.guilherme.game.FallingObj;
import self.frota.guilherme.game.GameObj;
import self.frota.guilherme.game.objs.Pedra;
import self.frota.guilherme.game.objs.Player;
import self.frota.guilherme.game.objs.Vida;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.game.GameState;
import self.frota.guilherme.principal.GameThread;
import self.frota.guilherme.R;
import self.frota.guilherme.tools.Util;
import self.frota.guilherme.tools.Variator;
import self.frota.guilherme.tools.VariatorNumero;

/**
 * Created by Frota on 06/08/2017.
 *
 */

public class JogoState extends GameState {

    private static int score = 0;
    private int dificuldade = 1;
    private int ultimaVida = 0;
    private Player player;

    private boolean perdeu = false;
    private boolean maiorScore = false;
    private boolean maiorMaxVida = false;
    private boolean maiorMaxTick = false;

    private int pauseTextAlpha = 255;

    private Botao botaoNovoJogo;
    private Botao botaoTelaInicial;

    private Variator varPauseTextAlpha;

    private int tickInit;

    protected void initState() {
        score = 0;
        tickInit = GameThread.TOTAL_TICK;

        player = new Player(Game.getTela(), this);
        player.init();

        botaoNovoJogo = new Botao(this, 0, Game.getTela().getHeight()/2.0f + (int)Util.inDP(66), Util.inDP(140), Util.inDP(38), R.string.novo_jogo_botao) {
            @Override
            public void acao() {
                game.setGameState(new JogoState());
            }
        };
        botaoNovoJogo.centerX();
        botaoNovoJogo.setAtivo(false);


        botaoTelaInicial = new Botao(this, 0, Game.getTela().getHeight()/2.0f + (int)Util.inDP(133), Util.inDP(140), Util.inDP(38), R.string.tela_inicial_botao) {
            @Override
            public void acao() {
                game.setGameState(new MenuState());

            }
        };
        botaoTelaInicial.centerX();
        botaoTelaInicial.setAtivo(false);

        varPauseTextAlpha = new Variator(new VariatorNumero() {
            @Override
            public double getNumero() {
                return pauseTextAlpha;
            }

            @Override
            public void setNumero(double numero) {
                pauseTextAlpha = (int)numero;
            }

            @Override
            public boolean devoContinuar() {
                return pauseTextAlpha > 0;
            }
        });
        varPauseTextAlpha.setAtivoQuandoPausado(true);


        pausar();
    }

    public void pausar() {
        super.pausar();
        player.pausar();

        pauseTextAlpha = 255;
        varPauseTextAlpha.variar(false);
    }

    public void despausar() {
        player.despausar();
        fadePauseText();
    }

    private void fadePauseText() {
        pauseTextAlpha = 255;
        varPauseTextAlpha.syncNumero();
        varPauseTextAlpha.fadeOutSin(255, 0, 30);
        varPauseTextAlpha.variar(true);
    }


    protected void closeState() {
        FallingObj.todosFallingObj.clear();

        botaoNovoJogo.clear();
        botaoTelaInicial.clear();
    }


    /*
     =========================================
                     UPDATE
     =========================================
     */


    public void updateState() {
        aumentaDificuldade();
        criaObjs();

        player.update();
        FallingObj.updateTodos();
    }

    @Override
    protected void updatePausado() {
        if (pauseTextAlpha <= 0) {
            super.despausar();

            pauseTextAlpha = 255;
            varPauseTextAlpha.variar(false);
        }
    }

    private void setFilter(Paint tintaPai) {
        if (player.getVida() <= 0) {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            tintaPai.setColorFilter(f);
        }
    }

    private void aumentaDificuldade() {
        if (GameThread.TOTAL_TICK % 20 == 1 && !perdeu)
            dificuldade++;
    }

    private void criaObjs() {
        if (freqPedra() && FallingObj.todosFallingObj.size() < limitePedra()) {

            if (checaVida()) {
                new Vida(this, Util.randomInt(0, Game.getTela().getWidth() - (int)Util.inDP(13)), Util.randomFloat(2 + (dificuldade / 60f), 4 + (dificuldade / 50f))/1.5);
                return;
            }

            new Pedra(this, Util.randomInt(0, Game.getTela().getWidth() - (int)Util.inDP(13)), Util.randomFloat(2 + (dificuldade / 60f), 4 + (dificuldade / 50f))/1.5);
        }
    }

    private boolean checaVida() {
        ultimaVida++;
        int lifeGap = 40 - (getDificuldade()/60);
        if (lifeGap < 30) lifeGap = 30;

        if (ultimaVida > lifeGap) {
            ultimaVida = 0;
            return true;
        }

        return  false;
    }

    public void aumentaScore(int quanto) {
        if (!perdeu)
            score+=quanto;
    }

    public void diminueScore(int quanto) {
        if (!perdeu && score >= quanto)
            score-=quanto;
    }

    private boolean freqPedra() {
        int freq = (20 - (dificuldade / 20));
        if (freq < 5) {
            freq = (5 - ((dificuldade - 300) / 100));
            if (freq < 0) freq = 1;
        }

        return freq <= 1 || GameThread.TOTAL_TICK % freq == 1;

    }

    private int getDificuldade() {
        return dificuldade;
    }

    public void perdeu() {
        if (perdeu) return;

        perdeu = true;

        int highScore = Game.getDados().getInt("maiorScore", 0);
        int maxNumVida = Game.getDados().getInt("maxNumVida", 0);
        int maxTick = Game.getDados().getInt("maxTick", 0);

        SharedPreferences.Editor editor = Game.getDados().edit();

        if (score > highScore) {
            maiorScore = true;
            editor.putInt("maiorScore", score);
        }

        if (player.getMaxNumVida() > maxNumVida) {
            maiorMaxVida = true;
            editor.putInt("maxNumVida", player.getMaxNumVida());
        }

        if (player.getMaiorTempoSemHit() > maxTick ) {
            maiorMaxTick = true;
            editor.putInt("maxTick", player.getMaiorTempoSemHit());
        }


        if (maiorScore || maiorMaxVida || maiorMaxTick) editor.apply();


        botaoNovoJogo.setAtivo(true);
        botaoTelaInicial.setAtivo(true);
    }

    private int limitePedra() {
        int limite = 20 + (dificuldade/50);

        if (limite > 30) limite = 30;

        return limite;
    }

    public GameObj getProximaVida() {
        return new GameObj(this, Util.inDP(2) + Util.inDP(2), Game.getTela().getHeight() - Util.inDP(20) + Util.inDP(2) - Util.inDP(15) * player.getVida(), Util.inDP(6), Util.inDP(6)) {
            @Override
            public void update() {

            }

            @Override
            public void draw(Canvas canvas, Paint tintaPai) {

            }
        };
    }

     /*
     =========================================
                     DRAW
     =========================================
     */



    public void drawState(Canvas canvas, Paint tintaPai) {
        setFilter(tintaPai);
        if (perdeu) drawScoreFinal(canvas, tintaPai);
        drawVidas(canvas, tintaPai);
        drawScore(canvas);
        drawDificuldade(canvas);
        player.draw(canvas, tintaPai);
        FallingObj.drawTodos(canvas, tintaPai);

        drawPausado(canvas, tintaPai);

        botaoNovoJogo.draw(canvas, tintaPai);
        botaoTelaInicial.draw(canvas, tintaPai);

    }

    private void drawVidas(Canvas canvas, Paint tintaPai) {
        Paint tinta = new Paint(tintaPai);
        tinta.setColor(Color.GREEN);

        float xOff = Util.inDP(15);
        for (int i = 0; i < player.getVida(); i++) {
            canvas.drawRect(Util.inDP(2), canvas.getHeight() - Util.inDP(20) -  xOff*i, Util.inDP(2) + Util.inDP(10), canvas.getHeight() - Util.inDP(10) -  xOff*i, tinta);
        }

    }

    private void drawScore(Canvas canvas) {
        String scoreText = game.getString(R.string.pontos) + ": " + score;
        float stringWidth = Game.getTintaFPS().measureText(scoreText);
        canvas.drawText(scoreText, canvas.getWidth() - stringWidth - Util.inDP(4), Util.inDP(13), Game.getTintaFPS());

    }

    private void drawScoreFinal(Canvas canvas, Paint tintaPai) {
        float fontSizeBase = 40;

        Paint tinta = new Paint(tintaPai);
        tinta.setColorFilter(null);
        tinta.setColor(Color.WHITE);
        tinta.setTextSize(Util.inDP(fontSizeBase));
        tinta.setTextAlign(Paint.Align.CENTER);

        int off = (int)((canvas.getHeight()/2  + Util.inDP(66))/2 - Util.inDP(125)/2);


        canvas.drawText(game.getString(R.string.pontos_final) + ": " + score, canvas.getWidth()/2f, off, tinta);
        off += Util.inDP(25);

        tinta.setTextSize(Util.inDP(fontSizeBase - 16));
        if (maiorScore) {
            tinta.setColor(Color.BLUE);
            canvas.drawText(game.getString(R.string.bateu_recorde), canvas.getWidth()/2f, off, tinta);
            tinta.setColor(Color.WHITE);
            canvas.drawText(game.getString(R.string.bateu_recorde), canvas.getWidth()/2f + 3, off +3, tinta);
        } else {
            canvas.drawText(game.getString(R.string.recorde) + ": " + Game.getDados().getInt("maiorScore", 0), canvas.getWidth()/2f, off, tinta);
        }

        off += Util.inDP(50);
        tinta.setTextSize(Util.inDP(fontSizeBase - 14));
        canvas.drawText(game.getString(R.string.max_vidas) + " " + player.getMaxNumVida(), canvas.getWidth()/2f, off, tinta);

        off += Util.inDP(20);

        tinta.setTextSize(Util.inDP(fontSizeBase - 17));
        if (maiorMaxVida) {
            tinta.setColor(Color.BLUE);
            canvas.drawText(game.getString(R.string.bateu_recorde), canvas.getWidth()/2f, off, tinta);
            tinta.setColor(Color.WHITE);
            canvas.drawText(game.getString(R.string.bateu_recorde), canvas.getWidth()/2f + 3, off +3, tinta);
        } else {
            canvas.drawText(game.getString(R.string.recorde) + ": " + Game.getDados().getInt("maxNumVida", 0), canvas.getWidth()/2f, off, tinta);
        }

        off += Util.inDP(50);
        tinta.setTextSize(Util.inDP(fontSizeBase - 14));

        int minutos = player.getMaiorTempoSemHit()/60;
        int segundos = player.getMaiorTempoSemHit();
        while (segundos >= 60) segundos -= 60;
        String segundosStr;
        if (segundos < 10) {
            segundosStr = "0" + segundos;
        } else {
            segundosStr = segundos + "";
        }

        int minutosMax = Game.getDados().getInt("maxTick", 0)/60;
        int segundosMax = Game.getDados().getInt("maxTick", 0);
        while (segundosMax >= 60) segundosMax -= 60;
        String segundosMaxStr;
        if (segundosMax < 10) {
            segundosMaxStr = "0" + segundosMax;
        } else {
            segundosMaxStr = segundosMax + "";
        }

        canvas.drawText(game.getString(R.string.max_tick) + " " + minutos + ":" + segundosStr, canvas.getWidth()/2f, off, tinta);

        off += Util.inDP(20);

        tinta.setTextSize(Util.inDP(fontSizeBase - 17));
        if (maiorMaxTick) {
            tinta.setColor(Color.BLUE);
            canvas.drawText(game.getString(R.string.bateu_recorde), canvas.getWidth()/2f, off, tinta);
            tinta.setColor(Color.WHITE);
            canvas.drawText(game.getString(R.string.bateu_recorde), canvas.getWidth()/2f + 3, off +3, tinta);
        } else {
            canvas.drawText(game.getString(R.string.recorde) + ": "+ minutosMax + ":" + segundosMaxStr, canvas.getWidth()/2f, off, tinta);
        }
    }

    private void drawDificuldade(Canvas canvas) {
        String dif = game.getString(R.string.nivel) + ":  " + ((dificuldade) / 20);
        float stringWidth = Game.getTintaFPS().measureText(dif);
        canvas.drawText(dif, canvas.getWidth()/2f - stringWidth/2,  Util.inDP(13), Game.getTintaFPS());
    }



    private void drawPausado(Canvas canvas, Paint tintaPai) {
        if (!isPausado()) return;

        Paint tintaPreta = new Paint();
        tintaPreta.setColor(Color.BLACK);
        tintaPreta.setAlpha(155);

        canvas.drawRect(0, canvas.getHeight()/2f - Util.inDP(33), canvas.getWidth(), canvas.getHeight()/2f + Util.inDP(33), tintaPreta);

        Paint tinta = new Paint(tintaPai);
        tinta.setColor(Color.WHITE);
        tinta.setTextSize(Util.inDP(40));
        tinta.setAntiAlias(true);
        tinta.setTextAlign(Paint.Align.CENTER);
        tinta.setAlpha(pauseTextAlpha);

        // maps pauseTextAlpha to radius
        // 255 - 0
        // 0   - 32

        float maxAlpha = 255f;
        float maxRadius = 8f;
        float radius = -((maxRadius / maxAlpha) * pauseTextAlpha) + maxRadius;

        if (radius != 0) {
            tinta.setMaskFilter(new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL));
        }

        canvas.drawText(game.getString(R.string.pausado), canvas.getWidth()/2f, canvas.getHeight()/2f, tinta);
        tinta.setTextSize(Util.inDP(24));
        canvas.drawText(game.getString(R.string.pausado_frase), canvas.getWidth()/2f, canvas.getHeight()/2f + Util.inDP(20), tinta);
    }

    /*
     =========================================
               GETTERS & SETTERS
     =========================================
     */

    public boolean jaPerdeu() {
        return perdeu;
    }

    public int getTickInit() {
        return tickInit;
    }

    public Player getPlayer() {
        return player;
    }
}

