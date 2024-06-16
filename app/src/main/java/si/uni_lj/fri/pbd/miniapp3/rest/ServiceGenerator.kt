package si.uni_lj.fri.pbd.miniapp3.rest

//import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import si.uni_lj.fri.pbd.miniapp3.Constants

object ServiceGenerator {
    private var BASE_URL = Constants.BASE_URL

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    private val httpClientBuilder = OkHttpClient.Builder()

//    private val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }

    init {
//        httpClientBuilder.addInterceptor(loggingInterceptor)

    }

    fun <T> createService(serviceClass: Class<T>): T {
        val retrofit = retrofitBuilder.client(httpClientBuilder.build()).build()
        return retrofit.create(serviceClass)
    }
}
