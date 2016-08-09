package reworked.tetris;

/**
 * A polyomino with 4 possible rotation states.
 * @author Davis
 *
 */
public class Polyomino {

	private ITile[] [] arr0;
	private ITile[] [] arr90;
	private ITile[] [] arr180;
	private ITile[] [] arr270;
	
	public Polyomino(ITile[] [] arr0, ITile[] [] arr90, ITile[] [] arr180, ITile[] [] arr270) {
		this.arr0 = arr0;
		this.arr90 = arr90;
		this.arr180 = arr180;
		this.arr270 = arr270;
	}
	
	public ITile[] [] getArr0() {
		return arr0;
	}
	
	public ITile[] [] getArr90() {
		return arr90;
	}
	
	public ITile[] [] getArr180() {
		return arr180;
	}
	
	public ITile[] [] getArr270() {
		return arr270;
	}
	
}
