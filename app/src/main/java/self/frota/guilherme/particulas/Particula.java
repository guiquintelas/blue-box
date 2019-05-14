package self.frota.guilherme.particulas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import self.frota.guilherme.game.GameObj;
import self.frota.guilherme.game.GameState;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.tools.Timer;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 11/08/2017.
 *
 */

public class Particula extends GameObj {
    private float width;
    private float height;

    private double angulo;
    private double speed;
    private float gravidade = 0;
    private float gravidadeRate = 0;

    private float alpha = 1.0f;
    private float alphaVar;
    private float speedRate;

    private int cor;

    private double xOff = 0;
    private double yOff = 0;
    private double xDiff;
    private double yDiff;

    private boolean seguindo = false;
    private boolean isAlphaPronto = false;
    private boolean isAlvo = false;

    private self.frota.guilherme.particulas.CriadorDeParticulas cdp;
    private GameObj alvo;

    public static ArrayList<Particula> todasParticulas = new ArrayList<>();

    Particula(GameState gameState, float x, float y, float width, float height, int cor, double speed, double angulo, float alphaVar, int tickDelayAlpha, boolean seguindo, float gravidadeRate, float speedRate, self.frota.guilherme.particulas.CriadorDeParticulas cdp) {
        super(gameState);
        this.x = x;
        this.y = y;
        this.cor = cor;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.angulo = angulo;
        this.alphaVar = alphaVar;
        this.seguindo = seguindo;
        this.gravidadeRate = gravidadeRate;
        this.cdp = cdp;
        this.speedRate = speedRate;

        xDiff = x - cdp.getX();
        yDiff = y - cdp.getY();

        if (tickDelayAlpha > 0) {
            new Timer(tickDelayAlpha) {
                public void acao() {
                    isAlphaPronto = true;
                }
            };

        } else {
            isAlphaPronto = true;
        }

        todasParticulas.add(this);


        //new Luz(this, Util.randomInt(3, 5), Util.randomInt(0, 255), Util.randomInt(0, 255), Util.randomInt(0, 255), Util.randomInt(60, 100), 25);
    }

    void setAlvo(GameObj alvo, int delay) {
        this.alvo = alvo;

        new Timer(delay) {
            public void acao() {
                isAlvo = true;
            }
        };
    }


    public void update() {
        move();
        updateAlpha();
        gravidade();
        updateSpeed();
        checaAlvo();
    }

    private void updateSpeed() {
        speed += speedRate;
    }


    private void checaAlvo() {
        if (isAlvo) {
            Rect particulaR = new Rect((int)x, (int)y, (int)(x + width), (int)(y + height));
            Rect alvoR = new Rect((int)alvo.getX(), (int)alvo.getY(), (int)(alvo.getX() + alvo.getWidth()), (int)(alvo.getY() + alvo.getHeight()));

            if (particulaR.intersect(alvoR)) remove();
        }
    }

    @Override
    public void draw(Canvas canvas, Paint tintaPai) {
        Paint tinta = new Paint(tintaPai);
        tinta.setColor(cor);
        tinta.setAlpha((int)(alpha * 255.0f));
        canvas.drawOval(x + 1, y + 1, x + width,y + height, tinta);
    }

    private void gravidade() {
        y += gravidade;
        gravidade += gravidadeRate;
    }


    private void move() {
        if (seguindo) {
            xOff += Math.cos(Math.toRadians(angulo)) * Util.inDP(speed);
            yOff -= Math.sin(Math.toRadians(angulo)) * Util.inDP(speed);
            x = (float)(cdp.getX() + xDiff);
            y = (float)(cdp.getY() + yDiff);

            x += xOff;
            y += yOff;
        } else {

            if (isAlvo) {
                updateAngulo();
            }

            x += Math.cos(Math.toRadians(angulo)) * Util.inDP(speed);
            y -= Math.sin(Math.toRadians(angulo)) * Util.inDP(speed);
        }

    }

    private void updateAngulo() {
        double anguloNovo = Math.toDegrees(Math.atan2(alvo.getXCentro() - getXCentro(), alvo.getYCentro() - getYCentro())) - 90;
        if (anguloNovo < 0) {
            anguloNovo += 360;
        }

        double diferenca = anguloNovo - angulo;
        if (diferenca > 180) {
            diferenca -= 360;
        }

        if (diferenca < -180) {
            diferenca += 360;
        }

        double dist = Math.sqrt((alvo.getXCentro() - getXCentro()) * (alvo.getXCentro() - getXCentro()) + (alvo.getYCentro() - getYCentro()) * (alvo.getYCentro() - getYCentro()));

        float anguloSharpness = 10.0f;
        if (dist < Util.inDP(35)) {
            anguloSharpness = 10.0f - (float)((Util.inDP(35) - dist)/Util.inDP(35)) * 10;
            if (anguloSharpness < 1) anguloSharpness = 1;
        }

        angulo += diferenca/anguloSharpness;
        if ( angulo > 360) angulo -= 360;
        if ( angulo < 0) angulo += 360;
    }

    private synchronized void updateAlpha() {
        if (isAlphaPronto) {
            alpha -= alphaVar;

            if (alpha <= 0) {
                alpha = 0;
                remove();
            }
        }

    }

    private synchronized void remove() {
        todasParticulas.remove(this);
    }

}
