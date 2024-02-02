// Owen Shen
// A program that prompts the user for two words and finds the shortest ladder between the words

import java.io.*;
import java.util.*;

public class wordLadder {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to CS III K Word Ladder.");
        System.out.println("Please give me two English words, and I will change the");
        System.out.println("first into the second by changing one letter at a time.");

        Set<String> dictionary = getDict(input); // Getting the dictionary of all words
        while (true) {
            String word1 = checkValidity(1, input, dictionary); // Checking validity of w1 and w2
            if (word1 == null) { // If user presses enter, break while loop
                break;
            }
            String word2 = checkValidity(2, input, dictionary);
            if (word2 == null) {
                break;
            }
            if (word1.length() != word2.length()) { // Ensuring w1 and w2 are == in length
                System.out.println("Words must be the same length!");
            } else if (word1.equals(word2)) {
                System.out.println("You cannot have two of the same word!"); // Ensuring w1 and w2 and different words
            } else {
                Stack<String> result = findLadder(word1, word2, dictionary);
                if (result == null) {
                    System.out.println("Unable to find ladder between " + word1 + " and " + word2);
                } else {
                    for (int i = 0; i < result.size() - 1; i++) {
                        System.out.print(result.get(i) + " -> "); // Getting word ladder
                    }
                    System.out.println(result.peek());
                }
            }
        }

        System.out.println("Have a nice day.");
    }

    private static Set<String> getDict(Scanner scanner) {
        Set<String> dictionary = new HashSet<>(); // Empty set that will be returned as dictionary
        while (true) {
            System.out.print("Enter dictionary file name: ");
            String fileName = scanner.nextLine();
            try (Scanner fileScanner = new Scanner(new File(fileName))) {
                while (fileScanner.hasNextLine()) { // As long as there is another element to add
                    String line = fileScanner.nextLine();
                    dictionary.add(line.toLowerCase()); // Adding elements from .txt file
                }
                return dictionary;
            } catch (FileNotFoundException e) {
                System.err.println("Error reading the dictionary file: " + e.getMessage()); // Throws exception if
                                                                                            // somethin hits the fans
            }
        }
    }

    private static String checkValidity(int x, Scanner scanner, Set<String> dictionary) {
        while (true) {
            System.out.print("Word #" + x + " (or Enter to quit): ");
            String word = scanner.nextLine().toLowerCase();
            if (word.isEmpty()) { // If user presses enter return null, which will break the loop in main method
                return null;
            }
            if (!dictionary.contains(word)) { // Ensures the word is in the dictionary
                System.out.println("The word is not in the dictionary. Please enter a valid English word.");
            } else {
                return word; // Returning validated word
            }
        }

    }

    private static Stack<String> findLadder(String word1, String word2, Set<String> dictionary) {
        Queue<Stack<String>> queueStack = new LinkedList<>(); // Empty stack of stacks
        Stack<String> initialStack = new Stack<>(); // Adding a stack containing w1 to the queue
        initialStack.push(word1);
        queueStack.add(initialStack);

        Set<String> usedWords = new HashSet<>(); // Set of already used words
        usedWords.add(word1); // Adding word 1 to usedWords

        while (!queueStack.isEmpty()) {
            Stack<String> currentStack = queueStack.poll(); // Dequeue partial ladder stack
            String currentWord = currentStack.peek(); // Last element in partial ladder stack
            for (int i = 0; i < currentWord.length(); i++) { // Iterates through each index of word1
                StringBuilder strWord = new StringBuilder(currentWord);
                for (char j = 'a'; j <= 'z'; j++) { // Iterates through ascii values 96-122
                    strWord.setCharAt(i, j);
                    String neighbor = strWord.toString();
                    if (dictionary.contains(neighbor) && !usedWords.contains(neighbor)) { // Checking if neighbor word
                                                                                          // is in dict and has not been
                                                                                          // used yet
                        if (neighbor.equals(word2)) { // If neighbor word == word2 then return stack containing shortest
                                                      // ladder between w1 and w2
                            currentStack.push(neighbor);
                            return currentStack;
                        } else {
                            usedWords.add(neighbor); // Adding neighbor word to used stack
                            Stack<String> newStack = new Stack<>(); // Creating copy of current partial ladder stack
                            newStack.addAll(currentStack);
                            newStack.push(neighbor);
                            queueStack.add(newStack); // Adds copy back to end of queue
                        }
                    }
                }
            }
        }
        return null; // If no ladders, return null
    }
}
