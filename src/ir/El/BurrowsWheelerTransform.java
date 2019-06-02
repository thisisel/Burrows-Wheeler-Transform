package ir.El;


public class BurrowsWheelerTransform {
    private String inputStr;
    private StringBuilder encodedOutput = new StringBuilder();
    private StringBuilder decodedOutPut = new StringBuilder();

    public BurrowsWheelerTransform(String inputStr) {
        this.inputStr = inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public StringBuilder getEncodedOutput() {
        return encode();
    }

    public StringBuilder getDecodedOutPut() {
        return decodedOutPut;
    }

    private StringBuilder encode() {
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
}
