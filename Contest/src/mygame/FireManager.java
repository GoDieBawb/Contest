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
  
  private void createFire(){
    Fire fire      = new Fire();
    fire.setName("Fire");
    //Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Orange);
    //mat.setTexture("Texture", assetManager.loadTexture("Textures/Fire.png"));
    fire.health  = 5;
    fire.spreadTime = System.currentTimeMillis()/1000;
    
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
    fire.attachChild(firePart);
    fireNode.attachChild(fire);
    placeFire(fire);
    }
  
  private void placeFire(Fire fire){
    Random rand  = new Random();
    int fireSpot = rand.nextInt(windowNode.getChildren().size());
    fire.setLocalTranslation(windowNode.getChild(fireSpot).getLocalTranslation());
    }
  
  @Override
  public void update(float tpf){
    
  if (!player.isDead) {
      
    if (fireNode.getChildren().size() < 5) {
      createFire();
      }
    
    for (int i = 0; i < fireNode.getChildren().size(); i++) {
      
      Fire currentFire = (Fire) fireNode.getChild(i);
      
      if(System.currentTimeMillis()/1000 - currentFire.spreadTime > 5) {
        
        Random rand            = new Random();
        int spreadChance       = rand.nextInt(5);
        currentFire.spreadTime = System.currentTimeMillis()/1000;
        
        if (spreadChance == 1) {
          createFire();
          System.out.println("Fire has spread: " + fireNode.getChildren().size());
          } else {
          System.out.println("Failed to spread: " + spreadChance);
          }
        }

      if (currentFire.health < 0) {
        player.score++;
        fireNode.detachChild(currentFire);
        System.out.println("Fire Removed");
        }
      }

    if (fireNode.getChildren().size() > 12) {
      player.isDead =  true;
      fireNode.detachAllChildren();
      stateManager.getState(WaterManager.class).waterNode.detachAllChildren();
      
      stateManager.getState(GuiManager.class).showStartButton();
      this.app.getFlyByCamera().setEnabled(false);
      System.out.println("You've died");
      }

    }
  }
}
