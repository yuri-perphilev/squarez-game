package com.zeroage.squarez;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

public class BombExplosionController extends BaseController
{
    public static final int SPARKLES_PER_UNIT = 10;
    public static final float SPARKLES_SIZE = 0.5f;
    public static final int LIFETIME = 3; // seconds

    private final TextureRegion sparkleTexture;

    private Vector2[] sparkles;
    private float time = 0;

    public BombExplosionController(GameController gameController, Set<int[]> blocksToExplode)
    {
        super(gameController);

        sparkleTexture = gameController.getTexture(TextureType.SPARKLE);
        Rectangle r = getGameController().getBoardRectangle();

        sparkles = new Vector2[SPARKLES_PER_UNIT * blocksToExplode.size()];

        int idx = 0;

        for (int[] xy : blocksToExplode) {
            for (int i = 0; i < SPARKLES_PER_UNIT; i++) {
                float x1 = xy[0] + random((1 - SPARKLES_SIZE));
                float y1 = xy[1] + random((1 - SPARKLES_SIZE));

                sparkles[idx++] = new Vector2(r.x + x1, r.y + (r.height - y1) - SPARKLES_SIZE);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        for (Vector2 v: sparkles) {
            batch.draw(sparkleTexture, v.x, v.y, SPARKLES_SIZE, SPARKLES_SIZE);
        }
    }

    @Override
    public boolean update(float delta)
    {
        time += delta;
        return time >= LIFETIME;
    }
}
