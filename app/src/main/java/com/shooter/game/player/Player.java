package com.shooter.game.player;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shooter.game.bullets.Bullet;
import com.shooter.game.weapons.Pistol;
import com.shooter.game.weapons.Weapon;
import com.shooter.game.world.Platform;

public class Player {
    public Vector2 position;
    public Vector2 velocity;
    public Rectangle bounds;
    public float width = 40;
    public float height = 60;

    public float health = 100;
    public float maxHealth = 100;
    public float moveSpeed = 300;
    public float jumpForce = 500;
    public float gravity = -1200;

    public float jetpackFuel = 100;
    public float maxJetpackFuel = 100;
    public float jetpackDrain = 40;
    public float jetpackRecharge = 20;
    public float jetpackForce = 800;

    public boolean facingRight = true;
    public boolean isAlive = true;
    public boolean isGrounded = false;

    public Weapon currentWeapon;

    // Input states managed by MobileControls
    public boolean moveLeft = false;
    public boolean moveRight = false;
    public boolean jump = false;
    public boolean jetpack = false;
    public boolean shoot = false;
    public float aimDirX = 1;
    public float aimDirY = 0;

    public int score = 0;

    public Player(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(x, y, width, height);
        currentWeapon = new Pistol();
    }

    public void update(float delta, Array<Platform> platforms, Array<Bullet> bullets) {
        if (!isAlive) return;

        currentWeapon.update(delta);

        // Horizontal movement
        if (moveLeft) {
            velocity.x = -moveSpeed;
            facingRight = false;
        } else if (moveRight) {
            velocity.x = moveSpeed;
            facingRight = true;
        } else {
            velocity.x = 0;
        }

        // Jetpack vs Jump & Gravity
        if (jetpack && jetpackFuel > 0) {
            velocity.y = jetpackForce; 
            jetpackFuel -= jetpackDrain * delta;
            if (jetpackFuel < 0) jetpackFuel = 0;
        } else {
            velocity.y += gravity * delta;
            if (jump && isGrounded) {
                velocity.y = jumpForce;
                isGrounded = false;
            }
            if (isGrounded && !jetpack) {
                jetpackFuel += jetpackRecharge * delta;
                if (jetpackFuel > maxJetpackFuel) jetpackFuel = maxJetpackFuel;
            }
        }

        // Apply velocity
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        // Collision resolution
        isGrounded = false;
        bounds.x = position.x;
        bounds.y = position.y;

        for (int i = 0; i < platforms.size; i++) {
            Platform p = platforms.get(i);
            if (bounds.overlaps(p.bounds)) {
                float overlapX = Math.min(bounds.x + bounds.width - p.bounds.x, p.bounds.x + p.bounds.width - bounds.x);
                float overlapY = Math.min(bounds.y + bounds.height - p.bounds.y, p.bounds.y + p.bounds.height - bounds.y);

                if (overlapX < overlapY) {
                    if (bounds.x < p.bounds.x) position.x = p.bounds.x - bounds.width;
                    else position.x = p.bounds.x + p.bounds.width;
                    velocity.x = 0;
                } else {
                    if (bounds.y < p.bounds.y) {
                        position.y = p.bounds.y - bounds.height;
                        velocity.y = 0;
                        isGrounded = true;
                    } else {
                        position.y = p.bounds.y + p.bounds.height;
                        velocity.y = 0;
                    }
                }
                bounds.x = position.x;
                bounds.y = position.y;
            }
        }

        // Shooting
        if (shoot) {
            float spawnX = position.x + width / 2;
            float spawnY = position.y + height / 2;
            currentWeapon.fire(spawnX, spawnY, aimDirX, aimDirY, bullets);
        }
    }

    public void takeDamage(float amount) {
        if (!isAlive) return;
        health -= amount;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
    }

    public void respawn(float x, float y) {
        position.set(x, y);
        velocity.set(0, 0);
        health = maxHealth;
        jetpackFuel = maxJetpackFuel;
        isAlive = true;
        currentWeapon.currentAmmo = currentWeapon.magSize;
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (!isAlive) return;
        shapeRenderer.setColor(0.2f, 0.6f, 1f, 1);
        shapeRenderer.rect(position.x, position.y, width, height);

        shapeRenderer.setColor(1, 1, 0, 1);
        float cx = position.x + width / 2;
        float cy = position.y + height / 2;
        shapeRenderer.line(cx, cy, cx + aimDirX * 40, cy + aimDirY * 40);
    }
}
