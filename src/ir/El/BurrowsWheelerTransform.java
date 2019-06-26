package ir.El;


public class BurrowsWheelerTransform {
    private String inputStr;

    public BurrowsWheelerTransform(){}

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public StringBuilder getEncodedOutput() {
        return encode();
    }

    public char[] getDecodedOutPut() {
        return decode(inputStr);
    }

    private StringBuilder encode() {
        StringBuilder encodedOutput = new StringBuilder();

        if (inputStr.equals(null)) {
            System.out.println("input is null");
        }

        int[] suffixArray = new SuffixArray(inputStr).getSuffixArray();
        for (int i = 0; i < inputStr.length(); i++) {
            if (suffixArray[i] > 0)
                encodedOutput.append(inputStr.charAt(suffixArray[i] - 1));
            if (suffixArray[i] == 0)
                encodedOutput.append("$");
        }
        return encodedOutput;
    }

    private char[] decode(String encodedStr) {
        int[] classEquivalence = new SuffixArray(encodedStr).getClassEquivalence();//last column contains class eq
        int strLength = encodedStr.length();
        int distinctLetters = classEquivalence[strLength] + 1;//distinctLetters =< strLength
        char[] decodedStr = new char[strLength];

        int[] count = new int[distinctLetters]; //k
        int[] firstOccurrence = new int[distinctLetters];//M
        int[] rank = new int[strLength];//C

        for (int i = 0; i < distinctLetters; i++) {
            count[i] = 0;
        }

        int eos = 0;
        for (int i = 0; i < strLength; i++) {
            rank[i] = count[classEquivalence[i]];
            if (classEquivalence[i] == 0)
                eos = i;//key to the original row
            count[classEquivalence[i]]++;
        }

        int start = 0;
        for (int i = 0; i < distinctLetters; i++) {
            firstOccurrence[i] = start;
            start += count[i];
        }

        int pointer = eos;
        for (int j = strLength - 1; j >= 0; j--) {
            decodedStr[j] = encodedStr.charAt(pointer);
            pointer = rank[pointer] + firstOccurrence[classEquivalence[pointer]];
        }

        return decodedStr;
    }
}
