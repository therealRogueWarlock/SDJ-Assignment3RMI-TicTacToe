package shared.transferobjects;

import java.io.Serializable;

public class TicTacToePiece implements Serializable {
	int x, y;
	String piece;
	int roomIdTarget;

	public TicTacToePiece(int x, int y, int roomIdTarget) {
		this.x = x;
		this.y = y;
		this.roomIdTarget = roomIdTarget;
	}

	public void setPiece(String name) {

		piece = name;
	}

	public String getPiece() {
		return piece;
	}

	public int getTargetGameRoom(){
		return roomIdTarget;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
