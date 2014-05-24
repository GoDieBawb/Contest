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
import com.jme3.input.ChaseCamera;

/**
 *
 * @author Bob
 */
public class CameraManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private Player            player;
  public  ChaseCamera       cam;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.player       = this.stateManager.getState(PlayerManager.class).player;
    initCamera();
    }
  
  //Creates camera
  public void initCamera(){
    //Creates a new chase cam and attached it to the player.model for the game
    cam = new ChaseCamera(this.app.getCamera(), player.model, this.app.getInputManager());
    cam.setMinDistance(1);
    cam.setMaxDistance(1);
    }
  
  }
