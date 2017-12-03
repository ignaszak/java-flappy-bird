package net.ignaszak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.ignaszak.game.sprites.Bird;
import net.ignaszak.game.sprites.Font;
import net.ignaszak.game.sprites.Scoring;
import net.ignaszak.game.sprites.Tube;
import net.ignaszak.game.FlappyBird;

/**
 * Created by tomek on 04.05.17.
 */
public class PlayState extends State {
    private static final int TUBE_SPACING = 150;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;
    private Bird bird;
    private Texture background;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Array<Tube> tubes;
    private Sound crashSound;
    private Scoring scoring;
    private BitmapFont scoreFont;
    private BitmapFont maxScoreFont;
    private boolean stop;
    private float speedRatio = 1.0f;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        background = new Texture("bg.png");
        cam.setToOrtho(false, FlappyBird.WIDTH/2, FlappyBird.HEIGHT/2);
        tubes = new Array<Tube>();
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth/2) + ground.getWidth(), GROUND_Y_OFFSET);
        crashSound = Gdx.audio.newSound(Gdx.files.internal("crash.wav"));
        scoring = new Scoring();
        Font font = new Font();
        scoreFont = font.getBitmap();
        font.setColor(Color.GOLD);
        font.setSize(Font.SIZE_BIG);
        maxScoreFont = font.getBitmap();
        stop = false;
        for (int i = 1; i <= TUBE_COUNT; ++ i) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        dt = dt * speedRatio;

        if (! stop) bird.update(dt);
        cam.position.x = bird.getPosition().x + 100;

        for (int i = 0; i < tubes.size; ++ i) {
            Tube tube = tubes.get(i);
            if (cam.position.x - (cam.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING)*TUBE_COUNT));
            }
            if (tube.collides(bird.getBounds())) stop();
            if(tube.score(bird.getBounds()) && ! tube.isScored()) {
                scoring.score();
                tube.markScored();
                if (scoring.getScore() % 30 == 0) speedRatio += 0.1f;
            }
        }
        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) stop();
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, cam.position.x - (cam.viewportWidth/2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube: tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        scoreFont.draw(sb, "Score: " + Integer.toString(scoring.getScore()), cam.position.x - cam.viewportWidth/2 + 10, 20);

        if (scoring.isNewRecord() && ! scoring.isRecordMessageRead()) {
            scoring.playRecordSound();
            String maxScoreText = "RECORD";
            GlyphLayout maxScoreGlyphLayout = new GlyphLayout(maxScoreFont, maxScoreText);
            maxScoreFont.draw(sb, maxScoreText, cam.position.x - maxScoreGlyphLayout.width/2, cam.viewportHeight - 10);
        }

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
        ground.dispose();
        scoring.dispose();
        scoreFont.dispose();
        maxScoreFont.dispose();
        for (Tube tube: tubes) {
            tube.dispose();
        }
        System.out.println("Play State Disposed");
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) bird.jump();
    }

    private void updateGround() {
        if (cam.position.x - (cam.viewportWidth/2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if (cam.position.x - (cam.viewportWidth/2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    private void stop() {
        if (! stop) crashSound.play();
        stop = true;
        gsm.set(new InfoState(gsm, scoring));
    }
}
