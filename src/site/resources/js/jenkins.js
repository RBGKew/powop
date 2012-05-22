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

var monthNames = [ "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December" ];

    function daysBetween(firstDate, secondDate) {
      var oneDay = 24*60*60*1000; // hours*minutes*seconds*milliseconds
      diffDays = Math.abs((firstDate.getTime() - secondDate.getTime())/(oneDay));
      return diffDays;
    }

    function calcBusinessDays(dDate1, dDate2) { // input given as Date objects
      var iWeeks, iDateDiff, iAdjust = 0;   
      if (dDate2 < dDate1) return -1; // error code if dates transposed
     
      var iWeekday1 = dDate1.getDay(); // day of week
      var iWeekday2 = dDate2.getDay();
     
      iWeekday1 = (iWeekday1 == 0) ? 7 : iWeekday1; // change Sunday from 0 to 7
      iWeekday2 = (iWeekday2 == 0) ? 7 : iWeekday2;
     
      if ((iWeekday1 > 5) && (iWeekday2 > 5)) iAdjust = 1; // adjustment if both days on weekend
     
      iWeekday1 = (iWeekday1 > 5) ? 5 : iWeekday1; // only count weekdays
      iWeekday2 = (iWeekday2 > 5) ? 5 : iWeekday2;
     
      // calculate differnece in weeks (1000mS * 60sec * 60min * 24hrs * 7 days = 604800000)
      iWeeks = Math.floor((dDate2.getTime() - dDate1.getTime()) / 604800000)
     
      if (iWeekday1 <= iWeekday2) {
        iDateDiff = (iWeeks * 5) + (iWeekday2 - iWeekday1)
      } else {
        iDateDiff = ((iWeeks + 1) * 5) - (iWeekday1 - iWeekday2)
      }

      iDateDiff -= iAdjust // take into account both days on weekend
     
      return (iDateDiff + 1); // add 1 because dates are inclusive 
  }

    function parseDate(d) {
       var year = parseInt(d.substring(0,4));
       var month = 0;
       if(d.substring(5,6) == "0") {
          month = parseInt(d.substring(6,7));
       } else {
           month = parseInt(d.substring(5,7));
       }

       var day = 0;
       
       if(d.substring(8,9) == "0") {
          day = parseInt(d.substring(9,10));
       } else {
          day = parseInt(d.substring(8,10));
       }
       return new Date(year, month - 1 , day);
       
    }
    
    function month(now, months, i) {
      var month = {};
      month.date = now;
      month.name = monthNames[now.getMonth()];
      
      var next = new Date(now.getFullYear(), now.getMonth() + 1, 1);
      month.days = daysBetween(now, next);
      if(i == 0) {
        month.offset = 0;
      } else {
        month.offset = months[i - 1].offset + months[i - 1].days;
      }
      months[i] = month;
      return next;
    }

    function week(now, weeks, i) {
    	var next = new Date(now.getTime() + (24 * 60 * 60 * 1000 *  (7 - now.getDay())));
    	var week = {};
    	
    	week.days = daysBetween(now,next);
    	if(i == 0) {
    		week.offset = 0;
    	} else {
    		week.offset = weeks[i - 1].offset + weeks[i -1].days;
    	}
    	var onejan = new Date(next.getFullYear(),0,1);
    	week.name =  Math.ceil((((next - onejan) / 86400000) + onejan.getDay()+1)/7);
    	weeks[i] = week;
    	return next;
    }

    function drawWeeksAndMonths(graph, scale, barHeight) {
    var numberOfMonths = 12;
        var numberOfWeeks = 52;
        var months = new Array(numberOfMonths);
        var weeks = new Array(numberOfWeeks);
        var now = new Date();
        for(var i = 0; i < numberOfMonths; i++) {          
          now = month(now,months, i);
        }

        now = new Date();
        for(var i = 0; i < numberOfWeeks; i++) {          
          now = week(now,weeks, i);
        }

        
        for(var i = 0; i < numberOfMonths; i++) {          
          graph.append("svg:rect")
                  .attr("width", months[i].days * scale)
                  .attr("x", months[i].offset * scale)
                  .attr("height",barHeight)
                  .attr("y",barHeight)
                  .attr("fill", "white")
                  .attr("stroke","black");
          graph.append("svg:text")                  
                  .attr("x", (months[i].offset + (months[i].days / 2)) * scale)
                  .attr("y",barHeight * 1.5)
                  .attr("text-anchor","middle")
                  .attr("stroke","black")
                  .text(months[i].name);

        }

        for(var i = 0; i < numberOfWeeks; i++) {          
          graph.append("svg:rect")
                  .attr("width", weeks[i].days * scale)
                  .attr("x", weeks[i].offset * scale)
                  .attr("height",barHeight)
                  .attr("y",0)
                  .attr("fill", "white")
                  .attr("stroke","black");
        }
    }

    function release(query, release, element, title, y, graph, scale, barHeight, width, velocity, people) {
        graph.append("svg:rect")
                  .attr("width", width)
                  .attr("height",barHeight)
                  .attr("y",y)
                  .attr("fill", "white")
                  .attr("stroke","black");
        var workRemaining = 0;
        
          bugzillaRPC.advancedSearch(query,
	      function(bugs) {
	        for (var i in bugs) {
	          var bug = bugs[i];
                 var r = parseInt(bug.internals.cf_release);
                 if(r == release) {
	            workRemaining += parseInt(bug.internals.estimated_time);	
                 }
               }
               bugzillaRPC.getBug(release,
                 function(bug) {
                   var now = new Date();
                   var date_started = parseDate(bug.internals.cf_scheduled_start);
                   var offset = 0;
                   
                   if(now < date_started) {
                      offset = date_started;
                   } else {
                      offset = now;
                   }
                   var deadline = parseDate(bug.internals.deadline);
                   var businessDays = calcBusinessDays(offset, deadline);
                   
                   var daysLeft = daysBetween(offset, deadline);
                   var offsetDays = daysBetween(now, offset);
                   
                   var lostDays = daysLeft - businessDays;
                   graph.append("svg:rect")
                     .attr("width", daysLeft * scale)
                     .attr("y",y)
                     .attr("x",offsetDays * scale)
                     .attr("height",barHeight)
                     .attr("fill-opacity",0.5)
                     .attr("fill", "blue")
                     .attr("stroke","blue");
                   graph.append("svg:rect")
                     .attr("width", (lostDays + (workRemaining /(people * velocity))) * scale)
                     .attr("y",y)
                     .attr("x",offsetDays * scale)
                     .attr("height",barHeight)
                     .attr("fill-opacity",0.5)
                     .attr("fill", "red")
                     .attr("stroke","red");
                   graph.append("svg:text")                  
                      .attr("x", (width / 2))
                      .attr("y",y + (barHeight / 2))
                      .attr("text-anchor","middle")
                      .attr("stroke","black")
                      .text(title);

              },
              function(errMsg) {alert(errMsg)}
              )                         
	    },
	    function(errMsg) {alert(errMsg)}
         );
    }
