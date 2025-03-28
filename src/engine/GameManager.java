package engine;
import model.player.Marble;
import model.Colour;
import exception.*;
public interface GameManager {
	public void sendHome(Marble marble);
	public void fieldMarble() throws CannotFieldException, IllegalDestroyException;
	public void discardCard(Colour colour) throws CannotDiscardException;
	public void discardCard() throws CannotDiscardException;
	public Colour getActivePlayerColour();
	public Colour getNextPlayerColour();
}
