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
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class PlayerManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private BulletAppState    physics;
  public  Player            player;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.physics      = this.stateManager.getState(SceneManager.class).physics;
    initPlayer();
    }
  
  private void initPlayer(){
    player            = new Player();
    player.isDead     =  true;
    player.model      = new Node();
    player.playerPhys = new BetterCharacterControl(1f, 2.5f, 1f);
    player.playerPhys.setGravity(new Vector3f(0, -50, 0));
    player.attachChild(player.model);
    player.addControl(player.playerPhys);
    this.app.getRootNode().attachChild(player);
    player.model.setLocalTranslation(0f, 2.5f, 0f);
    physics.getPhysicsSpace().add(player.playerPhys);
    player.playerPhys.warp(new Vector3f(15f, 5f, 15f));
    }
  
  }
