package assignment09;

import org.w3c.dom.Node;

import java.util.ArrayList;

public class BSPTree {
    private Node root = null;

    private static class Node {
        Node left;
        Node right;
        Segment value;

        public Node(Segment value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public BSPTree() {
        root = null;
    }

    public BSPTree(ArrayList<Segment> segments) {

    }

    public Node bulkBuild(ArrayList<Segment> segments) {
        if(segments.isEmpty()) {
            return null;
        } // checks if inserted list has no values.
        Segment splitter = segments.get(0); // Grabs the first segment
        Node n = new Node(splitter);
        ArrayList<Segment> positiveVals = new ArrayList<>();
        ArrayList<Segment> negativeVals = new ArrayList<>(); // divides values by positive an negative values.

        for(int i = 1; i < segments.size(); i++) {
            Segment seg = segments.get(i);
            int side = splitter.whichSide(seg);
            if(side < 0) {
                negativeVals.add(seg);
            }
            else if(side > 0) {
                positiveVals.add(seg);
            }
            else {
                Segment[] segArr = splitter.split(seg);
                negativeVals.add(segArr[0]);
                positiveVals.add(segArr[1]);
            }
        }
        n.left = bulkBuild(negativeVals);
        n.right = bulkBuild(positiveVals);
        return n;
    }

    public void insert(Segment segment) {

    }

    private void recursiveInsert(Segment segment) {

    }

    public void traverseFarToNear(double x, double y, SegmentCallback callback) {

    }

    private void recursiveTraverseFarToNear(double x, double y, SegmentCallback callback) {

    }

    Segment collision(Segment query) {
        return null;
    }
}
