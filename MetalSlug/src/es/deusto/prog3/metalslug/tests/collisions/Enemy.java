package es.deusto.prog3.metalslug.tests.collisions;

import java.util.ArrayList;
import java.util.Iterator;

public class Enemy extends Character {
	
	private Player player;
	private Thread updateShooting;
	private boolean shooting;
	private Thread changeDirection;
	private boolean movingLeft;
	private boolean dead;

	public Enemy(float x, float y, Player player) {
		super(x, y, 30, 30, 100);
		this.player = player;
		dead = false;
		updateShooting = new Thread(() -> {
			while(true) {
				if(distanceTo(this.getCenter(), player.getCenter()) < 500)
					shoot();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) { }
			}
		});
		
		changeDirection = new Thread(() -> {
			movingLeft = true;
			while(true) {
				movingLeft = !movingLeft;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) { }
			}
		});
		updateShooting.start();
		changeDirection.start();
	}

	private void shoot() {
		TestGame.enemyBullets.add(new Bullet(this, player.getCenterX(), player.getCenterY(), 0.5f));
	}

	private float distanceTo(float[] a, float[] b) {
		// TODO Auto-generated method stub
		return (float) Math.hypot(a[0] - b[0], a[1] - b[1]);
	}
	
	public void update(int delta, ArrayList<Bullet> playerbullets, ArrayList<Granada> granadas) {
		moveX(delta, movingLeft);
		
		moveY(delta);
		detectPlatformCollisions();
		for(Iterator<Bullet> iterator = playerbullets.iterator(); iterator.hasNext();) {
			Bullet b = iterator.next();
			if(this.contains(b.getX(), b.getY())) {
				dead = true;
			}
		}
		for(Iterator<Granada> iterator = granadas.iterator(); iterator.hasNext();) {
			Granada g = iterator.next();
			if(this.intersects(g)) {
				dead = true;
			}
		}
	}
	
	public boolean isDead() {
		return dead;
	}

}
