package mygame;

import com.jme3.app.SimpleApplication;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
    //Disable fly camera and attach all appstates
    this.getFlyByCamera().setEnabled(false);
    this.setDisplayFps(false);
    this.setDisplayStatView(false);
    this.setShowSettings(false);
    this.inputManager.setCursorVisible(true);
    this.stateManager.attach(new SceneManager());
    this.stateManager.attach(new PlayerManager());
    this.stateManager.attach(new FireManager());
    this.stateManager.attach(new WaterManager());
    this.stateManager.attach(new CameraManager());
    this.stateManager.attach(new GuiManager());
    this.stateManager.attach(new InteractionManager());
    }
}
