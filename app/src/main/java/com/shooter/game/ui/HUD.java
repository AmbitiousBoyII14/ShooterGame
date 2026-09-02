package com.shooter.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shooter.game.player.Player;

public class HUD {
    private BitmapFont font;

    public HUD() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);
    }

    public void render(SpriteBatch batch, Player player) {
        float y = Gdx.graphics.getHeight() - 40;

        font.draw(batch, "HP: " + (int)player.health + "/" + (int)player.maxHealth, 20, y);
        font.draw(batch, "WPN: " + player.currentWeapon.name, 20, y - 40);
        font.draw(batch, "AMMO: " + player.currentWeapon.currentAmmo + " / " + player.currentWeapon.reserveAmmo, 20, y - 80);

        String jetpackStr = "JETPACK: ";
        int bars = (int)((player.jetpackFuel / player.maxJetpackFuel) * 10);
        for (int i = 0; i < 10; i++) {
            if (i < bars) jetpackStr += "|";
            else jetpackStr += ".";
        }
        font.draw(batch, jetpackStr, 20, y - 120);

        font.draw(batch, "SCORE: " + player.score, Gdx.graphics.getWidth() - 200, y);
    }

    public void dispose() {
        font.dispose();
    }
}
