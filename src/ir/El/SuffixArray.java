package ir.El;

public class SuffixArray {

    private String string;
    private int[] suffixArray;
    private int[] classEquivalence;


    public SuffixArray(String string) {
        this.string = string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int[] getSuffixArray() {
        suffixArray = buildSuffixArray(string);
        return suffixArray;
    }

    public int[] getClassEquivalence() {

        return computeCharClass(string, sortCharIndex(string));
    }

    /**
     * although this method is not recursive, it could be viewed as one.
     * since we are working with strings, the basic case is about each character.
     * we initialize the process by sorting and creating the class equivalence for
     * each single cyclic shift(at this phase they are letters). using the order[]
     * and classEquivalence that we acquired for single cyclic shifts as the budding block
     * for the greater cyclic shifts, we enter the while loop and extend them.
     * length of the cyclic shift can not be greater than length of the string.
     * since we're making the new cyclic shifts with pairing the previous ones of l = i;
     * after each creation length must be doubled therefore O(logn) times through the loop.
     *
     * @param str input string
     * @return ordered suffix indexes
     */
    private int[] buildSuffixArray(String str) {
        if (str.equals(null))
            System.out.println("null input");

        int[] order = sortCharIndex(str);
        int[] classEquivalence = computeCharClass(str, order);

        int length = 1;
        while (length < str.length()) {
            order = sortDoubledCyclicShift(str, length, order, classEquivalence);
            classEquivalence = updateClasses(order, classEquivalence, length);
            length *= 2;
        }
        return order;
    }

    /**
     * cyclic shift of length 1
     * starting point of making the suffixes. the base case og the algorithm.
     * based on counting sort for small input
     *
     * @param str input string
     * @return ordered input indices.
     */
    private int[] sortCharIndex(String str) {
        /*
         * order: the indexes of sorted characters of the string.
         * sorted indexes wil be used for class computation
         * count: number of times each of 26 english characters(256 maped to ASCII) appear in the string.
         * count has 256 indexes starting from 0 to 255.
         */
        int[] order = new int[str.length()];
        int[] count = new int[256];//ASCII

        if (str == null)
            return null;

        /*
         * initializing with 0 to avoid null
         */
        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
        }

        /*
         * Each alphabet has its own uniq place in count[].
         * By traversing through the input string we increase the default value of
         * occurrences in the count[].
         */
        for (int i = 0; i < str.length(); i++) {
            count[str.charAt(i)]++;
        }
        /*
         * computing the number of occurrences up to j(inclusive) as sumCount.
         * no index is available before count[0], thus nothing is added to count[0].
         */
        for (int j = 1; j <= 255; j++) {
            count[j] += count[j - 1];
        }
        /*
         * iterating in descending manner to preserve the stability of the algorithm
         * count[c]=n indicates that c is the nth character in the string and there are n-1
         * characters prior to that in the string. since indices begin at 0, minus 1 is required
         */
        for (int i = str.length() - 1; i >= 0; i--) {
            order[count[str.charAt(i)] - 1] = i;
            count[str.charAt(i)]--;
        }

        return order;
    }

    /**
     * computing equivalence classes just for L=1 cyclic shift
     * O(str.length()). 1 loop str.length() times.
     *
     * @param str   input string
     * @param order attained from sortCharIndex(String str)
     * @return an array of class equivalences classEquivalence
     */
    private int[] computeCharClass(String str, int[] order) {

        // char[] cyclicShift = new char[str.length()] = str
        int[] classEquivalence = new int[str.length() + 1];//last index reserved for maxClass

        /*
         * order[0] is the position in which the smallest char occurs
         * and we initialize this char with class 0
         */
        classEquivalence[order[0]] = 0;
        int maxClass = 0;

        /*
         * classEquivalence is equal to the original string
         * only this time each alphabet has its distinct integer value.
         * order is an auxiliary array. because same characters are cramped together,
         * by iterating through order we can keep track of distinct integers
         * by comparing the present characters with the previous one.
         * in the end, all the indexes of classEquivalence are filled except for the last one.
         */
        for (int i = 1; i < str.length(); i++) {

            if (str.charAt(order[i]) != str.charAt(order[i - 1]))
                classEquivalence[order[i]] = classEquivalence[order[i - 1]] + 1;
            else

                classEquivalence[order[i]] = classEquivalence[order[i - 1]];
            maxClass = classEquivalence[order[i]];
        }

        classEquivalence[str.length()] = maxClass;

        return classEquivalence;
    }


    /**
     * this method is heavily based on counting sort but
     * instead of sorting the letters, the equivalent classes sorted.
     *
     * @param str              input string
     * @param length           length of the current cyclic shift
     * @param order            order of the current cyclic shifts
     * @param classEquivalence
     */
    private int[] sortDoubledCyclicShift(String str, int length, int[] order, int[] classEquivalence) {
        int[] count = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            count[i] = 0;
        }

        int[] newOrder = new int[str.length()];

        for (int i = 0; i < str.length(); i++) {
            count[classEquivalence[i]]++;
        }
        for (int j = 1; j < str.length(); j++) {
            count[j] += count[j - 1];
        }

        int start;
        int cl;
        for (int i = str.length() - 1; i >= 0; i--) {
            start = (order[i] - length + str.length()) % str.length();
            cl = classEquivalence[start];
            count[cl]--;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    /**
     * @param newOrder
     * @param classEquivalence
     * @param length
     * @return
     */
    private int[] updateClasses(int[] newOrder, int[] classEquivalence, int length) {
        int n = newOrder.length;
        int[] newClass = new int[n];//equal to the length of string and classEquivalence

        /*
         * initializing the class of the  least significant pair
         * that originally came at the end of the string
         */
        newClass[newOrder[0]] = 0;

        int currentFirstHalf;
        int previousFirstHalf;
        int currentSecondHalf;
        int previousSecondHalf;
        for (int i = 1; i < n; i++) {
            currentFirstHalf = newOrder[i];
            previousFirstHalf = newOrder[i - 1];
            currentSecondHalf = currentFirstHalf + length;
            previousSecondHalf = (previousFirstHalf + length) % n;

            if ((classEquivalence[currentFirstHalf] != classEquivalence[previousFirstHalf]) ||
                    (classEquivalence[currentSecondHalf] != classEquivalence[previousSecondHalf])) {
                newClass[currentFirstHalf] = newClass[previousFirstHalf] + 1;
            } else
                newClass[currentFirstHalf] = newClass[previousFirstHalf];
        }
        return newClass;
    }
}
