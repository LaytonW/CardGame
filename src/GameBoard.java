import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * @author layton
 * The game logics, rules and main board.
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel implements WindowListener {
	
	private ArrayList<Card> cardUsed;
	private Player player;
	private Dealer dealer;
	private int roundsPlayed;
	private int numReplaced;
	private int bestScore;
	private JPanel dealerPanel;
	private JPanel playerPanel;
	private JPanel controlPanel;
	private JButton betButton;
	private JButton resultButton;
	private JLabel moneyLabel;
	private JLabel betLabel;
	private JLabel money;
	private JLabel inputImage;
	private JLabel recordLabel;
	private JLabel record;
	private JTextField betInput;
	private BufferedReader recordReader;
	private BufferedWriter recordWriter;
	
	/**
	 * Constructor for allocating memory and simple initializing.
	 */
	public GameBoard() {
		dealerPanel = new JPanel();
		playerPanel = new JPanel();
		controlPanel = new JPanel();
		money = new JLabel();
		record = new JLabel();
		inputImage = new JLabel(new ImageIcon("res/INPUT.gif"));
		moneyLabel = new JLabel(new ImageIcon("res/MONEY.gif"));
		betLabel = new JLabel(new ImageIcon("res/MAKE_YOUR_BET.gif"));
		recordLabel = new JLabel(new ImageIcon("res/BEST_SCORE.gif"));
		betButton = new JButton(new ImageIcon("res/BET.gif"));
		resultButton = new JButton(new ImageIcon("res/RESULT.gif"));
		betInput = new JTextField();
		
		try {
			recordReader = new BufferedReader(new FileReader("res/record"));
			bestScore = Integer.parseInt(recordReader.readLine());
			recordReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		inputImage.setLayout(new BorderLayout());
		controlPanel.setLayout(new FlowLayout());
		
		betInput.setHorizontalAlignment(JTextField.CENTER);
		betButton.setBorder(BorderFactory.createEmptyBorder());
		betButton.setContentAreaFilled(false);
		resultButton.setBorder(BorderFactory.createEmptyBorder());
		resultButton.setContentAreaFilled(false);
		resultButton.setEnabled(false);
		betButton.addMouseListener(new BetListener());
		resultButton.addMouseListener(new ResultListener());
		betInput.setOpaque(false);
		betInput.setBorder(BorderFactory.createEmptyBorder());
		money.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
		record.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
		setOpaque(false);

		initGame();
		initRound();
	}
	
	/**
	 * Interface for outside to start a new game.
	 */
	public void newGame() {
		removeAll();
		initGame();
		initRound();
		repaint();
	}
	
	private void initGame() {
		cardUsed = new ArrayList<Card>();
		player = new Player();
		dealer = new Dealer();
		roundsPlayed = 0;
	}
	
	private void initRound() {
		SpringLayout mainLayout = new SpringLayout();
		setLayout(mainLayout);
		resultButton.setEnabled(false);
		betButton.setEnabled(true);
		inputImage.add(betInput);
		controlPanel.add(betLabel);
		controlPanel.add(inputImage);
		controlPanel.add(betButton);
		add(recordLabel);
		add(record);
		add(dealerPanel);
		add(playerPanel);
		add(controlPanel);
		add(moneyLabel);
		add(money);
		add(resultButton);
		mainLayout.putConstraint(SpringLayout.WEST, recordLabel, 154, SpringLayout.WEST, this);
		mainLayout.putConstraint(SpringLayout.NORTH, recordLabel, 20, SpringLayout.NORTH, this);
		mainLayout.putConstraint(SpringLayout.NORTH, record, 15, SpringLayout.NORTH, this);
		mainLayout.putConstraint(SpringLayout.WEST, record, 20, SpringLayout.EAST, recordLabel);
		mainLayout.putConstraint(SpringLayout.WEST, dealerPanel, 180, SpringLayout.WEST, this);
		mainLayout.putConstraint(SpringLayout.NORTH, dealerPanel, 70, SpringLayout.NORTH, this);
		mainLayout.putConstraint(SpringLayout.WEST, playerPanel, 180, SpringLayout.WEST, this);
		mainLayout.putConstraint(SpringLayout.NORTH, playerPanel, 10, SpringLayout.SOUTH, dealerPanel);
		mainLayout.putConstraint(SpringLayout.SOUTH, moneyLabel, -20, SpringLayout.SOUTH, this);
		mainLayout.putConstraint(SpringLayout.WEST, moneyLabel, 5, SpringLayout.WEST, this);
		mainLayout.putConstraint(SpringLayout.WEST, money, 20, SpringLayout.EAST, moneyLabel);
		mainLayout.putConstraint(SpringLayout.SOUTH, money, -15, SpringLayout.SOUTH, this);
		mainLayout.putConstraint(SpringLayout.SOUTH, resultButton, -13, SpringLayout.SOUTH, this);
		mainLayout.putConstraint(SpringLayout.EAST, resultButton, -70, SpringLayout.EAST, this);
		mainLayout.putConstraint(SpringLayout.SOUTH, controlPanel, -5, SpringLayout.NORTH, resultButton);
		assignCards();
		numReplaced = 0;
		betInput.setText(null);
		betInput.requestFocusInWindow();
	}
	
	/**
	 * Update changing components.
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		money.setText(String.valueOf(player.getMoney()));
		record.setText(String.valueOf(bestScore));
		dealerPanel.removeAll();
		playerPanel.removeAll();
		for (Card dealerCard : dealer.getCards())
			dealerPanel.add(dealerCard);
		for (Card playerCard : player.getCards())
			playerPanel.add(playerCard);
		revalidate();
	}
	
	private Card genNewCard() {
		Random rand = new Random();
		Card card = new Card(rand.nextInt(52) + 1);
		while (cardUsed.contains(card))
			card = new Card(rand.nextInt(52) + 1);
		cardUsed.add(card);
		return card;
	}
	
	private void assignCards() {
		player.clearCards();
		dealer.clearCards();
		for (int i = 0; i < 3; ++i) {
			player.assignCard(genNewCard());
			dealer.assignCard(genNewCard());
		}
	}
	
	private void judge() {
		if (player.numOfSpecialCards() > dealer.numOfSpecialCards()) {
			player.win();
			dealer.lose();
		} else if (player.numOfSpecialCards() < dealer.numOfSpecialCards()) {
			dealer.win();
			player.lose();
		} else {
			int playerScore = 0, dealerScore = 0;
			for (Card card : player.getCards()) {
				if (!card.isSpecial())
					playerScore += card.getValue();
				playerScore %= 10;
			}
			for (Card card : dealer.getCards()) {
				if (!card.isSpecial())
					dealerScore += card.getValue();
				dealerScore %= 10;
			}
			if (playerScore > dealerScore) {
				player.win();
				dealer.lose();
			} else {
				dealer.win();
				player.lose();
			}
		}
	}
	
	private class BetListener implements MouseListener {

		/**
		 * When the player click the BET button, check the input validity and process.
		 * @param e
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (betButton.isEnabled()) {
				int bet = 0;
				try {
					bet = Integer.parseInt(betInput.getText());
					if (bet <= 0 || bet > player.getMoney())
						throw null;
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Invalid bet");
					betInput.setText(null);
					betInput.requestFocusInWindow();
					return;
				}
			
				int d1 = dealer.makeDecision();
				if (d1 != -1) {
					dealer.changeCard(d1, genNewCard());
					int d2 = dealer.makeDecision();
					if (d2 != -1)
						dealer.changeCard(d2, genNewCard());
				}
				
				player.makeBet(bet);
				for (Card card : player.getCards())
					card.showFront();
				for (Card card : player.getCards())
					card.addMouseListener(new ReplaceListener());
				betButton.setEnabled(false);
				resultButton.setEnabled(true);
				repaint();
			}
		}

		/**
		 * Change the icon for mouse hovering.
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			if (betButton.isEnabled())
				betButton.setIcon(new ImageIcon("res/BET_HOVER.gif"));
		}

		/**
		 * Reset the default icon.
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			if (betButton.isEnabled())
				betButton.setIcon(new ImageIcon("res/BET.gif"));
		}

		/**
		 * Change the icon for mouse pressing.
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (betButton.isEnabled())
				betButton.setIcon(new ImageIcon("res/BET_PRESSED.gif"));
		}

		/**
		 * Reset the default icon.
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			if (betButton.isEnabled())
				betButton.setIcon(new ImageIcon("res/BET.gif"));
		}
	}
	
	private class ResultListener implements MouseListener {

		/**
		 * When the player click the RESULT button, update the UI and judge the game,
		 * and then restart a round or a game.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (resultButton.isEnabled()) {
				for (Card card : dealer.getCards())
					card.showFront();
				betInput.setText(null);
				judge();
				bestScore = player.getMoney() > bestScore ? player.getMoney() : bestScore;
				++roundsPlayed;
				repaint();
				if (player.getMoney() == 0) {
					JOptionPane.showMessageDialog(null, "All money lost, starting a new game :(");
					newGame();
				} else {
					removeAll();
					if (roundsPlayed == 5) {
						roundsPlayed = 0;
						JOptionPane.showMessageDialog(null, "reshuffling...");
						cardUsed.clear();
					}
					initRound();
					repaint();
				}
			}
		}

		/**
		 * Change the icon for mouse hovering.
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			if (resultButton.isEnabled())
				resultButton.setIcon(new ImageIcon("res/RESULT_HOVER.gif"));
		}
		
		/**
		 * Reset the default icon.
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			if (resultButton.isEnabled())
				resultButton.setIcon(new ImageIcon("res/RESULT.gif"));
		}
		
		/**
		 * Change the icon for mouse pressing.
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (resultButton.isEnabled())
				resultButton.setIcon(new ImageIcon("res/RESULT_PRESSED.gif"));
		}
		
		/**
		 * Reset the default icon.
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			if (resultButton.isEnabled())
				resultButton.setIcon(new ImageIcon("res/RESULT.gif"));
		}
	}
	
	private class ReplaceListener implements MouseListener {

		/**
		 * When the player click a card for replacing, process and change the card.
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (numReplaced < 2) {
				Card nCard = genNewCard();
				nCard.addMouseListener(this);
				player.changeCard(player.getCards().indexOf(e.getSource()), nCard);
				++numReplaced;
				repaint();
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	/**
	 * When exiting the game, save the best score to the record file.
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			recordWriter = new BufferedWriter(new FileWriter("res/record"));
			recordWriter.write(String.valueOf(bestScore));
			recordWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}
	
	@Override
	public void windowOpened(WindowEvent arg0) {}
}
