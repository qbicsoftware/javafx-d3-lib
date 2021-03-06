package org.treez.javafxd3.d3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.treez.javafxd3.d3.arrays.Array;
import org.treez.javafxd3.d3.arrays.foreach.ForEachCallback;
import org.treez.javafxd3.d3.arrays.foreach.ForEachCallbackWrapper;
import org.treez.javafxd3.d3.behaviour.Behavior;
import org.treez.javafxd3.d3.behaviour.Drag;
import org.treez.javafxd3.d3.behaviour.Drag.DragEvent;
import org.treez.javafxd3.d3.behaviour.Zoom;
import org.treez.javafxd3.d3.behaviour.Zoom.ZoomEvent;
import org.treez.javafxd3.d3.coords.Coords;
import org.treez.javafxd3.d3.core.Formatter;
import org.treez.javafxd3.d3.core.Prefix;
import org.treez.javafxd3.d3.core.Selection;
import org.treez.javafxd3.d3.core.Transform;
import org.treez.javafxd3.d3.core.Transition;
import org.treez.javafxd3.d3.core.Value;
import org.treez.javafxd3.d3.dsv.Dsv;
import org.treez.javafxd3.d3.dsv.DsvCallback;
import org.treez.javafxd3.d3.dsv.DsvObjectAccessor;
import org.treez.javafxd3.d3.event.D3Event;
import org.treez.javafxd3.d3.event.Event;
import org.treez.javafxd3.d3.functions.TimerFunction;
import org.treez.javafxd3.d3.functions.data.wrapper.PlainDataFunction;
import org.treez.javafxd3.d3.geo.Geography;
import org.treez.javafxd3.d3.geom.Geometry;
import org.treez.javafxd3.d3.interpolators.Interpolators;
import org.treez.javafxd3.d3.layout.Layout;
import org.treez.javafxd3.d3.scales.Scales;
import org.treez.javafxd3.d3.svg.SVG;
import org.treez.javafxd3.d3.time.Time;
import org.treez.javafxd3.d3.wrapper.Element;
import org.treez.javafxd3.d3.wrapper.JavaScriptObject;
import org.treez.javafxd3.d3.wrapper.JsArrayMixed;
import org.treez.javafxd3.d3.wrapper.Node;
import org.treez.javafxd3.d3.wrapper.NodeList;
import org.treez.javafxd3.d3.wrapper.Sort;
import org.treez.javafxd3.d3.wrapper.Widget;

import org.treez.javafxd3.d3.core.JsEngine;
import org.treez.javafxd3.d3.core.JsObject;

/**
 * Entry point for D3 api modules. A lot of methods of this class allow access
 * to other classes:
 * <ul>
 * <li>{@link D3#select(Element)} methods and {@link Selection} - manipulate
 * elements in the current document.
 * <li>Interpolation methods are in {@link Interpolators}.
 * <p>
 *
 *
 *
 * 
 *
 */
public class D3 extends JavaScriptObject {

	//#region ATTRIBUTES

	//#end region

	//#region CONSTRUCTORS

	public D3(JsEngine engine) {
		super(engine);
		Objects.requireNonNull(engine);
		Object d3Obj = engine.executeScript("d3");
		JsObject d3 = (JsObject) d3Obj;
		setJsObject(d3);
	}

	//#end region

	//#region METHODS

	/**
	 * @return the version of the d3 API
	 */
	public String version() {
		String result = getMemberForString("version");
		return result;
	};

	/**
	 * The scale factory module.
	 * 
	 * @return
	 */
	public Scales scale() {
		JsObject result = getMember("scale");
		return new Scales(engine, result);
	};

	// =========== select ==============
	/**
	 * Selects the first element that matches the specified selector string,
	 * returning a single-element selection. If no elements in the current
	 * document match the specified selector, returns the empty selection. If
	 * multiple elements match the selector, only the first matching element (in
	 * document traversal order) will be selected.
	 * <p>
	 * The selector is a valid CSS3 selector. For example, you can select by tag
	 * ("div"), class (".awesome"), unique identifier ("#foo"), attribute
	 * ("[color=red]"), or containment ("parent child"). Selectors can also be
	 * intersected (".this.that" for logical AND) or unioned (".this, .that" for
	 * logical OR)
	 *
	 * @param selector
	 *            a CSS3 selector
	 * @return the {@link Selection}
	 */
	public Selection select(String selector) {
		JsObject result = call("select", selector);
		return new Selection(engine, result);
	};

	/**
	 * Selects the specified element. This is useful if you already have a
	 * reference to an element, or a global such as Document#getBody()
	 *
	 * @param element
	 *            the element to select
	 * @return the {@link Selection}
	 */
	public Selection select(Element element) {
		JsObject jsElement=null;
		if(element!=null){
			jsElement = element.getJsObject();
		}		
		JsObject result = call("select", jsElement);
		return new Selection(engine, result);
	};

	/**
	 * Selects the specified widget. This is useful if you already have a
	 * reference to a widget, or a global such as RootPanel#get()
	 *
	 * @param widget
	 *            the widget to select
	 * @return the {@link Selection}
	 */
	public Selection select(final Widget widget) {
		Element element = widget.getElement();
		return select(element);

	}

	// ================ selectAll ================
	/**
	 * Selects all elements that match the specified selector. The elements will
	 * be selected in document traversal order (top-to-bottom). If no elements
	 * in the current document match the specified selector, returns the empty
	 * selection.
	 *
	 * @param selector
	 * @return
	 */
	public Selection selectAll(String selector) {
		JsObject result = evalForJsObject("this.selectAll('" + selector + "')");
		return new Selection(engine, result);
	};

	/**
	 * Selects the list of elements.
	 *
	 * @param nodes
	 *            the elements
	 * @return the selection
	 */
	public Selection selectAll(NodeList<?> nodes) {
		JsObject result = call("selectAll", nodes);
		return new Selection(engine, result);
	};

	/**
	 * Selects the specified array of elements.
	 *
	 * @param nodes
	 *            the elements
	 * @return the selection
	 */
	public Selection selectAll(Element... nodes) {

		JsObject d3JsObject = getD3();

		List<String> fullVarNames = new ArrayList<>();
		List<String> varNames = new ArrayList<>();
		for (Element element : nodes) {
			String varName = createNewTemporaryInstanceName();
			d3JsObject.setMember(varName, element.getJsObject());
			fullVarNames.add("d3." + varName);
			varNames.add(varName);
		}

		String command = "this.selectAll([" + String.join(",", fullVarNames) + "])";
		JsObject result = evalForJsObject(command);

		for (String varName : varNames) {
			d3JsObject.removeMember(varName);
		}

		if (result == null) {
			return null;
		}

		return new Selection(engine, result);

	};

	/**
	 * Selects the specified collection of elements.
	 *
	 * @param nodes
	 *            the elements
	 * @return the selection
	 */
	public final Selection selectAll(final Collection<Element> nodes) {
		Element[] nodeArray = nodes.toArray(new Element[nodes.size()]);
		return selectAll(nodeArray);
	}

	/**
	 * Selects the elements corresponding to the root elements of the widgets in
	 * the specified array.
	 *
	 * @param nodes
	 *            the elements
	 * @return the selection
	 */
	public final Selection selectAll(final Widget... nodes) {
		List<Element> elements = new ArrayList<Element>();
		for (Widget widget : nodes) {
			elements.add(widget.getElement());
		}
		return selectAll(elements);
	}

	/**
	 * Create an animated transition.
	 * <p>
	 * This is equivalent to {@link Selection#transition()
	 * D3.select(document).transition()}. This method is used rarely, as it is
	 * typically easier to derive a transition from an existing selection,
	 * rather than deriving a selection from an existing transition.
	 *
	 *
	 * @return the transition the new transition
	 */
	public Transition transition() {
		JsObject result = call("transition");
		return new Transition(engine, result);
	};

	public Transform transform(String transformString) {
		JsObject result = call("transform", transformString);
		return new Transform(engine, result);
	}

	// =========== Math ==============

	// =========== shuffle ==============

	/**
	 * Randomly shuffle the list of objects provided.
	 * <p>
	 * <i>Note: {@link Collections#shuffle(List)} is not GWT compatible</i>
	 *
	 * @param objects
	 *            the list to shuffle
	 */
	public void shuffle(final List<?> objects) {
		Random random = new Random();
		for (int i = objects.size(); i > 1; i--) {
			swap(objects, i - 1, random.nextInt(i));
		}
	}

	private static final <T> void swap(final List<T> list, final int idx1, final int idx2) {
		T temp = list.get(idx1);
		list.set(idx1, list.get(idx2));
		list.set(idx2, temp);
	}

	/**
	 * @param input
	 */
	public void shuffle(final int[] input) {
		//Shuffle by exchanging each element randomly
		Random random = new Random();

		for (int i = 0; i < input.length; i++) {
			int randomPosition = random.nextInt(input.length);
			int temp = input[i];
			input[i] = input[randomPosition];
			input[randomPosition] = temp;
		}
	}

	/**
	 * @param input
	 */
	public void shuffle(final char[] input) {
		//Shuffle by exchanging each element randomly
		Random random = new Random();

		for (int i = 0; i < input.length; i++) {
			int randomPosition = random.nextInt(input.length);
			char temp = input[i];
			input[i] = input[randomPosition];
			input[randomPosition] = temp;
		}
	}

	// ================ Colors ================

	// =========== svg ==============
	/**
	 * @return the svg module
	 */
	public SVG svg() {
		JsObject result = getMember("svg");
		if (result == null) {
			return null;
		}
		return new SVG(engine, result);
	};

	// =========== layouts ==============
	/**
	 * @return the layout module
	 */
	public Layout layout() {
		JsObject result = getMember("layout");
		if (result == null) {
			return null;
		}
		return new Layout(engine, result);
	};

	/**
	 * @return the {@link Geometry} module
	 */
	public Geometry geom() {
		JsObject result = getMember("geom");
		if (result == null) {
			return null;
		}
		return new Geometry(engine, result);
	};

	/**
	 * @return the {@link Geography} module
	 */
	public Geography geo() {
		JsObject result = getMember("geo");
		if (result == null) {
			return null;
		}
		return new Geography(engine, result);
	};

	// =========== interpolation ==============

	// =========== ease ==============
	// cf Easing

	// =========== timer ==============

	/**
	 * Alias for {@link #timer(TimerFunction, int)} with a delay equals to 0.
	 *
	 * @param command
	 *            the command to be executed until it returns true.
	 */
	public void timer(TimerFunction timerFunction) {

		assertObjectIsNotAnonymous(timerFunction);

		String methodName = createNewTemporaryInstanceName();
		JsObject d3JsObject = getD3();

		Object method = timerFunction;
		boolean isJavaScriptObject = timerFunction instanceof JavaScriptObject;
		if (isJavaScriptObject) {
			JavaScriptObject javaScriptObject = (JavaScriptObject) timerFunction;
			method = javaScriptObject.getJsObject();
		}

		d3JsObject.setMember(methodName, method);

		String command = "d3.timer(function() { " //
				+ "return d3." + methodName + ".execute();" //
				+ "});";
		eval(command);
	};

	/**
	 * Alias for {@link #timer(TimerFunction, int, int)} with a mark equals to
	 * the "now" timestamp (i.e <code>new Date().getTime()</code>).
	 *
	 * @param command
	 *            the command to be executed until it returns true.
	 * @param delayMillis
	 *            the delay to expires before the command should start being
	 *            invoked (may be negative if markMillis is in the future)
	 */
	public void timer(TimerFunction timerFunction, int delayMillis) {

		assertObjectIsNotAnonymous(timerFunction);

		String funcName = createNewTemporaryInstanceName();
		JsObject d3JsObject = getD3();
		d3JsObject.setMember(funcName, timerFunction);

		String command = "this.timer(function(d, i) { return d3." + funcName + ".execute();}, " + delayMillis + ");";
		eval(command);
	};

	/**
	 * Start a custom animation timer, invoking the specified
	 * {@link TimerFunction} repeatedly until it returns true.
	 * <p>
	 * There is no way to cancel the timer after it starts, so make sure your
	 * timer function returns true when done!
	 *
	 * The optional numeric delay [unit: integer, millisecond] may be specified
	 * when the given function should only start to be invoked after delay
	 * milliseconds have expired after the specified mark timestamp [unit:
	 * integer, milliseconds since epoch].
	 * <p>
	 * When mark is omitted, Date.now() is assumed instead. Otherwise, you may
	 * use Date.getTime to convert your Date object to a suitable mark
	 * timestamp.
	 * <p>
	 * You may use delay and mark to specify relative and absolute moments in
	 * time when the function should start being invoked, e.g. a calendar-based
	 * event might be coded as
	 *
	 * <pre>
	 * {@code
	 * 	Date appointment = new Date(2012, 09, 29, 14, 0, 0); // @ 29/sep/2012, 1400 hours
	 * ...
	 * // flash appointment on screen when it's due in 4 hours or less: note that negative (delay) is okay!
	 * d3.timer(flash_appointments_due, -4 * 3600 * 1000, appointment);
	 * }
	 * </pre>
	 *
	 * @param command
	 *            the command to be executed until it returns true.
	 * @param delayMillis
	 *            the delay to expires before the command should start being
	 *            invoked (may be negative if markMillis is in the future)
	 * @param markMillis
	 *            the timestamp from which the delay starts
	 */
	public void timer(TimerFunction timerFunction, int delayMillis, int markMillis) {

		assertObjectIsNotAnonymous(timerFunction);

		String funcName = createNewTemporaryInstanceName();
		JsObject d3JsObject = getD3();
		d3JsObject.setMember(funcName, timerFunction);

		String command = "this.timer(function(d, i) { return d3." + funcName + ".execute();}, " + delayMillis + ", "
				+ markMillis + ");";
		eval(command);

	};

	/**
	 * Immediately execute (invoke once) any active timers.
	 * <p>
	 * Normally, zero-delay transitions are executed after an instantaneous
	 * delay (<10ms).
	 * <p>
	 * This can cause a brief flicker if the browser renders the page twice:
	 * once at the end of the first event loop, then again immediately on the
	 * first timer callback.
	 * <p>
	 * By flushing the timer queue at the end of the first event loop, you can
	 * run any zero-delay transitions immediately and avoid the flicker.
	 * <p>
	 * Note: the original method is d3.timer.flush() but has been pushed here
	 * because the timer API is limited to this method
	 *
	 */
	public void timerFlush() {
		call("timer.flush");
	};

	// =========== time ==============

	/**
	 * @return the time module
	 */
	public Time time() {
		JsObject result = getMember("time");
		return new Time(engine, result);
	};

	// ========= events and interactions ============
	/**
	 * Retrieve the current event, if any.
	 * <p>
	 * This global variable exists during an event listener callback registered
	 * with the on operator. The current event is reset after the listener is
	 * notified in a finally block. This allows the listener function to have
	 * the same form as other operator functions, being passed the current datum
	 * d and index i.
	 * <p>
	 * The {@link D3#event()} object is a DOM event and implements the standard
	 * event fields like timeStamp and keyCode as well as methods like
	 * preventDefault() and stopPropagation(). While you can use the native
	 * event's pageX and pageY, it is often more convenient to transform the
	 * event position to the local coordinate system of the container that
	 * received the event. For example, if you embed an SVG in the normal flow
	 * of your page, you may want the event position relative to the top-left
	 * corner of the SVG image. If your SVG contains transforms, you might also
	 * want to know the position of the event relative to those transforms.
	 * <p>
	 * Use the d3.mouse operator for the standard mouse pointer, and use
	 * d3.touches for multitouch events on iOS.
	 * <p>
	 * You may get an instance of {@link D3Event} by an auto-casting call:
	 *
	 * <pre>
	 * <code>
	 *     D3Event d3e = D3.event();
	 *     //allow a call to d3e.sourceEvent();
	 * </code>
	 * </pre>
	 *
	 *
	 * @return the instance of {@link Event}
	 */
	public Event event() {
		JsObject result = getMember("event");
		if(result==null){
			return null;
		}
		return new Event(engine, result);
	};

	/**
	 * Retrieve the current event if any, as a {@link Coords} object containing
	 * the x and y of the mouse. This is useful when using {@link Drag}
	 * behavior.
	 *
	 * @return the current event as a Coords object
	 */
	public Coords eventAsCoords() {

		Object xObj = eval("d3.event.x");
		Object yObj = eval("d3.event.y");
		Double x = Double.parseDouble(xObj.toString());
		Double y = Double.parseDouble(yObj.toString());

		return new Coords(engine, x, y);
	};

	/**
	 * Retrieve the current event if any, as a {@link Coords} object containing
	 * the dx and dy representing the element's coordinates relative to its
	 * position at the beginning of the gesture. This is useful when using
	 * {@link Drag} behavior.
	 *
	 * @return the current event as a Coords object
	 */
	public Coords eventAsDCoords() {
		Object dxObj = eval("d3.event.dx");
		Object dyObj = eval("d3.event.dy");
		Double dx = Double.parseDouble(dxObj.toString());
		Double dy = Double.parseDouble(dyObj.toString());

		return new Coords(engine, dx, dy);
	};

	/**
	 * Returns the x and y coordinates of the current d3.event, relative to the
	 * specified container. The container may be an HTML or SVG container
	 * element, such as an svg:g or svg:svg. The coordinates are returned as a
	 * two-element array [ x, y].
	 *
	 * @param container
	 * @return
	 */
	public Array<Double> mouse(Node container) {
		JsObject jsContainer = container.getJsObject();
		JsObject result = call("mouse", jsContainer);
		return new Array<Double>(engine, result);
	};

	/**
	 * Returns the x and y coordinates of the current d3.event, relative to the
	 * specified container. The container may be an HTML or SVG container
	 * element, such as an svg:g or svg:svg. The coordinates are returned as a
	 * two-element array [ x, y].
	 *
	 * @param container
	 * @return
	 */
	public Coords mouseAsCoords(Node container) {
		JsObject containerObject = container.getJsObject();
		JsObject coordObj = call("mouse", containerObject);
		Double x = (Double) coordObj.call("m", 0);
		Double y = (Double) coordObj.call("m", 1);
		return new Coords(engine, x, y);
	};

	/**
	 * Returns the x coordinate of the current d3.event, relative to the
	 * specified container. The container may be an HTML or SVG container
	 * element, such as an svg:g or svg:svg. The coordinates are returned as a
	 * two-element array [ x, y].
	 *
	 * @param container
	 * @return
	 */
	public double mouseX(Node container) {
		JsObject containerObject = container.getJsObject();
		JsObject coordObj = call("mouse", containerObject);
		Object result = coordObj.getMember("0");
		Double x = Double.parseDouble("" + result);
		return x;
	};

	/**
	 * Returns the y coordinate of the current d3.event, relative to the
	 * specified container. The container may be an HTML or SVG container
	 * element, such as an svg:g or svg:svg. The coordinates are returned as a
	 * two-element array [ x, y].
	 *
	 * @param container
	 * @return
	 */
	public double mouseY(Node container) {
		JsObject containerObject = container.getJsObject();
		JsObject coordObj = call("mouse", containerObject);
		Object result = coordObj.getMember("1");
		Double y = Double.parseDouble("" + result);
		return y;
	};

	/**
	 * Returns the x and y coordinates of each touch associated with the current
	 * d3.event, based on the touches attribute, relative to the specified
	 * container.
	 * <p>
	 * The container may be an HTML or SVG container element, such as an svg:g
	 * or svg:svg.
	 * <p>
	 * The coordinates are returned as an array of two-element arrays [ [ x1,
	 * y1], [ x2, y2], … ].
	 * <p>
	 *
	 * @param container
	 *            the node to get the coords relative to
	 * @return an array of array of 2 elements.
	 */
	public JsArrayMixed touches(Node container) {
		JsObject containerObject = container.getJsObject();
		JsObject result = call("touches", containerObject);
		return new JsArrayMixed(engine, result);
	};

	// =========== csv ==============

	/**
	 * @return the CSV module
	 */
	public <T> Dsv<T> csv() {
		JsObject result = getMember("csv"); //call("dsvFormat", ",");
		return new Dsv<T>(engine, result);
	};

	/**
	 * Issues an HTTP GET request for the comma-separated values (CSV) file at
	 * the specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/csv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request. When
	 * the CSV data is available, the specified callback will be invoked with
	 * the parsed rows as the argument. If an error occurs, the callback
	 * function will instead be invoked with null.
	 * 
	 * @param url
	 * @param callback
	 * @return
	 */
	public <T> Dsv<T> csv(String url, DsvCallback<T> callback) {

		assertObjectIsNotAnonymous(callback);

		String callbackName = createNewTemporaryInstanceName();
		JsObject jsObj = getJsObject();
		jsObj.setMember(callbackName, callback);

		String command = "this.csv('" + url + "', function(error, rows) { " //
				+ "  this." + callbackName + ".get(error, rows); " //
				+ "});";
		JsObject result = evalForJsObject(command);
		return new Dsv<T>(engine, result);
	};

	/**
	 * Issues an HTTP GET request for the comma-separated values (CSV) file at
	 * the specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/csv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request. When
	 * the CSV data is available, the specified callback will be invoked with
	 * the parsed rows as the argument. If an error occurs, the callback
	 * function will instead be invoked with null. The accessor may be
	 * specified, which is then passed to d3.csv.parse; the accessor may also be
	 * specified by using the return request object’s row function.
	 * 
	 * @param url
	 * @param accessor
	 * @param callback
	 * @return
	 */
	public <T> Dsv<T> csv(String url, DsvObjectAccessor<T> accessor, DsvCallback<T> callback) {

		assertObjectIsNotAnonymous(accessor);
		assertObjectIsNotAnonymous(callback);

		String accessorMemberName = createNewTemporaryInstanceName();
		String callbackName = createNewTemporaryInstanceName();

		JsObject jsObj = getJsObject();
		jsObj.setMember(accessorMemberName, accessor);
		jsObj.setMember(callbackName, callback);

		String command = "this.csv('" + url + "', function(row, index) { " //			
				+ "  return this." + accessorMemberName + ".apply(row, index);" //
				+ " }, " + "function(error, rows) { " //			
				+ "  this." + callbackName + ".get(error, rows); " //
				+ "});";
		JsObject result = evalForJsObject(command);
		return new Dsv<T>(engine, result);

	};

	/**
	 * Issues an HTTP GET request for the comma-separated values (CSV) file at
	 * the specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/csv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request. When
	 * the CSV data is available, the specified callback will be invoked with
	 * the parsed rows as the argument. If an error occurs, the callback
	 * function will instead be invoked with null. The accessor may be
	 * specified, which is then passed to d3.csv.parse; the accessor may also be
	 * specified by using the return request object’s row function.
	 * 
	 * @param url
	 * @param accessor
	 * @return
	 */
	public <T> Dsv<T> csv(String url, DsvObjectAccessor<T> accessor) {

		assertObjectIsNotAnonymous(accessor);

		String accessorMemberName = createNewTemporaryInstanceName();

		JsObject jsObj = getJsObject();
		jsObj.setMember(accessorMemberName, accessor);

		String command = "this.csv('" + url + "', function(row, index) { " //				
				+ "  return this." + accessorMemberName + ".apply(row, index);" //
				+ " });";
		JsObject result = evalForJsObject(command);
		return new Dsv<T>(engine, result);
	};

	/**
	 * Issues an HTTP GET request for the comma-separated values (CSV) file at
	 * the specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/csv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request.
	 * 
	 * @param url
	 * @return
	 */
	public <T> Dsv<T> csv(String url) {
		JsObject result = call("csv", url);
		return new Dsv<T>(engine, result);
	};

	// =========== tsv ==============

	/**
	 * @return the TSV module
	 */
	public <T> Dsv<T> tsv() {
		JsObject result = getMember("tsv"); //call("dsvFormat", "\t");
		if (result == null) {
			throw new IllegalStateException("Could not get tsv");
		}
		return new Dsv<T>(engine, result);
	};

	/**
	 * Issues an HTTP GET request for the comma-separated values (TSV) file at
	 * the specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/tsv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request. When
	 * the TSV data is available, the specified callback will be invoked with
	 * the parsed rows as the argument. If an error occurs, the callback
	 * function will instead be invoked with null.
	 * 
	 * @param url
	 * @param callback
	 * @return
	 */
	public <T> Dsv<T> tsv(String url, DsvCallback<T> callback) {

		assertObjectIsNotAnonymous(callback);

		String callbackName = createNewTemporaryInstanceName();
		JsObject jsObj = getJsObject();
		jsObj.setMember(callbackName, callback);

		String command = "this.tsv('" + url + "', function(error, rows) { " //
				+ "  this." + callbackName + ".get(error, rows); " //
				+ "});";
		JsObject result = evalForJsObject(command);
		return new Dsv<T>(engine, result);

	};

	/**
	 * Issues an HTTP GET request for the tab-separated values (TSV) file at the
	 * specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/tsv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request. When
	 * the TSV data is available, the specified callback will be invoked with
	 * the parsed rows as the argument. If an error occurs, the callback
	 * function will instead be invoked with null. The accessor may be
	 * specified, which is then passed to d3.tsv.parse; the accessor may also be
	 * specified by using the return request object’s row function.
	 * 
	 * @param url
	 * @param accessor
	 * @param callback
	 * @return
	 */
	public <T> Dsv<T> tsv(String url, DsvObjectAccessor<T> accessor, DsvCallback<T> callback) {

		assertObjectIsNotAnonymous(accessor);
		assertObjectIsNotAnonymous(callback);

		String accessorMemberName = createNewTemporaryInstanceName();
		String callbackName = createNewTemporaryInstanceName();

		JsObject jsObj = getJsObject();
		jsObj.setMember(accessorMemberName, accessor);
		jsObj.setMember(callbackName, callback);

		String command = "this.tsv('" + url + "', function(row, index) { " //		
				+ "alert('inside tsv callback'); " //
				+ "  return this." + accessorMemberName + ".apply(row, index);" //
				+ " }, " + "function(error, rows) { " //			
				+ "  this." + callbackName + ".get(error, rows); " //
				+ "});";
		JsObject result = evalForJsObject(command);
		return new Dsv<T>(engine, result);

	};

	/**
	 * Issues an HTTP GET request for the comma-separated values (TSV) file at
	 * the specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/tsv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request. When
	 * the TSV data is available, the specified callback will be invoked with
	 * the parsed rows as the argument. If an error occurs, the callback
	 * function will instead be invoked with null. The accessor may be
	 * specified, which is then passed to d3.tsv.parse; the accessor may also be
	 * specified by using the return request object’s row function.
	 * 
	 * @param url
	 * @param accessor
	 * @return
	 */
	public <T> Dsv<T> tsv(String url, DsvObjectAccessor<T> accessor) {

		assertObjectIsNotAnonymous(accessor);

		String accessorMemberName = createNewTemporaryInstanceName();

		JsObject jsObj = getJsObject();
		jsObj.setMember(accessorMemberName, accessor);

		String command = "this.tsv('" + url + "', function(row, index) { " //				
				+ "  return this." + accessorMemberName + ".apply(row, index);" //
				+ " });";
		JsObject result = evalForJsObject(command);
		return new Dsv<T>(engine, result);

	};

	/**
	 * Issues an HTTP GET request for the comma-separated values (TSV) file at
	 * the specified url.
	 * <p>
	 * The file contents are assumed to be RFC4180-compliant. The mime type of
	 * the request will be "text/tsv". The request is processed asynchronously,
	 * such that this method returns immediately after opening the request.
	 * 
	 * @param url
	 * @return
	 */
	public <T> Dsv<T> tsv(String url) {
		JsObject result = call("tsv", url);
		return new Dsv<T>(engine, result);
	};

	// ============= json ============

	// ============= array ============

	/**
	 * Returns an array containing the property names of the specified object
	 * (an associative array). The order of the returned array is undefined.
	 * <p>
	 *
	 * @param object
	 *            the object to convert to an array
	 * @return an array containing the property names.
	 */
	public <T> Array<String> keys(JavaScriptObject object) {
		JsObject result = call("keys", object.getJsObject());
		return new Array<String>(engine, result);
	};

	// =================== format methods ====================

	/**
	 * Returns a new {@link Formatter} function with the given string specifier.
	 * A format function takes a number as the only argument, and returns a
	 * string representing the formatted number. Please see {@link Formatter}
	 * javadoc for the specifier specification.
	 *
	 * @see <a href= "https:d3_format">D3. js official documentation</a>
	 * @param specifier
	 *            the given string specifier.
	 * @return the format function.
	 */
	public Formatter format(String specifier) {
		JsObject result = call("format", specifier);
		return new Formatter(engine, result);
	};

	/**
	 * Return the SI Prefix for the specified value at the specified precision.
	 * <p>
	 * This method is used by {@link #format(String)} for the %s format.
	 *
	 * @param value
	 *            the value to be prefixed
	 * @param precision
	 *            the precision
	 * @return the prefix
	 */
	public Prefix formatPrefix(double value, double precision) {
		JsObject result = call("formatPrefix", value, precision);
		return new Prefix(engine, result);
	};

	/**
	 * Returns the value x rounded to n digits after the decimal point. If n is
	 * omitted, it defaults to zero. The result is a number. Values are rounded
	 * to the closest multiple of 10 to the power minus n; if two multiples are
	 * equally close, the value is rounded up in accordance with the built-in
	 * round function. Note that the resulting number when converted to a string
	 * may be imprecise due to IEEE floating point precision; to format a number
	 * to a string with a fixed number of decimal points, use d3.format instead.
	 *
	 * @param value
	 * @param digits
	 * @return
	 */
	public double round(double value, int digits) {
		Double result = callForDouble("round", value, digits);
		return result;
	};

	/**
	 * Returns a quoted (escaped) version of the specified string such that the
	 * string may be embedded in a regular expression as a string literal.
	 *
	 * @param string
	 *            the input string
	 * @return the regexp escaped string
	 */
	public String requote(String string) {
		String result = callForString("requote", string);
		return result;
	};

	// =========== range ===================

	public Array<Double> range(double stop) {
		JsObject result = call("range", stop);
		return new Array<Double>(engine, result);
	}

	public Array<Double> range(double start, double stop) {
		JsObject result = call("range", start, stop);
		return new Array<Double>(engine, result);
	}

	public Array<Double> range(double start, double stop, double step) {
		JsObject result = call("range", start, stop, step);
		return new Array<Double>(engine, result);
	}

	// =========== behaviours ==============
	/**
	 * @return the behaviour module
	 */
	public Behavior behavior() {
		JsObject result = getMember("behavior");
		return new Behavior(engine, result);
	};

	/**
	 *
	 * Get the ZoomEvent from within a {@link Zoom} listener.
	 *
	 * @return the zoom event
	 */

	public final ZoomEvent zoomEvent() {

		Event event = event();
		if (event == null) {
			return null;
		}

		JsObject jsEvent = event.getJsObject();

		return new ZoomEvent(engine, jsEvent);
	}

	/**
	 *
	 * Get the DragEvent from within a {@link Drag} listener.
	 *
	 * @return the drag event
	 */

	public final DragEvent dragEvent() {

		Event event = event();
		if (event == null) {
			return null;
		}

		JsObject jsEvent = event.getJsObject();

		return new DragEvent(engine, jsEvent);

	}

	// =========== misc ==========
	/**
	 * Return the identity function:
	 * <p>
	 * <code>function(d) { return d; }</code>
	 *
	 * @return the identity function
	 */
	public JsObject identity() {

		String command = "var identity = function(d) { return d; }";
		eval(command);
		JsObject result = evalForJsObject("identity");
		return result;
	}
	
	public void saveSvg(){
		call("saveSvg");
	}
	
	public void saveSvg(String filePath){
		call("saveSvgTo", filePath);
	}

	/**
	 * Creates a new variable in the JavaScript space and applies the given
	 * JsObject to assign a corresponding value
	 * 
	 * @param variableName
	 * @param value
	 */
	public void createJsVariable(String variableName, JavaScriptObject value) {
		JsObject valueObject = value.getJsObject();
		createJsVariable(variableName, valueObject);
	}

	/**
	 * Creates a new variable in the JavaScript space and applies the given
	 * JsObject to assign a corresponding value
	 * 
	 * @param variableName
	 * @param value
	 */
	public void createJsVariable(String variableName, JsObject value) {
		//store value in temporary dummy attribute
		String tempAttributeName = "tempDummyStorageAttribute";
		JsObject window = (JsObject) engine.executeScript("window");
		window.setMember(tempAttributeName, value);

		//create new variable and assign value to it    	
		String createCommand = "var " + variableName + " = window.tempDummyStorageAttribute;";
		eval(createCommand);

		//delete temporary dummy attribute
		window.removeMember(tempAttributeName);

		//Object val = eval(variableName);
		//boolean isOk = val.equals(value);
	}

	public Sort ascending() {
		JsObject result = getMember("ascending");
		return new Sort(engine, result);
	}

	public Sort descending() {
		JsObject result = getMember("descending");
		return new Sort(engine, result);
	}

	public <T> Array<T> extent(Array<T> array) {

		if (array.length() > 0) {
			Object firstElement = array.get(0, Object.class);
			boolean isJavaScriptObject = firstElement instanceof JavaScriptObject;
			if (isJavaScriptObject) {
				List<JsObject> elementList = extractJsObjectElements(array);
				return extent(elementList);
			}
		}

		JsObject result = call("extent", array.getJsObject());

		if (result == null) {
			return null;
		}
		return new Array<>(engine, result);
	}

	private <T> Array<T> extent(Collection<JsObject> elements) {

		JsObject d3JsObject = getD3();

		List<String> fullVarNames = new ArrayList<>();
		List<String> varNames = new ArrayList<>();
		for (JsObject jsObject : elements) {
			String varName = createNewTemporaryInstanceName();
			d3JsObject.setMember(varName, jsObject);
			fullVarNames.add("d3." + varName);
			varNames.add(varName);
		}

		String command = "this.extent([" + String.join(",", fullVarNames) + "])";
		JsObject result = evalForJsObject(command);

		for (String varName : varNames) {
			d3JsObject.removeMember(varName);
		}

		if (result == null) {
			return null;
		}

		return new Array<>(engine, result);
	}

	public <R, A> Array<R> extent(Array<A> array, Class<A> argumentClass, PlainDataFunction<R, A> accessor) {

		ForEachCallback<R> accessorWrapper = new ForEachCallbackWrapper<>(argumentClass, engine, accessor);
		return extent(array, accessorWrapper);
	}

	public <R, A> Array<R> extent(Array<A> array, ForEachCallback<R> accessor) {

		assertObjectIsNotAnonymous(accessor);

		String arrayMemberName = createNewTemporaryInstanceName();
		String accessorMemberName = createNewTemporaryInstanceName();

		JsObject jsObj = getJsObject();
		jsObj.setMember(arrayMemberName, array.getJsObject());
		jsObj.setMember(accessorMemberName, accessor);

		String command = "d3.extent(d3." + arrayMemberName + ", function(d, index, array) { " + //
				"   return d3." + accessorMemberName + ".forEach(this, d, index, array);" + //
				"}); ";

		JsObject result = evalForJsObject(command);

		jsObj.removeMember(arrayMemberName);
		jsObj.removeMember(accessorMemberName);

		if (result == null) {
			return null;
		}
		return new Array<>(engine, result);

	}

	private <T> List<JsObject> extractJsObjectElements(Array<T> array) {
		List<JsObject> elementList = new ArrayList<>();
		array.forEach((object) -> {
			JavaScriptObject wrapper = (JavaScriptObject) object;
			JsObject rawElement = wrapper.getJsObject();
			elementList.add(rawElement);
		});
		return elementList;
	}

	public Value max(Array<?> array) {
		JsObject result = call("max", array.getJsObject());
		if (result == null) {
			return null;
		}
		return new Value(engine, result);
	}

	public Value max(Array<?> array, ForEachCallback<?> accessor) {

		assertObjectIsNotAnonymous(accessor);

		String arrayMemberName = createNewTemporaryInstanceName();
		String accessorMemberName = createNewTemporaryInstanceName();

		JsObject jsObj = getJsObject();
		jsObj.setMember(arrayMemberName, array.getJsObject());
		jsObj.setMember(accessorMemberName, accessor);

		String command = "d3.max(d3." + arrayMemberName + ", function(d, index, array) { " + //
				"   return d3." + accessorMemberName + ".forEach(this, d, index, array);" + //
				"}); ";

		Object valueResult = eval(command);

		jsObj.removeMember(arrayMemberName);
		jsObj.removeMember(accessorMemberName);

		if (valueResult == null) {
			return null;
		}
		return Value.create(engine, valueResult);

	}

	public Value min(Array<?> array) {
		JsObject result = call("min", array.getJsObject());
		if (result == null) {
			return null;
		}
		return new Value(engine, result);
	}
	
	public int logNumberOfTempVars() {
		return logNumberOfTempVars("");
	}

	public int logNumberOfTempVars(String identifier) {

		String command = "var attributeNames = Object.getOwnPropertyNames(this);" + //
				"var temp_count = 0;" + //
				"var prefix = 'temp__instance__';" + //
				"var prefixLength = prefix.length;" + //
				"for(var index=0; index<attributeNames.length; index++){" + //
				" var attributeName = attributeNames[index];  " + //	
				"   if (attributeName.length > prefixLength){" + //
				"     if(attributeName.substring(0, prefixLength) == prefix){" + //
				"       temp_count++;" + //
				"     }" + //
				"   }" + //
				"}";
		eval(command);
		int count = evalForInteger("temp_count");
		eval("temp_count=undefined; attributeNames=undefined;");

		System.out.println("D3 =>Number of temporary D3 variables("+identifier+"): " + count);
		return count;

	}

	public void clearTempVars() {

		String command = "var attributeNames = Object.getOwnPropertyNames(this);" + //		
				"for(var index=0; index<attributeNames.length; index++){" + //
				" var attributeName = attributeNames[index];  " + //
				"   if(attributeName.includes('temp__instance__')){" + //
				"     this[attributeName]=undefined;" + //
				"   }" + //
				"}" + //
				"attributeNames=undefined;";
		eval(command);

	}

	//#end region

	//#region ACCESSORS

	/**
	 * @return
	 */
	public JsEngine getJsEngine() {
		return engine;
	}

	//#end region

}
