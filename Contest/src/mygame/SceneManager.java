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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import jme3tools.optimize.GeometryBatchFactory;

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
    
    //Initialize each of the nodes
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

    //Create nodes to be batched
    Node floorNode = (Node) scene.getChild("Floor");
    Node benchNode  = (Node) scene.getChild("BenchNode");
    Node planterNode  = (Node) scene.getChild("PlanterNode");
    
    //Batch the node into one node
    Node optGeom = new Node();
    optGeom.attachChild(floorNode);
    optGeom.attachChild(benchNode);
    optGeom.attachChild(planterNode);
    
    //Attach new batch to rootNode
    Node newGeom = GeometryBatchFactory.optimize(optGeom, false);
    rootNode.attachChild(newGeom);
    
    //Create a lawn and add to physics
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Green);
    Box box = new Box(150, .2f, 150);
    Geometry floor = new Geometry("the Floor", box);
    floor.setMaterial(mat1);
    floor.setLocalTranslation(0, -.5f, 0);
    RigidBodyControl floorPhys = new RigidBodyControl(0f);
    floor.addControl(floorPhys);
    scene.attachChild(floor);
    physics.getPhysicsSpace().add(floorPhys);
    }
  
  //Set the pillars to unshaded and wood
  private void initPillarMaterials(){
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Brown);
    scene.getChild("PillarNode").setMaterial(mat);
    }

   //Set the Walls to unshaded and Stone
  private void initWallMaterials(){
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key = new TextureKey("Textures/Plank.png", false);
    Texture tex    = assetManager.loadTexture(key);
    mat.setTexture("ColorMap", tex);
    scene.getChild("WallNode").setMaterial(mat);
    }
  
  //Set the Windows to unshaded and wood
  private void initWindowMaterials(){
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Brown);
    scene.getChild("WindowNode").setMaterial(mat);
    }

  //Sets the plants to unshaded and proper materials
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
    
    //Gets the planter node for iteration
    Node planterNode = (Node) scene.getChild("PlanterNode");
    
    //Iterates over each planter to set each material properly
    for (int i = 0; i < planterNode.getChildren().size(); i++) {
      Node currentPlant = (Node) planterNode.getChild(i);
      currentPlant.getChild(0).setMaterial(mat1);
      currentPlant.getChild(1).setMaterial(mat2);
      currentPlant.getChild(2).setMaterial(mat3);
      currentPlant.getChild(3).setMaterial(mat4);
      }
    }

  //Set the benches to unshaded and Brown Color
  private void initBenchMaterials(){
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Brown);
    scene.getChild("BenchNode").setMaterial(mat);
    }
  
  }
