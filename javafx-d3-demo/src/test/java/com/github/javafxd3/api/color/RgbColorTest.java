
package com.github.javafxd3.api.color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.javafxd3.api.AbstractTestCase;

/**
 * Tests the class RgbColor 
 */
public class RgbColorTest extends AbstractTestCase {

	
	@Override
	@Test
	public void doTest() {
		
		Colors colors = new Colors(webEngine);
		
		RGBColor rgb = colors.rgb("#ff0000");
		assertEquals(255, rgb.r());
		assertEquals(0, rgb.g());
		assertEquals(0, rgb.b());
		rgb = rgb.darker();
		assertTrue(rgb.r() < 255);
		rgb = rgb.brighter().brighter();
		assertEquals(255, rgb.r());
		HSLColor hsl = rgb.hsl();
		System.out.println(hsl.h());
		System.out.println(hsl.s());
		System.out.println(hsl.l());

		RGBColor rgb2 = colors.rgb(0, 0, 255);
		assertEquals(0, rgb2.r());
		assertEquals(0, rgb2.g());
		assertEquals(255, rgb2.b());
		System.out.println(rgb2.toHexaString());

		RGBColor rgb3 = colors.rgb(hsl);
		assertEquals(255, rgb3.r());
		assertEquals(0, rgb3.g());
		assertEquals(0, rgb3.b());
	}

}