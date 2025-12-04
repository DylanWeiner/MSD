package assignment08;

import java.io.IOException;

public class TestPathFinder {

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
    }
}