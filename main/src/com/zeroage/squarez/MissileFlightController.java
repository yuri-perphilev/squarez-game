package com.zeroage.squarez;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.round;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MissileFlightController extends BaseController
{
    public static final int TRAIL_LENGTH = 3;
    public static final int SPARKLES_PER_UNIT = 10;
    public static final float SPARKLES_SIZE = 0.5f;
    public static final int LIFETIME = 3; // seconds
    public static final float ROCKET_SPEED = 15.0f;
    public static final float TRAIL_VANISH_SPEED = 10.0f;
    public static final float SPARKLE_MAX_SPEED = 3f;

    private Vector2[][] sparkles;
    private Vector2[][] speed;

    private final TextureRegion sparkleTexture;

    private float time = 0;

    public MissileFlightController(GameController gameController, int fromX, int fromY, int toX, int toY, int dX, int dY)
    {
        super(gameController);

        Rectangle r = getGameController().getBoardRectangle();

        // Missile from 6, 13 to 0, 13; dx: -1, dy: 0
        sparkleTexture = gameController.getTexture(TextureType.SPARKLE);

        int len = max(abs(fromX - toX) + 1, abs(fromY - toY) + 1);
        sparkles = new Vector2[len][SPARKLES_PER_UNIT];
        speed = new Vector2[len][SPARKLES_PER_UNIT];

        int k = (dX + dY) > 0 ? 1 : 0;
        int k1 = (dX + dY) > 0 ? 0 : 1;

        for (int i = 0; i < sparkles.length; i++) {
            Vector2[] sparkle = sparkles[i];
            Vector2[] spd = speed[i];
            float sx = fromX + i * dX;
            float sy = fromY + i * dY;
            for (int j = 0; j < sparkle.length; j++) {
                float x1 = sx + random((1 + abs(dX) * k - SPARKLES_SIZE));
                float y1 = sy + random((1 + abs(dY) * k - SPARKLES_SIZE));
                sparkle[j] = new Vector2(r.x + x1, r.y + (r.height - y1) - SPARKLES_SIZE);
                spd[j] = new Vector2(random(SPARKLE_MAX_SPEED) * dX, random(SPARKLE_MAX_SPEED) * dY);
            }
            k = (i == sparkles.length - 2) ? k1 : 1;
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        int n = min(round(ROCKET_SPEED * time), sparkles.length);
        int vanishIndex = TRAIL_LENGTH - round(TRAIL_VANISH_SPEED * time);

        for (int i = 0; i < n; i++) {
            Vector2[] sparkle = sparkles[i];

            int m = min(i + vanishIndex, 0);
            for (int i1 = 0; i1 < sparkle.length + m; i1++) {
                Vector2 v = sparkle[i1];
                batch.draw(sparkleTexture, v.x, v.y, SPARKLES_SIZE, SPARKLES_SIZE);
            }
        }
    }

    @Override
    public boolean update(float delta)
    {
        time += delta;

        for (int i = 0; i < sparkles.length; i++) {
            Vector2[] sparkle = sparkles[i];
            Vector2[] spd = speed[i];
            for (int j = 0; j < sparkle.length; j++) {
                Vector2 v = sparkle[j];
                v.sub(spd[j].x * delta, spd[j].y * delta);
            }
        }

        return time >= LIFETIME;
    }

    private class Sparkle {
        Vector2 pos;
        Vector2 speed;
    }
}
