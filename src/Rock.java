import java.awt.Color;

public class Rock extends Item {

	public Rock(int x, int y, int r) {
		super(x,y,r,10,2,null);
	}


	@Override
	// Draws rock based on position and radius
	public void draw() {
		int x = xPosition;
		int y = yPosition;
		int rad = radius;

		g.setColor(new Color(145, 145, 145)); //yellow
		int[] xtotalPts = {x-rad*2/3,x-rad*7/8,x-rad/2,x+rad/8,x+rad*2/3,x+rad*5/6,x+rad/2,x-rad/8};
		int[] ytotalPts = {y-rad*2/5,y+rad/4,y+rad*2/3,y+rad*4/5,y+rad*2/3,y-rad/8,y-rad*2/3,y-rad*5/6};
		g.fillPolygon(xtotalPts, ytotalPts, 8);

		g.setColor(new Color(94, 94, 94));
		int[] xshadowPts = {x-rad*2/3,x-rad*7/8,x-rad/2,x+rad/8,x+rad*2/3,x,x-rad*5/12};
		int[] yshadowPts = {y-rad*2/5,y+rad/4,y+rad*2/3,y+rad*4/5,y+rad*2/3,y+rad/2,y+rad/4};
		g.fillPolygon(xshadowPts, yshadowPts, 7);

	}
	
	
}
