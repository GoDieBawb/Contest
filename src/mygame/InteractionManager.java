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
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 *
 * @author Bob
 */
public class InteractionManager extends AbstractAppState implements ActionListener {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    setUpKeys();
    }
  
  private void setUpKeys(){
    this.app.getInputManager().addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    this.app.getInputManager().addListener(this, "Click");
    }

  public void onAction(String binding, boolean isPressed, float tpf) {
  
    if (binding.equals("Click")) {
      stateManager.getState(WaterManager.class).createWater();
      }
    
    }
  
  }
