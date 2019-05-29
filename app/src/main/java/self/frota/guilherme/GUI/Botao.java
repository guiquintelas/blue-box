package self.frota.guilherme.GUI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import self.frota.guilherme.game.GameObj;
import self.frota.guilherme.game.GameState;
import self.frota.guilherme.input.GameListener;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.tools.Timer;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 21/09/2017.
 */

public abstract class Botao extends GameObj {

    private String texto;
    private boolean isAtivo = true;
    private boolean isApertado = false;

    private boolean isAni = false;
    private float xAni;
    private float yAni;
    private float aniProg = 0;

    private Paint tinta;

    private int corApertado = Color.argb(255, 179, 179, 179);
    private int corNaoApertado = Color.argb(255, 200, 200, 200);

    private float textSize = Util.inDP(15);

    private Canvas botao;
    private Bitmap bitmap;


    private GameListener listener;



    public Botao(GameState gameState, float x, float y, float width, float height, int texto) {
        super(gameState, x, y, width, height);

        this.texto = gameState.getGame().getString(texto);
        tinta = new Paint();
        tinta.setAntiAlias(true);
        tinta.setColor(corNaoApertado);
        tinta.setTextAlign(Paint.Align.CENTER);
        tinta.setTextSize(textSize);
        //tinta.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tinta.setTypeface(Util.FONT_BOTAO);

        bitmap = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_4444);
        botao = new Canvas(bitmap);


        listener = new GameListener() {
            @Override
            public boolean action(MotionEvent e) {
                if(!isAtivo) return true;

                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    if (e.getX() > getX() && e.getX() < getX() + getWidth()) {
                        if (e.getY() > getY() && e.getY() < getY() + getHeight()) {
                            if (!isApertado) {
                                acaoAni(e.getX(), e.getY());
                                isApertado = true;
                            }
                        }
                    }
                }

                if (e.getAction() == MotionEvent.ACTION_UP) {
                    if (e.getX() > getX() && e.getX() < getX() + getWidth()) {
                        if (e.getY() > getY() && e.getY() < getY() + getHeight()) {
                            if (isApertado && !isAni) {
                                isApertado = false;
                            }
                        }
                    }

                    isApertado = false;
                }

                return true;
            }
        };

        gameState.getListenerManager().addOnTouchListener(listener);
    }

    public abstract void acao();

    public void acaoPreAni() {

    };

    private void acaoAni(float x, float y) {
        isAni = true;
        xAni = x;
        yAni = y;

        Game.getVibrator().vibrate(30);


        final Timer aniLoop = new Timer(1) {
            @Override
            public void acao() {
                aniProg++;
            }
        };

        aniLoop.setLoop(true);

        acaoPreAni();

        new Timer(20) {
            @Override
            public void acao() {
                isAni = false;
                aniProg = 0;
                aniLoop.delete();

                Botao.this.acao();

            }
        };

    }

    public void centerX() {
        x = Game.getTela().getWidth()/2 - getWidth()/2;
    }

    public void setAtivo(boolean ativo) {
        this.isAtivo = ativo;
        isApertado = false;
    }

    public void clear() {
        gameState.getListenerManager().removeOnTouchListener(listener);
    }

    @Override
    public void update() {
        if (!isAtivo) return;
    }

    @Override
    public void draw(Canvas canvas, Paint tintaPai) {
        if (!isAtivo) return;

        if (isApertado || isAni) {
            tinta.setColor(corApertado);
        } else {
            tinta.setColor(corNaoApertado);
        }
        botao.drawRect(0, 0, getWidth(), getHeight(), tinta);



        tinta.setColor(Color.BLACK);
        botao.drawText(texto, getXCentro() - getX(), getYCentro() + Util.getFontHeight(tinta, texto)/2  - getY(), tinta);

        if (isAni) {
            tinta.setColor(Color.RED);
            float sizeAni = Util.inDP(7);

            Paint tintaAni = new Paint();


            float xInicial = xAni - sizeAni/2 - getX();
            float yInicial = yAni - sizeAni/2 - getY();

            for (float offX = -aniProg; offX <= aniProg; offX ++) {
                for (float offY = -aniProg; offY <= aniProg; offY ++) {
                    if (xInicial + offX > width || xInicial + offX < 0) continue;
                    if (yInicial + offY > height || yInicial + offY < 0) continue;

                    int alpha = 0;
                    float atual = aniProg - (Math.abs(offX) + Math.abs(offY));

                    if (atual == 0) alpha = 50;
                    if (atual == 1) alpha = 100;
                    if (atual == 2) alpha = 150;
                    if (atual == 3) alpha = 200;
                    //if (aniProg - (Math.abs(offX) + Math.abs(offY)) < 0) alpha = 0;


                    tintaAni.setAlpha(alpha);
                    if (alpha != 0)botao.drawRect(xInicial + (sizeAni * offX), yInicial + (sizeAni * offY), sizeAni + xInicial + (sizeAni * offX), sizeAni + yInicial  + (sizeAni * offY), tintaAni);
                }
            }
            //botao.drawRect(xInicial, yInicial, xInicial + sizeAni, yInicial + sizeAni, tintaAni);
        }

        canvas.drawBitmap(bitmap, getX(), getY(), tinta);
    }
}
