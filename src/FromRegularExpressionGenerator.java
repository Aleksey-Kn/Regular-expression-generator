import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FromRegularExpressionGenerator {
    private final String regular;
    private int maxSize;

    public FromRegularExpressionGenerator(String regular){
        this.regular = regular;
        if(regular.chars().filter(c -> c == '(').count() != regular.chars().filter(c -> c == ')').count())
            throw new IllegalArgumentException("Count '(' not equal count ')'");
    }

    public List<String> generateChains(int minLength, int maxLength){
        maxSize = maxLength;
        LinkedList<StringBuilder> results = new LinkedList<>();
        results.add(new StringBuilder());
        if(regular.charAt(0) == '('
                && regular.charAt(regular.length() - 1) == '*'
                && indexCloseFor(regular) == regular.length() - 2)
            results = manyStep(results, regular.substring(1, regular.length() - 2).split("\\+"));
        else {
            if(regular.charAt(0) == '(' && indexCloseFor(regular) == regular.length() - 1)
                results = oneStep(results, regular.substring(1, regular.length() - 1).split("\\+"));
            else
                results = oneStep(results, regular.split("\\+"));
        }
        return results.stream()
                .map(StringBuilder::toString)
                .filter(s -> s.length() <= maxLength)
                .filter(s -> s.length() >= minLength)
                .collect(Collectors.toList());
    }

    private LinkedList<StringBuilder> oneStep(LinkedList<StringBuilder> previousResult, String[] options){ // возвращает входные цепочки, на которые навешены варианты из входных options
        LinkedList<StringBuilder> tempResult;
        LinkedList<StringBuilder> newResult = new LinkedList<>();
        StringBuilder state;
        int ind;
        for(String nowOptions: options){
            tempResult = new LinkedList<>(previousResult);
            state = new StringBuilder(nowOptions);
            while (!state.isEmpty()){
                if(state.charAt(0) == '('){
                    ind = indexCloseFor(state.toString()); // индекс конца обрабатываемых скобок
                    String now = state.substring(0, ind);
                    if(state.charAt(ind + 1) == '*') {
                        tempResult = manyStep(tempResult, now.split("\\+"));
                        state.delete(0, ind + 2);
                    } else {
                        tempResult = oneStep(tempResult, now.split("\\+"));
                        state.delete(0, ind + 1);
                    }
                } else{ // когда нет ветвления
                    ind = state.indexOf("(");
                    if(ind == -1)
                        ind = state.length();
                    String now = state.substring(0, ind);
                    state.delete(0, ind);
                    tempResult.forEach(sb -> sb.append(now));
                }
            }
            newResult.addAll(tempResult);
        }
        return newResult;
    }

    private LinkedList<StringBuilder> manyStep(LinkedList<StringBuilder> previousResult, String[] options){
        LinkedList<StringBuilder> newResult = new LinkedList<>(previousResult);
        LinkedList<StringBuilder> tempResult;
        while (true){
            tempResult = oneStep(newResult, options);
            if(tempResult.stream().allMatch(e -> e.length() > maxSize))
                return newResult;
            newResult.addAll(tempResult);
        }
    }

    private int indexCloseFor(String nowRegular){
        int count = 1;
        char[] reg = nowRegular.toCharArray();
        for(int i = 1; i < nowRegular.length(); i++){
            if(reg[i] == '(')
                count++;
            else if(reg[i] == ')'){
                count--;
                if(count == 0)
                    return i;
            }
        }
        return -1;
    }
}
