import java.awt.Color;

public class Diamond extends Item{
	
	public Diamond(int x, int y, int r) {
		super(x,y,r,20,3,null);
	}

	// Draws diamond based on position and radius
	public void draw() { 
		int x = xPosition;
		int y = yPosition;
		int rad = radius;
		
		g.setColor(new Color(156, 186, 214));
		int[] xtotalPts = {x-rad*4/5,x+rad*4/5,x+rad,x,x-rad};
		int[] ytotalPts = {y-rad/4,y-rad/4,y,y+rad,y};
		g.fillPolygon(xtotalPts, ytotalPts, 5);
		
		g.setColor(new Color(216, 231, 255));
		int[] xRightPts = {x+rad*2/5,x+rad*4/5,x+rad,x,x+rad*5/10};
		int[] yRightPts = {y-rad/4,y-rad/4,y,y+rad,y,y};
		g.fillPolygon(xRightPts, yRightPts, 5);

		g.setColor(new Color(126, 144, 173));
		int[] xLeftPts = {x,x-rad*5/10,x-rad};
		int[] yLeftPts = {y+rad,y,y};
		g.fillPolygon(xLeftPts, yLeftPts, 3);
	}
	
	
}