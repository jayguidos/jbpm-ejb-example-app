JBPM 6 - KIE As A JEE Application Demo
========================================

  JEE Technologies

    - EJB
    - CDI
    - REST
    - JPA
    - JTA
    - JSR 303 (Validations)
    - JSR 222 (JAXB 2.0)

  Background

    This demo models a hypothetical 'work day' as a deployment unit, where each deployment
    represents a day of work, including all the processes that run for that work day.

  Kie Jar

    - project contains a single process and a simple custom threaded work item
    

  Installation Instructions

    - install the standard KIE distro and follow the instructions to install 
      it (look at http://docs.jboss.org/jbpm/v6.0.1/userguide/jBPMInstaller.html)
    - add a JNDI datasource name 'jbpm-demo' 
      in <jboss-installation-dir>/standalone/configuration/standalone-full.xml>
    - add user 'andi' and role 'demoAdmin' to 
      <jboss-installation-dir>/standalone/configuration/user.properties (and roles.properties)
    - clone the source code (that you are reading now) like this: 

       git clone https://github.com/jayguidos/jbpm-ejb-example-app.git

    - start the JBoss service in the conventional way:

       cd jbpm-installation
       ant start.demo.noeclipse 

  Building Instructions
    
    - cd into the cloned source directory and build with maven:
      
       cd jbpm-ejb-example-app
       mvn clean install -DskipTests=true

    - start KIE Workbench and clone a new KIE project repo 
      from https://github.com/jayguidos/jbpm-ejb-example-kie-project.git       
    - build and deploy the project, called WorkDay (GAV: demo-jbpm:WorkDay.1.0.0)

   Deploying Instructions 

    - you can deploy to JBoss in all the usual ways. There is a convienince BASH script that 
      will do live deploy/redeploys, and is probably the best place to start. To deploy:

        cd jbpm-ejb-example-app
        cd deploy
        pushEar.sh localhost 

   Running Instructions

    - deployments, process launching and status reports are all done via REST calls. The 
      API is all in the class called com.demo.bpm.jee.rest.DemoRestService and you are free
      to compose REST calls using your favorite RESTful techniques. For convenience, there 
      is a set of BASH scripts that call a Java-based REST client. Here is a helpful 
      sequence of REST calls to show deployment and running of a Work Day:

         cd jbpm-ejb-example-app/bin

         demo-start-day.sh WorkDay 1.0.0 2014-02-12
            --> deploys the WorkDay KIE Jar for work day 2014-02-13. The output of the command
                will show an assigned ID number for the deployment, eg. 1

         demo-show-depoyments.sh 
            --> will report all current deployed days

         demo-start-process.sh 1 WorkDay.SimpleWorkProcess
            --> starts process WorkDay.SimpleWorkProcess for Work Day 1 (2014-02-13)

         demo-show-process-status.sh 1 WorkDay.SimpleWorkProcess true 
            --> reports the detailed node traversal history for each touched node 
                in WorkDay.SimpleWorkProcess

         demo-show-deployment-facts.sh 1 
            --> dumps the current session agenda for Work Day 1 (2014-02-13)

         demo-stop-day.sh 1
            --> undeploys Work Day 2014-02-13
