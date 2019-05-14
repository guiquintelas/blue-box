package self.frota.guilherme.game.objs;

import android.graphics.Color;


import self.frota.guilherme.game.FallingObj;
import self.frota.guilherme.game.GameObj;
import self.frota.guilherme.game.GameState;
import self.frota.guilherme.game.states.JogoState;
import self.frota.guilherme.particulas.CriadorDeParticulas;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 01/08/2017.
 *
 */

public class Pedra extends FallingObj {

    static final double PEDRA_SCALE = 24.61538461538462;

    public Pedra(JogoState jogoState, float x, double speed) {
        super(jogoState, x, 0, (float) Util.inDP((Game.getTela().getWidth()/Game.SCALE)/Pedra.PEDRA_SCALE), speed, Color.CYAN);
    }


    @Override
    protected void fimTelaAcao() {
        jogoState.aumentaScore(5);
    }

    @Override
    protected void colisaoAcao() {
        player.perdeVida();
    }

    @Override
    protected void updateObj() {

    }
}
