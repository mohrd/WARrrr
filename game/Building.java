package warrrr.game;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public abstract class Building extends Property {

	public Building(Game game, Block container, Team team) {
		super(game, container, team);
	}

	@Override
	public boolean canMove() {
		return false;
	}
}
