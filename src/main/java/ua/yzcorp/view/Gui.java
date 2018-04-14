package ua.yzcorp.view;

import ua.yzcorp.controller.*;
import ua.yzcorp.model.ArcadeMap;
import ua.yzcorp.model.Enemy;
import ua.yzcorp.model.MyTableModel;
import static ua.yzcorp.controller.Glob.HERO;
import static ua.yzcorp.model.Hero.heroPos;
import static ua.yzcorp.controller.Glob.MAPENEMIES;
import static ua.yzcorp.controller.Glob.MAP;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.*;
import java.util.List;

public class Gui {
	private static DefaultStat defaultStat = new DefaultStat();
	private static HeroManager heroManager = new HeroManager();
	private static JFrame frame;
	private static JTextField nameHeroTextField = new JTextField();
	private static String classHero;
	private static JButton createButton;
	private static JButton chooseButton;
	private static JTextPane textPane = new JTextPane();
	private static JPanel panelButtonCreateAndChoose = new JPanel();
	private static JPanel panelTableChoose = new JPanel();
	private static JPanel panelFieldCreate = new JPanel();
	private static ImagePanel mainPanel;

	private static MyTableModel tableModel = null;
	private static JTable allHeroesTable;
	private static JScrollPane scrollPane;
	private static Integer selectRow = null;

	private static StyledDocument doc;
	private static SimpleAttributeSet center;
	private static ImagePanel panelMap;
	private static CreateStartMap createMap = new CreateStartMap();
	private static JPanel[][] cell;
	private static BufferedImage heroImage;
	private static BufferedImage enemyImage;
	//TODO 3.3
	private static BufferedImage wonImage;
	private static Image image;
	private static int imageSize;
	private static int mapSize;

	private static JPanel heroInfoPanel = new JPanel();
	private static JProgressBar progressLevel = new JProgressBar();
	private static JProgressBar progressHP = new JProgressBar();

	private static ImagePanel gameOverPanel;
	private static ImagePanel winPanel;
	private static JButton exitButton = new JButton("EXIT");
	private static JButton tryAgainButton = new JButton("Try again");
	private static JPanel finalButtonPanel = new JPanel();
	private static JMenuItem switchItem;

	public static void startWithConsole() {
		Glob.onGui();
		if (frame == null) {
			start();
		} else {
			panelMap.setVisible(false);
			gameOverPanel.setVisible(false);
			winPanel.setVisible(false);
			panelTableChoose.setVisible(false);
			frame.setVisible(true);
			createButton.setText("Create");
			mainPanel.setVisible(true);
		}
		if (HERO != null) {
			createMap.run();
		}
	}

	public static void start() {
		Glob.onGui();
		initAllComp();
		panelButtonCreateAndChoose.add(createButton, new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 5), 10, 0 ));
		panelButtonCreateAndChoose.add(chooseButton, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 5 , 10, 10), 0, 0 ));
		mainPanel.add(panelButtonCreateAndChoose, new GridBagConstraints(0, 4, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 150 , 10, 150), 0, 0 ));
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		createHero();
		chooseHero();
	}

	private static void initAllComp() {
		newWindow();
		createButton = new JButton("Create");
		chooseButton = new JButton("Choose");
		mainPanel = new ImagePanel(new ImageIcon(Glob.PIC + "main.png").getImage(), frame.getWidth(), frame.getHeight());
		panelMap = new ImagePanel(new ImageIcon(Glob.PIC + "map.png").getImage(), frame.getWidth(), frame.getHeight());
		mainPanel.setLayout(new GridBagLayout());
		panelButtonCreateAndChoose.setLayout(new GridBagLayout());
		panelButtonCreateAndChoose.setBackground(new Color(0,0,0,200));

		createButton.addActionListener(new ButtonAction());
		createButton.setBorderPainted(false);
		createButton.setBackground(new Color(0, 0, 0, 0));
		createButton.setForeground(Color.WHITE);
		chooseButton.addActionListener(new ButtonAction());
		chooseButton.setBorderPainted(false);
		chooseButton.setBackground(new Color(0, 0, 0, 0));
		chooseButton.setForeground(Color.WHITE);

		panelFieldCreate.setBackground(new Color(0,0,0,200));
		panelFieldCreate.setLayout(new GridBagLayout());

		textPane.setEditable(false);
		textPane.setVisible(false);
		textPane.setBackground(new Color(0, 0, 0, 0));
		doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_JUSTIFIED);
		StyleConstants.setForeground(center, Color.WHITE);
		doc.setParagraphAttributes(0, doc.getLength(), center, true);
		panelFieldCreate.add(textPane, new GridBagConstraints(0, 3, 2, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));

		gameOverPanel = new ImagePanel(new ImageIcon(Glob.PIC + "die.png").getImage(), frame.getWidth(), frame.getHeight());
		winPanel = new ImagePanel(new ImageIcon(Glob.PIC + "won.png").getImage(), frame.getWidth(), frame.getHeight());

		JMenuBar menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(frame.getWidth(), 15));
		menuBar.add(createFileMenu());
		frame.setJMenuBar(menuBar);
	}

	private static JMenu createFileMenu() {
		JMenu menu = new JMenu("Menu");
		switchItem = new JMenuItem("Console view",
				new ImageIcon(Glob.PIC + "switch.png"));
		menu.add(switchItem);
		switchItem.addActionListener(e -> {
			if (HERO == null) {
				frame.dispose();
				Glob.onConsole();
				Console.start();
			} else if (heroPos[0] == 0 || heroPos[1] == 0 || heroPos[0] == mapSize - 1 || heroPos[1] == mapSize - 1) {
				HERO = null;
				frame.dispose();
				Glob.onConsole();
				Message.youWon();
				if (MainGame.finalChoose()) {
					Console.start();
				}
			} else if (HERO.getHP() > 0) {
				frame.dispose();
				Glob.onConsole();
				MainGame.startGame();
			} else if (HERO.getHP() <= 0) {
				HERO = null;
				frame.dispose();
				Glob.onConsole();
				Message.gameOver();
				if (MainGame.finalChoose()) {
					Console.start();
				}
			}
		});
		return menu;
	}

	public static class CreateStartMap extends Thread {
		@Override
		public void run() {
			super.run();
			createHeroInfoBar();
			try {
				mapSize = MAP.getSize();
				imageSize = panelMap.getWidth() / mapSize;
				heroImage = ImageIO.read(new File(Glob.PIC + HERO.getClassHero().toLowerCase() + ".png"));
				panelMap.setLayout(new GridLayout(mapSize, mapSize, 2, 2));
				cell = new JPanel[mapSize][mapSize];
				frame.getContentPane().add(heroInfoPanel, BorderLayout.PAGE_END);
				updateMap();
				mainPanel.setVisible(false);
				frame.getContentPane().add(panelMap, BorderLayout.CENTER);
				heroInfoPanel.setVisible(false);
				heroInfoPanel.setVisible(true);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createHeroInfoBar() {
		heroInfoPanel.setLayout(new BoxLayout(heroInfoPanel, BoxLayout.Y_AXIS));
		heroInfoPanel.setBackground(Color.WHITE);
		progressHP.setForeground(new Color(128, 21, 21, 150));
		progressHP.setMaximum(HERO.getMustHP());
		progressHP.setMinimum(0);
		progressHP.setValue(HERO.getHP());
		progressHP.setStringPainted(true);
		progressHP.setBorderPainted(false);
		progressHP.setPreferredSize(new Dimension(frame.getWidth(), 15));
		progressLevel.setForeground(new Color(13, 77, 77, 150));
		progressLevel.setMaximum(HERO.getMustLevel()[1]);
		progressLevel.setMinimum(HERO.getMustLevel()[0]);
		progressLevel.setValue(HERO.getExp());
		progressLevel.setStringPainted(true);
		progressLevel.setBorderPainted(false);
		progressLevel.setPreferredSize(new Dimension(frame.getWidth(), 15));
		heroInfoPanel.add(progressHP);
		heroInfoPanel.add(progressLevel);
		progressLevel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		progressHP.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	}

	public static void finalView(ImagePanel finalPanel) {
		panelMap.setVisible(false);
		heroInfoPanel.setVisible(false);
		frame.getContentPane().add(finalPanel, BorderLayout.CENTER);
		frame.pack();
		tryAgainButton.addActionListener(new finalButtonAction());
		exitButton.addActionListener(new finalButtonAction());
		tryAgainButton.setBackground(new Color(0, 0, 0, 0));
		tryAgainButton.setBorderPainted(false);
		exitButton.setBorderPainted(false);
		exitButton.setBackground(new Color(0, 0, 0, 0));
		finalButtonPanel.setLayout(new GridBagLayout());
		finalButtonPanel.setBackground(new Color(255, 255, 255, 100));
		finalButtonPanel.add(tryAgainButton, new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 5), 0, 0 ));
		finalButtonPanel.add(exitButton, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 5 , 10, 10), 30, 0 ));
		finalPanel.setLayout(new GridBagLayout());
		finalPanel.add(finalButtonPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(525, 150 , 10, 150), 0, 0 ));
		finalPanel.setVisible(true);
	}

	public static void updateMap() {
		try {
			panelMap.setVisible(false);
			panelMap.removeAll();
			for (int i = 0; i < cell.length; i++) {
				for (int j = 0; j < cell[i].length; j++) {
					cell[i][j] = new JPanel();
					cell[i][j].setLayout(new BorderLayout());
					String[] levelEnemy = findEnemy(new int[]{i, j});
					if (levelEnemy != null && !Arrays.equals(heroPos, new int[]{i, j})) {
						enemyImage = ImageIO.read(new File(Glob.PIC + levelEnemy[1] + ".png"));
						image = enemyImage.getScaledInstance(imageSize, imageSize, Image.SCALE_REPLICATE);
						JLabel picLabel = new JLabel(new ImageIcon(image));
						cell[i][j].add(picLabel, BorderLayout.CENTER);
						cell[i][j].setBackground(new Color(255, 255, 255, 100));
					} else if (Arrays.equals(heroPos, new int[]{i, j})) {
						image = heroImage.getScaledInstance(imageSize, imageSize, Image.SCALE_REPLICATE);
						JLabel picLabel = new JLabel(new ImageIcon(image));
						cell[i][j].add(picLabel, BorderLayout.CENTER);
						cell[i][j].setBackground(new Color(0, 0, 0, 0));
					} else {
						cell[i][j].setBackground(new Color(255, 255, 255, 100));
					}
					cell[i][j].addMouseListener(new moveListener());
					panelMap.add(cell[i][j]);
				}
			}
			panelMap.setVisible(true);
			heroInfoPanel.setVisible(true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static String[] findEnemy(int[] ints) {
		List<Enemy> allEnemies = MAPENEMIES;
		for (int i = 0; i < allEnemies.size(); i++) {
			if (Arrays.equals(allEnemies.get(i).getEnemyPos(), ints)) {
				return new String[] {String.valueOf(i), String.valueOf(allEnemies.get(i).getLevel())};
			}
		}
		return null;
	}

	private static void createHero() {
		String[] classHero = {"Human", "Orc", "Elf", "Dwarf"};
		JComboBox<String> comboBox = new JComboBox<>(classHero);
		comboBox.addActionListener(new ButtonAction());
		JLabel nickNameLabel = new JLabel("Nickname");
		JLabel classHeroLabel = new JLabel("Hero class");

		classHeroLabel.setForeground(Color.WHITE);
		nickNameLabel.setForeground(Color.WHITE);

		panelFieldCreate.add(nickNameLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		panelFieldCreate.add(nameHeroTextField, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		panelFieldCreate.add(classHeroLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		panelFieldCreate.add(comboBox, new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		mainPanel.add(panelFieldCreate, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 150 , 10, 150), 0, 0 ));
		panelFieldCreate.setVisible(false);
	}

	private static void chooseHero() {
		if (tableModel != null) {
			panelTableChoose.remove(scrollPane);
		}
		tableModel = new MyTableModel(heroManager.getAllTarget());					//init choose  heroes table
		allHeroesTable = new JTable(tableModel);
		scrollPane = new JScrollPane(allHeroesTable);
		scrollPane.setPreferredSize(new Dimension(300, 300));
		panelTableChoose.setLayout(new BorderLayout());
		panelTableChoose.add(scrollPane, BorderLayout.CENTER);
		tableSelect();
		mainPanel.add(panelTableChoose, new GridBagConstraints(0, 0, 2, 3, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 150 , 10, 150), 0, 0 ));
		panelTableChoose.setVisible(false);
	}

	public static class moveListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JPanel clicked = (JPanel) e.getSource();
			mainLoop: for (int i = 0; i < cell.length; i++) {
				for (int j = 0; j < cell[i].length ; j++) {
					if (cell[i][j] == clicked) {
						if (i == heroPos[0] && j == heroPos[1] - 1) {
							setNewPosHero(new int[] {i, j});
							break mainLoop;
						} else if (i == heroPos[0] && j == heroPos[1] + 1) {
							setNewPosHero(new int[] {i, j});
							break mainLoop;
						} else if (i == heroPos[0] - 1 && j == heroPos[1]) {
							setNewPosHero(new int[] {i, j});
							break mainLoop;
						} else if (i == heroPos[0] + 1 && j == heroPos[1]) {
							setNewPosHero(new int[] {i, j});
							break mainLoop;
						}
					}
				}
			}
		}
		void setNewPosHero(int[] newPos) {
			String levelEnemy[] = findEnemy(newPos);
			if (levelEnemy != null) {
				try {
					oneMove(fightCell(), emptyCell(), newPos);
					enemyImage = ImageIO.read(new File(Glob.PIC + levelEnemy[1] + ".png"));
					image = enemyImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
					Object[] options = {"Fight", "Run"};
					int choose = JOptionPane.showOptionDialog(null,
							MAPENEMIES.get(Integer.parseInt(levelEnemy[0])).toString(),
							"Information about the enemy", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
							new ImageIcon(image), options, options[0]);
					switch (choose) {
						case 0:
							JOptionPane.showMessageDialog(null, "I'll kill you hardly.", "Start fight" ,JOptionPane.INFORMATION_MESSAGE, new ImageIcon(image));
							fight(levelEnemy[0], newPos);
							break;
						default:
							Random random = new Random();
							int[] trueOrFalse = {1, 0};
							switch (trueOrFalse[random.nextInt(2)]) {
								case 1:
									JOptionPane.showMessageDialog(null, "You managed to escape from the fight", "Hooray :)", JOptionPane.INFORMATION_MESSAGE);
									oneMove(enemyCell(newPos), heroCell(), newPos);
									break;
								default:
									JOptionPane.showMessageDialog(null, "You could not escape. This creature has caught up with you.", "Oops :(", JOptionPane.WARNING_MESSAGE);
									fight(levelEnemy[0], newPos);
							}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				oneMove(heroCell(), emptyCell(), newPos);
				heroPos[0] = newPos[0];
				heroPos[1] = newPos[1];
				maybeWinner();
			}
		}

		boolean maybeWinner() {
			if (heroPos[0] == 0 || heroPos[1] == 0 || heroPos[0] == mapSize - 1 || heroPos[1] == mapSize - 1) {
				try {
					wonImage = ImageIO.read(new File(Glob.PIC + "won.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				//TODO 1.1
				image = wonImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);
				winPanel = new ImagePanel(image, frame.getWidth(), frame.getHeight());
				finalView(winPanel);
				return true;
			} else {
				return false;
			}
		}

		void fight(String levelEnemy, int[] newPos) {
			if (MainGame.startFight(MAPENEMIES.get(Integer.parseInt(levelEnemy)))) {
				MainGame.dropItem(MAPENEMIES.get(Integer.parseInt(levelEnemy)));
				int pickUP = JOptionPane.showConfirmDialog(null,
						Glob.INFORM.toString(), "Pick Up Drop", JOptionPane.YES_NO_OPTION);
				switch (pickUP) {
					case 0:
						MainGame.pickUpDrop(MAPENEMIES.get(Integer.parseInt(levelEnemy)), MAP.getRandomDrop());
						MAPENEMIES.remove(Integer.parseInt(levelEnemy));
						oneMove(heroCell(), emptyCell(), newPos);
						heroPos[0] = newPos[0];
						heroPos[1] = newPos[1];
						if (maybeWinner()) { }
						else if (MainGame.levelUp()) {
							MAP.updateMainInfo();
							createMap.run();
							JOptionPane.showMessageDialog(null, Glob.HERO.toString(), "LEVEL UP", JOptionPane.INFORMATION_MESSAGE);
						}
					default:
						break;
				}
			} else {
				gameOverPanel = new ImagePanel(new ImageIcon(Glob.PIC + "die.png").getImage(), frame.getWidth(), frame.getHeight());
				finalView(gameOverPanel);
			}
			progressHP.setValue(HERO.getHP());
			progressLevel.setValue(HERO.getExp());
		}

		private void oneMove(JPanel newCell, JPanel curCell, int[] newPos) {
			panelMap.setVisible(false);
			panelMap.remove(newPos[0] * mapSize + newPos[1]);
			panelMap.add(newCell, newPos[0] * mapSize + newPos[1]);
			panelMap.remove(heroPos[0] * mapSize + heroPos[1]);
			panelMap.add(curCell, heroPos[0] * mapSize + heroPos[1]);
			panelMap.setVisible(true);
		}

		private JPanel emptyCell() {
			JPanel empty = new JPanel();
			empty.setBackground(new Color(255, 255, 255, 100));
			empty.addMouseListener(new moveListener());
			cell[heroPos[0]][heroPos[1]] = empty;
			return empty;
		}

		private JPanel fightCell() {
			JPanel fight = null;
			try {
				fight = new JPanel();
				fight.setLayout(new BorderLayout());
				BufferedImage fightImage = ImageIO.read(new File(Glob.PIC + "fight.png"));
				image = fightImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
				JLabel picLabel = new JLabel(new ImageIcon(image));
				fight.add(picLabel, BorderLayout.CENTER);
				fight.setBackground(new Color(255, 255, 255, 100));
				fight.addMouseListener(new moveListener());

			} catch (IOException e) {
				e.printStackTrace();
			}
			return fight;
		}

		private JPanel enemyCell(int[] newPos) {
			JPanel enemy = new JPanel();
			enemy.setLayout(new BorderLayout());
			image = enemyImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
			JLabel picLabel = new JLabel(new ImageIcon(image));
			enemy.add(picLabel, BorderLayout.CENTER);
			enemy.setBackground(new Color(255, 255, 255, 100));
			enemy.addMouseListener(new moveListener());
			cell[newPos[0]][newPos[1]] = enemy;
			return enemy;
		}

		private JPanel heroCell() {
			JPanel hero = new JPanel();
			hero.setLayout(new BorderLayout());
			image = heroImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
			JLabel picLabel = new JLabel(new ImageIcon(image));
			hero.add(picLabel, BorderLayout.CENTER);
			hero.setBackground(new Color(0, 0, 0, 0));
			return hero;
		}
	}

	public static class finalButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (((JButton) e.getSource()).getText().equals("EXIT")) {
				Message.goodBye();
			} else {
				HERO = null;
				panelMap.setVisible(false);
				gameOverPanel.setVisible(false);
				winPanel.setVisible(false);
				createButton.setText("Create");
				panelTableChoose.setVisible(false);
				mainPanel.setVisible(true);
			}
		}
	}

	public static class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				if (((JButton) e.getSource()).getText().equals("Create")) {
					switchItem.setEnabled(false);
					createButton.setText("Save");
					panelTableChoose.setVisible(false);
					panelFieldCreate.setVisible(true);
				} else if (((JButton) e.getSource()).getText().equals("Save")) {
					String nameHero = nameHeroTextField.getText();
					if (nameHero == null || nameHero.isEmpty()) {
						JOptionPane.showMessageDialog(null, heroManager.emptyName(), "Error", JOptionPane.ERROR_MESSAGE);
					} else if (!HeroManager.checkName(nameHero)) {
						JOptionPane.showMessageDialog(null, heroManager.existsName(), "Error", JOptionPane.ERROR_MESSAGE);
					} else if (classHero == null || classHero.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Choose a hero class", "Warning", JOptionPane.WARNING_MESSAGE);
					} else {
						HERO = Console.getHero(classHero, nameHero, 1, 0, null, 0);
						heroManager.save(HERO, ConnectSQL.getConnection());
						createButton.setText("Start");
						panelFieldCreate.setVisible(false);
					}
				} else if (((JButton) e.getSource()).getText().equals("Choose")) {
					switchItem.setEnabled(false);
					chooseHero();
					if (tableModel.getHeroes().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Your heroes are empty. Create a new hero", "Warning", JOptionPane.WARNING_MESSAGE);
					} else {
						createButton.setText("Create");
						panelFieldCreate.setVisible(false);
						panelTableChoose.setVisible(true);
					}
				} else if (((JButton) e.getSource()).getText().equals("Start")) {
					MAP = new ArcadeMap();
					if (HERO != null) {
						mainPanel.setVisible(false);
						createMap.run();
						switchItem.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(null, "Choose a hero", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			} else if (e.getSource() instanceof JComboBox) {
				JComboBox box = (JComboBox)e.getSource();
				classHero = (String)box.getSelectedItem();
				try {
					doc.remove(0, doc.getLength());
					doc.insertString(0, defaultStat.toString(classHero), center);
					textPane.setVisible(true);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private static void tableSelect() {
		ListSelectionModel selectionModel = allHeroesTable.getSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionModel.addListSelectionListener(e -> {
			selectRow = allHeroesTable.getSelectedRow();
			HERO = heroManager.getAllTarget().get(selectRow);
			panelButtonCreateAndChoose.setVisible(false);
			createButton.setText("Start");
			panelButtonCreateAndChoose.setVisible(true);
		});
	}

	private static JFrame newWindow() {
		frame = new JFrame("Swingy");
		//TODO 2.2
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight() - screenSize.getHeight() * 0.1;
		int frameSize = Math.toIntExact(Math.round(height));
		frame.setSize(frameSize, frameSize);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		return frame;
	}

	public static class ImagePanel extends JPanel {
		private Image	img;
		private int		width;
		private int		height;

		ImagePanel(Image img, int width, int height) {
			super();
			this.img = img;
			this.width = width;
			this.height = height;

			Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, width, height, null);
		}
	}
}