package org.grails.plugin.platform

import grails.core.GrailsApplication
import org.grails.plugin.platform.events.EventsImpl

import org.grails.plugin.platform.events.dispatcher.GormTopicSupport2X
import org.grails.plugin.platform.events.publisher.DefaultEventsPublisher
import org.grails.plugin.platform.events.publisher.GormBridgePublisher
import org.grails.plugin.platform.events.registry.DefaultEventsRegistry

/**
 * Created by sudhir on 26/12/16.
 */
class PlatformCorePluginSupport {
	private static Closure beans = { ->
		xmlns task: "http://www.springframework.org/schema/task"
		def config = grailsApplication.config.plugin.platformcore
		String grailsVersion = grailsApplication.metadata['info.app.grailsVersion']

		// Events API
		if (!config.events.disabled) {
			task.executor(id: "grailsTopicExecutor", 'pool-size': config.events.poolSize)

			//init api bean
			grailsEventsRegistry(DefaultEventsRegistry)
			grailsEventsPublisher(DefaultEventsPublisher) {
				grailsEventsRegistry = ref('grailsEventsRegistry')
				persistenceInterceptor = ref("persistenceInterceptor")
				catchFlushExceptions = config.events.catchFlushExceptions ?: true
			}

			platformcoreEvents(EventsImpl) {
				grailsApplication = ref('grailsApplication')
				grailsEventsRegistry = ref('grailsEventsRegistry')
				grailsEventsPublisher = ref('grailsEventsPublisher')
			}

			if (!config.events.gorm.disabled) {
				gormTopicSupport(GormTopicSupport2X)
				grailsEventsGormBridge(GormBridgePublisher) {
					gormTopicSupport = ref("gormTopicSupport")
					grailsEvents = ref("platformcoreEvents")
				}

			}
		}

	}


	public static doWithSpring(def delegate) {
		beans.delegate = delegate
		beans.call()
	}

	static void doWithApplicationContext(GrailsApplication grailsApplication) {
		def config = grailsApplication.config.plugin.platformcore
		if (!config.events.disabled) {
			grailsApplication.mainContext.platformcoreEvents.reloadListeners()
		}
	}

}
