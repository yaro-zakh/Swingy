package ua.yzcorp.view;

import ua.yzcorp.controller.ConnectSQL;
import ua.yzcorp.controller.DefaultStat;
import ua.yzcorp.controller.Glob;
import ua.yzcorp.controller.HeroManager;
import ua.yzcorp.model.ArcadeMap;
import ua.yzcorp.model.Enemy;
import ua.yzcorp.model.MyTableModel;
import static ua.yzcorp.controller.Glob.hero;
import static ua.yzcorp.model.Hero.heroPos;

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
import java.util.Arrays;
import java.util.List;

public class Gui {
	private static DefaultStat defaultStat = new DefaultStat();
	private static HeroManager heroManager = new HeroManager();
	private static JFrame frame;
	private static JTextField nameHeroTextField = new JTextField();
	private static String nameHero, classHero;
	private static JButton createButton = new JButton("Create");
	private static JButton chooseButton = new JButton("Choose");
	private static JTextPane textPane = new JTextPane();
	private static JPanel panelButtonCreateAndChoose = new JPanel();
	private static JPanel panelTableChoose = new JPanel();
	private static JPanel panelFieldCreate = new JPanel();
	private static ImagePanel mainPanel;
	private static MyTableModel tableModel;
	private static JTable allHeroesTable;
	private static StyledDocument doc;
	private static SimpleAttributeSet center;
	private static ImagePanel panelMap;
	private static CreateStartMap createMap = new CreateStartMap();
	private static JPanel[][] cell;

	private static BufferedImage heroImage;
	private static BufferedImage enemyImage;
	private static Image image;
	private static ArcadeMap arcadeMap;


	public static void start() {
		initAllComp();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		panelButtonCreateAndChoose.add(createButton, new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		panelButtonCreateAndChoose.add(chooseButton, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		mainPanel.add(panelButtonCreateAndChoose, new GridBagConstraints(0, 4, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 150 , 10, 150), 0, 0 ));
		mainPanel.setVisible(true);
		createHero();
		chooseHero();
	}

	public static class CreateStartMap extends Thread {
		@Override
		public void run() {
			try {
				super.run();
				arcadeMap = new ArcadeMap();
				int mapSize = arcadeMap.getSize();
				String levelEnemy;
				int imageSize = frame.getWidth() / mapSize;
				heroPos = new int[]{Math.round(mapSize / 2), Math.round(mapSize / 2)};
				heroImage = ImageIO.read(new File(Glob.PIC + hero.getClassHero().toLowerCase() + ".png"));
				panelMap.setLayout(new GridLayout(mapSize, mapSize, 2, 2));
				cell = new JPanel[mapSize][mapSize];
				for (int i = 0; i < cell.length; i++) {
					for (int j = 0; j < cell[i].length; j++) {
						cell[i][j] = new JPanel();
						cell[i][j].setLayout(new BorderLayout());
						levelEnemy = findEnemy(new int[] {i, j});
						if (levelEnemy != null && !Arrays.equals(heroPos, new int[]{i, j})) {
							enemyImage = ImageIO.read(new File(Glob.PIC + levelEnemy + ".png"));
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
				mainPanel.setVisible(false);
				frame.getContentPane().add(panelMap, BorderLayout.CENTER);
				panelMap.setVisible(true);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private String findEnemy(int[] ints) {
			List<Enemy> allEnemies = arcadeMap.getMapEnemies();
			for (Enemy enemy: allEnemies) {
				if (Arrays.equals(enemy.getEnemyPos(), ints)) {
					return String.valueOf(enemy.getLevel());
				}
			}
			return null;
		}
	}

	private static void initAllComp() {
		frame = newWindow();
		mainPanel = new ImagePanel(new ImageIcon(Glob.PIC + "main.png").getImage(), frame.getWidth(), frame.getHeight());
		panelMap = new ImagePanel(new ImageIcon(Glob.PIC + "map.png").getImage(), frame.getWidth(), frame.getHeight());
		mainPanel.setLayout(new GridBagLayout());

		panelButtonCreateAndChoose.setLayout(new GridBagLayout());						//two button create and choose
		panelButtonCreateAndChoose.setBackground(new Color(0,0,0,200));
		createButton.addActionListener(new ButtonAction());							//add Listener
		chooseButton.addActionListener(new ButtonAction());

		panelFieldCreate.setBackground(new Color(0,0,0,200));			//init create hero field
		panelFieldCreate.setLayout(new GridBagLayout());

		textPane.setEditable(false);											//init and add text about Default stat Heroes
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
		tableModel = new MyTableModel(heroManager.getAllTarget());					//init choose  heroes table
		allHeroesTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(allHeroesTable);
		scrollPane.setPreferredSize(new Dimension(300, 300));
		panelTableChoose.setLayout(new BorderLayout());
		panelTableChoose.add(scrollPane, BorderLayout.CENTER);
		tableSelect();
		mainPanel.add(panelTableChoose, new GridBagConstraints(0, 0, 2, 3, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 150 , 10, 150), 0, 0 ));
		panelTableChoose.setVisible(false);
	}

	public static class moveListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			cell[4][4].setBackground(Color.blue);
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}

	public static class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				if (!((JButton) e.getSource()).getText().equals("Save") && !((JButton) e.getSource()).getText().equals("Choose") &&
						!((JButton) e.getSource()).getText().equals("Start")) {
					createButton.setText("Save");
					panelTableChoose.setVisible(false);
					panelFieldCreate.setVisible(true);
				} else if (((JButton) e.getSource()).getText().equals("Save")) {
					nameHero = nameHeroTextField.getText();
					if (nameHero == null || nameHero.isEmpty()) {
						JOptionPane.showMessageDialog(null, heroManager.emptyName());
					} else if (!HeroManager.checkName(nameHero)) {
						JOptionPane.showMessageDialog(null, heroManager.existsName());
					} else if (classHero == null || classHero.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Choose a hero class");
					} else {
						hero = Console.getHero(classHero, nameHero, 1, 0, null, 0);
						heroManager.save(hero, ConnectSQL.getConnection());
						createButton.setText("Start");
						panelFieldCreate.setVisible(false);
					}
				} else if (((JButton) e.getSource()).getText().equals("Choose")){
					createButton.setText("Start");
					tableModel.setHeroes(heroManager.getAllTarget());
					tableModel.fireTableDataChanged();
					panelFieldCreate.setVisible(false);
					panelTableChoose.setVisible(true);
				} else if (((JButton) e.getSource()).getText().equals("Start")){
					panelTableChoose.setVisible(false);
					panelButtonCreateAndChoose.setVisible(false);
					createMap.start();
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
			int selectRow = allHeroesTable.getSelectedRow();
			hero = heroManager.getAllTarget().get(selectRow);
			createButton.setText("Start");
		});
	}

	private static JFrame newWindow() {
		JFrame frame = new JFrame("Swingy");
		frame.setSize(700, 700);
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

		public ImagePanel(Image img, int width, int height) {
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
