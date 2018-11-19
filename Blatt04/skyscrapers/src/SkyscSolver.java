import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import java.io.IOException;
import java.util.List;

/**
 * A skyscrapers solver.
 *
 * @author
 */
public class SkyscSolver {

    public static void main(String args[]) throws IOException {
        if(args != null && args.length > 0) {
            System.out.println("Read file "+ args[0] + ".");
            Instance instance = Reader.readSkyscInstance(args[0]);
            System.out.println("Gamefield:");
            instance.printGamefield();
            long start = System.currentTimeMillis();
            SkyscSolver.solve(instance);
            long end = System.currentTimeMillis();
            System.out.printf("time = %.3fs\n", (end - start) / 1000.0);
        } else {
            System.out.println("Please enter a skyscrapers file.");
        }
    }

    public static void solve(Instance instance) {
        // 1. create model
        Model model = new Model();

        // 2. create variables
        int k = 0;
        int[] gamefield_singleDim = new int[(int) Math.pow(instance.getGamefieldSize(), 2.0)];

        for (int i = 0; i < instance.getGamefieldSize(); i++) {
            for (int j = 0; j < instance.getGamefieldSize(); j++) {
                gamefield_singleDim[k] = instance.getGamefield()[i][j];
                k++;
            }
        }

        assert (gamefield_singleDim.length == ((int)Math.pow(instance.getGamefieldSize(), 2.0))) : "length differs";

        IntVar x[][] = model.intVarMatrix(instance.getGamefieldSize(),
                                          instance.getGamefieldSize(),
                                          gamefield_singleDim);

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                x[i][j] = model.intVar(0, instance.getGamefieldSize());
            }
        }

        IntVar north[] = model.intVarArray(instance.getNorth().length, 0, instance.getGamefieldSize());
        IntVar east[] = model.intVarArray(instance.getEast().length, 0, instance.getGamefieldSize());
        IntVar south[] = model.intVarArray(instance.getSouth().length, 0, instance.getGamefieldSize());
        IntVar west[] = model.intVarArray(instance.getWest().length, 0, instance.getGamefieldSize());

        IntVar domain = model.intVar(0, instance.getGamefieldSize());

        IntVar visible[] = model.intVarArray(instance.getGamefieldSize(), 0, instance.getGamefieldSize());

        // 3. add constraints
        for (int i = 0; i < instance.getGamefieldSize(); i++) {
            model.allDifferent(x[i]).post();
            for (int j = 0; j < instance.getGamefieldSize(); j++) {
                model.allDifferent(x[j]).post();
                model.arithm(x[i][j], "<=", instance.getGamefieldSize()).post();
                model.arithm(x[i][j], ">=", 0).post();
            }
        }



        // 4. get solver and solve model
        Solver solver = model.getSolver();
        //List<Solution> solutions = solver.findAllSolutions();
        if (solver.solve()) {
            System.out.println("found solution!");
        }
        else {
            System.out.println("no solution found");
        }

        // 5. print solutions
        int size = instance.getGamefieldSize();
        //System.out.println("Number of solutions: " + solutions.size());
        int cnt = 1;
        /*for (Solution solution : solutions) {
            int[][] solutionArray = new int[size][size];
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    solutionArray[i][j] = solution.getIntVal(x[i][j]);
                }
            }
            instance.setSolution(solutionArray);
            System.out.println("------- solution number " + cnt + "-------");
            instance.printSolution();

            ++cnt;
        }*/
    }
}
