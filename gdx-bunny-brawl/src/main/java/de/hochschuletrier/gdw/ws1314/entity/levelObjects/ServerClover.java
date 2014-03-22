package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 *
 */
public class ServerClover extends ServerLevelObject
{
	public static final float CLOVER_HEALTHBUFF_FACTOR = 1.5f;
	public static final float CLOVER_HEALTHBUFF_DURATION = 2.f;
	
	public ServerClover()
	{
		super();
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
	}
	
	@Override
	public void beginContact(Contact contact) {
//            ServerEntity otherEntity = this.identifyContactFixtures(contact);
//
//        switch(otherEntity.getEntityType()) {
//            case Tank:
//            case Hunter:
//            case Knight:
//            case Noob:
//                ServerEntityManager.getInstance().removeEntity(this);
//                break;
//            default:
//                break;
//        }
	}

	@Override
	public void endContact(Contact contact)
	{
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.Clover;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
            PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, manager)
                .position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
                .fixedRotation(false).create();

            body.createFixture(new PhysixFixtureDef(manager)
                .density(0.5f)
                .friction(0.0f)
                .restitution(0.0f)
                .shapeCircle(16)
                .sensor(true));

            body.setGravityScale(0);
            body.addContactListener(this);
            setPhysicsBody(body);
	}

    @Override
    public void update(float deltaTime) {
        
    }

}
