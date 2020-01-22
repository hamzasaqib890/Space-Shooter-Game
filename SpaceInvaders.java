 /**
 * SpaceInvaders.java
 * Hamza Saqib
 *
 * Simple game where you fly a spaceship to protect yourself and a larger spaceship by shooting incoming asteroids.
 *
 */
import java.io.*;
import java.net.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SpaceInvaders extends JFrame implements ActionListener, KeyListener{
	// SpaceInvaders class which creates the game object and updates it with keyboard input info.
	Timer myTimer;
	GamePanel game;
	
    public SpaceInvaders(){
    	// constructor method for SpaceInvaders class
    	super("Space Invaders");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1600, 1000);

		myTimer = new Timer(16, this);	 // trigger every 16 ms (~60 fps)
		myTimer.start();
		
		game = new GamePanel();
		add(game);
		addKeyListener(this);
		setResizable(false);
		setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt){
		if (game != null){
			game.refresh();
			game.repaint();
		}
	}

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
    	game.setKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
    	game.setKey(e.getKeyCode(), false);
    }

    public static void main(String[] arguments) {
		SpaceInvaders frame = new SpaceInvaders();
    }  
}

class GamePanel extends JPanel implements MouseListener{
	// GamePanel class which runs the mechanics of the game itself and displays it
	private int posBack0, posBack1, posBack2, posBack3, astTimer, mode, mx, my; // posBack for moving backgrounds
	private boolean[] keys;
	private Image logo, background0, background1, background2, background3; // multiple backgrounds
	private Ship playerShip, bigShip;
	private Image[] astImages1;
	private ArrayList<Asteroid> asteroids;
	private Font font40, font60, font90;
	private boolean click;
	

	public GamePanel(){
		addMouseListener(this);
		keys = new boolean[KeyEvent.KEY_LAST+1];
		logo = new ImageIcon("logo.png").getImage();
		posBack1 = 0;
		posBack2 = 400;
		posBack3 = 1000;
		background0 = new ImageIcon("background.jpg").getImage();
		background0 = background0.getScaledInstance(1600, 1000, Image.SCALE_SMOOTH);
		background1 = new ImageIcon("background1.png").getImage();
		background1 = background1.getScaledInstance(1600, 1000, Image.SCALE_SMOOTH);
		background2 = new ImageIcon("background2.png").getImage();
		background2 = background2.getScaledInstance(1600, 1000, Image.SCALE_SMOOTH);
		background3 = new ImageIcon("background3.png").getImage();
		background3 = background3.getScaledInstance(1600, 1000, Image.SCALE_SMOOTH);
		
		try{
			// loading Quanitfy font
			font40 = Font.createFont(Font.TRUETYPE_FONT, new File("Quantify Bold v2.6.ttf")).deriveFont(40f);
			font60 = Font.createFont(Font.TRUETYPE_FONT, new File("Quantify Bold v2.6.ttf")).deriveFont(60f);
			font90 = Font.createFont(Font.TRUETYPE_FONT, new File("Quantify Bold v2.6.ttf")).deriveFont(90f);
		}
		catch (Exception e){
		}
		
		astImages1 = new Image[16]; // 16 frames for the asteroid
		for (int i = 0; i < 16; i++){
			astImages1[i] = new ImageIcon("asteroids/a" + i + ".png").getImage();
		}
		
		astTimer = 0; // timer to spawn asteroids
		asteroids = new ArrayList<Asteroid>(); // array to contain asteroids
		
		playerShip = new Ship("ship", 3, 84, 94, 760, -4, 72, 800);
		bigShip = new Ship("big_ship", 1, 700, 405, 780, 0, 0, 0);
		
		mode = 0; // main menu mode
		
		setSize(1600, 1000);
	}
	
	public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
    	}
    public void mouseClicked(MouseEvent e){
    	click = true;
    	mx = e.getX();
    	my = e.getY();

    }
    public void mousePressed(MouseEvent e){}

    public void setKey(int k, boolean v) {
    	keys[k] = v;
    }

	public void refresh(){
		if (mode == 1){
			if(keys[KeyEvent.VK_RIGHT]){
				playerShip.move(7, 0);
			}
			if(keys[KeyEvent.VK_LEFT]){
				playerShip.move(-7, 0);
			}
			if(keys[KeyEvent.VK_SPACE]){
				playerShip.shoot(true);
			}
			else{
				playerShip.shoot(false);
			}
			
			if (astTimer <= 0){ // 0.5 chance of asteroid spawning every 40 updates of the game
				astTimer = 40;
				if (Math.random() <= 0.5){
					asteroids.add(new Asteroid(ThreadLocalRandom.current().nextInt(5, 1476), -120, astImages1));
				}
			}
			else{
				astTimer --;
			}
		}
		else if (mode == 0){
			if (click && mx >= 700 && mx <= 880 && my <= 655 && my >= 585){
    			mode = 1;
    		}
		}
		else if (mode == 2){
			if (click && mx >= 570 && mx <= 1000 && my <= 655 && my >= 585){
				// resetting the game settings for new game
				astTimer = 0;
				asteroids = new ArrayList<Asteroid>();
				playerShip = new Ship("ship", 3, 84, 94, 760, -4, 72, 800);
				bigShip = new Ship("big_ship", 1, 700, 405, 780, 0, 0, 0);
    			mode = 1;
    		}
		}
	}
	
	public static boolean collide(Rectangle rect1, Rectangle rect2){
		// simple collide function for two rectangles
		return rect1.y < rect2.y + rect2.height && rect1.height + rect1.y > rect2.y && rect1.x < rect2.x + rect2.width && rect1.x + rect1.width > rect2.x;
	}

    public void paintComponent(Graphics g){
    	// drawing everything on the screen
		g.drawImage(background0, 0, posBack0, this); // 4 layers for the moving background
    	g.drawImage(background0, 0, posBack0 - 1000, this);
    	g.drawImage(background1, 0, posBack1, this);
    	g.drawImage(background1, 0, posBack1 - 1000, this);
    	g.drawImage(background2, 0, posBack2, this);
    	g.drawImage(background2, 0, posBack2 - 1000, this);
    	g.drawImage(background3, 0, posBack3, this);
    	g.drawImage(background3, 0, posBack3 - 1000, this);
    	
    	if (posBack0 < 1000){
    		posBack0 += 1;
    	}
    	else{
    		posBack0 = 0;
    	}
    	if (posBack1 < 1000){
    		posBack1 += 3;
    	}
    	else{
    		posBack1 = 0;
    	}
    	if (posBack2 < 1000){
    		posBack2 += 5;
    	}
    	else{
    		posBack2 = 0;
    	}
    	if (posBack3 < 1000){
    		posBack3 += 7;
    	}
    	else{
    		posBack3 = 0;
    	}

    	if (mode == 1){
    		// game mode
    		bigShip.moveBig(playerShip); // moving and drawing big and small ships
    		bigShip.draw(g, this, keys);
	    	playerShip.draw(g, this, keys);
    		playerShip.updateAsteroidsBullets(asteroids, bigShip, g, this); // checks asteroid collision with everything
    		playerShip.drawHits(g, font40); // "+50" briefly displayed when user hits an asteroid 
    		playerShip.drawCooldown(g, this); // drawing user interface stuff
    		playerShip.drawScore(g, font60);
    		playerShip.drawHealth(g, this);
    		playerShip.drawControls(g, this, font40);
    		if (playerShip.getLives() <= 0){
    			// gameover screen when player dies
    			mode = 2;
    		}
    	}
    	else if (mode == 0){
    		// main menu
    		g.drawImage(logo, 450, -75, this);
    		g.setColor(Color.ORANGE);
			g.setFont(font90);
			g.drawString("Play", 700, 650);
    	}
    	else if (mode == 2){
    		// game over screen
    		g.setColor(Color.RED);
    		g.setFont(font90);
    		g.drawString("GAME OVER", 530, 230);
    		g.setColor(Color.BLUE);
    		g.setFont(font60);
    		g.drawString("Score: " + playerShip.getScore(), 650, 420);
    		g.setColor(Color.ORANGE);
			g.setFont(font90);
			g.drawString("Play Again", 570, 650);
    	}
    	
    	click = false;	
    }
}

class Ship{
	// ship class for the two ships in the game
	private int lenX, lenY, bulletX1, bulletX2, bulletY, shootTimer, lives;
	private double posX, posY, cooldown, score;
	private Image[] images; // ship sprites
	private Image bulletImage, cdEmpty, heart, controls1, controls2, shieldImg; // ui images and more
	private BufferedImage cdFull; // cooldown ui image (BufferedImage so it can be cropped later)
	private ArrayList<Bullet> bullets; // arraylist of bullets
	private boolean disabled, shield;
	private ArrayList<Hit> hits;
	private Polygon[] polygons; // 4 trapezoids drawed on the screen for the ui
	
	public Ship(String fileName, int img, int lenX, int lenY, int posY, int bulletX1, int bulletX2, int bulletY){
		// constructor method for the Ship class
		images = new Image[img];
		for (int i = 0; i < img; i++){
			images[i] = new ImageIcon(fileName + (i + 1) + ".png").getImage();
			images[i] = images[i].getScaledInstance(lenX, lenY, Image.SCALE_SMOOTH);
		}
		
		bulletImage = new ImageIcon("beams.png").getImage();
		bullets = new ArrayList<Bullet>();
		
		// ui images
		cdEmpty = new ImageIcon("cooldown1.png").getImage();
		try{
			// opening BufferedImage
			URL url = this.getClass().getResource("cooldown2.png");
			cdFull = ImageIO.read(url);
		}
		catch (Exception e){
		}
		controls1 = new ImageIcon("controls1.png").getImage();
		controls1 = controls1.getScaledInstance(159, 107, Image.SCALE_SMOOTH);
		controls2 = new ImageIcon("controls2.png").getImage();
		controls2 = controls2.getScaledInstance(161, 54, Image.SCALE_SMOOTH);
		
		shieldImg = new ImageIcon("shield.png").getImage();
		shieldImg = shieldImg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
		shield = false;
		
		polygons = new Polygon[] {new Polygon(new int[] {0, 66, 66, 0}, new int[] {300, 350, 650, 700}, 4), // shapes to make the ui pretty
								  new Polygon(new int[] {0, 300, 250, 0}, new int[] {0, 0, 100, 100}, 4),
								  new Polygon(new int[] {1600, 1300, 1350, 1600}, new int[] {0, 0, 100, 100}, 4),
								  new Polygon(new int[] {400, 1200, 1130, 470}, new int[] {0, 0, 115, 115}, 4)};
		
		lives = 3;
		heart = new ImageIcon("heart.png").getImage();
		heart = heart.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		
		this.lenX = lenX;
		this.lenY = lenY;
		
		posX = (1600 - lenX)/2; // spawn at the midde of the screen
		this.posY = posY;
		
		this.bulletX1 = bulletX1; // where bullets are shot from in reference to the ship
		this.bulletX2 = bulletX2;
		this.bulletY = bulletY;
		hits = new ArrayList<Hit>();
		cooldown = 200;
		disabled = false;
		shootTimer = 0;
	}
	
	public void move(int x, int y){
		// simple method to move the position of the ship
		if (posX + x >= 0 &&  posX + x <= 1600 - lenX){
			posX += x;
		}
		posY += y;
	}
	
	public void shoot(boolean shoot){
		// shoot method called everytime game updates but with boolean shoot = true when player is shooting
		if (cooldown > 0 && shoot && !disabled){
			// player succesfully shoots
			cooldown -= 0.5;
			if (shootTimer <= 0){
				// adding bullets to arraylist
				bullets.add(new Bullet((int)posX + bulletX1, bulletY, bulletImage));
				bullets.add(new Bullet((int)posX + bulletX2, bulletY, bulletImage));
				shootTimer = 600;
			}
		}
		if (cooldown == 0 && shoot){
			// cooldown reaches 0 then the ship gets disabled
			disabled = true;
		}		
		if ((cooldown < 200 && !shoot && !disabled) || (cooldown < 200 && disabled)){
			// cooldown increasing when player is not shooting or once the ship gets disabled
			cooldown ++;
		}
		if (disabled && cooldown == 200){
			// ship recools
			disabled = false;
		}
		if (shootTimer > 0){
			// increases fire rate as cooldown decreases
			shootTimer -= 200 - cooldown;
		}
	}	
	public void draw(Graphics g, GamePanel gp, boolean[] keys){
		// drawing ship and shield
		if(images.length > 1 && keys[KeyEvent.VK_RIGHT] ){
			// player turning right
			g.drawImage(images[1], (int) posX,(int) posY, gp);
		}
		else if(images.length > 1 && keys[KeyEvent.VK_LEFT] ){
			// player turning left
			g.drawImage(images[2], (int) posX, (int) posY, gp);
		}
		else{
			// player standing still
			g.drawImage(images[0], (int) posX, (int) posY, gp);
		}
		if (shield){
			g.drawImage(shieldImg, (int) posX - 28, (int) posY - 21, gp);
		}
	}
	
	public void drawScore(Graphics g, Font font){
		score += 0.04; // score increasing every update of the game
		if (score % 500 <= 5){
			// free shield for the player every 500 score
			shield = true;
		}
		g.setColor(Color.GRAY);
		g.fillPolygon(polygons[2]);
		if (hits.size() > 0){
			// score is displayed as green when player hits an asteroid
			g.setColor(Color.GREEN);
		}
		else{
			// default colour
			g.setColor(Color.ORANGE);
		}
		g.setFont(font);
		g.drawString("" + (int)score, 1400, 78); // displaying score
	}
	
	public int getScore(){
		return (int) score;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void drawHealth(Graphics g, GamePanel gp){
		g.setColor(Color.GRAY);
		g.fillPolygon(polygons[1]);
		for (int i = 0; i < lives; i ++){
			// drawing a heart for each of the player's lives
			g.drawImage(heart, 50 + 70 * i, 25, gp);
		}
	}
	
	public void drawCooldown(Graphics g, GamePanel gp){
		g.setColor(Color.GRAY);
		g.fillPolygon(polygons[0]);
		g.drawImage(cdEmpty, 10, 359, gp);
		// draws cropped cooldown timer as it descreases
		g.drawImage(cdFull.getSubimage(0, 282 - 282 * (int)cooldown / 200, 46, 282 * (int)cooldown / 200), 10, 359 + 282 - 282 * (int)cooldown / 200, gp);
	}
	
	public void drawControls(Graphics g, GamePanel gp, Font font){
		// displays the controls at the top of the screen
		g.setColor(Color.GRAY);
		g.fillPolygon(polygons[3]);
		g.drawImage(controls1, 460, 5, gp);
		g.drawImage(controls2, 690, 10, gp);
		g.setColor(Color.ORANGE);
		g.setFont(font);
		g.drawString("Hold to Shoot", 651, 100);
		g.setColor(Color.BLUE);
		g.drawString("Every 500 score", 890, 45);
		g.drawString("get a Shield!", 910, 95);
	}
	
	public double getCentreX(){
		// returns centre of the ship
		return posX + lenX / 2;
	}
	
	public void moveBig(Ship playerShip){
		// method to move the big ship 0.01 of the way towards the player's ship
		posX += (playerShip.getCentreX() - getCentreX()) * 0.01;
	}
	
	public void updateAsteroidsBullets(ArrayList<Asteroid> asteroids, Ship bigShip, Graphics g, GamePanel gp){
		// checks collision of all the asteroids with bullets and ships
		boolean removeA;
		for (int a = asteroids.size() - 1; a >=0; a--){
			removeA = false;
			if (asteroids.get(a).onScreen()){
    			asteroids.get(a).draw(g, gp);
    			asteroids.get(a).move(2 + (int) score / 150); // asteroids gettings faster as game progresses	
    		}
    		else{
    			// remove if the asteroid is of the screen
    			removeA = true;
    		}
    		if (GamePanel.collide(asteroids.get(a).getRect(), getRect()) || GamePanel.collide(asteroids.get(a).getRect(), bigShip.getRect()) && asteroids.get(a).getRect().y < 1000){
    			// asteroid hits a ship
    			removeA = true;
    			if (shield){
    				shield = false;
    			}
    			else{
    				lives --;
    			}
    		}
			for (int b = bullets.size() - 1; b >= 0; b--){
				if (GamePanel.collide(bullets.get(b).getRect(), asteroids.get(a).getRect())){
					// bullet hits an asteroid
					hits.add(new Hit(asteroids.get(a).getRect().x, asteroids.get(a).getRect().y));
					score += 5;
    				bullets.remove(b);
    				removeA = true;
    				break;
				}
			}
			if (removeA){
				asteroids.remove(a);
			}	
    	}
    	drawBullets(g, gp); // drawing bullets
	}
	public void drawHits(Graphics g, Font font) {
		// looping through all the hits to draw them
		for (int i = hits.size() - 1; i >=0; i--){
			if (hits.get(i).updateDraw(g, font)){
				hits.remove(i);
			}
		}
	}
	public void drawBullets(Graphics g, GamePanel gp){
		// drawing all the bullets
		for (int i = bullets.size() - 1; i >= 0; i--){
			if (bullets.get(i).onScreen()){
    			bullets.get(i).draw(g, gp);
    			bullets.get(i).move();	
    		}
    		else{
    			// removing from arraylist if they're off the screen
    			bullets.remove(i);
    		}
		}
	}
	public Rectangle getRect(){
		return new Rectangle((int)posX, (int)posY, lenX, lenY);
	}
	
}

class Hit{
	// hit class used to draw "+5" when player hits an asteroid
	private int posX, posY, timer;
	
	public Hit(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
		timer = 20; // only displaying the "+5" for 20 updates of the game
	}
	
	public boolean updateDraw(Graphics g, Font font){
		if (timer >= 0){
			timer --;
			posY -= 3; // giving the effect that the "+5" is floating away
			g.setColor(Color.GREEN);
			g.setFont(font);
			g.drawString("+5", posX, posY);
			return false;
		}
		else{
			return true;
		}
	}
}

class Bullet{
	// bullet class for the ship's bullets
	private int posX, posY, lenX, lenY;
	private Image image;
	
	public Bullet(int posX, int posY, Image image){
		this.image = image;
		
		this.posX = posX;
		this.posY = posY;
		
		lenX = 17;
		lenY = 25;
	}
	
	public void draw(Graphics g, GamePanel gp){
		// method to draw bullet
		g.drawImage(image, posX, posY, gp);
	}
	
	public boolean onScreen(){
		// method which checks if the bullet is still on the screen
		if (posY < -lenY){
			return false;
		}
		else{
			return true;
		}
	}
	
	public void move(){
		// moving the bullet upwards
		posY -= 35;
	}
	
	public Rectangle getRect(){
		// used for collision detection
		return new Rectangle(posX, posY, lenX, lenY);
	}
}

class Asteroid{
	// asteroid class for the incoming flying asteroids
	private int posX, posY, lenX, lenY, imageC;
	private Image[] images;
	
	public Asteroid(int posX, int posY, Image[] images){
		this.posX = posX;
		this.posY = posY;
		
		this.images = images;
		
		lenX = 120;
		lenY = 120;
		
		imageC = 0; // used to keep track of the frame
	}
	
	public void draw(Graphics g, GamePanel gp){
		g.drawImage(images[(int) imageC/5], posX, posY, gp);
	}
	
	public void move(int x){
		posY += x;
		if (imageC == 75){
			imageC = 0;
		}
		else{
			// changing the frame
			imageC ++;
		}
	}
	
	public boolean onScreen(){
		// method to check if the asteroid is still on the screen
		if (posY > 1000 + lenY){
			return false;
		}
		else{
			return true;
		}
	}
	
	public Rectangle getRect(){
		// used for collision detection
		return new Rectangle(posX + 40, posY + 40, 40, 40);
	}
}