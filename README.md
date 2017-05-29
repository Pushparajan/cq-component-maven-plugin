cq-component-maven-plugin
=========================

The CQ Component Plugin generates many of the artifacts necessary for the creation of a CQ Component based on the information provided
by the Component's backing Java Class.

For more information on the Plugin and its usage, please see the [CQ Component Plugin site](http://code.citytechinc.com/cq-component-maven-plugin/)

Added Code to generate HTL code based on Velocity Template. This will help create WhiteLabel Site from the sling model.


This plugin will be invoked from the codegen.core projects pom file so whenever the build is triggered for the core project the dialogs and the htl will be generated

Build the apps.ui project first and copy to the content.zip in the core project or Build core and copy the components generated from the tempComp folder to the jcr_content folder in the UI project.

Either case deploy the core jar via the system console and the apps package via the package manager

