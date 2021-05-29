package dev.cerus.jdasc.http;

import java.util.Arrays;
import okhttp3.Request;
import okhttp3.Response;

public class DiscordApiException extends RuntimeException {

    private final int[] expectedStatusCodes;
    private final Request request;
    private final Response response;

    public DiscordApiException(final int[] expectedStatusCodes, final Request request, final Response response) {
        super("Expected one of " + Arrays.toString(expectedStatusCodes) + " but got " + response.code());
        this.expectedStatusCodes = expectedStatusCodes;
        this.request = request;
        this.response = response;
    }

    public int[] getExpectedStatusCodes() {
        return this.expectedStatusCodes;
    }

    public Request getRequest() {
        return this.request;
    }

    public Response getResponse() {
        return this.response;
    }

}
