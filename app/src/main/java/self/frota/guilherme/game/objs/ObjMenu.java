package self.frota.guilherme.game.objs;

import android.graphics.Color;

import self.frota.guilherme.game.FallingObj;
import self.frota.guilherme.game.states.MenuState;
import self.frota.guilherme.particulas.CriadorDeParticulas;
import self.frota.guilherme.principal.Game;
import self.frota.guilherme.tools.Util;

/**
 * Created by Frota on 25/09/2017.
 *
 */

public class ObjMenu extends FallingObj {

    private static int index = 0;

    private boolean temParticula = false;

    private CriadorDeParticulas particulas;

    public ObjMenu(MenuState menuState, float x, double speed) {
        super(menuState, x, (float)-Util.inDP((Game.getTela().getWidth()/Game.SCALE)/ Pedra.PEDRA_SCALE + 3), (float) Util.inDP((Game.getTela().getWidth()/Game.SCALE)/Pedra.PEDRA_SCALE), speed, Color.CYAN);
        index++;

        if (index % 20 == 0) {
            temParticula = true;

            particulas = new CriadorDeParticulas(gameState, this, x, y, width, height, Util.inDP(3), Util.inDP(3), Color.GREEN, 30);
            particulas.setGravidadeRate(0);
            particulas.setAngulo(90, 20);
            particulas.setSpeedVar(0.133, 0.02);
            particulas.addColor(Color.rgb(50, 255, 50));
            particulas.setAlphaDelay(20);
            particulas.setAlphaVar(20, 10);
            particulas.setProduzindo(true);
        }
    }

    @Override
    protected void fimTelaAcao() {

    }

    @Override
    protected void colisaoAcao() {

    }

    @Override
    protected void updateObj() {
        if (temParticula) particulas.update();
    }

    @Override
    protected void checaColisaoPlayer() {
        //nada
    }
}
