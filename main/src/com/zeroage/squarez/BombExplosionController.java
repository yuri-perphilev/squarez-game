package com.zeroage.squarez;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

public class BombExplosionController extends BaseController
{
    public static final int SPARKLES_PER_UNIT = 10;
    public static final float SPARKLES_SIZE = 0.5f;

    public static final float PHASE_1_LENGTH = 0.1f; // seconds
    public static final float PHASE_2_LENGTH = 1; // seconds

    public static final float LIFETIME = PHASE_1_LENGTH + PHASE_2_LENGTH; // seconds

    private final TextureRegion sparkleTexture;

    private List<Sparkle> sparkles;
    private int index = 0;
    private float time = 0;

    private int sparklesCount;

    public BombExplosionController(GameController gameController, int x, int y, Set<int[]> blocksToExplode)
    {
        super(gameController);

        sparkleTexture = gameController.getTexture(TextureType.SPARKLE);
        Rectangle r = getGameController().getBoardRectangle();

        sparkles = new ArrayList<Sparkle>(SPARKLES_PER_UNIT * blocksToExplode.size());

        int idx = 0;

        for (int[] xy : blocksToExplode) {
            for (int i = 0; i < SPARKLES_PER_UNIT; i++) {
                float px = x + random((1 - SPARKLES_SIZE));
                float py = y + random((1 - SPARKLES_SIZE));
                Vector2 pos = new Vector2(r.x + px, r.y + (r.height - py) - SPARKLES_SIZE);

                float vx = random(1f);
                float vy = random(1f);

                Vector2 vel = new Vector2(xy[0] - x + vx, xy[1] - y + vy);

                sparkles.add(new Sparkle(pos, vel, -random(0.1f, 0.5f)));
            }
        }

        sparklesCount = sparkles.size();

    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        Rectangle scissors = renderUtils.getScissors(getGameController().getBoardRectangle());
        ScissorStack.pushScissors(scissors);

        for (int i = 0; i < Math.min(index, sparkles.size()); i++) {
            Sparkle s = sparkles.get(i);
            batch.draw(sparkleTexture, s.pos.x, s.pos.y, SPARKLES_SIZE, SPARKLES_SIZE);
        }

        batch.flush();
        ScissorStack.popScissors();
    }

    @Override
    public boolean update(float delta)
    {
        time += delta;
        if (time < PHASE_1_LENGTH) {
            index = MathUtils.round(time * sparkles.size() / PHASE_1_LENGTH);
        }
        else {
            for (Sparkle s : sparkles) {
                s.pos.x = s.pos.x + s.vel.x * delta;
                s.pos.y = s.pos.y + s.vel.y * delta;
                s.vel.x = s.vel.x - s.a * delta;
                s.vel.y = s.vel.y - s.a * delta;
            }

            int count = MathUtils.round(sparklesCount * delta / PHASE_2_LENGTH);
            if (sparkles.size() >= count) {
                for (int i = 0; i < count; i++) {
                    sparkles.remove(random(sparkles.size() - 1));
                }
            }
        }
        return time >= LIFETIME;
    }

    private class Sparkle {
        private Sparkle(Vector2 pos, Vector2 vel, float a)
        {
            this.pos = pos;
            this.vel = vel;
            this.a = a;
        }

        public Vector2 pos;
        public Vector2 vel;
        public float a;
    }
}
