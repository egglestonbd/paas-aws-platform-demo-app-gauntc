paas-aws-platform-demo-app-gauntc
=============================

A simple app used by the engineering platform team to test and demo their AWS platform offering.

To execute acceptance tests locally against the stage reference:
  * cd acceptance-web
  * mvn cargo:run
  * curl -v --data "url=http://stage.familysearch.org/paas-aws-platform-demo-app-gauntc" http://localhost:8180/test
