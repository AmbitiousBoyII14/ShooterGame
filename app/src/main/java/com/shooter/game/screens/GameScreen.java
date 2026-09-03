package com.shooter.game.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.game.MainGame;
import com.shooter.game.bullets.Bullet;
import com.shooter.game.enemies.Enemy;
import com.shooter.game.player.Player;
import com.shooter.game.ui.HUD;
import com.shooter.game.ui.MobileControls;
import com.shooter.game.world.GameMap;
public class GameScreen implements Screen {
    private MainGame game; private OrthographicCamera camera; private Viewport viewport;
    private OrthographicCamera uiCamera; private ShapeRenderer shapeRenderer; private SpriteBatch batch;
    private GameMap map; private Player player; private Enemy enemy; private Array<Bullet> bullets;
    private MobileControls controls; private HUD hud;
    private float playerRespawnTimer = 0, enemyRespawnTimer = 0;
    public GameScreen(MainGame game) {
        this.game = game; camera = new OrthographicCamera(); viewport = new FitViewport(1280, 720, camera);
        uiCamera = new OrthographicCamera(); uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer = new ShapeRenderer(); batch = new SpriteBatch(); map = new GameMap();
        player = new Player(100, 100); enemy = new Enemy(1000, 100); bullets = new Array<Bullet>();
        controls = new MobileControls(); hud = new HUD();
    }
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        controls.handleTouch(player); player.update(delta, map.platforms, bullets); enemy.update(delta, player, map.platforms, bullets);
        for (int i = 0; i < bullets.size; i++) {
            Bullet b = bullets.get(i); if (!b.active) continue; b.update(delta);
            for (int j = 0; j < map.platforms.size; j++) { if (b.bounds.overlaps(map.platforms.get(j).bounds)) { b.active = false; break; } }
            if (!b.active) continue;
            if (b.isPlayerBullet) { if (enemy.isAlive && b.bounds.overlaps(enemy.bounds)) { enemy.takeDamage(b.damage); b.active = false; if (!enemy.isAlive) player.score += 100; } }
            else { if (player.isAlive && b.bounds.overlaps(player.bounds)) { player.takeDamage(b.damage); b.active = false; } }
        }
        if (!player.isAlive) { if (playerRespawnTimer <= 0) playerRespawnTimer = 2.0f; playerRespawnTimer -= delta; if (playerRespawnTimer <= 0) player.respawn(100, 100); }
        if (!enemy.isAlive) { if (enemyRespawnTimer <= 0) enemyRespawnTimer = 3.0f; enemyRespawnTimer -= delta; if (enemyRespawnTimer <= 0) enemy.respawn(); }
        camera.position.set(player.position.x + player.width / 2, player.position.y + player.height / 2, 0); camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined); shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        map.render(shapeRenderer); player.render(shapeRenderer); enemy.render(shapeRenderer);
        shapeRenderer.setColor(1, 1, 0, 1);
        for (int i = 0; i < bullets.size; i++) { Bullet b = bullets.get(i); if (b.active) shapeRenderer.circle(b.position.x, b.position.y, 4); }
        shapeRenderer.end();
        uiCamera.update(); shapeRenderer.setProjectionMatrix(uiCamera.combined); shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        controls.render(shapeRenderer); shapeRenderer.end();
        batch.setProjectionMatrix(uiCamera.combined); batch.begin(); hud.render(batch, player); batch.end();
    }
    @Override public void resize(int width, int height) { viewport.update(width, height); uiCamera.setToOrtho(false, width, height); controls = new MobileControls(); }
    @Override public void show() {} @Override public void pause() {} @Override public void resume() {} @Override public void hide() {}
    @Override public void dispose() { shapeRenderer.dispose(); batch.dispose(); hud.dispose(); }
}