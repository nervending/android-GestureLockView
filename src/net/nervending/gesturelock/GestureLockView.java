package net.nervending.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class GestureLockView extends View {
	private int[] nextIndexs;
	private boolean[] pointCheckedState;
	private static final int kNoneIndex = -1;
	private static final int kPointCount = 9;
	private int startIndex = kNoneIndex;
	private int lastIndex = kNoneIndex;
	private Point touchPoint;
	private Point[] pointLoctions;
	private int size_3;
	private int size_3x2;
	private LockViewListener l;

	public GestureLockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		commonInit(context);
	}

	public GestureLockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		commonInit(context);
	}

	public GestureLockView(Context context) {
		super(context);
		commonInit(context);
	}

	protected void commonInit(Context context) {
		nextIndexs = new int[kPointCount];
		pointCheckedState = new boolean[kPointCount];

		clearIndexs();
		touchPoint = new Point();
		pointLoctions = new Point[kPointCount];

		for (int i = 0; i != kPointCount; ++i) {
			pointLoctions[i] = new Point();
		}
	}

	private void clearIndexs() {
		for (int i = 0; i != kPointCount; ++i) {
			nextIndexs[i] = kNoneIndex;
		}

		for (int i = 0; i != kPointCount; ++i) {
			pointCheckedState[i] = false;
		}

		startIndex = kNoneIndex;
		lastIndex = kNoneIndex;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int wMode = MeasureSpec.getMode(widthMeasureSpec);

		int h = MeasureSpec.getSize(heightMeasureSpec);
		int hMode = MeasureSpec.getMode(heightMeasureSpec);

		if (h < w) {
			w = h;
		} else {
			h = w;
		}

		setMeasuredDimension(w, h);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		calcutePointLocation(w);
	};

	private void calcutePointLocation(int size) {
		size_3 = size / 3;
		size_3x2 = size - size_3;
		int size_2 = size / 2;
		int size_6 = size_3 / 2;
		int size_5_6 = size - size_6;
		pointLoctions[0].y = pointLoctions[1].y = pointLoctions[2].y = pointLoctions[0].x = pointLoctions[3].x = pointLoctions[6].x = size_6;
		pointLoctions[3].y = pointLoctions[4].y = pointLoctions[5].y = pointLoctions[1].x = pointLoctions[4].x = pointLoctions[7].x = size_2;
		pointLoctions[6].y = pointLoctions[7].y = pointLoctions[8].y = pointLoctions[2].x = pointLoctions[5].x = pointLoctions[8].x = size_5_6;
	}

	private int getTouchedPointIndex(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int col = 0;
		int row = 0;
		if (x < size_3) {
			col = 0;
		} else if (x > size_3x2) {
			col = 2;
		} else {
			col = 1;
		}

		if (y < size_3) {
			row = 0;
		} else if (y > size_3x2) {
			row = 2;
		} else {
			row = 1;
		}

		int index = getPointIndexAtRowCol(row, col);
		if (pointCheckedState[index]) {
			return kNoneIndex;
		}
		if (touchInPoint(x, y, pointLoctions[index])) {
			return index;
		}
		return kNoneIndex;
	}

	private int getPointIndexAtRowCol(int row, int col) {
		return row * 3 + col;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int touchIndex = getTouchedPointIndex(event);
		touchPoint.x = (int) event.getX();
		touchPoint.y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			addNewTouchPoint(touchIndex);
			break;
		case MotionEvent.ACTION_MOVE:
			addNewTouchPoint(touchIndex);
			break;
		case MotionEvent.ACTION_UP:
			addNewTouchPoint(touchIndex);
			submitPassword();
			break;
		case MotionEvent.ACTION_CANCEL:
			cancelInput();
			break;
		}
		invalidate();
		return true;
	}

	public void addNewTouchPoint(int touchIndex) {
		if (touchIndex == kNoneIndex) {
			return;
		}
		if (lastIndex != touchIndex) {
			if (lastIndex != kNoneIndex) {
				nextIndexs[lastIndex] = touchIndex;
				lastIndex = touchIndex;
			} else {
				lastIndex = startIndex = touchIndex;
			}
			pointCheckedState[touchIndex] = true;
		}
	}

	public void cancelInput() {
		clearIndexs();
	}

	private void submitPassword() {
		int passwordSize = 0;
		for (int i = 0; i != kPointCount; ++i) {
			if (pointCheckedState[i]) {
				passwordSize++;
			}
		}
		int[] password = new int[passwordSize];
		for (int index = startIndex, i = 0; i != passwordSize; ++i, index = nextIndexs[index]) {
			password[i] = index;
		}
		if (null != l) {
			l.onSubmitPassword(password);
		}
		clearIndexs();
	}

	public void setLockViewListener(LockViewListener l) {
		this.l = l;
	}

	public interface LockViewListener {
		void onSubmitPassword(int[] password);

		void onCancelInput();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < kPointCount; i++) {
			if (pointCheckedState[i]) {
				drawHotPoint(canvas, pointLoctions[i]);
			} else {
				drawWaitPoint(canvas, pointLoctions[i]);
			}
		}

		for (int i = startIndex; i != kNoneIndex;) {
			if (nextIndexs[i] != kNoneIndex) {
				drawLine(canvas, pointLoctions[i], true,
						pointLoctions[nextIndexs[i]], true);
			} else {
				drawLine(canvas, pointLoctions[i], true, touchPoint, false);
			}
			i = nextIndexs[i];
		}
	}

	protected int getSize_3() {
		return size_3;
	}

	protected abstract boolean touchInPoint(int x, int y, Point point);

	protected abstract void drawWaitPoint(Canvas canvas, Point point);

	protected abstract void drawHotPoint(Canvas canvas, Point point);

	protected abstract void drawLine(Canvas canvas, Point fromPoint,
			boolean isfromPoint, Point toPoint, boolean isToPoint);
}
