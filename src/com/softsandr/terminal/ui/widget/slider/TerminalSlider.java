package com.softsandr.terminal.ui.widget.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.softsandr.terminal.R;

/**
 * Date: 12/6/13
 *
 * @author Drachuk O.V.
 */
public class TerminalSlider extends ViewGroup {
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    private static final int TAP_THRESHOLD = 6;
    private static final float MAXIMUM_TAP_VELOCITY = 100.0f;
    private static final float MAXIMUM_MINOR_VELOCITY = 150.0f;
    private static final float MAXIMUM_MAJOR_VELOCITY = 200.0f;
    private static final float MAXIMUM_ACCELERATION = 2000.0f;
    private static final int VELOCITY_UNITS = 1000;
    private static final int MSG_ANIMATE = 1000;
    private static final int ANIMATION_FRAME_DURATION = 1000 / 60;
    private static final int EXPANDED_FULL_OPEN = -10001;
    private static final int COLLAPSED_FULL_CLOSED = -10002;
    private final int mHandleId;
    private final int mContentId;
    private final Rect mFrame = new Rect();
    private final Rect mInvalidate = new Rect();
    private final Handler mHandler = new SlidingHandler();
    private final int mTapThreshold;
    private final int mMaximumTapVelocity;
    private final int mMaximumMinorVelocity;
    private final int mMaximumMajorVelocity;
    private final int mMaximumAcceleration;
    private final int mVelocityUnits;
    private View mHandle;
    private View mContent;
    private boolean mTracking;
    private boolean mLocked;
    private VelocityTracker mVelocityTracker;
    private boolean mVertical;
    private boolean mExpanded;
    private int mSliderOffset;
    private int mHandleHeight;
    private int mHandleWidth;
    private OnDrawerOpenListener mOnDrawerOpenListener;
    private OnDrawerCloseListener mOnDrawerCloseListener;
    private OnDrawerScrollListener mOnDrawerScrollListener;
    private float mAnimatedAcceleration;
    private float mAnimatedVelocity;
    private float mAnimationPosition;
    private long mAnimationLastTime;
    private long mCurrentAnimationTime;
    private int mTouchDelta;
    private boolean mAnimating;

    /**
     * Creates a new TerminalSlider from a specified set of attributes defined in XML.
     *
     * @param context The application's environment.
     * @param attrs   The attributes defined in XML.
     */
    public TerminalSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Creates a new TerminalSlider from a specified set of attributes defined in XML.
     *
     * @param context  The application's environment.
     * @param attrs    The attributes defined in XML.
     * @param defStyle The style to apply to this widget.
     */
    public TerminalSlider(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TerminalSlider, defStyle, 0);

        int orientation = a.getInt(R.styleable.TerminalSlider_orientation, ORIENTATION_VERTICAL);
        mVertical = orientation == ORIENTATION_VERTICAL;
        mSliderOffset = (int) a.getDimension(R.styleable.TerminalSlider_offset, 0.0f);

        int handleId = a.getResourceId(R.styleable.TerminalSlider_handle, 0);
        if (handleId == 0) {
            throw new IllegalArgumentException("The handle attribute is required and must refer "
                    + "to a valid child.");
        }

        int contentId = a.getResourceId(R.styleable.TerminalSlider_content, 0);
        if (contentId == 0) {
            throw new IllegalArgumentException("The content attribute is required and must refer "
                    + "to a valid child.");
        }

        if (handleId == contentId) {
            throw new IllegalArgumentException("The content and handle attributes must refer "
                    + "to different children.");
        }

        mHandleId = handleId;
        mContentId = contentId;

        final float density = getResources().getDisplayMetrics().density;
        mTapThreshold = (int) (TAP_THRESHOLD * density + 0.5f);
        mMaximumTapVelocity = (int) (MAXIMUM_TAP_VELOCITY * density + 0.5f);
        mMaximumMinorVelocity = (int) (MAXIMUM_MINOR_VELOCITY * density + 0.5f);
        mMaximumMajorVelocity = (int) (MAXIMUM_MAJOR_VELOCITY * density + 0.5f);
        mMaximumAcceleration = (int) (MAXIMUM_ACCELERATION * density + 0.5f);
        mVelocityUnits = (int) (VELOCITY_UNITS * density + 0.5f);

        a.recycle();

        setAlwaysDrawnWithCacheEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        mHandle = findViewById(mHandleId);
        if (mHandle == null) {
            throw new IllegalArgumentException("The handle attribute is must refer to an"
                    + " existing child.");
        }
        mHandle.setOnClickListener(new DrawerToggler());

        mContent = findViewById(mContentId);
        if (mContent == null) {
            throw new IllegalArgumentException("The content attribute is must refer to an"
                    + " existing child.");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == View.MeasureSpec.UNSPECIFIED || heightSpecMode == View.MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("TerminalSlider cannot have UNSPECIFIED dimensions");
        }

        final View handle = mHandle;
        measureChild(handle, widthMeasureSpec, heightMeasureSpec);

        if (mVertical) {
            int height = heightSpecSize - handle.getMeasuredHeight() - mSliderOffset;
            mContent.measure(View.MeasureSpec.makeMeasureSpec(widthSpecSize, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        } else {
            int width = widthSpecSize - handle.getMeasuredWidth() - mSliderOffset;
            mContent.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(heightSpecSize, View.MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final long drawingTime = getDrawingTime();
        final View handle = mHandle;
        drawChild(canvas, handle, drawingTime);
        if (!mExpanded && !mTracking && !mAnimating) {
            drawChild(canvas, mContent, drawingTime);
        }
        final boolean isVertical = mVertical;
        if (mTracking || mAnimating) {
            canvas.save();
            canvas.translate(isVertical ? 0 : handle.getLeft() - mSliderOffset,
                    isVertical ? handle.getTop() - mSliderOffset : 0);
            drawChild(canvas, mContent, drawingTime);
            canvas.restore();
        } else if (mExpanded) {
            drawChild(canvas, mContent, drawingTime);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mTracking) {
            return;
        }
        final int width = r - l;
        final int height = b - t;
        final View handle = mHandle;
        int childWidth = handle.getMeasuredWidth();
        int childHeight = handle.getMeasuredHeight();
        int childLeft;
        int childTop;
        final View content = mContent;
        if (mVertical) {
            childLeft = (width - childWidth) / 2;
            if (mExpanded) {
                childTop = mSliderOffset;
                content.layout(0, mSliderOffset + childHeight, content.getMeasuredWidth(),
                        mSliderOffset + childHeight + content.getMeasuredHeight());
            } else {
                childTop =  height - childHeight - mSliderOffset;
                content.layout(0, height - mSliderOffset, content.getMeasuredWidth(),
                        height);
            }
        } else {
            childTop = (height - childHeight) / 2;
            if (mExpanded) {
                childLeft = mSliderOffset;
                content.layout(mSliderOffset + childWidth, 0,
                        mSliderOffset + childWidth + content.getMeasuredWidth(),
                        content.getMeasuredHeight());
            } else {
                childLeft = width - childWidth - mSliderOffset;
                content.layout(width - mSliderOffset, 0, width,
                        content.getMeasuredHeight());
            }
        }
        handle.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        mHandleHeight = handle.getHeight();
        mHandleWidth = handle.getWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mLocked) {
            return false;
        }
        final int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        final Rect frame = mFrame;
        final View handle = mHandle;
        handle.getHitRect(frame);
        if (!mTracking && !frame.contains((int) x, (int) y)) {
            return false;
        }
        if (action == MotionEvent.ACTION_DOWN) {
            mTracking = true;
//            handle.setPressed(true);
            // Must be called before prepareTracking()
            prepareContent();
            // Must be called after prepareContent()
            if (mOnDrawerScrollListener != null) {
                mOnDrawerScrollListener.onScrollStarted();
            }
            if (mVertical) {
                final int top = mHandle.getTop();
                mTouchDelta = (int) y - top;
                prepareTracking(top);
            } else {
                final int left = mHandle.getLeft();
                mTouchDelta = (int) x - left;
                prepareTracking(left);
            }
            mVelocityTracker.addMovement(event);
        }
        moveHandle((int) (mVertical ? event.getY() : event.getX()) - mTouchDelta);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLocked) {
            return true;
        }
        if (mTracking) {
            mVelocityTracker.addMovement(event);
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    moveHandle((int) (mVertical ? event.getY() : event.getX()) - mTouchDelta);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(mVelocityUnits);

                    float yVelocity = velocityTracker.getYVelocity();
                    float xVelocity = velocityTracker.getXVelocity();
                    boolean negative;

                    final boolean vertical = mVertical;
                    if (vertical) {
                        negative = yVelocity < 0;
                        if (xVelocity < 0) {
                            xVelocity = -xVelocity;
                        }
                        if (xVelocity > mMaximumMinorVelocity) {
                            xVelocity = mMaximumMinorVelocity;
                        }
                    } else {
                        negative = xVelocity < 0;
                        if (yVelocity < 0) {
                            yVelocity = -yVelocity;
                        }
                        if (yVelocity > mMaximumMinorVelocity) {
                            yVelocity = mMaximumMinorVelocity;
                        }
                    }
                    float velocity = (float) Math.hypot(xVelocity, yVelocity);
                    if (negative) {
                        velocity = -velocity;
                    }
                    final int top = mHandle.getTop();
                    final int left = mHandle.getLeft();

                    if (Math.abs(velocity) < mMaximumTapVelocity) {
                        if (vertical ? (mExpanded && top < mTapThreshold + mSliderOffset) ||
                                (!mExpanded && top > getBottom() - getTop() -
                                        mHandleHeight - mTapThreshold) :
                                (mExpanded && left < mTapThreshold + mSliderOffset) ||
                                        (!mExpanded && left > getRight() - getLeft() -
                                                mHandleWidth - mTapThreshold)) {
                            performFling(vertical ? top : left, velocity, false);
                        } else {
                            performFling(vertical ? top : left, velocity, false);
                        }
                    } else {
                        performFling(vertical ? top : left, velocity, false);
                    }
                }
                break;
            }
        }
        return mTracking || mAnimating || super.onTouchEvent(event);
    }

    private void animateClose(int position) {
        prepareTracking(position);
        performFling(position, mMaximumAcceleration, true);
    }

    private void animateOpen(int position) {
        prepareTracking(position);
        performFling(position, -mMaximumAcceleration, true);
    }

    private void performFling(int position, float velocity, boolean always) {
        mAnimationPosition = position;
        mAnimatedVelocity = velocity;
        if (mExpanded) {
            if (always || (velocity > mMaximumMajorVelocity ||
                    (position > mSliderOffset + (mVertical ? mHandleHeight : mHandleWidth) &&
                            velocity > -mMaximumMajorVelocity))) {
                // We are expanded, but they didn't move sufficiently to cause
                // us to retract.  Animate back to the expanded position.
                mAnimatedAcceleration = mMaximumAcceleration;
                if (velocity < 0) {
                    mAnimatedVelocity = 0;
                }
            } else {
                // We are expanded and are now going to animate away.
                mAnimatedAcceleration = -mMaximumAcceleration;
                if (velocity > 0) {
                    mAnimatedVelocity = 0;
                }
            }
        } else {
            if (!always && (velocity > mMaximumMajorVelocity ||
                    (position > (mVertical ? getHeight() : getWidth()) / 2 &&
                            velocity > -mMaximumMajorVelocity))) {
                // We are collapsed, and they moved enough to allow us to expand.
                mAnimatedAcceleration = mMaximumAcceleration;
                if (velocity < 0) {
                    mAnimatedVelocity = 0;
                }
            } else {
                // We are collapsed, but they didn't move sufficiently to cause
                // us to retract.  Animate back to the collapsed position.
                mAnimatedAcceleration = -mMaximumAcceleration;
                if (velocity > 0) {
                    mAnimatedVelocity = 0;
                }
            }
        }
        long now = SystemClock.uptimeMillis();
        mAnimationLastTime = now;
        mCurrentAnimationTime = now + ANIMATION_FRAME_DURATION;
        mAnimating = true;
        mHandler.removeMessages(MSG_ANIMATE);
        mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE), mCurrentAnimationTime);
        stopTracking();
    }

    private void moveHandle(int position) {
        final View handle = mHandle;
        if (mVertical) {
            if (position == EXPANDED_FULL_OPEN) {
                handle.offsetTopAndBottom(mSliderOffset);
                invalidate();
            } else if (position == COLLAPSED_FULL_CLOSED) {
                handle.offsetTopAndBottom(getHeight() - mSliderOffset - mHandleHeight);
                invalidate();
            } else {
                if (position <= getHeight() - mHandleHeight - mSliderOffset) {
                    final int top = handle.getTop();
                    int deltaY = position - top;
                    if (position < mSliderOffset) {
                        deltaY = mSliderOffset - top;
                    } else if (deltaY > getHeight() - mHandleHeight - top) {
                        deltaY = getHeight() - mHandleHeight - top;
                    }
                    handle.offsetTopAndBottom(deltaY);

                    final Rect frame = mFrame;
                    final Rect region = mInvalidate;

                    handle.getHitRect(frame);
                    region.set(frame);

                    region.union(frame.left, frame.top - deltaY, frame.right, frame.bottom - deltaY);
                    region.union(0, frame.bottom - deltaY, getWidth(),
                            frame.bottom - deltaY + mContent.getHeight());

                    invalidate(region);
                }
            }
        } else {
            if (position == EXPANDED_FULL_OPEN) {
                handle.offsetLeftAndRight(mSliderOffset);
                invalidate();
            } else if (position == COLLAPSED_FULL_CLOSED) {
                handle.offsetLeftAndRight(getWidth() - mSliderOffset - mHandleWidth);
                invalidate();
            } else {
                if (position <= getWidth() - mHandleWidth - mSliderOffset) {
                    final int left = handle.getLeft();
                    int deltaX = position - left;
                    if (position < mSliderOffset) {
                        deltaX = mSliderOffset - left;
                    } else if (deltaX > getWidth() - mHandleWidth - left) {
                        deltaX = getWidth() - mHandleWidth - left;
                    }
                    handle.offsetLeftAndRight(deltaX);

                    final Rect frame = mFrame;
                    final Rect region = mInvalidate;

                    handle.getHitRect(frame);
                    region.set(frame);

                    region.union(frame.left - deltaX, frame.top, frame.right - deltaX, frame.bottom);
                    region.union(frame.right - deltaX, 0,
                            frame.right - deltaX + mContent.getWidth(), getHeight());

                    invalidate(region);
                }
            }
        }
    }

    private void prepareContent() {
        if (mAnimating) {
            return;
        }
        // Something changed in the content, we need to honor the layout request
        // before creating the cached bitmap
        final View content = mContent;
        if (mVertical) {
            final int childHeight = mHandleHeight;
            int height = getWidth() - childHeight - mSliderOffset;
            content.measure(View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
            content.layout(0, mSliderOffset + childHeight, content.getMeasuredWidth(),
                    mSliderOffset + childHeight + content.getMeasuredHeight());
        } else {
            final int childWidth = mHandle.getWidth();
            int width = getWidth() - childWidth - mSliderOffset;
            content.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(getHeight(), View.MeasureSpec.EXACTLY));
            content.layout(childWidth + mSliderOffset, 0,
                    mSliderOffset + childWidth + content.getMeasuredWidth(),
                    content.getMeasuredHeight());
        }
        // Try only once... we should really loop but it's not a big deal
        // if the draw was cancelled, it will only be temporary anyway
        content.getViewTreeObserver().dispatchOnPreDraw();
        if (!content.isHardwareAccelerated()) content.buildDrawingCache();

        content.setVisibility(View.GONE);
    }

    private void prepareTracking(int position) {
        mTracking = true;
        mVelocityTracker = VelocityTracker.obtain();
        boolean opening = !mExpanded;
        if (opening) {
            mAnimatedAcceleration = mMaximumAcceleration;
            mAnimatedVelocity = mMaximumMajorVelocity;
            mAnimationPosition = (mVertical ? getHeight() - mHandleHeight : getWidth() - mHandleWidth);
            moveHandle((int) mAnimationPosition);
            mAnimating = true;
            mHandler.removeMessages(MSG_ANIMATE);
            long now = SystemClock.uptimeMillis();
            mAnimationLastTime = now;
            mCurrentAnimationTime = now + ANIMATION_FRAME_DURATION;
            mAnimating = true;
        } else {
            if (mAnimating) {
                mAnimating = false;
                mHandler.removeMessages(MSG_ANIMATE);
            }
            moveHandle(position);
        }
    }

    private void stopTracking() {
        mHandle.setPressed(false);
        mTracking = false;

        if (mOnDrawerScrollListener != null) {
            mOnDrawerScrollListener.onScrollEnded();
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void doAnimation() {
        if (mAnimating) {
            incrementAnimation();
            if (mAnimationPosition >= (mVertical ? getHeight() : getWidth()) - 1) {
                mAnimating = false;
                closeDrawer();
            } else if (mAnimationPosition < mSliderOffset) {
                mAnimating = false;
                openDrawer();
            } else {
                moveHandle((int) mAnimationPosition);
                mCurrentAnimationTime += ANIMATION_FRAME_DURATION;
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE),
                        mCurrentAnimationTime);
            }
        }
    }

    private void incrementAnimation() {
        long now = SystemClock.uptimeMillis();
        float t = (now - mAnimationLastTime) / 1000.0f;                   // ms -> s
        final float position = mAnimationPosition;
        final float v = mAnimatedVelocity;                                // px/s
        final float a = mAnimatedAcceleration;                            // px/s/s
        mAnimationPosition = position + (v * t) + (0.5f * a * t * t);     // px
        mAnimatedVelocity = v + (a * t);                                  // px/s
        mAnimationLastTime = now;                                         // ms
    }

    /**
     * Toggles the drawer open and close. Takes effect immediately.
     *
     * @see #open()
     * @see #close()
     * @see #animateClose()
     * @see #animateOpen()
     */
    public void toggle() {
        if (!mExpanded) {
            openDrawer();
        } else {
            closeDrawer();
        }
        invalidate();
        requestLayout();
    }

    /**
     * Opens the drawer immediately.
     *
     * @see #toggle()
     * @see #close()
     * @see #animateOpen()
     */
    public void open() {
        openDrawer();
        invalidate();
        requestLayout();

        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    /**
     * Closes the drawer immediately.
     *
     * @see #toggle()
     * @see #open()
     * @see #animateClose()
     */
    public void close() {
        closeDrawer();
        invalidate();
        requestLayout();
    }

    /**
     * Closes the drawer with an animation.
     *
     * @see #close()
     * @see #open()
     * @see #animateOpen()
     * @see #toggle()
     */
    public void animateClose() {
        prepareContent();
        final OnDrawerScrollListener scrollListener = mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateClose(mVertical ? mHandle.getTop() : mHandle.getLeft());

        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    /**
     * Opens the drawer with an animation.
     *
     * @see #close()
     * @see #open()
     * @see #animateClose()
     * @see #toggle()
     */
    public void animateOpen() {
        prepareContent();
        final OnDrawerScrollListener scrollListener = mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateOpen(mVertical ? mHandle.getTop() : mHandle.getLeft());

        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);

        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TerminalSlider.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TerminalSlider.class.getName());
    }

    private void closeDrawer() {
        moveHandle(COLLAPSED_FULL_CLOSED);
        mContent.setVisibility(View.VISIBLE);
        mContent.destroyDrawingCache();

        if (!mExpanded) {
            return;
        }

        mExpanded = false;
        if (mOnDrawerCloseListener != null) {
            mOnDrawerCloseListener.onDrawerClosed();
        }
    }

    private void openDrawer() {
        moveHandle(EXPANDED_FULL_OPEN);
        mContent.setVisibility(View.VISIBLE);
        mContent.destroyDrawingCache();

        if (mExpanded) {
            return;
        }

        mExpanded = true;

        if (mOnDrawerOpenListener != null) {
            mOnDrawerOpenListener.onDrawerOpened();
        }
    }

    /**
     * Sets the listener that receives a notification when the drawer becomes open.
     *
     * @param onDrawerOpenListener The listener to be notified when the drawer is opened.
     */
    public void setOnDrawerOpenListener(OnDrawerOpenListener onDrawerOpenListener) {
        mOnDrawerOpenListener = onDrawerOpenListener;
    }

    /**
     * Sets the listener that receives a notification when the drawer becomes close.
     *
     * @param onDrawerCloseListener The listener to be notified when the drawer is closed.
     */
    public void setOnDrawerCloseListener(OnDrawerCloseListener onDrawerCloseListener) {
        mOnDrawerCloseListener = onDrawerCloseListener;
    }

    /**
     * Sets the listener that receives a notification when the drawer starts or ends
     * a scroll. A fling is considered as a scroll. A fling will also trigger a
     * drawer opened or drawer closed event.
     *
     * @param onDrawerScrollListener The listener to be notified when scrolling
     *                               starts or stops.
     */
    public void setOnDrawerScrollListener(OnDrawerScrollListener onDrawerScrollListener) {
        mOnDrawerScrollListener = onDrawerScrollListener;
    }

    /**
     * Returns the handle of the drawer.
     *
     * @return The View reprenseting the handle of the drawer, identified by
     *         the "handle" id in XML.
     */
    public View getHandle() {
        return mHandle;
    }

    /**
     * Returns the content of the drawer.
     *
     * @return The View reprenseting the content of the drawer, identified by
     *         the "content" id in XML.
     */
    public View getContent() {
        return mContent;
    }

    /**
     * Unlocks the TerminalSlider so that touch events are processed.
     *
     * @see #lock()
     */
    public void unlock() {
        mLocked = false;
    }

    /**
     * Locks the TerminalSlider so that touch events are ignores.
     *
     * @see #unlock()
     */
    public void lock() {
        mLocked = true;
    }

    /**
     * Indicates whether the drawer is currently fully opened.
     *
     * @return True if the drawer is opened, false otherwise.
     */
    public boolean isOpened() {
        return mExpanded;
    }

    /**
     * Indicates whether the drawer is scrolling or flinging.
     *
     * @return True if the drawer is scroller or flinging, false otherwise.
     */
    public boolean isMoving() {
        return mTracking || mAnimating;
    }

    /**
     * Callback invoked when the drawer is opened.
     */
    public static interface OnDrawerOpenListener {
        /**
         * Invoked when the drawer becomes fully open.
         */
        public void onDrawerOpened();
    }

    /**
     * Callback invoked when the drawer is closed.
     */
    public static interface OnDrawerCloseListener {
        /**
         * Invoked when the drawer becomes fully closed.
         */
        public void onDrawerClosed();
    }

    /**
     * Callback invoked when the drawer is scrolled.
     */
    public static interface OnDrawerScrollListener {
        /**
         * Invoked when the user starts dragging/flinging the drawer's handle.
         */
        public void onScrollStarted();

        /**
         * Invoked when the user stops dragging/flinging the drawer's handle.
         */
        public void onScrollEnded();
    }

    private class DrawerToggler implements View.OnClickListener {
        public void onClick(View v) {
            if (mLocked) {
                return;
            }
            // mAllowSingleTap isn't relevant here; you're *always*
            // allowed to open/close the drawer by clicking with the
            // trackball.
            toggle();
        }
    }

    private class SlidingHandler extends Handler {
        public void handleMessage(Message m) {
            switch (m.what) {
                case MSG_ANIMATE:
                    doAnimation();
                    break;
            }
        }
    }
}
