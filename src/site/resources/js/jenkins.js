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