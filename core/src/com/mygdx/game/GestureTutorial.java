package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GestureTutorial extends ApplicationAdapter implements GestureDetector.GestureListener {
    SpriteBatch batch;
    Sprite sprite;
    OrthographicCamera orthoCam;
    GestureDetector gestureDetector;

    @Override
    public void create() {
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("storm_trooper.png")));
        sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
        sprite.setCenter(0.5f, 0.5f);
        orthoCam = new OrthographicCamera(1280, 720);
        orthoCam.update();
        gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(orthoCam.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public boolean touchDown(float fX, float fY, int nPointer, int nButton) {
        return false;
    }

    @Override
    public boolean tap(float fX, float fY, int nCount, int nButton) {
        if (nCount > 1) {
            sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
            sprite.setSize(256f, 256f);
            sprite.setRotation(0f);
        } else {
            Vector3 touchPos = new Vector3(fX, fY, 0);
            orthoCam.unproject(touchPos);
            sprite.setPosition(touchPos.x, touchPos.y);
        }
        return true;
    }

    @Override
    public boolean longPress(float fX, float fY) {
        Vector3 touchPos = new Vector3(fX, fY, 0);
        orthoCam.unproject(touchPos);
        if (sprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
            float alpha = sprite.getColor().a;
            if (alpha >= 0.f)
                sprite.setAlpha(alpha - 0.25f);
            else
                sprite.setAlpha(1f);
        }
        return true;
    }

    @Override
    public boolean fling(float fVelocityX, float fVelocityY, int nButton) {
        return false;
    }

    @Override
    public boolean pan(float fX, float fY, float fDeltaX, float fDeltaY) {
        Vector3 touchPos = new Vector3(fX, fY, 0);
        orthoCam.unproject(touchPos);
        sprite.setPosition(touchPos.x - sprite.getWidth() / 2, touchPos.y - sprite.getHeight() / 2);
        return true;
    }

    @Override
    public boolean panStop(float fX, float fY, int nPointer, int nButton) {
        return false;
    }

    @Override
    public boolean zoom(float fInitialDistance, float fDistance) {
        sprite.setSize(fDistance, fDistance);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        float fDeltaX = pointer2.x - pointer1.x;
        float fDeltaY = pointer2.y - pointer1.y;
        float fAngle = (float) Math.atan2((double) fDeltaY, (double) fDeltaX) * MathUtils.radiansToDegrees;
        fAngle += 90f;
        if (fAngle < 0)
            fAngle = 360f - (-fAngle);
        sprite.setRotation(-fAngle);
        return true;
    }
}