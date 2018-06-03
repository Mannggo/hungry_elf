package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.constant.CameraConstants;

/**
 * @author xiezd 2018-05-31 10:35
 */
public class WorldController {
    private static final String TAG = WorldController.class.getName();
    public Sprite me;

    public WorldController() {
        init();
    }

    private void init() {
        initTestObjects();
    }

    private void initTestObjects() {

        // Create empty POT-sized Pixmap with 8 bit RGBA pixel data
        int width = 32;
        int height = 32;
        Pixmap pixmap = createProceduralPixmap(width, height);
        // Create a new texture from pixmap data
        Texture texture = new Texture(pixmap);
        // Create new sprites using the just created texture
        me = new Sprite(texture);
        float meWidth = CameraConstants.VIEWPORT_WIDTH / 10.0f;
        float meHeight = CameraConstants.VIEWPORT_HEIGHT / 10.0f;
        me.setSize(meWidth, meHeight);
        me.setOrigin(meWidth / 2.0f, meHeight / 2.0f);
        me.setPosition(0,0);
    }

    private Pixmap createProceduralPixmap(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 0.8f);
        pixmap.fill();
        pixmap.setColor(1, 1, 0, 0.7f);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        return pixmap;
    }

    public void update(float deltaTime) {
        updateTestObjects(deltaTime);
    }

    private void updateTestObjects(float deltaTime) {
        // Get current rotation from selected sprite
        float rotation = me.getRotation();
        // Rotate sprite by 90 degrees per second
        rotation += 90 * deltaTime;
        // Wrap around at 360 degrees
        rotation %= 360;
        // Set new rotation value to selected sprite
        me.setRotation(rotation);
    }

}
