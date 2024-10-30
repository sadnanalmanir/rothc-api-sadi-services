package uk.ac.rothamsted.ide;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.rdf.model.Resource;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import junit.framework.TestCase;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class FetchRothcMonthlyDataCinpParamsTest extends TestCase {
    String url = "https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/";

    Dotenv dotenv = Dotenv.configure().load();

    String nwfp_data_api_key = dotenv.get("NWFP_DATA_API_KEY");
    String nwfp_data_api_secret = dotenv.get("NWFP_DATA_API_SECRET");

    String startDateValue = "2011-01-01";
    String endDateValue = "2012-12-31";
    String catchmentIdValue = "\"5\"";
    String fieldIdValue = "\"1\"";


    String body = "{\n" +
            "    \"startDate\": \"" +startDateValue+ "\",\n" +
            "    \"endDate\": \""+endDateValue+"\",\n" +
            "    \"catchmentID\": "+catchmentIdValue+",\n" +
            "    \"fieldid\": "+fieldIdValue+"\n" +
            "}";

    public void testEnsureAPICallReturnStatusCode200() throws Exception {

        if (nwfp_data_api_key == null || nwfp_data_api_secret == null)
            throw new DotenvException("Credentials missing for the North Wyke Farm Platform DATA API");

        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        post.addHeader(nwfp_data_api_key, nwfp_data_api_secret);

        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            assertEquals(200, response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void testEnsureThatJsonIsReturnedAsContentType() throws Exception {

        if (nwfp_data_api_key == null || nwfp_data_api_secret == null)
            throw new DotenvException("Credentials missing for the North Wyke Farm Platform DATA API");

        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        post.addHeader(nwfp_data_api_key, nwfp_data_api_secret);

        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            for (Header header : response.getAllHeaders()) {
                if (header.getName().equalsIgnoreCase(HttpHeaders.CONTENT_TYPE)) {
                    assertEquals("application/json", header.getValue());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void testEnsureThatCinpValueEquals0166and0168() throws Exception {

        if (nwfp_data_api_key == null || nwfp_data_api_secret == null)
            throw new DotenvException("Credentials missing for the North Wyke Farm Platform DATA API");

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
                    assertEquals(0.168, cinpVal, 0.003f);
                    assertEquals(0.166, cinpVal, 0.003f);
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
}