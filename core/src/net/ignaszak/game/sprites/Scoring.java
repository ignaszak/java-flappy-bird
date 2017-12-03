package net.ignaszak.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by tomek on 05.05.17.
 */
public class Scoring {
    private Sound scoreSound;
    private Sound recordSound;
    private int score;
    private int step = 5;
    private int maxScore = 0;
    private Pref pref;
    private boolean isRecordSoundPlayed = false;

    public Scoring() {
        scoreSound = Gdx.audio.newSound(Gdx.files.internal("score.wav"));
        recordSound = Gdx.audio.newSound(Gdx.files.internal("record.wav"));
        pref = new Pref();
        maxScore = pref.getMaxScore();
    }

    public void score() {
        scoreSound.play(0.3f);
        score += step;
    }

    public boolean isNewRecord() {
        return maxScore > 0 && score > maxScore;
    }

    public boolean isRecordMessageRead() {
        return score > maxScore + step;
    }

    public int getScore() {
        return score;
    }

    public void dispose() {
        scoreSound.dispose();
        recordSound.dispose();
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void saveNewRecord() {
        if (score > maxScore) {
            maxScore = score;
            pref.setMaxScore(score);
        }
    }

    public void playRecordSound() {
        if (! isRecordSoundPlayed) recordSound.play();
        isRecordSoundPlayed = true;
    }

    public int getMaxScore() {
        return maxScore;
    }
}
