package com.zeroage.squarez;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.zeroage.squarez.model.BasicBlock;
import com.zeroage.squarez.model.Board;

import java.util.ArrayList;
import java.util.List;

public class GameController implements Controller
{
    public static final int FIGURE_LIFETIME = 10;

    private List<Controller> controllers = new ArrayList<Controller>();

    private Board board;
    private Rectangle boardRectangle;

    private FrameController frame;
    private FigureController figure;
    private TimerController timer;
    private TrailController trail;
    private BoardController boardController;

    public GameController(float viewportWidth, float viewportHeight)
    {
        int boardWidth = Math.round(viewportWidth) - 1; // half of a unit from the left and from the right
        int boardHeight = Math.round(viewportHeight) - 7; // 2 (score line) + 3 (pocket) + 2 (spacers)

        boardRectangle = new Rectangle(0.5f, 2.5f, boardWidth, boardHeight);

        board = new Board(boardWidth, boardHeight);

        board.set(5, 5, new BasicBlock());
        board.set(11, 11, new BasicBlock());
        board.set(3, 13, new BasicBlock());
        board.set(10, 2, new BasicBlock());


        frame = addController(new FrameController(this));
        boardController = addController(new BoardController(this));
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

    @Override
    public void renderDebug(ShapeRenderer renderer, float delta)
    {
        for (Controller controller : controllers) {
            controller.renderDebug(renderer, delta);
        }
    }

    @Override
    public void render(float delta)
    {
        for (Controller controller : controllers) {
            controller.render(delta);
        }
    }

    @Override
    public void update(float delta)
    {
        for (Controller controller : controllers) {
            controller.update(delta);
        }
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

    public Rectangle getBoardRectangle()
    {
        return boardRectangle;
    }

}