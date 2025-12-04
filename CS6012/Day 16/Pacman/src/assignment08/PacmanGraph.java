package assignment08;

import java.io.*;
import java.util.*;

public class PacmanGraph {
    private PacmanNode[][] maze;
    int height;
    int width;
    int column_start;
    int row_start;
    int column_end;
    int row_end;

    public PacmanGraph() {
        PacmanNode[][] nodes;
    }

    private class PacmanNode {
        char character; // character of the node
        boolean visited = false; // tracks if we visited each node.
        int column;
        int row;
        PacmanNode prevNode;

        PacmanNode(char character, int column, int row) {
            this.character = character;
            this.column = column;
            this.row = row;
            this.prevNode = null;
            this.visited = false;
        }
    }

    public void GraphBuilder(String inputFile, String outputFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String[] dimensions = br.readLine().split(" ");
            String line;
            int curr_row = 0;
            BufferedWriter mazeOutput = new BufferedWriter(new FileWriter(outputFile));
            height = Integer.parseInt(dimensions[0]);
            width = Integer.parseInt(dimensions[1]);
            maze = new PacmanNode[height][width];
            mazeOutput.write(height + " " + width + "\n");
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    maze[curr_row][i] = new PacmanNode(line.charAt(i), i, curr_row);
                    if (maze[curr_row][i].character == 'S') {
                        column_start = i;
                        row_start = curr_row;
                    } else if (maze[curr_row][i].character == 'G') {
                        column_end = i;
                        row_end = curr_row;
                    }
                    mazeOutput.write(maze[curr_row][i].character);
                }
                mazeOutput.write("\n");
                curr_row++;
            }
            mazeOutput.close();
        }
    }

    public void BFS() {
        Queue<PacmanNode> queue = new LinkedList<>() {
        };
//        ArrayList<PacmanNode> directions = new ArrayList<PacmanNode>();
        boolean[][] visited = new boolean[height][width];
        visited[row_start][column_start] = true;
        PacmanNode source = new PacmanNode('S', column_start, row_start);

        int[][] edges = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        queue.offer(source);
        source.visited = true;

        while (!queue.isEmpty()) {
            PacmanNode curr = queue.poll();
            System.out.println("Visiting " + curr.character + " at " + curr.row + " " + curr.column);
            if (curr.character == 'G') {
                return;
//                return true;
            }

            for (int[] direction : edges) {
                if (curr.row + direction[0] < height && curr.column + direction[1] < width) {
                    PacmanNode neighborNode = maze[curr.row + direction[0]][curr.column + direction[1]];
                    if (!visited[neighborNode.row][neighborNode.column] && neighborNode.character != 'X') {
                        neighborNode.prevNode = curr;
                        visited[neighborNode.row][neighborNode.column] = true;
                        queue.add(neighborNode);
                    }
                }
            } // retrieves upNode, downNode, leftNode, and rightNode in order.
        }
        return;
    }



    public void markPath() {
        PacmanNode goal = this.maze[row_end][column_end];
        while (goal.prevNode != null) {
            if(goal.prevNode.character == 'S') {
                return;
            }
            goal.prevNode.character = '.';
            goal = goal.prevNode;
        }
    }

    public void createFile(String outputFile) throws IOException {
        try (BufferedWriter mazeOutput = new BufferedWriter(new FileWriter(outputFile))) {
            int count = 0;
            mazeOutput.write(height + " " + width + "\n");
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    if (maze[i][j].character == '.') {
                        count += 1;
                    }
                    mazeOutput.write(maze[i][j].character);
                }
                mazeOutput.write("\n");
            }
            System.out.println(count);
        }
    }
}
