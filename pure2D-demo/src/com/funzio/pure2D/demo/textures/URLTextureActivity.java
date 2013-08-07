package com.funzio.pure2D.demo.textures;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.funzio.pure2D.Scene;
import com.funzio.pure2D.demo.activities.StageActivity;
import com.funzio.pure2D.demo.objects.Bouncer;
import com.funzio.pure2D.gl.gl10.GLState;
import com.funzio.pure2D.gl.gl10.textures.Texture;

public class URLTextureActivity extends StageActivity {
    private Texture mTexture;
    private PointF mTempPoint = new PointF();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // need to get the GL reference first
        mScene.setListener(new Scene.Listener() {

            @Override
            public void onSurfaceCreated(final GLState glState, final boolean firstTime) {
                if (firstTime) {
                    // load the textures
                    loadTexture(new Texture.Listener() {
                        @Override
                        public void onTextureLoad(final Texture texture) {
                            // create first obj
                            addObject(mDisplaySizeDiv2.x, mDisplaySizeDiv2.y);
                        }
                    });
                }
            }
        });
    }

    private void loadTexture(final Texture.Listener listener) {
        final String url = "http://product.gree.net/share/img/header-logo-01.png";

        // create texture
        // mTexture = mScene.getTextureManager().createURLTexture(url, null);
        // listener.onTextureLoad(mTexture);

        // or create texture async
        mTexture = mScene.getTextureManager().createURLTextureAsync(url, null);
        mTexture.setListener(listener);

    }

    private void addObject(final float x, final float y) {
        // convert from screen to scene's coordinates
        mScene.screenToGlobal(x, y, mTempPoint);

        // create object
        final Bouncer obj = new Bouncer();
        obj.setTexture(mTexture);

        // center origin
        // obj.setOriginAtCenter();

        // set positions
        obj.setPosition(mTempPoint.x, mTempPoint.y);

        // add to scene
        mScene.addChild(obj);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mStage.queueEvent(new Runnable() {

                @Override
                public void run() {
                    addObject(event.getX(), event.getY());
                }
            });
        }

        return true;
    }
}