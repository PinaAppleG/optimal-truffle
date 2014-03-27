package edu.gatech.cs7641.assignment2.part1.debris;

import java.util.Random;

import opt.NeighborFunction;
import shared.Instance;

public class RandomBitFlipNeighborFunction implements NeighborFunction {

	Random random;
	public RandomBitFlipNeighborFunction(Random random) {
		this.random=random;
	}
	
	@Override
	public Instance neighbor(Instance d) {
		Instance neigh = (Instance) d.copy();
		int i=this.random.nextInt(neigh.size());
		neigh.getData().set(i, neigh.getData().get(i)==0?1:0);
		return neigh;
	}

}
