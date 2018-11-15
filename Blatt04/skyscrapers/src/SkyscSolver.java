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

        // 2. create varaibles
        int k = 0;
        int[] gamefield_singleDim = new int[instance.getGamefieldSize()];
        for (int i = 0; i < (int)Math.sqrt(instance.getGamefieldSize()); i++) {
            for (int j = 0; j < (int)Math.sqrt(instance.getGamefieldSize()); j++) {
                gamefield_singleDim[k] = instance.getGamefield()[i][j];
            }
        }
        IntVar x[][] = model.intVarMatrix((int)Math.sqrt(instance.getGamefieldSize()),
                                          (int)Math.sqrt(instance.getGamefieldSize()),
                                          gamefield_singleDim);

        // 3. add constraints

        // 4. get solver and solve model
        List<Solution> solutions = null;

        // 5. print solutions
        int size = instance.getGamefieldSize();
        System.out.println("Number of solutions: " + solutions.size());
        int cnt = 1;
        for (Solution solution : solutions) {
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
        }
    }
}
