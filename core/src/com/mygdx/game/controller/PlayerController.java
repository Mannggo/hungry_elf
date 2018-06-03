package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.camera.CameraHelper;
import com.mygdx.game.constant.CameraConstants;
import com.mygdx.game.constant.MapConstants;
import com.mygdx.game.constant.PlayerConstants;
import com.mygdx.game.sprite.Floor;
import com.mygdx.game.sprite.SpeedUpBill;
import com.mygdx.game.sprite.Star;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author xiezd 2018-05-31 20:01
 */
public class PlayerController extends InputAdapter {
	private String TAG = getClass().getSimpleName();
	private Properties properties = new Properties();
	public Sprite player;
	public ArrayList<Sprite> floors;
	public List<Sprite> stars;
	public ArrayList<Sprite> speedBills;
	public CameraHelper cameraHelper;
	public Integer playerSpeed;

	public PlayerController() {
		init();
	}

	private void init() {
		// 注册使用本对象作为监听器
		Gdx.input.setInputProcessor(this);
		initStar();
		initFloor();
		initPlayer();
		initSpeedBill();
		cameraHelper = new CameraHelper();
		// 初始化相机以玩家为中心
		cameraHelper.setTarget(player);
	}

	private void initSpeedBill() {
		Texture billTexture = new Texture(Gdx.files.internal("pic/speed_bill.png"));

		speedBills = new ArrayList();
		for (int a = 0; a < 10; a++) {
			SpeedUpBill bill = new SpeedUpBill(billTexture);
			bill.setSize(MapConstants.ITEM_WIDTH, MapConstants.ITEM_HEIGHT);
			float randomX = MathUtils.random(0f, MapConstants.LENGTH_OF_FLOOR_SIDE * MapConstants.FLOOR_X_TOTAL);
			float randomY = MathUtils.random(0f, MapConstants.LENGTH_OF_FLOOR_SIDE * MapConstants.FLOOR_Y_TOTAL);
			bill.setPosition(randomX, randomY);
			bill.setOrigin(bill.getWidth() / 2.0f, bill.getHeight() / 2.0f);
			speedBills.add(bill);
		}
		
	}

	private void initPlayer() {
		Texture texture = new Texture(Gdx.files.internal("pic/player_" + (int) (Math.random() * 10) + ".png"));
		player = new Sprite(texture);
		player.setSize(1.5f, 1.5f);
		player.setOrigin(player.getWidth() / 2.0f, player.getHeight() / 2.0f);
		player.setPosition(MathUtils.random(0f, MapConstants.FLOOR_X_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE),
				MathUtils.random(0f, MapConstants.FLOOR_Y_TOTAL * MapConstants.LENGTH_OF_FLOOR_SIDE));
		playerSpeed = PlayerConstants.STEP_LEVEL_LOW;

	}

	private void initFloor() {
		Texture floorTexture = new Texture(Gdx.files.internal("pic/floor_1.png"));

		floors = new ArrayList();
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

	private void initStar() {
		Texture starTexture = new Texture(Gdx.files.internal("pic/starGold.png"));
		stars = new ArrayList();
		for (int a = 0; a < MapConstants.STAR_TOTAL; a++) {
			Star star = new Star(starTexture);
			float randomX = MathUtils.random(0f, MapConstants.LENGTH_OF_FLOOR_SIDE * MapConstants.FLOOR_X_TOTAL);
			float randomY = MathUtils.random(0f, MapConstants.LENGTH_OF_FLOOR_SIDE * MapConstants.FLOOR_Y_TOTAL);
			star.setPosition(randomX, randomY);
			star.setSize(MapConstants.ITEM_WIDTH, MapConstants.ITEM_HEIGHT);
			star.setOrigin(star.getWidth() / 2.0f, star.getHeight() / 2.0f);
			stars.add(star);
		}

	}

	public void update(float deltaTime) {
		updateStar(deltaTime);
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
		Gdx.app.debug(TAG, "user pressed key :【" + Keys.toString(keycode) + "】");
		// Reset game world
		if (keycode == Keys.R) {
			init();
		} else if (keycode == Keys.PAGE_UP) {
			playerSpeed = playerSpeed == PlayerConstants.STEP_LEVEL_HIGH ? PlayerConstants.STEP_LEVEL_HIGH : (playerSpeed + 1);
		} else if (keycode == Keys.PAGE_DOWN) {
			playerSpeed = playerSpeed == PlayerConstants.STEP_LEVEL_LOW ? PlayerConstants.STEP_LEVEL_LOW : (playerSpeed - 1);
		} else if (keycode == Keys.ENTER) {
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
}
