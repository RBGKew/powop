package org.emonocot.portal.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.emonocot.api.OrganisationService;
import org.emonocot.model.registry.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.base.Strings;

@ControllerAdvice
public class LayoutControllerAdvice {
	private static Logger logger = LoggerFactory.getLogger(LayoutControllerAdvice.class);

	private OrganisationService organisationService;

	/**
	 * @param service Set the organisation service
	 */
	@Autowired
	public void setOrganisationService(final OrganisationService service) {
		organisationService = service;
	}

	private long startUpTime = System.currentTimeMillis();
	private long refreshTime = 0;
	private List<List<Organisation>> footerOrganisations = new ArrayList<List<Organisation>>();

	@ModelAttribute("footerOrganisations")
	public List<List<Organisation>> footerOrganisations() {
		// Cache result for 15 minutes, unless the app has only just started up (for functional testing).
		if ((refreshTime + 15 * 60 * 1000) < System.currentTimeMillis() || (startUpTime + 15 * 60 * 1000 > System.currentTimeMillis())) {
			refreshOrganisations();
		}

		return footerOrganisations;
	}

	/**
	 * Retrives Organisations, and orders them into a table.
	 * Row is index/100, column is index%100, gaps are ignored.
	 */
	protected void refreshOrganisations() {
		logger.debug("Refreshing organisations for page footer");

		// Wrap in try so database problems don't prevent page display
		try {
			List<Organisation> organisations = organisationService.list(null, null, null).getRecords();

			Collections.sort(organisations, new Comparator<Organisation>() {
				@Override
				public int compare(Organisation o1, Organisation o2) {
					if (o1.getFooterLogoPosition() == null && o2.getFooterLogoPosition() == null) return 0;
					if (o1.getFooterLogoPosition() == null) return 1;
					if (o2.getFooterLogoPosition() == null) return -1;
					return o1.getFooterLogoPosition().compareTo(o2.getFooterLogoPosition());
				}
			});

			footerOrganisations.clear();

			List<Organisation> curRow = null;
			int curRowIdx = Integer.MIN_VALUE;
			for (Organisation o : organisations) {
				if (o.getFooterLogoPosition() != null && !Strings.isNullOrEmpty(o.getLogoUrl())) {
					int rowIdx = o.getFooterLogoPosition() / 100;
					if (rowIdx > curRowIdx || curRow == null) {
						curRow = new ArrayList<Organisation>();
						footerOrganisations.add(curRow);
						curRowIdx = rowIdx;
					}
					curRow.add(o);
				}
			}

			refreshTime = System.currentTimeMillis();
		}
		catch (Exception e) {
			logger.error("Error when adding organisations to page footer", e);
		}
	}
}
