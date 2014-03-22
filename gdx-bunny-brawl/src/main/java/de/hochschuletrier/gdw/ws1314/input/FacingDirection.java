package de.hochschuletrier.gdw.ws1314.input;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author ElFapo
 *
 */

public enum FacingDirection 
{
	NONE(0.0f, 0.0f),
	
	LEFT(-1.0f, 0.0f),
	DOWN(0.0f, 1.0f),
	RIGHT(1.0f, 0.0f),
	UP(0.0f, -1.0f),
	
	DOWN_LEFT(1.0f / -(float)Math.sqrt(2.0f), 1.0f / (float)Math.sqrt(2.0f)),
	DOWN_RIGHT(1.0f / (float)Math.sqrt(2.0f), 1.0f / (float)Math.sqrt(2.0f)),
	UP_LEFT(1.0f / -(float)Math.sqrt(2.0f), 1.0f / -(float)Math.sqrt(2.0f)),
	UP_RIGHT(1.0f / (float)Math.sqrt(2.0f), 1.0f / -(float)Math.sqrt(2.0f));
	
	private final Vector2 direction;
	private final float	  angle;
	
	private FacingDirection(float x, float y)
	{
		direction = new Vector2(x, y);
		angle = direction.x == 0.0f && 
				direction.y == 0.0f ? 0 //180deg
						: direction.getAngleRad();
	}
	
	public Vector2 getDirectionVector()
	{
		return direction;
	}
	
	public float getAngle()
	{
		return angle;
	}
}
