package edu.gatech.cs7641.assignment2.part1;

import opt.HillClimbingProblem;
import opt.OptimizationAlgorithm;
import shared.Instance;

public class HillClimbingAlgorithm extends OptimizationAlgorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6190575179210166690L;
	private Instance cur;
	private double curVal;

	public HillClimbingAlgorithm(HillClimbingProblem problem, Instance start) {
		super(problem);
		this.cur=start;
		this.curVal=problem.value(start);
	}

	@Override
	public double train() {
        HillClimbingProblem hcp = (HillClimbingProblem) getOptimizationProblem();
        Instance neigh = hcp.neighbor(cur);
        double neighVal = hcp.value(neigh);
        if (neighVal > curVal) {
            curVal = neighVal;
            cur = neigh;
        }
        return curVal;
	}

	@Override
	public Instance getOptimal() {
        return cur;
	}

}
