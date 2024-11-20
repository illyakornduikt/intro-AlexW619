package entity;

 
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{

	
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public int hasKey = 0;
	
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle(16, 26, 15,15);
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		setDefaultValues();
		getPlayerImage();
	}
	
public void setDefaultValues() {
	
	worldX = gp.tileSize *23;
	worldY = gp.tileSize *21;
	speed = 4;
	direction = "down";
}
public void getPlayerImage() {
	try {
		up1 = ImageIO.read(getClass().getResourceAsStream("/player/up_1.png"));
		up2 = ImageIO.read(getClass().getResourceAsStream("/player/up_2.png"));
		down1 = ImageIO.read(getClass().getResourceAsStream("/player/down_1.png"));
		down2 = ImageIO.read(getClass().getResourceAsStream("/player/down_2.png"));
		left1 = ImageIO.read(getClass().getResourceAsStream("/player/left_1.png"));
		left2 = ImageIO.read(getClass().getResourceAsStream("/player/left_2.png"));
		right1 = ImageIO.read(getClass().getResourceAsStream("/player/right_1.png"));
		right2 = ImageIO.read(getClass().getResourceAsStream("/player/right_2.png"));
	}catch(IOException e) {
		e.printStackTrace();
	}
}

public void update() {
	
	if(keyH.upPressed == true || keyH.downPressed == true || 
			keyH.leftPressed == true || keyH.rightPressed == true) {
		if(keyH.upPressed == true) {
			direction = "up";			 
		}
		else if(keyH.downPressed == true) {
			direction = "down";			 
		}
		else if (keyH.leftPressed == true) {
			direction = "left";			 
		}
		else if(keyH.rightPressed == true) {
			direction = "right";			 
		}
		// Check tile collision
		collisionOn = false;
		gp.Checker.checkTile(this);
		
		//Check obj collision
		int objIndex = gp.Checker.checkObject(this, true);
		pickUpObject(objIndex);
		
		//if coll false player can move
		if(collisionOn == false) {
			
			switch(direction) {
			case "up":
				worldY -= speed;
				break;
				
			case "down":
				worldY += speed;
				break;
				
			case "left":
				worldX -= speed;
				break;
				
			case "right":
				worldX += speed;
				break;
			}
		}
		spriteCounter++;
		if(spriteCounter > 10) {
			if(spriteNum == 1) {
				spriteNum = 2;
			}
			else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
	}
	
	
}
public void pickUpObject(int i) {
	
	if(i != 999) {
		
		String objectName = gp.obj[i].name;
		
		switch(objectName) {
		case"Key":
			gp.playSE(1);
			hasKey++;
			gp.obj[i] =null;
			 gp.ui.showMessage("You picked up the Key!");
			break;
		case"Door":
			gp.playSE(3);
			if(hasKey > 0) {
				gp.obj[i] = null;
				hasKey--;
				gp.ui.showMessage("You opened the door");
			}
			else {
				gp.ui.showMessage("You need a key");
			}
			break;
			
		case "Boots":
			gp.playSE(2);
			speed +=1.3;
			gp.obj[i] = null;
			gp.ui.showMessage("Like it!");
			break;
		case"Chest":
			gp.ui.gameFinished = true;
			gp.stopMusic();
			gp.playSE(4);
			break;
		}
	}
}
public void draw(Graphics2D g2) {
	
	//g2.setColor(Color.white);
//g2.fillRect(x, y, gp.tileSize, gp.tileSize);
	
	BufferedImage image = null;
	
	switch(direction) {
	case "up" :
		if(spriteNum == 1) {
			image = up1;
		}
		if(spriteNum ==2) {
			image = up2;
		}
		break;
	case "down":
		if(spriteNum == 1) {
		image = down1;
		}
		if(spriteNum == 2) {
			image = down2;
		}
		break;
	case "left":
		if(spriteNum == 1) {
		image = left1;
		}
		if(spriteNum == 2) {
			image = left2;
		}
		break;
	case "right":
		if(spriteNum == 1) {
		image = right1;
		}
			if(spriteNum == 2) {
		image = right2;
			}
		
		break;
	}
	g2.drawImage(image, screenX, screenY,gp.tileSize,gp.tileSize, null );
	//g2.setColor(Color.red);
	//g2.drawRect(screenX + solidArea.x,screenY + solidArea.y, solidArea.width, solidArea.height );
}
}
