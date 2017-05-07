package pl.tomaszignaszak.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by tomek on 05.05.17.
 */

public class Scoring {
    private Sound sound;
    private int score;

    public Scoring() {
        sound = Gdx.audio.newSound(Gdx.files.internal("score.wav"));
    }

    public void score() {
        sound.play(0.3f);
        ++ score;
    }

    public int getScore() {
        return score;
    }

    public void dispose() {
        sound.dispose();
    }
}
