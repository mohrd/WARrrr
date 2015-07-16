package warrrr.game;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created with ♥ by Mohammad-Reza on 26/Jun/2015.
 */
public class Team {
	public String name;
	private Color color;
	private Palace palace;
	private boolean AI = true;
	private Game game;

	private int coins = 0;
	private int reposCount = 0;
	private ArrayList<Warrior> warriors = new ArrayList<>(1);

	public Team(Game game, String name, Color color, boolean AI) {
		this.game = game;
		this.name = name;
		this.color = color;
		this.AI = AI;
	}

	public boolean isAI() {
		return AI;
	}

	public void setAI(boolean AI) {
		this.AI = AI;
	}

	public Color getColor() {
		return color;
	}

	public Palace getPalace() {
		return palace;
	}

	public void setPalace(Palace palace) {
		this.palace = palace;
	}

	public int getCoins() {
		return coins;
	}

	public void collectCoins(int coins) {
		this.coins += coins;
	}
	public void pay(int coins) throws NotEnoughCoins {
		if (coins > this.coins) {
			throw new NotEnoughCoins();
		}
		this.coins -= coins;
	}

	public ArrayList<Warrior> getWarriors() {
		return warriors;
	}

	public void setWarriors(ArrayList<Warrior> warriors) {
		this.warriors = warriors;
	}

	public void removeWarrior(Warrior warrior) {
		this.warriors.remove(warrior);
	}
	public void addWarrior(Warrior warrior) {
		this.warriors.add(warrior);
	}
	public boolean canHaveNewWarrior() {
		return (this.warriors.size() == 0);
	}

	public int getReposCount() {
		return reposCount;
	}
	public void addRepo() {
		this.reposCount++;
	}
	public void removeRepo() {
		int amount = this.getCoins() / this.getReposCount();
		this.game.getEnemy(this).collectCoins(amount);
		this.coins -= amount;

		this.reposCount--;
	}

	@Override
	public String toString() {
		return this.name + "(AI: " + this.AI + ")";
	}
}
