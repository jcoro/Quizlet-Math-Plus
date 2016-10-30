package net.coronite.quizlet_math_plus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        final Button button = (Button)view.findViewById(R.id.answer_button);
        final View answer = view.findViewById(R.id.answer);
        answer.setVisibility(View.INVISIBLE);
        button.setText(R.string.show_answer);
        button.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v){
                if(button.getText().equals(getText(R.string.show_answer))){
                    button.setText(R.string.hide_answer);
                    answer.setVisibility(View.VISIBLE);
                } else {
                    button.setText(R.string.show_answer);
                    answer.setVisibility(View.INVISIBLE);
                }

            }
        });
        return view;
    }
}
