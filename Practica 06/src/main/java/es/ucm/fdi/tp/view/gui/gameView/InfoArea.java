package es.ucm.fdi.tp.view.gui.gameView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import es.ucm.fdi.tp.extra.jcolor.*;

/**
 * this class is a field of RectBoardGameView, this class contains the color table, and the JTextArea 
 * @author alex
 *
 */
public class InfoArea extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea textArea;
	private Map<Integer, Color> colors;
	private ColorChooser colorChooser;
	private MyTableModel tModel;
	
	/*
	 * We need a reference to GameView in color changes (method changeColor(int)
	 */
	private GameView<?, ?> gameView; 
	
	
	public InfoArea(GameView<?, ?> gameView) {
		this.colors = new HashMap<>();
		this.gameView = gameView;
		initGui();
	}

	private void initGui() {
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel gameM = new JLabel("Game messages");
		this.add(gameM);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		// We create the ScrollPane and instantiate it with the TextArea as an
		// argument
		// along with two constants that define the behaviour of the scrollbars.
		JScrollPane scrollArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// We then set the preferred size of the scrollpane.
		scrollArea.setPreferredSize(new Dimension(200, 200));
		this.add(scrollArea);

		JLabel playerInfo = new JLabel("Player Info:");
		this.add(playerInfo);
		
		addColorsTable();
	}

	private void addColorsTable() {
		colors = new HashMap<>();
		colorChooser = new ColorChooser(new JFrame(), "Choose a color", Color.black);
		tModel = new MyTableModel();
		tModel.getRowCount();
		final JTable table = new JTable(tModel) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1)
					comp.setBackground(colors.get(row));
				else
					comp.setBackground(Color.WHITE);

				comp.setForeground(Color.BLACK);
				return comp;
			}
		};

		table.setToolTipText("Click on a row to change the color of a player");

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
				}
			}

		});

		//Default colors
		colors.put(0, Color.blue);
		colors.put(1, Color.red);

		this.add(table);
	}

	private void changeColor(int row) {
		colorChooser.setSelectedColorDialog(colors.get(row));
		colorChooser.openDialog();
		if (colorChooser.getColor() != null) {
			colors.put(row, colorChooser.getColor());
			gameView.repaint();
			repaint();
		}
	}

	public Map<Integer, Color> getColors() {
		return colors;
	}

	public void appendText(String text) {
		this.textArea.append(text);
	}

	public void clear() {
		this.textArea.setText("");
	}
}
