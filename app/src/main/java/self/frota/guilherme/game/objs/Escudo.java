package self.frota.guilherme.game.objs;

import android.graphics.Color;

import self.frota.guilherme.game.FallingObj;
import self.frota.guilherme.game.states.JogoState;
import self.frota.guilherme.particulas.CriadorDeParticulas;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 05/10/2017.
 */

public class Escudo extends FallingObj {
    private CriadorDeParticulas particulasEscudo;


    public Escudo(JogoState jogoState, float x, float y, float size, double speed, int cor) {
        super(jogoState,
                x,
                0,
                (float) Util.inDP((Game.getTela().getWidth()/Game.SCALE)/ Pedra.PEDRA_SCALE + Util.randomFloat(0.2f, 0.8f)),
                speed,
                Color.CYAN);



    }

    @Override
    protected void fimTelaAcao() {

    }

    @Override
    protected void colisaoAcao() {

    }

    @Override
    protected void updateObj() {

    }
}
