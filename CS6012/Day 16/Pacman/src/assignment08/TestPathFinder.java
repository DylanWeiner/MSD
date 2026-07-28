package assignment08;

import org.junit.jupiter.api.Test;

import java.io.*;

class TestPathFinder {

    public static void main(String[] args) throws IOException {

        /*
         * The below code assumes you have a file "tinyMaze.txt" in your project folder.
         * If PathFinder.solveMaze is implemented, it will create a file "tinyMazeOutput.txt" in your project folder.
         *
         * REMEMBER - You have to refresh your project to see the output file in your package explorer.
         * You are still required to make JUnit tests...just lookin' at text files ain't gonna fly.
         */
        PathFinder.solveMaze("mazes/tinyMaze.txt", "mazes/tinyMazeOutput.txt");
        PathFinder.solveMaze("mazes/bigMaze.txt", "mazes/bigMazeOutput.txt");
        PathFinder.solveMaze("mazes/classic.txt", "mazes/classicOutput.txt");
        PathFinder.solveMaze("mazes/demoMaze.txt", "mazes/demoMazeOutput.txt");
        PathFinder.solveMaze("mazes/mediumMaze.txt", "mazes/mediumMazeOutput.txt");
        PathFinder.solveMaze("mazes/randomMaze.txt", "mazes/randomMazeOutput.txt");
        PathFinder.solveMaze("mazes/tinyOpen.txt", "mazes/tinyOpenOutput.txt");
        PathFinder.solveMaze("mazes/turn.txt", "mazes/turnOutput.txt");
        PathFinder.solveMaze("mazes/unsolvable.txt", "mazes/unsolvableOutput.txt");
        PathFinder.solveMaze("mazes/straight.txt", "mazes/straightOutput.txt");
        PathFinder.solveMaze("mazes/customMediumMazeOne.txt", "mazes/customMediumMazeOneOutput.txt");
        PathFinder.solveMaze("mazes/customMediumMazeTwo.txt", "mazes/customMediumMazeTwoOutput.txt");
    }

    @Test
    public void compareSolutionsTest() throws IOException {
        compareSolutions("mazes/bigMazeSol.txt", "mazes/bigMazeOutput.txt");
        compareSolutions("mazes/classicSol.txt", "mazes/classicOutput.txt");
        compareSolutions("mazes/demoMazeSol.txt", "mazes/demoMazeOutput.txt");
        compareSolutions("mazes/mediumMazeSol.txt", "mazes/mediumMazeOutput.txt");
        compareSolutions("mazes/randomMazeSol.txt", "mazes/randomMazeOutput.txt");
        compareSolutions("mazes/tinyMazeSol.txt", "mazes/tinyMazeOutput.txt");
        compareSolutions("mazes/tinyOpenSol.txt", "mazes/tinyOpenOutput.txt");
        compareSolutions("mazes/turnSol.txt", "mazes/turnOutput.txt");
        compareSolutions("mazes/unsolvableSol.txt", "mazes/unsolvableOutput.txt");
        compareSolutions("mazes/straightSol.txt", "mazes/straightOutput.txt");
        compareSolutions("mazes/customMediumMazeOneSol.txt", "mazes/customMediumMazeOneOutput.txt");
        compareSolutions("mazes/customMediumMazeTwoSol.txt", "mazes/customMediumMazeTwoOutput.txt");
    }

    public void compareSolutions(String solutionFile, String outputFile) throws IOException {
        int curr_row = 0;
        int outputStepCount = 0;
        int solutionStepCount = 0;
        try (BufferedReader solution = new BufferedReader(new FileReader(solutionFile))) {
            String line;
            while ((line = solution.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '.') {
                        solutionStepCount++;
                    }
                }
                curr_row++;
            }
        }
        try (BufferedReader output = new BufferedReader(new FileReader(outputFile))) {
            curr_row = 0;
            String line;
            while ((line = output.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '.') {
                        outputStepCount++;
                    }
                }
                curr_row++;
            }
            System.out.println("Output: " + outputStepCount + " " + "Solution: " + solutionStepCount);
        }
        if(solutionStepCount < outputStepCount) {
            System.out.println("Solution is better");
        }
        else if(solutionStepCount > outputStepCount) {
            System.out.println("Output is better");
        }
        else {
            System.out.println("Solutions are equal");
        }
    }


}