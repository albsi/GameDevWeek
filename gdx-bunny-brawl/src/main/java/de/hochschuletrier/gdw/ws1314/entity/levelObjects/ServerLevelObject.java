package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;

/**
 * 
 * @author yannick
 * 
 */
public abstract class ServerLevelObject extends ServerEntity
{
	protected boolean isVisible;
	protected EntityStates entityState = EntityStates.NONE;

	@Override
	public void enable()
	{
		this.isVisible = true;
	}

	@Override
	public void disable()
	{
		this.isVisible = false;
	}

	@Override
	public void initialize()
	{
		this.isVisible = true;
	}

    @Override
    public void reset()
    {
        physicsBody.setPosition(new Vector2(properties.getFloat("x"), properties.getFloat("y")));
    }



	public void setVisibility(boolean b)
	{
		this.isVisible = b;
	}

	public boolean getVisibility()
	{
		return this.isVisible;
	}

	public void setEntityState(EntityStates state){this.entityState = state;}
	public EntityStates getEntityState(){return entityState;}
}
