package knapsack;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.variables.IntVar;

public class ChocoSolverDumb implements SolverInterface {

    public ChocoSolverDumb() {
        super();
    }

    @Override
    public Object solve(Instance instance) {
        Model model = new Model();
        int vMax = 0;
        int vBiggest = 0;
        int wBiggest = 0;

        for (int i = 0; i < instance.getSize(); i++) {
            vMax += instance.getValue(i);
            if (instance.getValue(i) > vBiggest) {
                vBiggest = instance.getValue(i);
            }
            if (instance.getWeight(i) > wBiggest) {
                wBiggest = instance.getWeight(i);
            }
        }

        IntVar weight = model.intVar(0, instance.getCapacity());
        IntVar energySum = model.intVar("energySum", 0, vMax);
        IntVar[] occ = model.intVarArray("occ", instance.getSize(), 0, 1);
        IntVar[] values = model.intVarArray("values", instance.getValueArray().length, 0, vBiggest);
        IntVar[] weights = model.intVarArray("weights", instance.getWeightArray().length, 0, wBiggest);

        model.arithm(weight, "<=", instance.getCapacity()).post();


        for (int i = 0; i < instance.getSize(); i++) {
            values[i] = model.intVar(instance.getValue(i));
            weights[i] = model.intVar(instance.getWeight(i));
        }

        for (int i = 0; i < instance.getSize(); i++) {
            model.ifThen(
                    model.arithm(weight, "<", instance.getCapacity()),
                    model.arithm(occ[i], "=", 1)
            );
        }

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
        Logger.println("weight: " + s[0].getWeight());
        return s[0];
    }
}
