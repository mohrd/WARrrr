package warrrr.game;

import java.util.ArrayList;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public abstract class Warfare extends Property {
	protected Block target;

	public Warfare(Game game, Block container, Team team) {
		super(game, container, team);
	}

	public abstract void attack();
	public abstract int getRange();
	protected abstract int getRate();

	public boolean findMyAttacker() {
		if (this.getAttacker() != null && this.isInRange(this.getAttacker().container)) {
			this.removeTargetAttacker();
			this.target = this.getAttacker().container;
			return true;
		}
		return false;
	}
	public boolean findAndSetTarget() {
		if (this.findMyAttacker()) {
			return true;
		}

		ArrayList<Block> targets = new ArrayList<>();

		int r = this.container.position.getRowNo();
		int c = this.container.position.getColumnNo();
		Position pos = new Position(0, 0);
		Block block;
		for (int i = -this.getRange(); i <= this.getRange(); i++) {
			for (int j = -this.getRange(); j <= this.getRange(); j++) {
				pos.change(r + i, c + j);
				if (this.game.isInMap(pos) && (block = this.game.at(pos)) != null && block.isEnemy(this.getTeam())) {
					targets.add(block);
				}
			}
		}
		if (targets.size() > 0) {
			this.target = (Block) Game.randomItem(targets);
			return true;
		}
		return false;
	}

	@Override
	public void act() {
		if (this.status == STATUS.BUILDING) {
			this.build();
		} else {
			if (this.status == STATUS.ATTACK) {
				attack();
				return;
			}
			if (findAndSetTarget()) {
				this.setStatus(STATUS.ATTACK);
				attack();
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.removeTargetAttacker();
	}

	public void removeTargetAttacker() {
		if (this.target != null && this.target.getContent().getAttacker() == this) {
			this.target.getContent().removeAttacker();
		}
	}

	public int getPower() {
		return this.getRate() * this.getLevel();
	}

	public boolean isInRange(Block block) {
		return Math.abs(block.position.getRowNo() - this.container.position.getRowNo()) <= this.getRange() &&
				Math.abs(block.position.getColumnNo() - this.container.position.getColumnNo()) <= this.getRange();
	}

}
