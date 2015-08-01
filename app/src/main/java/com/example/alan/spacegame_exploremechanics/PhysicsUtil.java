package com.example.alan.spacegame_exploremechanics;

import android.graphics.PointF;

/**
 * Created by Alan on 7/18/2015.
 */
public class PhysicsUtil {

    /**
     * Optimized version of transforming a vector in the standard coordinate system into another system
     * defined by a pair of normalized basis floats.
     *
     * @param x x-component in standard coordinate system
     * @param y y-component in standard coordinate system
     * @param xBasis i-hat component of basis of system to transform into
     * @param yBasis j-hat component of basis of system to transform into
     * @return PointF representing x and y components in new system
     */
    static PointF transformToCustom(float x, float y, PointF basis) {
        PointF toReturn = new PointF();
        toReturn.x = basis.x *x + basis.y *y;
        toReturn.y = -basis.y*x +basis.x*y;
        return toReturn;
    }

    /**
     * Optimized version of transforming a vector in the standard coordinate system into another system
     * defined by a pair of normalized basis floats.
     *
     * @param x x-component in standard coordinate system
     * @param y y-component in standard coordinate system
     * @param xBasis i-hat component of basis of system to transform from
     * @param yBasis j-hat component of basis of system to transform from
     * @return PointF representing x and y components in standard system
     */
    static PointF transformToStandard(float x, float y, PointF basis) {
        PointF toReturn = new PointF();
        toReturn.x = x*basis.x + -basis.y*y;
        toReturn.y = basis.y*x + basis.x*y;
        return toReturn;
    }

    static PointF toNorm(PointF vec) {
        return new PointF( vec.x/ vec.length() , vec.y/ vec.length()  );
    }
}
