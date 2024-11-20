package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
//screen settings
	final int originalTileSize = 16; //16 * 16
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; //48*48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;//778 pixels
	public final int screenHeight = tileSize * maxScreenRow;//576 pixels
	
	//World set
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
	//FPS
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Sound music = new Sound();
	Sound se = new Sound();
	
	public CollChecker Checker = new CollChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	Thread gameThread;
	
	//ENTITY And OBJ
	public Player player = new Player(this,keyH);
	public SuperObject obj[] = new SuperObject[10];
			
			
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		
		aSetter.setObject();
		
		playMusic(0);
	}
	
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
		
	}

	@Override
	public void run() {
		 
		double drawInterval = 1000000000/FPS; //0.01666 seconds
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		while(gameThread !=null) {
			 
			// 1 update: update information such as character positions
			update();
			
			
			// 2 draw: draw the screen with the updated information 
			repaint();
			
			double remainingTime = nextDrawTime - System.nanoTime();	
			remainingTime = remainingTime/1000000;
			
			if(remainingTime <0) {
				remainingTime = 0;
			}
			
			try {
				Thread.sleep((long) remainingTime);
				
				nextDrawTime += drawInterval;
				
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}
	public void update() {
		
		player.update();
	 
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	    Graphics2D g2= (Graphics2D)g; 
		// Tile
		tileM.draw(g2);
		//OBJect
		for(int i = 0; i < obj.length; i++ ) {
			if(obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		
		//Player
		player.draw(g2);
		
		//UI
		ui.draw(g2);
		 
		g2.dispose();
			
		}
		public void playMusic(int i) {
			
			music.setFile(i);
			music.play();
			music.loop();
		}
		public void stopMusic() {
			
			music.stop();
		}
		public void playSE(int i) {
			
			se.setFile(i);
			se.play();
		}
		
	}
	

