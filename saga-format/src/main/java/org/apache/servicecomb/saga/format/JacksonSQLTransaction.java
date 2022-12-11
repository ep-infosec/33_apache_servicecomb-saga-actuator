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

import org.apache.servicecomb.saga.core.Transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JacksonSQLTransaction extends JacksonSQLOperation implements Transaction {

  public JacksonSQLTransaction(String sql, List<List<String>> params) {
    this(INFINITE_RETRY, sql, params);
  }

  @JsonCreator
  public JacksonSQLTransaction(
      @JsonProperty("retries") int retries,
      @JsonProperty("sql") String sql,
      @JsonProperty("params") List<List<String>> params) {
    super(sql, retries <= 0? INFINITE_RETRY : retries, params);
  }
}
