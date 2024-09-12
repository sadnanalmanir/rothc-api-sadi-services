package uk.ac.rothamsted.ide;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.sadiframework.service.annotations.*;
import org.sadiframework.service.simple.SimpleSynchronousServiceServlet;

import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Name("fetchRothcMonthlyDataParams")
@Description("Rothc SADI services: Fetch non-monthly data values based on the start date, end date, and id of catchment and field")
@ContactEmail("sadnanalmanir@gmail.com")
@InputClass("http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataParams.owl#Input")
@OutputClass("http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataParams.owl#Output")
public class FetchRothcMonthlyDataParams extends SimpleSynchronousServiceServlet {
    private static final Logger log = Logger.getLogger(FetchRothcMonthlyDataParams.class);

    @Override
    public void processInput(Resource input, Resource output) {

        String apiSecretKey=null;
        String apiSecretValue=null;


        try (InputStream is = FetchRothcMonthlyDataParams.class.getClassLoader().getResourceAsStream("rothc-api-access.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            apiSecretKey = prop.getProperty("apiKey");
            apiSecretValue = prop.getProperty("apiValue");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        log.info("*** SADI Service ***");
        log.info("Invoking SADI service:  fetchRothcMonthlyDataParams");

        Model outputModel = output.getModel();

        String startDateValue = input.getPropertyResourceValue(Vocab.has_startDate).getRequiredProperty(Vocab.has_value).getString();
        if (startDateValue == null | startDateValue.equals("")){
            log.info("Failed to extract start date from: "
                    + input.getLocalName() + " -> " + Vocab.has_startDate.getLocalName() + " -> " + Vocab.has_value.getLocalName());
            throw new IllegalArgumentException("Failed to extract start date from: "
                    + input.getLocalName() + " -> " + Vocab.has_startDate.getLocalName() + " -> " + Vocab.has_value.getLocalName());
        }
        String endDateValue = input.getPropertyResourceValue(Vocab.has_endDate).getRequiredProperty(Vocab.has_value).getString();
        if (endDateValue == null){
            log.info("Failed to extract end date from: "
                    + input.getLocalName() + " -> " + Vocab.has_endDate.getLocalName() + " -> " + Vocab.has_value.getLocalName());
            throw new IllegalArgumentException("Failed to extract end date from: "
                    + input.getLocalName() + " -> " + Vocab.has_endDate.getLocalName() + " -> " + Vocab.has_value.getLocalName());
        }
        int catchmentIdValue = input.getPropertyResourceValue(Vocab.has_catchmentId).getRequiredProperty(Vocab.has_value).getInt();
        if (catchmentIdValue == 0){
            log.info("Failed to extract catchment Id from: "
                    + input.getLocalName() + " -> " + Vocab.has_catchmentId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
            throw new IllegalArgumentException("Failed to extract catchment Id from: "
                    + input.getLocalName() + " -> " + Vocab.has_catchmentId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
        }
        int fieldIdValue = input.getPropertyResourceValue(Vocab.has_fieldId).getRequiredProperty(Vocab.has_value).getInt();
        if (fieldIdValue == 0){
            log.info("Failed to extract field id from: "
                    + input.getLocalName() + " -> " + Vocab.has_fieldId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
            throw new IllegalArgumentException("Failed to extract start field id from: "
                    + input.getLocalName() + " -> " + Vocab.has_fieldId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
        }
        String body = "{\n" +
                "    \"startDate\": \"" +startDateValue+ "\",\n" +
                "    \"endDate\": \""+endDateValue+"\",\n" +
                "    \"catchmentID\": "+catchmentIdValue+",\n" +
                "    \"fieldid\": "+fieldIdValue+"\n" +
                "}";
        log.info("Data to send via POST method: " +body);



        String url = "http://149.155.17.203/api/utils/v1/fetchRothcdata/";

        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        post.addHeader(apiSecretKey, apiSecretValue);

        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info(response.getStatusLine().toString());
        log.info(response.getStatusLine().getStatusCode());
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            try {
                String results = EntityUtils.toString(responseEntity);
                JsonObject rootJson = new Gson().fromJson(results, JsonObject.class);
                JsonObject dataJson = rootJson.get("data").getAsJsonObject();
                JsonArray environmentDataArray = dataJson.get("environment_data").getAsJsonArray();
                JsonObject paramsObject = dataJson.get("params").getAsJsonObject();

                for (JsonElement environmentDataElement : environmentDataArray) {
                    JsonObject envJsonObject = environmentDataElement.getAsJsonObject();
                    float cinpVal = envJsonObject.get("C_inp").getAsFloat();
                    float dpmRpmVal = envJsonObject.get("DPM_RPM").getAsFloat();
                    float evapVal = envJsonObject.get("Evap").getAsFloat();
                    float fymVal = envJsonObject.get("FYM").getAsFloat();
                    int pcVal = envJsonObject.get("PC").getAsInt();

                    Resource cinpResource = outputModel.createResource();
                    cinpResource.addProperty(Vocab.type, Vocab.Cinp);
                    cinpResource.addLiteral(Vocab.has_value, cinpVal);
                    output.addProperty(Vocab.has_cinp, cinpResource);

                    Resource dpmRpmResource = outputModel.createResource();
                    dpmRpmResource.addProperty(Vocab.type, Vocab.DpmRpm);
                    dpmRpmResource.addLiteral(Vocab.has_value, dpmRpmVal);
                    output.addProperty(Vocab.has_dpmRpm, dpmRpmResource);

                    Resource evapResource = outputModel.createResource();
                    evapResource.addProperty(Vocab.type, Vocab.Evap);
                    evapResource.addLiteral(Vocab.has_value, evapVal);
                    output.addProperty(Vocab.has_evap, evapResource);

                    Resource fymResource = outputModel.createResource();
                    fymResource.addProperty(Vocab.type, Vocab.Fym);
                    fymResource.addLiteral(Vocab.has_value, fymVal);
                    output.addProperty(Vocab.has_fym, fymResource);

                    Resource pcResource = outputModel.createResource();
                    pcResource.addProperty(Vocab.type, Vocab.Pc);
                    pcResource.addLiteral(Vocab.has_value, pcVal);
                    output.addProperty(Vocab.has_pc, pcResource);
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            response.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static final class Vocab {
        private static final Model m_model = ModelFactory.createDefaultModel();
        // object properties
        public static final Property type = m_model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        public static final Property has_startDate = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_startDate");
        public static final Property has_endDate = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_endDate");
        public static final Property has_fieldId = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_fieldId");
        public static final Property has_catchmentId = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_catchmentId");
        public static final Property has_cinp = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_cinp");
        public static final Property has_dpmRpm = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_dpmRpm");
        public static final Property has_evap = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_evap");
        public static final Property has_fym = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_fym");
        public static final Property has_pc = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_pc");
        public static final Property has_rain = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_rain");
        public static final Property has_tmp = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_tmp");
        public static final Property has_modern = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_modern");
        public static final Property has_month = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_month");
        public static final Property has_year = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_year");
        // data property
        public static final Property has_value = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_value");
        // resources
        public static final Resource Cinp = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Cinp");
        public static final Resource DpmRpm = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#DpmRpm");
        public static final Resource Evap = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Evap");
        public static final Resource Fym = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Fym");
        public static final Resource Pc = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Pc");
        public static final Resource Rain = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Rain");
        public static final Resource Tmp = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Tmp");
        public static final Resource Modern = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Modern");
        public static final Resource Month = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Month");
        public static final Resource Year = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Year");
    }
}


