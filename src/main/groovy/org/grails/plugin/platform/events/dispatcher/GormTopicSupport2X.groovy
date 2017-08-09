/* Copyright 2011-2013 the original author or authors:
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
package org.grails.plugin.platform.events.dispatcher

import grails.util.GrailsNameUtils
import groovy.transform.CompileStatic
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Stephane Maldini <smaldini@vmware.com>
 * @version 1.0
 * @file
 * @date 03/01/12
 * @section DESCRIPTION
 * <p/>
 * [Does stuff]
 */
@CompileStatic
class GormTopicSupport2X implements GormTopicSupport {

    public static final String GDM_EVENT_PACKAGE = 'org.grails.datastore'
    public static final String EVENT_SUFFIX = "Event"

    //See http://gorm.grails.org/latest/api/org/grails/datastore/mapping/engine/event/package-summary.html for available events
    private Map<Class, String> eventClassToTopic = new ConcurrentHashMap<Class, String>().withDefault { Class eventClass ->
        def clsName = eventClass.name
        String logicalName = GrailsNameUtils.getLogicalPropertyName(clsName, EVENT_SUFFIX)
        if(clsName.startsWith(GDM_EVENT_PACKAGE)) {
            return logicalName
        } else {
            return null
        }

    }

    void processCancel(AbstractPersistenceEvent evt, def returnValue){
        if(evt != null & returnValue != null) {
            if(List.isAssignableFrom(returnValue.getClass())){
                for(Object val in returnValue){
                    if(!val){
                        evt.cancel()
                        break
                    }
                }
            }else if(!((Boolean)returnValue)){
                evt.cancel()
            }
        }
    }

    String convertTopic(AbstractPersistenceEvent evt) {
      return eventClassToTopic[evt.getClass()]
    }

    Object extractEntity(AbstractPersistenceEvent source) {
        //workaround for document db and hibernate gorm events
        source.entityObject ?: source.entityAccess?.entity
    }
}
