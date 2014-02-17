package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.zeroage.squarez.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameController implements Controller
{
    public static final int FIGURE_LIFETIME = 10;

    private List<Controller> controllers = new CopyOnWriteArrayList<Controller>();

    private Board board;
    private Rectangle boardRectangle;
    private Rectangle sceneRectangle;

    private FrameController frame;
    private FigureController figure;
    private TimerController timer;
    private TrailController trail;
    private BoardController brd;

    private TextureAtlas atlas;
    private Map<BlockTexture, TextureRegion> blockTextures = new HashMap<BlockTexture, TextureRegion>(32);
    private Map<TextureType, TextureRegion> textures = new HashMap<TextureType, TextureRegion>(32);

    public GameController(float viewportWidth, float viewportHeight, TextureAtlas atlas)
    {
        int boardWidth = Math.round(viewportWidth) - 1; // half of a unit from the left and from the right
        int boardHeight = Math.round(viewportHeight) - 7; // 2 (score line) + 3 (pocket) + 2 (spacers)

        boardRectangle = new Rectangle(0.5f, 2.5f, boardWidth, boardHeight);
        sceneRectangle = new Rectangle(0, 0, viewportWidth, viewportHeight);

        board = new Board3(boardWidth, boardHeight, new MainGameCallbacks());

        fillBoardWithRandomBlocks(boardWidth, boardHeight);

        this.atlas = atlas;
        loadTextures();

        frame = addController(new FrameController(this));
        brd = addController(new BoardController(this));
        figure = addController(new FigureController(this));
        timer = addController(new TimerController(this));
        trail = addController(new TrailController(this));
    }

    private void fillBoardWithRandomBlocks(int boardWidth, int boardHeight)
    {
        Random random = new Random();
        int n = random.nextInt(15) + 5;

        while (n > 0) {
            int x = random.nextInt(boardWidth);
            int y = random.nextInt(boardHeight);
            if (x < 4 && y < 4) continue;
            board.set(x, y, BlockFactory.makeRandomBlock());
            n--;
        }
    }

    public Board getBoard()
    {
        return board;
    }

    public void nextFigureTouchedOrTimeout()
    {
        board.action();
        figure.stopMoving();
        timer.reset();
    }

    public void loadTextures()
    {
        for (BlockTexture texture : BlockTexture.values()) {
            blockTextures.put(texture, atlas.findRegion(texture.toString().toLowerCase()));
        }

        for (TextureType textureType : TextureType.values()) {
            textures.put(textureType, atlas.findRegion(textureType.toString().toLowerCase()));
        }
    }

    public TextureRegion getTexture(Block block)
    {
        if (block != null) {
            return blockTextures.get(block.getTexture());
        }
        else {
            return null;
        }
    }

    public TextureRegion getTexture(TextureType blockType)
    {
        return textures.get(blockType);
    }

    public void drawBlock(final SpriteBatch batch, Block block, int x, int y)
    {
        if (block != null) {
            Vector2 pos = toGameCoords(x, y, 1);
            TextureRegion texture = getTexture(block);
            if (texture != null) {
                batch.draw(texture, pos.x, pos.y, 1, 1);
            }
            else {
                Gdx.app
                   .error("SQZ", String.format("Texture for block %s (%s) not found!", block.getType(), block.getClass().getSimpleName()));
            }
        }
    }

    public Vector2 toGameCoords(float x, float y)
    {
        return new Vector2(boardRectangle.x + x, boardRectangle.y + (boardRectangle.height) - y);
    }

    public Vector2 toGameCoords(float x, float y, float size)
    {
        return new Vector2(boardRectangle.x + x, boardRectangle.y + (boardRectangle.height - y) - size);
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, float delta)
    {
        for (Controller controller : controllers) {
            controller.renderDebug(renderer, delta);
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        for (Controller controller : controllers) {
            controller.render(batch, delta, renderUtils);
        }
    }

    @Override
    public boolean update(float delta)
    {
        for (Controller controller : controllers) {
            boolean remove = controller.update(delta);
            if (remove) {
                controllers.remove(controller);
            }
        }
        return false;
    }

    @Override
    public void touchDown(float x, float y, int pointer, int button)
    {
        for (Controller controller : controllers) {
            controller.touchDown(x, y, pointer, button);
        }
    }

    @Override
    public void touchUp(float x, float y, int pointer, int button)
    {
        for (Controller controller : controllers) {
            controller.touchUp(x, y, pointer, button);
        }
    }

    @Override
    public void touchDragged(float x, float y, int pointer)
    {
        for (Controller controller : controllers) {
            controller.touchDragged(x, y, pointer);
        }
    }

    private <T extends Controller> T addController(T controller)
    {
        controllers.add(controller);
        return controller;
    }

    public void removeController(BaseController controller)
    {
        Gdx.app.log("SQZ", "Removing controller " + controller);
        controllers.remove(controller);

    }

    public Rectangle getBoardRectangle()
    {
        return boardRectangle;
    }

    public Rectangle getSceneRectangle()
    {
        return sceneRectangle;
    }

    private class MainGameCallbacks implements GameCallbacks
    {
        @Override
        public void dissolving(List<PositionedBlock> blocks)
        {
            addController(new DissolvingAreaController(GameController.this, blocks));
        }

        @Override
        public BombCallback bomb(int x, int y)
        {
            return addController(new BombExplosionController(GameController.this, x, y));
        }

        @Override
        public MissileCallback missile(int fromX, int fromY, int dX, int dY)
        {
            return addController(new MissileFlightController(GameController.this, fromX, fromY, dX, dY));
        }

        @Override
        public void releaseSplodge(int x, int y, List<PositionedBlock> blocks, float delay)
        {
            //addController(new SplodgeContainerController(blocks, delay)); // TODO
        }

        @Override
        public void releaseAcid(int x, int y, List<PositionedBlock> blocks, float delay)
        {
            //addController(new AcidContainerController(blocks, delay)); // TODO
        }
    }
}
