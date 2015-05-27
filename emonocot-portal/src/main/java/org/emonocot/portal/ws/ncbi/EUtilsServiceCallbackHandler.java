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
/**
 * EUtilsServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

    package org.emonocot.portal.ws.ncbi;

    /**
     *  EUtilsServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class EUtilsServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public EUtilsServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public EUtilsServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for run_eSearch method
            * override this method for handling normal response from run_eSearch operation
            */
           public void receiveResultrun_eSearch(
                    org.emonocot.portal.ws.ncbi.EUtilsServiceStub.ESearchResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run_eSearch operation
           */
            public void receiveErrorrun_eSearch(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for run_eLink method
            * override this method for handling normal response from run_eLink operation
            */
           public void receiveResultrun_eLink(
                    org.emonocot.portal.ws.ncbi.EUtilsServiceStub.ELinkResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run_eLink operation
           */
            public void receiveErrorrun_eLink(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for run_eInfo method
            * override this method for handling normal response from run_eInfo operation
            */
           public void receiveResultrun_eInfo(
                    org.emonocot.portal.ws.ncbi.EUtilsServiceStub.EInfoResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run_eInfo operation
           */
            public void receiveErrorrun_eInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for run_eSpell method
            * override this method for handling normal response from run_eSpell operation
            */
           public void receiveResultrun_eSpell(
                    org.emonocot.portal.ws.ncbi.EUtilsServiceStub.ESpellResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run_eSpell operation
           */
            public void receiveErrorrun_eSpell(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for run_eSummary method
            * override this method for handling normal response from run_eSummary operation
            */
           public void receiveResultrun_eSummary(
                    org.emonocot.portal.ws.ncbi.EUtilsServiceStub.ESummaryResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run_eSummary operation
           */
            public void receiveErrorrun_eSummary(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for run_eGquery method
            * override this method for handling normal response from run_eGquery operation
            */
           public void receiveResultrun_eGquery(
                    org.emonocot.portal.ws.ncbi.EUtilsServiceStub.Result result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run_eGquery operation
           */
            public void receiveErrorrun_eGquery(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for run_ePost method
            * override this method for handling normal response from run_ePost operation
            */
           public void receiveResultrun_ePost(
                    org.emonocot.portal.ws.ncbi.EUtilsServiceStub.EPostResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run_ePost operation
           */
            public void receiveErrorrun_ePost(java.lang.Exception e) {
            }
                


    }
    