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
 */

package org.apache.jmeter.monitor.model;

/**
 * @version $Revision: 1763847 $
 *
 * @deprecated since 3.1
 */
@Deprecated
public interface Worker {
    int getRequestProcessingTime();

    void setRequestProcessingTime(int value);

    long getRequestBytesSent();

    void setRequestBytesSent(long value);

    String getCurrentQueryString();

    void setCurrentQueryString(String value);

    String getRemoteAddr();

    void setRemoteAddr(String value);

    String getCurrentUri();

    void setCurrentUri(String value);

    String getStage();

    void setStage(String value);

    String getVirtualHost();

    void setVirtualHost(String value);

    String getProtocol();

    void setProtocol(String value);

    long getRequestBytesReceived();

    void setRequestBytesReceived(long value);

    String getMethod();

    void setMethod(String value);

}
