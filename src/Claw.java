import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Claw{
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
	private double xPosition; // Position of Claw center
	private double yPosition; 
	private boolean isRotating; // Boolean tracking Claw rotation
	private double angle; // Current angle of Claw w.r.t. horizon
	private Graphics g; // Panel graphics object
	private double angleAdd; // Angle change rate (+/- determines rotation direction)
	private boolean isGrabbing; // Indicates if Claw has picked up item
	private boolean atOrgin; // Indicates if Claw is fully retracted
	private int score; // Cumulative item score
	private boolean speedBoost; // A power-up; if true, increases Claw retraction speed
	private boolean dynamite; // A power-up; if true, drops adversarial item (e.g. TNT) currently in claw 
	
	public Claw(Graphics g) {
		xPosition = screenSize.width/4;
		yPosition = 300;
		isRotating = true;
		angle = 200;
		this.g = g;
		isGrabbing = false;
		atOrgin = false;
		dynamite = false;
		angleAdd = 0;
		}

	public Dimension getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(Dimension screenSize) {
		this.screenSize = screenSize;
	}

	public double getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public double getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public boolean getIsRotating() {
		return isRotating;
	}

	public void setIsRotating(boolean isRotating) {
		this.isRotating = isRotating;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	// Returns index of object that has been touched
	// If not touching any, returns -1
	private int touching(int width, int height, ArrayList<Item> items) { 
		
		if(xPosition < 20 || xPosition > width-20 || yPosition > height-20)
			isGrabbing = true;
		
		for(int i = 0; i < items.size(); i++) {
			double xDif = (int) (xPosition-items.get(i).getxPosition());
			double yDif = (int) (yPosition-items.get(i).getyPosition());
			double distance = Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
			
			if(distance < items.get(i).getRadius()) {
				isGrabbing = true;
				grab(items.get(i));
				return i;
			}
				
		}
		
		return -1;
		
	}
	
	// Sets position of Item to center of Claw -- the visual effect of "grabbing"
	// If Claw retracts, item retracts with it
	private void grab(Item i) {

		i.setxPosition((int)xPosition);
		i.setyPosition((int)yPosition);
	}
	
	// Claw shoots out from origin in linear path (no rotation)
	// Speed dictated by presence of power up
	private void shootOut() {
		
		int change;
		if(speedBoost)
			change=6;
		else
			change=3;
			xPosition += Math.cos(angle*Math.PI/180)*change; 
			yPosition += Math.sin(angle*Math.PI/180)*(-change);
		
	}
	
	// Retracts Claw back to origin
	// If fully retracted, begin rotating again
	private void retract(int width, int height, double speed) { 
		
		xPosition -= Math.cos(angle*Math.PI/180)*speed;
		yPosition -= Math.sin(angle*Math.PI/180)*(-speed);
		
		double xDif = (int) (xPosition-width/2);
		double yDif = (int) (yPosition-height/4);
		double distance = Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
		
		if (distance < 100) 
			atOrgin = true;
	}
	
	// Draws the Claw 
	public void draw(int width, int height) {
		
		int x = (int) xPosition; 
		int y = (int) yPosition;
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(5));
		
		g.setColor(new Color(84, 95, 114));
		g2.drawLine(width/2, height/4, x, y);
		
		int lowerClawPtX = x+(int)(20*Math.cos((angle-90)*Math.PI/180));
		int upperClawPtX = x-(int)(20*Math.cos((angle-90)*Math.PI/180));
		int lowerClawPtY = y-(int)(20*Math.sin((angle-90)*Math.PI/180));
		int upperClawPtY = y+(int)(20*Math.sin((angle-90)*Math.PI/180));
		
		g.drawLine(lowerClawPtX,lowerClawPtY,upperClawPtX,upperClawPtY);
		g.drawLine(lowerClawPtX, lowerClawPtY, lowerClawPtX+(int)(20*Math.cos(angle*Math.PI/180)), lowerClawPtY-(int)(20*Math.sin(angle*Math.PI/180)));
		g.drawLine(upperClawPtX, upperClawPtY, upperClawPtX+(int)(20*Math.cos(angle*Math.PI/180)), upperClawPtY-(int)(20*Math.sin(angle*Math.PI/180)));
		
		g.setColor(new Color(38, 42, 51));
		g.fillOval(x-10, y-10, 20, 20);
		
	}
	
	// Changes claw position coordinates based on current angle
	private void changeCoordinates(int width, int height) {
		xPosition = width/2 + (100*Math.cos(angle*Math.PI/180));
		yPosition = height/4 - (100*Math.sin(angle*Math.PI/180));
	}
	
	// General Claw movement method
	// Returns True if hit TNT
	public boolean move(int width, int height, ArrayList<Item> items, boolean sb) { 
		
		speedBoost=sb;
		try {
			Thread.sleep(10);
		}
		
		catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		
		if(isRotating) {
			rotate(width,height);
			
		}else {
			int touchIndex = touching(width,height,items);
			if(!isGrabbing)
				shootOut();
				
			else
				
				if(!atOrgin) {
					if(dynamite&&touchIndex>-1) {
						items.remove(touchIndex);
						touchIndex=-1;
						dynamite=false;
					}
					double speed;
					if(touchIndex>=0) {
						speed = items.get(touchIndex).getRetractConstant();
					}else
						speed = 3;
					if(speedBoost)
						speed*=2;
					retract(width,height,speed);
					
				}else {
			
					if(touchIndex>-1) {
						if(items.get(touchIndex) instanceof Tnt) {
							return true;
						}else {	
							score += items.remove(touchIndex).getValue();
						}
					}
					resetAngleRotation(width,height);
				}
					
		}
		return false;
	}
	
	public boolean isDynamite() {
		return dynamite;
	}

	public void setDynamite(boolean dynamite) {
		this.dynamite = dynamite;
	}

	public boolean isGrabbing() {
		return isGrabbing;
	}

	public void setGrabbing(boolean isGrabbing) {
		this.isGrabbing = isGrabbing;
	}

	public Graphics getG() {
		return g;
	}

	public void setG(Graphics g) {
		this.g = g;
	}

	// Default constructor
	public Claw() {
		super();
		xPosition = 300;
		yPosition = 300;
		isRotating = true;
		angle = 200;
		
	}
	
	// Rotates Claw
	// Boundaries at 200 and 340 degrees
	private void rotate(int width, int height) {

		if(angle <= 200)
			angleAdd = 0.75;
		else if(angle >= 340)
			angleAdd = -0.75;
		
		angle += angleAdd;
		changeCoordinates(width,height);
	}
	
	// Once Claw has fully retracted, restart rotation and remove Item in Claw
	private void resetAngleRotation(int width, int height) {
		
			changeCoordinates(width,height);
			isRotating = true;
			isGrabbing = false;
			atOrgin = false;
		
		
	}

	
	
}
