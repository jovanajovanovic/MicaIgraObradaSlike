package com.mica.main;

import java.util.ArrayList;
import java.util.Arrays;

public class Stanje {
	private Polje[][] polja;
	
	private TipPolja igracNaPotezu;
	private Igrac plaviIgrac;
	private Igrac crveniIgrac;
	
	private Polje selektovanoPolje = null;
	
	private boolean pojedi;
	
	private double score;
	
	private final double konstantaZaMojeTare = 15;
	private final double konstantaZaProtivnikoveTare = 18;
	private final double konstantaZaMojePoluTare = 7;
	private final double konstantaZaProtivnikovePoluTare = 10;
	
	private final double konstantaZaKrajIgre = 1000;
	
	
	public Stanje() {
		polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) { 
				polja[i][j] = new Polje(i, j, TipPolja.ZUTO);;
			}
			
		}
		
		this.plaviIgrac = new Igrac("Plavi", TipPolja.PLAVO, Controller.BROJ_FIGURA, Controller.BROJ_FIGURA);
		this.crveniIgrac = new Igrac("Crveni", TipPolja.CRVENO, Controller.BROJ_FIGURA, Controller.BROJ_FIGURA);
		this.pojedi = false;
		
		this.score = 0;
	}
	
	public Stanje(TipPolja igracNaPotezu) {
		polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) { 
				polja[i][j] = new Polje(i, j, TipPolja.ZUTO);;
			}
			
		}
		
		this.plaviIgrac = new Igrac("Plavi", TipPolja.PLAVO, Controller.BROJ_FIGURA, Controller.BROJ_FIGURA);
		this.crveniIgrac = new Igrac("Crveni", TipPolja.CRVENO, Controller.BROJ_FIGURA, Controller.BROJ_FIGURA);
		
		this.igracNaPotezu = igracNaPotezu;
		this.pojedi = false;
		
		this.score = 0;
	}
	
	public Stanje(Polje[][] polja, TipPolja igracNaPotezu, Igrac plaviIgrac, Igrac crveniIgrac, Polje selektovanoPolje) {
		this.polja = polja;
		this.igracNaPotezu = igracNaPotezu;
		this.plaviIgrac = plaviIgrac;
		this.crveniIgrac = crveniIgrac;
		this.selektovanoPolje = selektovanoPolje;
	}
	
	public Stanje(Stanje stanje) {
		this.igracNaPotezu = stanje.igracNaPotezu;
			
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) { 
				polja[i][j] = new Polje(stanje.polja[i][j]);
			}
			
		}
		
		this.polja = polja;
		
		this.plaviIgrac = new Igrac(stanje.plaviIgrac);
		this.crveniIgrac = new Igrac(stanje.crveniIgrac);
		
		this.pojedi = stanje.pojedi;
		
		this.score = stanje.score;
		
		Polje selektovanoPolje = stanje.getSelektovanoPolje();
		if(selektovanoPolje != null) {
			this.selektovanoPolje = this.polja[selektovanoPolje.getPozicija().getX()][selektovanoPolje.getPozicija().getY()];
		}
		else this.selektovanoPolje = null;
	}

	public Stanje(String stanjeStr) {
		String[] tokens = stanjeStr.split("\\|");
		String[] tokensNepostavljeneFigure = tokens[1].split(",");
		String[] tokensPravougonici = tokens[0].split(";");
		
		int brojPlavihNepostavljenihFigura = Integer.parseInt(tokensNepostavljeneFigure[0]);
		int brojCrvenihNepostavljenihFigura = Integer.parseInt(tokensNepostavljeneFigure[1]);
		
		if(brojPlavihNepostavljenihFigura >= brojCrvenihNepostavljenihFigura) this.igracNaPotezu = TipPolja.PLAVO;
		else this.igracNaPotezu = TipPolja.CRVENO;
		
		int brojPlavihPreostalihFigura = brojPlavihNepostavljenihFigura;
		int brojCrvenihPreostalihFigura = brojCrvenihNepostavljenihFigura;
		
		polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		
		String[] tokensPolja;
		int tipPoljaInt;
		TipPolja tipPolja;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			tokensPolja = tokensPravougonici[i].split(",");
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				tipPoljaInt = Integer.parseInt(tokensPolja[j]);
				tipPolja = TipPolja.values()[tipPoljaInt];
				
				if(tipPolja == TipPolja.PLAVO) brojPlavihPreostalihFigura++;
				else if(tipPolja == TipPolja.CRVENO) brojCrvenihPreostalihFigura++;
				
				polja[i][j] = new Polje(i, j, tipPolja);
			}
			
		}
		
		this.plaviIgrac = new Igrac("Plavi", TipPolja.PLAVO, brojPlavihNepostavljenihFigura, brojPlavihPreostalihFigura);
		this.crveniIgrac = new Igrac("Crveni", TipPolja.CRVENO, brojCrvenihNepostavljenihFigura, brojCrvenihPreostalihFigura);
		
		this.pojedi = false;
		
		this.score = 0;
	}

	public static Stanje kreirajStanje(String[] tokeniStanje) {
		String[] tokeniIgrac, tokeni;
		String tokenPolje;
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		Igrac plaviIgrac, crveniIgrac;
		TipPolja igracNaPotezu;
		
		try {
			tokeni = tokeniStanje[0].trim().split(";");
			if(tokeni.length != 24) return null;
			
			for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
				for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
					tokenPolje = tokeni[j + Controller.BROJ_POLJA_U_KRUGU*i].trim();
					
					polja[i][j] = new Polje(i, j, TipPolja.values()[Integer.parseInt(tokenPolje)]);
				}
			}
			
			tokeniIgrac = tokeniStanje[1].trim().split(";");
			//if(tokeniIgrac.length != 4) return null;
			if(tokeniIgrac.length != 3) return null;
			plaviIgrac = Igrac.kreirajIgraca(tokeniIgrac, polja);
			if(plaviIgrac == null) return null;
			
			tokeniIgrac = tokeniStanje[2].trim().split(";");
			//if(tokeniIgrac.length != 4) return null;
			if(tokeniIgrac.length != 3) return null;
			crveniIgrac = Igrac.kreirajIgraca(tokeniIgrac, polja);
			if(crveniIgrac == null) return null;
			
			igracNaPotezu = TipPolja.values()[Integer.parseInt(tokeniStanje[3].trim())];
			
			//String[] tokeniSelektovanoPolje = tokeniStanje[4].trim().split(",");
			//int x = Integer.parseInt(tokeniSelektovanoPolje[0].trim());
			//int y = Integer.parseInt(tokeniSelektovanoPolje[1].trim());
			//Polje selektovanoPolje;
			//if(x == -1 && y == -1) selektovanoPolje = null;
			//else selektovanoPolje = polja[x][y];
			Polje selektovanoPolje;
			int a = Integer.parseInt(tokeniStanje[4].trim());
			if(a == -1) selektovanoPolje = null;
			else {
				int x = a / Controller.BROJ_POLJA_U_KRUGU;
				int y = a % Controller.BROJ_POLJA_U_KRUGU;
				selektovanoPolje = polja[x][y];
			}
			
			return new Stanje(polja, igracNaPotezu, plaviIgrac, crveniIgrac, selektovanoPolje);
		}
		catch(Exception e) {
			return null;
		}
		
	}
	
	
	public boolean daLiSuSveFigurePostavljene() {
		return plaviIgrac.getBrojNepostavljenihFigura() == 0 && crveniIgrac.getBrojNepostavljenihFigura() == 0;
	}
	
	public void podesiPoljaUTaramaIVanNjih() {
		TipPolja tipPolja;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				tipPolja = polja[i][j].getTipPolja();
				
				if(polja[i][j+1].getTipPolja() == tipPolja && polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja && tipPolja != TipPolja.ZUTO) {
					polja[i][j].setDaLiJeUTari(true);
					polja[i][j+1].setDaLiJeUTari(true);
					polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].setDaLiJeUTari(true);
				}
				else {
					if(j == 0) polja[i][j].setDaLiJeUTari(false);
					polja[i][j+1].setDaLiJeUTari(false);
					if(j != 6) polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].setDaLiJeUTari(false);
				}
			}
		}
		
		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
			tipPolja = polja[0][k].getTipPolja();
			
			if(polja[1][k].getTipPolja() == tipPolja && polja[2][k].getTipPolja() == tipPolja && tipPolja != TipPolja.ZUTO) {
				polja[0][k].setDaLiJeUTari(true);
				polja[1][k].setDaLiJeUTari(true);
				polja[2][k].setDaLiJeUTari(true);
			}
			/*else {
				polja[0][k].setDaLiJeUTari(false);
				polja[1][k].setDaLiJeUTari(false);
				polja[2][k].setDaLiJeUTari(false);
			}*/
		}
		
	}

	public Rezultat krajIgre() {
		if (plaviIgrac.getBrojPreostalihFigura() == 2) {
			return new Rezultat("Crveni", crveniIgrac.getAlgoritam(), "Plavi", plaviIgrac.getAlgoritam());
		}
		else if(crveniIgrac.getBrojPreostalihFigura() == 2) {
			return new Rezultat("Plavi", plaviIgrac.getAlgoritam(), "Crveni", crveniIgrac.getAlgoritam());
		}
		
		
		Igrac igrac;
		// vec je promenjen potez, tj. zapocet novi
		if(igracNaPotezu == TipPolja.PLAVO) {
			igrac = plaviIgrac;
		}
		else {
			igrac = crveniIgrac;
		}
		
		// Ako su mu preostale 3 figure, sigurno se moze mrdati, jer moze da skace, pa zato necemo nastaviti proveru
		if(igrac.getBrojPreostalihFigura() == 3) {
			return null;
		}
		
		if(!daLiSuSveFigurePostavljene()) {
			return null;
		}
		
		// moze li se protivnik mrdati?
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() != igracNaPotezu) continue;
				
				if(j % 2 == 1) {
					if(polja[i][(j+1) % polja[i].length].getTipPolja() == TipPolja.ZUTO || polja[i][j-1].getTipPolja() == TipPolja.ZUTO) {
						return null;
					}
					
					if(i == 0 || i == 2) {
						if(polja[1][j].getTipPolja() == TipPolja.ZUTO) {
							return null;
						}
					}
					
					if(i == 1) {
						if(polja[0][j].getTipPolja() == TipPolja.ZUTO || polja[2][j].getTipPolja() == TipPolja.ZUTO) {
							return null;
						}
					}
				}
				else {
					int a = j-1;
					if(a < 0) {
						a += polja[i].length;
					}
					if(polja[i][j+1].getTipPolja() == TipPolja.ZUTO || polja[i][a].getTipPolja() == TipPolja.ZUTO ) {
						return null;
					}
				}
			}
		}
		
		Rezultat rezultat;
		// vec je promenjen potez, tj. zapocet novi
		if(igracNaPotezu == TipPolja.PLAVO) {
			rezultat = new Rezultat("Crveni", crveniIgrac.getAlgoritam(), "Plavi", plaviIgrac.getAlgoritam());
		}
		else {
			rezultat = new Rezultat("Plavi", plaviIgrac.getAlgoritam(), "Crveni", crveniIgrac.getAlgoritam());
		}

		
		return rezultat;
	}

	public Polje[][] getPolja() {
		return polja;
	}

	public void setPolja(Polje[][] polja) {
		this.polja = polja;
	}
	
	public TipPolja getIgracNaPotezu() {
		return igracNaPotezu;
	}
	
	public void setIgracNaPotezu(TipPolja igracNaPotezu) {
		this.igracNaPotezu = igracNaPotezu;
	}

	public Igrac getPlaviIgrac() {
		return plaviIgrac;
	}

	public void setPlaviIgrac(Igrac plaviIgrac) {
		this.plaviIgrac = plaviIgrac;
	}

	public Igrac getCrveniIgrac() {
		return crveniIgrac;
	}

	public void setCrveniIgrac(Igrac crveniIgrac) {
		this.crveniIgrac = crveniIgrac;
	}
	
	public boolean isPojedi() {
		return pojedi;
	}

	public void setPojedi(boolean pojedi) {
		this.pojedi = pojedi;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public Polje getSelektovanoPolje() {
		return selektovanoPolje;
	}

	public void setSelektovanoPolje(Polje selektovanoPolje) {
		this.selektovanoPolje = selektovanoPolje;
	}
	
	public int krajIgreInt() {
		Rezultat rezultat = krajIgre();
		if(rezultat == null) return 0;
		
		String pobednik = rezultat.getPobednik();
		pobednik = pobednik.substring(0, pobednik.length() -1) + "o";
		
		if(!igracNaPotezu.name().equalsIgnoreCase(pobednik)) { 
			return -1;
		}
		
		return 1;
	}

	public double izracunajScore(TipPolja igracNaPotezu) {
		/*if(igracNaPotezu == TipPolja.PLAVO) {
			return plaviIgrac.getBrojPreostalihFigura() + (Controller.BROJ_FIGURA - crveniIgrac.getBrojPreostalihFigura());
		}
		
		return crveniIgrac.getBrojPreostalihFigura() + (Controller.BROJ_FIGURA - plaviIgrac.getBrojPreostalihFigura());
		*/
		score = 0.0;
		Rezultat rezultat = krajIgre();
		if(rezultat == null) {
			if(igracNaPotezu == TipPolja.PLAVO) {
				score = crveniIgrac.getBrojPreostalihFigura() - plaviIgrac.getBrojPreostalihFigura();
			}
			else {
				score = plaviIgrac.getBrojPreostalihFigura() - crveniIgrac.getBrojPreostalihFigura();
			}
			
			
			
			/*int plaviBrojTara = prebrojTare(TipPolja.PLAVO);
			int crveniBrojTara = prebrojTare(TipPolja.CRVENO); 
			
			int plaviBrojPoluTara = prebrojPoluTare(TipPolja.PLAVO);
			int crveniBrojPoluTara = prebrojPoluTare(TipPolja.CRVENO); 
			
			if(igracNaPotezu == TipPolja.PLAVO) {
				score += plaviIgrac.getBrojPreostalihFigura() - crveniIgrac.getBrojPreostalihFigura();
				score += konstantaZaMojeTare*plaviBrojTara - konstantaZaProtivnikoveTare*crveniBrojTara;
				score += konstantaZaMojePoluTare*plaviBrojPoluTara - konstantaZaProtivnikovePoluTare*crveniBrojPoluTara;
			}
			else {
				score += crveniIgrac.getBrojPreostalihFigura() - plaviIgrac.getBrojPreostalihFigura();
				score += konstantaZaMojeTare*crveniBrojTara - konstantaZaProtivnikoveTare*plaviBrojTara;
				score += konstantaZaMojePoluTare*crveniBrojPoluTara - konstantaZaProtivnikovePoluTare*plaviBrojPoluTara;
			}*/
			
			
		}
		else {
			String pobednik = rezultat.getPobednik();
			pobednik = pobednik.substring(0, pobednik.length() -1) + "o";
		
			if(!igracNaPotezu.name().equalsIgnoreCase(pobednik)) { // promenjen je igracNaPotezu, pobednik nije na potezu, vec je odigrao
				score = konstantaZaKrajIgre;
			}
			else {
				score = -konstantaZaKrajIgre;
			}
		}
	
		return score;
	}
	
	public int prebrojTare(TipPolja tipPolja) {
		int brojTara = 0;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojTara++;
				}
			}
		}
		
		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
			if(polja[0][k].getTipPolja() == tipPolja && polja[1][k].getTipPolja() == tipPolja && polja[2][k].getTipPolja() == tipPolja) {
				brojTara++;
			}
		}
		
		return brojTara;
	}
	
	public int prebrojPoluTare(TipPolja tipPolja) {
		// polu tare == situacije u kojima su dve figure vec u redu,
		// a treca moze biti stavljena u red vec u sledecem potezu
		
		/*  o + +    |    + + o     |   + o + 
		    +        |        +     |     + 
	    */
		
		boolean postavljene = daLiSuSveFigurePostavljene();
		
		int brojPoluTara = 0;
		
		int pomocniIndeks1;
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				pomocniIndeks1 = j-1;
				
				if (pomocniIndeks1 == -1) pomocniIndeks1 = 7;
				
				if(polja[i][j].getTipPolja() == TipPolja.ZUTO && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					if(!postavljene) {
						brojPoluTara++;
					}
					else if(polja[i][pomocniIndeks1].getTipPolja() == tipPolja) {
						brojPoluTara++;
					}
				}
				else if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == TipPolja.ZUTO 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					if(!postavljene) {
						brojPoluTara++;
					}
					else if(i == 0 || i == 2) {
						if(polja[1][(j+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojPoluTara++;
						}
					}
					else if(i == 1) {
						if(polja[0][(j+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja 
								|| polja[2][(j+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojPoluTara++;
						}
					}
				
				}
				else if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == TipPolja.ZUTO) {
					if(!postavljene) {
						brojPoluTara++;
					}
					else if(polja[i][(j+3)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
						brojPoluTara++;
					}
				}
			}
		}
		

		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
			if(polja[0][k].getTipPolja() == TipPolja.ZUTO && polja[1][k].getTipPolja() == tipPolja 
					&& polja[2][k].getTipPolja() == tipPolja) {
				if(!postavljene) {
					brojPoluTara++;
				}
				else if(polja[0][k-1].getTipPolja() == tipPolja || polja[0][(k+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojPoluTara++;
				}
				
			}
				
			else if(polja[0][k].getTipPolja() == tipPolja && polja[1][k].getTipPolja() == TipPolja.ZUTO 
					&& polja[2][k].getTipPolja() == tipPolja) {
				if(!postavljene) {
					brojPoluTara++;
				}
				else if(polja[1][k-1].getTipPolja() == tipPolja || polja[1][(k+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojPoluTara++;
				}
			}
			
			else if(polja[0][k].getTipPolja() == tipPolja && polja[1][k].getTipPolja() == tipPolja 
					&& polja[2][k].getTipPolja() == TipPolja.ZUTO) {
				if(!postavljene) {
					brojPoluTara++;
				}
				else if(polja[2][k-1].getTipPolja() == tipPolja || polja[2][(k+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojPoluTara++;
				}
			}
		}
		
		return brojPoluTara;
	}
	
	public int prebrojZiveTare(TipPolja tipPolja) {
		int brojZivihTara = 0;
		int prepravljenIndeksNakonOduzimanja, prepravljenIndeksNakonDodavanja;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				prepravljenIndeksNakonOduzimanja = j-1;
				if(prepravljenIndeksNakonOduzimanja == -1) prepravljenIndeksNakonOduzimanja = Controller.BROJ_POLJA_U_KRUGU-1;
				
				prepravljenIndeksNakonDodavanja = (j+3)%Controller.BROJ_POLJA_U_KRUGU;
				
				if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					
					if(polja[i][prepravljenIndeksNakonOduzimanja].getTipPolja() == TipPolja.ZUTO) {
						if(i == 0) {
							if(polja[i+1][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja 
									&& polja[i+2][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja) {
								brojZivihTara++;
							}
						}
						else if(i == 1) {
							if(polja[i-1][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja 
									&& polja[i+1][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja) {
								brojZivihTara++;
							}
						}
						else if(i == 2) {
							if(polja[i-1][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja 
									&& polja[i-2][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja) {
								brojZivihTara++;
							}
						}
					}
					
					if(polja[i][prepravljenIndeksNakonDodavanja].getTipPolja() == TipPolja.ZUTO) {
						if(i == 0) {
							if(polja[i+1][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja 
									&& polja[i+2][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja) {
								brojZivihTara++;
							}
						}
						else if(i == 1) {
							if(polja[i-1][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja 
									&& polja[i+1][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja) {
								brojZivihTara++;
							}
						}
						else if(i == 2) {
							if(polja[i-1][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja 
									&& polja[i-2][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja) {
								brojZivihTara++;
							}
						}
					}
					
					if(i == 0) {
						if(polja[i+1][j].getTipPolja() == tipPolja && polja[i+1][j+1].getTipPolja() == TipPolja.ZUTO 
								&& polja[i+1][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojZivihTara++;
						}
					}
					else if(i == 1) {
						if(polja[i-1][j].getTipPolja() == tipPolja && polja[i-1][j+1].getTipPolja() == TipPolja.ZUTO 
								&& polja[i-1][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojZivihTara++;
						}
						
						if(polja[i+1][j].getTipPolja() == tipPolja && polja[i+1][j+1].getTipPolja() == TipPolja.ZUTO 
								&& polja[i+1][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojZivihTara++;
						}
					}
					else if(i == 2) {
						if(polja[i-1][j].getTipPolja() == tipPolja && polja[i-1][j+1].getTipPolja() == TipPolja.ZUTO 
								&& polja[i-1][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojZivihTara++;
						}
					}
					
				}
				else if(polja[i][j].getTipPolja() == TipPolja.ZUTO && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					if(polja[0][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja 
							&& polja[1][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja
							&& polja[2][prepravljenIndeksNakonOduzimanja].getTipPolja() == tipPolja) {
						brojZivihTara++;
					}
				}
				
				else if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == TipPolja.ZUTO) {
					if(polja[0][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja  
							&& polja[1][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja
							&& polja[2][prepravljenIndeksNakonDodavanja].getTipPolja() == tipPolja) {
						brojZivihTara++;
					}
				}
			}
		}
		
		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
			if(polja[0][k].getTipPolja() == tipPolja && polja[1][k].getTipPolja() == tipPolja && polja[2][k].getTipPolja() == tipPolja) {
				brojZivihTara++;
			}
		}
		
		return brojZivihTara;
	}
	
	public boolean daLiSuSvaProtivnickaPoljaUTari(TipPolja tipPoljaZaJedenje) {
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() != tipPoljaZaJedenje) continue;
				
				if(!polja[i][j].isDaLiJeUTari()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void horizontalnaOsnaSimetrija() {
		int[] indeksi1 = {2, 3, 4};
		int[] indeksi2 = {0, 7, 6};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void vertikalnaOsnaSimetrija() {
		int[] indeksi1 = {0, 1, 2};
		int[] indeksi2 = {6, 5, 4};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void gornjiLeviDonjiDesniKosaOsnaSimetrija() {
		int[] indeksi1 = {1, 2, 3};
		int[] indeksi2 = {7, 6, 5};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void gornjiDesniDonjiLeviKosaOsnaSimetrija() {
		int[] indeksi1 = {3, 4, 5};
		int[] indeksi2 = {1, 0, 7};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void osnaSimetrija(int[] indeksi1, int[] indeksi2) {
		TipPolja pomTipPolja;
		boolean pomDaLiJeUTari;
		int indeks1, indeks2;
		
		boolean josNijePromenjenoPlavoSelektovanoPolje = true;
		boolean josNijePromenjenoCrvenoSelektovanoPolje = true;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < indeksi1.length; j++) {
				indeks1 = indeksi1[j];
				indeks2 = indeksi2[j];
				pomTipPolja = polja[i][indeks1].getTipPolja();
				pomDaLiJeUTari = polja[i][indeks1].isDaLiJeUTari();
				
				polja[i][indeks1].setTipPolja(polja[i][indeks2].getTipPolja());
				polja[i][indeks1].setDaLiJeUTari(polja[i][indeks2].isDaLiJeUTari());
				
				polja[i][indeks2].setTipPolja(pomTipPolja);
				polja[i][indeks2].setDaLiJeUTari(pomDaLiJeUTari);
				
				if(josNijePromenjenoPlavoSelektovanoPolje) {
					josNijePromenjenoPlavoSelektovanoPolje = !promeniSelektovanoPolje(polja, i, indeks1, indeks2);
				}
				
				if(josNijePromenjenoCrvenoSelektovanoPolje) {
					josNijePromenjenoCrvenoSelektovanoPolje = !promeniSelektovanoPolje(polja, i, indeks1, indeks2);
				}
			}
		}
	}
	
	private boolean promeniSelektovanoPolje(Polje[][] polja, int x, int indeks1, int indeks2) {
		if(selektovanoPolje != null) {
			if(selektovanoPolje.getPozicija().getX() == x) {
				if(selektovanoPolje.getPozicija().getY() == indeks1) {
					selektovanoPolje = polja[x][indeks2];
					return true;
				}
				else if(selektovanoPolje.getPozicija().getY() == indeks2) {
					selektovanoPolje = polja[x][indeks1];
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void promeniIgracNaPotezuIgraceIBojeFigurama() {
		TipPolja stariIgracNaPotezu = igracNaPotezu;
		
		if(igracNaPotezu == TipPolja.PLAVO) igracNaPotezu = TipPolja.CRVENO;
		else igracNaPotezu = TipPolja.PLAVO;
		
		Igrac pomIgrac = plaviIgrac;
		plaviIgrac = crveniIgrac;
		crveniIgrac = pomIgrac;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if (polja[i][j].getTipPolja() == TipPolja.ZUTO) continue;
				
				if(polja[i][j].getTipPolja() == stariIgracNaPotezu) {
					polja[i][j].setTipPolja(igracNaPotezu);
				}
				else {
					polja[i][j].setTipPolja(stariIgracNaPotezu);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sbPolja = new StringBuilder("");
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				sbPolja.append(";");
				sbPolja.append(polja[i][j]);
			}
		}
		
		StringBuilder sb = new StringBuilder("");
		sb.append(sbPolja.toString().substring(1));
		sb.append(":"); sb.append(plaviIgrac); sb.append(":"); sb.append(crveniIgrac);
		sb.append(":"); sb.append(igracNaPotezu.ordinal()); sb.append(":");
		if(selektovanoPolje != null) {
			//sb.append(selektovanoPolje.getPozicija().getX()); 
			//sb.append(",");
			//sb.append(selektovanoPolje.getPozicija().getY());
			sb.append(selektovanoPolje.getPozicija().getX() * Controller.BROJ_POLJA_U_KRUGU + selektovanoPolje.getPozicija().getY());
		}
		else {
			//sb.append(-1); 
			//sb.append(",");
			//sb.append(-1);
			sb.append(-1);
		}
		
		return sb.toString();
	}
	
	public String toStringForNN() {
		StringBuilder sb = new StringBuilder("");
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				sb.append(";");
				sb.append(polja[i][j]);
			}
		}
		
		sb.append(";"); sb.append(plaviIgrac); sb.append(";"); sb.append(crveniIgrac);
		sb.append(";"); sb.append(igracNaPotezu.ordinal()); sb.append(";");
		if(selektovanoPolje != null) {
			//sb.append(selektovanoPolje.getPozicija().getX()); 
			//sb.append(",");
			//sb.append(selektovanoPolje.getPozicija().getY());
			sb.append(selektovanoPolje.getPozicija().getX() * Controller.BROJ_POLJA_U_KRUGU + selektovanoPolje.getPozicija().getY());
		}
		else {
			//sb.append(-1); 
			//sb.append(",");
			//sb.append(-1);
			sb.append(-1);
		}
		
		int done;
		if(krajIgre() == null) done = 0;
		else done = 1;
		
		sb.append(";");
		sb.append(done);
		
		return sb.substring(1);
	}
	
	public ArrayList<PoljeAkcija> getMogucaSelektovanaPoljaIAkcijeZaDatoStanje() {
		ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije = new ArrayList<PoljeAkcija>();
		
		Polje selektovanoPolje;
		int indeksAkcije;
		
		Igrac igrac;
		TipPolja tipPoljaZaJedenje;
        if(igracNaPotezu == TipPolja.PLAVO) {
        	igrac = plaviIgrac;
        	tipPoljaZaJedenje = TipPolja.CRVENO;
        }
        else {
        	igrac = crveniIgrac;
        	tipPoljaZaJedenje = TipPolja.PLAVO;
        }
			 
        boolean postavljene = daLiSuSveFigurePostavljene();
        
        boolean sveFigureUTarama = daLiSuSvaProtivnickaPoljaUTari(tipPoljaZaJedenje);
        
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				selektovanoPolje = polja[i][j];
				
				if (pojedi) {
					if(selektovanoPolje.getTipPolja() != igracNaPotezu && selektovanoPolje.getTipPolja() != TipPolja.ZUTO) {
						if(!sveFigureUTarama && selektovanoPolje.isDaLiJeUTari()) continue;
						
						indeksAkcije = selektovanoPolje.getPozicija().getX()*Controller.BROJ_POLJA_U_KRUGU + selektovanoPolje.getPozicija().getY();
						mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, Akcije.JEDENJE[indeksAkcije]));
					}
				} 
				else {
					if(postavljene) {
						if(selektovanoPolje.getTipPolja() == igracNaPotezu) {
							if(igrac.getBrojPreostalihFigura() == 3) {
								proveriDaLiSuSkokoviMoguci(mogucaSelektovanaPoljaIAkcije, selektovanoPolje);
							}
							proveriDaLiSuGDLDAkcijeMoguce(mogucaSelektovanaPoljaIAkcije, selektovanoPolje);
						}
					}
					else {
						if(selektovanoPolje.getTipPolja() == TipPolja.ZUTO) {
							indeksAkcije = selektovanoPolje.getPozicija().getX()*Controller.BROJ_POLJA_U_KRUGU + selektovanoPolje.getPozicija().getY();
							mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, Akcije.POSTAVLJANJE[indeksAkcije]));
						}
					}
				}
				
			}
		}
		
		return mogucaSelektovanaPoljaIAkcije;
	}
	
	private void proveriDaLiSuSkokoviMoguci(ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije, Polje selektovanoPolje) {
		for (int k = 0; k < Akcije.SKOKOVI.length; k++) {
			if(polja[k/Controller.BROJ_POLJA_U_KRUGU][k%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() != TipPolja.ZUTO) {
				continue;
			}
			
			mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, Akcije.SKOKOVI[k]));
		}
		
	}
	
	private void proveriDaLiSuGDLDAkcijeMoguce(ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije, Polje selektovanoPolje) {
		Pozicija pozicija, koraci;
		int slojZaNovoPolje, indeksUSlojuZaNovoPolje;
		
		for(Akcija akcija: Akcije.GDLD) {
			pozicija = selektovanoPolje.getPozicija();
			if(pozicija.getY()% 2 == 0 && (akcija == Akcija.GORE || akcija == Akcija.DOLE)) continue; 
			
			koraci = Akcije.mapiranjeGDLDAkcijaNaKorake.get(akcija);
			slojZaNovoPolje = pozicija.getX() + koraci.getX();
			if( slojZaNovoPolje < 0 || slojZaNovoPolje > (Controller.BROJ_KRUGOVA-1)) continue;
			
			indeksUSlojuZaNovoPolje = (pozicija.getY() + koraci.getY()) % Controller.BROJ_POLJA_U_KRUGU;
			if (indeksUSlojuZaNovoPolje == -1) indeksUSlojuZaNovoPolje = Controller.BROJ_POLJA_U_KRUGU - 1;
			
			if(polja[slojZaNovoPolje][indeksUSlojuZaNovoPolje].getTipPolja() != TipPolja.ZUTO) continue;
			
			mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, akcija));
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crveniIgrac == null) ? 0 : crveniIgrac.hashCode());
		result = prime * result + ((igracNaPotezu == null) ? 0 : igracNaPotezu.hashCode());
		result = prime * result + ((plaviIgrac == null) ? 0 : plaviIgrac.hashCode());
		result = prime * result + Arrays.deepHashCode(polja);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Stanje) {
			Stanje stanje = (Stanje) obj;
			
			for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
				for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
					if(polja[i][j].getTipPolja() != stanje.polja[i][j].getTipPolja()) return false;
				}
			}
			
			if(igracNaPotezu != stanje.igracNaPotezu) return false;
			
			if(!crveniIgrac.equals(stanje.crveniIgrac)) return false;
			
			if(!plaviIgrac.equals(stanje.plaviIgrac)) return false;
			
			if(score != stanje.score) return false; 
			
			return true;
		}
		
		return false;
	}
	
}
