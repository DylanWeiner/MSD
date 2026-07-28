import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Integer sum = 0;
        ArrayList<Integer> testArray = new ArrayList<Integer>(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            testArray.add(random.nextInt());
            sum += testArray.get(i);
            System.out.println(testArray.get(i));
        }
        System.out.println(sum);
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = input.nextLine();
        System.out.print("Enter your age: ");
        Integer age = input.nextInt();
        if (age >= 18) {
            System.out.println("You are old enough to vote.");
        } else {
            System.out.println("You are not old enough to vote.");
        }
        if (age >= 98) {
            System.out.println("You are part of the Greatest Generation.");
        } else if (age >= 80) {
            System.out.println("You are part of the Silent Generation.");
        } else if (age >= 61) {
            System.out.println("You are part of the Baby Boomers.");
        } else if (age >= 45) {
            System.out.println("You are part of Gen X.");
        } else if (age >= 29) {
            System.out.println("You are part of the Millenials.");
        } else if (age >= 13) {
            System.out.println("You are part of the Zoomers.");
        } else if (age >= 0) {
            System.out.println("You are part of Generation Alpha.");
        } else if (age <= 0) {
            System.out.println("You are lying.");
        } else {
            System.out.println("You are ancient.");
        }
    }
}
