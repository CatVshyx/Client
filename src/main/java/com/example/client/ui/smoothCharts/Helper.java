package com.example.client.ui.smoothCharts;

import javafx.geometry.Point2D;

public class Helper {

    public static final int clamp(final int MIN, final int MAX, final int VALUE) {
        if (VALUE < MIN) return MIN;
        if (VALUE > MAX) return MAX;
        return VALUE;
    }
    public static final double clamp(final double MIN, final double MAX, final double VALUE) {
        if (VALUE < MIN) return MIN;
        if (VALUE > MAX) return MAX;
        return VALUE;
    }

    public static int roundDoubleToInt(final double VALUE){
        double dAbs = Math.abs(VALUE);
        int    i      = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return VALUE < 0 ? -i : i;
        } else {
            return VALUE < 0 ? -(i + 1) : i + 1;
        }
    }

    public static final Point2D[] subdividePoints(final Point2D[] POINTS, final int SUB_DEVISIONS) {
        assert POINTS != null;
        assert POINTS.length >= 3;

        int    noOfPoints = POINTS.length;

        Point2D[] subdividedPoints = new Point2D[((noOfPoints - 1) * SUB_DEVISIONS) + 1];

        double increments = 1.0 / (double) SUB_DEVISIONS;

        for (int i = 0 ; i < noOfPoints - 1 ; i++) {
            Point2D p0 = i == 0 ? POINTS[i] : POINTS[i - 1];
            Point2D p1 = POINTS[i];
            Point2D p2 = POINTS[i + 1];
            Point2D p3 = (i+2 == noOfPoints) ? POINTS[i + 1] : POINTS[i + 2];

            CatmullRom crs = new CatmullRom(p0, p1, p2, p3);

            for (int j = 0; j <= SUB_DEVISIONS; j++) {
                subdividedPoints[(i * SUB_DEVISIONS) + j] = crs.q(j * increments);
            }
        }
        return subdividedPoints;
    }
}