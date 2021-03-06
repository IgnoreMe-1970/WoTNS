/*
 * WoTNS - IdentityTargets.java - Copyright © 2011 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.wotns.main;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.pterodactylus.wotns.freenet.wot.Identity;

/**
 * Scans an {@link Identity}’s properties for WoTNS targets.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class IdentityTargets implements Iterable<Entry<String, String>> {

	/** The identity being scanned. */
	private final Identity identity;

	/** The located targets. */
	private final Map<String, String> targets = new HashMap<String, String>();

	/**
	 * Creates a new target scanner for the given identity.
	 *
	 * @param identity
	 *            The identity to scan for targets
	 */
	public IdentityTargets(Identity identity) {
		this.identity = identity;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the targets of the identity.
	 *
	 * @return The targets defined in the identity
	 */
	public Map<String, String> getTargets() {
		scanForTargets();
		return Collections.unmodifiableMap(targets);
	}

	/**
	 * Returns the target with the given name.
	 *
	 * @param name
	 *            The name of the target
	 * @return The target
	 */
	public String getTarget(String name) {
		scanForTargets();
		return targets.get(name);
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Re-scans the identity for targets.
	 */
	private void scanForTargets() {
		synchronized (targets) {
			targets.clear();
			for (Entry<String, String> property : identity.getProperties().entrySet()) {
				if (property.getKey().startsWith("tns.")) {
					targets.put(property.getKey().substring(4), property.getValue());
				}
			}
		}
	}

	//
	// ITERABLE METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Entry<String, String>> iterator() {
		synchronized (targets) {
			scanForTargets();
			return new HashMap<String, String>(targets).entrySet().iterator();
		}
	}

}
