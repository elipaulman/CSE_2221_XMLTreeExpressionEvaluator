import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author Elijah Paulman
 *
 */
public final class XMLTreeIntExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeIntExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static int evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";
        // Initializes integer return value for answer
        int answer = 0;

        // If exp has no children
        if (exp.numberOfChildren() == 0) {
            answer = Integer.parseInt(exp.attributeValue("value"));
            // If exp has children
        } else {
            // Gets integer value for left operand and right operand
            int opLeft = evaluate(exp.child(0));
            int opRight = evaluate(exp.child(1));

            // Performs given operation of right operand on left operand
            // multiplication
            if (exp.label().equals("times")) {
                answer = opLeft * opRight;
                // Division
            } else if (exp.label().equals("divide")) {
                // Checks for dividing int by 0
                if (opRight != 0) {
                    answer = opLeft / opRight;
                    // Can't divide by zero
                } else {
                    Reporter.fatalErrorToConsole(
                            "Error: Cannot divide by zero");
                }
                // Addition
            } else if (exp.label().equals("plus")) {
                answer = opLeft + opRight;
                // Subtraction
            } else if (exp.label().equals("minus")) {
                answer = opLeft - opRight;
            }
        }
        // Returns integer answer
        return answer;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
