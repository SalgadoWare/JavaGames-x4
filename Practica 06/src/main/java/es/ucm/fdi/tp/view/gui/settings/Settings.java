package es.ucm.fdi.tp.view.gui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.gui.gameWindow.GameWindowController;
import es.ucm.fdi.tp.view.gui.gameWindow.GameWindow;
import es.ucm.fdi.tp.view.gui.gameWindow.GameWindow.PlayerMode;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

public class Settings extends JPanel implements SettingsUse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton randButton, smartButton, exitButton, startButton, ok_comeBack, avoidTurnButton;
	private JComboBox<PlayerMode> comboBox;
	private JSpinner timerSpinner, threadsSpinner, comeBackStateSpinner;
	private JPanel aiPanel;
	private JButton stopIA;

	/**
	 * We need a reference to GameWindowController to interact with GameWindow
	 */
	@SuppressWarnings("rawtypes")
	private GameWindowController my_window;

	@SuppressWarnings("rawtypes")
	public Settings(GameWindowController gameWindowCtrl) {
		this.my_window = gameWindowCtrl;
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		initGui();
	}

	private void initGui() {
		addButtons();
		addJComboBox();
		addSettingsForAI();
		addSettingsToComeBackStates();
	}

	private void addSettingsToComeBackStates() {
		this.add(new JLabel("    Come back n sates:"));

		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 5000, 1);
		comeBackStateSpinner = new JSpinner(model);
		this.add(comeBackStateSpinner);

		ok_comeBack = new JButton("Ok");
		ok_comeBack.addActionListener(new ActionListener() {

			@SuppressWarnings({"rawtypes" })
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<GameState> a = GameWindow.all_states;
				a.size();
				int size = GameWindow.all_states.size();
				int state_n = size - (int) comeBackStateSpinner.getValue() - 1;
				if (state_n == 0) {
					my_window.restartGame();
				} else {
					if (state_n < 0) {
						my_window.setStateToComeBack(-1);
					} else {
						my_window.setStateToComeBack(state_n);
					}
				}
			}
		});

		this.add(ok_comeBack);
	}

	private void addSettingsForAI() {

		aiPanel = new JPanel();
		aiPanel.setLayout(new BoxLayout(aiPanel, BoxLayout.X_AXIS));
		aiPanel.setBackground(Color.green);

		aiPanel.add(new JLabel("  "));

		JLabel brain = new JLabel(new ImageIcon(Utils.loadImage("brain.png")));
		aiPanel.add(brain);
		this.add(aiPanel);

		int max_size = Runtime.getRuntime().availableProcessors();

		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, max_size, 1);
		threadsSpinner = new JSpinner(model);
		this.add(threadsSpinner);

		this.add(new JLabel("  ->threads   "));

		JLabel time = new JLabel(new ImageIcon(Utils.loadImage("timer.png")));
		this.add(time);

		SpinnerNumberModel model2 = new SpinnerNumberModel(1000, 500, 5000, 500);
		timerSpinner = new JSpinner(model2);
		this.add(timerSpinner);
		this.add(new JLabel("  ->ms.    "));

		stopIA = new JButton(new ImageIcon(Utils.loadImage("stop.png")));
		stopIA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				my_window.cancelAImove();
			}
		});

		this.add(stopIA);

	}

	public int getTimerValue() {
		return (int) timerSpinner.getValue();
	}

	public int getThreadsValue() {
		return (int) threadsSpinner.getValue();
	}

	public void setAIthought(boolean bool) {
		if (bool) {
			aiPanel.setBackground(Color.yellow);
		} else {
			aiPanel.setBackground(Color.green);
		}
	}

	private void addJComboBox() {

		this.add(new JLabel(" Player Mode: "));
		PlayerMode[] items = PlayerMode.values();
		DefaultComboBoxModel<PlayerMode> combo_default = new DefaultComboBoxModel<PlayerMode>(items) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectedItem(Object o) {
				super.setSelectedItem(o);

				switch (o.toString()) {
				case "random":
					my_window.changePlayerMode(PlayerMode.RANDOM);
					my_window.randPlayerMove();
					break;
				case "manual":
					my_window.changePlayerMode(PlayerMode.MANUAL);
					break;
				case "smart":
					my_window.changePlayerMode(PlayerMode.SMART);
					my_window.smartPlayerMove();
					break;
				default:
					break;
				}
			}
		};

		this.comboBox = new JComboBox<>(combo_default);
		this.add(comboBox);
	}

	private void addButtons() {

		randButton = new JButton();
		randButton.setIcon(new ImageIcon(Utils.loadImage("dice.png")));
		randButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				my_window.randPlayerMove();
			}
		});

		this.add(randButton);
		smartButton = new JButton();
		smartButton.setIcon(new ImageIcon(Utils.loadImage("nerd.png")));
		smartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				my_window.smartPlayerMove();
			}
		});

		this.add(smartButton);

		exitButton = new JButton();
		exitButton.setIcon(new ImageIcon(Utils.loadImage("exit.png")));
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final JFrame auxFrame = new JFrame("Are you sure to exit?");
				JButton no = new JButton("No!");
				JButton ok = new JButton("Ok");
				auxFrame.getContentPane().setLayout(new BorderLayout());
				auxFrame.getContentPane().add(ok, BorderLayout.CENTER);
				auxFrame.getContentPane().add(no, BorderLayout.PAGE_END);
				auxFrame.setSize(1500, 500);
				auxFrame.setLocation(80, 80);
				auxFrame.setVisible(true);
				auxFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				no.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						auxFrame.dispose();
					}
				});

				ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
						auxFrame.dispose();
					}
				});

			}
		});

		this.add(exitButton);
		startButton = new JButton();
		startButton.setIcon(new ImageIcon(Utils.loadImage("restart.png")));
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				my_window.restartGame();
			}
		});

		this.add(startButton);

		avoidTurnButton = new JButton("Avoid Turn");
		avoidTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				my_window.avoidTurn();
			}
		});
		
		avoidTurnButton.setSize(200, 200);
		this.add(avoidTurnButton);
	}

	public void setEnabled(boolean bool) {
		super.setEnabled(bool);
		randButton.setEnabled(bool);
		smartButton.setEnabled(bool);
		stopIA.setEnabled(bool);
	}

	@Override
	public void IAbuttonWasPressed() {
		stopIA.setEnabled(true);
		ok_comeBack.setEnabled(false);
	}
}
