package assignment08;

import java.io.*;
import java.util.*;

public class PacmanGraph {
    private static PacmanNode[][] maze;
    static int height;
    static int width;
    private static PacmanNode source;
    private static PacmanNode target;
    int x_start;
    int y_start;
    int x_end;
    int y_end;

    public PacmanGraph() {
        PacmanNode[][] nodes;
        this.height = height;
        this.width = width;
        this.x_start = x_start;
        this.y_start = y_start;
        this.x_end = x_end;
        this.y_end = y_end;
    }

    private class PacmanNode {
        char character; // character of the node
        boolean visited; // tracks if we visited each node.
        int x;
        int y;
        PacmanNode prevNode;

        PacmanNode(char character, int x, int y) {
            this.character = character;
            this.x = x;
            this.y = y;
            this.prevNode = null;
            this.visited = false;
        }
    }

    public void GraphBuilder(String inputFile, String outputFile) throws IOException {
        int height = 0;
        int width = 0;

        PacmanGraph pGraph = new PacmanGraph();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String[] dimensions = br.readLine().split(" ");
            String line;
            int row = 0;
            BufferedWriter mazeOutput = new BufferedWriter(new FileWriter(outputFile));
            height = Integer.parseInt(dimensions[0]);
            width = Integer.parseInt(dimensions[1]);
            maze = new PacmanNode[height][width];
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    maze[row][i] = new PacmanNode(line.charAt(i), i, row);
                    if (maze[row][i].character == 'S') {
                        x_start = i;
                        y_start = row;
                    } else if (maze[row][i].character == 'G') {
                        x_end = i;
                        y_end = row;
                    }
                    mazeOutput.write(maze[row][i].character);
                }
                mazeOutput.write("\n");
                row++;
            }
            mazeOutput.close();
        }
    }

    public static PacmanNode BFS() {
        Set<PacmanNode> seen = new HashSet<PacmanNode>();
        Queue<PacmanNode> queue = new LinkedList<>() {
        };
        queue.offer(source);
        while (!queue.isEmpty()) {
            PacmanNode curr = queue.poll();
            source = curr;
            source.visited = true;
            if(source.x+1 <= width) {
                source.x = curr.x + 1;
            } else if(source.y+1 <= height) {
                source.y = curr.y + 1;
            } else if(source.x + 1 > width && source.y + 1 > height) {
                source.x = 0;
                source.y = 0;
            }
            if (curr == target) {
                System.out.println("\n\n" + "test");
                return curr;
            }
            else {
//                seen.add(curr);
                for (PacmanNode neighbor : seen) {
                    if (!seen.contains(neighbor)) {
                        queue.offer(neighbor); // if it has never been in the queue.
                        // neighbor came from curr. won't be overwritten bc it's only ever written once.
                    }
                }
                BFSRecursive(curr, seen, queue);
            }
        }
        return null;
    }

    public static PacmanNode BFSRecursive(PacmanNode curr, Set<PacmanNode> seen, Queue<PacmanNode> queue) {
        queue.offer(source);
        while (!queue.isEmpty()) {
            source.prevNode = curr;
            curr = queue.poll();
            source.visited = true;
            if(source.x+1 <= width) {
                source.x = curr.x + 1;
            } else if(source.y+1 <= height) {
                source.y = curr.y + 1;
            } else if(source.x + 1 > width && source.y + 1 > height) {
                source.x = 0;
                source.y = 0;
            }
            if (curr == target) {
                System.out.println("\n\n" + "test");
                return curr;
            }
            else {
//                seen.add(curr);
                for (PacmanNode neighbor : seen) {
                    if (!seen.contains(neighbor)) {
                        queue.offer(neighbor); // if it has never been in the queue.
                        // neighbor came from curr. won't be overwritten bc it's only ever written once.
                    }
                }
                BFSRecursive(curr, seen, queue);
            }
        }
        return null;
    }
}
