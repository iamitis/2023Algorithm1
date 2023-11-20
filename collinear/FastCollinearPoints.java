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

public class FastCollinearPoints {

    private Point[] points;
    private int numberOfSegments;

    public FastCollinearPoints(Point[] points) {
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
        numberOfSegments = 0;
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LinkedList<LineSegment> lines = new LinkedList<>();
        int numberOfPoints = points.length;
        Point[] otherPoints = Arrays.copyOf(points, numberOfPoints);
        for (int p = 0; p < numberOfPoints; ++p) {
            Arrays.sort(otherPoints, points[p].slopeOrder());
            int q1 = 1; // otherPoints[0]为p点自己，因为两相同点连线斜率为-inf
            while (q1 < numberOfPoints - 2) {
                int q2 = q1 + 1;
                double slopeToQ1 = points[p].slopeTo(otherPoints[q1]);
                while (q2 < numberOfPoints && points[p].slopeTo(otherPoints[q2]) == slopeToQ1) {
                    ++q2;
                }
                if (q2 - q1 >= 3 && points[p].compareTo(findMin(otherPoints, q1, q2))
                        < 0) { // p点需小于其他点以免重复连线
                    lines.add(new LineSegment(points[p], findMax(otherPoints, q1, q2)));
                    ++numberOfSegments;
                }
                q1 = q2;
            }
        }
        return lines.toArray(new LineSegment[0]);
    }

    private Point findMin(Point[] otherPoints, int left, int right) {
        Point min = otherPoints[left];
        for (int i = left + 1; i < right; ++i) {
            if (otherPoints[i].compareTo(min) < 0) {
                min = otherPoints[i];
            }
        }
        return min;
    }

    private Point findMax(Point[] otherPoints, int left, int right) {
        Point max = otherPoints[left];
        for (int i = left + 1; i < right; ++i) {
            if (otherPoints[i].compareTo(max) > 0) {
                max = otherPoints[i];
            }
        }
        return max;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
