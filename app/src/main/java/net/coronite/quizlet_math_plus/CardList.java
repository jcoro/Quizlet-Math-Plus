package net.coronite.quizlet_math_plus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 10/30/2016.
 */
public class CardList {
    int id;
    List<Card> terms;
    // public constructor is necessary for collections
    public CardList() {
        terms = new ArrayList<Card>();
    }
    public static CardList parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        CardList cardList = gson.fromJson(response, CardList.class);
        return cardList;
    }
}
