import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class TruthTableGUI {
    private JFrame frame;
    private JRadioButton notP, notQ;
    private JRadioButton and, or, implies, equivalence;
    private JButton confirmButton;
    private JTable truthTable;
    private JTextField result;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TruthTableGUI window = new TruthTableGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TruthTableGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        notP = new JRadioButton("¬P");
        c.gridx = 0;
        c.gridy = 0;
        frame.getContentPane().add(notP, c);

        notQ = new JRadioButton("¬Q");
        c.gridx = 1;
        c.gridy = 0;
        frame.getContentPane().add(notQ, c);

        ButtonGroup group1 = new ButtonGroup();
        and = new JRadioButton("AND");
        or = new JRadioButton("OR");
        implies = new JRadioButton("IMPLIES");
        equivalence = new JRadioButton("EQUIVALENCE");

        group1.add(and);
        group1.add(or);
        group1.add(implies);
        group1.add(equivalence);

        JPanel panel1 = new JPanel();
        panel1.add(and);
        panel1.add(or);
        panel1.add(implies);
        panel1.add(equivalence);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        frame.getContentPane().add(panel1, c);

        // Add this ItemListener to call updateTruthTable() when a radio button is selected or deselected
        ItemListener radioButtonListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTruthTable();
            }
        };

        // Add the radioButtonListener to all radio buttons
        notP.addItemListener(radioButtonListener);
        notQ.addItemListener(radioButtonListener);
        and.addItemListener(radioButtonListener);
        or.addItemListener(radioButtonListener);
        implies.addItemListener(radioButtonListener);
        equivalence.addItemListener(radioButtonListener);

        confirmButton = new JButton("Confirm");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        frame.getContentPane().add(confirmButton, c);

        confirmButton.addActionListener(e -> updateTruthTable());

        truthTable = new JTable();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridheight = 2;
        frame.getContentPane().add(new JScrollPane(truthTable), c);

        result = new JTextField();
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 2;
        frame.getContentPane().add(result, c);
    }

    private void updateTruthTable() {
        boolean isNotP = notP.isSelected();
        boolean isNotQ = notQ.isSelected();
        String operator = and.isSelected() ? "∧" : or.isSelected() ? "∨" : implies.isSelected() ? "⇒" : equivalence.isSelected() ? "⇔" : "∧";

        String expression = (isNotP ? "¬" : "") + "P" + operator + (isNotQ ? "¬" : "") + "Q";
        TruthTable truthTableComputation = new TruthTable(expression);
        Object[][] tableData = truthTableComputation.computeTruthTable();

        DefaultTableModel model = (DefaultTableModel) truthTable.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);

        String[] variableNames = expression.replaceAll("[^PQR]", "").split("");
        for (String variableName : variableNames) {
            model.addColumn(variableName);
        }
        model.addColumn("Result");

        for (Object[] row : tableData) {
            model.addRow(row);
        }

        result.setText(truthTableComputation.getResult(tableData));
    }

}
