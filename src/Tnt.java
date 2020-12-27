import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Tnt extends Item{

	public Tnt(int x, int y) {
		super(x, y, 30, 0, 4, null);
	}


	@Override
	// Draws TNT based on position and radius
	public void draw() {
		int x = xPosition;
		int y = yPosition;
		int rad = radius;

		g.setColor(new Color(211, 168, 97)); 
		int[] xtotalPts = {x-rad*5/6,x-rad/2,x+rad/2,x+rad*5/6,x+rad/2,x-rad/2};
		int[] ytotalPts = {y,y-rad,y-rad,y,y+rad,y+rad};
		g.fillPolygon(xtotalPts, ytotalPts, 6);
		
		
		Graphics2D g2 = (Graphics2D)g;
		
		g.setColor(new Color(178, 136, 67)); 
		g2.setStroke(new BasicStroke(6));
		g2.drawLine(x-rad/6,y-rad, x-rad/3, y);
		g2.drawLine(x+rad/6,y-rad, x+rad/3, y);
		g2.drawLine(x-rad/6,y+rad, x-rad/3, y);
		g2.drawLine(x+rad/6,y+rad, x+rad/3, y);
		
		g.setColor(new Color(150, 104, 27)); 
		g2.setStroke(new BasicStroke(7));
		g2.drawLine(x-rad*5/6, y, x-rad/3, y+rad/12);
		g2.drawLine(x+rad*5/6, y, x+rad/3, y+rad/12);
		g2.drawLine(x-rad/3, y+rad/12, x+rad/3, y+rad/12);
		
		g2.drawLine(x-rad/2,y-rad,x+rad/2,y-rad);
		g2.drawLine(x-rad/2,y+rad,x+rad/2,y+rad);
		
		g.setColor(new Color(239, 227, 208));
		g.fillRect(x-rad*7/12, y-rad*2/3, rad*7/6, rad/2);
		
		g.setColor(new Color(216, 83, 43));
		int fontSize = 17;
		g.setFont(new Font("Helvetica", Font.BOLD, fontSize));
		g.drawString("TNT", x-rad/2, y-rad/6);
		
	}

}
