package warrrr.console;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import warrrr.game.Block;
import warrrr.game.Game;
import warrrr.game.Property;

public class Main extends Application {
	private final int sleepTimeMillis = 800;
	private final int transitionTimeMillis = 400;
	private final double paneGab = 2;
	private final double rectArc = 15;
	private final double blockSize = 50;
	private final Color topPaneText = Color.WHITE;
	private final Color topPaneColor = Color.BROWN;
	private final Color backgroundColor = Color.DARKKHAKI;
	private final Color plainColor = Color.KHAKI;
	private final String title = "WARrrr!";
	private int numberOfCol;
	private int numberOfRow;

	private boolean errorInMap = false;
	private String errorMsg;

	private BorderPane mainPane;
	private Label turnLabel;

	private Game game;
	private Box[][] table;

	public Main() {
		this.game = new Game();
		this.numberOfRow = this.game.getNumberOfRows();
		this.numberOfCol = this.game.getNumberOfColumns();
		this.table = new Box[this.numberOfRow][this.numberOfCol];
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		double minWidth = this.numberOfCol * (this.blockSize + this.paneGab) + 30;
		double minHeight = this.numberOfRow * (this.blockSize + this.paneGab) + 70;

		primaryStage.setTitle(this.title);
		this.mainPane = new BorderPane();
		this.mainPane.setBackground(new Background(new BackgroundFill(this.backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));

		this.addTopPane();
		this.addBottomPane();

		Scene scene = new Scene(this.mainPane, minWidth, minHeight);
		scene.getStylesheets().add(Main.class.getResource("main.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest((WindowEvent e) -> System.exit(0));
		primaryStage.setMinWidth(minWidth);
		primaryStage.setMinHeight(minHeight);
		primaryStage.show();

		this.addCenterPane();

		new Thread(() -> {
			while (this.game.isActive()) {
				Platform.runLater(() -> {
					this.game.doTurn();
					this.setTurn(this.game.getTurnNo());
					this.drawCells();
				});
				try {
					Thread.sleep(this.sleepTimeMillis);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}

	private void drawCells() {
		for (int i = 0; i < this.numberOfRow; i++) {
			for (int j = 0; j < this.numberOfCol; j++) {
				Block block = this.game.at(i, j);
				if (block.hasChanged()) {
					this.table[i][j].update(block);
				}
			}
		}
	}

	private void addCenterPane() {
		TilePane grid = new TilePane();
		grid.setBackground(new Background(new BackgroundFill(this.backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
		grid.setAlignment(Pos.CENTER);
		grid.setPrefColumns(this.numberOfCol);
		grid.setPrefRows(this.numberOfRow);
		grid.setMaxSize(this.numberOfCol * (this.blockSize + this.paneGab), this.numberOfRow * (this.blockSize + this.paneGab));
		grid.setPrefTileHeight(this.blockSize);
		grid.setPrefTileWidth(this.blockSize);
		grid.setVgap(this.paneGab);
		grid.setHgap(this.paneGab);

		for (int i = 0; i < this.numberOfRow; i++) {
			for (int j = 0; j < this.numberOfCol; j++) {
				this.table[i][j] = new Box(this.game.at(i, j));
				grid.getChildren().add(this.table[i][j]);
			}
		}
		this.mainPane.setCenter(grid);
	}

	private void addTopPane() {
		FlowPane pane = new FlowPane();
		pane.setBackground(new Background(new BackgroundFill(this.topPaneColor, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(5));

		this.turnLabel = new Label();
		this.turnLabel.setFont(new Font(15));
		this.turnLabel.setTextFill(this.topPaneText);
		pane.getChildren().add(this.turnLabel);

		this.mainPane.setTop(pane);
	}

	private void setTurn(int turn) {
		this.turnLabel.setText("Cycle " + turn +
				", " + this.game.teams[0].name + ": " + this.game.teams[0].getCoins() +
				", " + this.game.teams[1].name + ": " + this.game.teams[1].getCoins());
	}

	private void addBottomPane() {
		FlowPane pane = new FlowPane();
		pane.setBackground(new Background(new BackgroundFill(this.topPaneColor, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(5));

		this.mainPane.setBottom(pane);
	}

	private class Box extends StackPane {
		private Text health = new Text();
		private Text type = new Text();
		private Text status = new Text();
		private Rectangle rect = new Rectangle(0, 0, Main.this.blockSize, Main.this.blockSize);
		private Rectangle layer = new Rectangle(0, 0, Main.this.blockSize, Main.this.blockSize);
		private FadeTransition layerFade = new FadeTransition(Duration.millis(Main.this.transitionTimeMillis), this.layer);
		private FillTransition rectFill = new FillTransition(Duration.millis(Main.this.transitionTimeMillis / 2), this.rect, null, null);
		private double layerOpacity = 0.5;

		private int healthValue = -1;
		private Property content;

		public Box(Block block) {
			Rectangle clip = new Rectangle(0, 0, Main.this.blockSize, Main.this.blockSize);
			this.setClip(clip);

			VBox inner = new VBox();
			inner.getStyleClass().add("block");
			inner.setAlignment(Pos.CENTER);

			this.health.getStyleClass().add("health");
			this.type.getStyleClass().add("type");
			this.status.getStyleClass().add("status");

			this.update(block);

			inner.getChildren().addAll(this.health, this.type, this.status);

			this.setOnMouseClicked(block);

			this.rect.setFill(Main.this.plainColor);
			this.rect.setArcHeight(Main.this.rectArc);
			this.rect.setArcWidth(Main.this.rectArc);
			this.rect.setStrokeType(StrokeType.INSIDE);
			this.rect.setStrokeWidth(2);

			this.layer.setFill(Color.WHITE);
			this.layer.setArcHeight(Main.this.rectArc);
			this.layer.setArcWidth(Main.this.rectArc);
			this.layer.setOpacity(0);

			this.getChildren().addAll(this.rect, inner, layer);
		}

		public void update(Block block) {
			if (block == Main.this.game.selected) {
				this.layerFade.setFromValue(0);
				this.layerFade.setToValue(this.layerOpacity);
				this.layerFade.setCycleCount(1);
				this.layerFade.play();
				this.layer.setOpacity(this.layerOpacity);
			} else {
				this.layer.setOpacity(0);
			}
			if (block.getContent() != null) {
				this.health.setText("" + block.getContent().getHealth());
				this.type.setText(block.getContent().getName());
				this.status.setText(block.getContent().getStatus().toString());

				if (this.content == block.getContent() && this.healthValue > block.getContent().getHealth()) {
					this.showBlood();
				} else {
					this.hideBlood();
				}

				this.changeBackground(block.getTeam().getColor());
				this.content = block.getContent();
				this.healthValue = this.content.getHealth();
			} else {
				this.health.setText("");
				this.type.setText("");
				this.status.setText("");
				this.hideBlood();
				if (block.getTeam() == null) {
					this.changeBackground(Main.this.plainColor);
				}
			}
			block.setChanged(false);
		}

		private void showBlood() {
			this.rect.setStroke(Color.RED);

		}
		private void hideBlood() {
			this.rect.setStroke(Color.TRANSPARENT);
		}

		private void changeBackground(Color color) {
			this.rectFill.setToValue(color);
			this.rectFill.setCycleCount(1);
			this.rectFill.play();

			this.rect.setFill(color);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
