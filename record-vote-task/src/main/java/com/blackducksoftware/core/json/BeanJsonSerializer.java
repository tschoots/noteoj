package com.blackducksoftware.core.json;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.blackducksoftware.core.exception.BlackDuckServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Bean Json serializer. Use this util when storing beans in json pre-serialized format.
 * 
 * This code was ported (extracted from original class) from codesight project.
 * 
 * @see <a
 *      href="http://scm/gitweb/?p=engineering_git/product-core.git;a=blob;f=core.core/src/com/blackducksoftware/core/dao/customtype/AbstractJsonUserType.java;hb=HEAD">AbstractJsonUserType.java</a>
 * 
 * @author ydeboeck
 * @author pguzun
 * 
 */

public class BeanJsonSerializer {

    private final ObjectMapper objectMapper = JsonUtil.configuredObjectMapper();

    public <T> T getBean(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new BlackDuckServerException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public <T> String getJson(T bean) {
        if (bean == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException ex) {
            throw new BlackDuckServerException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

}
