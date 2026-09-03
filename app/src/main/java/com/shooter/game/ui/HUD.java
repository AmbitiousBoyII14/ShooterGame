package com.shooter.game.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shooter.game.player.Player;
public class HUD {
    public void render(ShapeRenderer sr, Player player) {
        float sw = Gdx.graphics.getWidth(), sh = Gdx.graphics.getHeight();
        sr.setColor(0.3f, 0.3f, 0.3f, 0.8f); sr.rect(20, sh - 40, 200, 20);
        sr.setColor(0.2f, 0.8f, 0.2f, 1f); sr.rect(20, sh - 40, 200 * (player.health / player.maxHealth), 20);
        sr.setColor(0.3f, 0.3f, 0.3f, 0.8f); sr.rect(20, sh - 70, 200, 20);
        sr.setColor(0.2f, 0.6f, 1f, 1f); sr.rect(20, sh - 70, 200 * (player.jetpackFuel / player.maxJetpackFuel), 20);
        float ammoX = sw - 20; sr.setColor(1, 0.8f, 0.2f, 1f);
        for(int i=0; i<player.currentWeapon.currentAmmo; i++) { sr.rect(ammoX - (i * 12), sh - 40, 8, 20); if (i > 15) break; }
    }
}