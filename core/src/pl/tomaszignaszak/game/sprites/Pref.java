package pl.tomaszignaszak.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by tomek on 07.09.17.
 */
public class Pref {

    private final static String PREF_NAME = "FLAPPY_BIRD";
    private static final String MAX_SCORE = "MAX_SCORE";

    private Preferences preferences;

    public Preferences get() {
        if (preferences == null) preferences = Gdx.app.getPreferences(PREF_NAME);
        return preferences;
    }

    public void setMaxScore(int score) {
        Preferences preferences = get();
        preferences.putInteger(MAX_SCORE, score);
        preferences.flush();
    }

    public int getMaxScore() {
        Preferences preferences = get();
        return preferences.getInteger(MAX_SCORE);
    }
}
