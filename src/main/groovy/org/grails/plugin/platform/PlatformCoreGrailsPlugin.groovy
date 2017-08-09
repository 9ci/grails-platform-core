package org.grails.plugin.platform

import grails.plugins.Plugin

/* Copyright 2011-2012 the original author or authors:
 *
 *    Marc Palmer (marc@grailsrocks.com)
 *    Stéphane Maldini (smaldini@vmware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class PlatformCoreGrailsPlugin extends Plugin {
    def version = "3.0.0.1-SNAPSHOT"
    def grailsVersion = "3.2.0 > *"

    def title = "Plugin Platform Core"
    def author = "Sudhir Nimavat"
    def authorEmail = "sudhir_nimavat@yahoo.com"
    def organization = [name: 'Grails Plugin Collective', url: 'http://github.com/gpc']
    def description = 'Grails Platform core events'
    def profiles = ['web']

    def scm = [url: "https://github.com/grails-plugins/grails-platform-core"]
    def documentation = "http://grails-plugins.github.io/grails-platform-core/"
    def issueManagement = [system: "JIRA", url: "http://jira.grails.org/browse/GPPLATFORMCORE"]

    def developers = [
            [name: "Marc Palmer", email: "marc@grailsrocks.com"],
            [name: "Graeme Rocher", email: "grocher@gopivotal.com"],
            [name: "Stéphane Maldini", email: "smaldini@gopivotal.com"],
            [name: "Sudhir Nimavat", email: "sudhir_nimavat@yahoo.com"]
    ]

    //def loadBefore = ['core'] // Before rest of beans are initialized
    def loadAfter = ['hibernate', 'hibernate4', 'hibernate5']

    def pluginExcludes = [
            "grails-app/domain/org/grails/plugin/platform/test/**/*.groovy",
            "grails-app/controllers/org/grails/plugin/platform/test/**/*.groovy",
            "grails-app/services/org/grails/plugin/platform/test/**/*.groovy"
    ]

    def license = "APACHE"


    @Override
    Closure doWithSpring() { { ->
            PlatformCorePluginSupport.doWithSpring(delegate)
        }
    }

    @Override
    void doWithApplicationContext() {
       PlatformCorePluginSupport.doWithApplicationContext(grailsApplication)
    }

    //TODO
    def onChange = { event ->
        /*
        def ctx = event.application.mainContext
        def config = event.application.config.plugin.platformCore

        def eventArtefactType = getEventsArtefactHandler().TYPE

        if (event.source instanceof Class) {
            if (!config.events.disabled && grailsApplication.isArtefactOfType(eventArtefactType, event.source)) {
                event.application.addArtefact(eventArtefactType, event.source)
                ctx.grailsEvents.reloadListeners()
            }
            else if (!config.events.disabled && grailsApplication.isServiceClass(event.source)) {
                ctx.grailsEvents.reloadListener(event.source)
            }

        }
        */
    }

}
