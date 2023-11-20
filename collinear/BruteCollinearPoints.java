/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {

    private Point[] points;
    private int numberOfSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("there is no point");
        }
        int numberOfPoints = points.length;
        for (int i = 0; i < numberOfPoints - 1; ++i) {
            for (int j = i + 1; j < numberOfPoints; ++j) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException(
                            "there are two same points: " + points[i] + ", " + points[j]);
                }
            }
        }

        this.points = Arrays.copyOf(points, numberOfPoints);
        Arrays.sort(this.points);
        numberOfSegments = 0;
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LinkedList<LineSegment> lines = new LinkedList<>();
        int numberOfPoints = points.length;
        for (int p1 = 0; p1 < numberOfPoints - 3; ++p1) {
            for (int p2 = p1 + 1; p2 < numberOfPoints - 2; ++p2) {
                double s1 = points[p1].slopeTo(points[p2]);
                for (int p3 = p2 + 1; p3 < numberOfPoints - 1; ++p3) {
                    double s2 = points[p1].slopeTo(points[p3]);
                    if (s2 != s1) {
                        continue;
                    }
                    for (int p4 = p3 + 1; p4 < numberOfPoints; ++p4) {
                        double s3 = points[p1].slopeTo(points[p4]);
                        if (s3 == s1) {
                            lines.add(new LineSegment(points[p1], points[p4]));
                            ++numberOfSegments;
                        }
                    }
                }
            }
        }
        return lines.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
