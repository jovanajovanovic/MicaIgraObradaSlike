package com.mica.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.mica.main.Akcija;
import com.mica.main.Controller;
import com.mica.main.PoljeAkcija;
import com.mica.main.Potez;
import com.mica.main.QVrednostSelektovanoPoljeAkcija;
import com.mica.main.Stanje;
import com.mica.main.StanjeAkcija;
import com.mica.main.TipPolja;

public class ReinforcementLearning {
	private HashMap<StanjeAkcija, QVrednost> qVrednosti;
	
	private HashMap<PoljeAkcija, Double> qVrednostiZaMoguceAkcije = null;
	
	private double epsilon; 
	private double zanemarivanje =  0.9;
	
	private HashMap<String, Double> tezine;
	
	private Random random = new Random();
	
	private final int GRANICA_ZA_PRELAZ_NA_APROKSIMACIJU = 500000;

	public ReinforcementLearning(HashMap<StanjeAkcija, QVrednost> qVrednosti, HashMap<String, Double> tezine) {
		this.qVrednosti = qVrednosti;
		this.tezine = tezine;
		
		System.out.println("Spreman! " + qVrednosti.size());
		
		this.epsilon = 1.0;
	}
	
	public HashMap<StanjeAkcija, QVrednost> getqVrednosti() {
		return qVrednosti;
	}

	public void setqVrednosti(HashMap<StanjeAkcija, QVrednost> qVrednosti) {
		this.qVrednosti = qVrednosti;
	}

	/*public HashMap<StanjeAkcija, Integer> getBrojMenjanjaQVrednosti() {
		return brojMenjanjaQVrednosti;
	}

	public void setBrojMenjanjaQVrednosti(HashMap<StanjeAkcija, Integer> brojMenjanjaQVrednosti) {
		this.brojMenjanjaQVrednosti = brojMenjanjaQVrednosti;
	}
	*/
	
	public static boolean baciNovcic(double epsilon) {	
		double randomVrednost = new Random().nextDouble();
		System.out.println("Random vrednost: " + randomVrednost);
		
	    return randomVrednost < epsilon;
	}
	
	/*public ArrayList<PoljeAkcija> getMogucaSelektovanaPoljaIAkcijeZaDatoStanje(Stanje stanje) {
		ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije = new ArrayList<PoljeAkcija>();
		
		Polje[][] polja = stanje.getPolja();
		Polje selektovanoPolje;
		int indeksAkcije;
		
		Igrac igrac;
		TipPolja tipPoljaZaJedenje;
        if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) {
        	igrac = stanje.getPlaviIgrac();
        	tipPoljaZaJedenje = TipPolja.CRVENO;
        }
        else {
        	igrac = stanje.getCrveniIgrac();
        	tipPoljaZaJedenje = TipPolja.PLAVO;
        }
			 
        boolean postavljene = stanje.daLiSuSveFigurePostavljene();
        
        boolean sveFigureUTarama = stanje.daLiSuSvaProtivnickaPoljaUTari(tipPoljaZaJedenje);
        
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				selektovanoPolje = polja[i][j];
				
				if (stanje.isPojedi()) {
					if(selektovanoPolje.getTipPolja() != stanje.getIgracNaPotezu() && selektovanoPolje.getTipPolja() != TipPolja.ZUTO) {
						if(!sveFigureUTarama && selektovanoPolje.isDaLiJeUTari()) continue;
						
						indeksAkcije = selektovanoPolje.getPozicija().getX()*Controller.BROJ_POLJA_U_KRUGU + selektovanoPolje.getPozicija().getY();
						mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, Akcije.JEDENJE[indeksAkcije]));
					}
				} 
				else {
					if(postavljene) {
						if(selektovanoPolje.getTipPolja() == stanje.getIgracNaPotezu()) {
							if(igrac.getBrojPreostalihFigura() == 3) {
								proveriDaLiSuSkokoviMoguci(stanje.getPolja(), mogucaSelektovanaPoljaIAkcije, selektovanoPolje);
							}
							proveriDaLiSuGDLDAkcijeMoguce(stanje.getPolja(), mogucaSelektovanaPoljaIAkcije, selektovanoPolje);
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
	}*/
	
	/*private void proveriDaLiSuSkokoviMoguci(ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije, Polje selektovanoPolje) {
		for (int k = 0; k < Akcije.SKOKOVI.length; k++) {
			if(polja[k/Controller.BROJ_POLJA_U_KRUGU][k%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() != TipPolja.ZUTO) {
				continue;
			}
			
			mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, Akcije.SKOKOVI[k]));
		}
		
	}*/
	
	/*private void proveriDaLiSuGDLDAkcijeMoguce(Polje[][] polja, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije, Polje selektovanoPolje) {
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
		
	}*/

	public PoljeAkcija getNovoSelektovanoPoljeIAkcija(Stanje stanje) {
		ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije = stanje.getMogucaSelektovanaPoljaIAkcijeZaDatoStanje();
		if(mogucaSelektovanaPoljaIAkcije.isEmpty()) {
			return null;
		}
		
		
        if (baciNovcic(this.epsilon)) {
        	// TODO: bolje bi bilo prvo proveriti moze li se doci u stanje koje jos nikad nismo posetili,
        	// ako ima takvih, izabracemo random stanje od tih neposecenih, a ako nema neposecenih, tj. sva su posecena,
        	// onda izabrati random od svih mogucih stanja 
        	int randomIndeks = this.random.nextInt(mogucaSelektovanaPoljaIAkcije.size());
        	qVrednostiZaMoguceAkcije = null;

        	return mogucaSelektovanaPoljaIAkcije.get(randomIndeks);
        }
        
        postaviQVrednostiZaMoguceAkcije(stanje, mogucaSelektovanaPoljaIAkcije);
        
        PoljeAkcija pa = getAkcijaPoPolotici(stanje, mogucaSelektovanaPoljaIAkcije);
        /*if(pa == null) {
        	return null;
        }*/
        return pa;
	}
	
	
	private void postaviQVrednostiZaMoguceAkcije(Stanje stanje, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije) {
		qVrednostiZaMoguceAkcije = new HashMap<PoljeAkcija, Double>();
        
        PoljeAkcija selektovanoPoljeAkcija;
        QVrednost qVrednost;
        double vrednost;
        Stanje kopijaStanja = new Stanje(stanje);
        StanjeAkcija trenutnoStanjeAKcija = new StanjeAkcija();
        trenutnoStanjeAKcija.setStanje(kopijaStanja);
		
		for(int i = 0; i < mogucaSelektovanaPoljaIAkcije.size(); i++) {
        	selektovanoPoljeAkcija = mogucaSelektovanaPoljaIAkcije.get(i);
        	
        	kopijaStanja.setSelektovanoPolje(selektovanoPoljeAkcija.getPolje());
    		trenutnoStanjeAKcija.setAkcija(selektovanoPoljeAkcija.getAkcija());
    		
        	if(daLiJePredjenaGranicaZaPrelazNaAproksimaciju()) {
        		vrednost = getAproksimiranaQVrednost(trenutnoStanjeAKcija);
        	}
        	else {
	        	qVrednost = getQVrednost(trenutnoStanjeAKcija);
	            if(qVrednost != null) vrednost = qVrednost.getVrednost();
	            else vrednost = 0.0;
        	}
        	
        	qVrednostiZaMoguceAkcije.put(selektovanoPoljeAkcija, vrednost);
            
        }
	}

	public PoljeAkcija getAkcijaPoPolotici(Stanje stanje, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije) {
		return izracunajNajboljuAkcijuPoQvrednostima(stanje, mogucaSelektovanaPoljaIAkcije).getSelektovanoPoljeAkcija();
	}

	public void postaviNovuQVrednost(Stanje stanje, Akcija akcija, Stanje sledeceStanje, double nagrada) {
		StanjeAkcija stanjeAkcija = new StanjeAkcija(stanje, akcija);
		
		double alfa;
		int n;
		/*Integer n = this.brojMenjanjaQVrednosti.get(stanjeAkcija);
		if(n == null) {
			alfa = 1;
			n = 0;
		}
		else {	
			alfa = 1 / n;
		}*/
		
		// gledamo potez u napred
		ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije = sledeceStanje.getMogucaSelektovanaPoljaIAkcijeZaDatoStanje();
		QVrednostSelektovanoPoljeAkcija maksimumQVrednostSelektovanoPoljeAkcija = izracunajNajboljuAkcijuPoQvrednostima(sledeceStanje, mogucaSelektovanaPoljaIAkcije);
		double uzorak = nagrada + Math.pow(this.zanemarivanje, Controller.brojPoteza)*maksimumQVrednostSelektovanoPoljeAkcija.getqVrednost();
		
		QVrednost staraQVrednost = getQVrednost(stanjeAkcija);
		double staraVrednost;
		
		if(staraQVrednost != null) {
			staraVrednost = staraQVrednost.getVrednost();
			n = staraQVrednost.getBrojMenjanja();
			alfa = 1 / n;
		}
		else {
			staraVrednost = 0.0;
			alfa = 1;
			n = 0;
		}
		
		if(daLiJePredjenaGranicaZaPrelazNaAproksimaciju()) {
			postaviNoveTezine(stanjeAkcija, uzorak, alfa);
		}
		else {
			// ako nismo dovoljno naucili, tj. jos nemamo dovoljno q vrednosti (jos ne radimo aproksimaciju)
			
			//double novaVrednost = (1 - alfa) * staraVrednost + alfa * (nagrada + Math.pow(this.zanemarivanje, Controller.brojPoteza)*maksimumQVrednostSelektovanoPoljeAkcija.getqVrednost());
			double novaVrednost = (1 - alfa) * staraVrednost + alfa * uzorak;
			QVrednost novaQVrednost = new QVrednost(novaVrednost, n+1);

			System.out.println("Stara qVrednost: " + staraVrednost);
			System.out.println("Nova qVrednst: " + novaQVrednost.getVrednost());
			
			this.qVrednosti.put(stanjeAkcija, novaQVrednost);
			//this.brojMenjanjaQVrednosti.put(stanjeAkcija, n+1);
			System.out.println("Stari broj menjanja qVrednosti: " + n);
			//System.out.println("Novi broj menjanja qVrednosti: " + this.qVrednosti.get(stanjeAkcija).getBrojMenjanja());
		}
		 
	}
		
	private void postaviNoveTezine(StanjeAkcija stanjeAkcija, double uzorak, double alfa) {
		double staraVrednost = getAproksimiranaQVrednost(stanjeAkcija);
		Stanje stanje = stanjeAkcija.getStanje();
		
		TipPolja protivnik;
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) protivnik = TipPolja.CRVENO;
		else protivnik = TipPolja.PLAVO;
		
		double razlika = uzorak - staraVrednost;
		
		double novaTezinaZaBrojTara = tezine.get("tezina_za_broj_tara") + alfa * razlika * stanje.prebrojTare(stanje.getIgracNaPotezu());
		double novaTezinaZaBrojTaraProtivnika = tezine.get("tezina_za_broj_tara_protivnika") + alfa * razlika * stanje.prebrojTare(protivnik);
		double novaTezinaZaBrojPoluTara = tezine.get("tezina_za_broj_polutara") + alfa * razlika * stanje.prebrojPoluTare(stanje.getIgracNaPotezu());
		double novaTezinaZaBrojPoluTaraProtivnika = tezine.get("tezina_za_broj_polutara_protivnika") + alfa * razlika * stanje.prebrojPoluTare(protivnik);
		double novaTezinaZaBrojZivihTara = tezine.get("tezina_za_broj_zivih_tara") + alfa * razlika * stanje.prebrojZiveTare(stanje.getIgracNaPotezu());
		double novaTezinaZaBrojZivihTaraProtivnika = tezine.get("tezina_za_broj_zivih_tara_protivnika") + alfa * razlika * stanje.prebrojZiveTare(protivnik);
		//double novaTezinaZaKrajIgre = tezine.get("tezina_za_kraj_igre")  + alfa * razlika * stanje.krajIgreInt();
		
		System.out.println("Stara tezina za broj tara: " + tezine.get("tezina_za_broj_tara") + ", nova tezina za broj tara: " + novaTezinaZaBrojTara);
		System.out.println("Stara tezina za broj tara protivnika: " + tezine.get("tezina_za_broj_tara_protivnika") + ", nova tezina za broj tara protivnika: " + novaTezinaZaBrojTaraProtivnika);
		System.out.println("Stara tezina za broj polutara: " + tezine.get("tezina_za_broj_polutara") + ", nova tezina za broj polutara: " + novaTezinaZaBrojPoluTara);
		System.out.println("Stara tezina za broj polutara protivnika: " + tezine.get("tezina_za_broj_polutara_protivnika") + ", nova tezina za broj polutara protivnika: " + novaTezinaZaBrojPoluTaraProtivnika);
		System.out.println("Stara tezina za broj zivih tara: " + tezine.get("tezina_za_broj_zivih_tara") + ", nova tezina za broj zivih tara: " + novaTezinaZaBrojZivihTara);
		System.out.println("Stara tezina za broj zivih tara protivnika: " + tezine.get("tezina_za_broj_zivih_tara_protivnika") + ", nova tezina za broj zivih tara protivnika: " + novaTezinaZaBrojZivihTaraProtivnika);
		//System.out.println("Stara tezina za kraj igre: " + tezine.get("tezina_za_kraj_igre") + ", nova tezina za kraj igre: " + novaTezinaZaKrajIgre);
		
		
		// ovde menjamo tezine vaznih karakteristika za micu, a samim tim uticemo i na q-vrednosti
		// jer q vrednosti zavise od tezina
		tezine.put("tezina_za_broj_tara", novaTezinaZaBrojTara);
		tezine.put("tezina_za_broj_tara_protivnika", novaTezinaZaBrojTaraProtivnika);
		tezine.put("tezina_za_broj_polutara", novaTezinaZaBrojPoluTara);
		tezine.put("tezina_za_broj_polutara_protivnika", novaTezinaZaBrojPoluTaraProtivnika);
		tezine.put("tezina_za_broj_zivih_tara", novaTezinaZaBrojZivihTara);
		tezine.put("tezina_za_broj_zivih_tara_protivnika", novaTezinaZaBrojZivihTaraProtivnika);
		//tezine.put("tezina_za_kraj_igre", novaTezinaZaKrajIgre);
	}

	public boolean daLiJePredjenaGranicaZaPrelazNaAproksimaciju() {
		return qVrednosti.size() > GRANICA_ZA_PRELAZ_NA_APROKSIMACIJU;
	}

	public QVrednostSelektovanoPoljeAkcija izracunajNajboljuAkcijuPoQvrednostima(Stanje stanje, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije) {
		double maksimumQVrednost = -Double.MAX_VALUE;
		QVrednost qVrednost;
		double vrednost;
        PoljeAkcija maksimumSelektovanoPoljeAkcija = null;

        /*Igrac igrac;
        if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) {
        	igrac = stanje.getPlaviIgrac();
        }
        else {
        	igrac = stanje.getCrveniIgrac();
        }
        */
        
        Stanje kopijaStanja = new Stanje(stanje);
    
        StanjeAkcija trenutnoStanjeAKcija = new StanjeAkcija();
        trenutnoStanjeAKcija.setStanje(kopijaStanja);
        
        qVrednostiZaMoguceAkcije = new HashMap<PoljeAkcija, Double>();
        
        PoljeAkcija selektovanoPoljeAkcija;
        for(int i = 0; i < mogucaSelektovanaPoljaIAkcije.size(); i++) {
        	selektovanoPoljeAkcija = mogucaSelektovanaPoljaIAkcije.get(i);
        	
        	kopijaStanja.setSelektovanoPolje(selektovanoPoljeAkcija.getPolje());
    		trenutnoStanjeAKcija.setAkcija(selektovanoPoljeAkcija.getAkcija());
    		
        	if(qVrednosti.size() > GRANICA_ZA_PRELAZ_NA_APROKSIMACIJU) {
        		vrednost = getAproksimiranaQVrednost(trenutnoStanjeAKcija);
        	}
        	else {
	        	qVrednost = getQVrednost(trenutnoStanjeAKcija);
	            if(qVrednost != null) vrednost = qVrednost.getVrednost();
	            else vrednost = 0.0;
        	}
        	
        	qVrednostiZaMoguceAkcije.put(selektovanoPoljeAkcija, vrednost);
        	System.out.println("*****************VREDNOST: " + vrednost);
            if (vrednost > maksimumQVrednost) {
            	maksimumQVrednost = vrednost;
            	maksimumSelektovanoPoljeAkcija = selektovanoPoljeAkcija;
            }
            
        }
        
        return new QVrednostSelektovanoPoljeAkcija(maksimumQVrednost, maksimumSelektovanoPoljeAkcija);
	}

	public QVrednost getQVrednost(StanjeAkcija key) {
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Pronasao vec postojecu qvrednost");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama();
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Promenjena boja nad obicnim stanjem");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama(); // vrati na staro
		key.getStanje().horizontalnaOsnaSimetrija();
	
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Horizontalna osna simetrija");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama();
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Horizontalna osna simetrija i promenjena boja");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama(); // vrati na staro
		key.getStanje().horizontalnaOsnaSimetrija(); // vrati na staro
		key.getStanje().vertikalnaOsnaSimetrija();
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Vertikalna osna simetrija");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama();
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Vertikalna osna simetrija i promenjena boja");
			return this.qVrednosti.get(key);
		}
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama(); // vrati na staro
		key.getStanje().vertikalnaOsnaSimetrija(); // vrati na staro
		key.getStanje().gornjiLeviDonjiDesniKosaOsnaSimetrija();
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Gornji levi donji desni kosa osna simetrija");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama();
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Gornji levi donji desni kosa osna simetrija i promenjena boja");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama(); // vrati na staro
		key.getStanje().gornjiLeviDonjiDesniKosaOsnaSimetrija(); // vrati na staro
		key.getStanje().gornjiDesniDonjiLeviKosaOsnaSimetrija();
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Gornji desni donji levi kosa osna simetrija");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama();
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Gornji desni donji levi kosa osna simetrija i promenjena boja");
			return this.qVrednosti.get(key);
		}
		
		key.getStanje().promeniIgracNaPotezuIgraceIBojeFigurama(); // vrati na staro
		key.getStanje().gornjiDesniDonjiLeviKosaOsnaSimetrija(); // vrati na staro
		
		return null;
	}
	
	public double getAproksimiranaQVrednost(StanjeAkcija stanjeAkcija) {
		Stanje stanje = new Stanje(stanjeAkcija.getStanje()); 
		
		Potez potez = Controller.pretvoriSelektovanoPoljeiAkcijuUPotez(stanje, new PoljeAkcija(stanje.getSelektovanoPolje(), stanjeAkcija.getAkcija()));
		if(potez == null) {
			System.out.println();
		}
		if(potez.getAkcija().name().contains("POJEDI")) {
			if(potez.getPolje() == null) {
				System.out.println();
			}
			potez.getPolje().setTipPolja(TipPolja.ZUTO);
		}
		else {
			if(potez.getPolje() == null) {
				System.out.println();
			}
			/*if(potez.getSelektovanoPolje() == null) {
				System.out.println();
			}*/
			
			Controller.izvrsiPostavljanjeIliPomeranjeFigura(stanje, potez.getPolje(), potez.getSelektovanoPolje());
		}
		
		stanje.podesiPoljaUTaramaIVanNjih();
		
		double qVrednost = 0.0;
		 
		TipPolja protivnik;
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) protivnik = TipPolja.CRVENO;
		else protivnik = TipPolja.PLAVO;
		
		qVrednost += tezine.get("tezina_za_broj_tara") * stanje.prebrojTare(stanje.getIgracNaPotezu());
		qVrednost += tezine.get("tezina_za_broj_tara_protivnika") * stanje.prebrojTare(protivnik);
		qVrednost += tezine.get("tezina_za_broj_polutara") * stanje.prebrojPoluTare(stanje.getIgracNaPotezu());
		qVrednost += tezine.get("tezina_za_broj_polutara_protivnika") * stanje.prebrojPoluTare(protivnik);
		qVrednost += tezine.get("tezina_za_broj_zivih_tara") * stanje.prebrojZiveTare(stanje.getIgracNaPotezu());
		qVrednost += tezine.get("tezina_za_broj_zivih_tara_protivnika") * stanje.prebrojZiveTare(protivnik);
		//qVrednost += tezine.get("tezina_za_kraj_igre") * stanje.krajIgreInt();
	
		return qVrednost;
	}
	
	public HashMap<PoljeAkcija, Double> getqVrednostiZaMoguceAkcije() {
		return qVrednostiZaMoguceAkcije;
	}

	public void setqVrednostiZaMoguceAkcije(HashMap<PoljeAkcija, Double> qVrednostiZaMoguceAkcije) {
		this.qVrednostiZaMoguceAkcije = qVrednostiZaMoguceAkcije;
	}

	public HashMap<String, Double> getTezine() {
		return tezine;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
}
