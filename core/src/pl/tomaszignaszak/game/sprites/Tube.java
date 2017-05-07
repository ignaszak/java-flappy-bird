package pl.tomaszignaszak.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by tomek on 05.05.17.
 */

public class Tube {
    public static final int TUBE_WIDTH = 52;
    private static final int FLUCTUATION = 130;
    private static final int TUBE_GAP = 100;
    private static final int LOWEST_OPENING = 120;
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBottomTube;
    private Rectangle boundsTop, boundsBottom, boundsIn;
    private Random rand;
    private boolean isScored;

    public Tube(float x) {
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();
        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBottomTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBottom = new Rectangle(posBottomTube.x, posBottomTube.y, bottomTube.getWidth(), bottomTube.getHeight());
        boundsIn = new Rectangle(posBottomTube.x + topTube.getWidth(), posBottomTube.y + bottomTube.getHeight(), 50, TUBE_GAP);
        isScored = false;
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBottomTube() {
        return posBottomTube;
    }

    public void reposition(float x) {
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBottomTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBottom.setPosition(posBottomTube.x, posBottomTube.y);
        boundsIn.setPosition(x + topTube.getWidth(), posBottomTube.y + boundsBottom.getHeight());
        isScored = false;
    }

    public boolean score(Rectangle player) {
        return player.overlaps(boundsIn);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBottom);
    }

    public void markScored() {
        isScored = true;
    }

    public boolean isScored() {
        return isScored;
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }
}
