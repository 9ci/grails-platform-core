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
package org.grails.plugin.platform.test

import grails.events.Listener
import groovy.transform.CompileStatic
import org.grails.plugin.platform.events.EventMessage

/**
 * @file
 * @author Stephane Maldini <smaldini@vmware.com>
 * @version 1.0
 * @date 02/01/12

 * @section DESCRIPTION
 *
 * [Does stuff]
 */
class TestService {

    @Listener(topic = "testSync", namespace = "test")
    Map testListenerSync(Map data) {
        data["called"] = true
        return data
    }

    @Listener(topic = "testAsync", namespace = "test")
    Map testListenerAsyncOne(Map data) {
        data["listener1"] = true
        return data
    }

    @Listener(topic = "testAsync", namespace = "test")
    Map testListenerAsyncTwo(Map data) {
        data["listener2"] = true
        return data
    }

    @Listener(namespace = "test", topic = "listenerWithError")
    void testListenerWhichThrowsError() {
        throw new RuntimeException("test")
    }

    @Listener(topic = 'validation', namespace = 'gorm')
    void beforeInsertBook(EventMessage msg) {
        if(msg.data instanceof Book) {
            Book book = msg.data
            book.testProperty = "event"
        }
    }

    @Listener(topic = 'validation', namespace = 'gorm')
    @CompileStatic
    void beforeInsertAuthor(Author author) {
        author.testProperty = "event"
    }

    @Listener(namespace = 'gorm')
    void postInsert(Author author) {
        println "after insert author -  $author.name"
    }

}
