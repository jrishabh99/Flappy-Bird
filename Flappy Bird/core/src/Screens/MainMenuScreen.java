package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.games.game.FlappyBirdGame;


public class MainMenuScreen implements Screen {
    private final FlappyBirdGame game;
    private Texture touch_to_jump,startScreen;
    private SpriteBatch batch;
    private Music backmusic;
    BitmapFont font;

    private int flag;

    public MainMenuScreen(final FlappyBirdGame game) {

        this.game=game;
        font=new BitmapFont(Gdx.files.internal("kenpixel.fnt"));
        font.setColor(Color.CYAN);
        font.getData().setScale(1.5f);
        backmusic=Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        backmusic.setLooping(true);
        backmusic.play();
        startScreen=new Texture("FlappyBirdStartScreen.jpg");
        flag=0;
        touch_to_jump=new Texture("touch_to_start.jpg");
        batch=new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(flag==0){
            GlyphLayout glyphLayout=new GlyphLayout();
            glyphLayout.setText(font,"Created by Rishabh    ");
            batch.begin();
            batch.draw(startScreen,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            font.draw(batch,"Created by Rishabh",Gdx.graphics.getWidth()-glyphLayout.width,glyphLayout.height +40);
            batch.end();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    flag=1;
                }
            },2f);
        }
        if(flag==1) {
            batch.begin();
            batch.draw(touch_to_jump, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
            if (Gdx.input.justTouched())
                game.setScreen(new GameScreen(game, 1));
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
        batch.dispose();
        backmusic.dispose();
        touch_to_jump.dispose();
        startScreen.dispose();
    }
}
