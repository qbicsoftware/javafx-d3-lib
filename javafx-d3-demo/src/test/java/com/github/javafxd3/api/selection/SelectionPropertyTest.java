package com.github.javafxd3.api.selection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.javafxd3.api.coords.Coords;
import com.github.javafxd3.api.core.Selection;
import com.github.javafxd3.api.core.Value;
import com.github.javafxd3.api.functions.DatumFunction;
import com.github.javafxd3.api.wrapper.D3NodeFactory;
import com.github.javafxd3.api.wrapper.Element;
import com.github.javafxd3.api.wrapper.JavaScriptObject;


@SuppressWarnings("javadoc")
public class SelectionPropertyTest extends AbstractSelectionTest {

	private static final String PROPERTY = "checked";
	
	private static final Double DELTA = 1e-4;

	@Override
	@Test
	public void doTest() {
		testGetter();
		testSetterConstantBoolean();
		testSetterConstantDouble();
		testSetterConstantString();
		testSetterConstantJavascriptObject();
		testSetterFunction();
		testSetterGetter();
	}

	/**
	 * 
	 */
	private void testSetterGetter() {
		// works with single selection
		Selection selection = givenASimpleSelection(new Label());
		final double value = 1.56;
		selection.property(SelectionPropertyTest.PROPERTY, new DatumFunction<Double>() {
			@Override
			public Double apply(final Object context, final Object datum, final int index) {
				return value;
			}
		});
		assertEquals(value, selection.property(SelectionPropertyTest.PROPERTY).asDouble(),DELTA);

		// works with multiple selection
		Selection selection2 = givenAMultipleSelection(new Label(), new Label(), new Label());
		selection2.property(SelectionPropertyTest.PROPERTY, new DatumFunction<Double>() {
			@Override
			public Double apply(final Object context, final Object datum, final int index) {
				return value;
			}
		});
		assertEquals(value, selection.property(SelectionPropertyTest.PROPERTY).asDouble(),DELTA);

	}

	protected void testSetterFunction() {
		// works with single selection
		Selection selection = givenASimpleSelection(new Label());
		final String value = "1";
		selection.property(SelectionPropertyTest.PROPERTY, new DatumFunction<String>() {
			@Override
			public String apply(final Object context, final Object datum, final int index) {
				return value;
			}
		});
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asString());

		// works with multiple selection
		Selection selection2 = givenAMultipleSelection(new Label(), new Label(), new Label());
		selection2.property(SelectionPropertyTest.PROPERTY, new DatumFunction<String>() {
			@Override
			public String apply(final Object context, final Object datum, final int index) {
				return value;
			}
		});
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asString());
		assertEquals(value, getElementProperty(1, SelectionPropertyTest.PROPERTY).asString());
		assertEquals(value, getElementProperty(2, SelectionPropertyTest.PROPERTY).asString());
	}

	protected void testSetterConstantString() {
		// works with single selection
		Selection selection = givenASimpleSelection(new Label());
		String value = "1";
		selection.property(SelectionPropertyTest.PROPERTY, value);
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asString());

		// works with multiple selection
		Selection selection2 = givenAMultipleSelection(new Label(), new Label(), new Label());
		selection2.property(SelectionPropertyTest.PROPERTY, value);
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asString());
		assertEquals(value, getElementProperty(1, SelectionPropertyTest.PROPERTY).asString());
		assertEquals(value, getElementProperty(2, SelectionPropertyTest.PROPERTY).asString());

	}

	protected void testSetterConstantDouble() {
		// works with single selection
		Selection selection = givenASimpleSelection(new Label());
		double value = 3.56;
		selection.property(SelectionPropertyTest.PROPERTY, value);
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asDouble(),DELTA);

		// works with multiple selection
		Selection selection2 = givenAMultipleSelection(new Label(), new Label(), new Label());
		selection2.property(SelectionPropertyTest.PROPERTY, value);
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asDouble(),DELTA);
		assertEquals(value, getElementProperty(1, SelectionPropertyTest.PROPERTY).asDouble(),DELTA);
		assertEquals(value, getElementProperty(2, SelectionPropertyTest.PROPERTY).asDouble(),DELTA);
	}

	protected void testSetterConstantBoolean() {
		boolean value = true;
		// works with single selection
		Selection selection = givenASimpleSelection(new Label());
		selection.property(SelectionPropertyTest.PROPERTY, value);
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asBoolean());

		// works with multiple selection
		Selection selection2 = givenAMultipleSelection(new Label(), new Label(), new Label());
		selection2.property(SelectionPropertyTest.PROPERTY, value);
		assertEquals(value, getElementProperty(0, SelectionPropertyTest.PROPERTY).asBoolean());
		assertEquals(value, getElementProperty(1, SelectionPropertyTest.PROPERTY).asBoolean());
		assertEquals(value, getElementProperty(2, SelectionPropertyTest.PROPERTY).asBoolean());

	}

	protected void testSetterConstantJavascriptObject() {
		
		// works with single selection
		Selection selection = givenASimpleSelection(new Label());
				
		// any object
		JavaScriptObject value = new Coords(webEngine,1.0,2.0);
		
		String propName = "__test__";
		selection.property(propName, value);
		assertEquals(value, getElementProperty(0, propName).as());

		// works with multiple selection
		Selection selection2 = givenAMultipleSelection(new Label(), new Label(), new Label());
		selection2.property(propName, value);
		assertEquals(value, getElementProperty(0, propName).as(Object.class));
		assertEquals(value, getElementProperty(1, propName).as(Object.class));
		assertEquals(value, getElementProperty(2, propName).as(Object.class));

	}

	protected void testGetter() {
		// try a boolean
		Label cb = new Label();
		Selection sel = givenASimpleSelection(cb);
		sel.node().setPropertyBoolean("checked", true);
		assertEquals(true, sel.property("checked").asBoolean());

		// with multiple selection, should return the first element
		Selection selection2 = givenAMultipleSelection(createLabel(SelectionPropertyTest.PROPERTY, "true"), createLabel(SelectionPropertyTest.PROPERTY, "false"),
				createLabel(SelectionPropertyTest.PROPERTY, "false"));
		assertEquals("true", selection2.property(SelectionPropertyTest.PROPERTY).asString());
	}

	private D3NodeFactory createLabel(final String attr, final String value) {
		Label label = new Label();
		label.setPropertyString(attr, value);
		return label;
	}
}