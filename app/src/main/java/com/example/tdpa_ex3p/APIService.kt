

import com.example.tdpa_ex3p.RandomUserResponse
import retrofit2.Response
import retrofit2.http.GET

interface APIService {
    @GET("api/")
    suspend fun getImagenes(): Response<RandomUserResponse>

}