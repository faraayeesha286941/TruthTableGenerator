package funandgames;

import java.util.*;

public class ttb {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the logical expression (use variables A, B, C, ...):");
        String expression = scanner.nextLine().replace(" ", "");

        Set<Character> variables = extractVariables(expression);
        int numVariables = variables.size();
        List<Character> variableList = new ArrayList<>(variables);

        boolean[][] truthTable = generateTruthTable(numVariables);

        printHeader(variableList, expression);

        for (boolean[] row : truthTable) {
            printRow(row);
            Map<String, Boolean> intermediateResults = new HashMap<>();
            boolean result = evaluateExpression(expression, variableList, row, intermediateResults);
            printIntermediateResults(intermediateResults, expression);
            System.out.println(result ? " 1" : " 0");
        }
    }

    private static Set<Character> extractVariables(String expression) {
        Set<Character> variables = new HashSet<>();
        for (char ch : expression.toCharArray()) {
            if (Character.isLetter(ch)) {
                variables.add(ch);
            }
        }
        return variables;
    }

    private static boolean[][] generateTruthTable(int numVariables) {
        int numRows = (int) Math.pow(2, numVariables);
        boolean[][] table = new boolean[numRows][numVariables];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numVariables; j++) {
                table[i][j] = (i & (1 << (numVariables - j - 1))) != 0;
            }
        }

        return table;
    }

    private static void printHeader(List<Character> variableList, String expression) {
        for (char variable : variableList) {
            System.out.print(" " + variable + " ");
        }
        for (char ch : expression.toCharArray()) {
            if (!Character.isLetter(ch)) {
                System.out.print(" " + ch + " ");
            }
        }
        System.out.println(" Result");
    }

    private static void printRow(boolean[] row) {
        for (boolean b : row) {
            System.out.print(b ? " 1 " : " 0 ");
        }
    }

    private static boolean evaluateExpression(String expression, List<Character> variableList, boolean[] row, Map<String, Boolean> intermediateResults) {
        Map<Character, Boolean> variableValues = new HashMap<>();
        for (int i = 0; i < variableList.size(); i++) {
            variableValues.put(variableList.get(i), row[i]);
        }
        return evaluateParsedExpression(parseExpression(expression), variableValues, intermediateResults);
    }

    private static List<Object> parseExpression(String expression) {
        List<Object> output = new ArrayList<>();
        Stack<Character> operators = new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isLetter(ch)) {
                output.add(ch);
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.add(operators.pop());
                }
                operators.pop();
            } else {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    output.add(operators.pop());
                }
                operators.push(ch);
            }
        }
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }
        return output;
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '~': return 3;
            case '&': return 2;
            case '|': return 1;
            case '>': return 0;
            case '=': return -1;
            default: return -2;
        }
    }

    private static boolean evaluateParsedExpression(List<Object> parsedExpression, Map<Character, Boolean> variableValues, Map<String, Boolean> intermediateResults) {
        Stack<Boolean> stack = new Stack<>();
        for (Object token : parsedExpression) {
            if (token instanceof Character) {
                char ch = (Character) token;
                if (Character.isLetter(ch)) {
                    stack.push(variableValues.get(ch));
                } else {
                    boolean b1, b2;
                    String key;
                    switch (ch) {
                        case '~':
                            b1 = stack.pop();
                            stack.push(!b1);
                            key = "~" + b1;
                            intermediateResults.put(key, !b1);
                            break;
                        case '&':
                            b1 = stack.pop();
                            b2 = stack.pop();
                            stack.push(b1 && b2);
                            key = b2 + "&" + b1;
                            intermediateResults.put(key, b1 && b2);
                            break;
                        case '|':
                            b1 = stack.pop();
                            b2 = stack.pop();
                            stack.push(b1 || b2);
                            key = b2 + "|" + b1;
                            intermediateResults.put(key, b1 || b2);
                            break;
                        case '>':
                            b1 = stack.pop();
                            b2 = stack.pop();
                            stack.push(!b2 || b1);
                            key = b2 + ">" + b1;
                            intermediateResults.put(key, !b2 || b1);
                            break;
                        case '=':
                            b1 = stack.pop();
                            b2 = stack.pop();
                            stack.push(b1 == b2);
                            key = b2 + "=" + b1;
                            intermediateResults.put(key, b1 == b2);
                            break;
                    }
                }
            }
        }
        return stack.pop();
    }

    private static void printIntermediateResults(Map<String, Boolean> intermediateResults, String expression) {
        for (char ch : expression.toCharArray()) {
            if (!Character.isLetter(ch)) {
                String key = intermediateResults.keySet().stream().filter(k -> k.contains(Character.toString(ch))).findFirst().orElse(null);
                if (key != null) {
                    System.out.print(intermediateResults.get(key) ? " 1 " : " 0 ");
                }
            }
        }
    }
}
