import java.util.ArrayList;

/**
 * @author layton
 * A class representing the dealer.
 */
public class Dealer extends Agent {
	
	/**
	 * For now, do nothing when the dealer wins.
	 * @see Agent#win()
	 */
	public void win() {}
	
	/**
	 * For now, do nothing when the dealer loses.
	 * @see Agent#lose()
	 */
	public void lose() {}
	
	/**
	 * Simple AI which allows the computer dealer to do simple card replacing.
	 * @return
	 */
	public int makeDecision() {
		ArrayList<Card> cards = getCards();
		int value = 0;
		int minIndex = 0;
		for (Card card : cards)
			if (!card.isSpecial()) {
				value += card.getValue();
				if (card.getValue()%10 < cards.get(minIndex).getValue()%10
						|| cards.get(minIndex).isSpecial())
					minIndex = cards.indexOf(card);
			}
		value %= 10;
		if (this.numOfSpecialCards() == 3)
			return -1;
		if (this.numOfSpecialCards() == 2)
			if (value > 5)
				return -1;
		return minIndex;
	}
}
