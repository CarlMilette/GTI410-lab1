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

					/*les points de contrôle pour la jonction doivent être déplacés de façon que les deux tangentes
					soient alignées (continuité G1).
					*/
					//Define the position of the points
					ControlPoint courantControlPoint = (ControlPoint) s;
					ControlPoint suivantControlPoint= (ControlPoint) curve.getShapes().get(controlPointIndex + 1);
					ControlPoint precedentControlPoint = (ControlPoint) curve.getShapes().get(controlPointIndex - 1);
					
					//Definir le point de control precedent
					double dx = Math.abs((precedentControlPoint.getCenter().x - courantControlPoint.getCenter().x));
					double dy = Math.abs((precedentControlPoint.getCenter().y - courantControlPoint.getCenter().y));
					double pPrecedentCourant= Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

					//Definir les vecteur unitaires
					double VectorUnitaireX = dx / pPrecedentCourant;
					double VectorUnitaireY = dy / pPrecedentCourant;

					//Definir le point de control courant
					float c1 = Math.abs((courantControlPoint.getCenter().x - suivantControlPoint.getCenter().x));
					float c2 = Math.abs((courantControlPoint.getCenter().y - suivantControlPoint.getCenter().y));
					double pCourantSuivant= Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));

					//Definir les directions des vecteurs
					double VecteurDirectionX = VectorUnitaireX * pCourantSuivant * -1.F;
					double VecteurDirectionY = VectorUnitaireY * pCourantSuivant;

					//Definir les nouvel prochaine point
					double NouvelProchainetPointX = courantControlPoint.getCenter().x + (VecteurDirectionX * -1.F);
					double NouvelProchainetPointY = courantControlPoint.getCenter().y + VecteurDirectionY;

					//Assigner le suivant point de control
					suivantControlPoint.getCenter().x = (int) Math.round(NouvelProchainetPointX);
					suivantControlPoint.getCenter().y = (int) Math.round(NouvelProchainetPointY);
					suivantControlPoint.notifyObservers();
				}
				else{
					System.out.println("select another point: error in this point.");
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

					/*les points de contrôle pour cette jonction doivent être déplacés de façon que les deux tangentes
					soient égales (continuité C1)*/
					//Define the position of the points
					ControlPoint courantControlPoint = (ControlPoint) s;
					ControlPoint suivantControlPoint= (ControlPoint) curve.getShapes().get(controlPointIndex + 1);
					ControlPoint precedentControlPoint = (ControlPoint) curve.getShapes().get(controlPointIndex - 1);

					//Definir le point de control precedent
					double dx = Math.abs((precedentControlPoint.getCenter().x - courantControlPoint.getCenter().x));
					double dy = Math.abs((precedentControlPoint.getCenter().y - courantControlPoint.getCenter().y));
					double pPrecedentCourant = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

					//Definir les vecteur unitaires
					double VectorUnitaireX = dx / pPrecedentCourant;
					double VectorUnitaireY = dy / pPrecedentCourant;

					//Definir les directions des vecteurs
					double VecteurDirectionX = VectorUnitaireX * pPrecedentCourant * -1.F;
					double VecteurDirectionY = VectorUnitaireY * pPrecedentCourant;

					//Definir les nouvel prochaine point
					double NouvelProchainetPointX = courantControlPoint.getCenter().x + (VecteurDirectionX * -1.F);
					double NouvelProchainetPointY = courantControlPoint.getCenter().y + VecteurDirectionY;

					//Assigner le suivant point de control
					suivantControlPoint.getCenter().x = (int) Math.round(NouvelProchainetPointX);
					suivantControlPoint.getCenter().y = (int) Math.round(NouvelProchainetPointY);
					suivantControlPoint.notifyObservers();
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
