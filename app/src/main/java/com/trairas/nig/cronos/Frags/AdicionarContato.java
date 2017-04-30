package com.trairas.nig.cronos.Frags;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trairas.nig.cronos.R;
import com.trairas.nig.cronos.Util.OperArquivos;
import com.trairas.nig.cronos.Util.util;


public class AdicionarContato extends Fragment {

    util u = new util();
    OperArquivos opr = new OperArquivos();

    static String ips = "1234567890.";
    static String letrasValidas = "aáãâbcçdeéẽêfghiíjklmnopqrstuúûũvxyzAÁÃÂBCÇDEÉẼÊFGHIÍJKLMNOPQRSTUÚÛŨVXYZ";

    public AdicionarContato() {
        // Required empty public constructor
    }

    EditText ed_1;
    EditText ed_2;
    Button bt_1;

    private boolean validarIP(String ip, Context c){

        boolean check = false;

        if(u.validarCaracters(ip, ips)){

            String resultado_2 = "";

            if(u.verificar_se_ja_tem(ip,c, "lista_ip.txt")){
                resultado_2 = "A palavra "+ip+" já contem!";
            }
            else{
                resultado_2 = "A palavra "+ip+" foi Salva!";
                u.print("Esta pronta para ser Salvo");
                check = true;
            }
            u.print(resultado_2);
        }
        return check;
    }

    private boolean validarNome(String nome, Context contento){

        boolean check = false;

        if(u.validarCaracters(nome, letrasValidas)){

            String resultado_2 = "";

            if(u.verificar_se_ja_tem(nome,contento, "lista_ip.txt")){
                resultado_2 = "A palavra "+nome+" já contem!";
            }
            else{
                resultado_2 = "A palavra "+nome+" foi Salva!";
                u.print("Esta pronta para ser Salvo");
                check = true;
            }
            u.print(resultado_2);
        }
        return check;
    }

    private boolean verificaExiste(String nome, String ip){

        final String[] palavras = opr.Todas_palavras(opr.ler(getContext(), "dados.txt"));

        String[] _nome = u.getNome(palavras);
        String[] _ip = u.getIp(palavras);
        boolean _n = false;
        boolean _i = false;

        for (int i=0;i<_nome.length;i++){
            if(_nome[i].equals(nome)){
                _n = true;
            }
        }

        for (int i=0;i<_ip.length;i++){
            if(_ip[i].equals(ip)){
                _i = true;
            }
        }

        if (_n && _i){
            Toast.makeText(getContext(), "Host cadastrado", Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(_i){
            Toast.makeText(getContext(), "Host cadastrado", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void validaCampos(String nome, String ip){

        if(nome.length() > 0 && ip.length() > 0){
            u.print("Campos nao vazios");

            if (validarIP(ip, getContext())){
                u.print("Ip valido");

                if (validarNome(nome, getContext())){
                    u.print("nome valido");

                    if (verificaExiste(nome, ip)){
                        u.print("dados validos - > salvando...");
                        opr.salvar(nome+":"+ip+"\n",getContext(), "dados.txt");
                        ed_1.setText("");
                        ed_2.setText("");
                        u.print("Dados Salvos com Sucesso!");
                        Toast.makeText(getContext(), "Dados Salvos com Sucesso!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_adicionar_contato, container, false);

         ed_1 = (EditText) view.findViewById(R.id.ac_ed_1);
         ed_2 = (EditText) view.findViewById(R.id.ac_ed_2);
         bt_1 = (Button) view.findViewById(R.id.ac_bt_1);

        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaCampos(ed_1.getText().toString().trim(), ed_2.getText().toString().trim());
            }
        });



        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
