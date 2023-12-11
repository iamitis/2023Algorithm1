/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree {

    private Node kdtr;
    private int size;

    public KdTree() {
        kdtr = new Node();
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node current = kdtr;
        while (current != null) {
            if (p.equals(current.getPoint())) {
                return;
            }
            if (current.getRightRect().contains(p)) { // 在右子树
                if (current.right == null) { // 右子树为空则于此插入p
                    current.right = new Node(p, current);
                    ++size;
                    return;
                }
                current = current.right;
            }
            // 在左子树
            else {
                if (current.left == null) { // 左子树为空则于此插入p
                    current.left = new Node(p, current);
                    ++size;
                    return;
                }
                current = current.left;
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node current = kdtr.right;
        while (current != null) {
            if (p.equals(current.getPoint())) {
                return true;
            }
            if (current.getRightRect().contains(p) && current.right != null) {
                current = current.right;
            }
            else if (current.getLeftRect().contains(p) && current.left != null) {
                current = current.left;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public void draw() {
        draw(kdtr.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Point2D> points = new LinkedList<>();
        range(points, kdtr.right, rect);
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D nearest = kdtr.right.getPoint();
        double minDist = nearest.distanceTo(p);
        nearest = nearest(p, kdtr.right, nearest, minDist);
        return nearest;
    }

    /**
     * 递归查找点集中在给定rect范围内的点
     *
     * @param points 已找到的点
     * @param n      当前根节点
     * @param rect   给定范围
     */
    private void range(LinkedList<Point2D> points, Node n, RectHV rect) {
        if (n != null) {
            if (rect.contains(n.getPoint())) {
                points.add(n.getPoint());
            }
            if (rect.intersects(n.getRightRect())) {
                range(points, n.right, rect);
            }
            if (rect.intersects(n.getLeftRect())) {
                range(points, n.left, rect);
            }
        }
    }

    private Point2D nearest(Point2D p, Node n, Point2D nearest, double minDist) {
        if (n == null) {
            return nearest;
        }
        Point2D newNearest = nearest;
        double p2Rect; // p到左子树rect或右子树rect的距离
        double p2r = n.getPoint().distanceTo(p); // 此节点的point到p的距离
        if (p2r <= minDist) {
            newNearest = n.getPoint();
            minDist = p2r;
        }
        if (n.getRightRect().contains(p)) {
            newNearest = nearest(p, n.right, newNearest, minDist);
            minDist = newNearest.distanceTo(p);
            p2Rect = n.getLeftRect().distanceTo(p);
            if (p2Rect < minDist) { // 左子树可能有更近的点，需要查左子树
                newNearest = nearest(p, n.left, newNearest, minDist);
            }
        }
        else {
            newNearest = nearest(p, n.left, newNearest, minDist);
            minDist = newNearest.distanceTo(p);
            p2Rect = n.getRightRect().distanceTo(p);
            if (p2Rect < minDist) { // 右子树可能有更近的点，需要查右子树
                newNearest = nearest(p, n.right, newNearest, minDist);
            }
        }
        return newNearest;
    }

    private void draw(Node n) {
        if (n == null) {
            return;
        }
        StdDraw.setPenColor();
        StdDraw.setPenRadius(0.02);
        StdDraw.point(n.getPoint().x(), n.getPoint().y());
        StdDraw.setPenRadius(0.01);
        if (n.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.getPoint().x(), n.getLeftRect().ymin(), n.getPoint().x(),
                         n.getLeftRect().ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.getLeftRect().xmin(), n.getPoint().y(), n.getLeftRect().xmax(),
                         n.getPoint().y());
        }
        draw(n.left);
        draw(n.right);
    }

    private class Node {
        private Point2D point;
        private boolean isVertical; // 当前节点的方向
        private RectHV leftRect;    // 左子树的范围
        private RectHV rightRect;   // 右子树的范围
        private Node left = null;
        private Node right = null;

        /**
         * 构造节点
         *
         * @param p        插入的点
         * @param prevNode 上一级节点
         */
        public Node(Point2D p, Node prevNode) {
            point = p;
            this.isVertical = !prevNode.isVertical;
            // 构造本节点代表的rect
            if (isVertical) {
                double xmin = prevNode.getRightRect().xmin();
                double xmax = prevNode.getRightRect().xmax();
                double ymin = point.y() < prevNode.getPoint().y() ? prevNode.getLeftRect().ymin() :
                              prevNode.getPoint().y();
                double ymax = point.y() < prevNode.getPoint().y() ? prevNode.getPoint().y() :
                              prevNode.getRightRect().ymax();
                leftRect = new RectHV(xmin, ymin, point.x(), ymax);
                rightRect = new RectHV(point.x(), ymin, xmax, ymax);
            }
            else {
                double xmin = point.x() < prevNode.getPoint().x() ? prevNode.getLeftRect().xmin() :
                              prevNode.getPoint().x();
                double xmax = point.x() < prevNode.getPoint().x() ? prevNode.getPoint().x() :
                              prevNode.getRightRect().xmax();
                double ymin = prevNode.getRightRect().ymin();
                double ymax = prevNode.getRightRect().ymax();
                leftRect = new RectHV(xmin, ymin, xmax, point.y());
                rightRect = new RectHV(xmin, point.y(), xmax, ymax);
            }
        }

        // 构造根节点，point=(min, min), leftRect=(min,min,min,min), rightRect=(min,min,max,max)
        public Node() {
            point = new Point2D(-0.1, -0.1);
            isVertical = false;
            leftRect = new RectHV(-0.1, -0.1, -0.1, -0.1);
            rightRect = new RectHV(-0.1, -0.1, 1.1, 1.1);
        }

        public Point2D getPoint() {
            return point;
        }

        public RectHV getLeftRect() {
            return leftRect;
        }

        public RectHV getRightRect() {
            return rightRect;
        }

    }

    public static void main(String[] args) {
        String filename = "./input/mytest.txt";
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        Point2D query = new Point2D(0.655, 0.686);
        System.out.println(kdtree.nearest(query).toString());
        kdtree.draw();
    }

}
