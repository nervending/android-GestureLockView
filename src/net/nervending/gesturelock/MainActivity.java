package net.nervending.gesturelock;

import net.nervending.gesturelock.GestureLockView.LockViewListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements LockViewListener {
	
	private GestureLockView lockView;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lockView = (GestureLockView) findViewById(R.id.lockView);
		textView = (TextView) findViewById(R.id.textPassword);
		
		lockView.setLockViewListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onSubmitPassword(int[] password) {
		StringBuilder sb = new StringBuilder();
		sb.append("√‹¬Î:");
		for (int i : password) {
			sb.append(i);
		}
		textView.setText(sb.toString());
	}

	@Override
	public void onCancelInput() {
		textView.setText("»°œ˚ ‰»Î");
	}

}
