/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.view.provenance;

import org.emonocot.model.BaseData;
import org.emonocot.model.registry.Organisation;

public class ProvenanceHolderImpl implements ProvenanceHolder, Comparable<ProvenanceHolderImpl>{

	private String license;

	private String rights;

	private String key;

	private Organisation organisation;

	ProvenanceHolderImpl(BaseData data) {
		this.organisation = data.getAuthority();
		this.rights = data.getRights();
		this.license = data.getLicense();
	}

	@Override
	public String getRights() {
		return rights;
	}

	@Override
	public String getLicense() {
		return license;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	@Override
	public int hashCode() {
		String string = this.organisation.getIdentifier() + "|" + nullToEmpty(this.license) + "|" + nullToEmpty(this.rights);
		return string.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}

		if(this == obj) {
			return true;
		}

		if(obj instanceof ProvenanceHolderImpl) {
			ProvenanceHolderImpl prov = (ProvenanceHolderImpl)obj;
			String o1 = this.organisation.getIdentifier() + "|" + nullToEmpty(this.license) + "|" + nullToEmpty(this.rights);
			String o2 = prov.organisation.getIdentifier() + "|" + nullToEmpty(prov.license) + "|" + nullToEmpty(prov.rights);
			return o1.equals(o2);
		} else {
			return false;
		}
	}

	private String nullToEmpty(String string) {
		if(string == null) {
			return "";
		} else if(string.isEmpty()) {
			return "";
		} else {
			return string;
		}
	}

	public static int nullSafeStringComparator(final String one, final String two) {
		if ((one == null || one.isEmpty())  ^ (two == null || two.isEmpty())) {
			return (one == null || one.isEmpty()) ? -1 : 1;
		}

		if ((one == null || one.isEmpty()) && (two == null || two.isEmpty())) {
			return 0;
		}

		return one.compareToIgnoreCase(two);
	}

	@Override
	public int compareTo(ProvenanceHolderImpl o) {
		int i = this.organisation.getIdentifier().compareTo(o.getOrganisation().getIdentifier());

		if(i == 0) {
			int j = nullSafeStringComparator(this.license, o.getLicense());
			if(j == 0) {
				int k = nullSafeStringComparator(this.rights,o.getRights());
				return k;
			} else {
				return j;
			}
		} else {
			return i;
		}
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
