package com.shooter.game.enemies;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shooter.game.bullets.Bullet;
import com.shooter.game.player.Player;
import com.shooter.game.world.Platform;
public class Enemy {
    public Vector2 position, velocity, spawnPos; public Rectangle bounds;
    public float width = 40, height = 60, health = 100, maxHealth = 100, moveSpeed = 150, gravity = -1200;
    public boolean isAlive = true, isGrounded = false;
    public static final int STATE_PATROL = 1, STATE_CHASE = 2; public int state = STATE_PATROL;
    public float shootCooldown = 0, shootRate = 1.2f, detectionRange = 500, patrolTimer = 0, patrolDir = 1;
    public Enemy(float x, float y) { position = new Vector2(x, y); velocity = new Vector2(0, 0); bounds = new Rectangle(x, y, width, height); spawnPos = new Vector2(x, y); }
    public void update(float delta, Player player, Array<Platform> platforms, Array<Bullet> bullets) {
        if (!isAlive) return; velocity.y += gravity * delta;
        float dist = position.dst(player.position); state = (player.isAlive && dist < detectionRange) ? STATE_CHASE : STATE_PATROL;
        if (state == STATE_PATROL) { patrolTimer += delta; if (patrolTimer > 2.0f) { patrolDir *= -1; patrolTimer = 0; } velocity.x = moveSpeed * patrolDir; }
        else if (state == STATE_CHASE) { velocity.x = (player.position.x > position.x) ? moveSpeed : -moveSpeed; shootCooldown -= delta; if (shootCooldown <= 0) { shootAtPlayer(player, bullets); shootCooldown = shootRate; } }
        else velocity.x = 0;
        position.x += velocity.x * delta; position.y += velocity.y * delta; isGrounded = false; bounds.x = position.x; bounds.y = position.y;
        for (int i = 0; i < platforms.size; i++) { Platform p = platforms.get(i); if (bounds.overlaps(p.bounds)) { float overlapX = Math.min(bounds.x + bounds.width - p.bounds.x, p.bounds.x + p.bounds.width - bounds.x); float overlapY = Math.min(bounds.y + bounds.height - p.bounds.y, p.bounds.y + p.bounds.height - bounds.y); if (overlapX < overlapY) { position.x = (bounds.x < p.bounds.x) ? p.bounds.x - bounds.width : p.bounds.x + p.bounds.width; velocity.x = 0; patrolDir *= -1; } else { if (bounds.y < p.bounds.y) { position.y = p.bounds.y - bounds.height; velocity.y = 0; isGrounded = true; } else { position.y = p.bounds.y + p.bounds.height; velocity.y = 0; } } bounds.x = position.x; bounds.y = position.y; } }
    }
    private void shootAtPlayer(Player player, Array<Bullet> bullets) {
        float startX = position.x + width / 2, startY = position.y + height / 2, targetX = player.position.x + player.width / 2, targetY = player.position.y + player.height / 2;
        float dx = targetX - startX, dy = targetY - startY, len = (float)Math.sqrt(dx * dx + dy * dy); if (len > 0) { dx /= len; dy /= len; }
        Bullet b = null; for (int i = 0; i < bullets.size; i++) { if (!bullets.get(i).active) { b = bullets.get(i); break; } } if (b == null) { b = new Bullet(); bullets.add(b); }
        b.init(startX, startY, dx * 400, dy * 400, 10, false);
    }
    public void takeDamage(float amount) { if (!isAlive) return; health -= amount; if (health <= 0) { health = 0; isAlive = false; } }
    public void respawn() { position.set(spawnPos); velocity.set(0, 0); health = maxHealth; isAlive = true; }
    public void render(ShapeRenderer sr) { if (!isAlive) return; sr.setColor(1f, 0.2f, 0.2f, 1); sr.rect(position.x, position.y, width, height); }
}