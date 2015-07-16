package warrrr.game;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public class Mine extends Building {
	public static final int COST = 150;

	private int rate = 1;

//	public Mine(Game game, Block container, Team team) {
//		super(game, container, team);
//	}
	public Mine(Game game, Block container, Team team) {
		this(game, container, team, false);
	}
	public Mine(Game game, Block container, Team team, boolean instantBuild) {
		super(game, container, team);
		this.build(instantBuild);
	}

	public void collect() {
		int gain = ((Resource) this.container).mine(this.getMiningRate());
		this.getTeam().collectCoins(gain);
	}

	private int getMiningRate() {
		return this.rate * this.getLevel();
	}

	@Override
	public void act() {
		if (this.status == STATUS.BUILDING) {
			this.build();
		} else {
			this.collect();
		}
	}

	@Override
	public String getName() {
		return "Mine";
	}
}
