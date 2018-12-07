/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.siddhi.query.api.annotation;

import org.wso2.siddhi.query.api.SiddhiElement;
import org.wso2.siddhi.query.api.util.SiddhiConstants;

/**
 * Annotation element
 */
public class Element implements SiddhiElement {

    private static final long serialVersionUID = 1L;
    private static final String KEY_VALUE_SEPARATOR = " = ";

    private final String key;
    private final String value;
    private int[] queryContextStartIndex;
    private int[] queryContextEndIndex;

    public Element(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (key != null) {
            return key + KEY_VALUE_SEPARATOR + "\"" + value + "\"";
        } else {
            return "\"" + value + "\"";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Element)) {
            return false;
        }

        Element element = (Element) o;

        if (key != null ? !key.equals(element.key) : element.key != null) {
            return false;
        }
        if (value != null ? !value.equals(element.value) : element.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public int[] getQueryContextStartIndex() {
        return queryContextStartIndex;
    }

    @Override
    public void setQueryContextStartIndex(int[] lineAndColumn) {
        queryContextStartIndex = lineAndColumn;
    }

    @Override
    public int[] getQueryContextEndIndex() {
        return queryContextEndIndex;
    }

    @Override
    public void setQueryContextEndIndex(int[] lineAndColumn) {
        queryContextEndIndex = lineAndColumn;
    }

    /**
     *Converts an element in string format to another string with escape characters
     * which is the siddhi app code representation of the element.
     * @param elementStr The element in String format. E.g. title = "The wonder of "foo""
     * @return The code representation of element. E.g. title = """The wonder of "foo""""
     */
    public static String toStringWithEscapeChars(String elementStr) {
        String key = null, value;
        if (elementStr == null || elementStr.isEmpty()) {
            throw new IllegalArgumentException("Input string is either null or empty.");
        }
        String[] keyValuePair = elementStr.split(KEY_VALUE_SEPARATOR);
        if (keyValuePair.length == 1) {
            value = keyValuePair[0].trim().substring(1, keyValuePair[0].length() - 1);
        } else if (keyValuePair.length == 2) {
            key = keyValuePair[0];
            value = keyValuePair[1].trim().substring(1, keyValuePair[1].length() - 1);
        } else {
            throw new IllegalArgumentException("Could not convert to Element object. String format is invalid: "
                    + elementStr);
        }

        StringBuilder valueWithQuotes = new StringBuilder();
        if (value != null && (value.contains("\"") || value.contains("\n"))) {
            valueWithQuotes.append(SiddhiConstants.ESCAPE_SEQUENCE).append(value)
                    .append(SiddhiConstants.ESCAPE_SEQUENCE);
        } else {
            valueWithQuotes.append(SiddhiConstants.DOUBLE_QUOTE).append(value)
                    .append(SiddhiConstants.DOUBLE_QUOTE);
        }
        if (key != null) {
            return key + KEY_VALUE_SEPARATOR + valueWithQuotes.toString();
        } else {
            return valueWithQuotes.toString();
        }
    }
}
