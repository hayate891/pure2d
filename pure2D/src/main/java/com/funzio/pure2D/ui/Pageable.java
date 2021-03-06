/**
 * ****************************************************************************
 * Copyright (C) 2012-2014 GREE, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ****************************************************************************
 */
/**
 *
 */
package com.funzio.pure2D.ui;

import android.view.KeyEvent;

import com.funzio.pure2D.DisplayObject;

/**
 * @author long.ngo
 */
public interface Pageable extends DisplayObject {

    public boolean isPageFloating();

    public void setPageFloating(final boolean pageFloating);

    public void transitionIn(final boolean pushing);

    public void transitionOut(final boolean pushing);

    public void setTransitionListener(final TransitionListener listener);

    public boolean isDismissible();

    public void setDismissible(final boolean value);

    public TransitionListener getTransitionListener();

    public boolean isPageActive();

    public boolean isModal();

    public boolean onKeyDown(final int keyCode, final KeyEvent event);

    public boolean onKeyUp(final int keyCode, final KeyEvent event);

    public boolean onBackPressed();

    public void onDismiss();

    public static interface TransitionListener {
        public void onTransitionInComplete(final Pageable page);

        public void onTransitionOutComplete(final Pageable page);
    }

}
