package knapsack;

/**
 * Solution of a integer or binary knapsack problem
 *
 * @author Stephan Beyer
 */
public class Solution extends GenericSolution<Integer> {
	public Solution(Instance instance) {
		super(instance);
	}

	/**
	 * Copy a solution (copy constructor)
	 */
	public Solution(Solution solution) {
		super(solution);
	}

	/**
	 * Assign a quantity to an item.
	 *
	 * @param item index of the item
	 * @param quantity quantity to be assigned
	 */
	@Override
	public void set(int item, Integer quantity) {
		assert sol.size() > item : "Item number " + item + " not found!";
		assert sol.get(item) != null : "Item " + item + " not initialized in solution.";
		// TODO: update solValue, solWeight, sol[item]
		// Note: values are visible in this class.
	}

	/**
	 * Check if the solution is feasible.
	 */
	@Override
	public boolean isFeasible() {
		// TODO: implement this method (and you may then use it in your Solvers)
		// NOTE: check if the current weight is higher than supported weight
		int weight = 0;
		for (int i = 0; i < instance.getWeightArray().length(), i++) {
			weight += instance.getWeight[i];
		}
		return weight <= instance.getCapacity();
	}

	/**
	 * Check if the solution is a binary solution
	 */
	@Override
	public boolean isBinary() {
		for (int quantity : sol) {
			if (quantity != 0 && quantity != 1) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Integer zero() {
		return 0;
	}
}
