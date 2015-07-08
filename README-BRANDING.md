Portal "Branding" — Developer Information
=========================================

The original eMonocot project has been adapted to demonstrate a possible World Flora Online Portal (WFO), and will be used at Kew for the Plants of the World Online Project (POWOP).

We aim to keep these systems running from the same codebase, so the differences are configurable using a system property.  For Tomcat, this property can be added to global web.xml:

```xml
	<env-entry>
		<env-entry-name>portal</env-entry-name>
		<env-entry-value>world-flora</env-entry-value>
		<env-entry-type>java.lang.String</env-entry-type>
	</env-entry>
```

Valid values are "default" (which means eMonocot) and "world-flora".  At present, the only differences are in the application messages (see emonocot-portal/src/main/webapp/WEB-INF/i18n) and static files (see emonocot-static/src/main).  See the relevant pom.xml for details on how these are built.
