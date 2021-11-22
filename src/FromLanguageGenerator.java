import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FromLanguageGenerator {
    private final Set<Character> alphabet;
    private final char multiplicityCharacter;
    private final int multiplicity;
    private final String startPart, endPart;
    private int maxSize, minSize;

    public FromLanguageGenerator(Set<Character> alphabet, char multiplicityCharacter, int multiplicity,
                                 String startPart, String endPart) {
        this.alphabet = alphabet;
        this.multiplicityCharacter = multiplicityCharacter;
        this.multiplicity = multiplicity;
        this.startPart = startPart;
        this.endPart = endPart;

        if (!alphabet.contains(multiplicityCharacter))
            throw new IllegalArgumentException("Multiplicity character not contain in alphabet");
        if (!startPart.chars().allMatch(c -> alphabet.contains((char) c)))
            throw new IllegalArgumentException(
                    "Required part of the chain symbol contain symbols, with not contains in alphabet");
        if (multiplicity < 1)
            throw new IllegalArgumentException("Multiplicity number must me more 0");
    }

    public String createRegularExpression() {
        StringBuilder allWithoutMultiplicityBuilder = new StringBuilder("(");
        StringBuilder result = new StringBuilder();
        alphabet.forEach(c -> {
            if (c != multiplicityCharacter) {
                allWithoutMultiplicityBuilder.append(c).append('+');
            }
        });
        allWithoutMultiplicityBuilder.replace(allWithoutMultiplicityBuilder.length() - 1,
                allWithoutMultiplicityBuilder.length(), ")*");
        String allWithoutMultiplicity = allWithoutMultiplicityBuilder.toString();

        int excess = (int) (multiplicity -
                ((startPart.chars().filter(c -> c == multiplicityCharacter).count()
                        + endPart.chars().filter(c -> c == multiplicityCharacter).count())
                        % multiplicity));
        if (excess == 0)
            excess = multiplicity;

        StringBuilder repeatingMultiplicityBuilder = new StringBuilder("(");
        for (int i = 0; i < multiplicity; i++) {
            repeatingMultiplicityBuilder.append(allWithoutMultiplicity).append(multiplicityCharacter);
        }
        repeatingMultiplicityBuilder.append(allWithoutMultiplicity).append(")*");
        String repeatingMultiplicity = repeatingMultiplicityBuilder.toString();

        if (excess == multiplicity || multiplicity == 1)
            result.append(startPart)
                    .append('(')
                    .append(allWithoutMultiplicity)
                    .append('+')
                    .append(repeatingMultiplicity)
                    .append(')')
                    .append(endPart);
        else {
            result.append(startPart);
            for (int i = 0; i < excess; i++) {
                result.append(allWithoutMultiplicity);
                result.append(multiplicityCharacter);
            }
            result.append(allWithoutMultiplicity).append(repeatingMultiplicity).append(endPart);
        }
        return result.toString();
    }

    public List<String> createChains(int min, int max) {
        LinkedList<String> chains = new LinkedList<>();
        minSize = min;
        maxSize = max;
        chainsGenerator(startPart, chains);
        return chains;
    }

    private void chainsGenerator(String previous, List<String> results) {
        if (previous.length() <= maxSize) {
            if (previous.length() >= minSize
                    && previous.chars().filter(c -> c == multiplicityCharacter).count() % multiplicity == 0
                    && previous.lastIndexOf(endPart) == previous.length() - endPart.length()) {
                results.add(previous);
            }
            for (char c : alphabet) {
                chainsGenerator(previous + c, results);
            }
            chainsGenerator(previous + endPart, results);
        }
    }
}
