package com.zeroage.squarez;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.zeroage.squarez.model.Board;
import com.zeroage.squarez.model.MissileCallback;
import com.zeroage.squarez.model.PositionedBlock;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.round;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MissileFlightController extends BaseController implements MissileCallback
{
    public static final int TRAIL_LENGTH = 3;
    public static final int SPARKLES_PER_UNIT = 10;
    public static final float SPARKLES_SIZE = 0.5f;
    public static final int LIFETIME = 3; // seconds
    public static final float ROCKET_SPEED = 15.0f;
    public static final float ROCKET_SIZE = 1;
    public static final float TRAIL_VANISH_SPEED = 10.0f;
    public static final float SPARKLE_MAX_SPEED = 3f;

    private Vector2[][] sparkles;
    private Vector2[][] speed;
    private Vector2 rocketPos;
    private Vector2 rocketSpeed;
    private Vector2 d;
    private Vector2 from;

    private final TextureRegion sparkleTexture;
    private final TextureRegion rocketTexture;

    private float time = 0;
    private float delay = 0;
    private List<PositionedBlock> blocksToHit;

    private boolean fired = false;

    public MissileFlightController(GameController gameController, int fromX, int fromY, int dX, int dY)
    {
        super(gameController);

        this.from = new Vector2(fromX, fromY);

        sparkleTexture = gameController.getTexture(TextureType.SPARKLE);
        rocketTexture = gameController.getTexture(TextureType.ROCKET);

        rocketPos = getGameController().toGameCoords(fromX, fromY, ROCKET_SIZE);
        rocketSpeed = new Vector2(ROCKET_SPEED * dX, -ROCKET_SPEED * dY);
        d = new Vector2(dX,  -dY);

        int len = calcVapourTrailLength(getGameController().getBoard(), from, d);
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
                sparkle[j] = getGameController().toGameCoords(x1, y1, SPARKLES_SIZE);
                spd[j] = new Vector2(random(SPARKLE_MAX_SPEED) * dX, random(SPARKLE_MAX_SPEED) * dY);
            }
            k = (i == sparkles.length - 2) ? k1 : 1;
        }
    }

    private int calcVapourTrailLength(Board board, Vector2 from, Vector2 d)
    {
        return Math.round(d.x > 0 ? board.getWidth() - from.x :
                          d.x < 0 ? from.x + 1 :
                          d.y < 0 ? board.getHeight() - from.y :
                          d.y > 0 ? from.y + 1 :
                          0);
    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        for (PositionedBlock block : blocksToHit) {
            Vector2 pos = getGameController().toGameCoords(block.getX(), block.getY(), 1);
            if ((pos.x - rocketPos.x) * d.x >= 0 && (pos.y - rocketPos.y) * d.y >= 0) {
                getGameController().drawBlock(batch, block.getBlock(), block.getX(), block.getY());
            }
        }

        if (!fired || delay > 0) {
            return;
        }

        Rectangle r = getGameController().getBoardRectangle();
        Rectangle scissors = renderUtils.getScissors(r);
        ScissorStack.pushScissors(scissors);

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

        batch.draw(rocketTexture, rocketPos.x, rocketPos.y, ROCKET_SIZE, ROCKET_SIZE);

        batch.flush();
        ScissorStack.popScissors();
    }

    @Override
    public boolean update(float delta)
    {
        if (!fired) {
            return false;
        }

        if (delay > 0) {
            delay -= delta;
            return false;
        }

        time += delta;

        rocketPos.add(rocketSpeed.x * delta, rocketSpeed.y * delta);

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

    @Override
    public float getHitTime(int x, int y)
    {
        return max(abs(x - from.x), abs(y - from.y)) / ROCKET_SPEED ;
    }

    @Override
    public void fire(List<PositionedBlock> blocksToHit, float delay)
    {
        this.blocksToHit = blocksToHit;
        this.fired = true;
        this.delay = delay;
    }

    private class Sparkle {
        Vector2 pos;
        Vector2 speed;
    }
}
