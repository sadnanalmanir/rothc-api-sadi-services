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

@Name("fetchRothcInputParams")
@Description("Rothc SADI services: Fetch non-monthly data values based on the start date, end date, and id of catchment and field")
@ContactEmail("sadnanalmanir@gmail.com")
@InputClass("http://localhost:8080/ontology/service-ontology/fetchRothcInputParams.owl#Input")
@OutputClass("http://localhost:8080/ontology/service-ontology/fetchRothcInputParams.owl#Output")
public class FetchRothcInputParams extends SimpleSynchronousServiceServlet {
    private static final Logger log = Logger.getLogger(FetchRothcInputParams.class);

    @Override
    public void processInput(Resource input, Resource output) {

        String apiKey="X-Api-Key";
        String apiValue="243fa5b-eb83-470b-aa36-0f3c2566114b";
        /*
        Properties properties = new Properties();
        try (InputStream inputStream = FetchRothcInputParams.class.getResourceAsStream("rothc-api-access.properties")) {
            properties.load(inputStream);
            apiKey = properties.getProperty("apiKey");
            apiValue = properties.getProperty("apiValue");
            System.out.println("hello " + apiKey + " " + apiValue);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
         */

        //PropertyConfigurator.configure(log.getClass().getClassLoader().getResource("log4j.properties"));

        log.info("*** SADI Service ***");
        log.info("Invoking SADI service:  fetchRothcInputParams");
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
        String catchmentIdValue = input.getPropertyResourceValue(Vocab.has_catchmentId).getRequiredProperty(Vocab.has_value).getString();
        if (catchmentIdValue == null){
            log.info("Failed to extract catchment Id from: "
                    + input.getLocalName() + " -> " + Vocab.has_catchmentId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
            throw new IllegalArgumentException("Failed to extract catchment Id from: "
                    + input.getLocalName() + " -> " + Vocab.has_catchmentId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
        }
        String fieldIdValue = input.getPropertyResourceValue(Vocab.has_fieldId).getRequiredProperty(Vocab.has_value).getString();
        if (fieldIdValue == null){
            log.info("Failed to extract field id from: "
                    + input.getLocalName() + " -> " + Vocab.has_fieldId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
            throw new IllegalArgumentException("Failed to extract start field id from: "
                    + input.getLocalName() + " -> " + Vocab.has_fieldId.getLocalName() + " -> " + Vocab.has_value.getLocalName());
        }
        String body = "{\n" +
                "    \"startDate\": \"" +startDateValue+ "\",\n" +
                "    \"endDate\": \""+endDateValue+"\",\n" +
                "    \"catchmentID\": \""+catchmentIdValue+"\",\n" +
                "    \"fieldID\": "+fieldIdValue+"\n" +
                "}";
        log.info("Data to send via POST method: " +body);



        String endPoint = "https://149.155.17.203:8000/api/utils/v1/fetchRothcdata/";
        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
        HttpPost post = new HttpPost(endPoint);
        post.setEntity(entity);
        post.addHeader(apiKey, apiValue);
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
                int clayVal = paramsObject.get("clay").getAsInt();
                int depthVal = paramsObject.get("depth").getAsInt();
                float iomVal = paramsObject.get("iom").getAsFloat();
                int nstepsVal = paramsObject.get("nsteps").getAsInt();

                Resource clayResource = outputModel.createResource();
                clayResource.addProperty(Vocab.type, Vocab.Clay);
                clayResource.addLiteral(Vocab.has_value, clayVal);
                output.addProperty(Vocab.has_clay, clayResource);

                Resource depthResource = outputModel.createResource();
                depthResource.addProperty(Vocab.type, Vocab.Depth);
                depthResource.addLiteral(Vocab.has_value, depthVal);
                output.addProperty(Vocab.has_depth, depthResource);

                Resource iomResource = outputModel.createResource();
                iomResource.addProperty(Vocab.type, Vocab.Iom);
                iomResource.addLiteral(Vocab.has_value, iomVal);
                output.addProperty(Vocab.has_iom, iomResource);

                Resource nstepsResource = outputModel.createResource();
                nstepsResource.addProperty(Vocab.type, Vocab.Nsteps);
                nstepsResource.addLiteral(Vocab.has_value, nstepsVal);
                output.addProperty(Vocab.has_nsteps, nstepsResource);
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
        public static final Property has_clay = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_clay");
        public static final Property has_depth = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_depth");
        public static final Property has_iom = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_iom");
        public static final Property has_nsteps = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_nsteps");
        // data property
        public static final Property has_value = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_value");
        // resources
        public static final Resource StartDate = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#StartDate");
        public static final Resource EndDate = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#EndDate");
        public static final Resource FieldId = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#FieldId");
        public static final Resource CatchmentId = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#CatchmentId");
        public static final Resource Clay = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Clay");
        public static final Resource Depth = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Depth");
        public static final Resource Iom = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Iom");
        public static final Resource Nsteps = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Nsteps");
        public static final Resource Input = m_model.createResource("http://localhost:8080/ontology/service-ontology/fetchRothcInputParams.owl#Input");
        public static final Resource Output = m_model.createResource("http://localhost:8080/ontology/service-ontology/fetchRothcInputParams.owl#Output");

    }

    private static String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
}

