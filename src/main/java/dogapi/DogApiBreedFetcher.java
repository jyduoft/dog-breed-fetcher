package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String SUCCESS_CODE = "success";
    private static final String ERROR_CODE = "error";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException{
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.
        Request request = new Request.Builder()
                .url(String.format("https://dog.ceo/api/breed/%s/list", breed)).build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());

            if (Objects.equals(responseBody.getString(STATUS), SUCCESS_CODE)){
                JSONArray JSONbreeds = responseBody.getJSONArray(MESSAGE);
                ArrayList<String> breeds = new ArrayList<>();
                for (int i = 0; i < JSONbreeds.length(); i++) {
                    breeds.add(JSONbreeds.getString(i));
                }
                return breeds;
            }
            else {
                throw new BreedNotFoundException(breed);
            }
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}