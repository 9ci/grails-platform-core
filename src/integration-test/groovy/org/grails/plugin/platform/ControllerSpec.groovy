package org.grails.plugin.platform

import grails.util.GrailsWebMockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

/**
 * Created by sudhir on 26/12/16.
 */
class ControllerSpec extends Specification {

	@Autowired
	WebApplicationContext ctx

	def setup() {
		GrailsWebMockUtil.bindMockWebRequest(ctx)
	}

	def cleanup() {
		RequestContextHolder.resetRequestAttributes()
	}
}
