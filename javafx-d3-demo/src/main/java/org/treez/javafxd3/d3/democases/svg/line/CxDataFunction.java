package org.treez.javafxd3.d3.democases.svg.line;

import org.treez.javafxd3.d3.core.Value;
import org.treez.javafxd3.d3.functions.DataFunction;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class CxDataFunction implements DataFunction<Double> {

	//#region ATTRIBUTES

	private WebEngine webEngine;

	//#end region

	//#region CONSTRUCTORS

	public CxDataFunction(WebEngine webEngine) {
		this.webEngine = webEngine;
	}

	//#end region

	@Override
	public Double apply(Object context, Object value, int index) {

		JSObject datum = (JSObject) value;		
		Value valueObj = new Value(webEngine, datum);
		
		CustomCoords coords = valueObj.<CustomCoords> as(CustomCoords.class);
		if (coords!=null){
			Double x =  coords.x();
			return x;
		}

		return null;
	}

	public Double apply(String context, String d, int index) {
		return null;
	}
}