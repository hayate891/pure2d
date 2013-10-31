/**
 * 
 */
package com.funzio.pure2D.ui;

import java.util.List;

import android.graphics.PointF;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import com.funzio.pure2D.containers.Container;
import com.funzio.pure2D.containers.DisplayGroup;
import com.funzio.pure2D.particles.nova.NovaEmitter;
import com.funzio.pure2D.particles.nova.NovaFactory;

/**
 * @author long.ngo
 */
public class NovaGroup extends DisplayGroup {
    protected static final String TAG = NovaGroup.class.getSimpleName();

    protected static final String ATT_SOURCE = "source";
    protected static final String ATT_ASYNC = "async";
    protected static final String ATT_AUTO_PLAY = "autoStart";
    protected static final String ATT_PARAM_NUM = "paramNum";
    protected static final String ATT_CONTAINER = "container";

    protected NovaFactory mNovaFactory;
    protected boolean mNovaLoaded = false;
    protected boolean mAutoStart = true;
    protected boolean mStarted = false;

    protected String[] mParams;
    protected String mContainerId;

    public NovaGroup() {
        super();
    }

    @Override
    public boolean update(final int deltaTime) {
        // async support and diff check
        if (mAutoStart && !mStarted) {

            if (!mNovaLoaded && mNovaFactory != null && mNovaFactory.getNovaVO() != null) {
                mNovaLoaded = true; // flag now
            }

            if (mNovaLoaded) {
                addEmiters();
            }
        }

        return super.update(deltaTime);
    }

    @Override
    public void setXMLAttributes(final XmlPullParser xmlParser, final UIManager manager) {
        super.setXMLAttributes(xmlParser, manager);

        // nova params
        final String paramNum = xmlParser.getAttributeValue(null, ATT_PARAM_NUM);
        if (paramNum != null) {
            final int num = Integer.valueOf(paramNum);
            mParams = new String[num];
            for (int i = 0; i < num; i++) {
                mParams[i] = xmlParser.getAttributeValue(null, "param" + i);
            }
        }

        final String container = xmlParser.getAttributeValue(null, ATT_CONTAINER);
        if (container != null) {
            mContainerId = container;
        }

        final String autoPlay = xmlParser.getAttributeValue(null, ATT_AUTO_PLAY);
        if (autoPlay != null) {
            mAutoStart = Boolean.valueOf(autoPlay);
        }

        final String source = xmlParser.getAttributeValue(null, ATT_SOURCE);
        if (source != null) {
            final String async = xmlParser.getAttributeValue(null, ATT_ASYNC);
            mNovaFactory = manager.getTextureManager().getUriNova(source, async != null ? Boolean.valueOf(async) : UIConfig.DEFAULT_ASYNC); // async by default
            mNovaLoaded = (mNovaFactory.getNovaVO() != null);
        }
    }

    /**
     * Create and add the emitters to a specific container
     * 
     * @param x
     * @param y
     * @param params
     */
    protected List<NovaEmitter> addEmiters() {
        Log.v(TAG, "addEmitters()");

        // find container
        Container container = null;
        if (mContainerId != null && mContainerId.length() > 0) {
            if (mContainerId.equals(UIConfig.LAYER_PARENT)) {
                container = mParent;
            } else if (mContainerId.equals(UIConfig.LAYER_SCENE)) {
                container = getScene();
            } else if (getScene() != null) {
                container = (Container) getScene().getChildById(mContainerId);
            }
        }
        final Container finalContainer = container == null ? this : container;

        // find position
        PointF position = null;
        if (finalContainer != this) {
            final PointF global = new PointF(), newLocal = new PointF();
            // translate position to the container's
            mParent.localToGlobal(mPosition, global);
            finalContainer.globalToLocal(global, newLocal);
            position = newLocal;
        }

        final List<NovaEmitter> emitters = mNovaFactory.createEmitters(position, (Object[]) mParams);
        // null check first
        if (emitters == null) {
            Log.e(TAG, "Emiters not created!", new Exception());
            return null;
        }

        // flag
        mStarted = true;

        // queue to add later
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                final int size = emitters.size();
                for (int i = 0; i < size; i++) {
                    finalContainer.addChild(emitters.get(i));
                }
            }
        };
        if (!queueEvent(runnable)) {
            runnable.run();
        }

        return emitters;
    }

    public boolean isAutoStart() {
        return mAutoStart;
    }

    public void setAutoStart(final boolean autoPlay) {
        mAutoStart = autoPlay;
    }

    public boolean start() {
        if (!mNovaLoaded) {
            return false;
        }

        addEmiters();

        return true;
    }
}
