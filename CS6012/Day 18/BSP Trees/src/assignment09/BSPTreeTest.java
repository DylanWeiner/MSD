package assignment09;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BSPTreeTest {
    BSPTree bspTree = new BSPTree();
    ArrayList<Segment> segments = new ArrayList<>();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.Test
    void bulkBuild() {
    }

    @org.junit.jupiter.api.Test
    void insert() {
    }

    @org.junit.jupiter.api.Test
    void traverseFarToNear() {
        Segment s1 = new Segment(10,0,15,0);
        Segment s2 = new Segment(10,40,15,40);
        Segment s3 = new Segment(10,60,15,60);
        Segment s4 = new Segment(10,20,15,20);

        segments.add(s1);
        segments.add(s2);
        segments.add(s3);
        segments.add(s4);

        ArrayList<Segment> correctOrder = new ArrayList<>();
        correctOrder.add(s3);
        correctOrder.add(s2);
        correctOrder.add(s4);
        correctOrder.add(s1);

        for(int i = 0; i < 10; i++) {
            bspTree = new BSPTree(segments);
            BSPTree insertBuild = new BSPTree();
            for (Segment s : segments) {
                insertBuild.insert(s);
            }

            ArrayList<Segment> segmentsBulkList = new ArrayList<>();
            bspTree.traverseFarToNear(12, -1, e ->
                    segmentsBulkList.add(e)
            );

            ArrayList<Segment> segmentsInOrder = new ArrayList<>();
            insertBuild.traverseFarToNear(12, -1, e -> segmentsInOrder.add(e));

            assertEquals(correctOrder, segmentsBulkList);
            assertEquals(correctOrder, segmentsInOrder);
        }
    }

    @org.junit.jupiter.api.Test
    void collision() {
    }
}
