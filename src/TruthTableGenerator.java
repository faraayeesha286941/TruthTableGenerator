import javax.swing.*;
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

            JTextArea resultArea = new JTextArea(10, 40);
            resultArea.setEditable(false);
            panel.add(resultArea);

            generateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String statement = variable1.getSelectedItem().toString() + operator.getSelectedItem().toString() + variable2.getSelectedItem().toString();
                    String truthTable = generateTruthTable(statement);
                    resultArea.setText(truthTable);
                }
            });

            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static String generateTruthTable(String statement) {
        // TODO: Implement the logic to generate the truth table and determine if the statement is a tautology, contingency, or contradiction.
        return "Truth table for statement: " + statement + "\n\nThis feature is not yet fully implemented.";
    }
}