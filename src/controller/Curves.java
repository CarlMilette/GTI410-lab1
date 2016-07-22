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
package controller;

import java.awt.event.MouseEvent;
import java.util.List;

import model.*;
import view.Application;
import view.CurvesPanel;

/**
 * <p>Title: Curves</p>
 * <p>Description: (AbstractTransformer)</p>
 * <p>Copyright: Copyright (c) 2004 S�bastien Bois, Eric Paquette</p>
 * <p>Company: (�TS) - �cole de Technologie Sup�rieure</p>
 * @author unascribed
 * @version $Revision: 1.9 $
 */
public class Curves extends AbstractTransformer implements DocObserver {
		
	/**
	 * Default constructor
	 */
	public Curves() {
		Application.getInstance().getActiveDocument().addObserver(this);
	}	

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_CURVES; }
	
	public void activate() {
		firstPoint = true;
		Document doc = Application.getInstance().getActiveDocument();
		List selectedObjects = doc.getSelectedObjects();
		if (selectedObjects.size() > 0){
			Shape s = (Shape)selectedObjects.get(0);
			if (s instanceof Curve){
				curve = (Curve)s;
				firstPoint = false;
				cp.setCurveType(curve.getCurveType());
				cp.setNumberOfSections(curve.getNumberOfSections());
			}
			else if (s instanceof ControlPoint){
				curve = (Curve)s.getContainer();
				firstPoint = false;
			}
		}
		
		if (firstPoint) {
			// First point means that we will have the first point of a new curve.
			// That new curve has to be constructed.
			curve = new Curve(100,100);
			setCurveType(cp.getCurveType());
			setNumberOfSections(cp.getNumberOfSections());
		}
	}
    
	/**
	 * 
	 */
	protected boolean mouseReleased(MouseEvent e){
		int mouseX = e.getX();
		int mouseY = e.getY();

		if (firstPoint) {
			firstPoint = false;
			Document doc = Application.getInstance().getActiveDocument();
			doc.addObject(curve);
		}
		ControlPoint cp = new ControlPoint(mouseX, mouseY);
		curve.addPoint(cp);
				
		return true;
	}

	/**
	 * @param string
	 */
	public void setCurveType(String string) {
		//Activer la selection du type de courbe
		if (string == CurvesModel.BEZIER) {
			curve.setCurveType(new BezierCurveType(CurvesModel.BEZIER));
		} else if (string == CurvesModel.LINEAR) {
			curve.setCurveType(new PolylineCurveType(CurvesModel.LINEAR));
		} else if (string == CurvesModel.BSPLINE) {
			curve.setCurveType(new BSplineCurveType(CurvesModel.BSPLINE));
		} else if (string == CurvesModel.HERMITE) {
			curve.setCurveType(new HermiteCurveType(CurvesModel.HERMITE));
		} else {
			System.out.println("Curve type [" + string + "] is unknown.");
		}
	}
	
	public void alignControlPoint() {
		if (curve != null) {
			Document doc = Application.getInstance().getActiveDocument();
			List selectedObjects = doc.getSelectedObjects(); 
			if (selectedObjects.size() > 0){
				Shape s = (Shape)selectedObjects.get(0);
				if (curve.getShapes().contains(s)){
					int controlPointIndex = curve.getShapes().indexOf(s);
					System.out.println("Try to apply G1 continuity on control point [" + controlPointIndex + "]");

					//code need to be adapted

					ControlPoint previousControlPoint = (ControlPoint) curve.getShapes().get(controlPointIndex - 1);
					ControlPoint currentControlPoint = (ControlPoint) s;
					ControlPoint nextControlPoint= (ControlPoint) curve.getShapes().get(controlPointIndex + 1);



					float a = Math.abs((currentControlPoint.getCenter().x - nextControlPoint.getCenter().x));
					float b = Math.abs((currentControlPoint.getCenter().y - nextControlPoint.getCenter().y));
					double cCurrentNext = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

					double deltaX = Math.abs((previousControlPoint.getCenter().x - currentControlPoint.getCenter().x));
					double deltaY = Math.abs((previousControlPoint.getCenter().y - currentControlPoint.getCenter().y));
					double cPreviousCurrent= Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

					double xUnitVector = deltaX / cPreviousCurrent;
					double yUnitVector = deltaY / cPreviousCurrent;

					double xDirectionVector = xUnitVector * cCurrentNext * -1.F;
					double yDirectionVector = yUnitVector * cCurrentNext;

					double xNewNextPoint = currentControlPoint.getCenter().x + (xDirectionVector * -1.F);
					double yNewNextPoint = currentControlPoint.getCenter().y + yDirectionVector;

					nextControlPoint.getCenter().x = (int) Math.round(xNewNextPoint);
					nextControlPoint.getCenter().y = (int) Math.round(yNewNextPoint);
					nextControlPoint.notifyObservers();
				}
				else{
					System.out.println("The point is not valid.");
				}
			}
			
		}
	}
	
	public void symetricControlPoint() {
		if (curve != null) {
			Document doc = Application.getInstance().getActiveDocument();
			List selectedObjects = doc.getSelectedObjects(); 
			if (selectedObjects.size() > 0){
				Shape s = (Shape)selectedObjects.get(0);
				if (curve.getShapes().contains(s)){
					int controlPointIndex = curve.getShapes().indexOf(s);
					System.out.println("Try to apply C1 continuity on control point [" + controlPointIndex + "]");

					//Code adapted

					ControlPoint previousControlPoint = (ControlPoint) curve.getShapes().get(controlPointIndex - 1);
					ControlPoint currentControlPoint = (ControlPoint) s;
					ControlPoint nextControlPoint= (ControlPoint) curve.getShapes().get(controlPointIndex + 1);


					float a = Math.abs((currentControlPoint.getCenter().x - nextControlPoint.getCenter().x));
					float b = Math.abs((currentControlPoint.getCenter().y - nextControlPoint.getCenter().y));

					double deltaX = Math.abs((previousControlPoint.getCenter().x - currentControlPoint.getCenter().x));
					double deltaY = Math.abs((previousControlPoint.getCenter().y - currentControlPoint.getCenter().y));
					double cPreviousCurrent= Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

					double xUnitVector = deltaX / cPreviousCurrent;
					double yUnitVector = deltaY / cPreviousCurrent;

					double xDirectionVector = xUnitVector * cPreviousCurrent * -1.F;
					double yDirectionVector = yUnitVector * cPreviousCurrent;

					double xNewNextPoint = currentControlPoint.getCenter().x + (xDirectionVector * -1.F);
					double yNewNextPoint = currentControlPoint.getCenter().y + yDirectionVector;

					nextControlPoint.getCenter().x = (int) Math.round(xNewNextPoint);
					nextControlPoint.getCenter().y = (int) Math.round(yNewNextPoint);
					nextControlPoint.notifyObservers();
				}
			}
			
		}
	}

	public void setNumberOfSections(int n) {
		curve.setNumberOfSections(n);
	}
	
	public int getNumberOfSections() {
		if (curve != null)
			return curve.getNumberOfSections();
		else
			return Curve.DEFAULT_NUMBER_OF_SECTIONS;
	}
	
	public void setCurvesPanel(CurvesPanel cp) {
		this.cp = cp;
	}
	
	/* (non-Javadoc)
	 * @see model.DocObserver#docChanged()
	 */
	public void docChanged() {
	}

	/* (non-Javadoc)
	 * @see model.DocObserver#docSelectionChanged()
	 */
	public void docSelectionChanged() {
		activate();
	}

	private boolean firstPoint = false;
	private Curve curve;
	private CurvesPanel cp;
}
