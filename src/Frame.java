import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;
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
        setBounds(200, 100, 600, 500);
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
        add(sizePanel, BorderLayout.NORTH);

        sizePanel.add(new JLabel("Size of chains: "));
        sizePanel.add(new JLabel("from "));
        JTextField fromSize = new JTextField();
        sizePanel.add(fromSize);
        sizePanel.add(new JLabel(" to "));
        JTextField toSize = new JTextField();
        sizePanel.add(toSize);

        inputPane.add(new JLabel("Alphabet:"));
        JTextField language = new JTextField();
        language.getDocument().addDocumentListener(languageListener);
        inputPane.add(language);
        inputPane.add(new JLabel("Repeating character:"));
        JTextField repeatingCharacter = new JTextField();
        repeatingCharacter.getDocument().addDocumentListener(languageListener);
        inputPane.add(repeatingCharacter);
        inputPane.add(new JLabel("Multiplicity frequency of occurrence:"));
        JTextField multiplicity = new JTextField();
        multiplicity.getDocument().addDocumentListener(languageListener);
        inputPane.add(multiplicity);
        inputPane.add(new JLabel("Required part of the chain"));
        JTextField requiredPart = new JTextField();
        requiredPart.getDocument().addDocumentListener(languageListener);
        inputPane.add(requiredPart);
        JButton fromLanguage = new JButton("Generate grammar and chains");
        inputPane.add(fromLanguage);

        JTextArea expressionGrammar = new JTextArea();
        expressionPane.add(new JLabel("Expression grammar:"));
        expressionGrammar.getDocument().addDocumentListener(new DocumentListener() {
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
        expressionPane.add(expressionGrammar);
        JButton fromRegularExpression = new JButton("Generate chains");
        expressionPane.add(fromRegularExpression);

        fromLanguage.addActionListener(l -> {
            chainsPane.removeAll();
            try {
                if (editLanguage) {
                    fromLanguageGenerator = new FromLanguageGenerator(
                            Arrays.stream(language.getText().trim().split(" "))
                                    .map(c -> c.charAt(0))
                                    .collect(Collectors.toSet()),
                            repeatingCharacter.getText().trim().charAt(0),
                            Integer.parseInt(multiplicity.getText()),
                            requiredPart.getText());
                    editLanguage = false;
                }
                StringBuilder stringBuilder = new StringBuilder(fromLanguageGenerator.createRegularExpression());
                for(int i = 50; i < stringBuilder.length(); i += 50)
                    stringBuilder.insert(i, '\n');
                expressionGrammar.setText(stringBuilder.toString());
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
                    fromRegularExpressionGenerator = new FromRegularExpressionGenerator(expressionGrammar.getText());
                    editRegular = false;
                }
                fromRegularExpressionGenerator
                        .generateChains(Integer.parseInt(fromSize.getText()), Integer.parseInt(toSize.getText()))
                        .stream().sorted()
                        .forEach(s -> chainsPane.add(new Label(s)));
            } catch (Exception e){
                JLabel exceptionLabel = new JLabel(e.getMessage());
                exceptionLabel.setForeground(Color.RED);
                chainsPane.add(exceptionLabel);
                e.printStackTrace();
            }
            chainsPane.updateUI();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
