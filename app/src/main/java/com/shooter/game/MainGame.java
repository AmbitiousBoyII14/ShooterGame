package com.shooter.game;

import com.badlogic.gdx.Game;
import com.shooter.game.screens.GameScreen;

public class MainGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}