package org.xmpp.packet;

import org.junit.Test;

/**
 * Tests compliance of {@link JID} with the restrictions defined in RFC-3920 for
 * the node identifier.
 * 
 * @author Guus der Kinderen, guus.der.kinderen@gmail.com
 * @see <a href="http://www.ietf.org/rfc/rfc3920.txt">RFC 3920 - Extensible
 *      Messaging and Presence Protocol (XMPP): Core</a>
 */
public class JIDCreationNodeTest {

	/**
	 * A domain identifier that's RFC 3920 valid.
	 */
	public static final String DOMAIN = "domain";

	/**
	 * A resource identifier that's RFC 3920 valid.
	 */
	public static final String RESOURCE = "resource";

	/**
	 * Node identifiers are an optional part of a JID. This testcase verifies
	 * that node identifiers can be left out of the creation of a JID.
	 */
	@Test
	public void testOptinality() throws Exception {
		new JID(DOMAIN);
		new JID(null, DOMAIN, null);
		new JID(null, DOMAIN, RESOURCE);
		new JID("", DOMAIN, RESOURCE);
	}

	/**
	 * The maximum size of the node identifier is 1023 bytes (note: bytes, not
	 * characters!). This test verifies that using as much characters as
	 * possible without crossing the 1023 byte boundry, will not cause a
	 * problem.
	 */
	@Test
	public void testMaximumSize() throws Exception {
		// setup
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i + 1 < 1023; i += 2) {
			builder.append('a');
		}
		final String longestPossibleValue = builder.toString();

		// do magic / verify
		new JID(longestPossibleValue, DOMAIN, RESOURCE);
	}

	/**
	 * The maximum size of the node identifier is 1023 bytes (note: bytes, not
	 * characters!). This test verifies that using more bytes will cause an
	 * exception to be thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testOverMaximumSize() throws Exception {
		// setup
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i + 1 < 1023; i += 2) {
			builder.append('a');
		}
		builder.append('a');
		final String toBig = builder.toString();

		// do magic / verify
		new JID(toBig, DOMAIN, RESOURCE);
	}
}
