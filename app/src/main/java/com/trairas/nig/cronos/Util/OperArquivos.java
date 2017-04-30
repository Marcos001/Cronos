package com.trairas.nig.cronos.Util;

import android.content.Context;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nig on 03/04/17.
 */

public class OperArquivos {

    private void print(String m){
        Log.v("Operar Arquivos", m);
    }

    public OperArquivos(){

    }

    public String[] Todas_palavras(String palavras_arquivo){


        int quantidade=0;

        for(int i=0;i < palavras_arquivo.length();i++){
            if(palavras_arquivo.charAt(i) == '\n'){
                quantidade +=1;
            }
        }

        String[] palavras = new String[quantidade];

        String tmp = "";
        int indice = 0;

        for(int i=0;i < palavras_arquivo.length();i++){

            if(palavras_arquivo.charAt(i) == '\n'){
                palavras[indice] = tmp;
                indice+=1;
                tmp = "";
            }
            else{
                tmp += palavras_arquivo.charAt(i);
            }
        }
        return palavras;
    }


    public String ler(Context contexto, String FILE){

        try{
            File arquivo_lido = contexto.getFileStreamPath(FILE);

            if (arquivo_lido.exists()){
                print("Arquivo existe");
                FileInputStream arquivo_ler = contexto.openFileInput(FILE);
                int tamanho =  arquivo_ler.available();
                byte dadosBytesLidos[] = new byte[tamanho];
                arquivo_ler.read(dadosBytesLidos);
                String dadosLidos = new String(dadosBytesLidos);


            return dadosLidos;
            }

            else{
                print("Arquivo nao existe.");
            }

        }catch (Exception erro){
            print("Erro ao Ler.");
        }
    return "Palavra\n";
    }


    public void salvar(String palavra, Context ctx, String FILE){

        if (ctx == null){
            print("Contexto nulo.");
            return;
        }

        print("word = "+palavra);

        try{
            
            FileOutputStream arquivo = ctx.openFileOutput(FILE, MODE_APPEND);
            arquivo.write(palavra.getBytes());
            arquivo.close();
            print("Arquivo Gravado");
        }

        catch (FileNotFoundException erro){
            print("Arquivo nao encontrado");
        }

        catch (Exception e) {
            print("Erro ao Gravar");
        }

    }

    public void salvar_tmp(String palavra, Context ctx, String FILE){

        if (ctx == null){
            print("Contexto nulo.");
            return;
        }

        print("word = "+palavra);

        try{

            FileOutputStream arquivo = ctx.openFileOutput(FILE, MODE_PRIVATE);
            arquivo.write(palavra.getBytes());
            arquivo.close();
            print("Arquivo Gravado");
        }

        catch (FileNotFoundException erro){
            print("Arquivo nao encontrado");
        }

        catch (Exception e) {
            print("Erro ao Gravar");
        }

    }

    /**-------------------------------------------------------------*/






}

