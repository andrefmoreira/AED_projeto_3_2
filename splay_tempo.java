import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;



public class splay_tempo
{
    public static class Global
    {   
        public static int oferta_atualizada = 0;
        public static int encontrado = 0;
        public static int inserido = 0;
    }   

  static class Artigo implements Comparable<Artigo>
  {

    String nome , hash , oferta;

    public Artigo(String nome , String hash , String oferta)
    {
        this.nome = nome;
        this.hash = hash;
        this.oferta = oferta;
    }

    @Override public int compareTo(Artigo artigo)
    {
        String compara_nome = ((Artigo)artigo).nome;

        return 
        this.nome.compareTo(compara_nome);
    }

    @Override
    public String toString()
    {
        return this.nome + " " + this.hash + " " + this.oferta + "\n";
    }

}


static class No {
    Artigo artigo;
    No esquerda, direita;
 
    No(Artigo artigo1) 
    {
        artigo = artigo1;

    }

    No printInOrder(ArrayList<Artigo> elementos , No raiz)
    {
    if(raiz.esquerda != null )
        raiz.esquerda.printInOrder(elementos , raiz.esquerda); // Left

    elementos.add(raiz.artigo); // Node

    if( raiz.direita != null )
        raiz.direita.printInOrder(elementos , raiz.direita); // Right

    return raiz;
    }
 
} 
 
static class Arvore 
{
    No raiz;

    No rotacao_direita(No y) 
    {
        No x = y.esquerda;
        No filho = x.direita;
 
        //x fica no sitio de y e y fica com o filho de x.
        x.direita = y;
        y.esquerda = filho;
        
        return x;
    }

 

    No rotacao_esquerda(No x) 
    {
        No y = x.direita;
        No filho = y.esquerda;
 
        //y fica no sitio de x e x fica com o filho de y. 
        y.esquerda = x;
        x.direita = filho;

        return y;
    }

    No consulta(No no, String artigo ) //Por raiz aqui para depois dar splay
    {
        //Chegamos ao fim e nao foi encontrado
        if (no == null)
        {
            //System.out.println("ARTIGO NAO REGISTADO");
            return no;
        }
        //Novo no e mais pequeno que o atual ir para a esquerda
        if (no.artigo.nome.compareTo(artigo) > 0)
            no.esquerda = consulta(no.esquerda, artigo);
        

        //Novo no e maior que o atual, ir para a direita
        else if (no.artigo.nome.compareTo(artigo) < 0)
            no.direita = consulta(no.direita, artigo);


        //Encontramos um no igual    
        else
        {   
            //System.out.print(no.artigo.toString()); 
            Global.encontrado++;
            return no;
        }
        return no;
    }




    No oferta(No no, String nome_procura , String oferta) 
    {
        //Chegamos ao fim e nao foi encontrado
        if (no == null)
        {
            System.out.println("ARTIGO NAO REGISTADO");
            return no;
        }
        //Novo no e mais pequeno que o atual ir para a esquerda
        if (no.artigo.nome.compareTo(nome_procura) > 0)
            no.esquerda = oferta(no.esquerda, nome_procura , oferta);
        

        //Novo no e maior que o atual, ir para a direita
        else if (no.artigo.nome.compareTo(nome_procura) < 0)
            no.direita = oferta(no.direita, nome_procura , oferta);


        //Encontramos um no igual    
        else
        {   
            no.artigo.oferta = oferta;
            System.out.println("OFERTA ATUALIZADA");
            Global.oferta_atualizada++;
            return no;
        }
        return no;
    }


    No artigo(No no, Artigo artigo) 
    {
        //Nao encontramos ou e logo a raiz
        if (no == null)
        {
            //System.out.println("NOVO ARTIGO INSERIDO");
            Global.inserido++;
            return (new No(artigo));
        } 
        //Novo no e mais pequeno que o atual ir para a esquerda
        if (no.artigo.nome.compareTo(artigo.nome) > 0)
            no.esquerda = artigo(no.esquerda, artigo);

        //Novo no e maior que o atual, ir para a direita
        else if (no.artigo.nome.compareTo(artigo.nome) < 0)
            no.direita = artigo(no.direita, artigo);

        //Encontramos um no igual    
        else
        {
            //System.out.println("ARTIGO JA EXISTENTE");
            return no;
        }
 
 
        return no;
    }

    No splay(No no, String nome)
    {   
        //Raiz ainda nao existe ou o elemento e logo a raiz.
        if(no == null || no.artigo.nome.equals(nome))
            return no;
        
        //Elemento mais pequeno ir para a esquerda (ZIG)
        if(no.artigo.nome.compareTo(nome) > 0) 
        {
            if (no.esquerda == null) 
                return no;
            //Elemento mais pequeno ir para a esquerda (ZIGZIG)
            if(no.artigo.nome.compareTo(nome) > 0)
            {
                //Fazer recursao ate encontrar o elemento
                no.esquerda.esquerda = splay(no.esquerda.esquerda , nome);
                no = rotacao_direita(no);
            }
            //Elemento maior ir para a direita (ZIGZAG) 
            else if(no.artigo.nome.compareTo(nome) < 0)
            {
                no.esquerda.direita = splay(no.esquerda.direita , nome);
                
                if(no.esquerda.direita != null)
                   no.esquerda = rotacao_esquerda(no.esquerda);
            }  

            if (no.esquerda == null)
                return no;
            else
                return rotacao_direita(no);
        }
        //Elemento maior ir para a direita (ZIG)
        else
        {
            if (no.direita == null)
                return no;
             //Elemento maior ir para a direita (ZIGZIG) 
            if(no.artigo.nome.compareTo(nome) > 0)
            {
                no.direita.esquerda = splay(no.direita.esquerda , nome);

                if(no.direita.esquerda != null)
                   no.direita =  rotacao_direita(no.direita);
            }
             //Elemento mais pequeno ir para a esquerda (ZIGZAG)
            else if(no.artigo.nome.compareTo(nome) < 0)
            {   
                no.direita.direita = splay(no.direita.direita , nome);
                no = rotacao_esquerda(no);
            }

            if (no.direita == null)
                return no;
            else
                return rotacao_esquerda(no);
        }    
    }

}
 
    static String le_parametros(Scanner sc)
    {
        String str;  
         
        try{
                
        str = sc.nextLine();

        }
        //Se o valor for um valor causar um erro, ira ser avisado ao usuario que o valor nao e valido.
        catch (java.util.InputMismatchException e){
            System.out.printf("Valor Introduzido nao e valido.");
            return null;
        }
        
        return str;
    }


    static void opcoes(Arvore raiz , int elementos)
    {   
        long tempo = 0;

        for(int x = 0 ; x < elementos/10 ; x++)
        {
            Artigo art = new Artigo(String.valueOf(x) , String.valueOf(x) , String.valueOf(x));
            raiz.raiz = raiz.artigo(raiz.raiz , art);

            if(Global.inserido == 1)
            {
                Global.inserido = 0;
                //apos inserir um elemento realizamos splaying a esse elemento
                raiz.raiz = raiz.splay(raiz.raiz, art.nome);
            }
        }
        for(int x = 0 ; x < elementos/10 ; x++)
        {
        Random r = new Random();
        int randomNumber = r.nextInt(101);
    
        if(randomNumber <= 5)
        {
            Random r1 = new Random();
            int randomNumber1 = (int) (r1.nextInt((int) ((elementos/10) - (elementos/10 * 0.05))) + (elementos/10 * 0.05));
            long start = System.nanoTime();
            raiz.consulta(raiz.raiz , String.valueOf(randomNumber1));

            if(Global.encontrado == 1)
            {   

                Global.encontrado = 0;
                //apos consultar um elemento realizamos splaying a esse elemento
                raiz.raiz = raiz.splay(raiz.raiz , String.valueOf(randomNumber1));
            }
            long end = System.nanoTime();
            tempo += end-start;
        }
        else
        {
            Random r1 = new Random();
            int randomNumber1 = r1.nextInt((int) (elementos/10 * 0.05));
            long start = System.nanoTime();
            raiz.consulta(raiz.raiz, String.valueOf(randomNumber1));

            if(Global.encontrado == 1)
            {   
                Global.encontrado = 0;
                //apos consultar um elemento realizamos splaying a esse elemento
                raiz.raiz = raiz.splay(raiz.raiz , String.valueOf(randomNumber1));
            }
            long end = System.nanoTime();
            tempo += end-start;
        }
    }
        System.out.println(tempo);
}


    public static void main(String[] args) 
    {   
        Arvore tree = new Arvore();
        opcoes(tree , 120000);

    }
}
