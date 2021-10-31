import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private Frame(){
        super("Regular expression generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 100, 600, 500);
        setLayout(new BorderLayout());

        JPanel inputPane = new JPanel();
        inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.Y_AXIS));
        add(inputPane, BorderLayout.WEST);
        JPanel chainsPane = new JPanel();
        JScrollPane centerPane = new JScrollPane(chainsPane);
        add(centerPane, BorderLayout.CENTER);
        JPanel expressionPane = new JPanel();
        add(expressionPane, BorderLayout.EAST);

        inputPane.add(new JLabel("Alphabet:"));
        JTextField language = new JTextField();
        inputPane.add(language);
        inputPane.add(new JLabel("Repeating character:"));
        JTextField repeatingCharacter = new JTextField();
        inputPane.add(repeatingCharacter);
        inputPane.add(new JLabel("Multiplicity frequency of occurrence:"));
        JTextField multiplicity = new JTextField();
        inputPane.add(multiplicity);
        inputPane.add(new JLabel("Required part of the chain"));
        JTextField requiredPart = new JTextField();
        inputPane.add(requiredPart);
        inputPane.add(new JLabel("Size of chains:"));
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
        sizePanel.add(new JLabel("from "));
        JTextField fromSize = new JTextField();
        sizePanel.add(fromSize);
        sizePanel.add(new JLabel(" to "));
        JTextField toSize = new JTextField();
        sizePanel.add(toSize);
        inputPane.add(sizePanel);
        JButton fromLanguage = new JButton("Generate grammar and chains");
        inputPane.add(fromLanguage);

        JTextArea expressionGrammar = new JTextArea();
        expressionPane.add(new JLabel("Expression grammar:"));
        expressionPane.add(expressionGrammar);
        JButton fromRegularExpression = new JButton("Generate chains");
        expressionPane.add(fromRegularExpression);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
