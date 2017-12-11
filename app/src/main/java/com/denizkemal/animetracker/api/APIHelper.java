package com.denizkemal.animetracker.api;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.denizkemal.animetracker.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHelper {
    //It's not best practice to use internals, but there is no other good way to get the OkHttp default UA
    private static final String okUa = okhttp3.internal.Version.userAgent();
    private static final String USER_AGENT = "Atarashii! (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/" + Build.DISPLAY + ") " + okUa;

    /**
     * Init Retrofit client properly.
     *
     * @param ApiUrl The base API URL
     * @param Interface The interface class with the request methods
     * @param permission The Auth permission
     * @return <T> T The created service
     */
    public static <T> T createClient(String ApiUrl, Class<T> Interface, String permission) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.interceptors().add(new APIInterceptor(USER_AGENT, permission));

        Retrofit retrofit = new Retrofit.Builder()
                .client(client.build())
                .baseUrl(ApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Interface);
    }

    /**
     * Checks if the API is returning a successful response.
     * <p/>
     * With successful we mean 200-299.
     *
     * @param responseBody The responseBody.
     * @param methodName   Method name which made the request
     * @return boolean True is it was a good response
     */
    public static boolean isOK(Call<ResponseBody> responseBody, String methodName) {
        retrofit2.Response response = null;
        try {
            response = responseBody.execute();
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This will log the exception for Crashlytics.
     *
     * @param activity   Activity where the SnackBar should be shown
     * @param response   Retrofit response
     * @param className  The class name of the request executor
     * @param methodName method name
     * @param e          The exception which was caught
     */
    public static void logE(Activity activity, Response response, String className, String methodName, Exception e) {
       /* if (response != null && activity != null) {
            AppLog.log(Log.ERROR, "Atarashii", className + "." + methodName + "(): " + response.message());
            switch (response.code()) {
                case 400: // Bad Request
                    Theme.Snackbar(activity, R.string.toast_error_api);
                    break;
                case 401: // Unauthorized
                    AppLog.log(Log.ERROR, "Atarashii", className + ".doInBackground(): User is not logged in");
                    Theme.Snackbar(activity, R.string.toast_info_password);
                    break;
                case 404: // Not Found
                    if (methodName.contains("search"))
                        Theme.Snackbar(activity, R.string.toast_error_nothingFound);
                    else
                        Theme.Snackbar(activity, R.string.toast_error_Records);
                    AppLog.log(Log.ERROR, "Atarashii", className + ".doInBackground(): Error while getting records");
                    break;
                case 500: // Internal Server Error
                    AppLog.log(Log.ERROR, "Atarashii", className + ".doInBackground(): Internal server error, API bug?");
                    Theme.Snackbar(activity, R.string.toast_error_api);
                    break;
                case 503: // Service Unavailable
                case 504: // Gateway Timeout
                    AppLog.log(Log.ERROR, "Atarashii", className + ".doInBackground(): Gateway Timeout");
                    Theme.Snackbar(activity, R.string.toast_error_maintenance);
                    break;
                default:
                    Theme.Snackbar(activity, R.string.toast_error_Records);
                    AppLog.log(Log.ERROR, "Atarashii", className + ".doInBackground(): Unknown API error: " + response.code() + ": " + response.message());
                    break;
            }
        } else {
            AppLog.log(Log.ERROR, "Atarashii", className + "." + methodName + "(): " + e.getMessage());
        }
        AppLog.logException(e);*/
        e.printStackTrace();
    }

    /**
     * Check if the device is connected with the internet.
     *
     * @param context The context.
     * @return boolean True if the app can receive internet requests
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
