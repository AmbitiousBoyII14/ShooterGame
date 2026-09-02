package com.shooter.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.shooter.game.player.Player;

public class MobileControls {
    public Rectangle btnLeft, btnRight, btnUp, btnDown;
    public Rectangle btnShoot, btnJetpack, btnGrenade;

    public boolean leftPressed, rightPressed, upPressed, downPressed;
    public boolean shootPressed, jetpackPressed;

    public float screenWidth;
    public float screenHeight;
    public float btnSize;
    public float padding;

    public MobileControls() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        btnSize = Math.min(screenWidth, screenHeight) * 0.12f;
        padding = btnSize * 0.3f;

        float leftBaseX = padding;
        float leftBaseY = padding + btnSize;

        btnUp = new Rectangle(leftBaseX + btnSize + padding, leftBaseY + btnSize + padding, btnSize, btnSize);
        btnDown = new Rectangle(leftBaseX + btnSize + padding, leftBaseY - btnSize - padding, btnSize, btnSize);
        btnLeft = new Rectangle(leftBaseX, leftBaseY, btnSize, btnSize);
        btnRight = new Rectangle(leftBaseX + (btnSize + padding) * 2, leftBaseY, btnSize, btnSize);

        float rightBaseX = screenWidth - btnSize - padding;
        float rightBaseY = padding + btnSize;

        btnShoot = new Rectangle(rightBaseX - btnSize - padding, rightBaseY + btnSize + padding, btnSize * 1.5f, btnSize * 1.5f);
        btnJetpack = new Rectangle(rightBaseX, rightBaseY + btnSize + padding, btnSize, btnSize);
        btnGrenade = new Rectangle(rightBaseX, rightBaseY - btnSize - padding, btnSize, btnSize);
    }

    public void handleTouch(Player player) {
        leftPressed = false; rightPressed = false; upPressed = false; downPressed = false;
        shootPressed = false; jetpackPressed = false;

        for (int i = 0; i < 10; i++) {
            if (!Gdx.input.isTouched(i)) continue;

            float x = Gdx.input.getX(i);
            float y = Gdx.graphics.getHeight() - Gdx.input.getY(i); 

            if (btnLeft.contains(x, y)) leftPressed = true;
            if (btnRight.contains(x, y)) rightPressed = true;
            if (btnUp.contains(x, y)) upPressed = true;
            if (btnDown.contains(x, y)) downPressed = true;

            if (btnShoot.contains(x, y)) shootPressed = true;
            if (btnJetpack.contains(x, y)) jetpackPressed = true;
        }

        player.moveLeft = leftPressed;
        player.moveRight = rightPressed;
        player.jump = upPressed; 
        player.jetpack = jetpackPressed || upPressed; 
        player.shoot = shootPressed;

        float dx = 0;
        float dy = 0;
        if (leftPressed) dx -= 1;
        if (rightPressed) dx += 1;
        if (upPressed) dy += 1;
        if (downPressed) dy -= 1;

        if (dx == 0 && dy == 0) {
            dx = player.facingRight ? 1 : -1;
        }

        float len = (float)Math.sqrt(dx * dx + dy * dy);
        if (len > 0) {
            player.aimDirX = dx / len;
            player.aimDirY = dy / len;
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 1, 1, 0.3f);
        shapeRenderer.rect(btnLeft.x, btnLeft.y, btnLeft.width, btnLeft.height);
        shapeRenderer.rect(btnRight.x, btnRight.y, btnRight.width, btnRight.height);
        shapeRenderer.rect(btnUp.x, btnUp.y, btnUp.width, btnUp.height);
        shapeRenderer.rect(btnDown.x, btnDown.y, btnDown.width, btnDown.height);

        shapeRenderer.setColor(1, 0, 0, 0.4f);
        shapeRenderer.rect(btnShoot.x, btnShoot.y, btnShoot.width, btnShoot.height);

        shapeRenderer.setColor(0, 1, 1, 0.3f);
        shapeRenderer.rect(btnJetpack.x, btnJetpack.y, btnJetpack.width, btnJetpack.height);
        shapeRenderer.rect(btnGrenade.x, btnGrenade.y, btnGrenade.width, btnGrenade.height);
    }
}
