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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;
import tonegod.gui.effects.Effect.EffectEvent;
import tonegod.gui.effects.Effect.EffectType;

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
  private TextElement       infoText;
  private TextElement       insText;
  private Indicator         fireInd;
  private Indicator         waterInd;
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
    font              = this.assetManager.loadFont("Interface/Fonts/Impact.fnt");
    screen            = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
    screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
    screen.setUseMultiTouch(true);
    this.app.getGuiNode().addControl(screen);
    initHud();
    initScoreDisplay();
    initFireLevel();
    initWaterLevel();
    }
  
  private void initHud(){
    //Creates the start Button
    startButton = new ButtonAdapter( screen, "StartButton", new Vector2f(15, 15) ) {
    
    @Override
      public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
        
        startButton.hideWithEffect();
        infoText.hideWithEffect();
        insText.hideWithEffect();
        
        //Set the cursor invisible as the game is starting
        inputManager.setCursorVisible(false);
        stateManager.getState(CameraManager.class).cam.setDragToRotate(false);
        
        //Resets the player
        player.isDead = false;
        player.score  = 0;
        }
      };
    
    //Sets up the start button details
    screen.addElement(startButton);
    startButton.setDimensions(screen.getWidth()/8, screen.getHeight()/10);
    startButton.setPosition(screen.getWidth() / 2 - startButton.getWidth()/2, screen.getHeight() / 2);
    startButton.setFont("Interface/Fonts1/Impact.fnt");
    startButton.hide();
    
    Effect slideIn = new Effect(EffectType.SlideIn, EffectEvent.Show, 1f);
    Effect slideOut = new Effect(EffectType.SlideOut, EffectEvent.Hide, 1f);
    startButton.addEffect(EffectEvent.Hide, slideOut);
    startButton.addEffect(EffectEvent.Show, slideIn);
    
    //Sets up the infoText
    infoText = new TextElement(screen, "InfoText", Vector2f.ZERO, new Vector2f(300,50), font) {
    @Override
    public void onUpdate(float tpf) {  }
    @Override
    public void onEffectStart() {  }
    @Override
    public void onEffectStop() {  }
    };
    
    //Sets up the details of the info display
    infoText.setIsResizable(false);
    infoText.setIsMovable(false);
    infoText.setTextWrap(LineWrapMode.NoWrap);
    infoText.setTextVAlign(VAlign.Center);
    infoText.setTextAlign(Align.Center);
    infoText.setFontSize(font.getPreferredSize());
    infoText.setFontColor(ColorRGBA.Orange);
    
    Effect slideIn1 = new Effect(EffectType.SlideIn, EffectEvent.Show, 1f);
    Effect slideOut1 = new Effect(EffectType.SlideOut, EffectEvent.Hide, 1f);
    //infoText.addEffect(EffectEvent.Hide, slideOut1);
    //infoText.addEffect(EffectEvent.Show, slideIn1);
    
    //Add the info display
    screen.addElement(infoText);
    infoText.setLocalTranslation(screen.getWidth()/2 - infoText.getWidth()/2, screen.getHeight()/2 + infoText.getHeight()*2, -1);
    infoText.hide();
 
    //Sets up the instructionText
    insText = new TextElement(screen, "InsText", Vector2f.ZERO, new Vector2f(300,50), font) {
    @Override
    public void onUpdate(float tpf) {  }
    @Override
    public void onEffectStart() {  }
    @Override
    public void onEffectStop() {  }
    };

    //Sets up the details of the Info display
    insText.setIsResizable(false);
    insText.setIsMovable(false);
    insText.setTextWrap(LineWrapMode.NoWrap);
    insText.setTextVAlign(VAlign.Center);
    insText.setTextAlign(Align.Center);
    insText.setFontSize(font.getPreferredSize()/2);
    
    Effect slideIn2 = new Effect(EffectType.SlideIn, EffectEvent.Show, 1f);
    Effect slideOut2 = new Effect(EffectType.SlideOut, EffectEvent.Hide, 1f);
    //insText.addEffect(EffectEvent.Hide, slideOut2);
    //insText.addEffect(EffectEvent.Show, slideIn2);
    
    //Add the instruction display
    screen.addElement(insText);
    insText.setLocalTranslation(screen.getWidth()/2 - insText.getWidth()/2, screen.getHeight()/2 - insText.getHeight(), -1);
    insText.hide();
    
    showStartButton("Start Game", "Fire Department", "Extinguish 100 Fires To Save The Building");
    }
  
  //Sets up the score display
  private void initScoreDisplay(){
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
 
    //Add the score display
    screen.addElement(scoreText);
    
    scoreText.setText("Fires Extinguished: " + player.score);
    scoreText.setLocalTranslation(screen.getWidth() / 1.1f - scoreText.getWidth()/1.8f, screen.getHeight() / 1.05f - scoreText.getHeight()/2, -1);
    }
  
  private void initFireLevel(){
    fireInd = new Indicator(
      screen,
      "Fire Ind",
      new Vector2f(12,12),
      Indicator.Orientation.HORIZONTAL
      ) {

    @Override
      public void onChange(float currentValue, float currentPercentage) {  
        }
      };
    
    fireInd.setMaxValue(13 - 5 );
    fireInd.setCurrentValue(0);
    fireInd.setIndicatorColor(ColorRGBA.Red);
    fireInd.setText("Fire Level");
    fireInd.setTextWrap(LineWrapMode.NoWrap);
    fireInd.setTextVAlign(VAlign.Center);
    fireInd.setTextAlign(Align.Center);
    fireInd.setFont("Interface/Fonts2/Impact.fnt");
    fireInd.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
    fireInd.setIndicatorPadding(new Vector4f(7,7,7,7));
    fireInd.setDimensions(150, 30);
    screen.addElement(fireInd);
    }

  private void initWaterLevel(){
    waterInd = new Indicator(
      screen,
      "Water Ind",
      new Vector2f(12,50),
      Indicator.Orientation.HORIZONTAL
      ) {

    @Override
      public void onChange(float currentValue, float currentPercentage) {  
        }
      };
    
    waterInd.setMaxValue(8);
    waterInd.setCurrentValue(0);
    waterInd.setIndicatorColor(ColorRGBA.Blue);
    waterInd.setText("Water");
    waterInd.setTextWrap(LineWrapMode.NoWrap);
    waterInd.setTextVAlign(VAlign.Center);
    waterInd.setTextAlign(Align.Center);
    waterInd.setFont("Interface/Fonts2/Impact.fnt");
    waterInd.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
    waterInd.setIndicatorPadding(new Vector4f(7,7,7,7));
    waterInd.setDimensions(150, 30);
    screen.addElement(waterInd);
    }
  
  //Shows the start button
  public void showStartButton(String buttonText, String otherText, String ins){
    inputManager.setCursorVisible(true);
    stateManager.getState(CameraManager.class).cam.setDragToRotate(true);
    
    startButton.setText(buttonText);
    startButton.showWithEffect();
    
    infoText.setText(otherText);
    infoText.showWithEffect();
    
    insText.setText(ins);
    insText.showWithEffect();
    }
  
  //Updates the score display to the player's current score
  private void updateHud(){
    scoreText.setText("Fires Extinguished: " + player.score);
    fireInd.setCurrentValue(stateManager.getState(FireManager.class).fireNode.getChildren().size() - 5);
    waterInd.setCurrentValue(8 - stateManager.getState(WaterManager.class).waterNode.getChildren().size());
    }
  
  //Update logic to update the score
  @Override
  public void update(float tpf){
    updateHud();
    }
  
  }