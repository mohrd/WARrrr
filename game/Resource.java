package warrrr.game;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public class Resource extends Block {
	public static final int MAX_AMOUNT = 500;
	private int amount;

	public Resource(Game game, Position pos) {
		this(game, pos, Resource.MAX_AMOUNT);
	}
	public Resource(Game game, Position pos, int amount) {
		super(game, pos);
		this.amount = amount;
	}

	public int mine(int amount) {
		int mine = Math.min(this.amount, amount);
		this.amount -= mine;
		return mine;
	}
}
