package com.zeroage.squarez;

public abstract class BaseController implements Controller
{
    private GameController gameController;

    protected BaseController(GameController gameController)
    {
        this.gameController = gameController;
    }

    public GameController getGameController()
    {
        return gameController;
    }
}
