package warrrr.game;

import java.util.Stack;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public class Warrior extends Warfare {
	public static final int COST = 150;
	public static final int UPGRADE_COST = 100;

	private Stack<Block> path = new Stack<>();
	private int range = 1;
	private int rate = 5;

	private Warrior(Game game, Block container, Team team) {
		this(game, container, team, false);
	}
	private Warrior(Game game, Block container, Team team, boolean instantBuild) {
		super(game, container, team);
		team.addWarrior(this);
		this.build(instantBuild);
	}
	public static Warrior getWarrior(Game game, Block container, Team team, boolean instantBuild) {
		if (team.canHaveNewWarrior()) {
			return new Warrior(game, container, team, instantBuild);
		}
		return team.getWarriors().get(0);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public void attack() {
		this.findMyAttacker(); // in both modes, warrior has to kill its attacker first.
		if (target == null || target.getContent() == null) {
			this.removeAttacker();
			this.setStatus(STATUS.NORMAL);
			return;
		}
		if (!target.getContent().hurt(this.getPower(), this)) {
			this.removeAttacker();
			this.setStatus(STATUS.MOVE);
		}
	}

	@Override
	public int getRange() {
		return range;
	}

	@Override
	public int getRate() {
		return rate;
	}

	public void move() {
		if (this.target == null) {
			return;
		}

		// always tries to find a path if no path is defined
		if (path.isEmpty()) {
			path = this.game.findPath(this.container, this.target);
		}

		// if there is a path, go through it.
		for (int i = 0; i < this.range; i++) {
			if (!path.isEmpty()) {
				Block newBlock = this.path.pop();
				if (newBlock.isEmpty()) {
					this.setStatus(STATUS.MOVE);
					this.container.setContent(null);

					this.container = newBlock;
					this.container.setContent(this);

					if (newBlock == target) {
						this.setStatus(STATUS.NORMAL);
						target = null;
						path.clear();
					}
				} else {
					path = this.game.findPath(this.container, this.target);
				}
			} else {
				this.setStatus(STATUS.NORMAL);
				break;
			}
		}
	}

	public void findNewPosition() {
		target = this.game.findEmptyBlock(this.container, range);
//		target = this.game.at(0, 9);
//		System.out.println("print my target is: " + target);
	}

	@Override
	public void act() {
		if (this.status == STATUS.BUILDING) {
			build();
		} else if (this.status == STATUS.ATTACK) {
			attack();
		} else if (this.status == STATUS.MOVE) {
			move();
		} else if (this.getTeam().isAI()) {
			if (findAndSetTarget()) {
				this.setStatus(STATUS.ATTACK);
				attack();
			} else {
				findNewPosition();
				this.setStatus(STATUS.MOVE);
				move();
			}
		}
	}

	@Override
	public void destroy() {
		this.getTeam().removeWarrior(this);
		super.destroy();
	}

	@Override
	public String getName() {
		return "Warrior";
	}
}
