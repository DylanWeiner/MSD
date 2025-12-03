package assignment08;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

public class PathFinder {
    public static void solveMaze(String inputFile, String outputFile) throws IOException {
        // initialize graph
        // BFS
        // write path into txt file

        PacmanGraph p = new PacmanGraph();
        p.GraphBuilder(inputFile, outputFile);
    }
}