import tc.TC;
public class Maintest {
	 static boolean[][] reg = new boolean[3][]; 
	 static boolean[][] retro= new boolean[3][];
	public static void main(String[] args){
		//Arguments
		/*for (int i=0;i<args.length;i++){
			TC.println("Arg#"+i+"=\"" + args[i]+"\"");
		}
		*/
	 //initialiser les registres
		int n=10;
		final int N=1000;
	 initGenerateur();
	 
	  
	 
	/* boolean []registre={true, false, false,false};
	 boolean []retroact={false,false,true,true};
	 TC.print("La registre originale est:");
	 Ex2_2.printTableauBool(registre);
	 boolean retroaction=Ex2_2.lfsrRetroaction(registre, retroact);
	 TC.println("La premierer retroaction est:" +retroaction);
	 for(int i=5;i>0;i--){
		 boolean sortie=Ex2_2.miseAJourLfsr(registre, retroact);
	 System.out.println("Sauter:"+ sortie);
	// boolean retroaction_suivant=Ex2_2.miseAJourLfsr(registre, retroact);
	 System.out.print("L'etat suivant est:");
	 Ex2_2.printTableauBool(registre);
	// TC.println("La retroaction suivante est:" +retroaction_suivant);
	 }*/
	 
	boolean [] bits= genererSuite(n);
	System.out.println("Les "+n+" premiers bits produits par le generateur sont :");
	Ex2_2.printTableauBool(bits);
	
	
	//chiffrement 
	testChiffrerDechiffrer("test.txt");
	
	//Corrélation entre les sorties des LFSR et les sorties du générateur
	for(int i=0;i<3;i++){
		double pro=calculProba(i,N);
		TC.println("La probabilité que la sortie du générateur pseudo-aléatoire soit "
				+ "la même que celle du"+ i +"-ième LFSR est "+ pro);
	}
	
	boolean [] coe={true,true,true,false,true};
	boolean [] mono=monome(6);
	boolean [] poly=polyDeRetroaction(coe);
	printPolynome(mono);
	printPolynome(poly);
	boolean []reste =reste(poly,mono);
	printPolynome(reste);
	boolean egale=egalite(poly,reste);
	TC.println(egale);
	
	
	 return;
		
	}
	
	//Initialisation des registres
	static void initGenerateur(){
		
	boolean[][]	etat= {{true,false,true,false},{false,true,true,false,true},{false,true,false,false,false,true}};
	boolean[][]	coeffiecient={{false,false,true,true},{false,false,true,false,true},{false,false,false,false,true,true}};
	reg=etat;
	retro=coeffiecient;
	}
	
	// ecrire la fonction de combinaison de bits
	static boolean combiner(boolean x0, boolean x1, boolean x2){
		boolean resultat;
		resultat=x0^x1;
		resultat^=(x0&x1);
		resultat^=(x1&x2);
		resultat^=(x0&x2);
		return resultat;
	}
	
	//Calcul de l'état suivant et sortie du générateur
	static boolean miseAJourGenerateur(){
	   boolean sortie0=Ex2_2.miseAJourLfsr(reg[0], retro[0]);
	   boolean sortie1=Ex2_2.miseAJourLfsr(reg[1], retro[1]);
	   boolean sortie2=Ex2_2.miseAJourLfsr(reg[2], retro[2]);
	   
	   boolean valeur=combiner(sortie0,sortie1,sortie2);
	   return valeur;
	
	}
	
	static boolean[] genererSuite(int n){
		boolean []bits = new boolean[n];
		for(int i=n-1;i>=0;i--){
			bits[i]=miseAJourGenerateur();			
		}
		return bits;
		
	}
	
	static boolean[] genererLfsr(int i,int n){
		
		boolean []bits = new boolean[n];
		for(int j= n-1; j>=0;j--){
			bits[j]=Ex2_2.miseAJourLfsr(reg[i], retro[i]);
		}
		return bits;
		
	}
	//Chiffrement d'un caractère en un autre caractère
	static char chiffrerChar(char c){
		boolean []origi=Ex2_2.charVersTableau(c);
		boolean []bits = genererSuite(origi.length);
		for(int i=0;i<origi.length;i++){
			origi[i]^=bits[i];
	    }
		char carac = Ex2_2.tableauVersChar(origi);
		return carac;
	}
	
	//Chiffrement d'un tableau de caractères
	static char[] chiffrerTableauChar(char[] s){
		char [] chiffe = new char [s.length];
		for(int i=0;i<s.length;i++){
			chiffe[i]=chiffrerChar(s[i]);
		}
		return chiffe;
	}
	
	//Test du chiffrement
	
	static void testChiffrerDechiffrer( String fichier ){
		TC.lectureDansFichier(fichier);
		char[] s = TC.lireLigne().toCharArray();
		TC.println("Les caractères originales sont :");
		TC.println(s);
		initGenerateur();
		char [] chiffe = chiffrerTableauChar(s);
		TC.println("Les caractères chiffrées sont :");
		TC.println(chiffe);
		initGenerateur();
		char [] dechiffe = chiffrerTableauChar(chiffe);
		TC.println("Les caractères déchiffrées sont :");
		TC.println(dechiffe);
	}
	
	//Probabilité d'égalité entre la sortie d'un LFSR et la sortie du générateur
	
	static double calculProba(int i, int N){
		boolean [] lfsr = new boolean [N];
		boolean [] suite = new boolean [N];
		double prob = 0.0;
		initGenerateur();
		suite = genererSuite(N);
		initGenerateur();
		lfsr = genererLfsr(i,N);
		
		for(int j=0;j<N;j++){
			if(lfsr[j]==suite[j]) prob+=1;			
		}
		
		prob/=(double)N;
		return prob;
		
		
	}
	
	
	//Calcul des trinômes multiples des polynômes de rétroaction
	
	//Quelques fonctions simples
	
	static boolean[] monome(int i){
		boolean [] repre= new boolean [i+1];
		for(int j=0;j<i;j++){
			repre[j]=false;
		}
		repre[i]=true;
		return repre;
	}
	
	static boolean[] polyDeRetroaction(boolean[] retroact){
		boolean [] poly = new boolean [retroact.length+1];
		for (int i=1;i<poly.length;i++){
			poly[i]=retroact[i-1];
		}
		poly[0]=false;
		return poly;
	}
	
	
	static void printPolynome(boolean []poly){
		boolean nul=true;
		for(int i=0;i<poly.length;i++){
			if(poly[i]==true){
				if(nul==true){
					if(i==0) TC.print("1");
					if(i==1) TC.print("X");
					else TC.print("X^"+i);
					nul=false;
				}
				else{
					if(i==1) TC.print("+X");
					else
					  TC.print("+X^"+i);
				}						
			}
		}
		TC.println();
	}
	
	//Addition de deux polynômes
	static boolean[] ajoute(boolean[] p0, boolean[] p1){
		int lenth= (p0.length>=p1.length)? p0.length:p1.length;
		boolean []resultat= new boolean [lenth];
		for(int i=0;i<lenth;i++){
			//resultat[i]=false;
			boolean a=(i<p0.length)? p0[i]:false;
			boolean b=(i<p1.length)? p1[i]:false;
			resultat[i]=a^b;
		}
		return resultat;
	}
	
	//Multiplication d'un polynôme par un monôme
	static boolean[] multiplieParMonome(boolean[] p, int i){
		boolean []resultat = new boolean[p.length+i];
		for(int j=0;j<resultat.length;j++){
			resultat[j]=false;
		}
		for(int j=0;j<p.length;j++){
			resultat[j+i]=p[j];			
		}
		return resultat;
	}
	
	//Calcul du degré d'un polynôme
	static int deg(boolean[] p){
		int dege=-1;
	 for (int i=p.length-1;i>=0;i--){
		 if(p[i]==true) {
			 dege=i;
			 break;
		 }
	 }
	 return dege;	 
	}

	//Reste dans la division euclidienne de polynômes
	static boolean[] reste(boolean[] p0, boolean[] p1){
		int deg=deg(p0)-deg(p1);
		if (deg<0) 
			return p0;
		else{
			boolean [] p2=multiplieParMonome(p1,deg);
			boolean [] p3=ajoute(p0,p2);
			return reste(p3,p1);
		}
	}
	
	static boolean egalite(boolean[] p0, boolean[] p1){
		boolean []reste= reste(p0,p1);
		if((deg(p0)==deg(p1))&&(deg(reste)==-1)) return true;
		else return false;
	}
}
