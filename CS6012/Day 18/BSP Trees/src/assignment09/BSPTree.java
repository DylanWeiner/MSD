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
        bulkBuild(segments);
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
            int side = splitter.whichSide(seg); // retrieves segments and splits them for left and right.
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
            }  // properly assigns segment to each side.
        }
        n.left = bulkBuild(negativeVals);
        n.right = bulkBuild(positiveVals);
        return n;
    } // builds and returns nodes off of all the input information.

    public void insert(Segment segment) {
        recursiveInsert(segment, root);
    }

    private Node recursiveInsert(Segment segment, Node current) {
        if(segment == null) {
            return null;
        }

        if(root == null) {
            root = new Node(segment);
        }

        int side = current.value.whichSide(segment);

        if(side < 0) {
            current.left = recursiveInsert(segment, current.left);
        }
        else if(side > 0) {
            current.right = recursiveInsert(segment, current.right);
        }
        else { // We call the split method on the segment
            Segment[] segArr = segment.split(segment);
            current.left = recursiveInsert(segment, current.left);
            current.right = recursiveInsert(segment, current.right);
        }
        return current;
    }

    public void traverseFarToNear(double x, double y, SegmentCallback callback) {
        recursiveTraverseFarToNear(x, y, root, callback);
    }

    private void recursiveTraverseFarToNear(double x, double y, Node current, SegmentCallback callback) {
        if(current == null) {
            return;
        }

//        int side = current.value.whichSide(segment);
        // take a side

        // if they're equal you don't need to split since there's no difference btwn going left and right
        // side < 0 go right, use callback, go left
        //otherwise do the inverse.
    }

    public Segment collision(Segment query) {
        return recursiveCollision(query, root);
    }

    private Segment recursiveCollision(Segment query, Node current) {
        if(current == null) {
            return null;
        }
        // Look at segment file to know which methods to use.
        // make a near and far node
        // if startside >= 0 nearnode is right and farnode is left
        // otherwise it's the inverse.
        // Make a segment equal to recursive collision
        // hit is null we can return hit
        // if it intersects we'll return the node value
        // no collision will return null.
        // if the start side or end side is negative it means the camera can't see it but multiplying the start and end point
        // and seeing if it is <= 0 will let us know it is still there so if this is <= 0, we can put another collision check
        // in the if statement so we still collide w walls we can't see.
    }
}
