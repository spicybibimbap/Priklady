import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Generátor příkladů na odečítání");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel labelNumExamples = new JLabel("Počet příkladů:");
        JTextField textNumExamples = new JTextField();

        JLabel labelMaxValue = new JLabel("Horní mez (max):");
        JTextField textMaxValue = new JTextField();

        JLabel labelFileName = new JLabel("Název souboru:");
        JTextField textFileName = new JTextField();

        JTextArea textAreaResults = new JTextArea(10, 30);
        textAreaResults.setEditable(false);

        JButton generateButton = new JButton("Generovat");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numExamples;
                int maxValue;
                String fileName;

                try {
                    numExamples = Integer.parseInt(textNumExamples.getText());
                    maxValue = Integer.parseInt(textMaxValue.getText());
                    fileName = textFileName.getText();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Zadejte platné číselné hodnoty.", "Chyba", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (fileName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Zadejte název souboru.", "Chyba", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Example> examples = generateExamples(numExamples, maxValue);
                displayExamples(examples, textAreaResults);
                saveExamplesToJson(examples, fileName);
            }
        });

        panel.add(labelNumExamples);
        panel.add(textNumExamples);
        panel.add(labelMaxValue);
        panel.add(textMaxValue);
        panel.add(labelFileName);
        panel.add(textFileName);
        panel.add(generateButton);
        panel.add(new JScrollPane(textAreaResults));

        frame.add(panel);
        frame.setVisible(true);
    }

    private static List<Example> generateExamples(int numExamples, int maxValue) {
        Random random = new Random();
        List<Example> examples = new ArrayList<>();

        for (int i = 0; i < numExamples; i++) {
            int a, b;
            do {
                a = random.nextInt(maxValue) + 1;
                b = random.nextInt(maxValue) + 1;
            } while (a <= b);

            examples.add(new Example(a, b));
        }

        return examples;
    }

    private static void displayExamples(List<Example> examples, JTextArea textArea) {
        StringBuilder sb = new StringBuilder();

        for (Example example : examples) {
            sb.append(example.a).append(" - ").append(example.b).append(" = ").append(example.a - example.b).append("\n");
        }

        textArea.setText(sb.toString());
    }

    private static void saveExamplesToJson(List<Example> examples, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(examples);

        try (FileWriter writer = new FileWriter(fileName + ".json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}