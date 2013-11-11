package com.zeroage.squarez;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Controller
{
    void renderDebug(ShapeRenderer renderer, float delta);

    void render(float delta);

    /**
     * Updates controller state
     *
     * @param delta time from the last update in seconds
     * @return true if controller should be removed from the controllers queue
     */
    boolean update(float delta);

    void touchDown(float x, float y, int pointer, int button);

    void touchUp(float x, float y, int pointer, int button);

    void touchDragged(float x, float y, int pointer);
}
