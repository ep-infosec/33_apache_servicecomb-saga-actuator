/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.saga.format;

import java.util.List;

import org.apache.servicecomb.saga.core.Operation;
import org.apache.servicecomb.saga.core.SQLOperation;
import org.apache.servicecomb.saga.core.SagaResponse;
import org.apache.servicecomb.saga.transports.SQLTransport;
import org.apache.servicecomb.saga.transports.TransportFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JacksonSQLOperation extends SQLOperation implements TransportAware<SQLTransport> {

  @JsonIgnore
  private SQLTransport transport;

  public JacksonSQLOperation(String sql, int retries, List<List<String>> params) {
    super(sql, retries, params);
  }

  @Override
  public Operation with(TransportFactory<SQLTransport> transport) {
    this.transport = transport.getTransport();
    return this;
  }

  @Override
  public SagaResponse send(String datasource) {
    return transport.with(datasource, sql(), params());
  }
}
