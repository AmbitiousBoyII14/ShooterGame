package com.shooter.game.weapons;
import com.badlogic.gdx.utils.Array;
import com.shooter.game.bullets.Bullet;
public class Pistol extends Weapon {
    public Pistol() { name = "PISTOL"; damage = 20; fireRate = 0.3f; magSize = 12; currentAmmo = 12; reserveAmmo = 60; reloadTime = 1.5f; bulletSpeed = 800; spread = 0.05f; }
    @Override public void fire(float x, float y, float dirX, float dirY, Array<Bullet> bulletPool) {
        if (fireCooldown <= 0 && !isReloading && currentAmmo > 0) {
            currentAmmo--; fireCooldown = fireRate; Bullet b = null;
            for (int i = 0; i < bulletPool.size; i++) { if (!bulletPool.get(i).active) { b = bulletPool.get(i); break; } }
            if (b == null) { b = new Bullet(); bulletPool.add(b); }
            float spreadX = dirX + (float)(Math.random() * spread * 2 - spread), spreadY = dirY + (float)(Math.random() * spread * 2 - spread);
            float len = (float)Math.sqrt(spreadX * spreadX + spreadY * spreadY); if (len > 0) { spreadX /= len; spreadY /= len; }
            b.init(x, y, spreadX * bulletSpeed, spreadY * bulletSpeed, damage, true);
        }
    }
}