/**
 * 
 */
package net.sf.jasperreports.engine.fill;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.LinkedMap;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.Resource;

/**
 * @author jk00kg
 * 
 * A Simple Report Writer that currently supports a subset of report functionality.  Specifically, report templates that
 * require summary and aggregate figures have not been tested within the batch context.
 * 
 * The code sits in this package as protected scope methods & objects are required and much of the JasperReportFiller 
 * functionality is copied from {@link JRVerticalFiller}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRVerticalFiller.java 5687 2012-10-01 14:18:01Z lucianc $
 *
 */
public class JRVerticalReportWriter<T> extends JRBaseFiller implements ItemWriter<T>, StepExecutionListener {
    
    /**
     * @throws Exception 
     * 
     */
    public JRVerticalReportWriter(JasperReport jasperReport) throws Exception {
        this(DefaultJasperReportsContext.getInstance(), jasperReport);
    }
    
    /**
     * 
     */
    private Logger logger = LoggerFactory.getLogger(JRVerticalReportWriter.class);
    
    /**
     * Records whether there is data
     */
    private boolean hadData = false;
    
    /**
     * 
     */
    private Resource outputResouce;

    /**
     * By default an empty map of parameters used by JasperReports
     */
    private Map<String, Object> parameterValues = new HashMap<String, Object>();

    /**
     * 
     */
    private String defaultOutputDir;

    /**
     * @return the defaultOutputDir
     */
    public String getDefaultOutputDir() {
        return defaultOutputDir;
    }

    /**
     * @param defaultOutputDir the defaultOutputDir to set
     */
    public void setDefaultOutputDir(String defaultOutputDir) {
        this.defaultOutputDir = defaultOutputDir;
    }

    /**
     * @return the outputResouce
     */
    public Resource getOutputResouce() {
        return outputResouce;
    }

    /**
     * @param outputResouce the outputResouce to set
     */
    public void setOutputResouce(Resource outputResouce) {
        this.outputResouce = outputResouce;
    }

    /**
     * @return the parameterValues
     */
    public Map<String, Object> getParameterValues() {
        return parameterValues;
    }

    /**
     * @param parameterValues the parameterValues to set
     */
    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        hadData = false;
        // Functionality from JRBaseFiller, other 'pre-write/pre-fill functions can only happen once *some*
        // data has been loaded and is therefore called in this.write()
        baseInit();
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // fill/write footers
        String fileName = null;
        try {
            if (hadData) {
                fillReportEnd();
            } else {
                //TODO Add from 'else' block.  We don't expect to hit this code; possibly if attempting to download data
                // that doesn't contain a license
            }
            //Then export and return appropriate execution status
            if(outputResouce != null) {
                fileName = outputResouce.getFile().getCanonicalPath();
            } else {
                fileName = defaultOutputDir + "/checklist" + Math.abs((new Random()).nextLong()) + ".pdf";
            }
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
        } catch (JRException e) {
            logger.error("Unable to export report to " + fileName, e);
            return ExitStatus.FAILED;
        } catch (IOException e) {
            logger.error("Unable to write to file" + fileName + ". There was probably a problem trying to use the file " + outputResouce, e);
        }
        baseClose();
        return ExitStatus.COMPLETED;
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
     */
    @Override
    public void write(List<? extends T> items) throws Exception {
        //To make Filler compatible with chunk-oriented processing:
        //Update the dataset now instead of beginning writing as we don't operate on whole datasets.
        JRDataSource ds = new JRBeanCollectionDataSource(items);
        getMainDataset().getParametersMap().get(JRParameter.REPORT_DATA_SOURCE).setValue(ds);
        getMainDataset().initDatasource();
        
        //Then use tweeked JasperReports code 
        if(!hadData) {//On the first chunk
            try {
                if(next()) {
                    hadData = true; //Alternative to calling next() which isn't idempotent.
                    fillReportStart();
                }
            } catch (JRException e) {
                logger.error("Unable to fill headers of file");
            }
        }
        setLastPageFooter(false);

        //Fill it in
        if (hadData) {
            while (next()) {
                fillReportContent();
            }
        }
        else
        {
            logger.debug("Fill " + fillerId + ": no data");

            switch (whenNoDataType)
            {
                case ALL_SECTIONS_NO_DETAIL :
                {
                    logger.debug("Fill " + fillerId + ": all sections");
                    
                    scriptlet.callBeforeReportInit();
                    calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
                    scriptlet.callAfterReportInit();

                    printPage = newPage();
                    addPage(printPage);
                    setFirstColumn();
                    offsetY = topMargin;

                    fillBackground();

                    fillTitle();

                    fillPageHeader(JRExpression.EVALUATION_DEFAULT);

                    fillColumnHeader(JRExpression.EVALUATION_DEFAULT);

                    fillGroupHeaders(true);

                    fillGroupFooters(true);

                    fillSummary();

                    break;
                }
                case BLANK_PAGE :
                {
                    logger.debug("Fill " + fillerId + ": blank page");

                    printPage = newPage();
                    addPage(printPage);
                    break;
                }
                case NO_DATA_SECTION:
                {
                    logger.debug("Fill " + fillerId + ": all sections");

                    scriptlet.callBeforeReportInit();
                    calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
                    scriptlet.callAfterReportInit();

                    printPage = newPage();
                    addPage(printPage);
                    setFirstColumn();
                    offsetY = topMargin;

                    fillBackground();

                    fillNoData();
                    
                    break;

                }
                case NO_PAGES :
                default :
                {
                    logger.debug("Fill " + fillerId + ": no pages");
                }
            }
        }

        if (isSubreport())
        {
            //if (
            //  columnIndex == 0 ||
            //  (columnIndex > 0 && printPageStretchHeight < offsetY + bottomMargin)
            //  )
            //{
                printPageStretchHeight = offsetY + bottomMargin;
            //}
        }

        if (fillContext.isIgnorePagination())
        {
            jasperPrint.setPageHeight(offsetY + bottomMargin);
        }
    }

//Unmodified methods from jasperreports 5.0.0 JRVerticalFiller
   /**
    *
    */
   protected JRVerticalReportWriter(
       JasperReportsContext jasperReportsContext, 
       JasperReport jasperReport
       ) throws JRException
   {
       this(jasperReportsContext, jasperReport, null, null);
   }

   /**
    *
    */
   protected JRVerticalReportWriter(
       JasperReportsContext jasperReportsContext, 
       JasperReport jasperReport, 
       JRFillSubreport parentElement
       ) throws JRException
   {
       super(jasperReportsContext, jasperReport, null, parentElement);

       setPageHeight(pageHeight);
   }

   /**
    *
    */
   protected JRVerticalReportWriter(
       JasperReportsContext jasperReportsContext,
       JasperReport jasperReport, 
       DatasetExpressionEvaluator evaluator, 
       JRFillSubreport parentElement
       ) throws JRException
   {
       super(jasperReportsContext, jasperReport, evaluator, parentElement);

       setPageHeight(pageHeight);
   }

   /**
    *
    */
   protected JRVerticalReportWriter(
       JasperReportsContext jasperReportsContext, 
       JasperReport jasperReport, 
       JREvaluator evaluator, 
       JRFillSubreport parentElement
       ) throws JRException
   {
       this(jasperReportsContext, jasperReport, (DatasetExpressionEvaluator) evaluator, parentElement);
   }


   /**
    *
    */
   protected void setPageHeight(int pageHeight)
   {
       this.pageHeight = pageHeight;

       columnFooterOffsetY = pageHeight - bottomMargin;
       if (pageFooter != null)
       {
           columnFooterOffsetY -= pageFooter.getHeight();
       }
       if (columnFooter != null)
       {
           columnFooterOffsetY -= columnFooter.getHeight();
       }
       lastPageColumnFooterOffsetY = pageHeight - bottomMargin;
       if (lastPageFooter != null)//FIXMENOW testing with null is awkward since bands can never be null, but rather equal to missingFillBand
       {
           lastPageColumnFooterOffsetY -= lastPageFooter.getHeight();
       }
       if (columnFooter != null)
       {
           lastPageColumnFooterOffsetY -= columnFooter.getHeight();
       }
       
       logger.debug("Filler " + fillerId + " - pageHeight: " + pageHeight
               + ", columnFooterOffsetY: " + columnFooterOffsetY
               + ", lastPageColumnFooterOffsetY: " + lastPageColumnFooterOffsetY);
       
   }


   /**
    *
    */
   protected synchronized void fillReport() throws JRException
   {
       setLastPageFooter(false);

       if (next())
       {
           fillReportStart();

           while (next())
           {
               fillReportContent();
           }

           fillReportEnd();
       }
       else
       {
           logger.debug("Fill " + fillerId + ": no data");

           switch (whenNoDataType)
           {
               case ALL_SECTIONS_NO_DETAIL :
               {
                   logger.debug("Fill " + fillerId + ": all sections");

                   scriptlet.callBeforeReportInit();
                   calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
                   scriptlet.callAfterReportInit();

                   printPage = newPage();
                   addPage(printPage);
                   setFirstColumn();
                   offsetY = topMargin;

                   fillBackground();

                   fillTitle();

                   fillPageHeader(JRExpression.EVALUATION_DEFAULT);

                   fillColumnHeader(JRExpression.EVALUATION_DEFAULT);

                   fillGroupHeaders(true);

                   fillGroupFooters(true);

                   fillSummary();

                   break;
               }
               case BLANK_PAGE :
               {
                   logger.debug("Fill " + fillerId + ": blank page");

                   printPage = newPage();
                   addPage(printPage);
                   break;
               }
               case NO_DATA_SECTION:
               {
                   logger.debug("Fill " + fillerId + ": all sections");

                   scriptlet.callBeforeReportInit();
                   calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
                   scriptlet.callAfterReportInit();

                   printPage = newPage();
                   addPage(printPage);
                   setFirstColumn();
                   offsetY = topMargin;

                   fillBackground();

                   fillNoData();
                   
                   break;

               }
               case NO_PAGES :
               default :
               {
                   logger.debug("Fill " + fillerId + ": no pages");
               }
           }
       }

       if (isSubreport())
       {
           //if (
           //  columnIndex == 0 ||
           //  (columnIndex > 0 && printPageStretchHeight < offsetY + bottomMargin)
           //  )
           //{
               printPageStretchHeight = offsetY + bottomMargin;
           //}
       }

       if (fillContext.isIgnorePagination())
       {
           jasperPrint.setPageHeight(offsetY + bottomMargin);
       }
   }


   /**
    *
    */
   private void fillReportStart() throws JRException
   {
       scriptlet.callBeforeReportInit();
       calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
       scriptlet.callAfterReportInit();

       printPage = newPage();
       addPage(printPage);
       setFirstColumn();
       offsetY = topMargin;

       fillBackground();

       fillTitle();

       fillPageHeader(JRExpression.EVALUATION_DEFAULT);

       fillColumnHeader(JRExpression.EVALUATION_DEFAULT);

       fillGroupHeaders(true);

       fillDetail();
   }


   /**
    *
    */
   private void fillReportContent() throws JRException
   {
       calculator.estimateGroupRuptures();

       fillGroupFooters(false);

       resolveGroupBoundElements(JRExpression.EVALUATION_OLD, false);
       scriptlet.callBeforeGroupInit();
       calculator.initializeVariables(ResetTypeEnum.GROUP, IncrementTypeEnum.GROUP);
       scriptlet.callAfterGroupInit();

       fillGroupHeaders(false);

       fillDetail();
   }


   /**
    *
    */
   private void fillReportEnd() throws JRException
   {
       fillGroupFooters(true);

       fillSummary();
   }


   /**
    *
    */
    private void fillTitle() throws JRException
    {
       logger.debug("Fill " + fillerId + ": title at " + offsetY);

       title.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

       if (title.isToPrint())
       {
           while (
               title.getBreakHeight() > pageHeight - bottomMargin - offsetY
               )
           {
               addPage(false);
           }

           title.evaluate(JRExpression.EVALUATION_DEFAULT);

           JRPrintBand printBand = title.fill(pageHeight - bottomMargin - offsetY);

           if (title.willOverflow() && title.isSplitPrevented() && isSubreport())
           {
               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);

               printBand = title.refill(pageHeight - bottomMargin - offsetY);
           }

           fillBand(printBand);
           offsetY += printBand.getHeight();

           while (title.willOverflow())
           {
               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);

               printBand = title.fill(pageHeight - bottomMargin - offsetY);

               fillBand(printBand);
               offsetY += printBand.getHeight();
           }

           resolveBandBoundElements(title, JRExpression.EVALUATION_DEFAULT);

           if (isTitleNewPage)
           {
               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);
           }
       }
   }


   /**
    *
    */
   private void fillPageHeader(byte evaluation) throws JRException
   {
       logger.debug("Fill " + fillerId + ": page header at " + offsetY);

       setNewPageColumnInBands();

       pageHeader.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

       if (pageHeader.isToPrint())
       {
           int reattempts = getMasterColumnCount();
           if (isCreatingNewPage)
           {
               --reattempts;
           }

           boolean filled = fillBandNoOverflow(pageHeader, evaluation);

           for (int i = 0; !filled && i < reattempts; ++i)
           {
               resolveGroupBoundElements(evaluation, false);
               resolveColumnBoundElements(evaluation);
               resolvePageBoundElements(evaluation);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);

               filled = fillBandNoOverflow(pageHeader, evaluation);
           }

           if (!filled)
           {
               throw new JRRuntimeException("Infinite loop creating new page due to page header overflow.");
           }
       }

       columnHeaderOffsetY = offsetY;

       isNewPage = true;
       isFirstPageBand = true;
   }


   private boolean fillBandNoOverflow(JRFillBand band, byte evaluation) throws JRException
   {
       int availableHeight = columnFooterOffsetY - offsetY;
       boolean overflow = availableHeight < band.getHeight();

       if (!overflow)
       {
           band.evaluate(evaluation);
           JRPrintBand printBand = band.fill(availableHeight);

           overflow = band.willOverflow();
           if (overflow)
           {
               band.rewind();
           }
           else
           {
               fillBand(printBand);
               offsetY += printBand.getHeight();

               resolveBandBoundElements(band, evaluation);
           }
       }

       return !overflow;
   }


   /**
    *
    */
   private void fillColumnHeader(byte evaluation) throws JRException
   {
       logger.debug("Fill " + fillerId + ": column header at " + offsetY);

       setNewPageColumnInBands();

       columnHeader.evaluatePrintWhenExpression(evaluation);

       if (columnHeader.isToPrint())
       {
           int reattempts = getMasterColumnCount();
           if (isCreatingNewPage)
           {
               --reattempts;
           }

           setOffsetX();

           boolean filled = fillBandNoOverflow(columnHeader, evaluation);

           for (int i = 0; !filled && i < reattempts; ++i)
           {
               while (columnIndex < columnCount - 1)
               {
                   resolveGroupBoundElements(evaluation, false);
                   resolveColumnBoundElements(evaluation);
                   scriptlet.callBeforeColumnInit();
                   calculator.initializeVariables(ResetTypeEnum.COLUMN, IncrementTypeEnum.COLUMN);
                   scriptlet.callAfterColumnInit();

                   columnIndex += 1;
                   setOffsetX();
                   offsetY = columnHeaderOffsetY;

                   setColumnNumberVar();
               }

               fillPageFooter(evaluation);

               resolveGroupBoundElements(evaluation, false);
               resolveColumnBoundElements(evaluation);
               resolvePageBoundElements(evaluation);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);

               fillPageHeader(evaluation);

               filled = fillBandNoOverflow(columnHeader, evaluation);
           }

           if (!filled)
           {
               throw new JRRuntimeException("Infinite loop creating new page due to column header overflow.");
           }
       }

       isNewColumn = true;
       isFirstColumnBand = true;
   }


   /**
    *
    */
   private void fillGroupHeaders(boolean isFillAll) throws JRException
   {
       if (groups != null && groups.length > 0)
       {
           for(int i = 0; i < groups.length; i++)
           {
               JRFillGroup group = groups[i];

               if(isFillAll || group.hasChanged())
               {
                   SavePoint newSavePoint = fillGroupHeader(group);
                   // fillGroupHeader never returns null, because we need a save point 
                   // regardless of the group header printing or not
                   newSavePoint.groupIndex = i;
                   
                   if (keepTogetherSavePoint == null && group.isKeepTogether())
                   {
                       keepTogetherSavePoint = newSavePoint;
                   }
               }
           }
       }
   }


   /**
    *
    */
   private SavePoint fillGroupHeader(JRFillGroup group) throws JRException
   {
       SavePoint savePoint = null;
       
       JRFillSection groupHeaderSection = (JRFillSection)group.getGroupHeaderSection();

       logger.debug("Fill " + fillerId + ": " + group.getName() + " header at " + offsetY);

       byte evalPrevPage = (group.isTopLevelChange()?JRExpression.EVALUATION_OLD:JRExpression.EVALUATION_DEFAULT);

       if ( (group.isStartNewPage() || group.isResetPageNumber()) && !isNewPage )
       {
           fillPageBreak(
               group.isResetPageNumber(),
               evalPrevPage,
               JRExpression.EVALUATION_DEFAULT,
               true
               );
       }
       else if ( group.isStartNewColumn() && !isNewColumn )
       {
           fillColumnBreak(
               evalPrevPage,
               JRExpression.EVALUATION_DEFAULT
               );
       }

       JRFillBand[] groupHeaderBands = groupHeaderSection.getFillBands();
       for(int i = 0; i < groupHeaderBands.length; i++)
       {
           JRFillBand groupHeaderBand = groupHeaderBands[i];

           groupHeaderBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

           if (groupHeaderBand.isToPrint())
           {
               while (
                   groupHeaderBand.getBreakHeight() > columnFooterOffsetY - offsetY ||
                   group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
                   )
               {
                   fillColumnBreak(
                       evalPrevPage,
                       JRExpression.EVALUATION_DEFAULT
                       );
               }
           }

           if (i == 0)
           {
               setNewGroupInBands(group);

               group.setFooterPrinted(false);
           }

           if (groupHeaderBand.isToPrint())
           {
               SavePoint newSavePoint = fillColumnBand(groupHeaderBand, JRExpression.EVALUATION_DEFAULT);
               
               savePoint = advanceSavePoint(savePoint, newSavePoint);

               isFirstPageBand = false;
               isFirstColumnBand = false;
           }
       }

       group.setHeaderPrinted(true);

       isNewGroup = true;

       if (savePoint == null)
       {
           // fillGroupHeader never returns null, because we need a save point 
           // regardless of the group header printing or not
           savePoint = 
               new SavePoint(
                   getCurrentPage(), 
                   columnIndex,
                   isNewPage,
                   isNewColumn,
                   offsetY
                   );
       }
       
       return savePoint;
   }


   /**
    *
    */
   private void fillGroupHeadersReprint(byte evaluation) throws JRException
   {
       if (groups != null && groups.length > 0)
       {
           for(int i = 0; i < groups.length; i++)
           {
               fillGroupHeaderReprint(groups[i], evaluation);
           }
       }
   }


   /**
    *
    */
    private void fillGroupHeaderReprint(JRFillGroup group, byte evaluation) throws JRException
    {
       if (
           group.isReprintHeaderOnEachPage() &&
           (!group.hasChanged() || (group.hasChanged() && group.isHeaderPrinted()))
           )
       {
           JRFillSection groupHeaderSection = (JRFillSection)group.getGroupHeaderSection();

           JRFillBand[] groupHeaderBands = groupHeaderSection.getFillBands();
           for(int i = 0; i < groupHeaderBands.length; i++)
           {
               JRFillBand groupHeaderBand = groupHeaderBands[i];

               groupHeaderBand.evaluatePrintWhenExpression(evaluation);

               if (groupHeaderBand.isToPrint())
               {
                   while (
                       groupHeaderBand.getBreakHeight() > columnFooterOffsetY - offsetY 
                       || group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
                       )
                   {
                       fillColumnBreak(evaluation, evaluation);
                   }

                   fillColumnBand(groupHeaderBand, evaluation);

                   isFirstPageBand = false;
                   isFirstColumnBand = false;
               }
           }
       }
   }


   /**
    *
    */
   private void fillDetail() throws JRException
   {
       logger.debug("Fill " + fillerId + ": detail at " + offsetY);

       if (!detailSection.areAllPrintWhenExpressionsNull())
       {
           calculator.estimateVariables();
       }

       JRFillBand[] detailBands = detailSection.getFillBands();
       for(int i = 0; i < detailBands.length; i++)
       {
           JRFillBand detailBand = detailBands[i];
           
           detailBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_ESTIMATED);

           if (detailBand.isToPrint())
           {
               while (
                   detailBand.getBreakHeight() > columnFooterOffsetY - offsetY
                   )
               {
                   byte evalPrevPage = (isNewGroup?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD);

                   fillColumnBreak(
                       evalPrevPage,
                       JRExpression.EVALUATION_DEFAULT
                       );
               }
               
               break;
           }
       }

       scriptlet.callBeforeDetailEval();
       calculator.calculateVariables();
       scriptlet.callAfterDetailEval();

       if (detailBands != null)
       {
           for(int i = 0; i < detailBands.length; i++)
           {
               JRFillBand detailBand = detailBands[i];
                       
               detailBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

               if (detailBand.isToPrint())
               {
                   fillColumnBand(detailBand, JRExpression.EVALUATION_DEFAULT);

                   isFirstPageBand = false;
                   isFirstColumnBand = false;
               }
           }
       }

       isNewPage = false;
       isNewColumn = false;
       isNewGroup = false;
   }


   /**
    *
    */
   private void fillGroupFooters(boolean isFillAll) throws JRException
   {
       if (groups != null && groups.length > 0)
       {
           SavePoint savePoint = null;
           
           byte evaluation = (isFillAll)?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD;

           for(int i = groups.length - 1; i >= 0; i--)
           {
               JRFillGroup group = groups[i];
               
               if (isFillAll || group.hasChanged())
               {
                   SavePoint newSavePoint = fillGroupFooter(group, evaluation);
                   // fillGroupFooter might return null, because if the group footer does not print, 
                   // its footer position is completely irrelevant
                   if (newSavePoint != null)
                   {
                       switch (group.getFooterPositionValue())
                       {
                           case STACK_AT_BOTTOM:
                           {
                               savePoint = advanceSavePoint(savePoint, newSavePoint);

                               if (savePoint != null)
                               {
                                   savePoint.footerPosition = FooterPositionEnum.STACK_AT_BOTTOM;
                               }

                               break;
                           }
                           case FORCE_AT_BOTTOM:
                           {
                               savePoint = advanceSavePoint(savePoint, newSavePoint);

                               if (savePoint != null)
                               {
                                   savePoint.moveSavePointContent();
                                   offsetY = columnFooterOffsetY;
                               }

                               savePoint = null;

                               break;
                           }
                           case COLLATE_AT_BOTTOM:
                           {
                               savePoint = advanceSavePoint(savePoint, newSavePoint);

                               break;
                           }
                           case NORMAL:
                           default:
                           {
                               if (savePoint != null)
                               {
                                   //only "StackAtBottom" and "CollateAtBottom" save points could get here
                                   
                                   // check to see if the new save point is on the same page/column as the previous one
                                   if (
                                       savePoint.page == newSavePoint.page
                                       && savePoint.columnIndex == newSavePoint.columnIndex
                                       )
                                   {
                                       // if the new save point is on the same page/column, 
                                       // we just move the marker on the existing save point,
                                       // but only if was a "StackAtBottom" one
                                       
                                       if (savePoint.footerPosition == FooterPositionEnum.STACK_AT_BOTTOM)
                                       {
                                           savePoint.saveHeightOffset(newSavePoint.heightOffset);
                                       }
                                       else
                                       {
                                           // we cancel the "CollateAtBottom" save point
                                           savePoint = null;
                                       }
                                   }
                                   else
                                   {
                                       // page/column break occurred, so the move operation 
                                       // must be performed on the previous save point, regardless 
                                       // whether it was a "StackAtBottom" or a "CollateAtBottom"
                                       savePoint.moveSavePointContent();
                                       savePoint = null;
                                   }
                               }
                               else
                               {
                                   // only "ForceAtBottom" save points could get here, but they are already null
                                   savePoint = null;
                               }
                           }
                       }
                   }
                   
                   // regardless of whether the fillGroupFooter returned a save point or not 
                   // (footer was printed or not), we just need to mark the end of the group 
                   if (
                       keepTogetherSavePoint != null
                       && i <= keepTogetherSavePoint.groupIndex
                       )
                   {
                       keepTogetherSavePoint = null;
                   }
               }
           }
           
           if (savePoint != null)
           {
               savePoint.moveSavePointContent();
               offsetY = columnFooterOffsetY;
           }
       }
   }


   /**
    *
    */
   private SavePoint fillGroupFooter(JRFillGroup group, byte evaluation) throws JRException
   {
       SavePoint savePoint = null;
       
       JRFillSection groupFooterSection = (JRFillSection)group.getGroupFooterSection();

       logger.debug("Fill " + fillerId + ": " + group.getName() + " footer at " + offsetY);

       JRFillBand[] groupFooterBands = groupFooterSection.getFillBands();
       for(int i = 0; i < groupFooterBands.length; i++)
       {
           JRFillBand groupFooterBand = groupFooterBands[i];
           
           groupFooterBand.evaluatePrintWhenExpression(evaluation);

           if (groupFooterBand.isToPrint())
           {
               if (
                   groupFooterBand.getBreakHeight() > columnFooterOffsetY - offsetY
                   )
               {
                   fillColumnBreak(evaluation, evaluation);
               }

               SavePoint newSavePoint = fillColumnBand(groupFooterBand, evaluation);
               newSavePoint.footerPosition = group.getFooterPositionValue();
               
               savePoint = advanceSavePoint(savePoint, newSavePoint);
               
               isFirstPageBand = false;
               isFirstColumnBand = false;
           }
       }

       isNewPage = false;
       isNewColumn = false;

       group.setHeaderPrinted(false);
       group.setFooterPrinted(true);
       
       return savePoint;
   }


   /**
    *
    */
    private void fillColumnFooter(byte evaluation) throws JRException
    {
       logger.debug("Fill " + fillerId + ": column footer at " + offsetY);

       setOffsetX();
       
       /*
       if (!isSubreport)
       {
           offsetY = columnFooterOffsetY;
       }
       */

       if (isSubreport() && !isSubreportRunToBottom() && columnIndex == 0)
       {
           columnFooterOffsetY = offsetY;
       }

       int oldOffsetY = offsetY;
       if (!isFloatColumnFooter && !fillContext.isIgnorePagination())
       {
           offsetY = columnFooterOffsetY;
       }

       columnFooter.evaluatePrintWhenExpression(evaluation);

       if (columnFooter.isToPrint())
       {
           fillFixedBand(columnFooter, evaluation);
       }

       if (isFloatColumnFooter && !fillContext.isIgnorePagination())
       {
           offsetY += columnFooterOffsetY - oldOffsetY;
       }
   }


   /**
    *
    */
   private void fillPageFooter(byte evaluation) throws JRException
   {
       JRFillBand crtPageFooter = getCurrentPageFooter();

       logger.debug("Fill " + fillerId + ": " + (isLastPageFooter ? "last " : "") + "page footer at " + offsetY);
       
       offsetX = leftMargin;

       if ((!isSubreport() || isSubreportRunToBottom()) && !fillContext.isIgnorePagination())
       {
           offsetY = pageHeight - crtPageFooter.getHeight() - bottomMargin;
       }

       crtPageFooter.evaluatePrintWhenExpression(evaluation);

       if (crtPageFooter.isToPrint())
       {
           fillFixedBand(crtPageFooter, evaluation);
       }
   }


   /**
    *
    */
   private void fillSummary() throws JRException
   {
       logger.debug("Fill " + fillerId + ": summary at " + offsetY);

       offsetX = leftMargin;
       
       if (lastPageFooter == missingFillBand)
       {
           if (
               !isSummaryNewPage
               && columnIndex == 0
               && summary.getBreakHeight() <= columnFooterOffsetY - offsetY
               )
           {
               fillSummaryNoLastFooterSamePage();
           }
           else
           {
               fillSummaryNoLastFooterNewPage();
           }
       }
       else
       {
           if (isSummaryWithPageHeaderAndFooter)
           {
               fillSummaryWithLastFooterAndPageBands();
           }
           else
           {
               fillSummaryWithLastFooterNoPageBands();
           }
       }

       resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
       resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
       resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
       resolveReportBoundElements();
   }


   /**
    *
    */
   private void fillSummaryNoLastFooterSamePage() throws JRException
   {
       summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

       if (summary != missingFillBand && summary.isToPrint())
       {
           summary.evaluate(JRExpression.EVALUATION_DEFAULT);

           JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

           if (summary.willOverflow() && summary.isSplitPrevented())
           {
               fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

               fillPageFooter(JRExpression.EVALUATION_DEFAULT);

               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);
               
               if (isSummaryWithPageHeaderAndFooter)
               {
                   fillPageHeader(JRExpression.EVALUATION_DEFAULT);
               }

               printBand = summary.refill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

               fillBand(printBand);
               offsetY += printBand.getHeight();
           }
           else
           {
               fillBand(printBand);
               offsetY += printBand.getHeight();

               fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

               fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               
               if (summary.willOverflow())
               {
                   resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                   resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                   resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                   scriptlet.callBeforePageInit();
                   calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                   scriptlet.callAfterPageInit();

                   addPage(false);
                   
                   if (isSummaryWithPageHeaderAndFooter)
                   {
                       fillPageHeader(JRExpression.EVALUATION_DEFAULT);
                   }

                   printBand = summary.fill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

                   fillBand(printBand);
                   offsetY += printBand.getHeight();
               }
           }

           /*   */
           fillSummaryOverflow();
           
           //DONE
       }
       else
       {
           fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

           fillPageFooter(JRExpression.EVALUATION_DEFAULT);
           
           //DONE
       }
   }


   /**
    *
    */
   private void fillSummaryNoLastFooterNewPage() throws JRException
   {
       fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

       fillPageFooter(JRExpression.EVALUATION_DEFAULT);

       summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

       if (summary != missingFillBand && summary.isToPrint())
       {
           resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
           resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
           resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
           scriptlet.callBeforePageInit();
           calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
           scriptlet.callAfterPageInit();

           addPage(false);

           if (isSummaryWithPageHeaderAndFooter)
           {
               fillPageHeader(JRExpression.EVALUATION_DEFAULT);
           }

           summary.evaluate(JRExpression.EVALUATION_DEFAULT);

           JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

           if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
           {
               if (isSummaryWithPageHeaderAndFooter)
               {
                   fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               }

               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);

               if (isSummaryWithPageHeaderAndFooter)
               {
                   fillPageHeader(JRExpression.EVALUATION_DEFAULT);
               }

               printBand = summary.refill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));
           }

           fillBand(printBand);
           offsetY += printBand.getHeight();

           /*   */
           fillSummaryOverflow();
       }
       
       //DONE
   }


   /**
    *
    */
   private void fillSummaryWithLastFooterAndPageBands() throws JRException
   {
       if (
           !isSummaryNewPage
           && columnIndex == 0
           && summary.getBreakHeight() <= columnFooterOffsetY - offsetY
           )
       {
           summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

           if (summary != missingFillBand && summary.isToPrint())
           {
               summary.evaluate(JRExpression.EVALUATION_DEFAULT);

               JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

               if (summary.willOverflow() && summary.isSplitPrevented())
               {
                   fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

                   fillPageFooter(JRExpression.EVALUATION_DEFAULT);

                   resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                   resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                   resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                   scriptlet.callBeforePageInit();
                   calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                   scriptlet.callAfterPageInit();

                   addPage(false);
                   
                   fillPageHeader(JRExpression.EVALUATION_DEFAULT);
                   
                   printBand = summary.refill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());

                   fillBand(printBand);
                   offsetY += printBand.getHeight();
               }
               else
               {
                   fillBand(printBand);
                   offsetY += printBand.getHeight();

                   if (!summary.willOverflow())
                   {
                       setLastPageFooter(true);
                   }
                   
                   fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
               }
               
               /*   */
               fillSummaryOverflow();

               //DONE
           }
           else
           {
               setLastPageFooter(true);

               fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

               fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               
               //DONE
           }
       }
       else if (columnIndex == 0 && offsetY <= lastPageColumnFooterOffsetY)
       {
           summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

           if (summary != missingFillBand && summary.isToPrint())
           {
               fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

               fillPageFooter(JRExpression.EVALUATION_DEFAULT);

               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);
               
               fillPageHeader(JRExpression.EVALUATION_DEFAULT);

               summary.evaluate(JRExpression.EVALUATION_DEFAULT);

               JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());

               if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
               {
                   fillPageFooter(JRExpression.EVALUATION_DEFAULT);

                   resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                   resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                   resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                   scriptlet.callBeforePageInit();
                   calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                   scriptlet.callAfterPageInit();

                   addPage(false);
                   
                   fillPageHeader(JRExpression.EVALUATION_DEFAULT);

                   printBand = summary.refill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());
               }

               fillBand(printBand);
               offsetY += printBand.getHeight();

               /*   */
               fillSummaryOverflow();
               
               //DONE
           }
           else
           {
               setLastPageFooter(true);

               fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

               fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               
               //DONE
           }
       }
       else
       {
           fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

           fillPageFooter(JRExpression.EVALUATION_DEFAULT);

           resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
           resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
           resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
           scriptlet.callBeforePageInit();
           calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
           scriptlet.callAfterPageInit();

           addPage(false);

           fillPageHeader(JRExpression.EVALUATION_DEFAULT);

           summary.evaluate(JRExpression.EVALUATION_DEFAULT);

           JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());

           if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
           {
               fillPageFooter(JRExpression.EVALUATION_DEFAULT);

               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);
               
               fillPageHeader(JRExpression.EVALUATION_DEFAULT);

               printBand = summary.refill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());
           }

           fillBand(printBand);
           offsetY += printBand.getHeight();

           /*   */
           fillSummaryOverflow();
           
           //DONE
       }
   }


   /**
    *
    */
   private void fillSummaryWithLastFooterNoPageBands() throws JRException
   {
       if (
           !isSummaryNewPage
           && columnIndex == 0
           && summary.getBreakHeight() <= lastPageColumnFooterOffsetY - offsetY
           )
       {
           setLastPageFooter(true);

           summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

           if (summary != missingFillBand && summary.isToPrint())
           {
               summary.evaluate(JRExpression.EVALUATION_DEFAULT);

               JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

               if (summary.willOverflow() && summary.isSplitPrevented())
               {
                   fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

                   fillPageFooter(JRExpression.EVALUATION_DEFAULT);

                   resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                   resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                   resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                   scriptlet.callBeforePageInit();
                   calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                   scriptlet.callAfterPageInit();

                   addPage(false);

                   printBand = summary.refill(pageHeight - bottomMargin - offsetY);

                   fillBand(printBand);
                   offsetY += printBand.getHeight();
               }
               else
               {
                   fillBand(printBand);
                   offsetY += printBand.getHeight();

                   fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

                   fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               }

               /*   */
               fillSummaryOverflow();
               
               //DONE
           }
           else
           {
               fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

               fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               
               //DONE
           }
       }
       else if (
           !isSummaryNewPage
           && columnIndex == 0
           && summary.getBreakHeight() <= columnFooterOffsetY - offsetY
           )
       {
           summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

           if (summary != missingFillBand && summary.isToPrint())
           {
               summary.evaluate(JRExpression.EVALUATION_DEFAULT);

               JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

               if (summary.willOverflow() && summary.isSplitPrevented())
               {
                   if (offsetY <= lastPageColumnFooterOffsetY)
                   {
                       setLastPageFooter(true);

                       fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

                       fillPageFooter(JRExpression.EVALUATION_DEFAULT);

                       resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                       resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                       resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                       scriptlet.callBeforePageInit();
                       calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                       scriptlet.callAfterPageInit();

                       addPage(false);

                       printBand = summary.refill(pageHeight - bottomMargin - offsetY);

                       fillBand(printBand);
                       offsetY += printBand.getHeight();
                   }
                   else
                   {
                       fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);

                       setLastPageFooter(true);

                       printBand = summary.refill(lastPageColumnFooterOffsetY - offsetY);

                       fillBand(printBand);
                       offsetY += printBand.getHeight();

                       fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

                       fillPageFooter(JRExpression.EVALUATION_DEFAULT);
                   }
               }
               else
               {
                   fillBand(printBand);
                   offsetY += printBand.getHeight();

                   fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);

                   setLastPageFooter(true);

                   if (summary.willOverflow())
                   {
                       printBand = summary.fill(lastPageColumnFooterOffsetY - offsetY);

                       fillBand(printBand);
                       offsetY += printBand.getHeight();
                   }

                   fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

                   fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               }

               /*   */
               fillSummaryOverflow();
               
               //DONE
           }
           else
           {
               if(offsetY > lastPageColumnFooterOffsetY)
               {
                   fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);
               }

               setLastPageFooter(true);

               fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

               fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               
               //DONE
           }
       }
       else if (columnIndex == 0 && offsetY <= lastPageColumnFooterOffsetY)
       {
           setLastPageFooter(true);

           fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

           fillPageFooter(JRExpression.EVALUATION_DEFAULT);

           summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

           if (summary != missingFillBand && summary.isToPrint())
           {
               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);

               summary.evaluate(JRExpression.EVALUATION_DEFAULT);

               JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY);

               if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
               {
                   resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                   resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                   resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                   scriptlet.callBeforePageInit();
                   calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                   scriptlet.callAfterPageInit();

                   addPage(false);

                   printBand = summary.refill(pageHeight - bottomMargin - offsetY);
               }

               fillBand(printBand);
               offsetY += printBand.getHeight();

               /*   */
               fillSummaryOverflow();
           }
           
           //DONE
       }
       else
       {
           fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

           fillPageFooter(JRExpression.EVALUATION_DEFAULT);

           resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
           resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
           resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
           scriptlet.callBeforePageInit();
           calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
           scriptlet.callAfterPageInit();

           addPage(false);

           fillPageHeader(JRExpression.EVALUATION_DEFAULT);

           //fillColumnHeader(JRExpression.EVALUATION_DEFAULT);

           setLastPageFooter(true);

           if (isSummaryNewPage)
           {
               fillPageFooter(JRExpression.EVALUATION_DEFAULT);

               summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

               if (summary != missingFillBand && summary.isToPrint())
               {
                   resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                   resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                   resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                   scriptlet.callBeforePageInit();
                   calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                   scriptlet.callAfterPageInit();

                   addPage(false);

                   summary.evaluate(JRExpression.EVALUATION_DEFAULT);

                   JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY);

                   if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
                   {
                       resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                       resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                       resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                       scriptlet.callBeforePageInit();
                       calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                       scriptlet.callAfterPageInit();

                       addPage(false);

                       printBand = summary.refill(pageHeight - bottomMargin - offsetY);
                   }

                   fillBand(printBand);
                   offsetY += printBand.getHeight();

                   /*   */
                   fillSummaryOverflow();
               }
               
               //DONE
           }
           else
           {
               summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

               if (summary != missingFillBand && summary.isToPrint())
               {
                   summary.evaluate(JRExpression.EVALUATION_DEFAULT);

                   JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

                   if (summary.willOverflow() && summary.isSplitPrevented())//FIXMENOW check subreport here?
                   {
                       fillPageFooter(JRExpression.EVALUATION_DEFAULT);

                       resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
                       resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
                       resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
                       scriptlet.callBeforePageInit();
                       calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
                       scriptlet.callAfterPageInit();

                       addPage(false);

                       printBand = summary.refill(pageHeight - bottomMargin - offsetY);

                       fillBand(printBand);
                       offsetY += printBand.getHeight();
                   }
                   else
                   {
                       fillBand(printBand);
                       offsetY += printBand.getHeight();

                       fillPageFooter(JRExpression.EVALUATION_DEFAULT);
                   }

                   /*   */
                   fillSummaryOverflow();
               }
               else
               {
                   fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               }
               
               //DONE
           }
       }
   }


   /**
    *
    */
   private void fillSummaryOverflow() throws JRException
   {
       while (summary.willOverflow())
       {
           if (isSummaryWithPageHeaderAndFooter)
           {
               fillPageFooter(JRExpression.EVALUATION_DEFAULT);
           }
           
           resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
           resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
           resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
           scriptlet.callBeforePageInit();
           calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
           scriptlet.callAfterPageInit();

           addPage(false);

           if (isSummaryWithPageHeaderAndFooter)
           {
               fillPageHeader(JRExpression.EVALUATION_DEFAULT);
           }
           
           JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

           fillBand(printBand);
           offsetY += printBand.getHeight();
       }

       resolveBandBoundElements(summary, JRExpression.EVALUATION_DEFAULT);

       if (isSummaryWithPageHeaderAndFooter)
       {
           if (offsetY > pageHeight - bottomMargin - lastPageFooter.getHeight())
           {
               fillPageFooter(JRExpression.EVALUATION_DEFAULT);
               
               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();

               addPage(false);

               fillPageHeader(JRExpression.EVALUATION_DEFAULT);
           }
           
           if (lastPageFooter != missingFillBand)
           {
               setLastPageFooter(true);
           }
           
           fillPageFooter(JRExpression.EVALUATION_DEFAULT);
       }
   }


   /**
    *
    */
   private void fillBackground() throws JRException
   {
       logger.debug("Fill " + fillerId + ": background at " + offsetY);

       //offsetX = leftMargin;

       //if (!isSubreport)
       //{
       //  offsetY = pageHeight - pageFooter.getHeight() - bottomMargin;
       //}

       if (background.getHeight() <= pageHeight - bottomMargin - offsetY)
       {
           background.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

           if (background.isToPrint())
           {
                   background.evaluate(JRExpression.EVALUATION_DEFAULT);

                   JRPrintBand printBand = background.fill(pageHeight - bottomMargin - offsetY);

                   fillBand(printBand);
                   //offsetY += printBand.getHeight();
           }
       }
   }


   /**
    *
    */
   private void addPage(boolean isResetPageNumber) throws JRException
   {
       if (isSubreport())
       {
           if (!parentFiller.isBandOverFlowAllowed())
           {
               throw new JRRuntimeException("Subreport overflowed on a band that does not support overflow.");
           }

           //if (
           //  columnIndex == 0 ||
           //  (columnIndex > 0 && printPageStretchHeight < offsetY + bottomMargin)
           //  )
           //{
               printPageStretchHeight = offsetY + bottomMargin;
           //}

           suspendSubreportRunner();
       }

       printPage = newPage();

       if (isResetPageNumber)
       {
           calculator.getPageNumber().setValue(Integer.valueOf(1));
       }
       else
       {
           calculator.getPageNumber().setValue(
               Integer.valueOf(((Number)calculator.getPageNumber().getValue()).intValue() + 1)
               );
       }

       calculator.getPageNumber().setOldValue(
           calculator.getPageNumber().getValue()
           );

       addPage(printPage);

       setFirstColumn();
       offsetY = topMargin;

       fillBackground();
   }

   private void setFirstColumn()
   {
       columnIndex = 0;
       offsetX = leftMargin;
       setColumnNumberVar();
   }

   private void setColumnNumberVar()
   {
       JRFillVariable columnNumber = calculator.getColumnNumber();
       columnNumber.setValue(Integer.valueOf(columnIndex + 1));
       columnNumber.setOldValue(columnNumber.getValue());
   }

   /**
    *
    */
   private void fillPageBreak(
       boolean isResetPageNumber,
       byte evalPrevPage,
       byte evalNextPage,
       boolean isReprintGroupHeaders
       ) throws JRException
   {
       if (isCreatingNewPage)
       {
           throw new JRException("Infinite loop creating new page.");
       }

       if (keepTogetherSavePoint != null)
       {
           keepTogetherSavePoint.saveEndOffsetY(offsetY);
       }
       
       isCreatingNewPage = true;

       fillColumnFooter(evalPrevPage);

       fillPageFooter(evalPrevPage);

       resolveGroupBoundElements(evalPrevPage, false);
       resolveColumnBoundElements(evalPrevPage);
       resolvePageBoundElements(evalPrevPage);
       scriptlet.callBeforePageInit();
       calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
       scriptlet.callAfterPageInit();

       if (
           keepTogetherSavePoint != null
           && !keepTogetherSavePoint.isNewPage 
           )
       {
           keepTogetherSavePoint.removeContent();
       }

       addPage(isResetPageNumber);

       fillPageHeader(evalNextPage);

       fillColumnHeader(evalNextPage);

       boolean savePointContentMoved = moveKeepTogetherSavePointContent();
       if (
           !savePointContentMoved
           && isReprintGroupHeaders
           )
       {
           fillGroupHeadersReprint(evalNextPage);
       }

       isCreatingNewPage = false;
   }


   /**
    *
    */
   private void fillColumnBreak(
       byte evalPrevPage,
       byte evalNextPage
       ) throws JRException
   {
       if (columnIndex == columnCount - 1)
       {
           fillPageBreak(false, evalPrevPage, evalNextPage, true);
       }
       else
       {
           if (keepTogetherSavePoint != null)
           {
               keepTogetherSavePoint.saveEndOffsetY(offsetY);
           }
           
           fillColumnFooter(evalPrevPage);

           resolveGroupBoundElements(evalPrevPage, false);
           resolveColumnBoundElements(evalPrevPage);
           scriptlet.callBeforeColumnInit();
           calculator.initializeVariables(ResetTypeEnum.COLUMN, IncrementTypeEnum.COLUMN);
           scriptlet.callAfterColumnInit();

           if (
               keepTogetherSavePoint != null
               && !keepTogetherSavePoint.isNewColumn 
               )
           {
               keepTogetherSavePoint.removeContent();
           }

           columnIndex += 1;
           setOffsetX();
           offsetY = columnHeaderOffsetY;

           setColumnNumberVar();

           fillColumnHeader(evalNextPage);

           moveKeepTogetherSavePointContent();
       }
   }


   /**
    *
    */
   protected void fillPageBand(JRFillBand band, byte evaluation) throws JRException
   {
       band.evaluate(evaluation);

       JRPrintBand printBand = band.fill(columnFooterOffsetY - offsetY);

       if (band.willOverflow() && band.isSplitPrevented())
       {
           fillPageBreak(false, evaluation, evaluation, true);

           printBand = band.refill(columnFooterOffsetY - offsetY);
       }

       fillBand(printBand);
       offsetY += printBand.getHeight();

       while (band.willOverflow())
       {
           fillPageBreak(false, evaluation, evaluation, true);

           printBand = band.fill(columnFooterOffsetY - offsetY);

           fillBand(printBand);
           offsetY += printBand.getHeight();
       }

       resolveBandBoundElements(band, evaluation);
   }


   /**
    *
    */
   protected SavePoint fillColumnBand(JRFillBand band, byte evaluation) throws JRException
   {
       band.evaluate(evaluation);

       JRPrintBand printBand = band.fill(columnFooterOffsetY - offsetY);

       if (
           band.willOverflow() 
           && (band.isSplitPrevented() || keepTogetherSavePoint != null)
           )
       {
           fillColumnBreak(evaluation, evaluation);

           printBand = band.refill(columnFooterOffsetY - offsetY);
       }

       SavePoint savePoint = 
           new SavePoint(
               getCurrentPage(), 
               columnIndex, 
               isNewPage,
               isNewColumn,
               offsetY
               );
       
       fillBand(printBand);
       offsetY += printBand.getHeight();
       
       savePoint.saveHeightOffset(columnFooterOffsetY - offsetY);
       // we mark the save point here, because overflow content beyond this point
       // should be rendered normally, not moved in any way 

       while (band.willOverflow())
       {
           fillColumnBreak(evaluation, evaluation);

           printBand = band.fill(columnFooterOffsetY - offsetY);

           fillBand(printBand);
           offsetY += printBand.getHeight();
       }

       resolveBandBoundElements(band, evaluation);
       
       return savePoint;
   }


   /**
    *
    */
   protected void fillFixedBand(JRFillBand band, byte evaluation) throws JRException
   {
       band.evaluate(evaluation);

       JRPrintBand printBand = band.fill();

       fillBand(printBand);
       offsetY += printBand.getHeight();

       resolveBandBoundElements(band, evaluation);
   }


   /**
    *
    */
   protected void fillBand(JRPrintBand band)
   {
       for(Iterator<JRPrintElement> it = band.iterateElements(); it.hasNext();)
       {
           JRPrintElement element = it.next();
           element.setX(element.getX() + offsetX);
           element.setY(element.getY() + offsetY);
           printPage.addElement(element);
       }
   }


   /**
    *
    */
   private void setNewPageColumnInBands()
   {
       title.setNewPageColumn(true);
       pageHeader.setNewPageColumn(true);
       columnHeader.setNewPageColumn(true);
       detailSection.setNewPageColumn(true);
       columnFooter.setNewPageColumn(true);
       pageFooter.setNewPageColumn(true);
       lastPageFooter.setNewPageColumn(true);
       summary.setNewPageColumn(true);
       noData.setNewPageColumn(true);

       if (groups != null && groups.length > 0)
       {
           for(int i = 0; i < groups.length; i++)
           {
               ((JRFillSection)groups[i].getGroupHeaderSection()).setNewPageColumn(true);
               ((JRFillSection)groups[i].getGroupFooterSection()).setNewPageColumn(true);
           }
       }
   }


   /**
    *
    */
   private void setNewGroupInBands(JRGroup group)
   {
       title.setNewGroup(group, true);
       pageHeader.setNewGroup(group, true);
       columnHeader.setNewGroup(group, true);
       detailSection.setNewGroup(group, true);
       columnFooter.setNewGroup(group, true);
       pageFooter.setNewGroup(group, true);
       lastPageFooter.setNewGroup(group, true);
       summary.setNewGroup(group, true);
       noData.setNewGroup(group, true);

       if (groups != null && groups.length > 0)
       {
           for(int i = 0; i < groups.length; i++)
           {
               ((JRFillSection)groups[i].getGroupHeaderSection()).setNewGroup(group, true);
               ((JRFillSection)groups[i].getGroupFooterSection()).setNewGroup(group, true);
           }
       }
   }


   /**
    *
    */
   private JRFillBand getCurrentPageFooter()
   {
       return isLastPageFooter ? lastPageFooter : pageFooter;
   }


   /**
    *
    */
   private void setLastPageFooter(boolean isLastPageFooter)
   {
       this.isLastPageFooter = isLastPageFooter;

       if (isLastPageFooter)
       {
           columnFooterOffsetY = lastPageColumnFooterOffsetY;
       }
   }

   
   /**
    *
    */
   private void fillNoData() throws JRException
   {
       logger.debug("Fill " + fillerId + ": noData at " + offsetY);

       noData.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

       if (noData.isToPrint())
       {
           while (noData.getBreakHeight() > pageHeight - bottomMargin - offsetY)
           {
               addPage(false);
           }

           noData.evaluate(JRExpression.EVALUATION_DEFAULT);

           JRPrintBand printBand = noData.fill(pageHeight - bottomMargin - offsetY);

           if (noData.willOverflow() && noData.isSplitPrevented() && isSubreport())
           {
               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();
               
               addPage(false);
               
               printBand = noData.refill(pageHeight - bottomMargin - offsetY);
           }

           fillBand(printBand);
           offsetY += printBand.getHeight();

           while (noData.willOverflow())
           {
               resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
               resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
               resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
               scriptlet.callBeforePageInit();
               calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
               scriptlet.callAfterPageInit();
               
               addPage(false);
               
               printBand = noData.fill(pageHeight - bottomMargin - offsetY);
               
               fillBand(printBand);
               offsetY += printBand.getHeight();
           }

           resolveBandBoundElements(noData, JRExpression.EVALUATION_DEFAULT);
       }
   }

   
   /**
    *
    */
   private void setOffsetX()
   {
       if (columnDirection == RunDirectionEnum.RTL)
       {
           offsetX = pageWidth - rightMargin - columnWidth - columnIndex * (columnSpacing + columnWidth);
       }
       else
       {
           offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
       }
   }
   
//Methods that are out of scope (visibility)
   /**
    * 
    */
   private void baseInit() {
       JasperReportsContext localContext = LocalJasperReportsContext.getLocalContext(getJasperReportsContext(), parameterValues);
       if (localContext != getJasperReportsContext())
       {
           setJasperReportsContext(localContext);
       }
//       fillingThread = Thread.currentThread();
       try
       {
           boundElements = new HashMap<JREvaluationTime, LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>>>();

           createBoundElementMaps(JREvaluationTime.EVALUATION_TIME_REPORT);
           createBoundElementMaps(JREvaluationTime.EVALUATION_TIME_PAGE);
           createBoundElementMaps(JREvaluationTime.EVALUATION_TIME_COLUMN);

           if (groups != null)
           {
               for (int i = 0; i < groups.length; i++)
               {
                   createBoundElementMaps(JREvaluationTime.getGroupEvaluationTime(groups[i].getName()));
               }
           }

           for (Iterator<JRBand> i = bands.iterator(); i.hasNext();)
           {
               JRFillBand band = (JRFillBand) i.next();
               createBoundElementMaps(JREvaluationTime.getBandEvaluationTime(band));
           }

           if (parentFiller != null)
           {
               parentFiller.registerSubfiller(this);
           }

           setParameters(parameterValues);

           loadStyles();

           jasperPrint.setName(name);
           jasperPrint.setPageWidth(pageWidth);
           jasperPrint.setPageHeight(pageHeight);
           jasperPrint.setTopMargin(topMargin);
           jasperPrint.setLeftMargin(leftMargin);
           jasperPrint.setBottomMargin(bottomMargin);
           jasperPrint.setRightMargin(rightMargin);
           jasperPrint.setOrientation(orientation);

           jasperPrint.setFormatFactoryClass(jasperReport.getFormatFactoryClass());
           jasperPrint.setLocaleCode(JRDataUtils.getLocaleCode(getLocale()));
           jasperPrint.setTimeZoneId(JRDataUtils.getTimeZoneId(getTimeZone()));

           jasperPrint.setDefaultStyle(defaultStyle);

           /*   */
           if (styles != null && styles.length > 0)
           {
               for (int i = 0; i < styles.length; i++)
               {
                   addPrintStyle(styles[i]);
               }
           }

           /*   */
           //Done in write(List<T>) mainDataset.start();
       } catch (Exception e) {
           logger.error("Unable to initialise report", e);
       }
       
   }
   
   /**
    * @param evaluationTime
    */
   private void createBoundElementMaps(JREvaluationTime evaluationTime)
   {
       LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> boundElementsMap = 
               new LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>>();
       boundElements.put(evaluationTime, boundElementsMap);
   }

   /**
    * 
    */
   private void baseClose() {
       getMainDataset().closeDatasource();
       getMainDataset().disposeParameterContributors();
       
       if (parentFiller == null)
       {
           // commit the cached data
           fillContext.cacheDone();
       }

       if (parentFiller != null)
       {
           parentFiller.unregisterSubfiller(this);
       }
       
       if (fillContext.isUsingVirtualizer())
       {
           // removing the listener
           virtualizationContext.removeListener(virtualizationListener);
       }
       
       if (parentFiller == null)
       {
           fillContext.dispose();
       }
   }
    
}
