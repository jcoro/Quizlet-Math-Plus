package net.coronite.quizlet_math_plus.data;


import net.coronite.quizlet_math_plus.data.models.SetList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The interface used by Retrofit to retrieve flash card sets.
 */
public interface QuizletSetsAPI {
    @GET("{username}?client_id=FtVwPj4gRf")
    Call<SetList> loadSets(@Path("username") String username );
}
