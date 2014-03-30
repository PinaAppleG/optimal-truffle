optimal-truffle
===============

## CS7641 Assignment 2

### Instructions

All source code is located in CS7641-02/src.  CS7641-02 includes an eclipse project, and is dependent on the ABAGAIL project.  The easiest way to run this code is to import this git repository [nathanielmeyer/optimal-truffle](https://github.com/nathanielmeyer/optimal-truffle.git) and the ABAGAIL repository [pushkar/ABAGAIL](https://github.com/pushkar/ABAGAIL.git) and then import each of them into your eclipse workspace.  

#### Project organization

All runnable classes are in edu.gatech.cs7641.assignment2.part1 and edu.gatech.cs7641.assignment2.part2.  Classes supporting the final product are in subpackages titled support, and classes developed but not used are in subpackges titled debris.

#### Running the main classes.

1. Open Eclipse, and if you have not already done so, import the ABAGAIL and CS7641-02 projects.
2. In the part1 package, runnable experiments include:
  * TrainNNWeightsWithRHC
  * TrainNNWeightsWithSA
  * TrainNNWeightsWithGA
  * TrainNNWeightsWithHybridGARHC
  * OptimizationProblems
3. In the part2 package, runnable experiments include:
  * ICAProjector

Note that the current implementation of ICAProjector produces one projection, but has not been seen to terminate while producting the second projection.  There is no need to run it as the original and projected datasets are included here.  The failure to project the second dataset is discussed in the analysis.

#### Supporting files

The datasets used are all contained in the datasets.  Documents and spreadsheets containing draft work, intermediate results, and graphics used in the final analysis are in CS7641-02.

#### Author's Note

This project is less polished than I'd like it to be before submitting.  Please consider splitting it into two projects with two deadlines for future sections.  Balancing multiple classes is the students job, and I did poorly at it this past month.  However, once I realized I needed to devote more time to this individual project and less to my SDP group project, I was too far behind to finish parts one and two satisfactorily.  The distant deadline early in the assignment made it easy to believe I could shift gears later, and I wasn't sufficiently skilled at this to make good on that assumption.
