package uk.ac.rothamsted.ide;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.sadiframework.service.annotations.*;
import org.sadiframework.service.simple.SimpleSynchronousServiceServlet;

import java.io.IOException;

@Name("fetchRothcInputDepthParams")
@Description("Rothc SADI services: Fetch non-monthly data values based on the start date, end date, and id of catchment and field")
@ContactEmail("sadnanalmanir@gmail.com")
@InputClass("http://localhost:8080/ontology/service-ontology/fetchRothcInputDepthParams.owl#Input")
@OutputClass("http://localhost:8080/ontology/service-ontology/fetchRothcInputDepthParams.owl#Output")
public class FetchRothcInputDepthParams extends SimpleSynchronousServiceServlet {
    private static final Logger log = Logger.getLogger(FetchRothcInputDepthParams.class);

    @Override
    public void processInput(Resource input, Resource output) {

        Dotenv dotenv = Dotenv.configure().load();

        String nwfp_data_api_key = dotenv.get("NWFP_DATA_API_KEY");
        String nwfp_data_api_secret = dotenv.get("NWFP_DATA_API_SECRET");

        if (nwfp_data_api_key == null || nwfp_data_api_secret == null)
            throw new DotenvException("Credentials missing for the North Wyke Farm Platform DATA API");

        log.info("*** SADI Service ***");
        log.info("Invoking SADI service:  fetchRothcInputDepthParams");

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



        String url = "https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/";

        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        post.addHeader(nwfp_data_api_key, nwfp_data_api_secret);

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
                int depthVal = paramsObject.get("depth").getAsInt();

                Resource depthResource = outputModel.createResource();
                depthResource.addProperty(Vocab.type, Vocab.Depth);
                depthResource.addLiteral(Vocab.has_value, depthVal);
                output.addProperty(Vocab.has_depth, depthResource);
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
        public static final Property has_depth = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_depth");
        // data property
        public static final Property has_value = m_model.createProperty("http://localhost:8080/ontology/domain-ontology/rothc.owl#has_value");
        // resources
        public static final Resource Depth = m_model.createResource("http://localhost:8080/ontology/domain-ontology/rothc.owl#Depth");
    }

    private static String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
}

