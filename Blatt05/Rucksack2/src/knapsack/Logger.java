package knapsack;

/**
 * A logger class for debug output
 *
 * @author Stephan Beyer
 */
public class Logger {
	private static boolean enabled = false;
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_RESET = "\u001B[0m";

	public static void enable() {
		enabled = true;
	}

	public static void disable() {
		enabled = false;
	}

	public static void print(Object obj) {
		if (enabled) {
			System.out.print(obj.toString());
		}
	}

	public static void println(Object obj) {
		if (enabled) {
			System.out.println(obj.toString());
		}
	}

	public static void println() {
		if (enabled) {
			System.out.println();
		}
	}

	public static void printlnColor(String s) {
		if (enabled) {
			System.out.println(ANSI_GREEN + s + ANSI_RESET);
		}
	}
}
