package nl.utwente.di.OVSoftware;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Provider
@Produces("text/csv")
@Consumes("text/csv")


public class CsvWriter implements MessageBodyWriter, MessageBodyReader {

	@Override
	public long getSize(Object arg0, Class arg1, Type arg2, Annotation[] arg3, MediaType arg4) {
		return 0;
	}
	
	@Override
	public boolean isReadable(Class arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return true;
	}

	@Override
	public boolean isWriteable(Class arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return true;
	}

	@Override
	public void writeTo(Object o, Class arg1, Type arg2, Annotation[] arg3, MediaType arg4, MultivaluedMap arg5,
			OutputStream out) throws IOException, WebApplicationException {
		if (o instanceof List<?>) {
			List<?> data = (List<?>) o;
			if (data != null && data.size() > 0) {
				CsvMapper m = new CsvMapper();
				CsvSchema s = m.schemaFor(data.get(0).getClass()).withHeader();
				m.writer(s).writeValue(out, data);
			}
		}
	}

	@Override
	public List<Object> readFrom(Class arg0, Type type, Annotation[] arg2, MediaType arg3, MultivaluedMap arg4,
			InputStream in) throws IOException, WebApplicationException {
		CsvMapper m = new CsvMapper();
        Class csvClass = (Class) (((ParameterizedType) type).getActualTypeArguments())[0];
        CsvSchema s = m.schemaFor(csvClass).withHeader();
        List<Object> l = m.reader(csvClass).with(s).readValues(in).readAll();
        return null;
	}

}
