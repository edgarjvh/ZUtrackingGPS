package ve.com.zuprevencion.zutrackinggps;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;

public class GetDataWs {
    public Object getData(ArrayList<Object> parametros){
        Object data;
        int x = parametros.size();
        String namespace = "http://zutrackinggps/";
        String direccion = "http://cctv.zuprevencion.org:9700/zutrackinggps.asmx";
        String metodo = parametros.get(x - 1).toString();
        String soapAction = namespace + metodo;

        SoapObject request = new SoapObject(namespace, metodo);

        if (parametros.size() > 0){
            int i;
            for(i = 0; i < x - 1; i++){
                String property[];
                property = parametros.get(i).toString().split("\\*");

                PropertyInfo pi = new PropertyInfo();
                pi.setName(property[0]);
                pi.setValue(property[1]);
                pi.setType(String.class);
                request.addProperty(pi);
            }
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(direccion);

        try {
            httpTransport.call(soapAction, envelope);
            data = envelope.getResponse();
        } catch (Exception exception) {
            data = exception.toString();
        }

        return data;
    }
}