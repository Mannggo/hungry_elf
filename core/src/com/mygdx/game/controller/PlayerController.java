package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;
import com.mygdx.game.camera.CameraHelper;
import com.mygdx.game.constant.CameraConstants;
import com.mygdx.game.constant.MapConstants;
import com.mygdx.game.constant.PlayerConstants;
import com.mygdx.game.dto.BillInfo;
import com.mygdx.game.dto.LoginInfo;
import com.mygdx.game.dto.PlayerInfo;
import com.mygdx.game.dto.StarInfo;
import com.mygdx.game.dto.TransferInfo;
import com.mygdx.game.sprite.Floor;
import com.mygdx.game.sprite.SpeedUpBill;
import com.mygdx.game.sprite.Star;
import com.mygdx.game.udp.client.UDPClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author xiezd 2018-05-31 20:01
 */
public class PlayerController extends InputAdapter {
	private String TAG = getClass().getSimpleName();
	private Properties properties = new Properties();
	public Sprite player;
	public Sprite player2;
	public ArrayList<Sprite> floors;
	public List<Sprite> stars;
	public ArrayList<Sprite> speedBills;
	public CameraHelper cameraHelper;
	public Integer playerSpeed;
	
	
	public boolean isPlayer2In;
	public TransferInfo transferInfo;
	public LoginInfo loginInfo;
	public PlayerInfo playerInfo;
	public PlayerInfo player2Info;

	public PlayerController() {
		init();
	}

	private void init() {
		isPlayer2In = false;
		// 注册使用本对象作为监听器
		Gdx.input.setInputProcessor(this);
		initFloor();
		initPlayer();
		cameraHelper = new CameraHelper();
		// 初始化相机以玩家为中心
		cameraHelper.setTarget(player);
	}


	private void initPlayer() {
		String avatarNum = String.valueOf((int)(Math.random() *10));
		player2 = null;
		Texture texture = new Texture(Gdx.files.internal("pic/player_" + avatarNum + ".png"));
		player = new Sprite(texture);
		player.setSize(PlayerConstants.PLAYER_SIZE_WIDTH, PlayerConstants.PLAYER_SIZE_HEIGHT);
		player.setOrigin(player.getWidth() / 2.0f, player.getHeight() / 2.0f);
		player.setPosition(MathUtils.random(0f, MapConstants.FLOOR_X_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE),
				MathUtils.random(0f, MapConstants.FLOOR_Y_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE));
		playerSpeed = PlayerConstants.STEP_LEVEL_LOW;
		playerInfo = new PlayerInfo();
		playerInfo.setElfAvatar(avatarNum);
		playerInfo.setPositionX(player.getX());
		playerInfo.setPositionY(player.getY());
		playerInfo.setScore(0);
		loginInfo = new LoginInfo("", "login");
		transferInfo = new TransferInfo(playerInfo, loginInfo);
		
	}

	private void initFloor() {
		Texture floorTexture = new Texture(Gdx.files.internal("pic/floor_1.png"));

		floors = new ArrayList<Sprite>();
		for (int a = 0; a < MapConstants.FLOOR_X_TOTAL * MapConstants.FLOOR_Y_TOTAL; a++) {
			Floor floor = new Floor(floorTexture);
			floor.setSize(MapConstants.LENGTH_OF_FLOOR_SIDE, MapConstants.LENGTH_OF_FLOOR_SIDE);
			floor.setPosition(a % MapConstants.FLOOR_X_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE,
					a / MapConstants.FLOOR_Y_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE);
			floor.setOrigin(floor.getWidth() / MapConstants.LENGTH_OF_FLOOR_SIDE,
					floor.getHeight() / MapConstants.LENGTH_OF_FLOOR_SIDE);
			floors.add(floor);
		}

	}

	public void update(float deltaTime) {
		
		//updateStar(deltaTime);
		handleDebugInput(deltaTime);
		cameraHelper.update(deltaTime);
	}

	private void updateStar(float deltaTime) {
		for (Sprite sp : stars) {
			float rotationTest = sp.getRotation();
			rotationTest += 180 * deltaTime;
			rotationTest %= 360;
			sp.setRotation(rotationTest);
		}
	}

	/*
	 * 玩家移动相关方法
	 */
	public void left() {
		if (player.getX() <= 0)
			return;
		player.setX(player.getX() - PlayerConstants.SPEED_MAP.get(playerSpeed));
		
	}

	public void right() {
		if (player.getX() >= MapConstants.FLOOR_X_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE
				- PlayerConstants.PLAYER_SIZE_WIDTH)
			return;
		player.setX(player.getX() + PlayerConstants.SPEED_MAP.get(playerSpeed));
	}

	public void up() {
		if (player.getY() >= MapConstants.FLOOR_Y_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE
				- PlayerConstants.PLAYER_SIZE_HEIGHT)
			return;
		player.setY(player.getY() + PlayerConstants.SPEED_MAP.get(playerSpeed));
	}

	public void down() {
		if (player.getY() <= 0)
			return;
		player.setY(player.getY() - PlayerConstants.SPEED_MAP.get(playerSpeed));
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.ENTER) {
			// 切换是否相机跟随
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : player);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		cameraHelper.addZoom(amount * CameraConstants.CAMERA_SCROLL_STEP);
		return false;
	}

	/**
	 * 相机焦点放到玩家身上
	 */
	public void focusPlayer() {
		cameraHelper.setPosition(player.getX() + player.getOriginX(), player.getY() + player.getOriginY());
	}

	private void handleDebugInput(float deltaTime) {
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP))
			moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			moveCamera(0, -camMoveSpeed);
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	public void setPlayer2Info(PlayerInfo player2Info) {
		if (Objects.nonNull(player2Info) && Objects.nonNull(player2Info.getPositionX())) {
			this.player2Info = player2Info;
			isPlayer2In = true;
			Texture texture = new Texture(Gdx.files.internal("pic/player_" + player2Info.getElfAvatar() + ".png"));
			
			player2 = new Sprite(texture);
			player2.setX(player2Info.getPositionX());
			player2.setY(player2Info.getPositionY());
			player2.setSize(PlayerConstants.PLAYER_SIZE_WIDTH, PlayerConstants.PLAYER_SIZE_HEIGHT);
		} else {
			return;
		}
	}

	public void updatePlayer2Info(PlayerInfo player2Info) {
		this.player2Info = player2Info;
		player2.setX(player2Info.getPositionX());
		player2.setY(player2Info.getPositionY());
		
	}

	public void setPlayerRoom(PlayerInfo roomInfo) {
		playerInfo.setRoomNumber(roomInfo.getRoomNumber());
		
	}

	public void setStarInfos() {
		if (Objects.isNull(transferInfo.getStarInfos())) {
			return;
		}
		Texture starTexture = new Texture(Gdx.files.internal("pic/starGold.png"));
		stars = new ArrayList<Sprite>();
		for (StarInfo starInfo : transferInfo.getStarInfos()) {
			Star star = new Star(starTexture);
			star.setPosition(starInfo.getPositionX(), starInfo.getPositionY());
			star.setSize(MapConstants.ITEM_WIDTH, MapConstants.ITEM_HEIGHT);
			star.setOrigin(star.getWidth() / 2.0f, star.getHeight() / 2.0f);
			stars.add(star);
		}
		
	}

	public void setBillInfos() {
		if (Objects.isNull(transferInfo.getBillInfos())) {
			return;
		}
		Texture billTexture = new Texture(Gdx.files.internal("pic/speed_bill.png"));

		speedBills = new ArrayList<Sprite>();
		for (BillInfo billInfo : transferInfo.getBillInfos()) {
			SpeedUpBill bill = new SpeedUpBill(billTexture);
			bill.setSize(MapConstants.ITEM_WIDTH, MapConstants.ITEM_HEIGHT);
			bill.setPosition(billInfo.getPositionX(), billInfo.getPositionY());
			bill.setOrigin(bill.getWidth() / 2.0f, bill.getHeight() / 2.0f);
			speedBills.add(bill);
		}
		
	}
}
