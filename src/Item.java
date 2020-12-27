import java.awt.Graphics;

public abstract class Item {

	protected int xPosition; // Position of center of item on screen
	protected int yPosition;
	protected int radius; // Item size and region where item is "capturable" by Claw
	protected int value; // Item score
	protected Graphics g; // Panel graphics object
	protected double retractConstant; // Claw rectraction speed

	public double getRetractConstant() {
		return retractConstant;
	}

	public void setRetractConstant(double retractConstant) {
		this.retractConstant = retractConstant;
	}

	// Overwritten in Children classes
	public abstract void draw();
	
	// Constructor called in Children constructors
	public Item(int x, int y, int r, int v, int type, Graphics paramG) {
		xPosition = x;
		yPosition = y;
		radius = r;
		g = paramG;
		value = v*r;
		retractConstant = 30.0*type/(r);
	}

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Graphics getG() {
		return g;
	}

	public void setG(Graphics g) {
		this.g = g;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}