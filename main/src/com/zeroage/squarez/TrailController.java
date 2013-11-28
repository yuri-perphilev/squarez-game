package com.zeroage.squarez;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class TrailController extends BaseController
{
    private Vector3[] trail = new Vector3[15];
    private int trailPointer = 0;
    private float trailReduceCounter = 0f;


    protected TrailController(GameController gameController)
    {
        super(gameController);

        Rectangle r = getGameController().getBoardRectangle();

        Random random = new Random();

        for (int i = 0; i < trail.length; i++) {
            trail[i] = new Vector3(random.nextInt(Math.round(r.width)), random.nextInt(Math.round(r.height)), 0);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, float delta)
    {
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        int ptr = trailPointer;

        float a = .75f;
        float d = (a - .1f) / trail.length;

        for (int i = 0; i < trail.length; i++) {
            ptr--;
            if (ptr < 0) {
                ptr = trail.length - 1;
            }

            Vector3 v = trail[ptr];
            if (v != null) {
                renderer.setColor(1, 1, 1, a);
                renderer.circle(v.x, v.y, .5f, 10);
            }
            a -= d;
        }

        renderer.end();
    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {

    }

    @Override
    public boolean update(float delta)
    {
        trailReduceCounter += delta;

        if (trailReduceCounter > .02) {
            trailReduceCounter = 0;
            trail[trailPointer] = null;

            trailPointer++;
            if (trailPointer >= trail.length) {
                trailPointer = 0;
            }
        }
        return false;
    }

    @Override
    public void touchDragged(float x, float y, int pointer)
    {
        trail[trailPointer] = new Vector3(x, y, 0);
        trailPointer++;
        if (trailPointer >= trail.length) {
            trailPointer = 0;
        }
    }
}
