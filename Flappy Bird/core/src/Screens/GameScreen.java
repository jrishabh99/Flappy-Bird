package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.games.game.FlappyBirdGame;

import java.util.Random;

public class GameScreen implements Screen {
    private int score=0;
    private int scoringTube=0;
    private Rectangle[] topTubeRectangles;
    private Rectangle[]bottomTubeRectangles;
    private Texture bg;
    private Texture birds[];
    private SpriteBatch batch;
    private Texture topTube,bottomTube,dark_background;
    private BitmapFont font;
    private final FlappyBirdGame game;
    private int flapState=0;
    private float gravity=1.7f;
    private float birdY;
    private float upward_push=-24;
    private float velocity=0;
    private int gameState=0;
    private float gap=400;
    private float maxTubeOffset;
    private float tubeVelocity=5.5f;
    private Random random;
    private int numberoftubes=5;
    private float tubex[]=new float[numberoftubes];
    private float tubeOffset[]=new float[numberoftubes];
    private float distancebetweentubes;
    private float pipeoffset=200;
    private Circle birdcircle;
    private Music hit,pointsound;
    private Texture ground[];
    private float groundx[];
    private int jump,flag;


    GameScreen(final FlappyBirdGame game,int jump){
        this.game=game;
        this.jump=jump;
        gameState=jump;
        flag=-1;
        score=0;
        dark_background=new Texture("dark_theme.jpg");
        font=new BitmapFont(Gdx.files.internal("kenpixel.fnt"));
        font.setColor(Color.BROWN);
        batch=new SpriteBatch();
        hit=Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
        pointsound=Gdx.audio.newMusic(Gdx.files.internal("pointsound.mp3"));
        bg=new Texture("bg.png");
        birdcircle=new Circle();
        ground=new Texture[2];
        ground[0]=new Texture("ground.png");
        ground[1]=new Texture("ground.png");
        topTube=new Texture("toptube.png");
        bottomTube=new Texture("bottomtube.png");
        maxTubeOffset=Gdx.graphics.getHeight()/2f - gap/2 - pipeoffset;
        random=new Random();
        birds=new Texture[2];
        topTubeRectangles=new Rectangle[numberoftubes];
        bottomTubeRectangles=new Rectangle[numberoftubes];
        birds[0]=new Texture("bird.png");
        birds[1]=new Texture("bird2.png");
        groundx=new float[2];
        groundx[0]=0;
        groundx[1]=Gdx.graphics.getWidth();
        distancebetweentubes=Gdx.graphics.getWidth()* 3f/4 + 40;
        birdY=Gdx.graphics.getHeight() / 2f - birds[1].getHeight() / 2f +300;
        batch.begin();
        batch.draw(bg,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        for(int i=0;i<numberoftubes;i++){
            topTubeRectangles[i]=new Rectangle();
            bottomTubeRectangles[i]=new Rectangle();
            tubex[i]=Gdx.graphics.getWidth()/2f - topTube.getWidth() / 2f  + Gdx.graphics.getWidth()+ i*distancebetweentubes;
        }
        tubeOffset[0]=0;

        tubeOffset[1]=180;

        tubeOffset[2]=-50;

        tubeOffset[3]=-150;

        tubeOffset[4]=0;


    }
    @Override
    public void show() {

    }
    @Override
    public void render(float delta) {
        batch.begin();
        if(score ==0 || flag==-1)
            batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        else
            batch.draw(dark_background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        if (gameState == 1) {
            for(int i=0;i<2;i++) {
                if(groundx[i]<-Gdx.graphics.getWidth())
                    groundx[i]+=2*Gdx.graphics.getWidth();
                else
                    groundx[i]-=tubeVelocity;
                batch.draw(ground[i], groundx[i], 0, Gdx.graphics.getWidth(), ground[0].getHeight());
            }
            if (tubex[scoringTube] < Gdx.graphics.getWidth() / 2 - topTube.getWidth()) {
                score += 2;
                if(score%16==0)
                    flag*=-1;
                pointsound.play();

                if (scoringTube < numberoftubes - 1) {
                    scoringTube++;
                } else
                    scoringTube = 0;
            }
            if(jump==1){
                velocity=upward_push;
                jump=0;
            }
            if (Gdx.input.justTouched()) {
                velocity = upward_push;
            }
            for (int i = 0; i < numberoftubes; i++) {
                if (tubex[i] < -topTube.getWidth()) {
                    tubex[i] += numberoftubes * distancebetweentubes;
                    tubeOffset[i] = (random.nextFloat() - .5f) * (Gdx.graphics.getHeight() - gap - pipeoffset * 2);
                } else {
                    tubex[i] = tubex[i] - tubeVelocity;
                }
                batch.draw(topTube, tubex[i], Gdx.graphics.getHeight() / 2f + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubex[i], Gdx.graphics.getHeight() / 2f - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangles[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2f + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2f - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }
            velocity += gravity;
            birdY -= velocity;
            if (birdY < ground[0].getHeight() || birdY> Gdx.graphics.getHeight()-birds[0].getHeight()) {
                hit.play();
                game.setScreen(new GameOverScreen(game,score));
            }
        } else {
            if (Gdx.input.justTouched()) {
                gameState = 1;
                velocity=upward_push;
            }

        }
        flapState = ((flapState == 0) ? 1 : 0);
        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2f - birds[0].getHeight() / 2f,
                birdY);
        font.getData().setScale(2f);
        font.draw(batch, "Score :" + score, Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 100);
        batch.end();
        if (score == 16) {
            tubeVelocity = 6;
            gap=395;
        }
        if(score==24)
            tubeVelocity=6.5f;


        birdcircle.set(Gdx.graphics.getWidth() / 2f, birdY + (birds[flapState].getHeight() / 2f), birds[flapState].getWidth() / 2f);

        for (int i = 0; i < numberoftubes; i++) {
            if (Intersector.overlaps(birdcircle, bottomTubeRectangles[i]) || Intersector.overlaps(birdcircle, topTubeRectangles[i])) {
                hit.play();
                game.setScreen(new GameOverScreen(game,score));
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
        birds[0].dispose();
        birds[1].dispose();
        topTube.dispose();
        bottomTube.dispose();
        pointsound.dispose();
        bg.dispose();
        hit.dispose();
        batch.dispose();
        font.dispose();
    }
}
