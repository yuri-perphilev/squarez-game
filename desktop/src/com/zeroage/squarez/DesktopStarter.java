package com.zeroage.squarez;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter
{
    public static void main(String[] args)
    {
        int scale = 2;
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Squarez";
        cfg.useGL20 = true;
        cfg.width = 800;
        cfg.height = 1280;
//        new LwjglApplication(new SquarezGame(), cfg);
        new LwjglApplication(new SquarezGame(), "Squarez", cfg.width/ scale, cfg.height /2, false);
    }
}
