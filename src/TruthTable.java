import java.util.Stack;

public class TruthTable {
    private String expression;

    public TruthTable(String expression) {
        this.expression = expression;
    }

    private boolean applyOperator(boolean p, boolean q, char operator) {
        switch (operator) {
            case '∧':
                return p && q;
            case '∨':
                return p || q;
            case '⇒':
                return !p || q;
            case '⇔':
                return (p && q) || (!p && !q);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private boolean evaluateExpression(boolean p, boolean q) {
        Stack<Boolean> values = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == 'P') {
                values.push(p);
            } else if (ch == 'Q') {
                values.push(q);
            } else if (ch == '¬') {
                if (!values.empty()) {
                    values.push(!values.pop());
                }
            } else if (ch == '∧' || ch == '∨' || ch == '⇒' || ch == '⇔') {
                if (values.size() >= 2) {
                    boolean qValue = values.pop();
                    boolean pValue = values.pop();
                    values.push(applyOperator(pValue, qValue, ch));
                }
            }
        }

        return !values.empty() ? values.pop() : false;
    }

    public Object[][] computeTruthTable() {
        int numRows = 4;
        Object[][] tableData = new Object[numRows][3];

        for (int i = 0; i < numRows; i++) {
            boolean p = (i / 2) % 2 == 0;
            boolean q = i % 2 == 0;

            boolean result = evaluateExpression(p, q);

            tableData[i][0] = p;
            tableData[i][1] = q;
            tableData[i][2] = result;
        }

        return tableData;
    }

    public String getResult(Object[][] truthTable) {
        int trueCount = 0;
        int falseCount = 0;

        for (Object[] row : truthTable) {
            if ((boolean) row[truthTable[0].length - 1]) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        if (trueCount == truthTable.length) {
            return "Tautology";
        } else if (falseCount == truthTable.length) {
            return "Contradiction";
        } else {
            return "Contingency";
        }
    }
}