package com.trairas.nig.cronos.Frags;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.trairas.nig.cronos.Act.chat;
import com.trairas.nig.cronos.R;
import com.trairas.nig.cronos.Util.OperArquivos;
import com.trairas.nig.cronos.Util.util;

import java.util.ArrayList;


public class lista_contatos extends Fragment {

    ListView lista;
    util u = new util();
    OperArquivos opr = new OperArquivos();

    private ArrayList<String> preencherDados(String[] dados_vetor) {

        ArrayList<String> dados = new ArrayList<>();

        if (dados_vetor.length == 0) {
            dados.add("Adicione um Contato");
        } else {
            for (int i = 0; i < dados_vetor.length; i++) {
                dados.add(dados_vetor[i]);
            }
        }
        return dados;
    }


    private void adicionarElementosNaLista(){

        final String[] palavras = opr.Todas_palavras(opr.ler(getContext(), "dados.txt"));
        final ArrayList<String> pal = preencherDados(palavras);
        ArrayAdapter array_adapter =  new ArrayAdapter <> (getContext(), android.R.layout.simple_list_item_1, pal);
        lista.setAdapter(array_adapter);
    }


    private void config_list_view(View view){
        lista = (ListView) view.findViewById(R.id.lc_lv_1);

        final String[] palavras = opr.Todas_palavras(opr.ler(getContext(), "dados.txt"));

        //confirurando nome e ip veindos do arquivo

        final String[] nome = u.getNome(palavras);
        final String[] ips = u.getIp(palavras);

        for(int i=0;i < nome.length;i++){
            u.print("nome "+i+" = "+nome[i]);
        }

        for(int i=0;i < ips.length;i++){
            u.print("ips "+i+" = "+ips[i]);
        }

        adicionarElementosNaLista();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_contatos, container, false);

        config_list_view(view);

        final String[] palavras = opr.Todas_palavras(opr.ler(getContext(), "dados.txt"));

        //confirurando nome e ip veindos do arquivo

        final String[] nome = u.getNome(palavras);
        final String[] ips = u.getIp(palavras);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                u.print("Selecionado na posicao "+position);

                u.print("nome = "+nome[position]);
                u.print("ip = "+ips[position]);

                //escrever no arquvo nome e ip

                opr.salvar_tmp(nome[position]+":"+ips[position]+".", getContext(),"ses.txt");

                Intent it = new Intent(getActivity(), chat.class);
                startActivity(it);

            }
        });

        return view;
    }

}
