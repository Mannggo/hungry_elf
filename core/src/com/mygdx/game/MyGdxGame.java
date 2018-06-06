package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.google.gson.Gson;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.dto.LoginInfo;
import com.mygdx.game.dto.PlayerInfo;
import com.mygdx.game.dto.TransferInfo;
import com.mygdx.game.renderer.PlayerRenderer;
import com.mygdx.game.udp.client.UDPClient;

public class MyGdxGame extends ApplicationAdapter {
	
	private String TAG = getClass().getSimpleName();
	private PlayerController playerController;
	private PlayerRenderer playerRenderer;
	
	
	
	@Override
	public void create () {
		
		playerController = new PlayerController();
		playerRenderer = new PlayerRenderer(playerController);
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.debug(TAG, "Game Starting.");
	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(0xf,0xf,0xf, 0.2f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		playerRenderer.render();
		
	}
	
	@Override
	public void dispose () {
		playerRenderer.dispose();
		Gdx.app.debug(TAG, "Game Over.");
	}

    @Override
    public void resize (int width, int height) {
		playerRenderer.resize(width, height);
    }

}
