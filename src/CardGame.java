import java.awt.event.*;
import javax.swing.*;

/**
 * @author layton
 * The main class to start the program.
 */
@SuppressWarnings("serial")
public class CardGame extends JFrame {
	
	private JMenuBar menubar;
	private JMenu controlMenu;
	private JMenu helpMenu;
	private JMenuItem newGameMenuItem;
	private JMenuItem helpMenuItem;
	private GameBoard game;
	
	/**
	 * Constructor to initialize the game.
	 */
	public CardGame() {
		init();
	}
	
	private void init() {
		game = new GameBoard();
		add(game);
		addWindowListener(game);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("A SIMPLE CARD GAME");
		setSize(600, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		menubar = new JMenuBar();
		controlMenu = new JMenu("Control");
		helpMenu = new JMenu("Help");
		newGameMenuItem = new JMenuItem("New game");
		helpMenuItem = new JMenuItem("Help");
		newGameMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.newGame();
			}
		});
		helpMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Welcome to BET GAME!\n"
						+ "3 cards will be given to you after making your bet,\n"
						+ "you can replace up to 2 cards\n"
						+ "by clicking the card you want to replace.\n"
						+ "After replacing, click the 'RESULT' button,\n"
						+ "and the dealer's cards will be revealed.\n"
						+ "To win, you need to hold more 'special cards'\n"
						+ "(i.e., J, Q, and K).\n"
						+ "If you have the same number of special cards,\n"
						+ "calculate the sum of your non-special cards' value.\n"
						+ "The remainder divided by 10 has to be larger than\n"
						+ "the dealer's counterpart.\n"
						+ "\nEnjoy the game!");
			}
		});
		controlMenu.add(newGameMenuItem);
		helpMenu.add(helpMenuItem);
		menubar.add(controlMenu);
		menubar.add(helpMenu);
		setJMenuBar(menubar);
	}
	
	/**
	 * Main method, entry point of the program.
	 * @param args
	 */
	public static void main(String[] args) {
		CardGame cardGame = new CardGame();
		cardGame.setVisible(true);
	}
}
