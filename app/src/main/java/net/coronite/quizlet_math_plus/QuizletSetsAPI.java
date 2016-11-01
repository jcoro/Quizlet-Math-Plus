package net.coronite.quizlet_math_plus;


import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizletSetsAPI {
    @GET("CoroniteCreative?client_id=FtVwPj4gRf")
    Call<SetLists> loadSets();
}
