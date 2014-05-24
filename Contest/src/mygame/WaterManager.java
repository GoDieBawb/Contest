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
  public  Node              waterNode;
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
  
  //Creates water on click
  public void createWater() {
    //Checks to see if water may be created
    if (waterNode.getChildren().size() < 5 && !player.isDead){
    
    //Creates the water Object, emitter and material for the water
    Water            water     = new Water();
    ParticleEmitter  waterPart = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    RigidBodyControl waterPhys = new RigidBodyControl(1f);
    Material         mat       = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    mat.setTexture("Texture", assetManager.loadTexture("Textures/Water.jpg"));
    
    //Creates the box to be launched
    Box b = new Box(.1f, .1f, .1f);
    Geometry geom = new Geometry("Water", b);
    geom.setMaterial(mat);
    
    //Sets the water's creation time
    water.startTime = System.currentTimeMillis()/1000;
    
    //Sets up the emitter details
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
    waterPart.setParticlesPerSec(5);
    waterPart.setNumParticles(10);

    //Puts together the water Node
    geom.addControl(waterPhys);
    water.attachChild(waterPart);
    water.attachChild(geom);
    waterNode.attachChild(water);
    
    //Adds the water's physics tot he physics space
    physics.getPhysicsSpace().add(waterPhys);
    
    //Sets the waters location to the camera, and launches it in the camera's direction
    waterPhys.setPhysicsLocation(this.app.getCamera().getLocation());
    waterPhys.setLinearVelocity(this.app.getCamera().getDirection().mult(25));
    }
    
  }
  
  @Override
  public void update(float tpf){
    //Iterates over the water Node
    for(int i = 0; i < waterNode.getChildren().size(); i++) {
 
      Water currentWater = (Water) waterNode.getChild(i);
      
      //constantly sets the water's emitter to its parent location
      currentWater.getChild("Emitter").setLocalTranslation(currentWater.getChild("Water").getWorldTranslation());
      
      //Create collision for the currentWater to collide with the scene
      CollisionResults results = new CollisionResults();
      stateManager.getState(SceneManager.class).scene.collideWith(currentWater.getChild("Water").getWorldBound(), results);
       
      //If there are results, make sure it's not with the floor or the "FuckPillar" which bugged me
      if (results.size() > 0 && !results.getCollision(0).getGeometry().getName().equals("Floor") && !results.getCollision(0).getGeometry().getName().equals("FuckPillar")) {
          
        //Gets the spot in which the water hit the building
        Vector3f waterSpot = results.getCollision(0).getGeometry().getWorldTranslation();
        
        //Iterates over the fireNode
        for (int j = 0; j < fireNode.getChildren().size(); j++) {
          
          Fire currentFire = (Fire) fireNode.getChild(j);
          
          //Checks to see if any fire was within range of the water and hurts the fire
          if (waterSpot.distance(currentFire.getLocalTranslation()) < 5) {
            currentFire.health--;
            }
          
          }
        //Removes the water due to hitting the building
        currentWater.removeFromParent();
        }
      
      //Cleans up water if falls off map, or is stuck for too long
      if (currentWater.getChild("Water").getLocalTranslation().y < -1f ^ System.currentTimeMillis()/1000 - currentWater.startTime > 3){
        waterNode.detachChild(currentWater);
        }
      
      } 
 
    }
  }