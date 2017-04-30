package com.trairas.nig.cronos.Frags;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.trairas.nig.cronos.R;
import com.trairas.nig.cronos.MB.*;



public class BlaBlaBla extends Fragment {

    Button bt_1;
    EditText ed_1;

    public BlaBlaBla() {
        // Required empty public constructor
    }

    private void enviarMensagem(final String fuxico){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (fuxico.length() > 0){
                    new Produtor(fuxico);
                }

            }
        }).start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enviar_mensagem, container, false);

         bt_1 = (Button) view.findViewById(R.id.em_bt_1);
         ed_1 = (EditText) view.findViewById(R.id.em_ed_1);

        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem(ed_1.getText().toString());
            }
        });

        return view;
    }

        @Override
    public void onDetach() {
        super.onDetach();

    }


}
