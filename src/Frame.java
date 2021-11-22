import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Frame extends JFrame {
    private boolean editLanguage = true;
    private boolean editRegular = true;
    private FromRegularExpressionGenerator fromRegularExpressionGenerator;
    private FromLanguageGenerator fromLanguageGenerator;

    private Frame(){
        super("Regular expression generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 100, 650, 500);
        setLayout(new BorderLayout());

        DocumentListener languageListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                editLanguage = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                editLanguage = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                editLanguage = true;
            }
        };

        JPanel inputPane = new JPanel();
        inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.Y_AXIS));
        add(inputPane, BorderLayout.WEST);
        JPanel chainsPane = new JPanel();
        JScrollPane centerPane = new JScrollPane(chainsPane);
        chainsPane.setLayout(new BoxLayout(chainsPane, BoxLayout.Y_AXIS));
        add(centerPane, BorderLayout.CENTER);
        JPanel expressionPane = new JPanel();
        expressionPane.setLayout(new BoxLayout(expressionPane, BoxLayout.Y_AXIS));
        add(expressionPane, BorderLayout.EAST);
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
        add(sizePanel, BorderLayout.SOUTH);
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        add(menuPanel, BorderLayout.NORTH);

        JButton about = new JButton("About author");
        menuPanel.add(about);
        JButton theme = new JButton("Theme");
        menuPanel.add(theme);
        JButton open = new JButton("Open initial data");
        menuPanel.add(open);
        JButton saveExpression = new JButton("Save expression to file");
        menuPanel.add(saveExpression);
        JButton saveChains = new JButton("Save chains to file");
        menuPanel.add(saveChains);

        sizePanel.add(new JLabel("Size of chains: "));
        sizePanel.add(new JLabel("from "));
        JTextField fromSize = new JTextField();
        sizePanel.add(fromSize);
        sizePanel.add(new JLabel(" to "));
        JTextField toSize = new JTextField();
        sizePanel.add(toSize);

        inputPane.add(new JLabel("Alphabet:"));
        JTextField alphabet = new JTextField();
        alphabet.getDocument().addDocumentListener(languageListener);
        inputPane.add(alphabet);
        inputPane.add(new JLabel("Repeating character:"));
        JTextField repeatingCharacter = new JTextField();
        repeatingCharacter.getDocument().addDocumentListener(languageListener);
        inputPane.add(repeatingCharacter);
        inputPane.add(new JLabel("Multiplicity frequency of occurrence:"));
        JTextField multiplicity = new JTextField();
        multiplicity.getDocument().addDocumentListener(languageListener);
        inputPane.add(multiplicity);
        inputPane.add(new JLabel("Start part of the chain"));
        JTextField startPart = new JTextField();
        startPart.getDocument().addDocumentListener(languageListener);
        inputPane.add(startPart);
        inputPane.add(new JLabel("End part of the chain"));
        JTextField endPart = new JTextField();
        endPart.getDocument().addDocumentListener(languageListener);
        inputPane.add(endPart);
        JButton fromLanguage = new JButton("Generate grammar and chains");
        inputPane.add(fromLanguage);

        JTextArea regularExpression = new JTextArea();
        expressionPane.add(new JLabel("Regular expression:"));
        regularExpression.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                editRegular = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                editRegular = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                editRegular = true;
            }
        });
        expressionPane.add(regularExpression);
        JButton fromRegularExpression = new JButton("Generate chains");
        expressionPane.add(fromRegularExpression);

        fromLanguage.addActionListener(l -> {
            chainsPane.removeAll();
            try {
                if (editLanguage) {
                    fromLanguageGenerator = new FromLanguageGenerator(
                            Arrays.stream(alphabet.getText().trim().split(" "))
                                    .map(c -> c.charAt(0))
                                    .collect(Collectors.toSet()),
                            repeatingCharacter.getText().trim().charAt(0),
                            Integer.parseInt(multiplicity.getText()),
                            startPart.getText(), endPart.getText());
                    editLanguage = false;
                }
                StringBuilder stringBuilder = new StringBuilder(fromLanguageGenerator.createRegularExpression());
                for(int i = 50; i < stringBuilder.length(); i += 50)
                    stringBuilder.insert(i, '\n');
                regularExpression.setText(stringBuilder.toString());
                new TreeSet<>(fromLanguageGenerator
                        .createChains(Integer.parseInt(fromSize.getText()), Integer.parseInt(toSize.getText())))
                        .forEach(s -> chainsPane.add(new Label(s)));
            } catch (Exception e){
                JLabel exceptionLabel = new JLabel(e.getMessage());
                exceptionLabel.setForeground(Color.RED);
                chainsPane.add(exceptionLabel);
                e.printStackTrace();
            }
            chainsPane.updateUI();
        });

        fromRegularExpression.addActionListener(l -> {
            chainsPane.removeAll();
            try {
                if (editRegular) {
                    fromRegularExpressionGenerator = new FromRegularExpressionGenerator(regularExpression.getText());
                    editRegular = false;
                }
                new TreeSet<>(fromRegularExpressionGenerator
                        .generateChains(Integer.parseInt(fromSize.getText()), Integer.parseInt(toSize.getText())))
                        .forEach(s -> chainsPane.add(new Label(s)));
            } catch (Exception e){
                JLabel exceptionLabel = new JLabel(e.getMessage());
                exceptionLabel.setForeground(Color.RED);
                chainsPane.add(exceptionLabel);
                e.printStackTrace();
            }
            chainsPane.updateUI();
        });

        about.addActionListener(l ->
                JOptionPane.showMessageDialog(null, "Кондрачук Данил, ИП-816"));
        theme.addActionListener(l ->
                JOptionPane.showMessageDialog(null,
                        """
                                Написать программу, которая по предложенному описанию языка построит регулярное выражение,
                                задающее этот язык, и сгенерирует с его помощью все цепочки языка в заданном диапазоне длин.
                                Предусмотреть также возможность генерации цепочек по введённому пользователем РВ. Вариант задания языка:
                                Алфавит, заданные начальная и конечная подцепочки и кратность вхождения некоторого символа алфавита во все цепочки языка.
                                """));
        saveExpression.addActionListener(l -> {
            String name = JOptionPane.showInputDialog(null,
                    "Введите имя файла для сохранения",
                    "Сохранение регулярного выражения", JOptionPane.QUESTION_MESSAGE);
            try {
                FileWriter writer = new FileWriter(name + ".txt");
                writer.write(regularExpression.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        saveChains.addActionListener(l -> {
            String name = JOptionPane.showInputDialog(null,
                    "Введите имя файла для сохранения",
                    "Сохранение цепочек", JOptionPane.QUESTION_MESSAGE);
            if(name != null) {
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(name + ".txt"));
                    for (Component component : chainsPane.getComponents()) {
                        writer.println(((Label) component).getText());
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        open.addActionListener(l -> {
            String name = JOptionPane.showInputDialog(null,
                    "Введите имя файла",
                    "Чтение параметров языка из файла", JOptionPane.QUESTION_MESSAGE);
            if(name != null) {
                try {
                    Scanner scanner = new Scanner(new File(name + ".txt"));
                    alphabet.setText(scanner.nextLine());
                    repeatingCharacter.setText(scanner.nextLine());
                    multiplicity.setText(scanner.nextLine());
                    startPart.setText(scanner.nextLine());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
