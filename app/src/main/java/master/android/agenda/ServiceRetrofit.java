package master.android.agenda;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface ServiceRetrofit {

        @GET("contacts/")
        Call<ArrayList<Contacto>> getContacts();

        @POST("contacts/")
        Call<Contacto> postContact(@Body Contacto doc);

        @DELETE("contacts/{id}/")
        Call<Response<Void>> deleteContact(@Path("id") long contactId);

        public static final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.103:8000/api/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


