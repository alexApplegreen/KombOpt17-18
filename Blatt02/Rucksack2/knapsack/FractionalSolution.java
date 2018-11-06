package knapsack;

/**
 * Solution of a fractional knapsack problem
 *
 * @author Stephan Beyer
 */
public class FractionalSolution extends GenericSolution<Double> {

	private double epsilon = 1e-6;

	public FractionalSolution(Instance instance) {
		super(instance);
	}

	/**
	 * Copy a solution (copy constructor)
	 */
	public FractionalSolution(FractionalSolution solution) {
		super(solution);
	}

	@Override
	public void set(int item, Double quantity) {
		assert sol.size() > item : "Item number " + item + " not found!";
		assert sol.get(item) != null : "Item " + item + " not initialized in solution.";
		Double value = new Double(instance.getValue(item));
		Double weight = new Double(instance.getWeight(item));
		this.solValue += value * quantity;
		this.solWeight += weight * quantity;
		this.sol.set(item, quantity);
	}

	public void unset(int item) {
		if (sol.get(item) == this.zero()) {
			throw new RuntimeException("cannot unset Value.");
		}
		this.solValue -= instance.getValue(item);
		this.solWeight -= instance.getWeight(item);
		this.sol.set(item, 0.0);
	}

	@Override
	public boolean isFeasible() {
		return solWeight <= instance.getCapacity();
	}

	@Override
	public boolean isBinary() {
		for (double quantity : sol) {
			if (quantity > epsilon
			 && quantity < 1 - epsilon) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Double zero() {
		return 0.0;
	}
}
