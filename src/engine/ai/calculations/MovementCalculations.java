package engine.ai.calculations;

import engine.ai.controller.AiController;
import engine.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class MovementCalculations {

	protected Player aiPlayer;
	protected Rectangle map;	
	
	public MovementCalculations(AiController aiCon, Rectangle map) {
		this.aiPlayer = aiCon.getAiPlayer();
		this.map = map;
	}


	public boolean reachedAnEdge() {
		double x = aiPlayer.getLocation().getX();
		double y = aiPlayer.getLocation().getY();
		if(x<map.getHeight()*0.02 || x>map.getHeight()*0.98 || y<map.getWidth()*0.02 || y>map.getWidth()*0.98)
			return true;
		return false;
	}
	
	/*returns integer value, between 0 and 7 inclusive, to indicate movement direction to move away from edge
	 * 0 = down 
	 * 1 = right cart
	 * 2 = down cart
	 * 3 = right 
	 * 4 = up cart
	 * 5 = left 
	 * 6 = left cart
	 * 7 = up 
	 */
	public int closestEdgeLocation() {
		Point2D loc = aiPlayer.getLocation();
		int dir = -1;
		double x = loc.getX();
		double y = loc.getY();
		double width = map.getWidth();
		double height = map.getHeight();
		//x < 1000
		if(x<width/2) {
			//x<1000 and y < 1000 - first block, up-most
			if(y<height/2) {
				//x < 100 
				if(x<(width*0.05)) {
					// x and y < 100,(at start point) - move down cart 
					if(y<(height*0.05)) {
						dir = 0;
					}
					// x < 100 but 100> y >1000 left-up wall - move right
					else {
						dir = 1;
					}
				}
				//x > 100
				else {
					// 1000> x >100 and y < 100, right-up wall - move down
					if(y<(height*0.05)) {
						dir = 2;
					}
					// 1000 > x&y > 100  - close to middle point, not near a wall
					else {
					}
				}
			}
			//x<1000 and y > 1000 - second block, left-most
			else {
				//x < 100
				if(x<(width*0.05)) {
					// x < 100 and y > 1900,(left start point) - right cart
					if(y>(height*0.95)) {
						dir = 3;
					}
					// x < 100 and 1000 < y < 1900, left-up wall - move right
					else {
						dir = 1;
					}
				}
				//x 100 < x < 1000
				else {
					//x 100 < x < 1000 and y > 1900 left-down wall, move up
					if(y>(height*0.95))
						dir = 4;
					//close to middle point, not near a wall
					else {
					}
						
				}
				
			}
		}
		//x > 1000
		else {
			//y < 1000 - third block, right-most
			if(y<height/2) {
				// x > 1000 & y < 100
				if(y<height*0.05) {
					// x < 1900 and y < 100,(right start point) - left cart
					if(x>width*0.95) {
						dir = 5;
					}
					//y < 100 and x < 1900, right-up wall -  down
					else {
						dir = 2;
					}
				}
				//x > 1000 and 100 > y > 1000
				else {
					// x > 1900 and 100 < y < 1000, right-down wall, left
					if(x>width*0.95)
						dir = 6;
					//else goes to the centre, not near a wall
					else {
					}
				}
				
			}
			//x and y > 1000	fourth block, down-most
			else {
				//x > 1900 and y > 1000
				if(x>width*0.95) {
					//x and y > 1900, (down start point) - up cart
					if(y>height*0.95) {
						dir = 7;
					}
					//x > 1900 and 1000 < y < 1900, right-down wall, left
					else {
						dir = 6;
					}
				}
				//1000 < x < 1900
				else {
					//1000 < x < 1900 and y > 1900, left-down wall, up
					if(y>height*0.95) {
						dir = 4;
					}
					//going to the centre, not close to wall
					else {
					}
				}
			}
		}
		
		return dir;
	}
	
	public double calcAngle(Point2D loc1, Point2D loc2) {

		double x = loc1.getX() - loc2.getX();
		double y = loc1.getY() - loc2.getY();
		double angle = Math.toDegrees(Math.atan2(y, x)) - 90.0;
		return angle;
	}

	public double calcDistance(Point2D a, Point2D b) {
		return a.distance(b);
	}
	
	
}
