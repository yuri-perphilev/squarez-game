package com.zeroage.squarez;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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

    public GameController(Board board, float viewportWidth, float viewportHeight)
    {
        this.board = board;
        float boardWidth = Math.round(viewportWidth) - 1; // half of a unit from the left and from the right
        float boardHeight = Math.round(viewportHeight) - 7; // 2 (score line) + 3 (pocket) + 2 (spacers)

        boardRectangle = new Rectangle(0.5f, 2.5f, boardWidth, boardHeight);

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
