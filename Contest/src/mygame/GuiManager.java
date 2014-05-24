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
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import com.jme3.input.InputManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bob
 */
public class GuiManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private ButtonAdapter     startButton;
  private TextElement       scoreText;
  private BitmapFont        font;
  private Screen            screen;
  private Player            player;
  private InputManager      inputManager;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.player       = this.stateManager.getState(PlayerManager.class).player;
    this.inputManager = this.app.getInputManager();
    screen            = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
    screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
    screen.setUseMultiTouch(true);
    this.app.getGuiNode().addControl(screen);
    initHud();
    initScoreDisplay();
    }
  
  private void initHud(){
    //Creates the start Button
    startButton = new ButtonAdapter( screen, "StartButton", new Vector2f(15, 15) ) {
    @Override
      public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
        //On click hide start menu, set invis cursor and proper camera, reset player
        startButton.hide();
        inputManager.setCursorVisible(false);
        stateManager.getState(CameraManager.class).cam.setDragToRotate(false);
        player.isDead = false;
        player.score  = 0;
        }
      };
    
    //Sets up the start button details
    screen.addElement(startButton);
    startButton.setDimensions(screen.getWidth()/8, screen.getHeight()/10);
    startButton.setPosition(screen.getWidth() / 2 - startButton.getWidth()/2, screen.getHeight() / 2 - startButton.getHeight()/2);
    startButton.setText("Start Game");
    startButton.setFont("Interface/Fonts/Impact.fnt");
    }
  
  //Sets up the score display
  private void initScoreDisplay(){
    font              = this.assetManager.loadFont("Interface/Fonts/Impact.fnt");
    scoreText = new TextElement(screen, "ScoreText", Vector2f.ZERO, new Vector2f(300,50), font) {
    @Override
    public void onUpdate(float tpf) {  }
    @Override
    public void onEffectStart() {  }
    @Override
    public void onEffectStop() {  }
    };
    
    //Sets up the details of the score display
    scoreText.setIsResizable(false);
    scoreText.setIsMovable(false);
    scoreText.setTextWrap(LineWrapMode.NoWrap);
    scoreText.setTextVAlign(VAlign.Center);
    scoreText.setTextAlign(Align.Center);
    scoreText.setFontSize(18);
    scoreText.setText("This is a sample TextElement");
 
    //Add the score display
    screen.addElement(scoreText);
    
    scoreText.setText("Fires Extinguished: " + player.score);
    scoreText.setLocalTranslation(screen.getWidth() / 1.1f - scoreText.getWidth()/1.8f, screen.getHeight() / 1.05f - scoreText.getHeight()/2, -1);
    }
  
  //Shows the start button
  public void showStartButton(){
    inputManager.setCursorVisible(true);
    stateManager.getState(CameraManager.class).cam.setDragToRotate(true);
    startButton.show();
    }
  
  //Updates the score display to the player's current score
  private void updateHud(){
    scoreText.setText("Fires Extinguished: " + player.score);
    }
  
  //Update logic to update the score
  @Override
  public void update(float tpf){
    updateHud();
    }
  
  }