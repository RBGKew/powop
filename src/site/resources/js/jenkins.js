var JenkinsTable = function(baseUrl, jobName) {
	this.baseUrl = baseUrl;
	this.jobName = jobName;	
	document.write("<script type=\"text/javascript\" src=\"" + this.baseUrl + "/job/" + this.jobName + "/api/json?jsonp=jenkinsTable.create\"><\/script>");
}
JenkinsTable.prototype.create = function(data) {	
	var iconBaseUrl = this.baseUrl + "static/10142b80/images/16x16/";
	var healthReports = data.healthReport;
	var lastBuild = data.lastBuild;
	var innerHTML = "<p><a href=\"" + data.url + "\" class=\"externalLink\">" + data.displayName + "</a> <a href=\"" + lastBuild.url + "\" class=\"externalLink\">Last build (#" + lastBuild.number + ")</a></p>";
	innerHTML += "<table border=\"0\"><thead><tr><th align=\"left\">W</th><th align=\"left\">Description</th><th align=\"right\">%</th></tr></thead><tbody>";
	for (var i in healthReports) {
		var healthReport = healthReports[i];
		innerHTML += "<tr><td align=\"left\"><img src=\"" + iconBaseUrl + healthReport.iconUrl + "\" title=\"\" alt=\"\"/></img></td><td>" + healthReport.description + "</td><td align=\"right\">" + healthReport.score + "</td></tr>\n";
	}
	innerHTML += "</tbody></table>";
	document.write(innerHTML);
}

/* ***** BEGIN LICENSE BLOCK *****
 *   Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Bugzilla API Test Interface
 *
 * The Initial Developer of the Original Code is
 * Heather Arthur.
 * Portions created by the Initial Developer are Copyright (C) 2009
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s): 
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 * 
 * ***** END LICENSE BLOCK ***** */
var bugzillaRPC = {
  setUrl : function(url) {
    xmlRpc.setUrl(url);
  },

  getBug : function(id, callback, errback) {
    xmlRpc.methodCall("Bug.get_bugs", [{ids: [id]}],
                       function(resp) {callback(resp.bugs[0]);}, errback);
  },

  getBugs : function(ids, callback, errback) {
    xmlRpc.methodCall("Bug.get_bugs", [{ids: ids}],
                       function(resp) {callback(resp.bugs);}, errback);
  },

  advancedSearch : function(params, callback, errback) {
    var feedCallback = function(xml){
      var bugs = [];
      var entries = xml.getElementsByTagName("entry");
      for(var i = 0; i < entries.length; i++) {
        var title = entries[i].getElementsByTagName("title")[0].firstChild.data;
        var matches = /\Bug (\d*)/.exec(title);
        bugs[i] = matches[1];
      }
      bugzillaRPC.getBugs(bugs, callback, errback);
    };
    params['ctype'] = 'atom';
    var url = xmlRpc.url + "buglist.cgi?" + bugXhr.urlParams(params);
    bugXhr.xhrGET(url, feedCallback , errback);
  },

  /* fastAdvancedSearch is faster because it only makes one xhr call, but the 
     id and summary are the only pieces of information retreived for each bug */
  fastAdvancedSearch : function(params, callback, errback) {
    var feedCallback = function(xml){
      var bugs = [];
      var entries = xml.getElementsByTagName("entry");
      for(var i = 0; i < entries.length; i++) {
        var title = entries[i].getElementsByTagName("title")[0].firstChild.data;
        var matches = /\[Bug (\d*)\](.*)/.exec(title);
        bugs[i] = {id: matches[1], summary : matches[2]};
      }
      callback(bugs);
    };
    params['ctype'] = 'atom';
    var url = xmlRpc.url + "buglist.cgi?" + bugXhr.urlParams(params);
    bugXhr.xhrGET(url, feedCallback , errback);
  },

  login : function(username, password, callback, errback, remember) {
    xmlRpc.methodCall("User.login", 
                      [{login: username, password: password, remember: remember}],
                      function(resp) {callback(resp.id);} , errback);
  },

  logout : function(callback, errback) {
    xmlRpc.methodCall("User.logout", [], callback, errback);
  },

  createBug : function(bugInfo, callback, errback) {
    xmlRpc.methodCall("Bug.create", [bugInfo],
                      function(resp) {callback(resp.id);}, errback);
  },

  offerAccount : function(email, callback, errback) {
    xmlRpc.methodCall("User.offer_account_by_email", 
                     [{email: email}],
                     function(resp) {callback(resp.id);}, errback);
  },

  legalValues : function(field, product, callback, errback) {
    xmlRpc.methodCall("Bug.legal_values", 
                     [{field: field, product: product}],
                     function(resp) {callback(resp.values);}, errback);
  }
};


var xmlRpc = {
  url : "http://build.e-monocot.org/bugzilla/",

  setUrl : function(url) {
    xmlRpc.url = url;
  },

  methodCall : function(methodName, params, callback, errback) {
    var newCallback =
      function(response) {
        var resp = xmlRpc.getResponse(response);
        if(resp.faultString)
          errback(resp.faultString);
        else
          callback(resp);
      };
    var callText = xmlRpc.buildCall(methodName, params);
    bugXhr.xhrPOST(xmlRpc.url + "xmlrpc.cgi", callText, newCallback, errback);
  },

  buildCall : function(methodName, params) {
    var method = '<?xml version="1.0"?>';
    method += "<methodCall>";
    method += "<methodName>" + methodName + "</methodName><params>";
    for(var i = 0; i < params.length; i++)
      method += "<param>" + xmlRpc.buildValue(params[i]) + "</param>";
    method += "</params></methodCall>";
    return method;
  },

  buildStruct : function(members) {
    var struct = "<struct>";
    for(key in members) {
      struct += "<member><name>" + key + "</name>"
             + xmlRpc.buildValue(members[key]) + "</member>";
    }
    struct += "</struct>";
    return struct;
  },

  buildArray : function(values) {
    var array = "<array><data>";
    for(var i = 0; i < values.length; i++)
      array += xmlRpc.buildValue(values[i]);
    array += "</data></array>";
    return array;
  },
 
  buildValue : function(val) {
    var value = "<value>";
    switch(typeof(val)) {
      case 'number':
        value += "<int>" + val + "</int>";
        break;
      case 'string':
        value += "<string>" + val + "</string>";
        break;
      case 'boolean':
        value += "<boolean>" + val + "</boolean>";
        break;
      case 'object':
        if(val instanceof Array)
          value += xmlRpc.buildArray(val);
        else
          value += xmlRpc.buildStruct(val);
        break;
      default:
        break;
    }
    value += "</value>";
    return value;
  },

  getResponse : function(xml) {
    var fault = xml.firstChild.firstChild;
    if(fault.nodeName == 'fault')
       return xmlRpc.getStruct(fault.firstChild.firstChild);
    var type = xml.firstChild.firstChild.firstChild.firstChild.firstChild;
    if(type == null)
      return {};
    switch(type.nodeName) {
      case "struct":
        return xmlRpc.getStruct(type);
      case "array":
        return xmlRpc.getArray(type);
      case "value":
        return xmlRpc.getValue(type);
      default:
        return xmlRpc.getValue(type);
    };
  },

  getStruct : function(struct) {
    var members = struct.childNodes;
    var retval = {};
    for(var i = 0; i < members.length; i++) {
      var member = members[i];
      var key = member.firstChild.firstChild.data;
      var value = xmlRpc.getValue(member.childNodes[1]);
      retval[key] = value;
    }
    return retval;
  },

  getArray : function(array) {
    var values = array.firstChild.childNodes;
    var retval = [];
    for(var i = 0; i < values.length; i++) 
      retval.push(xmlRpc.getValue(values[i]));
    return retval;
  },

  getValue : function(value) {
    if(!value.firstChild)
      return '';
    if(!value.firstChild.firstChild)
      return '';
    var data = value.firstChild.firstChild.data;
    switch(value.firstChild.nodeName) {
      case 'i2': case 'int':
        return parseInt(data);
      case 'boolean':
        return parseInt(data) == 1 ? true : false;
      case 'string':
        return data;
      case 'double':
        return parseFloat(data);
      case 'dateTime':
        return Date(data);
      case 'array':
        return xmlRpc.getArray(value.firstChild);
      case 'struct':
        return xmlRpc.getStruct(value.firstChild);
      default:
        return data; 
    }
  }
};

var bugXhr = {
  xhrPOST : function (url, data, callback, errback) {
    var req = new XMLHttpRequest();
    req.open("POST", url, true);
    req.setRequestHeader("Content-type", "text/xml");
    req.setRequestHeader("Content-length", data.length);
    req.setRequestHeader("Connection", "close");
    req.onreadystatechange = function (event) {
      if (req.readyState == 4) {
        if(req.status == 200 && callback) {
          callback(req.responseXML);
        }
        else if(errback)
          errback("Connection error: HTTP code " + req.status);
      } 
    };
    req.send(data);
  },

  xhrGET : function(url, callback, errback) {
   
    var req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.onreadystatechange = function (event) {
      if (req.readyState == 4) {
        if(req.status == 200 && callback) {
          callback(req.responseXML);
        }
        else if(errback)
         errback("Connection error: HTTP code " + req.status);
      } 
    };
    req.send(null);
  },

  urlParams : function(params) {
    var url = [];
    for(var param in params)
      url.push(param + "=" + params[param]);
    return url.join("&");
  },

  urlEncode : function(params) {
    var url = [];
    for(var param in params)
      url.push(encodeURIComponent(param) + "=" + encodeURIComponent(params[param]));
    return url.join("&");
  }
};

var kanbanColumn =  function(query, element, title) {
	   bugzillaRPC.advancedSearch(query,
	                               function(bugs) {
	                                 var innerHTML = "<table><thead><tr><th align=\"left\">" + title + " (" + bugs.length + ")</th></tr></thead><tbody>";
	                                 for (var i in bugs) {
	                         		var bug = bugs[i];
	                                        innerHTML += "<tr><td align=\"left\" class=\"" + bug.priority + "\"><a href=\"http://build.e-monocot.org/bugzilla/show_bug.cgi?id=" + bug.id + "\">" + bug.id + "</a>&nbsp;" + bug.summary + "</tr>\n";
	                                 }
	                                 innerHTML += "</tbody></table>";
	                                 var elem = document.getElementById(element);  
		                         elem.innerHTML = innerHTML;
	                               },
	                        function(errMsg) {alert(errMsg)});
	  }
