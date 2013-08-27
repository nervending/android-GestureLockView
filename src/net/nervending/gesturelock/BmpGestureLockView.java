package net.nervending.gesturelock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;

public class BmpGestureLockView extends GestureLockView {

	private Bitmap bmpForHot;
	private Bitmap bmpForWait;
	private int size_12;
	private Rect srcRect;
	private Rect destRect;
	private Paint paint;

	public BmpGestureLockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BmpGestureLockView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BmpGestureLockView(Context context) {
		super(context);
	}

	@Override
	protected void commonInit(Context context) {
		super.commonInit(context);
		bmpForHot = BitmapFactory.decodeResource(getResources(),
				R.drawable.gesture_grid_focused);
		bmpForWait = BitmapFactory.decodeResource(getResources(),
				R.drawable.gesture_grid_normal);
		srcRect = new Rect();
		srcRect.left = 0;
		srcRect.right = bmpForWait.getWidth();
		srcRect.top = 0;
		srcRect.bottom = bmpForWait.getHeight();
		paint = new Paint();
		destRect = new Rect();
	}

	@Override
	protected boolean touchInPoint(int x, int y, Point point) {
		int dx = point.x - x;
		int dy = point.y - y;
		double d = dx * dx + dy*dy;
		double r = Math.sqrt(d);
		if (r < size_12 ) {
			return true;
		}
		return false;
	}

	@Override
	protected void drawWaitPoint(Canvas canvas, Point point) {
		destRect.left = point.x - size_12;
		destRect.right = point.x + size_12;
		destRect.top = point.y - size_12;
		destRect.bottom = point.y + size_12;
		canvas.drawBitmap(bmpForWait, srcRect, destRect, paint);
	}

	@Override
	protected void drawHotPoint(Canvas canvas, Point point) {
		destRect.left = point.x - size_12;
		destRect.right = point.x + size_12;
		destRect.top = point.y - size_12;
		destRect.bottom = point.y + size_12;
		canvas.drawBitmap(bmpForHot, srcRect, destRect, paint);
	}

	@Override
	protected void drawLine(Canvas canvas, Point fromPoint,
			boolean isfromPoint, Point toPoint, boolean isToPoint) {
		paint.setColor(Color.GREEN);
		canvas.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		size_12 = getSize_3() / 4;
	}
}
