package assignment09;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

// By Dylan Weiner

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
        root = bulkBuild(segments);
    } // Uses bulkBuild out of premade segments.

    public Node bulkBuild(ArrayList<Segment> segments) {
        if(segments.isEmpty()) {
            return null;
        } // checks if inserted list has no values.
        Random rand = new Random();
        int randomIndex = rand.nextInt(0,segments.size());
        Segment splitter = segments.get(randomIndex); // Grabs the first segment
        Node n = new Node(splitter);
        ArrayList<Segment> positiveVals = new ArrayList<>();
        ArrayList<Segment> negativeVals = new ArrayList<>(); // divides values by positive an negative values.

        for(int i = 0; i < segments.size(); i++) {
            if(i == randomIndex) {
                continue;
            }
            else {
                Segment seg = segments.get(i);
                int side = splitter.whichSide(seg); // retrieves segments and splits them for left and right.
                if (side < 0) {
                    negativeVals.add(seg);
                } else if (side > 0) {
                    positiveVals.add(seg);
                } else {
                    Segment[] segArr = splitter.split(seg);
                    negativeVals.add(segArr[0]);
                    positiveVals.add(segArr[1]);
                }  // properly assigns segment to each side.
            }
        }
        n.left = bulkBuild(negativeVals);
        n.right = bulkBuild(positiveVals);
        return n;
    } // builds and returns nodes off of all the input information.

    public void insert(Segment segment) {
        root = recursiveInsert(segment, root);
    }

    private Node recursiveInsert(Segment segment, Node current) {
        if(current == null) {
            return new Node(segment);
        }

        int side = current.value.whichSide(segment);

        if(side < 0) {
            current.left = recursiveInsert(segment, current.left);
        }
        else if(side > 0) {
            current.right = recursiveInsert(segment, current.right);
        }
        else { // We call the split method on the segment
            Segment[] segArr = current.value.split(segment);
            current.left = recursiveInsert(segArr[0], current.left);
            current.right = recursiveInsert(segArr[1], current.right);
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

        int side = current.value.whichSidePoint(x, y);
        // take a side

        // if they're equal you don't need to split since there's no difference btwn going left and right
        if(side < 0) {
            recursiveTraverseFarToNear(x, y, current.right, callback);
            callback.callback(current.value);
            recursiveTraverseFarToNear(x, y, current.left, callback);
        } // side < 0 go right, use callback, go left
        else { // if(side > 0)
            recursiveTraverseFarToNear(x, y, current.left, callback);
            callback.callback(current.value);
            recursiveTraverseFarToNear(x, y, current.right, callback);
        } //otherwise do the inverse. If side == 0, it can go with either statement, so we lump it in w/ else.
    }

    public Segment collision(Segment query) {
        return recursiveCollision(query, root);
    }

    private Segment recursiveCollision(Segment query, Node current) {
        if(current == null) {
            return null;
        }
        // Look at segment file to know which methods to use.
        int startSide = current.value.whichSidePoint(query.x1(), query.y1());
        int endSide = current.value.whichSidePoint(query.x2(), query.y2());

        // Make a near and far Node
        Node nearNode = null;
        Node farNode = null;

        if(startSide >= 0) { // if startSide >= 0, nearNode is right and farNode is left
            nearNode = current.right;
            farNode = current.left;
        } else { // otherwise it's the inverse.
            nearNode = current.left;
            farNode = current.right;
        }
        Segment collide = recursiveCollision(query, nearNode); // Make a segment equal to recursive collision
        if(collide != null) {
            return collide;
        } // collide is not null so we can return the value

        if(current.value.intersects(query)) {
            return current.value;
        } // if it intersects we'll return the node value

        if(startSide * endSide <= 0) {
            return recursiveCollision(query, farNode);
        }

        // no collision will return null.
        return null;
        // if the start side or end side is negative it means the camera can't see it but multiplying the start and end point
        // and seeing if it is <= 0 will let us know it is still there so if this is <= 0, we can put another collision check
        // in the if statement so we still collide w walls we can't see.
    }

    public Segment naiveCollision(Segment query) {
        return naiveCollisionRecursive(query, root);
    }

    private Segment naiveCollisionRecursive(Segment query, Node current) {
        AtomicBoolean collisionFound = new AtomicBoolean(false);
        traverseFarToNear(0, 0,  //they don't matter
        (segment) -> {
            if(current.value.intersects(query)){
                collisionFound.set(true);
            }
        });
        return collisionFound.get() ? current.value : null;
    }
}
