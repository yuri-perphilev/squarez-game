package com.zeroage.squarez;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class DesktopStarter
{
    public static void main(String[] args)
    {
        TexturePacker2.Settings settings = new TexturePacker2.Settings();
        settings.maxWidth = 512;
        settings.maxHeight = 512;
        TexturePacker2.process(settings, "images/texture-elements", "images/textures", "textures.pack");

        int scale = 2;
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Squarez";
        cfg.useGL20 = true;
        cfg.width = 800;
        cfg.height = 1280;

        new LwjglApplication(new SquarezGame(), "Squarez", cfg.width / scale, cfg.height / 2, false);
    }
}
