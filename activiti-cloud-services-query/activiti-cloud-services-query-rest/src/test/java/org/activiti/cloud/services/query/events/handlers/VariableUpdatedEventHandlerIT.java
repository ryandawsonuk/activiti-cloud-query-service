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

package org.activiti.cloud.services.query.events.handlers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.app.repository.VariableRepository;
import org.activiti.cloud.services.query.events.VariableUpdatedEvent;
import org.activiti.cloud.services.query.model.QVariable;
import org.activiti.cloud.services.query.model.Variable;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql=true)
@Sql(value="classpath:/jpa-test.sql")
public class VariableUpdatedEventHandlerIT {

    @Autowired
    private VariableRepository repository;

    @Autowired
    private VariableUpdatedEventHandler handler;

    @SpringBootConfiguration
    @EnableJpaRepositories(basePackageClasses = VariableRepository.class)
    @EntityScan(basePackageClasses = Variable.class)
    @Import({
        VariableUpdatedEventHandler.class, 
        ProcessVariableUpdateEventHandler.class,
        TaskVariableUpdatedEventHandler.class,
        VariableUpdater.class,
        EntityFinder.class
    })
    static class Configuation {
    }


    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void contextLoads() {
        // Should pass
    }

    @Test
    public void handleShouldUpdateAndStoreVariable() throws Exception {
        //given
    	String executionId = "10";
        String processInstanceId = "0";
        String taskId = "1";
        String variableName = "variable1";
        String variableType = String.class.getName();
        VariableUpdatedEvent event = new VariableUpdatedEvent(System.currentTimeMillis(),
                                                              "variableUpdated",
                                                              executionId,
                                                              "process_definition_id",
                                                              processInstanceId,
                                                    "runtime-bundle-a",
                                                    "runtime-bundle-a",
                                                    "runtime-bundle",
                                                    "1",
                                                    null,
                                                    null,
                                                              variableName,
                                                              "newValue",
                                                              variableType,
                                                              taskId);

        //when
        handler.handle(event);

        //then
        Optional<Variable> result=repository.findOne(QVariable.variable.name.eq(variableName));
        
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getValue()).isEqualTo("newValue");
        assertThat(result.get().getProcessInstance()).isNotNull();
        assertThat(result.get().getTask()).isNotNull();
    }	

    @Test
    public void handleShouldUpdateAndStoreProcessInstanceVariable() throws Exception {
        //given
    	String executionId = "10";
        String processInstanceId = "1";
        String taskId = null;
        String variableName = "initiator";
        String variableType = String.class.getName();
        VariableUpdatedEvent event = new VariableUpdatedEvent(System.currentTimeMillis(),
                                                              "variableUpdated",
                                                              executionId,
                                                              "process_definition_id",
                                                              processInstanceId,
                                                    "runtime-bundle-a",
                                                    "runtime-bundle-a",
                                                    "runtime-bundle",
                                                    "1",
                                                    null,
                                                    null,
                                                              variableName,
                                                              "newValue",
                                                              variableType,
                                                              taskId);

        //when
        handler.handle(event);

        //then
        Optional<Variable> result=repository.findOne(QVariable.variable.name.eq(variableName));
        
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getValue()).isEqualTo("newValue");
        assertThat(result.get().getProcessInstance()).isNotNull();
        assertThat(result.get().getTask()).isNull();
    }	

    /* having to temporarily remove to resolve https://github.com/Activiti/Activiti/issues/1539
    @Test(expected=ActivitiException.class)
    public void handleShouldFailUpdateAndStoreVariableWithNonExistingProcessInstanceReference() throws Exception {
        //given
    	String executionId = "10";
        String processInstanceId = "-1"; // does not exist
        String taskId = null;
        String variableName = "var";
        String variableType = String.class.getName();
        VariableUpdatedEvent event = new VariableUpdatedEvent(System.currentTimeMillis(),
                                                              "variableUpdated",
                                                              executionId,
                                                              "process_definition_id",
                                                              processInstanceId,
                                                              variableName,
                                                              "content",
                                                              variableType,
                                                              taskId);

        //when
        handler.handle(event);

        //then
        // should throw ActivitiException
    }
    
    @Test(expected=ActivitiException.class)
    public void handleShouldFailUpdateAndStoreVariableWithNonExistingTaskReference() throws Exception {
        //given
    	String executionId = "10";
        String processInstanceId = "-1"; // does not exist
        String taskId = "-1";
        String variableName = "var";
        String variableType = String.class.getName();
        VariableUpdatedEvent event = new VariableUpdatedEvent(System.currentTimeMillis(),
                                                              "variableUpdated",
                                                              executionId,
                                                              "process_definition_id",
                                                              processInstanceId,
                                                              variableName,
                                                              "content",
                                                              variableType,
                                                              taskId);

        //when
        handler.handle(event);

        //then
        // should throw ActivitiException
    }	    */
}
