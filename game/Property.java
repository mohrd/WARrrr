package warrrr.game;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public abstract class Property {
	protected Team team;
	protected int health = 0;
	protected STATUS status = STATUS.NORMAL;
	protected int level = 1;
	protected Block container;
	protected Game game;
	protected Warfare attacker;
	public String name = "Property";

	public Property(Game game, Block container, Team team) {
		this.container = container;
		this.team = team;
		this.game = game;

		this.setStatus(STATUS.BUILDING);
		this.game.newProperty(this);
	}

	public boolean canMove() {
		return false;
	}

	public Team getTeam() {
		return team;
	}

	public int getHealth() {
		return health;
	}

	public STATUS getStatus() {
		return status;
	}

	public String getName() { return "Property"; }
	public int getMaxHealth() {
		return 100;
	}
	public int getBuildRate() {
		return 10;
	}

	public int getLevel() {
		return level;
	}

	public void setStatus(STATUS status) {
		this.status = status;
		this.container.change();
	}

	public void upgrade() {
		this.level++;
	}

	protected void update() {
		this.container.setChanged(true);
	}

	public boolean hurt(int damage) {
		this.health -= damage;
		if (this.health <= 0) {
			this.health = 0;
			this.destroy();
		}
		this.update();
		return (this.health != 0);
	}
	public boolean hurt(int damage, Warfare attacker) {
		this.attacker = attacker;
		return hurt(damage);
	}

	public void destroy() {
		this.container.setContent(null);
		this.setStatus(STATUS.DELETED);
	}

	public abstract void act();

	public void build() {
		this.build(this.getBuildRate());
	}
	public void build(boolean instant) {
		if (instant) {
			this.build(this.getMaxHealth());
		} else {
			this.build(this.getBuildRate());
		}
	}
	public void build(int rate)  {
		if (this.health < this.getMaxHealth()) {
			this.health = Math.min(this.health + rate, this.getMaxHealth());
		}
		if (this.health == this.getMaxHealth()) {
			this.setStatus(STATUS.NORMAL);
		}
		this.container.change();
	}

	public Property getAttacker() {
		return this.attacker;
	}
	public void removeAttacker() {
		this.attacker = null;
		this.container.change();
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ", team: " + this.team + ", status: " + this.status;
	}
}
