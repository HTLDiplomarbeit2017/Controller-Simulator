package at.htl;

public enum ASCII_Control {
	ETX(3),ACK(6);

	public final int code;

	ASCII_Control(int code) {
        this.code = code;
        
    }
	public char getChar(){
		return (char)code;
	}
}
