package net.coronite.quizlet_math_plus.data;

import net.coronite.quizlet_math_plus.data.models.TermList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface QuizletTermsAPI {
    String ENDPOINT = "https://api.quizlet.com/2.0/sets/";

    @GET("{id}?client_id=FtVwPj4gRf")
    Call<TermList> getFeed(@Path("id") String id);
}
