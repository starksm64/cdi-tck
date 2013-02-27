/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.deployment.packaging.ear.modules;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.ProcessSessionBean;

/**
 * @author Martin Kouba
 */
public class ContainerEventsObserver implements Extension {

    private static boolean isAfterDeploymentValidationOk = false;
    private static boolean isBeforeBeanDiscoveryOk = false;
    private static boolean isAfterBeanDiscoveryOk = false;
    private static boolean isProcessAnnotatedTypeOk = false;
    private static boolean isProcessInjectionPointOk = false;
    private static boolean isProcessInjectionTargetOk = false;
    private static boolean isProcessProducerOk = false;
    private static boolean isProcessBeanAttributesOk = false;
    private static boolean isProcessManagedBeanOk = false;
    private static boolean isProcessObserverMethodOk = false;

    public void observeAfterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
        isAfterDeploymentValidationOk = (beanManager != null);
    }

    public void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) {
        isBeforeBeanDiscoveryOk = (beanManager != null);
    }

    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        isAfterBeanDiscoveryOk = (beanManager != null);
    }

    public void observeProcessAnnotatedType(@Observes ProcessAnnotatedType<Foo> event, BeanManager beanManager) {
        isProcessAnnotatedTypeOk = (beanManager != null);
    }

    public void observeProcessInjectionPoint(@Observes ProcessInjectionPoint<Bar, Business> event, BeanManager beanManager) {
        isProcessInjectionPointOk = (beanManager != null);
    }

    public void observeProcessInjectionTarget(@Observes ProcessInjectionTarget<Foo> event, BeanManager beanManager) {
        isProcessInjectionTargetOk = (beanManager != null);
    }

    public void observeProcessProducer(@Observes ProcessProducer<LegacyServiceProducer, LegacyService> event,
            BeanManager beanManager) {
        isProcessProducerOk = (beanManager != null);
    }

    public void observeProcessBeanAttributes(@Observes ProcessBeanAttributes<Foo> event, BeanManager beanManager) {
        isProcessBeanAttributesOk = (beanManager != null);
    }

    public void observeProcessBean(@Observes ProcessSessionBean<Foo> event, BeanManager beanManager) {
        isProcessManagedBeanOk = (beanManager != null);
    }

    public void observeProcessObserverMethod(@Observes ProcessObserverMethod<BusinessOperationEvent, Bar> event,
            BeanManager beanManager) {
        isProcessObserverMethodOk = (beanManager != null);
    }

    public static boolean allEventsOk() {
        return isAfterDeploymentValidationOk && isBeforeBeanDiscoveryOk && isAfterBeanDiscoveryOk
                && isProcessAnnotatedTypeOk && isProcessInjectionPointOk
                && isProcessInjectionTargetOk && isProcessProducerOk && isProcessBeanAttributesOk && isProcessManagedBeanOk
                && isProcessObserverMethodOk;
    }

}
