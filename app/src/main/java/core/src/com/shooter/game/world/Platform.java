package com.shooter.game.world;

import com.badlogic.gdx.math.Rectangle;

public class Platform {
    public Rectangle bounds;

    public Platform(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }
}
