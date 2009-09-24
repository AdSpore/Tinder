/**
 * Copyright (C) 2004-2009 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.xmpp.packet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Verifies {@link JID#resourceprep(String)}.
 * 
 * @author Guus der Kinderen, guus.der.kinderen@gmail.com
 */
public class ResourcePrepTest {

	/**
	 * Basic test that verifies that a string that shouldn't be modified by
	 * resource-prepping gets prepped without a problem.
	 */
	@Test
	public void testValidString() throws Exception {
		// setup
		final String resource = "resource";

		// do magic
		final String result = JID.resourceprep(resource);

		// verify
		assertEquals(resource, result);
	}

	/**
	 * Checks that resource-prepping is case sensitive.
	 */
	@Test
	public void testCaseSensitivity() throws Exception {
		// setup
		final String resource = "rEsOuRcE";

		// do magic
		final String result = JID.resourceprep(resource);

		// verify
		assertEquals(resource, result);
	}

	/**
	 * Verifies that an input value bigger than 1023 bytes will cause an
	 * exception to be thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testToLong() throws Exception {
		// setup
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i + 1 < 1023; i += 2) {
			builder.append('a');
		}
		builder.append('a');
		final String toBig = builder.toString();

		// do magic / verify
		JID.resourceprep(toBig);
	}

	/**
	 * Verifies that Stringprep mapping is correctly executed. This test uses a
	 * 'word joiner' character, which is listed on the B1 table of Stringprep.
	 * Characters on this table must be mapped in resource strings, according to
	 * RFC 3920. This specific character should be mapped to nothing.
	 */
	@Test
	public void testMapping() throws Exception {
		// setup;
		final String input = "word\u2060joiner";

		// do magic
		final String result = JID.resourceprep(input);

		// verify
		assertEquals("wordjoiner", result);
	}

	/**
	 * Checks cache usage, by making sure that a subsequent request returns the
	 * stringprepped answer, not the input data. Input data often equals the
	 * prepped answer, which allows a bug like this to slip by easily.
	 */
	@Test
	public void testCachedResult() throws Exception {
		// setup;
		final String input = "bword\u2060joiner";

		// do magic
		final String result1 = JID.resourceprep(input);
		final String result2 = JID.resourceprep(input);

		// verify
		assertEquals("bwordjoiner", result1);
		assertEquals(result1, result2);
	}
}
