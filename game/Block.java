package warrrr.game;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created with ♥ by Mohammad-Reza on 24/Jun/2015.
 */
public class Block implements EventHandler<MouseEvent> {
	protected Position position;
	private Property content;
	private boolean changed = false;
	private Team team;
	private Game game;

	public Block(Game game, Position pos) {
		this.game = game;
		this.position = pos;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Property getContent() {
		return content;
	}

	public void setContent(Property content) {
		this.content = content;
		this.change();
		if (content != null) {
			this.team = content.getTeam();
		}
	}

	public boolean hasChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public void change() { this.changed = true; }

	public boolean isEmpty() {
		return (this.content == null);
	}
	public boolean isAlly(Team compareTo) {
		return this.team == compareTo;
	}
	public boolean isEnemy(Team compareTo) {
		return !(this.isEmpty() || this.isAlly(compareTo));
	}

	@Override
	public void handle(MouseEvent event) {
//		if (this.game.selected != null) {
//			this.game.selected.change();
//		}
//		this.game.selected = this;
//		this.change();
		System.out.println("it's me!! " + ", team: " + this.getTeam());
	}

	@Override
	public String toString() {
		return this.getClass().getName()
				+ "(" + this.position.getRowNo() + ", " + this.position.getColumnNo() + ")";
	}
}
