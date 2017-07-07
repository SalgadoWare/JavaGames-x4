package es.ucm.fdi.tp.view.gui.gameView;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.extra.jboard.JBoard;
import es.ucm.fdi.tp.view.gui.GameController;

public abstract class RectBoardGameView<S extends GameState<S, A>, A extends GameAction<S, A>> extends GameView<S, A> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel gamesPanel;
	protected InfoArea infoAreaPanel;
	protected JBoard jboard;
	protected GameState<?, ?> state;

	public RectBoardGameView() {
		initGui();
	}

	private void initGui() {
		this.setLayout(new BorderLayout());
		gamesPanel = new JPanel();
		gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.X_AXIS));

		jboard = new JBoard() {

			boolean enable = false;;
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				if (enable)
					RectBoardGameView.this.mouseClicked(row, col, clickCount, mouseButton);
			}

			protected void keyPressed(int keyCode) {
				RectBoardGameView.this.keyTyped(keyCode);
			}

			protected JBoard.Shape getShape(int player) {
				if (player == -1) {
					return JBoard.Shape.RECTANGLE;
				} else {
					return JBoard.Shape.CIRCLE;
				}
			}

			protected Integer getPosition(int row, int col) {
				return RectBoardGameView.this.getPosition(row, col);
			}

			protected int getNumRows() {
				return RectBoardGameView.this.getNumRows();
			}

			protected int getNumCols() {
				return RectBoardGameView.this.getNumCols();
			}

			protected Color getColor(int player) {
				return RectBoardGameView.this.getColor(player);
			}

			protected Color getBackground(int row, int col) {
				return row % 2 + col % 2 == 1 ? Color.black : Color.white;
			}

			protected int getSepPixels() {
				return 1;
			}

			public void setEnabled(boolean enable) {
				this.enable = enable;
			}
		};

		gamesPanel.add(jboard, BorderLayout.CENTER);
		super.setLayout(new BorderLayout());
		super.add(gamesPanel, BorderLayout.CENTER);

		infoAreaPanel = new InfoArea(this);
		super.add(infoAreaPanel, BorderLayout.EAST);
	}

	protected Color getColor(int player) {
		return infoAreaPanel.getColors().get(player);
	}

	public void setEnabled(boolean enable) {
		jboard.setEnabled(enable);
	}

	@Override
	public void update(GameState<?, ?> state) {
		this.state = state;
		repaint();
	}

	@Override
	public void setGameViewCtrl(GameController<?, ?> control) {
		gameCtrl = control;
	}

	@Override
	public void showInfoMessage(String message) {
		infoAreaPanel.appendText(message);
	}

	public void clearMessages() {
		infoAreaPanel.clear();
	}

	protected abstract int getNumCols();

	protected abstract int getNumRows();

	protected abstract Integer getPosition(int row, int col);

	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);

	protected abstract void keyTyped(int keyCode);
	
	public void actionToShine(GameAction<?, ?> a) {
		jboard.addPoint(a.getFinCol(), a.getFinRow());
	}
	
	public void clearPoints() {
		jboard.clearPoints();
	}

}
