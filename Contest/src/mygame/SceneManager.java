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
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {

  private SimpleApplication app;
  private AssetManager      assetManager;
  public  Node              scene;
  private Node              rootNode;
  public  BulletAppState    physics;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.assetManager = this.app.getAssetManager();
    this.rootNode     = this.app.getRootNode();
    this.physics      = new BulletAppState();
    stateManager.attach(physics);
    initBuilding();
    }
  
  private void initBuilding() {
    scene            = new Node();
    scene            = (Node) assetManager.loadModel("Models/Building/Building.j3o");
    RigidBodyControl scenePhys = new RigidBodyControl(0f);
    scene.addControl(scenePhys);
    physics.getPhysicsSpace().add(scenePhys);
    rootNode.attachChild(scene);
    System.out.println("Building initialized: " + scene.getChildren());
    initPillarMaterials();
    initBenchMaterials();
    initWallMaterials();
    initWindowMaterials();
    initPlanterMaterials();
    initFloorMaterials();
    }
  
  private void initFloorMaterials(){
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.LightGray);
    scene.getChild("Floor").setMaterial(mat);
    }
  
  private void initPillarMaterials(){
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Brown);
    scene.getChild("PillarNode").setMaterial(mat);
    }

  private void initWallMaterials(){
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key = new TextureKey("Textures/Plank.png", false);
    Texture tex    = assetManager.loadTexture(key);
    mat.setTexture("ColorMap", tex);
    scene.getChild("WallNode").setMaterial(mat);
    }
  
  private void initWindowMaterials(){
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Brown);
    scene.getChild("WindowNode").setMaterial(mat);
    }

  private void initPlanterMaterials(){
    Material mat1   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    Material mat2   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    Material mat3   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    Material mat4   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key1 = new TextureKey("Textures/Plank.png", false);
    TextureKey key2 = new TextureKey("Textures/Dirt.png", false);
    TextureKey key3 = new TextureKey("Textures/Grass.jpg", false);
    TextureKey key4 = new TextureKey("Textures/Bark.jpg", false);
    Texture tex1    = assetManager.loadTexture(key1);
    Texture tex2    = assetManager.loadTexture(key2);
    Texture tex3    = assetManager.loadTexture(key3);
    Texture tex4    = assetManager.loadTexture(key4);
    mat1.setTexture("ColorMap", tex1);
    mat2.setTexture("ColorMap", tex2);
    mat3.setTexture("ColorMap", tex3);
    mat4.setTexture("ColorMap", tex4);
    
    Node planterNode = (Node) scene.getChild("PlanterNode");
    
    for (int i = 0; i < planterNode.getChildren().size(); i++) {
      Node currentPlant = (Node) planterNode.getChild(i);
      currentPlant.getChild(0).setMaterial(mat1);
      currentPlant.getChild(1).setMaterial(mat2);
      currentPlant.getChild(2).setMaterial(mat3);
      currentPlant.getChild(3).setMaterial(mat4);
      }
    }

  private void initBenchMaterials(){
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Brown);
    scene.getChild("BenchNode").setMaterial(mat);
    }
  
  }