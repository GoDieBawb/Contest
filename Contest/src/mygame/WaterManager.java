/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Bob
 */
public class WaterManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private Player            player;
  private BulletAppState    physics;
  private Node              waterNode;
  private Node              fireNode;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.player       = this.stateManager.getState(PlayerManager.class).player;
    this.fireNode     = this.stateManager.getState(FireManager.class).fireNode;
    this.physics      = stateManager.getState(SceneManager.class).physics;
    this.waterNode    = new Node();
    this.app.getRootNode().attachChild(waterNode);
    }
  
  public void createWater() {

    if (waterNode.getChildren().size() < 10 && !player.isDead){
    Node             water     = new Node("Water");
    ParticleEmitter  waterPart = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    RigidBodyControl waterPhys = new RigidBodyControl(1f);
    Material         mat       = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    mat.setTexture("Texture", assetManager.loadTexture("Textures/Water.jpg"));
    
    Box b = new Box(.1f, .1f, .1f);
    Geometry geom = new Geometry("Water", b);
    geom.setMaterial(mat);
    
    waterPart.setMaterial(mat);
    waterPart.setImagesX(2); 
    waterPart.setImagesY(2);
    waterPart.setEndColor(ColorRGBA.Cyan);
    waterPart.setStartColor(ColorRGBA.Blue);
    waterPart.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    waterPart.setStartSize(1.5f);
    waterPart.setEndSize(0.1f);
    waterPart.setGravity(0, 0, 0);
    waterPart.setLowLife(1f);
    waterPart.setHighLife(3f);
    waterPart.getParticleInfluencer().setVelocityVariation(0.3f);
    waterPart.setInWorldSpace(false);

    geom.addControl(waterPhys);
    water.attachChild(waterPart);
    water.attachChild(geom);
    waterNode.attachChild(water);
    
    physics.getPhysicsSpace().add(waterPhys);
    waterPhys.setPhysicsLocation(this.app.getCamera().getLocation());
    waterPhys.setLinearVelocity(this.app.getCamera().getDirection().mult(25));
    }
    
  }
  
  @Override
  public void update(float tpf){
    
    for(int i = 0; i < waterNode.getChildren().size(); i++) {
      Node currentWater = (Node) waterNode.getChild(i);
      
      currentWater.getChild("Emitter").setLocalTranslation(currentWater.getChild("Water").getWorldTranslation());
      
      CollisionResults results = new CollisionResults();
      stateManager.getState(SceneManager.class).scene.collideWith(currentWater.getChild("Water").getWorldBound(), results);
       
      if (results.size() > 0 && !results.getCollision(0).getGeometry().getName().equals("Floor")) {
          
        Vector3f waterSpot = results.getCollision(0).getGeometry().getWorldTranslation();
        
        for (int j = 0; j < fireNode.getChildren().size(); j++) {
          
          Fire currentFire = (Fire) fireNode.getChild(j);
          
          if (waterSpot.distance(currentFire.getLocalTranslation()) < 5) {
            System.out.println("Hit fire: " +  currentFire.health);
            currentFire.health--;
            } else {
            }
          
          }
        
        currentWater.removeFromParent();
        }
      
      if (currentWater.getChild("Water").getLocalTranslation().y < -10){
        waterNode.detachChild(currentWater);
        }
      
      } 
 
    }
  }