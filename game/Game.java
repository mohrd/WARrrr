package warrrr.game;

import javafx.scene.paint.Color;

import java.util.*;

public class Game {

	private int numberOfRows;
	private int numberOfColumns;
	private int turnNo;
	private Block[][] map;
	public Team[] teams = new Team[2];
	public ArrayList<Property> properties = new ArrayList<>();
	public ArrayList<Property> deletedProperties = new ArrayList<>();
	public ArrayList<Property> newProperties = new ArrayList<>();

	public Block selected;

	public Game() {
		this.initTeams();
		try {
			this.loadMap();
		} catch (UnknownProperty unknownProperty) {
			unknownProperty.printStackTrace();
		}
	}

	public void initTeams() {
		teams[0] = new Team(this, "Player", Color.SKYBLUE, true);
		teams[1] = new Team(this, "Computer", Color.CHARTREUSE, true);
	}

	public boolean isActive() {
		return !(this.teams[0].getPalace() == null || this.teams[1].getPalace() == null);
	}

	public int getNumberOfRows() {
		return this.numberOfRows;
	}

	public int getNumberOfColumns() {
		return this.numberOfColumns;
	}

	public int getTurnNo() {
		return this.turnNo;
	}

	public Block at(int r, int c) {
		Block d = this.at(new Position(r, c));
		return d;
	}

	public Block at(Position position) {
		if (this.isInMap(position)) {
			return this.map[position.getRowNo()][position.getColumnNo()];
		} else {
			return null;
		}
	}

	public Team getEnemy(Team me) {
		return (teams[0] == me) ? teams[1] : teams[0];
	}

	public void doTurn() {
		this.turnNo++;
		deletedProperties.clear();
		for (Property property : this.properties) {
			if (property.getStatus() == STATUS.DELETED) {
				deletedProperties.add(property);
			} else {
				property.act();
			}
		}
		this.properties.removeAll(deletedProperties);
		this.properties.addAll(newProperties);
		this.newProperties.clear();
//		for (int i = 0; i < this.properties.size(); i++) {
//			this.properties.get(i).act();
//		}
//		for (int i = 0; i < this.numberOfRows; i++) {
//			for (int j = 0; j < this.numberOfColumns; j++) {
//				if (!this.map[i][j].isEmpty()) {
//					this.map[i][j].getContent().act();
//				}
//			}
//		}
	}

	public void newProperty(Property property) {
		this.newProperties.add(property);
	}

	public Block findEmptyBlock(Block center, int range) {
		ArrayList<Block> available = new ArrayList<>();
		ArrayList<Block> untouched = new ArrayList<>();

		int row = center.position.getRowNo();
		int col = center.position.getColumnNo();

		Position pos = new Position(row, col);
		Block block;
		for (int i = -range; i <= range; i++) {
			for (int j = -range; j <= range; j++) {
				pos.change(i + row, j + col);
				if (this.isInMap(pos) && (block = this.at(pos)) != null && block.isEmpty()) {
					available.add(block);
					if (!block.isAlly(center.getTeam())) {
						untouched.add(block);
					}
				}
			}
		}
		if (available.size() > 0) {
			return (Block) Game.randomItem((untouched.size() > 0) ? untouched : available);
		}
		return null;
	}

	public Stack<Block> findPath(Block from, Block to) {
		HashMap<Block, Block> parents = new HashMap<>();
		HashMap<Block, Boolean> visited = new HashMap<>();
		parents.put(from, from);
		Queue<Block> queue = new LinkedList<>();
		queue.add(from);
		Block root, child;
		Position pos = new Position(0, 0);
		int r, c;
		while (!queue.isEmpty()) {
			root = queue.poll();
			r = root.position.getRowNo();
			c = root.position.getColumnNo();
			visited.put(root, true);
			if (root == to) {
				break;
			} else {
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						pos.change(r + i, c + j);
						if (this.isInMap(pos)) {
							child = this.at(pos);
							if (child.isEmpty() && !visited.containsKey(child)) {
								parents.put(child, root);
								visited.put(child, false);
								queue.add(child);
							}
						}
					}
				}
			}
		}
		Stack<Block> path = new Stack<>();
		if (parents.get(to) != null) { // there is a way
			root = to;
			do {
				path.add(root);
				root = parents.get(root);
			} while (root != from);
		}
		return path;
	}

	public boolean isInMap(Position pos) {
		int columnNo = pos.getColumnNo();
		int rowNo = pos.getRowNo();
		return rowNo >= 0
				&& columnNo >= 0
				&& rowNo < this.numberOfRows
				&& columnNo < this.numberOfColumns;
	}

	private void loadMap() throws UnknownProperty {
		this.numberOfRows = 10;
		this.numberOfColumns = 15;
		this.map = new Block[this.numberOfRows][this.numberOfColumns];

		String[] blocks = {
				"M..M...$.......",
				"P.........m....",
				"..T.......m....",
				"..........m.r..",
				"$..........$...",
				"...........t..p",
				"..$........r...",
				"......W........",
				"..$........$...",
				"w......$.......",
		};


		for (int i = 0; i < this.numberOfRows; i++) {
			for (int j = 0; j < this.numberOfColumns; j++) {
				this.map[i][j] = this.newBlock(i, j, blocks[i].charAt(j));
			}
		}

//		this.map[5][5].getContent().upgrade();
	}

	private Block newBlock(int i, int j, char ch) throws UnknownProperty {
		Position pos = new Position(i, j);
		Block block = ((ch == '$' || ch == 'm' || ch == 'M') ? new Resource(this, pos) : new Block(this, pos));

		if (Character.isAlphabetic(ch)) {
			Property content;
			Team team = Character.isUpperCase(ch) ? teams[0] : teams[1]; // Uppercase: Team 0, Lowercase: Team 1 (up to down)
			switch (ch) {
				case 'p':
				case 'P':
					content = new Palace(this, block, team);
					break;
				case 'r':
				case 'R':
					content = new Repository(this, block, team, true);
					break;
				case 'm':
				case 'M':
					content = new Mine(this, block, team, true);
					break;
				case 't':
				case 'T':
					content = new Tower(this, block, team, true);
					break;
				case 'w':
				case 'W':
					content = Warrior.getWarrior(this, block, team, true);
					break;
				default:
					throw new UnknownProperty();
			}
			block.setContent(content);
		}
		return block;
	}


	public static Random random = new Random();
	public static Object randomItem(List list) {
		int r = random.nextInt(list.size());
		return list.get(r);
	}
}
