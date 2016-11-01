package net.coronite.quizlet_math_plus;

import retrofit2.Call;
import retrofit2.http.GET;


public interface  QuizletService {
    String ENDPOINT = "https://api.quizlet.com/2.0/sets/";

    @GET("163432765?client_id=FtVwPj4gRf")
    Call<TermLists> getFeed();
}
