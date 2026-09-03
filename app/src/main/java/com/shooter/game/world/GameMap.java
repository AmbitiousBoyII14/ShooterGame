package com.shooter.game.world;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
public class GameMap {
    public Array<Platform> platforms; public float worldWidth = 1280, worldHeight = 720;
    public GameMap() {
        platforms = new Array<Platform>();
        platforms.add(new Platform(0, 0, worldWidth, 50));
        platforms.add(new Platform(0, 50, 50, worldHeight - 50));
        platforms.add(new Platform(worldWidth - 50, 50, 50, worldHeight - 50));
        platforms.add(new Platform(200, 200, 300, 30));
        platforms.add(new Platform(600, 350, 300, 30));
        platforms.add(new Platform(950, 200, 250, 30));
    }
    public void render(ShapeRenderer sr) { sr.setColor(0.3f, 0.3f, 0.3f, 1); for (int i = 0; i < platforms.size; i++) { Platform p = platforms.get(i); sr.rect(p.bounds.x, p.bounds.y, p.bounds.width, p.bounds.height); } }
}