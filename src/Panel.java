import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Panel extends JPanel {

	private Frame frame; // Frame used to listen to key strokes
	private Claw claw; 
	private int score; // Score at beginning of each level
	private ArrayList<Item> items; // Randomly generated Items per level
	private boolean inGame; // If true: a level is in play
	private long startTime; // Time at which a level begins
	private int secondsLeft; // Seconds left in a level at a given time
	private int level; // Current level the user is at
	private int winCondition; // Score necessary for player to beat level
	private JButton button; // Button used to start a level
	private JButton speedBoostButton; // Button used to purchase speed boost
	private JButton dynamiteButton; // Button used to purchase dynamite
	private JButton valueMultButton; // Button used to purchase value multiplier
	private String resultString; // Tells user if they've won or lost/what to do
	private boolean speedBoost; // If true, the user has bought the speed boost
	private boolean valueMult; // If true, the user has bought the value multiplier
	private boolean dynamiteChosen; // If true, the user has bought the dynamite
	private String speech; // Speech bubble that explains rules/instructions
	private double goldChance; // Probability of generating gold on a certain level
	private double diamondChance; // Probability of generating diamond on a certain level
	private double tntChance;// Probability of generating tnt on a certain level
	private double chance; // Random variable determining the probability of generating of each item


	public boolean isSpeedBoost() {
		return speedBoost;
	}

	public void setSpeedBoost(boolean speedBoost) {
		this.speedBoost = speedBoost;
	}

	public boolean isValueMult() {
		return valueMult;
	}

	public void setValueMult(boolean valueMult) {
		this.valueMult = valueMult;
	}

	public Panel(boolean ig, Frame f) {
		super();

		level = 0;
		inGame = ig;
		frame = f;
		this.setSize(frame.getSize());
		items = new ArrayList<Item>();
		resultString = "";
		resetLevel();

		button = new JButton("Play");
		speech = "Welcome! Press the down arrow to pick up\nitems. Collect as many as you can in order\nto meet the goal before the time runs out.";
		speedBoostButton = new JButton("Speed Boost");
		valueMultButton = new JButton("Value Multiplier");
		dynamiteButton = new JButton("Dynamite");
	}

	public void setInGame(boolean ig) {
		inGame = ig;
		if (inGame)
			startTime = System.currentTimeMillis();
	}

	// Sets and draws the panel based on if the user is currently playing a level
	// Resets game once the user has won all 10 levels
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (level == 10) {
			resultString = "You won all 10 levels!";
			speech="Press the button to play again.";
			button.setText("Play Again");
			restartGame();
		}

		// If playing, draws actual level; if in-between levels or before playing, draws a start screen
		if (inGame) {
			level(g);
		} else {
			startScreen(g);
		}

	}

	// Draws the background of the levels
	private void drawGameBackground(Graphics g) {
		g.setColor(new Color(244, 200, 66));
		g.fillRect(0, 0, getWidth(), getHeight() / 4);

		g.setColor(new Color(31, 45, 145));
		g.fillArc(getWidth() * 3 / 10, 10, getWidth() * 2 / 5, getHeight() / 2 - 10, 0, 180);

		g.setColor(new Color(226, 180, 111));
		g.fillRect(0, getHeight() / 4, getWidth(), getHeight() * 3 / 4);

		g.setColor(new Color(150, 119, 73));
		int[] xPoints1st = { 0, 0, getWidth(), getWidth(), getWidth() * 4 / 5, getWidth() * 3 / 5, getWidth() * 2 / 5,
				getWidth() / 5 };
		int[] yPoints1st = { getHeight() * 3 / 8, getHeight(), getHeight(), getHeight() / 2, getHeight() * 3 / 8 + 15,
				getHeight() / 2 - 10, getHeight() * 3 / 8, getHeight() * 3 / 8 + 50 };
		g.fillPolygon(xPoints1st, yPoints1st, 8);
		g.setColor(new Color(91, 72, 44));
		int[] xPoints2nd = { 0, 0, getWidth(), getWidth(), getWidth() * 4 / 5, getWidth() * 3 / 5, getWidth() * 2 / 5,
				getWidth() / 5 };
		int[] yPoints2nd = { getHeight() * 7 / 8 - 30, getHeight(), getHeight(), getHeight() * 5 / 8,
				getHeight() * 5 / 8 + 50, getHeight() * 5 / 8, getHeight() * 3 / 4 - 10, getHeight() * 5 / 8 + 40 };
		g.fillPolygon(xPoints2nd, yPoints2nd, 8);

		g.fillRect(0, getHeight() / 4, getWidth(), 20);

		// Draws character operating claw
		g.setColor(new Color(226, 180, 111));
		int[] xMoundPoints = { getWidth() * 7 / 20, getWidth() * 2 / 5, getWidth() * 3 / 5, getWidth() * 13 / 20 };
		int[] yMoundPoints = { getHeight() / 4, getHeight() / 4 - 15, getHeight() / 4 - 15, getHeight() / 4 };
		g.fillPolygon(xMoundPoints, yMoundPoints, 4);

		g.setColor(new Color(91, 72, 44));
		int[] xRShoe = { getWidth() / 2, getWidth() / 2 + 10, getWidth() / 2 + 30, getWidth() / 2 + 30 };
		int[] yRShoe = { getHeight() / 4 - 10, getHeight() / 4 - 20, getHeight() / 4 - 23, getHeight() / 4 - 10 };
		g.fillPolygon(xRShoe, yRShoe, 4);
		int[] xLShoe = { getWidth() / 2 + 35, getWidth() / 2 + 45, getWidth() / 2 + 65, getWidth() / 2 + 65 };
		int[] yLShoe = { getHeight() / 4 - 10, getHeight() / 4 - 20, getHeight() / 4 - 23, getHeight() / 4 - 10 };
		g.fillPolygon(xLShoe, yLShoe, 4);

		g.setColor(new Color(153, 102, 0));
		int[] xPants = { getWidth() / 2 + 30, getWidth() / 2 + 5, getWidth() / 2 - 5, getWidth() / 2 + 15,
				getWidth() / 2 + 55, getWidth() / 2 + 68, getWidth() / 2 + 40, getWidth() / 2 + 32, getWidth() / 2 + 35,
				getWidth() / 2 + 25 };
		int[] yPants = { getHeight() / 4 - 23, getHeight() / 4 - 19, getHeight() / 4 - 50, getHeight() / 4 - 80,
				getHeight() / 4 - 85, getHeight() / 4 - 23, getHeight() / 4 - 20, getHeight() / 4 - 50,
				getHeight() / 4 - 60, getHeight() / 4 - 50 };
		g.fillPolygon(xPants, yPants, 10);

		g.setColor(new Color(150, 119, 73));
		int[] xShirt = { getWidth() / 2 + 10, getWidth() / 2 + 57, getWidth() / 2 + 53, getWidth() / 2 + 45,
				getWidth() / 2 + 5, getWidth() / 2 - 5 };
		int[] yShirt = { getHeight() / 4 - 75, getHeight() / 4 - 80, getHeight() / 4 - 110, getHeight() / 4 - 120,
				getHeight() / 4 - 115, getHeight() / 4 - 95 };
		g.fillPolygon(xShirt, yShirt, 6);

		g.setColor(new Color(226, 180, 111));
		g.fillOval(getWidth() / 2 - 5, getHeight() / 4 - 160, 40, 40);

		g.setColor(new Color(145, 145, 145));
		int[] xBeard = { getWidth() / 2 - 6, getWidth() / 2 + 36, getWidth() / 2 + 30, getWidth() / 2 + 15,
				getWidth() / 2 };
		int[] yBeard = { getHeight() / 4 - 140, getHeight() / 4 - 140, getHeight() / 4 - 120, getHeight() / 4 - 100,
				getHeight() / 4 - 120 };
		g.fillPolygon(xBeard, yBeard, 5);
		int[] xHair = { getWidth() / 2 - 5, getWidth() / 2, getWidth() / 2 + 20, getWidth() / 2 + 33,
				getWidth() / 2 + 35, getWidth() / 2 + 25, getWidth() / 2 + 20, getWidth() / 2 };
		int[] yHair = { getHeight() / 4 - 145, getHeight() / 4 - 160, getHeight() / 4 - 160, getHeight() / 4 - 155,
				getHeight() / 4 - 140, getHeight() / 4 - 140, getHeight() / 4 - 150, getHeight() / 4 - 150 };
		// g.fillPolygon(xHair, yHair, 8);

		g.setColor(new Color(226, 180, 111));
		g.fillOval(getWidth() / 2, getHeight() / 4 - 145, 15, 20);

		g.setColor(new Color(84, 95, 114));
		g.fillRect(getWidth() / 2 - 50, getHeight() / 4 - 100, 70, 45);

		Graphics2D g2 = (Graphics2D) g;

		g.setColor(new Color(38, 42, 51));
		g2.setStroke(new BasicStroke(10));
		g2.drawLine(getWidth() / 2 - 50, getHeight() / 4 - 100, getWidth() / 2 - 50, getHeight() / 4 - 55);
		g2.drawLine(getWidth() / 2 + 20, getHeight() / 4 - 100, getWidth() / 2 + 20, getHeight() / 4 - 55);
		g2.setStroke(new BasicStroke(6));
		g2.drawLine(getWidth() / 2 - 15, getHeight() / 4 - 52, getWidth() / 2 - 15, getHeight() / 4 - 10);
		g2.drawLine(getWidth() / 2 - 25, getHeight() / 4 - 10, getWidth() / 2 - 5, getHeight() / 4 - 10);

		g.setColor(new Color(84, 95, 114));
		g2.setStroke(new BasicStroke(5));
		g2.drawLine(getWidth() / 2 - 4, getHeight() / 4 - 95, getWidth() / 2, getHeight() / 4);
		
		g.setColor(Color.BLACK);
		g.fillOval(getWidth() / 2 , getHeight() / 4 - 150, 4, 4);
		g.fillOval(getWidth() / 2 + 10, getHeight() / 4 - 150, 4, 4);

	}

	// Draws current score, goal, and level while user is playing a level
	private void drawPlayerAttributes(Graphics g) { 

		int fontSize = 18;
		g.setFont(new Font("Apple LiGothic", Font.BOLD, fontSize));
		g.setColor(new Color(153, 102, 0));
		g.drawString("Score: " + claw.getScore(), getWidth() / 32, getHeight() / 16);
		g.drawString("Goal: " + winCondition, getWidth() / 32, getHeight() / 8);
		g.drawString("Level: " + (level + 1) + "/10", getWidth() / 2 + getWidth() / 4 + getWidth() / 16, getHeight() / 8);

	}

	// Updates/draws remaining time while user is playing a level
	// Resets level based on score condition when time is 0
	private void drawCountdown(Graphics g) { 

		long currentTime = System.currentTimeMillis();
		secondsLeft = (int) (60 - (TimeUnit.MILLISECONDS.toSeconds(currentTime - startTime))); 
		g.drawString("Time: " + secondsLeft, getWidth() / 2 + getWidth() / 4 + getWidth() / 16, getHeight() / 16);
		
		if (secondsLeft <= 0) {

			inGame = false;
			frame.setInGame(false);

			if (claw.getScore() >= winCondition) {
				level++;
				score = claw.getScore();
				button.setText("Next Level");
				resultString = "You made it to the next level!\nYou have $" + score + " to spend";
				speech = "Welcome! Hover over an item for a description of its ability, \nand click an item to purchase. All items are $500.";
				resetLevel();
				
			} else {
				button.setText("Play Again");
				restartGame();
				speech = "Better luck next time, champ.";
				resultString = "GAME OVER!";
			}

		}

	}

	// Draws level graphics
	// Determines if dynamite has been used
	private void level(Graphics g) { 
		drawGameBackground(g);
		drawPlayerAttributes(g);
		drawCountdown(g);
		claw.setG(g);

		for (int i = 0; i < items.size(); i++) {
			items.get(i).setG(g);
		}

		claw.draw(getWidth(), getHeight());
		if (claw.move(getWidth(), getHeight(), items, speedBoost)) {
			startTime = -15; // before actual end time
			claw.setScore(0);
		}

		for (int i = 0; i < items.size(); i++)
			items.get(i).draw();
	}

	// Graphics of start screen/screen in between levels
	// Creates play button and buttons for power ups if applicable
	private void startScreen(Graphics g) {
		this.setBackground(new Color(112, 81, 38));
		g.setFont(new Font("Apple LiGothic", Font.BOLD, 100));
		g.setColor(new Color(230, 153, 0));
		g.drawString("Gold Miner", getWidth() / 32, getHeight() / 4);

		g.setFont(new Font("Apple LiGothic", Font.BOLD, 40));
		splitString(resultString, getWidth() / 32 + 5, getHeight() / 4 + getHeight() / 16, 50, g);
		
		g.setColor(new Color(91, 72, 44));
		g.fillRect(getWidth() / 16, getHeight() * 7 / 8, getWidth() * 7 / 8, getHeight() / 8);

		g.setColor(new Color(150, 119, 73));
		int[] xBody = { getWidth() * 3 / 4, getWidth() * 3 / 4 + 110, getWidth() * 3 / 4 + 110,
				getWidth() * 3 / 4 + 120, getWidth() * 3 / 4 + 160, getWidth() * 3 / 4 + 140, getWidth() * 3 / 4 - 30,
				getWidth() * 3 / 4 - 50, getWidth() * 3 / 4 - 10, getWidth() * 3 / 4 };
		int[] yBody = { getHeight() * 7 / 8, getHeight() * 7 / 8, getHeight() * 7 / 8 - 50, getHeight() * 7 / 8,
				getHeight() * 7 / 8, getHeight() * 7 / 8 - 100, getHeight() * 7 / 8 - 100, getHeight() * 7 / 8,
				getHeight() * 7 / 8, getHeight() * 7 / 8 - 50 };
		g.fillPolygon(xBody, yBody, 10);

		g.setColor(new Color(226, 180, 111));
		g.fillOval(getWidth() * 3 / 4, getHeight() * 7 / 8 - 200, 110, 110);

		g.setColor(new Color(145, 145, 145));
		int[] xBeard = { getWidth() * 3 / 4, getWidth() * 3 / 4 + 5, getWidth() * 3 / 4 + 55, getWidth() * 3 / 4 + 105,
				getWidth() * 3 / 4 + 110 };
		int[] yBeard = { getHeight() * 7 / 8 - 150, getHeight() * 7 / 8 - 80, getHeight() * 7 / 8 - 20,
				getHeight() * 7 / 8 - 80, getHeight() * 7 / 8 - 150 };
		g.fillPolygon(xBeard, yBeard, 5);

		g.setColor(new Color(226, 180, 111));
		g.fillArc(getWidth() * 3 / 4 + 40, getHeight() * 7 / 8 - 180, 30, 40, 180, 180);

		g.setColor(new Color(58, 58, 58));
		g.fillOval(getWidth() * 3 / 4 + 35, getHeight() * 7 / 8 - 165, 10, 10);
		g.fillOval(getWidth() * 3 / 4 + 65, getHeight() * 7 / 8 - 165, 10, 10);

		g.setColor(new Color(239, 227, 208));
		g.fillOval(getWidth() * 3 / 4 + 37, getHeight() * 7 / 8 - 163, 3, 3);
		g.fillOval(getWidth() * 3 / 4 + 67, getHeight() * 7 / 8 - 163, 3, 3);

		g.setColor(new Color(239, 227, 208));
		g.fillRect(getWidth() / 32, getHeight() / 2, getWidth() * 5 / 8, 100);
		int[] xSpeech = { getWidth() * 21 / 32, getWidth() * 21 / 32, getWidth() * 21 / 32 + 70 };
		int[] ySpeech = { getHeight() / 2 + 80, getHeight() / 2 + 100, getHeight() / 2 + 100 };
		g.fillPolygon(xSpeech, ySpeech, 3);

		g.setFont(new Font("Apple LiGothic", Font.BOLD, 25));
		g.setColor(new Color(153, 102, 0));
		splitString(speech, getWidth() / 32 + 5, getHeight() / 2 + 5, 25, g);
		
		button.setSize(300, 100);
		button.setFont(new Font("Apple LiGothic", Font.BOLD, 30));
		button.setForeground(new Color(117, 193, 110));
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setBackground(new Color(45, 104, 40));
		button.setLocation(getWidth() * 5 / 8, getHeight() / 8);


		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				setInGame(true);
				frame.setInGame(true);
				frame.setDynamite(dynamiteChosen);
				remove(button);
				remove(speedBoostButton);
				remove(valueMultButton);
				remove(dynamiteButton);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		add(button);

		powerUpButtons();
		
		// Establishes win condition
		// Win condition is based off the values of the randomly generated items; always a feasible score to attain
		winCondition = score;
		for (Item e : items) {
			if (!(e instanceof Rock))
				winCondition += e.getValue();
			else
				winCondition += 0.4 * e.getValue();
		}
		winCondition = (winCondition) / 100 * 100; 
		if(winCondition==0)
			winCondition=100;
		claw.setScore(score);

		drawSpeedBoost(g);
		drawValueMult(g);
		drawDynamite(g);
		
	}

	// Resets the variables to start a level from the beginning
	private void resetLevel() {
		dynamiteChosen = false;
		claw = new Claw();
		frame.setClaw(claw);
		makeLevel();
		valueMult = false;
		speedBoost = false;

	}

	// Restarts the game from the beginning (called when player looses)
	private void restartGame() {
		score = 0;
		level = 0;
		inGame = false;
		frame.setInGame(false);
		resetLevel();

	}

	// Produces items in each level randomly given specific probabilities
	private void makeLevel() {
		items.clear();

		if (level <= 3) {

			goldChance = (double) 20 / 100;
			diamondChance = (double) 30 / 100;
			tntChance = (double) (level * 10 / 100 + goldChance + diamondChance);

		}

		else {

			goldChance = (double) 20 / 100;
			diamondChance = (double) 40 / 100;
			tntChance = (double) 80 / 100;

		}

		for (int i = 0; i < 6 + level; i++) {

			chance = Math.random();

			if (chance < goldChance) {

				items.add(new Gold((int) (Math.random() * (getWidth() - 200) + 50),
						(int) (Math.random() * (getHeight() / 2 - 50)) + getHeight() / 2,
						(int) (Math.random() * 30) + 20));

			}

			else if (chance > goldChance && chance < diamondChance) {

				items.add(new Diamond((int) (Math.random() * (getWidth() - 200) + 50),
						(int) (Math.random() * (getHeight() / 2 - 50)) + getHeight() / 2,
						(int) (Math.random() * 30) + 20));

			}

			else if (chance > goldChance && chance < diamondChance) {

				items.add(new Diamond((int) (Math.random() * (getWidth() - 200) + 50),
						(int) (Math.random() * (getHeight() / 2 - 50)) + getHeight() / 2,
						(int) (Math.random() * 30) + 20));

			}

			else if (chance > diamondChance && chance < tntChance) {

				items.add(new Tnt((int) (Math.random() * (getWidth() - 200) + 50),
						((int) (Math.random() * (getHeight() / 2 - 50)) + getHeight() / 2)));

			}

			else {

				items.add(new Rock((int) (Math.random() * (getWidth() - 100) + 50),
						(int) (Math.random() * (getHeight() / 2 - 50)) + getHeight() / 2,
						(int) (Math.random() * 30) + 20));

			}

		}

	}

	// Draws power up buttons when user has enough money and hasn't already purchased boost
	// When user clicks a button, the power up is applied for the duration of the next level and money is subtracted from score
	// When the user hovers the mouse over a button, the user sees an explanation of the power up
	private void powerUpButtons() {

		speedBoostButton.setSize(170, 40);
		speedBoostButton.setFont(new Font("Apple LiGothic", Font.BOLD, 15));
		speedBoostButton.setForeground(new Color(226, 180, 111));
		speedBoostButton.setOpaque(true);
		speedBoostButton.setBorderPainted(false);
		speedBoostButton.setBackground(new Color(150, 119, 73));
		speedBoostButton.setLocation(getWidth() / 16 + 10, getHeight() * 7 / 8 + 5);

		valueMultButton.setSize(170, 40);
		valueMultButton.setFont(new Font("Apple LiGothic", Font.BOLD, 15));
		valueMultButton.setForeground(new Color(226, 180, 111));
		valueMultButton.setOpaque(true);
		valueMultButton.setBorderPainted(false);
		valueMultButton.setBackground(new Color(150, 119, 73));
		valueMultButton.setLocation(getWidth() / 16 + 190, getHeight() * 7 / 8 + 5);

		dynamiteButton.setSize(170, 40);
		dynamiteButton.setFont(new Font("Apple LiGothic", Font.BOLD, 15));
		dynamiteButton.setForeground(new Color(226, 180, 111));
		dynamiteButton.setOpaque(true);
		dynamiteButton.setBorderPainted(false);
		dynamiteButton.setBackground(new Color(150, 119, 73));
		dynamiteButton.setLocation(getWidth() / 16 + 370, getHeight() * 7 / 8 + 5);

		speedBoostButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (speedBoost == false && score >= 500) {
					score -= 500;
					speedBoost = true;
					remove(speedBoostButton);
					speech = "Welcome! Hover over an item for a description of its ability, \nand click an item to purchase. All items are $500.";
					resultString = "You made it to the next level!\nYou have $" + score + " to spend";
					revalidate();
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				speech = "This power-up makes the claw move faster\nwhen it is grabbing items.";
			}

			@Override
			public void mouseExited(MouseEvent e) {
				speech = "Welcome! Hover over an item for a description of its ability, \nand click an item to purchase. All items are $500.";
			}

		});

		if (score > 500 && speedBoost == false) {
			add(speedBoostButton);
		}else {
			remove(speedBoostButton);
		}
		revalidate();

		valueMultButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (valueMult == false && score >= 500) {
					score -= 500;
					valueMult = true;
					remove(valueMultButton);
					
					if (valueMult)
						for (Item i : items)
							i.setValue((int) (i.getValue() * 1.5));
					
					speech = "Welcome! Hover over an item for a description of its ability, \nand click an item to purchase. All items are $500.";
					resultString = "You made it to the next level!\nYou have $" + score + " to spend";
					revalidate();
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				speech = "This power-up makes every item worth 1.5\ntimes its original value.";
			}

			@Override
			public void mouseExited(MouseEvent e) {
				speech = "Welcome! Hover over an item for a description of its ability, \nand click an item to purchase. All items are $500.";

			}

		});

		if (score > 500 && valueMult == false) {
			add(valueMultButton);
		}else
			remove(valueMultButton);
		
		revalidate();

		dynamiteButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (dynamiteChosen == false && score >= 500) {
					score -= 500;
					dynamiteChosen = true;
					remove(dynamiteButton);
					speech = "Welcome! Hover over an item for a description of its ability, \nand click an item to purchase. All items are $500.";
					resultString = "You made it to the next level!\nYou have $" + score + " to spend";
					revalidate();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				speech = "This power-up allows you to drop items\nyou have picked up. Use the up arrow to\nactivate your dynamite in game.";
			}

			@Override
			public void mouseExited(MouseEvent e) {
				speech = "Welcome! Hover over an item for a description of its ability, \nand click an item to purchase. All items are $500.";

			}

		});

		if (score > 500 && dynamiteChosen == false) {
			add(dynamiteButton);
		} else {
			remove(dynamiteButton);
		}
		
		revalidate();
		
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	// Draws icon for speed boost
	private void drawSpeedBoost(Graphics g) {
		g.setColor(new Color(244, 200, 66));
		int[] x = { getWidth() / 16 + 65, getWidth() / 16 + 125, getWidth() / 16 + 105, getWidth() / 16 + 135,
				getWidth() / 16 + 55, getWidth() / 16 + 95 };
		int[] y = { getHeight() * 7 / 8 - 95, getHeight() * 7 / 8 - 135, getHeight() * 7 / 8 - 95,
				getHeight() * 7 / 8 - 55, getHeight() * 7 / 8 - 10, getHeight() * 7 / 8 - 55 };
		g.fillPolygon(x, y, 6);
	}

	// Draws icon for value multiplier
	private void drawValueMult(Graphics g) {
		g.setColor(new Color(31, 45, 145));
		g.setFont(new Font("Apple LiGothic", Font.BOLD, 70));
		g.drawString("x1.5", getWidth() / 16 + 200, getHeight() * 7 / 8 - 40);
	}

	// Draws icon for dynamite
	private void drawDynamite(Graphics g) {
		g.setColor(new Color(216, 83, 43));
		int[] x = { getWidth() / 16 + 490, getWidth() / 16 + 510, getWidth() / 16 + 440, getWidth() / 16 + 420 };
		int[] y = { getHeight() * 7 / 8 - 135, getHeight() * 7 / 8 - 115, getHeight() * 7 / 8 - 10,
				getHeight() * 7 / 8 - 30 };
		g.fillPolygon(x, y, 4);

		g.setColor(new Color(226, 180, 111));
		int[] xString = { getWidth() / 16 + 495, getWidth() / 16 + 505, getWidth() / 16 + 510, getWidth() / 16 + 500 };
		int[] yString = { getHeight() * 7 / 8 - 130, getHeight() * 7 / 8 - 120, getHeight() * 7 / 8 - 125,
				getHeight() * 7 / 8 - 135 };
		g.fillPolygon(xString, yString, 4);

	}

	// Draws a string, with a new line for every "\n"
	private void splitString(String text, int x, int y, int increment, Graphics g) {
		for (String e : text.split("\n"))
			g.drawString(e, x, y += increment);
	}
}
