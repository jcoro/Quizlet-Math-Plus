package net.coronite.quizlet_math_plus.data;


import net.coronite.quizlet_math_plus.data.models.SetList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizletSetsAPI {
    @GET("CoroniteCreative?client_id=FtVwPj4gRf")
    Call<SetList> loadSets();
}
