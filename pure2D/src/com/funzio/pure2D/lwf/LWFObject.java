package com.funzio.pure2D.lwf;

import android.util.Log;

import com.funzio.pure2D.BaseDisplayObject;
import com.funzio.pure2D.InvalidateFlags;
import com.funzio.pure2D.gl.gl10.GLState;

public class LWFObject extends BaseDisplayObject {
    private static final String TAG = LWFData.class.getSimpleName();

    private LWFData mData;
    private int mId;
    private long mPtr;

    private native int create(int lwfDataId);
    private native long getPointer(int lwfId);
    private native void destroy(int lwfId);
    private native void exec(long ptr, float tick);
    private native void render(long ptr);

    public LWFObject(LWFData data) {
        mId = create(data.getId());
        if (mId < 0)
            return;
        mPtr = getPointer(mId);
        mData = data;
    }

    @Override
    public boolean update(final int deltaTime) {
        super.update(deltaTime);
        if (mId < 0)
            return false;
        exec(mPtr, (float)deltaTime / 1000.f);
        invalidate(InvalidateFlags.VISUAL);
        return true;
    }

    @Override
    protected boolean drawChildren(final GLState glState) {
        if (mId < 0)
            return false;
        render(mPtr);

        // VBO

        return true;
    }

    public void dispose() {
        destroy(mId);
        mId = -1;
        mPtr = 0;
        mData = null;
    }
}
