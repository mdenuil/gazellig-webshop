const fs = require('fs');
const https = require('https');
const audit = JSON.parse(fs.readFileSync('audit.json', 'utf8'));
const suppresions = JSON.parse(fs.readFileSync('supression.json', 'utf8'));

let dependencyTemplate = fs.readFileSync('dependency-template.xml', 'utf8');
let dependencyReportTemplate = fs.readFileSync('dependency-check-report-template.xml', 'utf8');
let dependencies = "";

for (const advisoryid in audit.advisories) {
    let advisory = audit.advisories[advisoryid];
    let dependency = dependencyTemplate.replace("{module_name}", advisory.module_name);
    const suppresion = suppresions[advisory.module_name];

    // If all paths of the audit.json are in the suppression.json for that module_name, then we can suppress it.
    if (suppresion && advisory.findings[0].paths.every(path => suppresion.paths.includes(path))) {
        dependency = dependency.replace(/{vulnerabilityType}/g, "suppressedVulnerability");
    } else {
        dependency = dependency.replace(/{vulnerabilityType}/g, "vulnerability");
    }

    dependency = dependency.replace("{filepath}", advisory.findings[0].paths[0]);
    dependency = dependency.replace("{title}", advisory.title);
    dependency = dependency.replace("{severity}", advisory.severity);
    dependency = dependency.replace("{cwe}", advisory.cwe);
    dependency = dependency.replace("{overview}", advisory.overview);
    dependency = dependency.replace("{recommendation}", advisory.recommendation);

    if (advisory.severity === "info" || advisory.severity === "low") {
        dependency = dependency.replace("{cvss}", 3);
    } else if (advisory.severity === "moderate") {
        dependency = dependency.replace("{cvss}", 5);
    } else if (advisory.severity === "high" || advisory.severity === "critical") {
        dependency = dependency.replace("{cvss}", 8);
    }
    dependencies += "\n" + dependency;
}

let dependencyReport = dependencyReportTemplate.replace("{reportdate}", new Date());
dependencyReport = dependencyReport.replace("{dependencies}", dependencies);
fs.writeFileSync("dependency-check-report.xml", dependencyReport);