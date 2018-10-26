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
		// TODO add() enlarges arraylist size
		if (quantity > 0) {
			this.solValue += instance.getValue(item) * quantity;
			this.solWeight += instance.getWeight(item) * quantity;
		}
		else if (quantity == 0) {
			this.solValue -= instance.getValue(item);
			this.solWeight -= instance.getWeight(item);
		}
		this.sol.set(item, quantity);
	}

	/**
	 * Check if the solution is feasible.
	 */
	@Override
	public boolean isFeasible() {
		int weight = 0;
		for (int i = 0; i < instance.getSize(); i++) {
			weight += (instance.getWeight(i) * sol.get(i));
		}
		return weight <= instance.getCapacity();
	}

	// TODO move to Solver?
	/**
	 * method to calculate upper bound for this solution
	 * @return
	 */
	public double calculateUpperBound() {
		return 0.0;
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
