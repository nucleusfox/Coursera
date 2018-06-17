/******************************************************************************
 *  Class: TuringMachine
 *  Author: nucleusfox
 *
 *  Turing Machine
 *  As an argument accepts the filepath to configuration, by default is
 *  configured for decrement. As standard input accepts strings of bits and
 *  prints the result of calculation.
 *
 *  Used the code from lecture 'A simple model of computation' of course
 *  'Computer Science: Algorithms, Theory, and Machines' on Coursera.
 *  https://www.coursera.org/learn/cs-algorithms-theory-machines/lecture/cuK6f/a-simple-model-of-computation
 *
 *  Example:
 *  $ java TuringMachine
 *  010000
 *
 *  Result:
 *  001111#
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Scanner;

public class TuringMachine {
    private Stack<Character> left;
    private Stack<Character> right;
    private int state;
    private int start;
    private char[] action;
    private Map<Character, Integer>[] next;
    private Map<Character, Character>[] out;

    public TuringMachine(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line = br.readLine();
        String[] tokens = line.split("\\s+");
        start = Integer.parseInt(tokens[2]);

        line = br.readLine();
        Stack<String> a = new Stack<>();
        while(!line.isEmpty()) {
            a.push(line);
            line = br.readLine();
        }
        action = new char[a.size()];
        for (int i = a.size()-1; i >= 0; i--) action[i] = a.pop().charAt(0);

        line = br.readLine();
        while(!line.isEmpty()) {
            a.push(line);
            line = br.readLine();
        }
        next = new Map[a.size()];
        for (int i = a.size()-1; i >= 0; i--) {
            next[i] = new HashMap<>();
            String[] nextToken = a.pop().split("\\s+");
            next[i].put('0', Integer.valueOf(nextToken[0]));
            next[i].put('1', Integer.valueOf(nextToken[1]));
            next[i].put('#', Integer.valueOf(nextToken[2]));
        }

        line = br.readLine();
        while(line != null && !line.isEmpty()) {
            a.push(line);
            line = br.readLine();
        }
        out = new Map[a.size()];
        for (int i = a.size()-1; i >= 0; i--) {
            out[i] = new HashMap<>();
            String[] nextToken = a.pop().split("\\s+");
            out[i].put('0', nextToken[0].charAt(0));
            out[i].put('1', nextToken[1].charAt(0));
            out[i].put('#', nextToken[2].charAt(0));
        }

        left = new Stack<>();
        right = new Stack<>();
    }

    public char simulate(String input) {
        state = start;
        for (int i = input.length()-1; i >= 0; i--)
            right.push(input.charAt(i));
        while (action[state] != 'H') {
            char c = read();
            write(out[state].get(c));
            state = next[state].get(c);
            if (action[state] == 'R') moveRight();
            if (action[state] == 'L') moveLeft();
        }
        return action[state];
    }

    private char read() {
        if (right.isEmpty()) return '#';
        else return right.pop();
    }

    private void write(char c) {
        right.push(c);
    }

    private void moveRight() {
        if (right.isEmpty())    left.push('#');
        else                    left.push(right.pop());
    }

    private void moveLeft() {
        if (left.isEmpty())     right.push('#');
        else                    right.push(left.pop());
    }

    public void printConfiguration() {
        System.out.println("action[]: " + Arrays.toString(action));
        System.out.println("next[]: " + Arrays.toString(next));
        System.out.println("out[]: " + Arrays.toString(out));
    }

    public void printStacks() {
        System.out.println("right: " + right);
        System.out.println("left: " + left);
    }

    public void printOutput() {
        for (int i = 0; i < left.size(); i++) System.out.print(left.get(i));
        while (right.size() > 0) System.out.print(right.pop());
        System.out.println();
    }

    public static void main(String[] args) {
        String filename = "TuringMachine/DecrementorConf.txt";
        if (args != null && args.length > 0) {
            filename = args[0];
        }

        try {
            TuringMachine tm = new TuringMachine(filename);

            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            while (!input.isEmpty()) {
                tm.simulate(input);
                tm.printOutput();
                input = s.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
