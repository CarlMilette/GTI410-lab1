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
 * <p>Title: HermiteCurveType</p>
 * <p>Copyright: Copyright (c) 2004 - based on the BezierCurveType from Eric Paquette</p>
 * <p>Company: GTI410 - LAB 4 - �Ecole de Technologie Superieure</p>
 */
public class HermiteCurveType extends CurveType {

	public HermiteCurveType(String HERMITE) {
		super(HERMITE);
	}
	
	/* (non-Javadoc)
	 * @see model.CurveType#getNumberOfSegments(int)
	 */
	public int getNumberOfSegments(int numberOfControlPoints) {
		if (numberOfControlPoints >= 4) {
			return (numberOfControlPoints - 1) / 3;
		} else {
			return 0;
		}
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
		int controlPointIndex = segmentNumber * 3 + controlPointNumber;
		return (ControlPoint)controlPoints.get(controlPointIndex);
	}
	/* (non-Javadoc)
	 * @see model.CurveType#evalCurveAt(java.util.List, double)
	 */
	public Point evalCurveAt(List controlPoints, double t) {
		List tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1);
		//Defined the tangents to extremities of the Hermite curve
		//http://pulsar.webshaker.net/2012/08/29/les-courbes-de-bezier-1/ . Consulté le 23 juillet 2016.
		//tang1 = tangent to extremities 1
		int tang1X = ((ControlPoint)controlPoints.get(1)).getCenter().x - ((ControlPoint)controlPoints.get(0)).getCenter().x;
		int tang1Y = ((ControlPoint)controlPoints.get(1)).getCenter().y - ((ControlPoint)controlPoints.get(0)).getCenter().y;
		Point tang1 = new Point(tang1X, tang1Y);
		//tang2 = tangent to extremities 2
		int tang2X = ((ControlPoint)controlPoints.get(3)).getCenter().x - ((ControlPoint)controlPoints.get(2)).getCenter().x;
		int tang2Y = ((ControlPoint)controlPoints.get(3)).getCenter().y - ((ControlPoint)controlPoints.get(2)).getCenter().y;
		Point tang2 = new Point(tang2X, tang2Y);
/*(ControlPoint)controlPoints.get(1)) and (ControlPoint)controlPoints.get(2)).getCenter() take off
* and we added tang1 and tang2 to adapt the Bezier code to the Hermite code
* */
		List gVector = Matrix.buildColumnVector4(((ControlPoint)controlPoints.get(0)).getCenter(),
				                                ((ControlPoint)controlPoints.get(3)).getCenter(), tang1, tang2);
		Point p = Matrix.eval(tVector, matrix, gVector);
		return p;
	}
//Matrix Hermite defined
	private List HERMITEMatrix =
		Matrix.buildMatrix4( 2,-2, 1, 1,
				            -3, 3,-2,-1,
							 0, 0, 1, 0,
							 1, 0, 0, 0);
							 
	private List matrix = HERMITEMatrix;
}
