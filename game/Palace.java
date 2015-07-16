package warrrr.game;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public class Palace extends Building {
	public Palace(Game game, Block container, Team team) {
		super(game, container, team);
		team.setPalace(this);
		this.build(true);
	}

	@Override
	public String getName() {
		return "Palace";
	}

	@Override
	public void act() {
		Block block;
		if (this.getTeam().canHaveNewWarrior() && this.getCoins() > Warrior.COST && (block = this.game.findEmptyBlock(this.container, 1)) != null) {
			Warrior warrior = Warrior.getWarrior(this.game, block, this.getTeam(), false);
			block.setContent(warrior);
			this.getTeam().pay(Warrior.COST);
		}
	}

	private int getCoins() {
		return this.getTeam().getCoins();
	}

	@Override
	public void destroy() {
		this.getTeam().setPalace(null);
		super.destroy();
	}
}
