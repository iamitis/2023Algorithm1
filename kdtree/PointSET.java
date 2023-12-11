/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class PointSET {

    private SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw() {
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Point2D> pointsInRect = new LinkedList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                pointsInRect.add(p);
            }
        }
        return pointsInRect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point : points) {
            if (p.distanceTo(point) < minDist) {
                nearest = point;
                minDist = p.distanceTo(point);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        PointSET a = new PointSET();
        Point2D p = new Point2D(0.2, 0.2);
        Point2D q = new Point2D(0.3, 0.3);
        a.insert(p);
        a.insert(q);
        a.draw();
    }

}
