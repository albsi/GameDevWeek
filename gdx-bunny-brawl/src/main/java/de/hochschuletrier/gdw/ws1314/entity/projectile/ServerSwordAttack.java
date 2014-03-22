/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ws1314.entity.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import static de.hochschuletrier.gdw.ws1314.entity.EntityType.SwordAttack;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

/**
 *
 * @author Patrick
 */
// Modified by El Fapo: Added getSourceID
public class ServerSwordAttack extends ServerEntity {
    
    //========================================
    // VARIABLES
    private long sourceID;
    private TeamColor teamColor;
    private Vector2 originPosition;
    private float damage;
    private float despawnTime;
    private float height;
    private float amplitude;
    
    //========================================
    public ServerSwordAttack() {
        super();
        
    }
    
    public void setSource(long sourceID) {
        this.sourceID = sourceID;

        ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sourceID);

        this.teamColor = player.getTeamColor();
        setFacingDirection(player.getFacingDirection());
        this.originPosition = player.getPosition();
    }
    

	public long getSourceID() {
		return sourceID;
	}
    
    /**
     * The collision zone of the Sword attack has the shape of a triangle.
     * @param height of the triangle
     * @param amplitude  of the source angle
     */
    public void setSize(float height, float amplitude) {
        this.amplitude = amplitude;
        this.height = height;
    }
    
    public void setDamage(float dmg) {
    	damage = dmg;
    }
    
    public float getDamage() {
    	return damage;
    }
    
    public void setDespawnTime(float t) {
        despawnTime = t;
    }
    
    public float getDespawnTime() {
        return despawnTime;
    }
    
    public TeamColor getTeamColor() {
	return teamColor;
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float deltaTime) {
        despawnTime -= deltaTime;
        ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sourceID);
        this.physicsBody.setPosition(player.getPosition().x, player.getPosition().y);
        if(despawnTime < 0) {
            ServerEntityManager.getInstance().removeEntity(this);
        }
    }

    @Override
    public EntityType getEntityType() {
        return SwordAttack;
    }

    @Override
    public void initPhysics(PhysixManager manager) {
    	// Modified by ElFapo, because polygons do not work
//        int oX = (int) this.originPosition.x;
//        int oY = (int) this.originPosition.y;
//        Point pointA = new Point(oX, oY);
//        
//        float size = (float) Math.sqrt(2 * Math.pow(this.height, 2));
//        
//        Vector2 sideL = this.facingDirection.getDirectionVector().cpy().rotate((float) Math.toDegrees(this.amplitude) * 0.5f);
//        Vector2 sideR = this.facingDirection.getDirectionVector().cpy().rotate((float) Math.toDegrees(this.amplitude) * -0.5f);
//        
//        int lX = (int) (oX + (sideL.x * size));
//        int lY = (int) (oY + (sideL.y * size));
//        
//        int rX = (int) (oX + (sideR.x * size));
//        int rY = (int) (oY + (sideR.y * size));
//        
//        Point pointB = new Point(lX, lY);
//        Point pointC = new Point(rX, rY);
//        
//        ArrayList<Point> points = new ArrayList<>();
//        points.add(pointA);
//        points.add(pointB);
//        points.add(pointC);
        
        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, manager)
                .position(this.originPosition)
                .fixedRotation(true)
                .angle(getFacingDirection().getAngle() - (float) Math.PI / 2.0f)
                .create();
        
        body.createFixture(new PhysixFixtureDef(manager)
                .density(0.5f)
                .friction(0.0f)
                .restitution(0.0f)
                .shapeBox(amplitude, height * 2.0f, new Vector2(0.0f, height), 0.0f)
                .sensor(true));
        body.setGravityScale(0);
        body.addContactListener(this);
            
        setPhysicsBody(body);
    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void reset(){

    }
}
