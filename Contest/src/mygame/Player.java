/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class Player extends Node {
  public boolean isDead;
  public int     score;
  public BetterCharacterControl playerPhys;
  public Node model;
  }
