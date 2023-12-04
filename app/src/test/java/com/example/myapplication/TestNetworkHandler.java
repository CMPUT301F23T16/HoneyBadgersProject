package com.example.myapplication;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class TestNetworkHandler {

    private NetworkHandler networkHandler;

    @Mock
    private HttpURLConnection mockConnection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        networkHandler = new NetworkHandler();
    }

    @Test
    public void testSetupConnection_Success() throws IOException {
        URL url = new URL("https://example.com");
        HttpURLConnection connection = networkHandler.setupConnection(url);
        assertEquals(200, connection.getResponseCode());
    }


    @Test
    public void testReadResponse() throws IOException {
        String testData = "API Test Response";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Mockito.when(mockConnection.getInputStream()).thenReturn(inputStream);

        //Run the test method with mocked connection
        String response = networkHandler.readResponse(mockConnection);

        assertEquals(testData, response);
    }
}