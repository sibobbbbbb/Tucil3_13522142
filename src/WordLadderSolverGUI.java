import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordLadderSolverGUI extends JFrame implements ActionListener {

    private JTextField startWordField, endWordField;
    private JButton solveButton;
    private JTextArea outputArea;
    private JComboBox<String> algorithmComboBox;

    public static Set<String> readWordsFromFile(String filename) throws IOException {
        Set<String> words = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line.trim().toLowerCase());
        }
        reader.close();
        return words;
    }

    public WordLadderSolverGUI() {
        setTitle("Word Ladder Solver");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        JLabel startLabel = new JLabel("Kata Awal:");
        startLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        startWordField = new JTextField();
        startWordField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel endLabel = new JLabel("Kata Akhir:");
        endLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        endWordField = new JTextField();
        endWordField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel algoLabel = new JLabel("Pilih Algoritma:");
        algoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] algorithms = {"UCS", "A*", "GBFS"};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        inputPanel.add(startLabel);
        inputPanel.add(startWordField);
        inputPanel.add(endLabel);
        inputPanel.add(endWordField);
        inputPanel.add(algoLabel);
        inputPanel.add(algorithmComboBox);

        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve");
        solveButton.addActionListener(this);
        solveButton.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(solveButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 18));
        outputArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.setBackground(Color.LIGHT_GRAY);

        getContentPane().add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            String startWord = startWordField.getText().trim().toLowerCase();
            String endWord = endWordField.getText().trim().toLowerCase();
            String algorithm = (String) algorithmComboBox.getSelectedItem();

            try {
                Set<String> englishWords = readWordsFromFile("src\\words.txt");
                List<String> ladder = null;
                long startTime, endTime, waktu;
                int nodeCount;
                startTime = System.nanoTime();

                if (!englishWords.contains(startWord) || !englishWords.contains(endWord)) {
                    outputArea.setText("Kata yang anda masukan tidak ada di kamus.");
                    return;
                }
                else if (startWord.length() != endWord.length()) {
                    outputArea.setText("Kata awal dan kata akhir yang anda masukan tidak memiliki panjang yang sama.");
                    return;
                }

                switch (algorithm) {
                    case "UCS":
                        ladder = ucs.UCS(startWord, endWord, englishWords);
                        nodeCount = ucs.nodeCount;
                        break;
                    case "A*":
                        ladder = aStar.AStar(startWord, endWord, englishWords);
                        nodeCount = aStar.nodeCount;
                        break;
                    case "GBFS":
                        ladder = gbfs.GBFS(startWord, endWord, englishWords);
                        nodeCount = gbfs.nodeCount;
                        break;
                    default:
                        outputArea.setText("Pilihan algoritma tidak valid.");
                        return;
                }

                endTime = System.nanoTime();
                waktu = (endTime - startTime) / 1000000;

                if (ladder != null) {
                    outputArea.setText("Path dari " + startWord + " ke " + endWord + ":\n");
                    outputArea.append(String.join(" -> ", ladder) + "\n");
                    outputArea.append("\nTotal Node Yang Dikunjungi: " + nodeCount + "\n");
                    outputArea.append("\nWaktu Eksekusi: " + waktu + " milliseconds");
                } else {
                    outputArea.setText("Path tidak ditemukan dari " + startWord + " ke " + endWord + ".");
                }

            } catch (IOException ex) {
                outputArea.setText("Error reading words file: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WordLadderSolverGUI gui = new WordLadderSolverGUI();
            gui.setVisible(true);
        });
    }
}