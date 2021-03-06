package es.deusto.prog3.metalslug.tests.bullets;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Player extends Rectangle {
	
	private Input input;
	private int vy;
	private boolean hasjumped;
	private static final int GRAVITY = 1500; // In pixels/s^2
	private Bullet bala;

	public Player() {
		super(200, 200, 30, 30);
		input = new Input(720);
		
	}
	
	protected void jump() {
		// TODO Auto-generated method stub
		if(!hasjumped) {
			vy = -800;
			setY(getY() - 2);
		}
		hasjumped = true;
	}

	private static final long serialVersionUID = 1L;
	
	public void update(int delta) {
		if(input.isKeyDown(Input.KEY_LEFT))
			moveX(delta, true);
		if(input.isKeyDown(Input.KEY_RIGHT))
			moveX(delta, false);
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			shootBullet(delta, true);
		moveY(delta);
		
		detectCollisions();
	}

	private void shootBullet(int delta, boolean b) {
		
		bala = new Bullet(new Vector2f(getX(),getY()),new Vector2f(Mouse.getX(),Mouse.getY()));
		
	}

	private void detectCollisions() {
		// TODO Auto-generated method stub
		boolean intersected = false;
		for(Rectangle platform : TestGame.platforms) {
			if(this.intersects(platform)) {
				intersected = true;
				float leftOverlap = this.getMaxX() - platform.getMinX();
				float rightOverlap = platform.getMaxX() - this.getMinX();
				float bottomOverlap = this.getMaxY() - platform.getMinY();
				float topOverlap = platform.getMaxY() - this.getMinY();
				float overlappingSide = CollisionUtils.min(leftOverlap, rightOverlap, bottomOverlap, topOverlap);
				
				if(overlappingSide == leftOverlap) {
					setX(getX() - leftOverlap);
				} else if(overlappingSide == rightOverlap) {
					setX(getX() + rightOverlap);
				} else if(overlappingSide == bottomOverlap) {
					setY(getY() - bottomOverlap);
					vy = 0;
					hasjumped = false;
				} else if(overlappingSide == topOverlap) {
					vy = 0;
					setY(getY() + topOverlap + 1);
					
				}
			}
		}
		if(!intersected) {
			hasjumped = true;
		}
	}

	private void moveY(int delta) {
		// TODO Auto-generated method stub
		float deltaSeconds = delta/1000f;
		setY(getY() + vy * deltaSeconds);
		vy += GRAVITY * deltaSeconds;
	}

	private void moveX(int delta, boolean isLeft) {
		// TODO Auto-generated method stub
		float deltaSeconds = delta/1000f;
		setX(getX() + (isLeft ? -300*deltaSeconds : 300*deltaSeconds));
	}

	

}
