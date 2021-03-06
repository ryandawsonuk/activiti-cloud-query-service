/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.cloud.services.query.notifications.graphql;

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.cloud.services.query.notifications.RoutingKeyResolver;
import org.activiti.cloud.services.query.notifications.model.ProcessEngineNotification;
import org.junit.Test;

public class SpELRoutingKeyTest {


    @Test
    public void testRoutingKey () {
        RoutingKeyResolver routingKeyResolver = new SpELTemplateRoutingKeyResolver();

        ProcessEngineNotification notification = new GraphQLProcessEngineNotification();

        notification.put("serviceName", "my-rb");
        notification.put("appName","app");
        notification.put("processDefinitionId", "1:2");
        notification.put("processInstanceId", 12);

        String routingKey = routingKeyResolver.resolveRoutingKey(notification);

        assertThat(routingKey).isEqualTo("ProcessEngineNotification.my-rb.app.1:2.12");

    }

}
