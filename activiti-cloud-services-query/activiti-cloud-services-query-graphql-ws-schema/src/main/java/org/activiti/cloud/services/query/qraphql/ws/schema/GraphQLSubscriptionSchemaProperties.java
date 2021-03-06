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
package org.activiti.cloud.services.query.qraphql.ws.schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix="spring.activiti.cloud.services.query.graphql.ws.schema")
public class GraphQLSubscriptionSchemaProperties {

    /**
     * GraphQL subscription schema file name with .graphqls extension. Defaults to activiti.graphqls
     */
    @NotBlank
    private String graphqls;

    /**
     * GraphQL subscription schema field name. Defaults to ProcessEngineNotification
     */
    @NotBlank
    private String subscriptionFieldName;

    /**
     * GraphQL subscription field comma-separated list of argument names to build hierarchical Stomp destination subscription topic.
     * Defaults to serviceName,appName,processDefinitionId,processInstanceId
     */
    @NotNull
    private String[] subscriptionArgumentNames;

    @Configuration
    @PropertySource("classpath:org/activiti/cloud/services/query/graphql/ws/schema/default.properties")
    @EnableConfigurationProperties(GraphQLSubscriptionSchemaProperties.class)
    public static class AutoConfiguration {
        // auto configures parent properties class using spring.factories
    }

    public String getGraphqls() {
        return graphqls;
    }

    public void setGraphqls(String graphqls) {
        this.graphqls = graphqls;
    }

    public String getSubscriptionFieldName() {
        return subscriptionFieldName;
    }

    public void setSubscriptionFieldName(String subscriptionFieldName) {
        this.subscriptionFieldName = subscriptionFieldName;
    }

    public String[] getSubscriptionArgumentNames() {
        return subscriptionArgumentNames;
    }

    public void setSubscriptionArgumentNames(String[] argumentNames) {
        this.subscriptionArgumentNames = argumentNames;
    }

}
