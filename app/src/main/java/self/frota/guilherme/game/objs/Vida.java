package self.frota.guilherme.game.objs;

import android.graphics.Color;

import self.frota.guilherme.game.FallingObj;
import self.frota.guilherme.game.states.JogoState;
import self.frota.guilherme.particulas.CriadorDeParticulas;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 25/09/2017.
 *
 */

public class Vida extends FallingObj {


    private CriadorDeParticulas particulaVida;


    public Vida(JogoState jogoState, float x, double speed) {
        super(jogoState, x, 0, (float) Util.inDP((Game.getTela().getWidth()/Game.SCALE)/ Pedra.PEDRA_SCALE + 3), speed, Color.GREEN);

        particulaVida = new CriadorDeParticulas(gameState, x, y, width, height, Util.inDP(3), Util.inDP(3), Color.GREEN, 30);
        particulaVida.setGravidadeRate(0);
        particulaVida.setAngulo(90, 20);
        particulaVida.setSpeedVar(0.133, 0.02);
        particulaVida.addColor(Color.rgb(50, 255, 50));
        particulaVida.setAlphaDelay(20);
        particulaVida.setAlphaVar(20, 10);
        particulaVida.setProduzindo(true);
    }

    @Override
    protected void fimTelaAcao() {

    }

    @Override
    protected void colisaoAcao() {
        player.ganhaVida();
    }

    @Override
    protected void updateObj() {
        particulaVida.update(x, y);
    }


}
