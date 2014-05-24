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
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class FireManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private Node              windowNode;
  private Node              rootNode;
  public  Node              fireNode;
  private Player            player;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.windowNode   = (Node) this.stateManager.getState(SceneManager.class).scene.getChild("WindowNode");
    this.player       = this.stateManager.getState(PlayerManager.class).player;
    this.rootNode     = this.app.getRootNode();
    this.fireNode     = new Node();
    rootNode.attachChild(fireNode);
    }
  
  //Creates a new Fire
  private void createFire(){
    //Crate the fire object  
    Fire fire      = new Fire();
    fire.setName("Fire");
    
    //Create Particle textures
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    mat.setTexture("Texture", assetManager.loadTexture("Textures/Fire.png"));
    
    //set fire health and spreadTimes
    fire.health  = 5;
    fire.spreadTime = System.currentTimeMillis()/1000;
    
    //Creation of the fire particle emitter
    ParticleEmitter firePart = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    firePart.setMaterial(mat);
    firePart.setImagesX(2); 
    firePart.setImagesY(2);
    firePart.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));
    firePart.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f));
    firePart.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    firePart.setStartSize(1.5f);
    firePart.setEndSize(0.1f);
    firePart.setGravity(0, 0, 0);
    firePart.setLowLife(1f);
    firePart.setHighLife(3f);
    firePart.getParticleInfluencer().setVelocityVariation(0.3f);
    firePart.setParticlesPerSec(5);
    firePart.setNumParticles(10);
    
    //attaching the emitter to the fire and the fire to the fireNode
    fire.attachChild(firePart);
    fireNode.attachChild(fire);
    
    //Sends the fire to be randomly placed
    placeFire(fire);
    }
  
  //Places the fire at a random window in the window Node
  private void placeFire(Fire fire){
    Random rand  = new Random();
    int fireSpot = rand.nextInt(windowNode.getChildren().size());
    fire.setLocalTranslation(windowNode.getChild(fireSpot).getLocalTranslation());
    }
  
  //Fire Manager Update Logic
  @Override
  public void update(float tpf){
  //Make sure the player isn't dead  
  if (!player.isDead) {
    
    //Must be a minimum of 5 fires  
    if (fireNode.getChildren().size() < 5) {
      createFire();
      }
    
    //Iterate over each fire
    for (int i = 0; i < fireNode.getChildren().size(); i++) {
      
      Fire currentFire = (Fire) fireNode.getChild(i);
      
      //Causes the fire to have a chance to spread every 5 seconds
      if(System.currentTimeMillis()/1000 - currentFire.spreadTime > 5) {
        
        Random rand            = new Random();
        int spreadChance       = rand.nextInt(5);
        
        //Reset the spread time to the current time to wait another 5 seconds
        currentFire.spreadTime = System.currentTimeMillis()/1000;
        
        //If the random int equals one create a new fire
        if (spreadChance == 1) {
          createFire();
          }
        }

      //If the fires health is less or equal to 0 remove the fire
      if (currentFire.health <= 0) {
        player.score++;
        fireNode.detachChild(currentFire);
        System.out.println("Fire Removed");
        }
      }

    //If there is more than 12 fires the player has lost the game
    if (fireNode.getChildren().size() > 12) {
      //Set the player to dead  
      player.isDead =  true;
      //Removes all Children from the fire and water nodes
      fireNode.detachAllChildren();
      stateManager.getState(WaterManager.class).waterNode.detachAllChildren();
      //Shows the start menu
      stateManager.getState(GuiManager.class).showStartButton("Play Again", "You Lose", "You needed " + String.valueOf(100 - player.score) + " more fires");
      }

    if (player.score > 99) {
      //Set the player to dead  
      player.isDead =  true;
      //Removes all Children from the fire and water nodes
      fireNode.detachAllChildren();
      stateManager.getState(WaterManager.class).waterNode.detachAllChildren();
      //Shows the start menu
      stateManager.getState(GuiManager.class).showStartButton("Play Again", "Fire Extinguished!", "Good Job!");
      }

    }
  }
}
