import javax.swing.JFrame;

public class Driver {
	
	public static void main(String[] args) {
		
		Frame frame = new Frame(false); // Creates starting screen
		frame.setTitle("Gold Miner");
		frame.setSize(1200, 700); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	
		Panel panel = new Panel(false,frame); // Creates panel with frame
		frame.getContentPane().add(panel); 
		frame.setVisible(true);
		
		boolean play = true;
		
		while(play) {
			frame.repaint(); // Continuously updates/draws game
		}
		
	}
	

}