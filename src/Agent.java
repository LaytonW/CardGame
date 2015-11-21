import java.util.ArrayList;

/**
 * @author layton
 * A abstract class representing the general agent in the game.
 */
public abstract class Agent {
	
	private ArrayList<Card> cards;
	
	/**
	 * Constructor for Agent.
	 */
	public Agent() {
		cards = new ArrayList<Card>();
	}
	
	/**
	 * Assign a card to the agent's holding cards.
	 * @param card The card to be added.
	 */
	public void assignCard(Card card) {
		cards.add(card);
	}
	
	/**
	 * Clear the cards holding to starting a new round.
	 */
	public void clearCards() {
		cards.clear();
	}
	
	/**
	 * A general method for replacing a card.
	 * @param indexReplaced The index of the card to be replaced.
	 * @param cardReplacing The card object to replacing the old card.
	 */
	public void changeCard(int indexReplaced, Card cardReplacing) {
		cards.set(indexReplaced, cardReplacing);
	}
	
	/**
	 * Getter for Agent.cards.
	 * @return The agent's cards.
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	/**
	 * Get the number of special cards in the agent's cards.
	 * @return The number of special cards.
	 */
	public int numOfSpecialCards() {
		int num = 0;
		for (Card card : cards)
			if (card.isSpecial())
				++num;
		return num;
	}
	
	/**
	 * A general method for processing an agent winning.
	 * Abstract because detailed behaviors depend on different agents.
	 */
	abstract void win();
	
	/**
	 * A general method for processing an agent losing.
	 * Abstract because detailed behaviors depend on different agents.
	 */
	abstract void lose();
}
