
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Frame extends JFrame implements KeyListener{
	
	private Claw claw; 
	private boolean inGame; 
	private boolean dynamite;
	
	public boolean isDynamite() {
		return dynamite;
	}

	public void setDynamite(boolean dynamite) {
		this.dynamite = dynamite;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public Frame(boolean ig) throws HeadlessException {
		super();
		setFocusable(true);
		addKeyListener(this);
		setVisible(true);
		inGame = ig;
	}
	
	public void setClaw(Claw c) {
		claw = c;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {		
		if (inGame) {
			
			// If down arrow pressed, Claw shoots out
			if(e.getKeyCode()==KeyEvent.VK_DOWN) { 
					claw.setIsRotating(false);
			}
		
			// If up arrow is pressed while user has dynamite, item touching claw is discarded
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				if(dynamite) {
					if(!claw.getIsRotating() && claw.isGrabbing()) {
						claw.setDynamite(true);
						
					}
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

	
	

}
