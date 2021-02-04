package de.cerus.jdasc.http;

import java.util.Arrays;
import okhttp3.Request;
import okhttp3.Response;

public class DiscordApiException extends RuntimeException {

    private final int[] expectedStatusCode;
    private final Request request;
    private final Response response;

    public DiscordApiException(final int[] expectedStatusCode, final Request request, final Response response) {
        super("Expected one of " + Arrays.toString(expectedStatusCode) + " but got " + response.code());
        this.expectedStatusCode = expectedStatusCode;
        this.request = request;
        this.response = response;
    }

    public int[] getExpectedStatusCode() {
        return this.expectedStatusCode;
    }

    public Request getRequest() {
        return this.request;
    }

    public Response getResponse() {
        return this.response;
    }

}
