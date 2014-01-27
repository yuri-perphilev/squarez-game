package com.zeroage.squarez;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.zeroage.squarez.model.BombCallback;
import com.zeroage.squarez.model.PositionedBlock;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class BombExplosionController extends BaseController implements BombCallback
{
    public static final int SPARKLES_PER_UNIT = 10;
    public static final float SPARKLES_SIZE = 0.5f;

    public static final float PHASE_1_LENGTH = 0.1f; // seconds
    public static final float PHASE_2_LENGTH = 1; // seconds

    public static final float LIFETIME = PHASE_1_LENGTH + PHASE_2_LENGTH; // seconds

    private final TextureRegion sparkleTexture;

    private List<Sparkle> sparkles;
    private int sparklesCount;
    private int index = 0;
    private float time = 0;
    private Vector2 epicentre;

    private List<PositionedBlock> blocksToBlast;
    private boolean exploding = false;
    private float delay;

    public BombExplosionController(GameController gameController, int x, int y)
    {
        super(gameController);

        sparkleTexture = gameController.getTexture(TextureType.SPARKLE);
        epicentre = new Vector2(x, y);
    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        if (!exploding) {
            return;
        }

        Rectangle scissors = renderUtils.getScissors(getGameController().getBoardRectangle());
        ScissorStack.pushScissors(scissors);

        final Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, 1 - time / LIFETIME);
        for (PositionedBlock block : blocksToBlast) {
            getGameController().drawBlock(batch, block.getBlock(), block.getX(), block.getY());
        }
        batch.setColor(color);

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
        if (!exploding) {
            return false;
        }

        if (delay > 0) {
            delay -= delta;
            return false;
        }

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

    @Override
    public float getBlastTime(int x, int y)
    {
        return PHASE_1_LENGTH + new Vector2(x - epicentre.x, y - epicentre.y).len() ;
    }

    @Override
    public void explode(List<PositionedBlock> blocksToExplode, float delay)
    {
        this.blocksToBlast = blocksToExplode;

        sparkles = new ArrayList<Sparkle>(SPARKLES_PER_UNIT * blocksToExplode.size());

        for (PositionedBlock block : blocksToExplode) {
            for (int i = 0; i < SPARKLES_PER_UNIT; i++) {
                float px = epicentre.x + random((1 - SPARKLES_SIZE));
                float py = epicentre.y + random((1 - SPARKLES_SIZE));
                Vector2 pos = getGameController().toGameCoords(px, py, SPARKLES_SIZE);

                Vector2 vel = new Vector2(block.getX() - epicentre.x + random(1f), block.getY() - epicentre.y + random(1f));

                sparkles.add(new Sparkle(pos, vel, random(0.2f, 0.5f)));
            }
        }

        sparklesCount = sparkles.size();
        exploding = true;
        this.delay = delay;
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
