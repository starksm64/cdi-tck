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
package org.jboss.cdi.tck.tests.interceptors.definition.broken.sameClassListedTwiceInBeansXml;

import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;

import javax.enterprise.inject.spi.DeploymentException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class SameClassListedTwiceInBeansXmlTest extends AbstractTest {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SameClassListedTwiceInBeansXmlTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors()
                                .clazz(FordInterceptor.class.getName(), FordInterceptor.class.getName()).up()).build();
    }

    @Test
    @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "e")
    public void testSameInterceptorClassListedTwiceInBeansXmlNotOk() {
    }
}
