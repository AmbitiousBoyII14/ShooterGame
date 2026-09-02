package com.shooter.game.bullets;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public Vector2 position;
    public Vector2 velocity;
    public Rectangle bounds;
    public float damage;
    public float lifetime;
    public boolean active;
    public boolean isPlayerBullet;

    public Bullet() {
        position = new Vector2();
        velocity = new Vector2();
        bounds = new Rectangle();
        active = false;
    }

    public void init(float x, float y, float vx, float vy, float dmg, boolean fromPlayer) {
        position.set(x, y);
        velocity.set(vx, vy);
        damage = dmg;
        lifetime = 2.0f;
        active = true;
        isPlayerBullet = fromPlayer;
        bounds.set(x - 4, y - 4, 8, 8);
    }

    public void update(float delta) {
        if (!active) return;
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        bounds.x = position.x - 4;
        bounds.y = position.y - 4;
        lifetime -= delta;
        if (lifetime <= 0) active = false;
    }
}
