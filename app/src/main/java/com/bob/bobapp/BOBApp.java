package com.bob.bobapp;

import android.content.Context;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.encryption.AESEncryption;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.SettingPreferences;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import androidx.multidex.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BOBApp{

    private static APIInterface api;

    private static Context context;

    private static String actionGlobal;

    public static APIInterface getApi(Context ctx, String action) {

        actionGlobal = action;

        context = ctx;

        api = createApi();

        return api;
    }

    private static APIInterface createApi() {

        OkHttpClient.Builder client = new OkHttpClient.Builder()

                .readTimeout(120, TimeUnit.SECONDS)

                .connectTimeout(120,TimeUnit.SECONDS);

        client.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {

                String uniqueIdentifier = SettingPreferences.getRequestUniqueIdentifier(context);

                String tpa = uniqueIdentifier + "|" + Constants.SOURCE;

                System.out.println("PLAIN TPA :" + tpa);

                tpa = AESEncryption.encryptUsingKey(tpa);

                Request.Builder builder = chain.request().newBuilder()

                        .addHeader("Content-Type", "application/json")

                        .addHeader("Accept", "application/json")

                        .addHeader("TPA", tpa);

                if(!actionGlobal.equals(Constants.ACTION_AUTHENTICATE)){

                    Request request = chain.request();

                    RequestBody oldBody = request.body();

                    Buffer buffer = new Buffer();

                    oldBody.writeTo(buffer);

                    String strOldBody = buffer.readUtf8();

                    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

                    String strNewBody = AESEncryption.encryptUsingKey(strOldBody);

                    RequestBody body = RequestBody.create(mediaType, strNewBody);

                    request = request.newBuilder()
                            .header("Content-Type", body.contentType().toString())
                            .header("Accept", "application/json")
                            .header("TPA", tpa)
                            .method(request.method(), body).build();

                    System.out.println("ENCRYPTED REQUEST :" +strNewBody);
                    System.out.println("PLAIN REQUEST :" +strOldBody);

                    return chain.proceed(request);

                }else{

                    return chain.proceed(builder.build());
                }
            }
        });

        if (BuildConfig.DEBUG) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            client.addInterceptor(interceptor);
        }

        return new Retrofit.Builder()

                .baseUrl(Constants.BASE_URL)

                .client(client.build())

                .addConverterFactory(GsonConverterFactory.create())

                .build()

                .create(APIInterface.class);
    }
}