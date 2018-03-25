package ua.yzcorp.view;

import ua.yzcorp.controller.ConnectSQL;
import ua.yzcorp.controller.DefaultStat;
import ua.yzcorp.controller.Glob;
import ua.yzcorp.controller.HeroManager;
import ua.yzcorp.model.ImagePanel;
import ua.yzcorp.model.MyTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private static ImagePanel mainPanel = new ImagePanel(new ImageIcon(Glob.PIC + "1.png").getImage());
	private static MyTableModel tableModel;

	public static void start() {
		frame = newWindow();
		//panelCreate.setLayout(new GridBagLayout());
		frame.setLayout(new BorderLayout());
		mainPanel.setLayout(new GridBagLayout());
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		//frame.add(panelCreate, BorderLayout.CENTER);
		panelButtonCreateAndChoose.setLayout(new GridBagLayout());
		panelButtonCreateAndChoose.setBackground(new Color(0,0,0,200));
		panelButtonCreateAndChoose.add(createButton, new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		panelButtonCreateAndChoose.add(chooseButton, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		mainPanel.add(panelButtonCreateAndChoose, new GridBagConstraints(0, 4, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 250 , 10, 250), 0, 0 ));
		createButton.addActionListener(new ButtonAction());
		chooseButton.addActionListener(new ButtonAction());
		mainPanel.setVisible(true);
		createHero();
		chooseHero();
	}

	static void createHero() {
		String[] classHero = {"Human", "Orc", "Elf", "Dwarf"};
		JComboBox<String> comboBox = new JComboBox<>(classHero);
		comboBox.addActionListener(new ButtonAction());
		JLabel nickNameLabel = new JLabel("Nickname");
		JLabel classHeroLabel = new JLabel("Hero class");

		classHeroLabel.setForeground(Color.WHITE);
		nickNameLabel.setForeground(Color.WHITE);
		panelFieldCreate.setBackground(new Color(0,0,0,200));
		panelFieldCreate.setLayout(new GridBagLayout());

		panelFieldCreate.add(nickNameLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 10), 0, 0 ));
		panelFieldCreate.add(nameHeroTextField, new GridBagConstraints(1, 0, 1, 1, 0, 0,
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
				new Insets(10, 250 , 10, 250), 0, 0 ));
		panelFieldCreate.setVisible(false);
	}

	static void chooseHero() {
		tableModel = new MyTableModel(heroManager.getAllTarget());
		JTable allHerosTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(allHerosTable);
		scrollPane.setPreferredSize(new Dimension(300, 300));
		panelTableChoose.setLayout(new BorderLayout());
		panelTableChoose.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(panelTableChoose, new GridBagConstraints(0, 0, 2, 3, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 250 , 10, 250), 0, 0 ));
		panelTableChoose.setVisible(false);
	}

	public static class ButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				if (!((JButton) e.getSource()).getText().equals("Save") && !((JButton) e.getSource()).getText().equals("Choose")) {
					createButton.setText("Save");
					panelTableChoose.setVisible(false);
					panelFieldCreate.setVisible(true);
				} else if (((JButton) e.getSource()).getText().equals("Save")) {
					panelFieldCreate.setVisible(false);
					createButton.setText("Create");
					nameHero = nameHeroTextField.getText();
					if (nameHero == null || nameHero.isEmpty()) {
						JOptionPane.showMessageDialog(null, heroManager.emptyName());
					} else if (!HeroManager.checkName(nameHero)) {
						JOptionPane.showMessageDialog(null, heroManager.existsName());
					} else if (classHero == null || classHero.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Choose a hero class");
					} else {
						Glob.hero = Console.getHero(classHero, nameHero, 1, 0, null, 0);
						heroManager.save(Glob.hero, ConnectSQL.getConnection());
					}
				} else {
					tableModel.setHeroes(heroManager.getAllTarget());
					tableModel.fireTableDataChanged();
					panelFieldCreate.setVisible(false);
					panelTableChoose.setVisible(true);
				}
			} else if (e.getSource() instanceof JComboBox) {
				JComboBox box = (JComboBox)e.getSource();
				classHero = (String)box.getSelectedItem();

				StyledDocument doc = textPane.getStyledDocument();
				SimpleAttributeSet center = new SimpleAttributeSet();
				StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
				StyleConstants.setForeground(center, Color.WHITE);
				doc.setParagraphAttributes(0, doc.getLength(), center, true);
				textPane.setEditable(false);
				textPane.setBackground(new Color(0, 0, 0, 0));
				try {
					doc.remove(0, doc.getLength());
					doc.insertString(0, defaultStat.toString(classHero), center);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				panelFieldCreate.add(textPane, new GridBagConstraints(0, 3, 2, 1, 0, 0,
						GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
						new Insets(10, 10 , 10, 10), 0, 0 ));
				panelFieldCreate.setVisible(true);
			}
		}
	}

	public static JFrame newWindow() {
		JFrame frame = new JFrame("Swingy");
		frame.setSize(900, 900);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		return frame;
	}
}
