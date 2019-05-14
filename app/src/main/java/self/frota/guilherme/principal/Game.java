package self.frota.guilherme.principal;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import self.frota.guilherme.game.GameState;
import self.frota.guilherme.game.states.MenuState;
import self.frota.guilherme.tools.Util;


/**
 * Created by Frota on 05/08/2017.
 *
 */

public class Game extends Activity {

    public static GameThread gameThread;
    private  RelativeLayout gameBox;
    private static GameSurface tela;
    private static GameState gameState;
    private static SharedPreferences dadosGame;
    private static Vibrator gameVibrator;

    private static Paint tintaPai;
    private static Paint tintaFPS;

    private static boolean isInit = false;

    private static int novoJogoAdCounter = 2;

    public static float SCALE;

    public static final String VERSAO = "1.1";
    public static boolean DEBUG = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //disable rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        SCALE = getResources().getDisplayMetrics().density;

        dadosGame = getPreferences(Context.MODE_PRIVATE);
        gameVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        tela = new GameSurface(this);

        gameBox = new RelativeLayout(this);
        gameBox.setBackgroundColor(Color.BLACK);

        gameBox.addView(tela);

        setContentView(gameBox);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameState != null) gameState.getListenerManager().restart();
        gameThread = new GameThread(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameThread.pause();
    }

    public void init() {
        if (Game.isInit()) {
            return;
        }

        setGameState(new MenuState());

        tintaFPS = new Paint();
        tintaFPS.setColor(Color.WHITE);
        tintaFPS.setTextSize(Util.inDP(22));
        tintaFPS.setAntiAlias(true);
        tintaFPS.setTypeface(Util.FONT_PADRAO);

        isInit = true;
    }

    public void updateGame() {
        if (gameState != null) gameState.update();
    }

    public void drawGame(Canvas canvas) {
        if (!Game.isInit()) {
            return;
        }

        setupTintaPai();
        if (gameState != null) gameState.draw(canvas, tintaPai);
        drawFPS(canvas);
    }

    private void setupTintaPai() {
        tintaPai = new Paint();
        tintaPai.setAntiAlias(true);
        tintaPai.setTypeface(Util.FONT_PADRAO);
    }

    public synchronized void setGameState(GameState novo) {
        if (gameState != null) {
            gameState.close();
        }

        gameState = novo;
        gameState.init(this);
    }

    private void drawFPS(Canvas canvas) {
        canvas.drawText("FPS: " + GameThread.FPS_NOW,  Util.inDP(4),  Util.inDP(13), tintaFPS);
    }

    public static Paint getTintaFPS() {
        return tintaFPS;
    }

    public static boolean isInit() { return isInit; }

    public RelativeLayout getGameBox() { return gameBox; }

    public static GameSurface getTela() { return tela; }

    public static SharedPreferences getDados() { return dadosGame; }

    public static Vibrator getVibrator() { return gameVibrator; }

    public static GameState getGameState() {
        return gameState;
    }



}
