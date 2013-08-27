package net.nervending.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;

public class ColorGestureLockView extends GestureLockView {
	private Paint paint;
	private int size_12;

	public ColorGestureLockView(Context context) {
		super(context);
	}

	public ColorGestureLockView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColorGestureLockView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void commonInit(Context context) {
		super.commonInit(context);
		paint = new Paint();
	}

	protected boolean touchInPoint(int x, int y, Point point) {
		int dx = point.x - x;
		int dy = point.y - y;
		double d = dx * dx + dy * dy;
		double r = Math.sqrt(d);
		if (r < size_12) {
			return true;
		}
		return false;
	}

	protected void drawWaitPoint(Canvas canvas, Point point) {
		paint.setColor(Color.BLUE);
		canvas.drawCircle(point.x, point.y, size_12, paint);
	}

	protected void drawHotPoint(Canvas canvas, Point point) {
		paint.setColor(Color.RED);
		canvas.drawCircle(point.x, point.y, size_12, paint);
	}

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
