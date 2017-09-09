package pl.tomaszignaszak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pl.tomaszignaszak.game.FlappyBird;
import pl.tomaszignaszak.game.sprites.Font;
import pl.tomaszignaszak.game.sprites.Scoring;

/**
 * Created by tomek on 30.08.17.
 */

public class InfoState extends State {

    private Scoring scoring;
    private Texture background;
    private BitmapFont scoreFont;
    private BitmapFont maxScoreFont;

    public InfoState(GameStateManager gsm, Scoring scoring) {
        super(gsm);
        this.scoring = scoring;
        cam.setToOrtho(false, FlappyBird.WIDTH/2, FlappyBird.HEIGHT/2);
        background = new Texture("bg.png");
        Font font = new Font();
        scoreFont = font.getBitmap();
        font.setColor(Color.GOLD);
        font.setSize(Font.SIZE_BIG);
        maxScoreFont = font.getBitmap();
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        scoring.saveNewRecord();

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0);
        String scoreText = "Score: " + Integer.toString(scoring.getScore());
        scoreFont.draw(sb, scoreText, cam.position.x - getStringWidth(scoreFont, scoreText)/2, cam.position.y);

        String maxScoreText = "Record: " + Integer.toString(scoring.getMaxScore());
        this.maxScoreFont.draw(sb, maxScoreText, cam.position.x - getStringWidth(maxScoreFont, maxScoreText)/2, cam.position.y + 50);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        scoring.dispose();
        scoreFont.dispose();
        maxScoreFont.dispose();
        System.out.println("Info State Disposed");
    }

    private float getStringWidth(BitmapFont font, String str) {
        GlyphLayout glyphLayout = new GlyphLayout(font, str);
        return glyphLayout.width;
    }
}
