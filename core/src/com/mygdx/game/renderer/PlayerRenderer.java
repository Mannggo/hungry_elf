package com.mygdx.game.renderer;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.google.gson.Gson;
import com.mygdx.game.constant.CameraConstants;
import com.mygdx.game.constant.MapConstants;
import com.mygdx.game.constant.PlayerConstants;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.dto.PlayerInfo;
import com.mygdx.game.dto.TransferInfo;
import com.mygdx.game.sprite.Floor;
import com.mygdx.game.sprite.SpeedUpBill;
import com.mygdx.game.sprite.Star;
import com.mygdx.game.udp.client.UDPClient;

/**
 * @author xiezd 2018-05-31 21:04
 */
public class PlayerRenderer implements Disposable{
	private String TAG = getClass().getSimpleName();
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private PlayerController playerController;
    private Sound catchAudio;
    private Sound speedUpAudio;
    private Gson gson;
    
    UDPClient client;

    public PlayerRenderer(PlayerController playerController) {
        this.playerController = playerController;
        gson = new Gson();
        init();
    }

    private void init() {
    	client = new UDPClient();
    	//开始接收消息
		new Thread(client).start();
		client.sendMessage(new Gson().toJson(playerController.transferInfo));
		playerController.transferInfo.getLoginInfo().setType("common");
    	Music bgm = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm_2.mp3"));
    	bgm.setLooping(true);
//    	bgm.play();
    	catchAudio = Gdx.audio.newSound(Gdx.files.internal("audio/catch.mp3"));
    	speedUpAudio = Gdx.audio.newSound(Gdx.files.internal("audio/speed_up.mp3"));
        batch = new SpriteBatch();
        camera = new OrthographicCamera(CameraConstants.VIEWPORT_WIDTH,
                CameraConstants.VIEWPORT_HEIGHT);
        camera.position.set(CameraConstants.VIEWPORT_WIDTH / 2.0f, CameraConstants.VIEWPORT_HEIGHT / 2.0f, 0);
        camera.update();
    }

    public void render() {
        update(Gdx.graphics.getDeltaTime());
        playerController.transferInfo.setBillInfos(client.transferInfo.getBillInfos());
        playerController.transferInfo.setStarInfos(client.transferInfo.getStarInfos());
        setMapInfo();
        renderItem(playerController.floors);
        renderItem(playerController.stars);
        renderItem(playerController.speedBills);
        renderPlayer();
        //发送更新后的数据
        client.sendMessage(gson.toJson(playerController.transferInfo));
        playerController.cameraHelper.applyTo(camera);
    }
    private void setMapInfo() {
		playerController.setStarInfos();
		playerController.setBillInfos();
		
	}

	public void resize(int width, int height) {
        camera.viewportWidth = (CameraConstants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerController.loginInfo.setType("logout");
        client.sendMessage(gson.toJson(playerController.transferInfo));
    }

    public void update(float deltaTime) {
        playerController.update(deltaTime);
    }
	
	private void renderItem(List<Sprite> list) {
		batch.begin();
		if (Objects.nonNull(list) && !list.isEmpty()) {
			Iterator<Sprite> itr = list.iterator();
			int i = 0;
	        out: while (itr.hasNext()) {
	        	Sprite s = itr.next();
	        	if (s instanceof Floor){
	        		s.draw(batch);
	        		continue out;
	        	}
	        	if (playerCatch(s)) {
	        		if (s instanceof SpeedUpBill) {
	        			playerController.playerSpeed = playerController.playerSpeed == PlayerConstants.STEP_LEVEL_HIGH ? PlayerConstants.STEP_LEVEL_HIGH : (playerController.playerSpeed + 1);
//	        			speedUpAudio.play();
	        			playerController.transferInfo.getBillInfos().remove(i);
	        		} else if (s instanceof Star) {
	        			playerController.playerInfo.setScore(playerController.playerInfo.getScore() + 1);
//	        			catchAudio.play();
	        			playerController.transferInfo.getStarInfos().remove(i);
	        		}
	        		itr.remove();
	        		
	        	}
	            s.draw(batch);
	            i ++;
	        }
		}
        batch.end();
	}
    private boolean playerCatch(Sprite s) {
		if (playerController.player.getX() + playerController.player.getOriginX() >= s.getX() &&  playerController.player.getX() - playerController.player.getOriginX() <= s.getX() 
				&& playerController.player.getY() + playerController.player.getOriginY() >= s.getY() && + playerController.player.getY() - playerController.player.getOriginY() <= s.getY())
			return true;
		return false;
	}

	private void renderPlayer() {
        handleInput();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        playerController.setPlayerRoom(client.transferInfo.getPlayerInfo());
        playerController.setPlayer2Info(client.transferInfo.getPlayerInfo());
        if (playerController.isPlayer2In) {
        	playerController.updatePlayer2Info(client.transferInfo.getPlayerInfo());
        	playerController.player2.draw(batch);
        }
        playerController.player.draw(batch);
        batch.end();
        
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerController.left();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerController.right();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerController.up();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerController.down();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        	playerController.focusPlayer();
        }
        playerController.playerInfo.setPositionX(playerController.player.getX());
        playerController.playerInfo.setPositionY(playerController.player.getY());
        
    }
}
