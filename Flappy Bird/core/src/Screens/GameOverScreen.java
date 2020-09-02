package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.games.game.FlappyBirdGame;

public class GameOverScreen implements Screen {
    private final FlappyBirdGame game;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture bg,gameOver;
    private int score,gameState,high_score,flag=0;
    GameOverScreen(final FlappyBirdGame game,int score) {
        this.game=game;
        this.score=score;
        Preferences prefs=Gdx.app.getPreferences("flappy_db");
        this.high_score=prefs.getInteger("high_score",0);
        if(score>high_score){
            flag=1;
            prefs.putInteger("high_score",score);
            prefs.flush();
        }
        batch=new SpriteBatch();
        font=new BitmapFont(Gdx.files.internal("kenpixel.fnt"));
        bg=new Texture("bg.png");
        gameOver=new Texture("gameOver.jpg");
        gameState=0;
    }
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(gameState==0) {
            batch.begin();
            batch.draw(gameOver, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
            font.setColor(Color.BROWN);
            font.getData().setScale(2.5f);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    gameState = 1;
                }
            }, 1.2f);
        }
        else {
            batch.begin();
            batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            GlyphLayout glyphLayout = new GlyphLayout();
            glyphLayout.setText(font, "Your Score: " + String.valueOf(score));
            if(flag==0){
                font.draw(batch, "High Score: " + String.valueOf(high_score), Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2, Gdx.graphics.getHeight() - glyphLayout.height - 200);
            }
            else{
                GlyphLayout glyphLayout1=new GlyphLayout();
                glyphLayout1.setText(font,"New High Score !!!");
                font.draw(batch,"New High Score !!!",Gdx.graphics.getWidth() / 2f - glyphLayout1.width / 2f,Gdx.graphics.getHeight() - glyphLayout.height - 200);
            }
            font.draw(batch, "Your Score: " + String.valueOf(score), Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2, Gdx.graphics.getHeight() / 2f - glyphLayout.height / 2);
            batch.end();
            if (Gdx.input.justTouched()) {
                game.setScreen(new GameScreen(game,0));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        gameOver.dispose();
        bg.dispose();
    }
}
