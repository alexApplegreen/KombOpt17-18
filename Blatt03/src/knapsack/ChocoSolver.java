package knapsack;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.variables.IntVar;

public class ChocoSolver implements SolverInterface {

    public ChocoSolver() {
        super();
    }

    @Override
    public Solution solve(Instance instance) {
        Model model = new Model();
        int vMax = 0;

        for (int i = 0; i < instance.getSize(); i++) {
            vMax += instance.getValue(i);
        }

        IntVar energySum = model.intVar("energySum", 0, vMax);
        IntVar weightSum = model.intVar("weightSum", 0, instance.getCapacity());
        IntVar[] occ = model.intVarArray("occ", instance.getSize(), 0, 1);

        model.knapsack(occ, energySum, weightSum, instance.getValueArray(), instance.getWeightArray()).post();
        model.setObjective(Model.MAXIMIZE, energySum);

        Solver solver = model.getSolver();

        final Solution[] s = {null};
        solver.plugMonitor((IMonitorSolution) () -> {
            s[0] = new Solution(instance);
            for (int i = 0; i < instance.getSize(); i++) {
                s[0].set(i, occ[i].getValue());
            }
        });

        while (solver.solve());
        Logger.println("Weight: " + s[0].getWeight());
        return s[0];
    }
}