package com.example.myapplication;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class handles API calls to big-product website
 * Has methods to fetch item information from a given barcode
 */
public class ItemInfoFetcher {

    private final NetworkHandler networkHandler;
    private final Context context;

    /**
     * Constructor for this class
     * @param networkHandler Object that handles API calls to big-product website
     * @param context The Fragment context to which data will need to be sent back
     */
    public ItemInfoFetcher(NetworkHandler networkHandler, Context context) {
        this.networkHandler = networkHandler;
        this.context = context;
    }

    /**
     * Has callback functions
     * Enables generated item descriptions to be sent back to AddItemFragment
     */
    public interface ProductInfoCallback {
        void onSuccess(String description);
        void onError(String error);
    }

    /**
     * Function fetches the product information from its barcode.
     * In doing so, the method makes an API call to a rapid GTIN API:
     *      // https://rapidapi.com/bigproductdata/api/big-product-data/
     * @param barcode barcode number of the product
     */
    public void fetchProductInfo(String barcode, ProductInfoCallback callback) {
        new Thread(() -> {
            try {
                String responseString = networkHandler.performRequest("https://big-product-data.p.rapidapi.com/gtin/" + barcode);
                String description = getDescriptionFromResponse(responseString);
                runOnUIThread(() -> callback.onSuccess(description));
            } catch (Exception e) {
                runOnUIThread(() -> callback.onError(e.getMessage()));
            }
        }).start();
    }

    /**
     * This function runs a piece of code on a thread in the mainUI
     * Enables result of UI thread process to be sent to mainUI thread
     * @param runnable Anstraction of the code to run on the main UI thread
     */
    private void runOnUIThread(Runnable runnable) {
        new Handler(context.getMainLooper()).post(runnable);
    }

    /**
     * Method extracts description of item from the GTIN API response
     * @param responseData The string built from the rapid GTIN API response
     * @return (String) description of product extracted from the GTIN API response
     * @throws JSONException
     */
    public String getDescriptionFromResponse(String responseData) throws JSONException {

        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(responseData);
        String description = jsonObject.getJSONObject("properties")
                .getJSONArray("description")
                .getString(0);
        Log.d("API response", description);
        return description;
    }
}