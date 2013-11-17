package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.zeroage.squarez.model.BasicBlock;
import com.zeroage.squarez.model.BlockType;
import com.zeroage.squarez.model.Board;
import com.zeroage.squarez.model.GameEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameController implements Controller
{
    public static final int FIGURE_LIFETIME = 10;

    private List<Controller> controllers = new CopyOnWriteArrayList<Controller>();

    private Board board;
    private Rectangle boardRectangle;

    private FrameController frame;
    private FigureController figure;
    private TimerController timer;
    private TrailController trail;
    private BoardController brd;

    private TextureAtlas atlas;
    private Map<BlockType, TextureRegion> blockTextures = new HashMap<BlockType, TextureRegion>(32);

    public GameController(float viewportWidth, float viewportHeight, TextureAtlas atlas)
    {
        int boardWidth = Math.round(viewportWidth) - 1; // half of a unit from the left and from the right
        int boardHeight = Math.round(viewportHeight) - 7; // 2 (score line) + 3 (pocket) + 2 (spacers)

        boardRectangle = new Rectangle(0.5f, 2.5f, boardWidth, boardHeight);

        board = new Board(boardWidth, boardHeight, new MyGameEventListener());

        board.set(5, 5, new BasicBlock());
        board.set(11, 11, new BasicBlock());
        board.set(3, 13, new BasicBlock());
        board.set(10, 2, new BasicBlock());

        this.atlas = atlas;
        loadTextures();

        frame = addController(new FrameController(this));
        brd = addController(new BoardController(this));
        figure = addController(new FigureController(this));
        timer = addController(new TimerController(this));
        trail = addController(new TrailController(this));
    }

    public Board getBoard()
    {
        return board;
    }

    public void action()
    {
        board.action();
        figure.stopMoving();
        timer.reset();
    }

    public void loadTextures()
    {
        blockTextures.put(BlockType.BASIC, atlas.findRegion("basic"));
    }

    public TextureRegion getTexture(BlockType blockType)
    {
        return blockTextures.get(blockType);
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, float delta)
    {
        for (Controller controller : controllers) {
            controller.renderDebug(renderer, delta);
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta)
    {
        for (Controller controller : controllers) {
            controller.render(batch, delta);
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

    private class MyGameEventListener implements GameEventListener
    {
        @Override
        public void dissolving(List<Board.Area> areas)
        {
            addController(new DissolvingAreaController(GameController.this, areas));
        }
    }
}
