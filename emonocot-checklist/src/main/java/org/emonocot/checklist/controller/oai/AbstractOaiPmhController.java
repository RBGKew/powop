package org.emonocot.checklist.controller.oai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.persistence.IdentifiableService;
import org.emonocot.model.marshall.xml.DateTimeConverter;
import org.emonocot.model.pager.Page;
import org.openarchives.pmh.BadResumptionTokenException;
import org.openarchives.pmh.CannotDisseminateFormatException;
import org.openarchives.pmh.DeletedRecord;
import org.openarchives.pmh.Error;
import org.openarchives.pmh.ErrorCode;
import org.openarchives.pmh.Granularity;
import org.openarchives.pmh.IdDoesNotExistException;
import org.openarchives.pmh.Identify;
import org.openarchives.pmh.MetadataFormat;
import org.openarchives.pmh.MetadataPrefix;
import org.openarchives.pmh.NoRecordsMatchException;
import org.openarchives.pmh.NoSetHierarchyException;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.ResumptionToken;
import org.openarchives.pmh.Verb;
import org.openarchives.pmh.format.annotation.MetadataPrefixFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
public abstract class AbstractOaiPmhController<T extends IdentifiableEntity, SERVICE extends IdentifiableService<T>> {

    /**
     * Logger for debugging requests, errors etc.
     */
    private static Logger logger
        = LoggerFactory.getLogger(AbstractOaiPmhController.class);

    /**
     *
     */
    public static final String REQUEST_KEY = "request";

    /**
     *
     */
    public static final String RESUMPTION_TOKEN_KEY = "resumptionToken";

    /**
     *
     */
    public static final String OBJECT_KEY = "object";

    /**
     *
     */
    private static final String RDF_VIEW_NAME = "oai/getRecord.rdf";

    /**
     *
     */
    private static final String DUBLIN_CORE_VIEW_NAME = "oai/getRecord.dc";

    /**
     *
     */
    private static final String LIST_METADATA_FORMATS_VIEW
      = "oai/listMetadataFormats";

    /**
     *
     */
    private static final String LIST_SETS_VIEW = "oai/listSets";

    /**
     *
     */
    private static final String LIST_IDENTIFIERS_VIEW = "oai/listIdentifiers";

    /**
     *
     */
    private static final String IDENTIFY_VIEW = "oai/identify";

    /**
     *
     */
    private static final String LIST_RDF_VIEW = "oai/listRecords.rdf";

    /**
     *
     */
    private static final String LIST_DC_VIEW = "oai/listRecords.dc";

    /**
     *
     */
    private static final String EXCEPTION_VIEW = "oai/exception";

    /**
     *
     */
    private SERVICE service;

    /**
     *
     */
    private String repositoryName;

    /**
     *
     */
    private String baseURL;

    /**
     *
     */
    private String protocolVersion;

    /**
     *
     */
    private String adminEmail;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private Integer pageSize;

    /**
     *
     */
    private Ehcache cache;

    /**
     *
     */
    private DateTime earliestDatestamp;

    /**
     *
     * @param newEarliestDatestamp Set the earliest datestamp in this repository
     */
    public final void setEarliestDatestamp(
            final String newEarliestDatestamp) {
        this.earliestDatestamp = ISODateTimeFormat.dateTime().parseDateTime(
                newEarliestDatestamp);
    }

    /**
     *
     * @param newIdentifiableService Set the service used by this controller
     */
    public final void setService(
            final SERVICE newIdentifiableService) {
        this.service = newIdentifiableService;
    }

    /**
     *
     * @return the service used by this controller
     */
    protected final SERVICE getService() {
        return service;
    }

    /**
     * Sets cache to be used.
     *
     * @param newCache Set the cache
     */
    public final void setCache(final Ehcache newCache) {
        this.cache = newCache;
    }

    /**
     * Subclasses should override this method and return a collection of
     * org.openarchives.pmh.Set objects that will be returned in the response.
     *
     * @return the sets provided by this repository
     */
    protected Set<org.openarchives.pmh.Set> getSets() {
        return new HashSet<org.openarchives.pmh.Set>();
    }

    /**
     *
     * @param newRepositoryName Set the repository name
     */
    public final void setRepositoryName(final String newRepositoryName) {
        this.repositoryName = newRepositoryName;
    }

    /**
     *
     * @param newBaseURL Set the base url of the repostory
     */
    public final void setBaseURL(final String newBaseURL) {
        this.baseURL = newBaseURL;
    }

    /**
     *
     * @param newProtocolVersion Set the protocol version
     */
    public final void setProtocolVersion(final String newProtocolVersion) {
        this.protocolVersion = newProtocolVersion;
    }

    /**
     *
     * @param newAdminEmail Set the administrator email
     */
    public final void setAdminEmail(final String newAdminEmail) {
        this.adminEmail = newAdminEmail;
    }

    /**
     *
     * @param newDescription Set the description of the repository
     */
    public final void setDescription(final String newDescription) {
        this.description = newDescription;
    }

    /**
     *
     * @param newPageSize
     *            Set the maximum number of records to be included in a
     *            ListRecords or ListIdentifiers response
     */
    public final void setPageSize(final Integer newPageSize) {
        this.pageSize = newPageSize;
    }

    /**
     *
     * @param verb Set the verb of the request
     * @param from Set the earliest date of included records
     * @param until Set the latest date of included records
     * @param set Set the set to harvest from
     * @param resumptionToken Set the resumption token
     * @param metadataPrefix Set the metadata prefix for the output
     * @return the Request object populated with the parameters passed
     */
    protected final Request constructRequest(final Verb verb,
            final DateTime from, final DateTime until, final String set,
            final String resumptionToken, final MetadataPrefix metadataPrefix) {
        Request request = new Request();

        request.setValue(baseURL);
        request.setVerb(verb);
        request.setFrom(from);
        request.setUntil(until);
        request.setSet(set);
        request.setResumptionToken(resumptionToken);
        request.setMetadataPrefix(metadataPrefix);

        return request;
    }

    /**
     * Get a single record.
     *
     * @param identifier The identifier of the record
     * @param metadataPrefix The metadata prefix for the output
     * @return a model and view which can then be rendered
     * @throws IdDoesNotExistException
     *             if the id cannot be found in the repository
     */
    @RequestMapping(method = RequestMethod.GET, params = "verb=GetRecord")
    public final ModelAndView getRecord(
            @RequestParam(value = "identifier", required = true)
                final Serializable identifier,
            @RequestParam(value = "metadataPrefix", required = true)
            @MetadataPrefixFormat
                final MetadataPrefix metadataPrefix)
            throws IdDoesNotExistException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(
                AbstractOaiPmhController.REQUEST_KEY,
                constructRequest(Verb.GetRecord, null, null, null, null,
                        metadataPrefix));

        ChangeEvent object = service.find(identifier);

        if (object == null) {
            log(identifier, "GetRecord", 0, null);
            throw new IdDoesNotExistException(identifier);
        } else {
            log(identifier, "GetRecord", 1, null);
        }

        modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY, object);

        switch(metadataPrefix) {
        case RDF:
            modelAndView.setViewName(AbstractOaiPmhController.RDF_VIEW_NAME);
            break;
        case OAI_DC:
        default:
            modelAndView
              .setViewName(AbstractOaiPmhController.DUBLIN_CORE_VIEW_NAME);
        }

        return modelAndView;
    }

    /**
     *
     * @return the list of metadata formats provided by this repository
     */
    public abstract List<MetadataFormat> getMetadataFormats();

    /**
     *
     * @param identifier Set the identifier of the object requested
     * @param verb Set the verb of the request
     * @param count Set the number of results returned
     * @param set Set the set parameter (can be null)
     */
    public abstract void log(Serializable identifier, String verb,
            int count, String set);

    /**
     *
     * @return the metadata format supported by all repositories
     */
     protected final List<MetadataFormat> baseMetadataFormat() {
        List<MetadataFormat> metadataFormats = new ArrayList<MetadataFormat>();
        MetadataFormat oaiDc = new MetadataFormat();
        oaiDc.setMetadataPrefix(MetadataPrefix.OAI_DC);
        oaiDc.setSchema("http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
        oaiDc.setMetadataNamespace(
                "http://www.openarchives.org/OAI/2.0/oai_dc/");
        metadataFormats.add(oaiDc);

        return metadataFormats;
    }

    /**
     * List the metadata formats available for a given record.
     *
     * CannotDisseminateFormatException thrown by MetadataPrefixEditor.
     *
     * @param identifier The identifier of the record
     * @return a model and view containing the metadata formats available for
     *         that object
     * @throws IdDoesNotExistException if no record exists for that identifier
     */
    @RequestMapping(method = RequestMethod.GET,
            params = "verb=ListMetadataFormats")
    public final ModelAndView listMetadataFormats(
            @RequestParam(value = "identifier", required = false)
                final Serializable identifier)
            throws IdDoesNotExistException {

        ModelAndView modelAndView = new ModelAndView(
                AbstractOaiPmhController.LIST_METADATA_FORMATS_VIEW);
        modelAndView.addObject(
                AbstractOaiPmhController.REQUEST_KEY,
                constructRequest(Verb.ListMetadataFormats, null, null, null,
                        null, null));

        if (identifier != null) { // the identifier argument is optional
            ChangeEvent object = service.find(identifier);

            if (object == null) {
                throw new IdDoesNotExistException(identifier);
            }
        }
        modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY,
                getMetadataFormats());

        return modelAndView;
    }

    /**
     * List the sets provided by this repostory.
     *
     * CannotDisseminateFormatException thrown by MetadataPrefixEditor.
     *
     * @return a model and view containing the sets
     */
    @RequestMapping(method = RequestMethod.GET, params = "verb=ListSets")
    public final ModelAndView listSets() {

        ModelAndView modelAndView = new ModelAndView(
                AbstractOaiPmhController.LIST_SETS_VIEW);
        modelAndView.addObject(AbstractOaiPmhController.REQUEST_KEY,
                constructRequest(Verb.ListSets, null, null, null, null, null));

        Set<org.openarchives.pmh.Set> sets = getSets();
        if (sets.isEmpty()) {
            throw new NoSetHierarchyException();
        }
        modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY, sets);

        return modelAndView;
    }

    /**
     * Returns information about the repository.
     *
     * @return a model and view containing information about the repository
     */
    @RequestMapping(method = RequestMethod.GET, params = "verb=Identify")
    public final ModelAndView identify() {
        ModelAndView modelAndView = new ModelAndView(
                AbstractOaiPmhController.IDENTIFY_VIEW);
        modelAndView.addObject(AbstractOaiPmhController.REQUEST_KEY,
                constructRequest(Verb.Identify, null, null, null, null, null));
        Identify identify = new Identify();
        identify.setRepositoryName(repositoryName);
        identify.setBaseURL(baseURL);
        identify.setProtocolVersion(protocolVersion);
        identify.setDeletedRecord(DeletedRecord.NO);
        identify.setGranularity(Granularity.YYYY_MM_DD_THH_MM_SS_Z);
        identify.setEarliestDatestamp(earliestDatestamp);
        if (adminEmail != null) {
            identify.getAdminEmail().add(adminEmail);
        }
        if (description != null) {
            identify.getDescription().add(description);
        }
        modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY, identify);

        return modelAndView;
    }

    /**
     * Lists matching identifiers for requests without a resumption token.
     *
     * @param from The earliest date of a record which will be included in the
     *             result set
     * @param until The latest date of a record which will be included in the
     *              result set
     * @param metadataPrefix The metadata prefix for rendering the results
     * @param set The set to restrict the results to
     * @return a model and view containing matching records
     */
    @RequestMapping(method = RequestMethod.GET, params = {
            "verb=ListIdentifiers", "!resumptionToken" })
    public final ModelAndView listIdentifiers(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = DateTimeConverter.UTC_DATETIME_PATTERN)
                final DateTime from,
            @RequestParam(value = "until", required = false)
            @DateTimeFormat(pattern = DateTimeConverter.UTC_DATETIME_PATTERN)
                final DateTime until,
            @RequestParam(value = "metadataPrefix", required = true)
            @MetadataPrefixFormat
                final MetadataPrefix metadataPrefix,
            @RequestParam(value = "set", required = false)
                final String set) {

        ModelAndView modelAndView = new ModelAndView(
                AbstractOaiPmhController.LIST_IDENTIFIERS_VIEW);
        modelAndView.addObject(
                AbstractOaiPmhController.REQUEST_KEY,
                constructRequest(Verb.ListIdentifiers, from, until, set, null,
                        metadataPrefix));

        Page<ChangeEvent<T>> results = service.page(set, from, until, pageSize, 0);
        log(null, "ListIdentifiers", results.getRecords().size(), set);

        if (results.getSize() == 0) {
            throw new NoRecordsMatchException("No records match");
        }

        modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY, results);

        if (results.getSize() > results.getRecords().size() && cache != null) {
            ResumptionToken resumptionToken
                = new ResumptionToken(results.getSize(),
                    pageSize, results.getCurrentIndex(),
                    from, until, metadataPrefix, set);
            modelAndView.addObject(
                    AbstractOaiPmhController.RESUMPTION_TOKEN_KEY,
                    resumptionToken);

            cache.put(new Element(resumptionToken.getValue(), resumptionToken),
                    false);
        }

        return modelAndView;
    }

    /**
     * Lists matching identifiers for requests with a resumption token.
     *
     * @param rToken The resumption token
     * @return a model and view containing matching records
     */
    @RequestMapping(method = RequestMethod.GET, params = {
            "verb=ListIdentifiers", "resumptionToken" })
    public final ModelAndView listIdentifiers(
            @RequestParam(value = "resumptionToken", required = true)
                final String rToken) {

        ResumptionToken resumptionToken;
        if (cache != null && cache.get(rToken) != null) {
            resumptionToken = (ResumptionToken) cache.get(rToken)
                    .getObjectValue();
            ModelAndView modelAndView = new ModelAndView(
                    AbstractOaiPmhController.LIST_IDENTIFIERS_VIEW);
            modelAndView.addObject(
                    AbstractOaiPmhController.REQUEST_KEY,
                    constructRequest(Verb.ListIdentifiers, null, null, null,
                            rToken, null));

            Page<ChangeEvent<T>> results = service.page(resumptionToken
                    .getSet(), resumptionToken.getFrom(), resumptionToken
                    .getUntil(), pageSize, (resumptionToken.getCursor()
                    .intValue() / pageSize) + 1);

            log(null, "ListIdentifiers", results.getRecords().size(),
                    resumptionToken.getSet());

            if (results.getSize() == 0) {
                throw new NoRecordsMatchException("No records match");
            }

            modelAndView
                    .addObject(AbstractOaiPmhController.OBJECT_KEY, results);

            if (results.getSize() > ((results.getPageSize() * results
                    .getCurrentIndex()) + results.getRecords().size())) {
                resumptionToken.setValue(UUID.randomUUID().toString());
                resumptionToken.updateResults(results.getSize(), pageSize,
                        results.getCurrentIndex());
                modelAndView.addObject(
                        AbstractOaiPmhController.RESUMPTION_TOKEN_KEY,
                        resumptionToken);
                cache.put(new Element(resumptionToken.getValue(),
                        resumptionToken), false);
            }

            return modelAndView;
        } else {
            throw new BadResumptionTokenException();
        }
    }

    /**
     * List matching records for requests which have not provided a resumption
     * token.
     *
     * @param from The earliest date of a record which will be included in the
     *             result set
     * @param until The latest date of a record which will be included in the
     *              result set
     * @param metadataPrefix The metadata prefix for rendering the results
     * @param set The set to restrict the results to
     * @return a model and view containing matching records
     */
    @RequestMapping(method = RequestMethod.GET, params = { "verb=ListRecords",
            "!resumptionToken" })
    public final ModelAndView listRecords(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = DateTimeConverter.UTC_DATETIME_PATTERN)
            final DateTime from,
            @RequestParam(value = "until", required = false)
            @DateTimeFormat(pattern = DateTimeConverter.UTC_DATETIME_PATTERN)
                final DateTime until,
            @RequestParam(value = "metadataPrefix", required = true)
            @MetadataPrefixFormat
                final MetadataPrefix metadataPrefix,
            @RequestParam(value = "set", required = false)
                final String set) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(
                AbstractOaiPmhController.REQUEST_KEY,
                constructRequest(Verb.ListRecords, from, until, set, null,
                        metadataPrefix));

        switch(metadataPrefix) {
        case RDF:
            modelAndView.setViewName(AbstractOaiPmhController.LIST_RDF_VIEW);
            break;
        case OAI_DC:
            default:
            modelAndView.setViewName(AbstractOaiPmhController.LIST_DC_VIEW);
        }

        Page<ChangeEvent<T>> results = service.page(set, from, until, pageSize, 0);

        log(null, "ListRecords", results.getRecords().size(), set);

        if (results.getSize() == 0) {
            throw new NoRecordsMatchException("No records match");
        }

        modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY, results);

        if (results.getSize() > results.getRecords().size() && cache != null) {
            ResumptionToken resumptionToken
                = new ResumptionToken(results.getSize(),
                    pageSize, results.getCurrentIndex(),
                    from, until, metadataPrefix, set);
            modelAndView.addObject(
                    AbstractOaiPmhController.RESUMPTION_TOKEN_KEY,
                    resumptionToken);
            cache.put(new Element(resumptionToken.getValue(), resumptionToken),
                    false);
        }

        return modelAndView;
    }

    /**
     * List matching records for requests which have provided a resumption
     * token.
     *
     * @param rToken The resumption token
     * @return a model and view containing matching records
     */
    @RequestMapping(method = RequestMethod.GET, params = { "verb=ListRecords",
            "resumptionToken" })
    public final ModelAndView listRecords(
            @RequestParam("resumptionToken") final String rToken) {

        ResumptionToken resumptionToken;
        if (cache != null && cache.get(rToken) != null) {
            resumptionToken = (ResumptionToken) cache.get(rToken)
                    .getObjectValue();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(
                    AbstractOaiPmhController.REQUEST_KEY,
                    constructRequest(Verb.ListRecords, null, null, null,
                            rToken, null));

            switch (resumptionToken.getMetadataPrefix()) {
            case RDF:
                modelAndView
                        .setViewName(AbstractOaiPmhController.LIST_RDF_VIEW);
                break;
            case OAI_DC:
            default:
                modelAndView.setViewName(AbstractOaiPmhController.LIST_DC_VIEW);
            }

            Page<ChangeEvent<T>> results = service.page(resumptionToken
                    .getSet(), resumptionToken.getFrom(), resumptionToken
                    .getUntil(), pageSize, (resumptionToken.getCursor()
                    .intValue() / pageSize) + 1);

            log(null, "ListRecords", results.getRecords().size(),
                    resumptionToken.getSet());

            if (results.getSize() == 0) {
                throw new NoRecordsMatchException("No records match");
            }

            modelAndView
                    .addObject(AbstractOaiPmhController.OBJECT_KEY, results);

            if (results.getSize() > ((results.getPageSize() * results
                    .getCurrentIndex()) + results.getRecords().size())) {
                resumptionToken.setValue(UUID.randomUUID().toString());
                resumptionToken.updateResults(results.getSize(), pageSize,
                        results.getCurrentIndex());
                modelAndView.addObject(
                        AbstractOaiPmhController.RESUMPTION_TOKEN_KEY,
                        resumptionToken);
                cache.put(new Element(resumptionToken.getValue(),
                        resumptionToken), false);
            }

            return modelAndView;
        } else {
            throw new BadResumptionTokenException();
        }
    }

    /**
     *
     * @param ex The exception
     * @param httpServletRequest The httpServletRequest
     * @param code The error code
     * @return a model and view containing the exception
     */
    private ModelAndView doException(final Exception ex,
            final HttpServletRequest httpServletRequest, final ErrorCode code) {
        ModelAndView modelAndView = new ModelAndView(
                AbstractOaiPmhController.EXCEPTION_VIEW);
        Error error = new Error();
        error.setCode(code);
        error.setValue(ex.getMessage());
        Request request = new Request();
        request.setValue(baseURL);
        try {
            request.setVerb(Verb.valueOf(httpServletRequest
                    .getParameter("verb")));
        } catch (Exception e) {
            // do nothing
        }

        request.setResumptionToken(httpServletRequest
                .getParameter("resumptionToken"));
        modelAndView.addObject(AbstractOaiPmhController.REQUEST_KEY, request);
        modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY, error);
        return modelAndView;
    }

    /**
     *
     * @param ex The exception
     * @param request The httpServletRequest
     * @return a model and view containing the exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class,
            TypeMismatchException.class,
            MissingServletRequestParameterException.class })
    public final ModelAndView handleBadArgument(final Exception ex,
            final HttpServletRequest request) {
        return doException(ex, request, ErrorCode.badArgument);
    }

   /**
    *
    * @param ex The exception
    * @param request The httpServletRequest
    * @return a model and view containing the exception
    */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CannotDisseminateFormatException.class)
    public final ModelAndView handleCannotDisseminateFormat(final Exception ex,
            final HttpServletRequest request) {
        return doException(ex, request, ErrorCode.cannotDisseminateFormat);
    }

   /**
    *
    * @param ex The exception
    * @param request The httpServletRequest
    * @return a model and view containing the exception
    */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadResumptionTokenException.class)
    public final ModelAndView handleBadResumptionToken(final Exception ex,
            final HttpServletRequest request) {
        return doException(ex, request, ErrorCode.badResumptionToken);
    }

   /**
    *
    * @param ex The exception
    * @param request The httpServletRequest
    * @return a model and view containing the exception
    */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoRecordsMatchException.class)
    public final ModelAndView handleNoRecordsMatch(final Exception ex,
            final HttpServletRequest request) {
        return doException(ex, request, ErrorCode.noRecordsMatch);
    }

   /**
    *
    * @param ex The exception
    * @param request The httpServletRequest
    * @return a model and view containing the exception
    */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IdDoesNotExistException.class)
    public final ModelAndView handleIdDoesNotExist(final Exception ex,
            final HttpServletRequest request) {
        return doException(ex, request, ErrorCode.idDoesNotExist);
    }

   /**
    *
    * @param ex The exception
    * @param request The httpServletRequest
    * @return a model and view containing the exception
    */
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(NoSetHierarchyException.class)
    public final ModelAndView handleNoSetHierarchy(final Exception ex,
            final HttpServletRequest request) {
        return doException(ex, request, ErrorCode.noSetHierarchy);
    }
}
