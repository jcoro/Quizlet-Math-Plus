package net.coronite.quizlet_math_plus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by John on 10/30/2016.
 */
public interface  QuizletService {
    String ENDPOINT = "https://api.quizlet.com/2.0/sets/";

    @GET("163432765?client_id=FtVwPj4gRf&whitespace=1")
    Call<CardList> getFeed();
}
