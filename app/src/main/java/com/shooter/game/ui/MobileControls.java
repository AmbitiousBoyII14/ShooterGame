package com.shooter.game.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shooter.game.player.Player;
public class MobileControls {
    public float leftBaseX, leftBaseY, leftKnobX, leftKnobY;
    public float rightBaseX, rightBaseY, rightKnobX, rightKnobY;
    public float stickRadius;
    public int leftPointer = -1, rightPointer = -1;
    public boolean moveLeft, moveRight, jump, jetpack, shoot;
    public float aimDirX, aimDirY;
    public MobileControls() {
        stickRadius = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) * 0.15f;
        leftBaseX = 250; leftBaseY = 250; leftKnobX = leftBaseX; leftKnobY = leftBaseY;
        rightBaseX = Gdx.graphics.getWidth() - 250; rightBaseY = 250; rightKnobX = rightBaseX; rightKnobY = rightBaseY;
    }
    public void handleTouch(Player player) {
        moveLeft = moveRight = jump = jetpack = shoot = false;
        aimDirX = player.facingRight ? 1 : -1; aimDirY = 0;
        if (leftPointer >= 0 && !Gdx.input.isTouched(leftPointer)) { leftPointer = -1; leftKnobX = leftBaseX; leftKnobY = leftBaseY; }
        if (rightPointer >= 0 && !Gdx.input.isTouched(rightPointer)) { rightPointer = -1; rightKnobX = rightBaseX; rightKnobY = rightBaseY; }
        for (int i = 0; i < 10; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            float x = Gdx.input.getX(i), y = Gdx.graphics.getHeight() - Gdx.input.getY(i);
            boolean isLeftSide = (x < Gdx.graphics.getWidth() / 2);
            if (isLeftSide) {
                if (leftPointer == -1) { leftPointer = i; leftBaseX = x; leftBaseY = y; leftKnobX = x; leftKnobY = y; }
                if (i == leftPointer) {
                    float dx = x - leftBaseX, dy = y - leftBaseY, dist = (float)Math.sqrt(dx*dx + dy*dy);
                    if (dist > stickRadius) { dx = (dx / dist) * stickRadius; dy = (dy / dist) * stickRadius; }
                    leftKnobX = leftBaseX + dx; leftKnobY = leftBaseY + dy;
                    if (dx < -stickRadius * 0.3f) moveLeft = true;
                    if (dx > stickRadius * 0.3f) moveRight = true;
                    if (dy > stickRadius * 0.3f) { jump = true; jetpack = true; }
                }
            } else {
                if (rightPointer == -1) { rightPointer = i; rightBaseX = x; rightBaseY = y; rightKnobX = x; rightKnobY = y; }
                if (i == rightPointer) {
                    float dx = x - rightBaseX, dy = y - rightBaseY, dist = (float)Math.sqrt(dx*dx + dy*dy);
                    if (dist > stickRadius) { dx = (dx / dist) * stickRadius; dy = (dy / dist) * stickRadius; }
                    rightKnobX = rightBaseX + dx; rightKnobY = rightBaseY + dy;
                    if (dist > stickRadius * 0.2f) { shoot = true; aimDirX = dx / stickRadius; aimDirY = dy / stickRadius; }
                }
            }
        }
        player.moveLeft = moveLeft; player.moveRight = moveRight; player.jump = jump; player.jetpack = jetpack; player.shoot = shoot;
        player.aimDirX = aimDirX; player.aimDirY = aimDirY;
        if (aimDirX != 0 || aimDirY != 0) player.facingRight = aimDirX >= 0;
    }
    public void render(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 0.2f); sr.circle(leftBaseX, leftBaseY, stickRadius); sr.circle(rightBaseX, rightBaseY, stickRadius);
        sr.setColor(1, 1, 1, 0.5f); sr.circle(leftKnobX, leftKnobY, stickRadius * 0.5f); sr.circle(rightKnobX, rightKnobY, stickRadius * 0.5f);
    }
}