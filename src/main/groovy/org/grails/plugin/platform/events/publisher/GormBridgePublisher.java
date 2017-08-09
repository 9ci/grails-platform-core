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
package org.grails.plugin.platform.events.publisher;

import org.apache.log4j.Logger;
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent;
import org.grails.plugin.platform.events.EventReply;
import org.grails.plugin.platform.events.Events;
import org.grails.plugin.platform.events.dispatcher.GormTopicSupport;
import org.grails.plugin.platform.events.dispatcher.GormTopicSupport2X;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephane Maldini <smaldini@vmware.com>
 * @version 1.0
 * @file
 * @date 29/05/12
 * @section DESCRIPTION
 * <p/>
 * [Does stuff]
 */
public class GormBridgePublisher implements ApplicationListener<ApplicationEvent> {

    private GormTopicSupport gormTopicSupport;
    private Events grailsEvents;

    private final static Logger log = Logger.getLogger(GormBridgePublisher.class);

    public void setGrailsEvents(Events grailsEvents) {
        this.grailsEvents = grailsEvents;
    }

    public void setGormTopicSupport(GormTopicSupport gormTopicSupport) {
        this.gormTopicSupport = gormTopicSupport;
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent.getClass().getName().startsWith(GormTopicSupport2X.GDM_EVENT_PACKAGE)) {
            String topic = gormTopicSupport.convertTopic((AbstractPersistenceEvent) applicationEvent);
            if (topic != null) {
                log.debug("sending " + applicationEvent + " to topic " + topic);

                Map<String, Object> params = new HashMap<String, Object>();
                params.put(EventsPublisher.GORM, false);
                params.put(EventsPublisher.FORK, false);

                EventReply reply = grailsEvents.event(GormTopicSupport.GORM_SOURCE, topic, gormTopicSupport.extractEntity((AbstractPersistenceEvent) applicationEvent), params);
                try {
                    gormTopicSupport.processCancel((AbstractPersistenceEvent) applicationEvent, reply != null ? reply.getValues() : null);
                } catch (Throwable e) {
                    throw new RuntimeException(e);//shouldn't happen as its sync event
                }
            }
        }
    }
}
