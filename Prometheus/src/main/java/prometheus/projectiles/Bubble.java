package prometheus.projectiles;

import javafx.scene.paint.Color;
import prometheus.GameLoop;
import prometheus.constants.Direction;
import prometheus.enemies.Runner;
import prometheus.enemies.Wizard;
import prometheus.entity.Entity;
import prometheus.entity.MovingEntity;
import prometheus.entity.boundedbox.RectBoundedBox;
import prometheus.entity.staticobjects.AcidPool;
import prometheus.entity.staticobjects.Wall;
import prometheus.scenes.Sandbox;

public class Bubble implements MovingEntity{
	
	public static Long lastBubble = System.currentTimeMillis();
    public int positionX = 0;
    public int positionY = 0;
    private int height;
    private int width;
    RectBoundedBox entityBoundary;
    public Direction currentDirection = Direction.UP;
    public boolean alive;

    
    public Bubble(int x, int y, Direction d) {
    	lastBubble = System.currentTimeMillis();
        positionX = x;
    	positionY = y;
    	width = 16;
    	height = 16;
    	if(d == null)
    		d = Direction.UP;
    	this.currentDirection = d;
    	this.alive = true;
        entityBoundary = new RectBoundedBox(positionX-5, positionY-5, width+5, height+5);
    }
    
    public boolean isAlive(){
    	return alive;
    }
    
    @Override
    public boolean isColliding(Entity b) {
        RectBoundedBox otherEntityBoundary = b.getBoundingBox();
        return entityBoundary.checkCollision(otherEntityBoundary);
    }

    @Override
    public void draw() {
    	Sandbox.getGraphicsContext().setStroke(Color.CORNFLOWERBLUE);
    	Sandbox.getGraphicsContext().strokeOval(positionX+5, positionY+5, 20, 20);
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
    public RectBoundedBox getBoundingBox() {
        return entityBoundary;
    }

    @Override
    public boolean isPlayerCollisionFriendly() {
        return true;
    }

	@Override
	public void onCollision(Entity e) {
		if(e instanceof Wall) {
			this.alive = false;
		}
		if(e instanceof AcidPool) {
			this.alive = false;
		}
		if(e instanceof Wizard) {
			((Wizard) e).reduceHealth(10);
			this.alive = false;
		}
		if(e instanceof Runner) {
			((Runner) e).reduceHealth(10);
			this.alive = false;
		}
	}
	
    private boolean checkCollisions(int x, int y) {
    	
    	entityBoundary.setPosition(x, y);
    	
        for (Entity e : Sandbox.getEntities()) {
            if(e != this && isColliding(e)) {
            	this.onCollision(e);
            }
        }
        return false;
    }

	@Override
	public void move(int steps, Direction direction) {
        steps *= GameLoop.getDeltaTime();

        if (steps == 0) {
            return;
        } else {
            switch (direction) {
                case UP:
	                positionY -= steps;
	                currentDirection = Direction.UP;
                    break;
                case DOWN:
                		positionY += steps;
	                    currentDirection = Direction.DOWN;
                    break;
                case LEFT:
                		positionX -= steps;
	                    currentDirection = Direction.LEFT;
                        break;
                case RIGHT:
                		 positionX += steps;
	                    currentDirection = Direction.RIGHT;
                    break;
                default:
            }

            checkCollisions(positionX, positionY);
        }
		
	}
    

}
