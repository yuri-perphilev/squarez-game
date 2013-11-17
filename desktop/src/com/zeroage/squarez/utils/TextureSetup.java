package com.zeroage.squarez.utils;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class TextureSetup
{
    public static void main(String[] args)
    {
        TexturePacker2.process("D:\\projects\\squarez\\android\\assets\\images\\texture-elements",
                               "D:\\projects\\squarez\\android\\assets\\images\\textures",
                               "textures.pack");
    }
}
