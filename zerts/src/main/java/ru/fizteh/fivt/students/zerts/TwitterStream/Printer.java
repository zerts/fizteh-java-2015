package ru.fizteh.fivt.students.zerts.TwitterStream;

public class Printer {
    static final int LINE_LENGTH = 170;
    public static void print(String message) {
        System.out.print(message);
    }
    public static void print(char message) {
        System.out.print(message);
    }
    public static void printTweet(String tweet) {
        System.out.print(tweet + printLine());
    }
    public static void printError(String message) {
        System.err.println(message);
    }
    public static String printLine() {
        StringBuilder result = new StringBuilder().append("\n");
        for (int i = 0; i < LINE_LENGTH; i++) {
            result.append("-");
        }
        result.append("\n");
        return result.toString();
    }
}
