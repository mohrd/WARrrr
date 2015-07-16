package warrrr.game;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public class Tower extends Warfare {
	public static final int COST = 100;
	public static final int UPGRADE_COST = 100;

	private int range = 1;
	private int rate = 10;

	public Tower(Game game, Block container, Team team) {
		this(game, container, team, false);
	}
	public Tower(Game game, Block container, Team team, boolean instantBuild) {
		super(game, container, team);
		this.build(instantBuild);
	}

	@Override
	public int getRange() {
		return range;
	}

	@Override
	public int getRate() {
		return rate;
	}

	@Override
	public void attack() {
		this.findMyAttacker();
		if (target == null || target.getContent() == null || !target.getContent().hurt(this.getPower(), this)) {
			this.removeAttacker();
			this.target = null;
			this.setStatus(STATUS.NORMAL);
		}
	}

	@Override
	public String getName() {
		return "Tower";
	}
}
