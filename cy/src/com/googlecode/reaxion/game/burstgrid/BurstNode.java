package com.googlecode.reaxion.game.burstgrid;

import java.util.ArrayList;

/** 
 * The superclass of a node in the BurstGrid [to be renamed, perhaps]. Each BurstNode has some number 
 * of other nodes it links to. Each of these nodes is either an HPBoost, Gauge Boost, or a new Ability.
 * New types of nodes may be added in the future
 * 
 * @author Cy Neita
 */

public class BurstNode
{
	private ArrayList<int[]> NodeList;
	public int id; //ID number of the node; used to find nodes in the tree
	public boolean activated; //checks to see if this node has been activated by the player
	
	public BurstNode(int idNo){
		id = idNo;
	}
	
	public ArrayList<int[]> getNodeList(){
		return NodeList;
	}
}