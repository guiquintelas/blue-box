package self.frota.guilherme.game.objs;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import self.frota.guilherme.game.GameObj;
import self.frota.guilherme.game.GameState;
import self.frota.guilherme.game.states.JogoState;
import self.frota.guilherme.input.GameListener;
import self.frota.guilherme.particulas.CriadorDeParticulas;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.principal.GameSurface;
import self.frota.guilherme.principal.GameThread;
import self.frota.guilherme.tools.ActionQueue;
import self.frota.guilherme.tools.Util;
import self.frota.guilherme.tools.Variator;
import self.frota.guilherme.tools.VariatorNumero;

/**
 * Created by Frota on 01/08/2017.
 *
 */

public class Player extends GameObj {
    private GameSurface tela;
    private JogoState gameState;

    private int alphaAniPerdeVida = 0;

    private boolean ganhaVida = false;

    private int vida = 1;
    private int cor = Color.BLUE;
    private int maxNumVida = vida;
    private int tickUltimoHit;
    private int maiorTempoSemHit;
    private float initSize;

    private int corBola = Color.argb(255, 200, 200, 200);
    private int alphaBola = 16;

    private Variator varAniVida;
    private Variator varAlphaBola;

    private GameListener listenerPlayer;

    private CriadorDeParticulas particulaPegaVida;

    private CriadorDeParticulas particulasTeste;

    private static final double PLAYER_SCALE = 12.03007518796992;

    public Player(GameSurface tela, JogoState state) {
        super(state);
        this.tela = tela;
        this.width = (float)Util.inDP((Game.getTela().getWidth()/Game.SCALE)/PLAYER_SCALE);
        this.height = width;
        this.initSize = width;
        this.x = tela.getWidth()/2f - width/2;
        this.y = tela.getHeight() - Util.inDP(100);
        this.gameState = state;
        this.tickUltimoHit = state.getTickInit();
    }

    public void init() {
        listenerPlayer = new GameListener() {
            @Override
            public boolean action(MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_MOVE && !JogoState.isPausado()) {
                    x = (int)e.getX() - width/2;
                }

                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!(e.getX() >= getX() - Util.inDP(13.3) && e.getX() <= getX() + width + Util.inDP(13.3))) {
                        jogoState.pausar();
                    }
                }

                if (e.getAction() == MotionEvent.ACTION_UP) {
                    jogoState.pausar();
                }

                if (e.getAction() == MotionEvent.ACTION_DOWN && JogoState.isPausado()) {
                    if (e.getX() >= getX() - Util.inDP(13.3) && e.getX() <= getX() + width + Util.inDP(13.3)) {
                        jogoState.despausar();
                    }

                }

                return true;
            }
        };

        gameState.getListenerManager().addOnTouchListener(listenerPlayer);



        varAniVida = new Variator(new VariatorNumero() {
            @Override
            public double getNumero() {
                return alphaAniPerdeVida;
            }

            @Override
            public void setNumero(double numero) {
                alphaAniPerdeVida = (int)numero;

            }

            @Override
            public boolean devoContinuar() {
                return true;
            }
        });

        particulaPegaVida = new CriadorDeParticulas(gameState, x, y, width, height, Util.inDP(4), Util.inDP(4), Color.GREEN, 300);
        particulaPegaVida.setAngulo(0, 360);
        particulaPegaVida.setAlphaDelay(50);
        particulaPegaVida.setAlphaVar(40, 15);
        particulaPegaVida.setSpeedVar(1.33, 0.5);
        //particulaPegaVida.setGravidadeRateVar(0.2f + Util.limita(gameState.getDificuldade()/150.0f, 0, 0.2f), 0.05f);
        particulaPegaVida.setGravidadeRate(0);
        particulaPegaVida.setAlvo(gameState.getProximaVida(), 20);
        particulaPegaVida.setSpeedRate(0.1f);

        particulasTeste = new CriadorDeParticulas(jogoState, this, getXCentro(), getYCentro(), 60, Util.inDP(4), Util.inDP(4), Color.WHITE, 100);
        particulasTeste.setGravidadeRate(0);
        particulasTeste.setSpeed(1);
        particulasTeste.setSpeedRate(0);
        particulasTeste.setAlphaDelay(20);

        //particulasTeste.setProduzindo(true);

    }

    public int getVida() {
        return vida;
    }

    public int getMaxNumVida() {
        return maxNumVida;
    }

    public int getMaiorTempoSemHit() {
        return maiorTempoSemHit;
    }

    public GameListener getListener() {
        return listenerPlayer;
    }

    public void pausar() {
        alphaBola = 16;

        varAlphaBola = new Variator(new VariatorNumero() {
            public double getNumero() {
                return alphaBola;
            }

            public void setNumero(double numero) {
                alphaBola = (int) Util.limita(numero, 1, 32);
            }

            public boolean devoContinuar() {
                return JogoState.isPausado();
            }
        });
        varAlphaBola.setAtivoQuandoPausado(true);

        varAlphaBola.pararOscilar();
        varAlphaBola.variar(false);
        varAlphaBola.oscilar(32, 80, true);
        varAlphaBola.variar(true);
    }

    public void despausar() {
        varAlphaBola.variar(false);
    }

    public void perdeVida() {
        //se ja perdeu nao pode perder vida
        if (gameState.jaPerdeu()) return;

        ganhaVida = false;

        vida--;

        //aumenta tamanho
        if (width < initSize) {
            width += initSize/40.0f;
            height += initSize/40.0f;
        }

        //inicia variador de cor
        varAniVida.variar(false);

        varAniVida.fadeInSin(0, 255, 10);
        varAniVida.variar(true);
        varAniVida.addEsperaNaFila(5);
        varAniVida.addAcaoNaFila(new ActionQueue() {
            public boolean action() {
                alphaAniPerdeVida = 0;
                return true;
            }
        });

        int tempoSemHit = (GameThread.TOTAL_TICK - tickUltimoHit)/60;
        if (maiorTempoSemHit < tempoSemHit) maiorTempoSemHit = tempoSemHit;

        //seta ultimo tick de hit
        tickUltimoHit = GameThread.TOTAL_TICK;

        //diminue o score
        gameState.diminueScore(25);

        //vibra o cel
        Game.getVibrator().vibrate(70);

        //checa se perdeu
        if (vida <= 0) {
            gameState.perdeu();
            gameState.getListenerManager().removeOnTouchListener(listenerPlayer);
        }
    }

    public void ganhaVida() {
        //se ja perdeu nao pode ganhar vida
        if (gameState.jaPerdeu()) return;


        ganhaVida = true;

        //diminue o tamanho
        if (width > initSize/2) {
            width -= initSize/40.0f;
            height -= initSize/40.0f;
        }

        //aumenta o score
        gameState.aumentaScore(25);

        //varia a cor
        varAniVida.variar(false);

        varAniVida.fadeInSin(0, 255, 10);
        varAniVida.variar(true);
        varAniVida.addEsperaNaFila(5);
        varAniVida.addAcaoNaFila(new ActionQueue() {
            public boolean action() {
                alphaAniPerdeVida = 0;
                particulaPegaVida.setProduzindo(false);
                return true;
            }
        });

        particulaPegaVida.setAlvo(gameState.getProximaVida(), 10);
        particulaPegaVida.setProduzindo(true);

        //vibra o cel
        Game.getVibrator().vibrate(45);

        vida++;

        if (maxNumVida < vida) maxNumVida = vida;
    }

    public void update() {
        particulaPegaVida.update(x, y);
        particulasTeste.update();

    }


    public void draw(Canvas canvas, Paint tintaPai) {
        Paint tinta = new Paint(tintaPai);
        tinta.setColor(cor);
        canvas.drawRect(x, y, x + width, y + height, tinta);

        if (ganhaVida) {
            tinta.setColor(Color.GREEN);
        } else {
            tinta.setColor(Color.RED);
        }


        tinta.setAlpha(alphaAniPerdeVida);
        canvas.drawRect(x, y, x + width, y + height, tinta);

        if (JogoState.isPausado()) {
            Paint tintaBola = new Paint();
            tintaBola.setAntiAlias(true);
            tintaBola.setColor(corBola);
            tintaBola.setAlpha(255 - (int)(alphaBola * 7.96875));

            tintaBola.setMaskFilter(new BlurMaskFilter(alphaBola + 4, BlurMaskFilter.Blur.NORMAL));
            canvas.drawOval(getX() - Util.inDP(5), getY() + Util.inDP(50) - Util.inDP(5), getX() + width + Util.inDP(5), getY() + Util.inDP(50) + height + Util.inDP(5), tintaBola);
        }


    }
}
