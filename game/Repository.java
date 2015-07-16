package warrrr.game;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public class Repository extends Building {
	public static final int COST = 175;
	public static final int PREF_CAPACITY = 150;
	private final int maxHealth = 100;

	@Override
	public String getName() {
		return "Repo";
	}

	public Repository(Game game, Block container, Team team) {
		this(game, container, team, false);
	}
	public Repository(Game game, Block container, Team team, boolean instantBuild) {
		super(game, container, team);
		this.build(instantBuild);
	}

	@Override
	public void destroy() {
		this.getTeam().removeRepo();
		super.destroy();
	}

	@Override
	public void act() {
		if (this.status == STATUS.BUILDING) {
			build();
		}
	}

	@Override
	public void build(int rate) {
		super.build(rate);
		if (this.status == STATUS.NORMAL) {
			this.getTeam().addRepo();
		}
	}
}
