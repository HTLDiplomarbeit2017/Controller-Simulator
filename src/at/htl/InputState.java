package at.htl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InputState {
	public enum XBOX_Button {
		A(0), B(1), Y(2), X(3), D_LEFT(4), D_RIGHT(5), D_UP(6), D_DOWN(7), RB(8), LB(9), RT(10), LT(11), START(
				12), BACK(13), STICK_R(14), STICK_L(15);
		private int bit;

		XBOX_Button(int bit) {
			this.bit = bit;
		}

		public int getBit() {
			return bit;
		}

		public int getValue() {
			return 1 << bit;
		}
	}

	private Set<XBOX_Button> pressed_x = new HashSet<XBOX_Button>();

	public void xBOX_Down(XBOX_Button button) {
		pressed_x.add(button);
		inputStateChanged();
	}

	public void xBOX_Up(XBOX_Button button) {
		pressed_x.remove(button);
		inputStateChanged();
	}

	public void xBox_ReleaseAll() {
		pressed_x.clear();
		inputStateChanged();
	}
	private Set<InputStateChangeListener> changeListners=new HashSet<InputStateChangeListener>();
	public byte[] getSendableChars() {
		byte[] toSend = new byte[2];
		short data = 0;
		for (XBOX_Button b : pressed_x) {
			data += b.getValue();
		}
		System.out.println(data);
		toSend[0] = (byte) (data & 0xff);
		toSend[1] = (byte) ((data >> 8) & 0xff);
		return toSend;
	}
	public void addInputStateChangeListener(InputStateChangeListener listener){
		changeListners.add(listener);
	}
	private void inputStateChanged(){
		for (InputStateChangeListener inputStateChangeListener : changeListners) {
			inputStateChangeListener.inputChanged(this);
		}
	}
	public boolean isAnyDown(){
		return !pressed_x.isEmpty();
	}
}
