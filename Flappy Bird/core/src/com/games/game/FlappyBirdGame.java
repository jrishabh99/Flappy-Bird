package com.games.game;

import com.badlogic.gdx.Game;

import Screens.MainMenuScreen;

public class FlappyBirdGame extends Game {
	
	@Override
	public void create () {

        this.setScreen(new MainMenuScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {

	}
}
