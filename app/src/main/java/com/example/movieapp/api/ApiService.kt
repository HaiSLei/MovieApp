
import com.example.movieapp.models.Movies
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiService {


    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") api : String = "7bfe007798875393b05c5aa1ba26323e") : Response<Movies>

    companion object {
        val sharedInstance: ApiService by lazy {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor {
                    val builder = it.request().newBuilder()
                    it.proceed(builder.build())
                }.build()

             Retrofit.Builder()
                 .baseUrl("https://api.themoviedb.org/3/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .client(client)
                 .build()
                 .create(ApiService::class.java)
        }
    }
}


