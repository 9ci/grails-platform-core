package org.grails.plugin.platform.events

import grails.core.GrailsApplication
import grails.util.Holders
import grails.artefact.Enhances
import grails.plugins.metadata.GrailsPlugin
import grails.util.GrailsNameUtils
import groovy.transform.CompileStatic
import org.grails.core.artefact.ControllerArtefactHandler
import org.grails.core.artefact.DomainClassArtefactHandler
import org.grails.core.artefact.ServiceArtefactHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

import javax.persistence.Transient
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@CompileStatic
@Enhances([ControllerArtefactHandler.TYPE, ServiceArtefactHandler.TYPE])
trait PlatFormCoreEventsTrait {

	@Transient
	private String _cachedPluginName

	public EventReply event(String topic, Closure callback) {
		this.event(topic, null, null, callback)
	}

	public EventReply event(String topic, def data, Closure callback) {
		this.event(topic, data, null, callback)
	}

	public EventReply event(String topic, data = null, Map params = null, Closure callback = null) {
		params = params ?: [plugin:getPluginName()]
		platformcoreEvents.event(null, topic, data, params, callback)
	}

	public EventReply event( Map args, Closure callback = null) {
		args = args ?: [plugin: getPluginName()]
		String ns = (String)(args.remove('for') ?: args.remove('namespace'))
		String topic = (String)args.remove('topic')
		def data = args.remove('data')
		Map p = (Map) args.remove('params') ?: args
		platformcoreEvents.event(ns, topic, data, p, callback)
	}

	public Object[] waitFor(EventReply[] replies) throws ExecutionException, InterruptedException, TimeoutException {
		platformcoreEvents.waitFor(replies)
	}

	public Object[] waitFor(long l, TimeUnit timeUnit, EventReply[] replies) throws ExecutionException, InterruptedException, TimeoutException {
		platformcoreEvents.waitFor(l, timeUnit, replies)
	}

	
	public Events getPlatformcoreEvents() {
		return (Events)Holders.grailsApplication.mainContext.getBean("platformcoreEvents")
	}

	private String getPluginName() {
		if(_cachedPluginName == null) {
			GrailsPlugin annotation = (GrailsPlugin)getClass().getAnnotation(GrailsPlugin)
			if(annotation) {
				_cachedPluginName = GrailsNameUtils.getPropertyNameForLowerCaseHyphenSeparatedName(annotation.name())
			}
		}
		return _cachedPluginName
	}
}
