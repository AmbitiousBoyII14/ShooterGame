package com.shooter.game.weapons;
import com.badlogic.gdx.utils.Array;
import com.shooter.game.bullets.Bullet;
public abstract class Weapon {
    public String name; public float damage, fireRate, fireCooldown, reloadTime, reloadTimer, bulletSpeed, spread;
    public int magSize, currentAmmo, reserveAmmo; public boolean isReloading;
    public Weapon() { fireCooldown = 0; isReloading = false; reloadTimer = 0; }
    public abstract void fire(float x, float y, float dirX, float dirY, Array<Bullet> bulletPool);
    public void update(float delta) {
        if (fireCooldown > 0) fireCooldown -= delta;
        if (isReloading) { reloadTimer -= delta; if (reloadTimer <= 0) { int needed = magSize - currentAmmo; if (reserveAmmo >= needed) { currentAmmo = magSize; reserveAmmo -= needed; } else { currentAmmo += reserveAmmo; reserveAmmo = 0; } isReloading = false; } }
    }
}