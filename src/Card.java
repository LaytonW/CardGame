import javax.swing.*;

/**
 * @author layton
 * A class representing cards.
 */
@SuppressWarnings("serial")
public class Card extends JLabel {
	
	private int suit;
	private int value;
	
	/**
	 * Construct a Card using ID number.
	 * @param id A card's identification number. Range: 1 ~ 52.
	 */
	public Card(int id) {
		super(new ImageIcon("res/card_back.gif"));
		suit = (id - 1) % 4 + 1;
		value = (id - 1) % 13 + 1;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param other The other card object to be compared.
	 * @return true if the two cards have the same suit as well as value, false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Card)
			return (suit == ((Card)other).getSuit() && value == ((Card)other).getValue());
		return false;
	}
	
	/**
	 * Getter for Card.suit.
	 * @return The suit of the Card.
	 */
	public int getSuit() {
		return suit;
	}
	
	/**
	 * Getter for Card.value.
	 * @return The value of the Card.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Examine if the Card is a special card
	 * @return true if the card's value is 'J', 'Q', or 'K', false otherwise.
	 */
	public boolean isSpecial() {
		return (value == 11 || value == 12 || value == 13);
	}
	
	/**
	 * Flip the card to show its front side.
	 */
	public void showFront() {
		setIcon(new ImageIcon("res/card_"+String.valueOf(suit)+String.valueOf(value)+".gif"));
	}
}
