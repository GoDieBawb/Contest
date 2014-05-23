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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bob
 */
public class GuiManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private ButtonAdapter     hoseButton;
  private ButtonAdapter     startButton;
  private BitmapFont        font;
  private BitmapText        scoreText;
  private Screen            screen;
  private Player            player;
  private FlyByCamera       flyCam;
  private InputManager      inputManager;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.player       = this.stateManager.getState(PlayerManager.class).player;
    this.flyCam       = this.app.getFlyByCamera();
    this.inputManager = this.app.getInputManager();
    screen            = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
    screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
    screen.setUseMultiTouch(true);
    this.app.getGuiNode().addControl(screen);
    initHud();
    initScoreDisplay();
    }
  
  private void initHud(){

    /**hoseButton = new ButtonAdapter(screen, "ChaseButton", new Vector2f(15, 15) ) {
    @Override
    public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
      stateManager.getState(WaterManager.class).createWater();
      }
    };

    screen.addElement(hoseButton);
    hoseButton.setDimensions(screen.getWidth()/10, screen.getWidth()/10);
    hoseButton.setPosition(screen.getWidth() * .9f - hoseButton.getWidth()/2, screen.getHeight() * .1f - hoseButton.getHeight()/2);
    hoseButton.setText("Hose");
    hoseButton.setFont("Interface/Fonts/Impact.fnt");
    **/

    startButton = new ButtonAdapter( screen, "StartButton", new Vector2f(15, 15) ) {
    @Override
      public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
        startButton.hide();
        inputManager.setCursorVisible(false);
        stateManager.getState(CameraManager.class).cam.setDragToRotate(false);
        player.isDead = false;
        player.score  = 0;
        }
      };
    
    screen.addElement(startButton);
    startButton.setDimensions(screen.getWidth()/8, screen.getHeight()/10);
    startButton.setPosition(screen.getWidth() / 2 - startButton.getWidth()/2, screen.getHeight() / 2 - startButton.getHeight()/2);
    startButton.setText("Start Game");
    startButton.setFont("Interface/Fonts/Impact.fnt");
    }
  
  private void initScoreDisplay(){
    font              = this.assetManager.loadFont("Interface/Fonts/Impact.fnt");
    scoreText         = new BitmapText(font, false);
    scoreText.scale(.5f);
    scoreText.setText("Fires Extinguished: " + player.score);
    this.app.getGuiNode().attachChild(scoreText);
    scoreText.setLocalTranslation(screen.getWidth() / 1.1f - scoreText.getLineWidth()/2, screen.getHeight() / 1.05f - scoreText.getLineHeight()/2, -1);
    }
  
  public void showStartButton(){
    inputManager.setCursorVisible(true);
    stateManager.getState(CameraManager.class).cam.setDragToRotate(true);
    startButton.show();
    }
  
  private void updateHud(){
    scoreText.setText("Fires Extinguished: " + player.score);
    }
  
  @Override
  public void update(float tpf){
    updateHud();
    }
  
  }