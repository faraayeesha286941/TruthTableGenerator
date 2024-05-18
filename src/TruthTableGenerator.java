import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TruthTableGenerator {
    private static final String[] VARIABLES = {"P", "Q", "R"};
    private static final String[] OPERATORS = {"&", "|", "->", "<->"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Truth Table Generator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("Enter the statement in infix notation:"));

            JPanel inputPanel = new JPanel(new FlowLayout());
            JComboBox<String> variable1 = new JComboBox<>(VARIABLES);
            JComboBox<String> operator = new JComboBox<>(OPERATORS);
            JComboBox<String> variable2 = new JComboBox<>(VARIABLES);
            inputPanel.add(variable1);
            inputPanel.add(operator);
            inputPanel.add(variable2);

            panel.add(inputPanel);

            JButton generateButton = new JButton("Generate");
            panel.add(generateButton);

            String[] columnNames = {VARIABLES[0], VARIABLES[1], VARIABLES[2], "Result"};

            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            JTable resultTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(resultTable);
            panel.add(scrollPane);

            JLabel resultLabel = new JLabel();
            panel.add(resultLabel);

            generateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String statement = variable1.getSelectedItem().toString() + operator.getSelectedItem().toString() + variable2.getSelectedItem().toString();
                    generateTruthTable(statement, tableModel, resultLabel);
                }
            });

            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void generateTruthTable(String statement, DefaultTableModel tableModel, JLabel resultLabel) {
        tableModel.setRowCount(0);

        String[] symbols = {statement.substring(0, 1), statement.substring(1, statement.length() - 1), statement.substring(statement.length() - 1)};
        boolean tautology = true;
        boolean contradiction = true;

        for (int p = 0; p <= 1; p++) {
            for (int q = 0; q <= 1; q++) {
                for (int r = 0; r <= 1; r++) {
                    boolean result = evaluateExpression(symbols, p, q, r);
                    tableModel.addRow(new Object[]{p, q, r, result});

                    tautology &= result;
                    contradiction &= !result;
                }
            }
        }

        if (tautology) {
            resultLabel.setText("The statement is a tautology.");
        } else if (contradiction) {
            resultLabel.setText("The statement is a contradiction.");
        } else {
            resultLabel.setText("The statement is a contingency.");
        }
    }

    private static boolean evaluateExpression(String[] symbols, int p, int q, int r) {
        boolean[] values = new boolean[]{p == 1, q == 1, r == 1};
        int index1 = indexOf(VARIABLES, symbols[0]);
        int index2 = indexOf(VARIABLES, symbols[2]);

        if (index1 == -1 || index2 == -1) {
            throw new IllegalArgumentException("Invalid variable in statement: " + String.join("", symbols));
        }

        boolean value1 = values[index1];
        boolean value2 = values[index2];

        switch (symbols[1]) {
            case "&":
                return value1 & value2;
            case "|":
                return value1 | value2;
            case "->":
                return !value1 | value2;
            case "<->":
                return value1 == value2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + symbols[1]);
        }
    }

    private static int indexOf(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }
}