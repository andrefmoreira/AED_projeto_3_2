import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class splay_tree
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
            System.out.println("ARTIGO NAO REGISTADO");
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
            System.out.print(no.artigo.toString()); 
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
            System.out.println("NOVO ARTIGO INSERIDO");
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
            System.out.println("ARTIGO JA EXISTENTE");
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


    static void opcoes(Scanner sc,Arvore raiz)
    {   
        int fim = 0;

        while(fim == 0)
        {

        String[] parametros;
        parametros = le_parametros(sc).split(" ");


        if(parametros[0].equals("ARTIGO"))
        {
            Artigo art = new Artigo(parametros[1] , parametros[2] , parametros[3]);
            raiz.raiz = raiz.artigo(raiz.raiz , art);

            if(Global.inserido == 1)
            {
                Global.inserido = 0;
                //apos inserir um elemento realizamos splaying a esse elemento
                raiz.raiz = raiz.splay(raiz.raiz, art.nome);
            }
        }

        if(parametros[0].equals("OFERTA"))
            raiz.oferta(raiz.raiz , parametros[1] , parametros[2]);

            if(Global.oferta_atualizada == 1)
            {   
                Global.oferta_atualizada = 0;
                raiz.raiz = raiz.splay(raiz.raiz , parametros[1]);
            }


        if(parametros[0].equals("CONSULTA"))
        {   
            raiz.consulta(raiz.raiz, parametros[1]);

            if(Global.encontrado == 1)
            {   
                System.out.println("FIM");
                Global.encontrado = 0;
                //apos consultar um elemento realizamos splaying a esse elemento
                raiz.raiz = raiz.splay(raiz.raiz , parametros[1]);
            }

        }

        if(parametros[0].equals("LISTAGEM"))
        {
           ArrayList<Artigo> elementos = new ArrayList<>();
           if(raiz.raiz == null)
                System.out.println("FIM");
           else
           {
                raiz.raiz.printInOrder(elementos , raiz.raiz);
                Collections.sort(elementos);

                for(int i = 0 ; i < elementos.size() ; i ++)
                {    
                    System.out.print(elementos.get(i).toString());
                }
                System.out.println("FIM");
            }
        }

        if(parametros[0].equals("APAGA"))
        {
            System.out.println("CATALOGO APAGADO");
            raiz.raiz = null;
            
        }

        if(parametros[0].equals("FIM"))
        {
            fim++;
            sc.close();
        }
    }
}


    public static void main(String[] args) 
    {   
        Arvore tree = new Arvore();
        Scanner sc = new Scanner(System.in);

        opcoes(sc , tree);
        sc.close();
    }
}
