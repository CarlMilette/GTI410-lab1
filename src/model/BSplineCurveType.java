/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package model;

import java.awt.*;
import java.util.List;

/**
 * <p>Title: BSplineCurveType</p>
 * <p>Copyright: Copyright (c) 2004 - based on the BezierCurveType from Eric Paquette</p>
 * <p>Company: GTI410 - LAB 4 - �Ecole de Technologie Superieure</p>
 */
public class BSplineCurveType extends CurveType {

	public BSplineCurveType(String BSPLINE) {
		super(BSPLINE);
	}
	
	/* (non-Javadoc)
	 * @see model.CurveType#getNumberOfSegments(int)
	 */
	public int getNumberOfSegments(int numberOfControlPoints) {
	// NumberofSegments adapted to the B-spline curve
	//B-spline est défini par 4 points de contrôles
		if (numberOfControlPoints < 4) return 0;
		if (numberOfControlPoints == 4) return 1;
		return (numberOfControlPoints - 3);
	}

	/* (non-Javadoc)
	 * @see model.CurveType#getNumberOfControlPointsPerSegment()
	 */
	public int getNumberOfControlPointsPerSegment() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see model.CurveType#getControlPoint(java.util.List, int, int)
	 */
	public ControlPoint getControlPoint(
		List controlPoints,
		int segmentNumber,
		int controlPointNumber) {
		//segmentNumber adapted to B-spline curve
		int controlPointIndex = segmentNumber * 1 + controlPointNumber;
		return (ControlPoint)controlPoints.get(controlPointIndex);
	}

	/* (non-Javadoc)
	 * @see model.CurveType#evalCurveAt(java.util.List, double)
	 */
	public Point evalCurveAt(List controlPoints, double t) {
		// B-splline est défini par 4 points de contrôle
		List tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1);
		List gVector = Matrix.buildColumnVector4(((ControlPoint)controlPoints.get(0)).getCenter(), 
			((ControlPoint)controlPoints.get(1)).getCenter(), 
			((ControlPoint)controlPoints.get(2)).getCenter(),
			((ControlPoint)controlPoints.get(3)).getCenter());
		Point p = Matrix.eval(tVector, matrix, gVector);
		//4 points et On partage les points de contrôle entre les segments.
		p.x = p.x / 6;
		p.y = p.y / 6;
		return p;
	}
	//Matrix B-spline defined
	private List BSPLINEMatrix =
		Matrix.buildMatrix4(-1,  3, -3, 1,
							 3, -6,  3, 0, 
							-3,  0,  3, 0,
							 1,  4,  1, 0);
							 
	private List matrix = BSPLINEMatrix;
}
