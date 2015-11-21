import javax.swing.JOptionPane;

/**
 * @author layton
 * A class representing the player.
 */
public class Player extends Agent {
	
	private int money;
	private int bet;
	
	/**
	 * Constructor for Player, which initialize the money held to 100, the bet made to 0.
	 */
	public Player() {
		super();
		money = 100;
		bet = 0;
	}
	
	/**
	 * Setter for Player.bet.
	 * @param b The bet value.
	 */
	public void makeBet(int b) {
		bet = b;
	}
	
	/**
	 * Getter for Player.money.
	 * @return money
	 */
	public int getMoney() {
		return money;
	}
	
	/**
	 * Override the replacing card method to automatically flip
	 * the card replaced by the player to show its front side.
	 * @see Agent#changeCard(int, Card)
	 */
	@Override
	public void changeCard(int indexReplaced, Card cardReplacing) {
		cardReplacing.showFront();
		this.getCards().set(indexReplaced, cardReplacing);
	}
	
	/**
	 * Prompt the winning message and accumulate the money.
	 * @see Agent#win()
	 */
	@Override
	public void win() {
		money += bet;
		JOptionPane.showMessageDialog(null, "You win!");
	}
	
	/**
	 * Prompt the losing message and lose the money.
	 * @see Agent#lose()
	 */
	@Override
	public void lose() {
		money -= bet;
		JOptionPane.showMessageDialog(null, "You lose :(");
	}
}
