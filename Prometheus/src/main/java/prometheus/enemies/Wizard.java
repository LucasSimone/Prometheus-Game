package prometheus.enemies;

import javafx.scene.image.Image;
import prometheus.constants.Direction;
import prometheus.constants.GlobalConstants;
import prometheus.entity.Entity;
import prometheus.entity.KillableEntity;
import prometheus.entity.boundedbox.RectBoundedBox;
import prometheus.projectiles.WizardProjectile;
import prometheus.scenes.Sandbox;
import prometheus.utils.ImageUtils;

public class Wizard implements KillableEntity {

	//initalize stats
    private int health;
    private boolean isAlive;
    RectBoundedBox boundry;
    
    //declare images
    Image up;
    Image down;
    Image left;
    Image right;

    public Direction currentDirection;

    public int positionX = 0;
    public int positionY = 0;

    String name;
	public long lastShot;

    public Wizard() { // default wizard spawn 
        init(64,64);
    }

    public Wizard(int posX, int posY) { // wizard spawn given x,y spawn location
        init(posX, posY);
        health = 10;
        isAlive = true;
    }
    
    public void shoot(int speed) {	// wizard shoots bubble projectile 
    	Sandbox.addEntityToGame(new WizardProjectile(positionX, positionY, speed));
    }

    private void init(int x, int y) { // set wizard with appropriate location, images, and hitbox
        name = "Wizard";
        positionX = x;
        positionY = y;

        boundry = new RectBoundedBox(positionX-5, positionY-5, GlobalConstants.PLAYER_WIDTH+5, GlobalConstants.PLAYER_HEIGHT+5);
        
        down  = ImageUtils.loadImage("Resources/img/sprites/wizard.png");
        right  = ImageUtils.loadImage("Resources/img/sprites/wizard.png");
        up = ImageUtils.loadImage("Resources/img/sprites/wizard.png");
        left = ImageUtils.loadImage("Resources/img/sprites/wizard.png");
        this.lastShot = System.currentTimeMillis();
    }
    

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean isColliding(Entity b) { // boolean for if hitboxes of two entities are in contact
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) b.getBoundingBox();
        return boundry.checkCollision(otherEntityBoundary);
    }

    @Override
    public void draw() {
    	if(this.currentDirection == null) 
    		this.currentDirection = Direction.UP;
    	this.currentDirection = getDirectionTo(Sandbox.getPlayer());
    	
    	switch (this.currentDirection) { // changes character image based on which way the character is looking
		case DOWN:
			Sandbox.getGraphicsContext().drawImage(this.down, positionX, positionY, GlobalConstants.PLAYER_WIDTH * GlobalConstants.PLAYER_SCALE, GlobalConstants.PLAYER_HEIGHT * GlobalConstants.PLAYER_SCALE);
			break;
		case LEFT:
			Sandbox.getGraphicsContext().drawImage(this.left, positionX, positionY, GlobalConstants.PLAYER_WIDTH * GlobalConstants.PLAYER_SCALE, GlobalConstants.PLAYER_HEIGHT * GlobalConstants.PLAYER_SCALE);
			break;
		case RIGHT:
			Sandbox.getGraphicsContext().drawImage(this.right, positionX, positionY, GlobalConstants.PLAYER_WIDTH * GlobalConstants.PLAYER_SCALE, GlobalConstants.PLAYER_HEIGHT * GlobalConstants.PLAYER_SCALE);
			break;
		case UP:
			Sandbox.getGraphicsContext().drawImage(this.up, positionX, positionY, GlobalConstants.PLAYER_WIDTH * GlobalConstants.PLAYER_SCALE, GlobalConstants.PLAYER_HEIGHT * GlobalConstants.PLAYER_SCALE);
			break;
		case UP_LEFT:
			Sandbox.getGraphicsContext().drawImage(this.up, positionX, positionY, GlobalConstants.PLAYER_WIDTH * GlobalConstants.PLAYER_SCALE, GlobalConstants.PLAYER_HEIGHT * GlobalConstants.PLAYER_SCALE);
			break;
		case UP_RIGHT:
			Sandbox.getGraphicsContext().drawImage(this.up, positionX, positionY, GlobalConstants.PLAYER_WIDTH * GlobalConstants.PLAYER_SCALE, GlobalConstants.PLAYER_HEIGHT * GlobalConstants.PLAYER_SCALE);
			break;
		default:
			Sandbox.getGraphicsContext().drawImage(this.down, positionX, positionY, GlobalConstants.PLAYER_WIDTH * GlobalConstants.PLAYER_SCALE, GlobalConstants.PLAYER_HEIGHT * GlobalConstants.PLAYER_SCALE);
			break;
    	
    	}
    }

    @Override
    public void die() {
    	this.isAlive = false;
    	Sandbox.getPlayer().getStats().addKills(1);  //when enemy dies increment player's kill counter
    }

    @Override
    public void reduceHealth(int damage) { // method to reduce damage 
        if (health - damage <= 0) {
            die();
        } else {
            health -= damage;
        }
    }

    @Override
    public int getPositionX() {
        return positionX;
    }

    @Override
    public int getPositionY() {
        return positionY;
    }

    @Override
    public RectBoundedBox getBoundingBox() { // creates hitbox with given x,y values
        boundry.setPosition(positionX, positionY);
        return boundry;
    }

    @Override
    public boolean isPlayerCollisionFriendly() {
        return false;
    }

	@Override
	public void onCollision(Entity e) {

	}
	
	public Direction getDirectionTo(Entity e) {  // directions to get to set entity
		int deltaX = e.getPositionX() - this.positionX;
		int deltaY = e.getPositionY() - this.positionY;
		
		if(deltaX == 0) {
			if(deltaY == 0) {
				return Direction.DOWN;
			}
			else if(deltaY > 0) {
				return Direction.DOWN;
			}
			else if(deltaY < 0) {
				return Direction.UP;
			}
		}
		
		if(deltaY == 0) {
			if(deltaX == 0) {
				return Direction.DOWN;
			}
			else if(deltaX > 0) {
				return Direction.RIGHT;
			}
			else if(deltaX < 0) {
				return Direction.LEFT;
			}
		}
		
		if(deltaY > 0 && deltaX > 0)
			return Direction.DOWN_RIGHT;
		
		if(deltaY > 0 && deltaX < 0)
			return Direction.DOWN_LEFT;
		
		if(deltaY < 0 && deltaX > 0)
			return Direction.UP_RIGHT;
		
		if(deltaY < 0 && deltaX < 0)
			return Direction.UP_LEFT;
		
		return Direction.DOWN;
	}
}