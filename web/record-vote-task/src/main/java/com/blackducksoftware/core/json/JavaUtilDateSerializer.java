/**
 *
 */
package com.blackducksoftware.core.json;

import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author kkelley
 *
 */
public class JavaUtilDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException,
            JsonProcessingException {
        if (value != null) {
            DateTime dt = new DateTime(value);
            jgen.writeString(ISODateTimeFormat.dateTime().withZoneUTC().print(dt));
        }
    }
}
