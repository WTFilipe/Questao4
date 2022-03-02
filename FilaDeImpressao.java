import java.util.ArrayList;

class Fila {
  private ArrayList<String> impressoes;
  private int contador;
  
  public Fila() { 
     impressoes = new ArrayList<String>();
     contador = 0;
  }

  public synchronized void adicionaImpressao(String impressao) { 
     if(this.contador == 10) { //Irá aguardar caso a fila já esteja cheia
     	System.out.println("Sou a thread que cria impressão e vou aguardar para pedir a impressão do número " + impressao + " pois já tem 10 impressões na fila");
     	try { wait(); } catch (Exception e) { return;} 
     }
     this.impressoes.add(impressao);
     this.contador++;
  }

  public synchronized String proximaImpressao() { 
      while(contador == 0) {} //Só realiza impressão caso tenha impressões na fila
      String proximaImpressao = this.impressoes.remove(0); 
      this.contador--;
      notify();
      return proximaImpressao;
  }
  
}

class CriaImpressao extends Thread {
   //Simula uma entrada contínua de informações para impressão. Tenta adicionar um novo elemento a cada 1 segundo.
   private int id;
   private Fila fila;
  
   public CriaImpressao(int tid, Fila fila) { 
      this.id = tid; 
      this.fila = fila;
   }

   public void run(){
   	int contadorAuxiliar = 0;
   	while(true){
   		try {
   		   fila.adicionaImpressao(String.valueOf(contadorAuxiliar));
   		   contadorAuxiliar++;
   		   Thread.sleep(1000);
   		}
   		catch(Exception e) { }
   	}
   }
}

class RealizaImpressao extends Thread {
   //Imprime o elemento mais antigo presente na fila de impressão a cada 1,5 segundos. 
   private int id;
   private Fila fila;
  
   public RealizaImpressao(int tid, Fila fila) { 
      this.id = tid; 
      this.fila = fila;
   }

   public void run() {
   	int contadorAuxiliar = 0;
   	while(true){
   		try {
   		   System.out.println(fila.proximaImpressao());
   		   Thread.sleep(1500); 
   		}
   		catch(Exception e) { }
   	}
   }
}

//classe da aplicacao
class FilaDeImpressao {

   public static void main (String[] args) {
      // Cria a fila de impressão
      Fila fila = new Fila();
      
      //Cria a thread de criar impressão e de realizar impressão
      Thread criaImpressao = new CriaImpressao(0, fila);
      criaImpressao.start();
      Thread realizaImpressao = new RealizaImpressao(1, fila);
      realizaImpressao.start();
   }
}
