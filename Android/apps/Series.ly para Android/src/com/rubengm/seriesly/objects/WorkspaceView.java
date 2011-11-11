/**
 * Copyright 2010 Eric Taix (eric.taix@gmail.com) Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.rubengm.seriesly.objects;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;


/**
 * The workspace is a wide area with a infinite number of screens. Each screen contains a view. A workspace is meant to
 * be used with a fixed width only.<br/>
 * <br/>
 * This code has been done by using com.android.launcher.Workspace.java
 */
public class WorkspaceView extends ViewGroup {
	public static final String TAG = "WorkspaceView";
	private static final int INVALID_SCREEN = -1;
	public static final int DRAWABLE_LEFT = 0;
	public static final int DRAWABLE_TOP = 1;
	public static final int DRAWABLE_RIGHT = 2;
	public static final int DRAWABLE_BOTTOM = 3;

	// The velocity at which a fling gesture will cause us to snap to the next screen
	private static final int SNAP_VELOCITY = 750;

	// the default screen index
	private int defaultScreen;
	// The current screen index
	private int currentScreen;
	// The next screen index
	private int nextScreen = INVALID_SCREEN;
	// Wallpaper properties
	private Bitmap wallpaper;
	private Paint paint;
	private int wallpaperWidth;
	private int wallpaperHeight;
	private float wallpaperOffset;
	private boolean wallpaperLoaded;
	private boolean firstWallpaperLayout = true;

	// The scroller which scroll each view
	private Scroller scroller;
	// A tracker which to calculate the velocity of a mouvement
	private VelocityTracker mVelocityTracker;

	// Tha last known values of X and Y
	private float lastMotionX;
	private float lastMotionY;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	// The current touch state
	private int touchState = TOUCH_STATE_REST;
	// The minimal distance of a touch slop
	private int touchSlop;

	// An internal flag to reset long press when user is scrolling
	private boolean allowLongPress;
	// A flag to know if touch event have to be ignored. Used also in internal
	private boolean locked;
	private ArrayList<Button> tabs;
	private LinearLayout linearTabs;
	private int tabDrawable = -1;
	private int selectedTabDrawable = -1;
	private int tabFontColor = Color.BLACK;
	private int selectedTabFontColor = Color.BLACK;
	private int tabFontSize = 12;
	private boolean isHorizontal = true;

	/**
	 * Used to inflate the Workspace from XML.
	 * 
	 * @param context The application's context.
	 * @param attrs The attributes set containing the Workspace's customization values.
	 */
	public WorkspaceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		tabs = new ArrayList<Button>();
		nextScreen = defaultScreen;
	}

	/**
	 * Used to inflate the Workspace from XML.
	 * 
	 * @param context The application's context.
	 * @param attrs The attributes set containing the Workspace's customization values.
	 * @param defStyle Unused.
	 */
	public WorkspaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		tabs = new ArrayList<Button>();
		defaultScreen = 0;
		nextScreen = defaultScreen;
		initWorkspace();
	}

	/**
	 * Configura los tabs de navegacion para que se autoseleccionen al cambiar de pantalla
	 * 
	 * 100% opcional.
	 * 
	 * @param linear
	 * @param tabDrawable
	 * @param selectedTabDrawable
	 * @param tabFontColor
	 * @param selectedTabFontColor
	 * @param tabTitles
	 * @param drawables
	 */
	public void configTabs(LinearLayout linear, int tabDrawable, int selectedTabDrawable, int tabFontColor, int selectedTabFontColor, int tabFontSize, String[] tabTitles, boolean isHorizontal) {
		configTabs(linear, tabDrawable, selectedTabDrawable, tabFontColor, selectedTabFontColor, tabFontSize, isHorizontal);
		for(String s : tabTitles) {
			addTab(s, getContext());
		}
		redrawTabs(0);
	}

	/**
	 * Configura los tabs de navegacion para que se autoseleccionen al cambiar de pantalla
	 * 
	 * 100% opcional.
	 * 
	 * @param linear
	 * @param tabDrawable
	 * @param selectedTabDrawable
	 * @param tabFontColor
	 * @param selectedTabFontColor
	 * @param tabTitles
	 * @param drawables
	 */
	public void configTabs(LinearLayout linear, int tabDrawable, int selectedTabDrawable, int tabFontColor, int selectedTabFontColor, int tabFontSize, String[] tabTitles, int[] drawables, int drawablePosition, boolean isHorizontal) {
		configTabs(linear, tabDrawable, selectedTabDrawable, tabFontColor, selectedTabFontColor, tabFontSize, isHorizontal);
		for(int i = 0; i < tabTitles.length; i++) {
			addTab(tabTitles[i], drawables[i], drawablePosition, getContext());
		}
		redrawTabs(0);
	}

	public void configTabs(LinearLayout linear, int tabDrawable, int selectedTabDrawable, int tabFontColor, int selectedTabFontColor) {
		configTabs(linear, tabDrawable, selectedTabDrawable, tabFontColor, selectedTabFontColor, 12, true);
	}

	public void configTabs(LinearLayout linear, int tabDrawable, int selectedTabDrawable, int tabFontColor, int selectedTabFontColor, int tabFontSize, boolean isHorizontal) {
		setLinearTabs(linear);
		setTabDrawable(tabDrawable);
		setSelectedTabDrawable(selectedTabDrawable);
		setTabFontColor(tabFontColor);
		setSelectedTabFontColor(selectedTabFontColor);
		setTabFontSize(tabFontSize);
		setHorizontal(isHorizontal);
		redrawTabs(nextScreen);
	}

	public void addView(View child, String tabTitle, int drawable, int drawablePosition) {
		int actual = nextScreen;
		addView(child);
		addTab(tabTitle, drawable, drawablePosition, getContext());
		redrawTabs(actual);
	}

	public void addView(View child, String tabTitle) {
		int actual = nextScreen;
		addView(child);
		addTab(tabTitle, getContext());
		redrawTabs(actual);
	}

	public void setLinearTabs(LinearLayout l) {
		linearTabs = l;
	}

	public void setTabDrawable(int i) {
		tabDrawable = i;
	}

	public void setSelectedTabDrawable(int i) {
		selectedTabDrawable = i;
	}

	public void unlink() {
		if(linearTabs != null) {
			linearTabs.removeAllViews();
		}
		try{
			LinearLayout l = (LinearLayout)getParent();
			l.removeView(this);
		}catch(Exception ex) {
			Log.e(TAG, "Error unlink: " + ex.getLocalizedMessage());
		}
	}

	public void redrawTabs(int actual) {
		if(actual == -1) actual = 0;
		if(linearTabs != null) {
			linearTabs.removeAllViews();
			for(Button b : tabs) {
				if((Integer)b.getTag() == actual) {
					b.setBackgroundResource(selectedTabDrawable);
					b.setTextColor(selectedTabFontColor);
				} else {
					b.setBackgroundResource(tabDrawable);
					b.setTextColor(tabFontColor);
				}
				if(isHorizontal)
					b.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1));
				else
					b.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
				linearTabs.addView(b);
			}
		}
	}

	public void addTab(String s, Context context) {
		Log.i(TAG, "Addtab: " + s);
		final Button btn = new Button(context);
		btn.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		btn.setMarqueeRepeatLimit(-1);
		btn.setPadding(0, 2, 0, 8);
		if(isHorizontal)
			btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1));
		else
			btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		btn.setText(s);
		btn.setTextSize(tabFontSize);
		final int pos = tabs.size();
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				scrollToScreen(pos);
			}
		});
		tabs.add(btn);
	}

	public void addTab(String s, int drawable, int drawablePosition, Context context) {
		final Button btn = new Button(context);
		if(isHorizontal)
			btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1));
		else
			btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		if(s.length() > 0) {
			btn.setText(s);
			btn.setTextSize(tabFontSize);
			btn.setPadding(0, 2, 0, 8);
		}
		btn.setTag((Integer)tabs.size());
		switch(drawablePosition) {
		case DRAWABLE_LEFT:
			btn.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
			break;
		case DRAWABLE_TOP:
			btn.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);
			break;
		case DRAWABLE_RIGHT:
			btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
			break;
		case DRAWABLE_BOTTOM:
			btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable);
			break;
		}
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				scrollToScreen((Integer)btn.getTag());
			}
		});
		tabs.add(btn);
	}

	public void relocate() {
		scrollToScreen(currentScreen);
	}

	/**
	 * Initializes various states for this workspace.
	 */
	private void initWorkspace() {
		scroller = new Scroller(getContext());
		currentScreen = defaultScreen;

		paint = new Paint();
		paint.setDither(false);
		touchSlop = ViewConfiguration.getTouchSlop();
	}

	/**
	 * Set a new distance that a touch can wander before we think the user is scrolling in pixels slop<br/>
	 * 
	 * @param touchSlopP
	 */
	public void setTouchSlop(int touchSlopP) {
		touchSlop = touchSlopP;
	}

	/**
	 * Set the background's wallpaper.
	 */
	public void loadWallpaper(Bitmap bitmap) {
		wallpaper = bitmap;
		wallpaperLoaded = true;
		requestLayout();
		invalidate();
	}

	boolean isDefaultScreenShowing() {
		return currentScreen == defaultScreen;
	}

	/**
	 * Returns the index of the currently displayed screen.
	 * 
	 * @return The index of the currently displayed screen.
	 */
	public int getCurrentScreen() {
		return currentScreen;
	}

	/**
	 * Sets the current screen.
	 * 
	 * @param currentScreen
	 */
	void setCurrentScreen(int currentScreen) {
		currentScreen = Math.max(0, Math.min(currentScreen, getChildCount()));
		scrollTo(currentScreen * getWidth(), 0);
		invalidate();
	}

	/**
	 * Shows the default screen (defined by the firstScreen attribute in XML.)
	 */
	void showDefaultScreen() {
		setCurrentScreen(defaultScreen);
	}

	/**
	 * Registers the specified listener on each screen contained in this workspace.
	 * 
	 * @param l The listener used to respond to long clicks.
	 */
	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).setOnLongClickListener(l);
		}
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
		else if (nextScreen != INVALID_SCREEN) {
			currentScreen = nextScreen;
			nextScreen = INVALID_SCREEN;
		}
	}

	/**
	 * ViewGroup.dispatchDraw() supports many features we don't need: clip to padding, layout animation, animation
	 * listener, disappearing children, etc. The following implementation attempts to fast-track the drawing dispatch by
	 * drawing only what we know needs to be drawn.
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// First draw the wallpaper if needed
		if (wallpaper != null) {
			float x = getScrollX() * wallpaperOffset;
			if (x + wallpaperWidth < getRight() - getLeft()) {
				x = getRight() - getLeft() - wallpaperWidth;
			}
			canvas.drawBitmap(wallpaper, x, (float) ((getBottom() - getTop() - wallpaperHeight) / 2.0), paint);
		}
		// Determine if we need to draw every child or only the current screen
		boolean fastDraw = touchState != TOUCH_STATE_SCROLLING && nextScreen == INVALID_SCREEN;
		// If we are not scrolling or flinging, draw only the current screen
		if (fastDraw) {
			try {
				View v = getChildAt(currentScreen);
				drawChild(canvas, v, getDrawingTime());
			} catch (Exception e) {
				Log.e(TAG, "Error: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		else {
			final long drawingTime = getDrawingTime();
			// If we are flinging, draw only the current screen and the target screen
			if (nextScreen >= 0 && nextScreen < getChildCount() && Math.abs(currentScreen - nextScreen) == 1) {
				drawChild(canvas, getChildAt(currentScreen), drawingTime);
				drawChild(canvas, getChildAt(nextScreen), drawingTime);
			}
			else {
				// If we are scrolling, draw all of our children
				final int count = getChildCount();
				for (int i = 0; i < count; i++) {
					drawChild(canvas, getChildAt(i), drawingTime);
				}
			}
		}
	}

	/**
	 * Measure the workspace AND also children
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
		}

		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		// Compute wallpaper
		if (wallpaperLoaded) {
			wallpaperLoaded = false;
			wallpaper = centerToFit(wallpaper, width, MeasureSpec.getSize(heightMeasureSpec), getContext());
			wallpaperWidth = wallpaper.getWidth();
			wallpaperHeight = wallpaper.getHeight();
		}
		wallpaperOffset = wallpaperWidth > width ? (count * width - wallpaperWidth) / ((count - 1) * (float) width) : 1.0f;
		if (firstWallpaperLayout) {
			scrollTo(currentScreen * width, 0);
			firstWallpaperLayout = false;
		}
	}

	/**
	 * Overrided method to layout child
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int childLeft = 0;
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		if (direction == View.FOCUS_LEFT) {
			if (getCurrentScreen() > 0) {
				scrollToScreen(getCurrentScreen() - 1);
				return true;
			}
		}
		else if (direction == View.FOCUS_RIGHT) {
			if (getCurrentScreen() < getChildCount() - 1) {
				scrollToScreen(getCurrentScreen() + 1);
				return true;
			}
		}
		return super.dispatchUnhandledMove(focused, direction);
	}

	/**
	 * This method JUST determines whether we want to intercept the motion. If we return true, onTouchEvent will be called
	 * and we do the actual scrolling there.
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (locked) {
			return true;
		}

		/*
		 * Shortcut the most recurring case: the user is in the dragging state and he is moving his finger. We want to
		 * intercept this motion.
		 */
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (touchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			/*
			 * Locally do absolute value. mLastMotionX is set to the y value of the down event.
			 */
			final int xDiff = (int) Math.abs(x - lastMotionX);
			final int yDiff = (int) Math.abs(y - lastMotionY);
			boolean xMoved = xDiff > touchSlop;
			boolean yMoved = yDiff > touchSlop;

			if (xMoved || yMoved) {

				if (xMoved && !yMoved) {
					// Scroll if the user moved far enough along the X axis
					touchState = TOUCH_STATE_SCROLLING;
				}
				// Either way, cancel any pending longpress
				if (allowLongPress) {
					allowLongPress = false;
					// Try canceling the long press. It could also have been scheduled
					// by a distant descendant, so use the mAllowLongPress flag to block
					// everything
					final View currentView = getChildAt(currentScreen);
					currentView.cancelLongPress();
				}
			}
			break;

		case MotionEvent.ACTION_DOWN:
			// Remember location of down touch
			lastMotionX = x;
			lastMotionY = y;
			allowLongPress = true;

			/*
			 * If being flinged and user touches the screen, initiate drag; otherwise don't. mScroller.isFinished should be
			 * false when being flinged.
			 */
			touchState = scroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			touchState = TOUCH_STATE_REST;
			break;
		}

		/*
		 * The only time we want to intercept motion events is if we are in the drag mode.
		 */
		return touchState != TOUCH_STATE_REST;
	}

	/**
	 * Track the touch event
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (locked) {
			return true;
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished will be false if being flinged.
			 */
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}

			// Remember where the motion event started
			lastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (touchState == TOUCH_STATE_SCROLLING) {
				// Scroll to follow the motion event
				final int deltaX = (int) (lastMotionX - x);
				lastMotionX = x;

				if (deltaX < 0) {
					if (getScrollX() > 0) {
						scrollBy(Math.max(-getScrollX(), deltaX), 0);
					}
				}
				else if (deltaX > 0) {
					final int availableToScroll = getChildAt(getChildCount() - 1).getRight() - getScrollX() - getWidth();
					if (availableToScroll > 0) {
						scrollBy(Math.min(availableToScroll, deltaX), 0);
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (touchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();

				if (velocityX > SNAP_VELOCITY && currentScreen > 0) {
					// Fling hard enough to move left
					scrollToScreen(currentScreen - 1);
				}
				else if (velocityX < -SNAP_VELOCITY && currentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					scrollToScreen(currentScreen + 1);
				}
				else {
					snapToDestination();
				}

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			touchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			touchState = TOUCH_STATE_REST;
		}

		return true;
	}

	/**
	 * Scroll to the appropriated screen depending of the current position
	 */
	private void snapToDestination() {
		final int screenWidth = getWidth();
		final int whichScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;
		scrollToScreen(whichScreen);
	}

	/**
	 * Scroll to a specific screen
	 * 
	 * @param whichScreen
	 */
	public void scrollToScreen(int whichScreen) {
		//Log.d(TAG, "snapToScreen=" + whichScreen);

		boolean changingScreens = whichScreen != currentScreen;

		nextScreen = whichScreen;

		View focusedChild = getFocusedChild();
		if (focusedChild != null && changingScreens && focusedChild == getChildAt(currentScreen)) {
			focusedChild.clearFocus();
		}

		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta / 2));
		invalidate();
		redrawTabs(nextScreen);
		try{
			getChildAt(nextScreen).requestFocus();
		}catch(Exception ex) {
			Log.e(TAG, "Error scrollToScreen: " + ex.getLocalizedMessage());
		}
	}

	/**
	 * Return the parceable instance to be saved
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		final SavedState state = new SavedState(super.onSaveInstanceState());
		state.currentScreen = currentScreen;
		return state;
	}

	/**
	 * Restore the previous saved current screen
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		if (savedState.currentScreen != -1) {
			currentScreen = savedState.currentScreen;
		}
	}

	/**
	 * Scroll to the left right screen
	 */
	public void scrollLeft() {
		if (nextScreen == INVALID_SCREEN && currentScreen > 0 && scroller.isFinished()) {
			scrollToScreen(currentScreen - 1);
		}
	}

	/**
	 * Scroll to the next right screen
	 */
	public void scrollRight() {
		if (nextScreen == INVALID_SCREEN && currentScreen < getChildCount() - 1 && scroller.isFinished()) {
			scrollToScreen(currentScreen + 1);
		}
	}

	/**
	 * Return the screen's index where a view has been added to.
	 * 
	 * @param v
	 * @return
	 */
	public int getScreenForView(View v) {
		int result = -1;
		if (v != null) {
			ViewParent vp = v.getParent();
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (vp == getChildAt(i)) {
					return i;
				}
			}
		}
		return result;
	}

	/**
	 * Return a view instance according to the tag parameter or null if the view could not be found
	 * 
	 * @param tag
	 * @return
	 */
	public View getViewForTag(Object tag) {
		int screenCount = getChildCount();
		for (int screen = 0; screen < screenCount; screen++) {
			View child = getChildAt(screen);
			if (child.getTag() == tag) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Unlocks the SlidingDrawer so that touch events are processed.
	 * 
	 * @see #lock()
	 */
	public void unlock() {
		locked = false;
	}

	/**
	 * Locks the SlidingDrawer so that touch events are ignores.
	 * 
	 * @see #unlock()
	 */
	public void lock() {
		locked = true;
	}

	/**
	 * @return True is long presses are still allowed for the current touch
	 */
	public boolean allowLongPress() {
		return allowLongPress;
	}

	/**
	 * Move to the default screen
	 */
	public void moveToDefaultScreen() {
		scrollToScreen(defaultScreen);
		getChildAt(defaultScreen).requestFocus();
	}

	// ========================= INNER CLASSES ==============================

	/**
	 * A SavedState which save and load the current screen
	 */
	public static class SavedState extends BaseSavedState {
		int currentScreen = -1;

		/**
		 * Internal constructor
		 * 
		 * @param superState
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Private constructor
		 * 
		 * @param in
		 */
		private SavedState(Parcel in) {
			super(in);
			currentScreen = in.readInt();
		}

		/**
		 * Save the current screen
		 */
		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(currentScreen);
		}

		/**
		 * Return a Parcelable creator
		 */
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	// ======================== UTILITIES METHODS ==========================

	/**
	 * Return a centered Bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @param context
	 * @return
	 */
	static Bitmap centerToFit(Bitmap bitmap, int width, int height, Context context) {
		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();

		if (bitmapWidth < width || bitmapHeight < height) {
			// Normally should get the window_background color of the context
			int color = Integer.valueOf("FF191919", 16);
			Bitmap centered = Bitmap.createBitmap(bitmapWidth < width ? width : bitmapWidth, bitmapHeight < height ? height
					: bitmapHeight, Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(centered);
			canvas.drawColor(color);
			canvas.drawBitmap(bitmap, (width - bitmapWidth) / 2.0f, (height - bitmapHeight) / 2.0f, null);
			bitmap = centered;
		}
		return bitmap;
	}

	public void setTabFontColor(int tabFontColor) {
		this.tabFontColor = tabFontColor;
	}

	public int getTabFontColor() {
		return tabFontColor;
	}

	public void setSelectedTabFontColor(int selectedTabFontColor) {
		this.selectedTabFontColor = selectedTabFontColor;
	}

	public int getSelectedTabFontColor() {
		return selectedTabFontColor;
	}

	public void setTabFontSize(int tabFontSize) {
		this.tabFontSize = tabFontSize;
	}

	public int getTabFontSize() {
		return tabFontSize;
	}

	public void setHorizontal(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

}