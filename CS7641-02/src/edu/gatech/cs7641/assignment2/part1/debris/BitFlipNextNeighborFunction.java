package edu.gatech.cs7641.assignment2.part1.debris;

import opt.NeighborFunction;
import shared.Instance;

public class BitFlipNextNeighborFunction implements NeighborFunction {

	private Instance remembered;
	private int nextPosition;

	@Override
	public Instance neighbor(Instance d) {
		if (this.remembered==null||!this.remembered.equals(d)||this.nextPosition>=d.size()) this.nextPosition=0; 
		remembered=d;
		Instance neigh = (Instance) d.copy();
		neigh.getData().set(nextPosition, neigh.getData().get(nextPosition)==0?1:0);
		this.nextPosition++;
		return neigh;
	}

}
