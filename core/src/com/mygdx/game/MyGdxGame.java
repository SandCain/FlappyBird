package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class  MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;//класс, при помощи которого управляется компьютерная графика
	Texture background;//изображение
	Texture[] birds;
	int birdStateFlag = 0;
	float flyHeight;
	int fallingSpeed = 0;
	int gameStateFlag = 0;

	Texture bottomTube;
	Texture topTube;
	int SpaceBetweenTubes = 500;

	Random random;
	int tubespeed = 5;
	int tubesNumber = 5;
	float distanceBetweenTubes;
	float tubeX[] = new float[tubesNumber];
	float tubeShift[] = new float[tubesNumber];


	Rectangle[] topTubesRectangles;
	Rectangle[] bottomTubesRectangles;
	Circle birdCircle;
	//ShapeRenderer shapeRenderer;

	int gameScore = 0;
	int pastTubeIndex;

	BitmapFont scoreFont;
	Texture gameOver;

	@Override
	public void create() {
		batch = new SpriteBatch();//пучек изображений
		background = new Texture("background.png");

		birds = new Texture[2];
		birds[0] = new Texture("bird_wings_up.png");
		birds[1] = new Texture("bird_wings_down.png");
		gameOver = new Texture("game_over.png");

		//shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();

		topTubesRectangles = new Rectangle[tubesNumber];
		bottomTubesRectangles = new Rectangle[tubesNumber];

		flyHeight = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		bottomTube = new Texture("bottom_tube.png");
		topTube = new Texture("top_tube.png");
		random = new Random();
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.CYAN);
		scoreFont.getData().setScale(10);
		distanceBetweenTubes = Gdx.graphics.getWidth() / 2;//между трубами расстояние пол экрана
		for(int i = 0; i<tubesNumber; i++){
			tubeX[i] = (Gdx.graphics.getWidth() / 2
					-topTube.getWidth() / 2 + Gdx.graphics.getWidth()+ i * distanceBetweenTubes);
			tubeShift[i] = (random.nextFloat() - 0.5f) *
					(Gdx.graphics.getHeight() - SpaceBetweenTubes - 200);
			topTubesRectangles[i]  =  new Rectangle();
			bottomTubesRectangles[i] = new Rectangle();
		}

	}

	@Override
	public void render() {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		if (Gdx.input.justTouched()) {
			Gdx.app.log("Tag", "Ooops");
			gameStateFlag = 1;
		}

		if (gameStateFlag == 1) {
			Gdx.app.log("GameScore", String.valueOf(gameScore));

			if(tubeX[pastTubeIndex] < Gdx.graphics.getWidth() / 2) {
				gameScore++;

				if (pastTubeIndex < tubesNumber - 1) {
					pastTubeIndex++;
				} else {
					pastTubeIndex = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				fallingSpeed = -30;
			}
			if (flyHeight > 0) {
				fallingSpeed++;
				flyHeight = flyHeight - fallingSpeed;
			}else {
				gameStateFlag = 2;
			}

		} else if(gameStateFlag == 0){
			if (Gdx.input.justTouched()) {
				Gdx.app.log("Tag", "Ooops");
				gameStateFlag = 1;
			}
		}else if(gameStateFlag ==2) {
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameOver.getHeight() /2);
		}

		for (int i = 0; i < tubesNumber; i++) {
			if(tubeX[i] < -topTube.getWidth()) {
				tubeX[i] = tubesNumber * distanceBetweenTubes;
			}else {
				tubeX[i] = tubeX[i] - tubespeed;
			}

		batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 +
				SpaceBetweenTubes / 2 + tubeShift[i]);
		batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 -
				SpaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i]);
		topTubesRectangles[i] = new Rectangle(tubeX[i],
				Gdx.graphics.getHeight() / 2 +
				SpaceBetweenTubes / 2 + tubeShift[i],
				topTube.getWidth(),
				topTube.getHeight());
		bottomTubesRectangles[i] = new Rectangle(tubeX[i],
				Gdx.graphics.getHeight() / 2 -
				SpaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i],
				bottomTube.getWidth(),
				bottomTube.getHeight());
	}



		if(birdStateFlag == 0){
			birdStateFlag = 1;
		}else {
			birdStateFlag = 0;
		}

		scoreFont.draw(batch, String.valueOf(gameScore), 100, 200);


		batch.draw(birds[birdStateFlag], Gdx.graphics.getWidth() / 2 - birds[birdStateFlag].getWidth() / 2,
				flyHeight);
		birdCircle.set(Gdx.graphics.getWidth() / 2,
				flyHeight + birds[birdStateFlag].getHeight() / 2,
				birds[birdStateFlag].getHeight() / 2);
		batch.end();
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.CYAN);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < tubesNumber; i++) {

//			shapeRenderer.rect(tubeX[i],
//					Gdx.graphics.getHeight() / 2 +
//							SpaceBetweenTubes / 2 + tubeShift[i],
//					topTube.getWidth(),
//					topTube.getHeight());
//			shapeRenderer.rect(tubeX[i],
//					Gdx.graphics.getHeight() / 2 -
//							SpaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i],
//					bottomTube.getWidth(),
//					bottomTube.getHeight());
			if(Intersector.overlaps(birdCircle, bottomTubesRectangles[i]) ||
					Intersector.overlaps(birdCircle, bottomTubesRectangles[i])) {
				Gdx.app.log("Tag", "Touch");
				gameStateFlag = 2;
			}

		}

		//shapeRenderer.end();
	}
	

}
