package self.frota.guilherme.particulas;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import self.frota.guilherme.game.GameObj;
import self.frota.guilherme.game.GameState;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 11/08/2017.
 */

public class CriadorDeParticulas extends GameObj {

    private float widthPar;
    private float heightPar;

    private float raio;

    private double speedPar = 0.25;
    private double speedParVariacao;
    private double anguloPar = 90;

    private double rotacaoVel = 0;
    private double rotacaoVelVariacao = 0;

    private float gravidadeRatePar = 0;
    private float gravdiadeRateVar = 0;
    private float speedRatePar = 0;

    private int porcentagem;
    private int alphaDuraçao = 50;
    private int anguloVariacao;
    private int alphaVariacao;
    private int alphaDelay = 0;
    private int porcentagemInicial = 0;
    private int alvoDelay;

    private ArrayList<Integer> cores = new ArrayList<>();

    private boolean isProduzindo = false;
    private boolean isAnguloVariado = false;
    private boolean isAlphaVariado = false;
    private boolean seguindo = false;
    private boolean isAlphaAtivo = true;
    private boolean isRotacaoVelVariado = false;
    private boolean isSpeedVariado = false;
    private boolean isGravidadeRateVariado = false;
    private boolean isAlvo = false;
    private boolean isCirculo = false;

    private GameObj alvo;
    private GameObj gameObj;


    public CriadorDeParticulas(GameState gameState, float xOri, float yOri, float widthOri, float heightOri, float widthPar, float heightPar, int color, int porcentagem) {
        super(gameState);
        this.x = xOri;
        this.y = yOri;
        this.widthPar = widthPar;
        this.heightPar = heightPar;
        this.width = widthOri;
        this.height = heightOri;
        cores.add(color);
        this.porcentagem = porcentagem;
        this.porcentagemInicial = porcentagem;
    }

    public CriadorDeParticulas(GameState gameState, GameObj gameObj, float xOri, float yOri, float widthOri, float heightOri, float widthPar, float heightPar, int color, int porcentagem) {
        super(gameState);
        this.x = xOri;
        this.y = yOri;
        this.widthPar = widthPar;
        this.heightPar = heightPar;
        this.width = widthOri;
        this.height = heightOri;
        cores.add(color);
        this.porcentagem = porcentagem;
        this.porcentagemInicial = porcentagem;
        this.gameObj = gameObj;
    }

    public CriadorDeParticulas(GameState gameState, GameObj gameObj, float xOri, float yOri, float raio, float widthPar, float heightPar, int color, int porcentagem) {
        super(gameState);
        this.x = xOri;
        this.y = yOri;
        this.widthPar = widthPar;
        this.heightPar = heightPar;
        this.raio = raio;
        isCirculo = true;
        cores.add(color);
        this.porcentagem = porcentagem;
        this.porcentagemInicial = porcentagem;
        this.gameObj = gameObj;
    }



    public void setPorcentagem(int porcentagem) {
        this.porcentagem = porcentagem;
    }

    public int getPorcentagemInicial() {
        return porcentagemInicial;
    }

    public void setAlphaDelay(int ticks) {
        this.alphaDelay = ticks;
    }

    public boolean isProduzindo() {
        return isProduzindo;
    }

    public boolean isAlphaAtivo() {
        return isAlphaAtivo;
    }

    public void setAlphaAtivo(boolean isAtivo) {
        this.isAlphaAtivo = isAtivo;
    }

    public void setAlvo(GameObj alvo, int delay) {
        this.alvo = alvo;
        this.alvoDelay = delay;
        isAlvo = true;
    }

    public void setSpeedRate(float rate) {
        this.speedRatePar = rate;
    }


    private float setRandomX() {
        if (!isCirculo) {
            int xVar = (int) (Math.random() * (width - widthPar));
            return getX() + xVar;
        } else {

            float xVar = Util.randomFloat(0, raio) * (float)Math.cos(Math.toRadians(Util.randomDouble(0, 360)));
            return getX() + xVar;
        }

    }

    private float setRandomY() {
        if (!isCirculo) {
            int yVar = (int) (Math.random() * (height - heightPar));
            return getY() + yVar;

        } else {

            float yVar = Util.randomFloat(0, raio) * (float)Math.sin(Math.toRadians(Util.randomDouble(0, 360)));
            return getY() + yVar;
        }
    }

    public void update(float x, float y) {
        updateXY(x, y);
        criarParticula();
    }

    public void addColor(int novaCor) {
        cores.add(novaCor);
    }

    public void removeColor(int novaCor) {
        if (!cores.contains(novaCor)) {
            return;
        }

        if (cores.size() == 1) {
            return;
        }

        cores.remove(novaCor);
    }


    public void resetCorPadrao() {
        int corPadrao = cores.get(0);
        cores.clear();
        cores.add(corPadrao);
    }

    private double setAngulo() {
        if (isAnguloVariado) {
            double novoAngulo = anguloPar;
            novoAngulo += anguloVariacao - (int) (Math.random() * (anguloVariacao * 2));
            return novoAngulo;

        } else {
            return anguloPar;
        }
    }

    public void setRotacaoVel(double rotacaoVel) {
        this.rotacaoVel = rotacaoVel;
    }

    public void setRotacaoVelVar(double rotacaoVel, double variacao) {
        this.rotacaoVel = rotacaoVel;
        this.rotacaoVelVariacao = variacao;
        this.isRotacaoVelVariado = true;
    }

    private double getRotacaoVel() {
        if (isRotacaoVelVariado) {
            return Util.randomDouble(rotacaoVel - rotacaoVelVariacao, rotacaoVel + rotacaoVelVariacao);
        } else {
            return rotacaoVel;
        }
    }

    public void setAlphaVar(int duraçao) {
        this.alphaDuraçao = duraçao;
    }

    public void setAlphaVar(int duraçao, int variacao) {
        this.alphaDuraçao = duraçao;
        this.alphaVariacao = variacao;
        this.isAlphaVariado = true;
    }

    private float setAlpha() {
        if (isAlphaVariado) {
            int novaDuraçao = alphaDuraçao;
            novaDuraçao += alphaVariacao - (int) (Math.random() * (alphaVariacao * 2));
            if (novaDuraçao <= 0) {
                novaDuraçao = 1;
            }
            return 1.0f / novaDuraçao;
        }

        return 1.0f / alphaDuraçao;
    }

    private void criarParticula() {
        if (isProduzindo) {
            int porcentagemTemp = porcentagem;

            while (porcentagemTemp > 100) {

                criaParticula();

                porcentagemTemp -= 100;
            }

            int chance = 1 + (int) (Math.random() * 100);
            if (chance <= porcentagemTemp) {

                criaParticula();
            }
            //setProduzindo(false);
        }

    }

    private void criaParticula() {
        Particula par = new Particula(gameState, setRandomX(), setRandomY(), widthPar, heightPar, getCor(), getSpeed(), setAngulo(), setAlpha(),alphaDelay, seguindo, getGravidateRate(), speedRatePar, this);

        if (isAlvo) {
            par.setAlvo(alvo, alvoDelay);
        }

    }

    private int getCor() {
        int randomCor = (int) (Math.random() * cores.size());
        return cores.get(randomCor);
    }

    public void setAngulo(double angulo) {
        this.anguloPar = angulo;
    }

    public void setAngulo(double angulo, int variacao) {
        this.anguloPar = angulo;
        this.anguloVariacao = variacao;
        this.isAnguloVariado = true;
    }

    public void setSpeed(double speed) {
        this.speedPar = speed;
    }

    public void setSpeedVar(double speed, double variacao) {
        this.speedPar = speed;
        this.speedParVariacao = variacao;
        isSpeedVariado = true;
    }

    private double getSpeed() {
        if (isSpeedVariado) {
            return Util.randomDouble(speedPar - speedParVariacao, speedPar + speedParVariacao);
        } else {
            return speedPar;
        }
    }

    public void setGravidadeRate(float rate) {
        this.gravidadeRatePar = rate;
    }

    public void setGravidadeRateVar(float rate, float var) {
        this.gravidadeRatePar = rate;
        this.gravdiadeRateVar = var;
        this.isGravidadeRateVariado = true;
    }

    private float getGravidateRate() {
        if (isGravidadeRateVariado) {
            return Util.randomFloat(gravidadeRatePar - gravdiadeRateVar, gravidadeRatePar + gravdiadeRateVar);
        } else {
            return gravidadeRatePar;
        }
    }

    public void setProduzindo(boolean isProduzindo) {
        this.isProduzindo = isProduzindo;
    }


    private void updateXY(float x, float y) {
        this.x = x;
        this.y = y;
    }



    @Override
    public void update() {
        update(gameObj.getX(), gameObj.getY());
    }

    @Override
    public void draw(Canvas canvas, Paint tintaPai) {

    }
}
