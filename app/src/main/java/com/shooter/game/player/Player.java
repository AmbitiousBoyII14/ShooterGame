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
    public Vector2 position, velocity; public Rectangle bounds; public float width = 40, height = 60;
    public float health = 100, maxHealth = 100, moveSpeed = 300, jumpForce = 500, gravity = -1200;
    public float jetpackFuel = 100, maxJetpackFuel = 100, jetpackDrain = 40, jetpackRecharge = 20, jetpackForce = 800;
    public boolean facingRight = true, isAlive = true, isGrounded = false, moveLeft = false, moveRight = false, jump = false, jetpack = false, shoot = false;
    public float aimDirX = 1, aimDirY = 0; public int score = 0; public Weapon currentWeapon;
    public Player(float x, float y) { position = new Vector2(x, y); velocity = new Vector2(0, 0); bounds = new Rectangle(x, y, width, height); currentWeapon = new Pistol(); }
    public void update(float delta, Array<Platform> platforms, Array<Bullet> bullets) {
        if (!isAlive) return; currentWeapon.update(delta);
        if (moveLeft) { velocity.x = -moveSpeed; facingRight = false; } else if (moveRight) { velocity.x = moveSpeed; facingRight = true; } else velocity.x = 0;
        if (jetpack && jetpackFuel > 0) { velocity.y = jetpackForce; jetpackFuel -= jetpackDrain * delta; if (jetpackFuel < 0) jetpackFuel = 0; }
        else { velocity.y += gravity * delta; if (jump && isGrounded) { velocity.y = jumpForce; isGrounded = false; } if (isGrounded && !jetpack) { jetpackFuel += jetpackRecharge * delta; if (jetpackFuel > maxJetpackFuel) jetpackFuel = maxJetpackFuel; } }
        position.x += velocity.x * delta; position.y += velocity.y * delta; isGrounded = false; bounds.x = position.x; bounds.y = position.y;
        for (int i = 0; i < platforms.size; i++) { Platform p = platforms.get(i); if (bounds.overlaps(p.bounds)) { float overlapX = Math.min(bounds.x + bounds.width - p.bounds.x, p.bounds.x + p.bounds.width - bounds.x); float overlapY = Math.min(bounds.y + bounds.height - p.bounds.y, p.bounds.y + p.bounds.height - bounds.y); if (overlapX < overlapY) { if (bounds.x < p.bounds.x) position.x = p.bounds.x - bounds.width; else position.x = p.bounds.x + p.bounds.width; velocity.x = 0; } else { if (bounds.y < p.bounds.y) { position.y = p.bounds.y - bounds.height; velocity.y = 0; isGrounded = true; } else { position.y = p.bounds.y + p.bounds.height; velocity.y = 0; } } bounds.x = position.x; bounds.y = position.y; } }
        if (shoot) currentWeapon.fire(position.x + width / 2, position.y + height / 2, aimDirX, aimDirY, bullets);
    }
    public void takeDamage(float amount) { if (!isAlive) return; health -= amount; if (health <= 0) { health = 0; isAlive = false; } }
    public void respawn(float x, float y) { position.set(x, y); velocity.set(0, 0); health = maxHealth; jetpackFuel = maxJetpackFuel; isAlive = true; currentWeapon.currentAmmo = currentWeapon.magSize; }
    public void render(ShapeRenderer sr) { if (!isAlive) return; sr.setColor(0.2f, 0.6f, 1f, 1); sr.rect(position.x, position.y, width, height); sr.setColor(1, 1, 0, 1); sr.line(position.x + width / 2, position.y + height / 2, position.x + width / 2 + aimDirX * 40, position.y + height / 2 + aimDirY * 40); }
}