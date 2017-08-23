/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.jmeter.report.writers;

import org.apache.jmeter.testelement.TestElement;

/**
 * HTMLReportWriter is a basic report writer that produces HTML pages.
 * It contains all the necessary helper method to write out the report.
 *
 */
public class HTMLReportWriter extends AbstractReportWriter {

    private static final long serialVersionUID = 240L;

    public HTMLReportWriter() {
        super();
    }

    @Override
    public ReportSummary writeReport(TestElement element) {
        return null;
    }

}
