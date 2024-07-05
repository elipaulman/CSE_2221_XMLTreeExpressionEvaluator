import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
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
public final class XMLTreeNNExpressionEvaluator2 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator2() {
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
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        // Initializes operand values
        NaturalNumber opLeft, opRight;

        // Checks if attribute of child(0) is a value
        if (!exp.child(0).hasAttribute("value")) {
            // If value is not found, calls evaluate on next child
            opLeft = evaluate(exp.child(0));
        } else {
            // Gets left operand if value is found
            opLeft = new NaturalNumber2(
                    Integer.parseInt(exp.child(0).attributeValue("value")));
        }

        // Checks if attribute of child(1) is a value
        if (!exp.child(1).hasAttribute("value")) {
            // If value is not found, calls evaluate on next child
            opRight = evaluate(exp.child(1));
        } else {
            // Gets right operand if value is found
            opRight = new NaturalNumber2(
                    Integer.parseInt(exp.child(1).attributeValue("value")));
        }

        // Gets operation to be performed as a string
        String operation = exp.label();
        // Performs given operation of right operand on left operand
        if (operation.equals("times")) {
            // Multiplication
            opLeft.multiply(opRight);
        } else if (operation.equals("divide")) {
            // Error for dividing by zero
            if (opRight.isZero()) {
                Reporter.fatalErrorToConsole("Error: Cannot divide by zero");
            }
            // Division
            opLeft.divide(opRight);
        } else if (operation.equals("plus")) {
            // Addition
            opLeft.add(opRight);
        } else if (operation.equals("minus")) {
            // Error for subtracting larger num from smaller (neg num)
            if (opRight.compareTo(opLeft) > 0) {
                Reporter.fatalErrorToConsole(
                        "Error: Cannot subtract bigger number");
            }
            // Subtraction
            opLeft.subtract(opRight);
        }
        /*
         * Creates and returns new natural number that is same as left operand
         * after all operations have been performed
         */
        NaturalNumber answer = new NaturalNumber2();
        answer.copyFrom(opLeft);
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
