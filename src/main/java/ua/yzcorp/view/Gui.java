package ua.yzcorp.view;

import ua.yzcorp.controller.ConnectSQL;
import ua.yzcorp.controller.DefaultStat;
import ua.yzcorp.controller.Glob;
import ua.yzcorp.controller.HeroManager;
import ua.yzcorp.model.Hero;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Gui {
	private static DefaultStat defaultStat = new DefaultStat();
	private static HeroManager heroManager = new HeroManager();
	private static JFrame frame;
	private static JPanel panel;
	private static JTextField nameHeroTextField = new JTextField();
	private static String nameHero, classHero;
	private static JButton createButton = new JButton("Create");
	private static JButton chooseButton = new JButton("Choose");
	private static JTextPane textPane = new JTextPane();

	public static void start() {
		frame = newWindow();
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		panel.add(createButton, new GridBagConstraints(0, 3, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 250 , 10, 10), 0, 0 ));
		panel.add(chooseButton, new GridBagConstraints(1, 3, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10 , 10, 250), 0, 0 ));
		createButton.addActionListener(new CreateButtonAction());
		frame.setVisible(true);
	}

	static void createHero() {
		String[] classHero = {"Human", "Orc", "Elf", "Dwarf"};
		JComboBox<String> comboBox = new JComboBox<>(classHero);
		comboBox.addActionListener(new CreateButtonAction());
		panel.add(new JLabel("Nickname"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 250 , 10, 5), 0, 0 ));
		panel.add(nameHeroTextField, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0 , 10, 250), 0, 0 ));
		panel.add(new JLabel("Class"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 250 , 10, 5), 0, 0 ));
		panel.add(comboBox, new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0 , 10, 250), 0, 0 ));
		createButton.setText("Save");
		frame.setVisible(true);
	}

	public static Hero chooseHero() {
		return null;
	}

	public static class CreateButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				if (!((JButton) e.getSource()).getText().equals("Save")) {
					createHero();
				} else if (((JButton) e.getSource()).getText().equals("Save")) {
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
				}
			} else if (e.getSource() instanceof JComboBox) {
				JComboBox box = (JComboBox)e.getSource();
				classHero = (String)box.getSelectedItem();

				StyledDocument doc = textPane.getStyledDocument();
				SimpleAttributeSet center = new SimpleAttributeSet();
				StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
				doc.setParagraphAttributes(0, doc.getLength(), center, true);
				textPane.setEditable(false);
				try {
					doc.remove(0, doc.getLength());
					doc.insertString(0, defaultStat.toString(classHero), center);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				panel.add(textPane, new GridBagConstraints(0, 2, 2, 1, 0, 0,
						GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
						new Insets(10, 250 , 10, 250), 0, 0 ));
				frame.setVisible(true);
			}
		}
	}

	public static JFrame newWindow() {
		JFrame frame = new JFrame("Swingy");
		frame.setSize(900, 900);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		return frame;
	}
}
