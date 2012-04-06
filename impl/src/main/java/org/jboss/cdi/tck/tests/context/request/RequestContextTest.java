/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.context.request;

import static org.jboss.cdi.tck.TestGroups.CONTEXTS;
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.SERVLET;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class RequestContextTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestContextTest.class)
                .withWebResource("SimplePage.html", "SimplePage.html").build();
    }

    /**
     * The request scope is active during the service() method of any Servlet in the web application.
     */
    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertions({ @SpecAssertion(section = "6.7.1", id = "aa"), @SpecAssertion(section = "6.7.1", id = "ac") })
    public void testRequestScopeActiveDuringServiceMethod() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "serviceMethodTest");
    }

    /**
     * The request scope is active during the doFilter() method of any Filter in the web application.
     */
    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertions({ @SpecAssertion(section = "6.7.1", id = "ab"), @SpecAssertion(section = "6.7.1", id = "ac") })
    public void testRequestScopeActiveDuringServletFilter() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "SimplePage.html");
    }

    /**
     * The request context is destroyed at the end of the servlet request, after the service() method and all doFilter()
     * methods, and all requestDestroyed() and onComplete() notifications return.
     */
    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertions({ @SpecAssertion(section = "6.7.1", id = "ba"), @SpecAssertion(section = "6.7.1", id = "bb"),
            @SpecAssertion(section = "6.7.1", id = "bc") })
    public void testRequestScopeIsDestroyedAfterServletRequest() throws Exception {

        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);

        // First request - response content contains SimpleRequestBean id
        TextPage firstRequestResult = webClient.getPage(contextPath + "introspectRequest");
        assertNotNull(firstRequestResult.getContent());
        assertNotEquals(Double.parseDouble(firstRequestResult.getContent()), 0);
        // Make a second request and make sure the same context is not there (compare SimpleRequestBean ids)
        TextPage secondRequestResult = webClient.getPage(contextPath + "introspectRequest");
        assertNotNull(secondRequestResult.getContent());
        assertNotEquals(Double.parseDouble(secondRequestResult.getContent()),
                Double.parseDouble(firstRequestResult.getContent()));

        // Make sure request context is destroyed after service(), doFilter(), requestDestroyed()
        webClient.getPage(contextPath + "introspectRequest?guard=collect");
        TextPage destroyRequestResult = webClient.getPage(contextPath + "introspectRequest?guard=verify");
        assertNotNull(destroyRequestResult.getContent());
        assertEquals(destroyRequestResult.getContent(), "true");
    }

}
