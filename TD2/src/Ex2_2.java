
public class Ex2_2 {
   
	//
	/*public static boolean[] charversTableau(char c){
		boolean bits[] = new boolean[16];
		int v= (int)c;
		
	}*/
	static void printTableauBool(boolean [] bits){
		for(int i=0;i<bits.length;i++){
			System.out.print(bits[i]? '1':'0');
		}
		System.out.println();
	}
	
	static boolean[] charVersTableau(char c){
 	   int a= (int)c;            //convertir la car 
 	   boolean [] t=new boolean [16];
 	   for(int i=15;i>=0;i--){            //construire la tableau
 		   if((a&1)==0) t[i]=false;
 		   else t[i]=true;
 		   a=a/2;
 	   }
 	   
 	   return t;
    }
    
    
   static char tableauVersChar(boolean[] tab){
 	   int a=0;
 	   char b;
 	   if(tab.length!=16){
 		   System.out.println("ce tableau doit contenir 16 booleans");
 		   return 0;
 	   }
 	   else 
 	   {   
 		   int c=1;
 		   for(int i=15;i>0;i--){
 			   
 			   if(tab[i]==true) a=a+c;
 			   c*=2;
 	   }
 		   b=(char)a;
 	   }
 	   return b;
    }
	
	//Calcul de la valeur de retroaction d'un LFSR 
	static boolean lfsrRetroaction(boolean[] registre, boolean[] retroact){
		if(registre.length!=retroact.length){
			System.out.println("Les deux tableaux n'ont pas la même longueur");
			return false;
		}
		else{

		boolean retroaction=false;
		for (int i=0;i<registre.length;i++){
			retroaction^=registre[i]&retroact[i];
		}
		return retroaction;
		}
	}
	
	//Calcul de l'etat suivant et de la valeur de sortie d'un LFSR
	static boolean miseAJourLfsr(boolean[] registre, boolean[] retroact){
		boolean retroaction1=lfsrRetroaction(registre,retroact);
		boolean sortie=registre[registre.length-1];
	//	System.out.println("Sauter:"+ registre[registre.length-1]);
		for(int i=registre.length-1;i>0;i--){
			registre[i]=registre[i-1];
		}
		registre[0]=retroaction1;		
		
		
		return sortie;
		
	}
}
