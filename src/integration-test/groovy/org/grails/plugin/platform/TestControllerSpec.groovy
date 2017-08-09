package org.grails.plugin.platform

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.grails.plugin.platform.test.Author
import org.grails.plugin.platform.test.Book
import org.grails.plugin.platform.test.SampleController
import org.springframework.beans.factory.annotation.Autowired
import org.grails.plugin.platform.events.EventsImpl

@Integration
@Rollback
class TestControllerSpec extends ControllerSpec {

	@Autowired
	SampleController sampleController

	@Autowired
	public EventsImpl platformcoreEvents

	def "test event return value : sync"() {
		when:
		Map resp = sampleController.testEventOneSync()

		then:
		resp != null
		resp.called == true

	}

	def "test event return multiple values : async"() {
		when:
		Map resp = sampleController.testEventOneAsync()

		then:
		resp != null
		resp.replies.size() == 2
		resp.replies.find({ it.listener1 == true}) != null
		resp.replies.find({ it.listener2 == true}) != null
	}

	def "test listener which throws error"() {
		when:
		boolean onErrorCalled = false
		Closure onError = { def r -> onErrorCalled = true }
		def reply = platformcoreEvents.event("test", "listenerWithError", null, [fork:true, onError: onError])
		reply.waitFor()

		then:
		onErrorCalled == true
		reply.hasErrors()
		reply.size() == 1
		reply.getErrors()[0] instanceof NullPointerException
	}

	def "test gorm events"() {
		setup:
		Book book = new Book(title: "test")
		Author author = new Author(name: "test")

		when:
		book.save(flush:true)
		author.save(flush:true)

		then:
		book.testProperty == "event"
		author.testProperty == "event"
	}

}
