package grad;

import java.awt.Color;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import ekran.Ekran;
import objekti.Ambulanta;
import osobe.AmbulantnoVozilo;
import osobe.BezopasniUcesnik;
import osobe.Dijete;
import osobe.PunoljetnaOsoba;
import osobe.RizicniUcesnik;
import osobe.Stanovnik;
import osobe.Ucesnik;
import util.Util;

public class Grad implements Externalizable{
	
	private static int dimenzijaGrada;
	private static int ukupnaPopulacija;
	private static int brojKuca;
	private static int brojStaraca;
	private static int brojOdraslih;
	private static int brojDjece;
	private static int brojPunktova;
	private static int brojOporavljenihLjudi;
	private static int brojZarazenihLjudi;
	public static Polje mapa[][];
	private static ArrayDeque<Stanovnik> zarazeniNaCekanju = new ArrayDeque<>();
	private static ArrayList<Ambulanta> ambulante = new ArrayList<>();

	static {
		dimenzijaGrada = new Random().nextInt(16) + 15;
		System.out.println(dimenzijaGrada);
	}

	public Grad()
	{
		
	}
	public Grad(int dimenzijaMapeGrada) {
		mapa = new Polje[dimenzijaMapeGrada][dimenzijaMapeGrada];
		for (int i = 0; i < dimenzijaMapeGrada; i++) {
			for (int j = 0; j < dimenzijaMapeGrada; j++) {
				mapa[i][j] = new Polje();
			}
		}
		for (int i = 0; i < dimenzijaMapeGrada; i++) {
			mapa[i][0].zauzmiBezopasnoj();
			mapa[0][i].zauzmiBezopasnoj();
			mapa[dimenzijaMapeGrada - 1][i].zauzmiBezopasnoj();
			mapa[i][dimenzijaMapeGrada - 1].zauzmiBezopasnoj();

		}

	}

	public static synchronized void povecajBrojZarazenih() {
		brojZarazenihLjudi++;
	}

	public static synchronized void povecajBrojOporavljenih() {
		brojOporavljenihLjudi++;
	}

	public static synchronized void smanjiBrojZarazenih() {
		brojZarazenihLjudi--;
	}

	public static synchronized void smanjiBrojOporavljenih() {
		brojOporavljenihLjudi--;
	}

	public static void saljiAmbulantnaVozila() {

		for (Stanovnik zarazen : zarazeniNaCekanju) {
			for (Ambulanta ambulanta : ambulante) {
				if (ambulanta.imaLiSlobodnihKreveta() && ambulanta.imaLiSlobodnihVozila()) {
					ambulanta.posaljiVozilo(zarazen);
					zarazen.getKuca().setAlarm(true); // ovo doda da se vrate kuci kada po njega krene hitna 
					zarazeniNaCekanju.remove(zarazen);
					break;
				}
			}
		}
		Ekran.buttonPosaljiAmbulantnoVozilo.setBackground(Color.CYAN);

	}

	public synchronized boolean[] pribaviInfoZaKretanjePunoljetnihOrtogonalno(int x, int y, int smjer) {
		boolean[] info = { true, true, true, true };
		int[][] pozicija = { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
		switch (smjer) {
		case 0:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x + i * 2;
				pozicija[i + 1][1] = y - 3;
			}
			pozicija[3][0] = x;
			pozicija[3][1] = y - 2;
			break;
		case 2:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x + 3;
				pozicija[i + 1][1] = y + i * 2;
			}
			pozicija[3][0] = x + 2;
			pozicija[3][1] = y;
			break;
		case 4:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x - i * 2;
				pozicija[i + 1][1] = y + 3;
			}
			pozicija[3][0] = x;
			pozicija[3][1] = y + 2;
			break;
		case 6:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x - 3;
				pozicija[i + 1][1] = y - i * 2;
			}
			pozicija[3][0] = x - 2;
			pozicija[3][1] = y;
			break;
		}
		for (int i = 0; i < 4; i++) {
			int px = pozicija[i][0];
			int py = pozicija[i][1];
			if (px > 0 && px < dimenzijaGrada - 1 && py > 0 && py < dimenzijaGrada - 1) // napravi static za maximalnu
																						
				info[i] = mapa[px][py].provjeriZaPunoljetne();
			else
				info[i] = false;
		}
		return info; // provjeri i za fizicke pozicije
	}

	public synchronized boolean[] pribaviInfoZaKretanjePunoljetnihDijagonalno(int x, int y, int smjer) {
		boolean[] info = { true, true, true, true };
		int[][] pozicija = { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
		switch (smjer) {
		case 1:
			pozicija[0][0] = x; // najljevlji
			pozicija[0][1] = y - 3;
			pozicija[1][0] = x + 3;
			pozicija[1][1] = y - 3;
			pozicija[2][0] = x + 3; // srednji
			pozicija[2][1] = y;
			pozicija[3][0] = x + 2; // specijalni
			pozicija[3][1] = y - 2;
			break;
		case 3:
			pozicija[0][0] = x + 3; // najljevlji
			pozicija[0][1] = y;
			pozicija[1][0] = x + 3;
			pozicija[1][1] = y + 3;
			pozicija[2][0] = x; // srednji
			pozicija[2][1] = y + 3;
			pozicija[3][0] = x + 2; // specijalni
			pozicija[3][1] = y + 2;
			break;
		case 5:

			pozicija[0][0] = x; // najljevlji
			pozicija[0][1] = y + 3;
			pozicija[1][0] = x - 3;
			pozicija[1][1] = y + 3;
			pozicija[2][0] = x - 3; // srednji
			pozicija[2][1] = y;
			pozicija[3][0] = x - 2; // specijalni
			pozicija[3][1] = y + 2;
			break;
		case 7:

			pozicija[0][0] = x - 3; // najljevlji
			pozicija[0][1] = y;
			pozicija[1][0] = x - 3;
			pozicija[1][1] = y - 3;
			pozicija[2][0] = x; // srednji
			pozicija[2][1] = y - 3;
			pozicija[3][0] = x - 2; // specijalni
			pozicija[3][1] = y - 2;
			break;
		}

		for (int i = 0; i < 4; i++) {
			int px = pozicija[i][0];
			int py = pozicija[i][1];
			if (px > 0 && px < dimenzijaGrada - 1 && py > 0 && py < dimenzijaGrada - 1) // napravi static za maximalnu
																						
				info[i] = mapa[px][py].provjeriZaPunoljetne();
			else
				info[i] = false;
		}

		return info; // provjeri i za fizicke pozicije
	}	
	public synchronized void zauzmiOblastUOdgovarajucojMatrici(Ucesnik ucesnik, int pozicijaOsobePoX,
			int pozicijaOsobePoY) {
		if (ucesnik instanceof RizicniUcesnik) {
			for (int i = pozicijaOsobePoX - 1; i <= pozicijaOsobePoX + 1; i++) {
				for (int j = pozicijaOsobePoY - 1; j <= pozicijaOsobePoY + 1; j++) {
					if (i > 0 && i < dimenzijaGrada - 1 && j > 0 && j < dimenzijaGrada - 1) 
					mapa[i][j].zauzmiZaRizicnu();
				}
			}
		}
		synchronized (mapa[pozicijaOsobePoX][pozicijaOsobePoY]) {
			mapa[pozicijaOsobePoX][pozicijaOsobePoY].zauzmiBezopasnoj();
		}
	}

	public void ponistiZauzetostRizicnihNaPoziciji(int x, int y) {
		mapa[x][y].oslobodiZaPunoljetnu();
	}

	public void ponistiZauzetostPoljaBezopasnihNaPoziciji(int x, int y) {
		mapa[x][y].oslobodiZaBezopasne();
	}

	public synchronized void oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(Ucesnik ucesnik, int stariX, int stariY,
			int smjer) {
		if (ucesnik instanceof RizicniUcesnik) {
			PunoljetnaOsoba pomPOsoba = (PunoljetnaOsoba) ucesnik;
			switch (smjer) {
			case 0:
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY + 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX, stariY + 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY + 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;

			case 1:
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY);
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY + 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX, stariY + 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY + 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;

			case 2:
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY);
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY + 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;
			case 3:

				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY);
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY + 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;

			case 4:
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY - 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;
			case 5:
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY + 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;
			case 6:
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY + 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;

			case 7:
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY - 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY);
				ponistiZauzetostRizicnihNaPoziciji(stariX + 1, stariY + 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX, stariY + 1);
				ponistiZauzetostRizicnihNaPoziciji(stariX - 1, stariY + 1);
				if (!(pomPOsoba.getKuca().getKoordinataX() == stariX && pomPOsoba.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);

				break;

			}
		} else if (ucesnik instanceof BezopasniUcesnik) {
			Dijete pomDijete = null;
			if (ucesnik instanceof Dijete) {
				pomDijete = (Dijete) ucesnik;
				if (!(pomDijete.getKuca().getKoordinataX() == stariX && pomDijete.getKuca().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);
			} else if (ucesnik instanceof AmbulantnoVozilo) {
				AmbulantnoVozilo aVozilo = (AmbulantnoVozilo) ucesnik;
				if (!(aVozilo.getAmbulanta().getKoordinataX() == stariX
						&& aVozilo.getAmbulanta().getKoordinataY() == stariY))
					ponistiZauzetostPoljaBezopasnihNaPoziciji(stariX, stariY);
			}

		}
	}

	public synchronized boolean daLiJeFizickiZauzeto(int x, int y, int smjer, Ucesnik ucesnik) {
		Stanovnik pomStanovnik = null;
		if (ucesnik instanceof Stanovnik) {
			pomStanovnik = (Stanovnik) ucesnik;
		}
		switch (smjer) {
		case 0:
			--y;
			break;
		case 1:
			++x;
			--y;
			break;
		case 2:
			++x;
			break;
		case 3:
			++x;
			++y;
			break;
		case 4:
			++y;
			break;
		case 5:
			--x;
			++y;
			break;
		case 6:
			--x;
			break;
		case 7:
			--x;
			--y;
			break;
		}
		if (x > 0 && x < dimenzijaGrada - 1 && y > 0 && y < dimenzijaGrada - 1) {
			if (mapa[x][y].provjeriZaBezopasne()) {
				if (pomStanovnik.getKuca().getKoordinataX() == x && pomStanovnik.getKuca().getKoordinataY() == y) {
					return false;
				}
			} else
				return false;
		}
		return true;
	}

	public synchronized void iscrtajMatricuGrada() {
		for (int i = 0; i < mapa.length; i++) {
			for (int j = 0; j < mapa[i].length; j++) {
				if (mapa[j][i].provjeriZaBezopasne())
					System.out.print("T ");
				else
					System.out.print("* ");
			}
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------");
	}

	public boolean[] pribaviInfoZaKretanjeFizickih(int x, int y, int smjer, int kucaX, int kucaY) {
		boolean[] info = { true, true, true};
		int[][] pozicija = { { -1, -1 }, { -1, -1 }, { -1, -1 }};
		switch (smjer) {
		case 0:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x + i;
				pozicija[i + 1][1] = y - 1;
			}
			break;
		case 1:
			pozicija[0][0] = x; // najljevlji
			pozicija[0][1] = y - 1;
			pozicija[1][0] = x + 1;
			pozicija[1][1] = y - 1;
			pozicija[2][0] = x + 1; // srednji
			pozicija[2][1] = y;
			break;
		case 2:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x + 1;
				pozicija[i + 1][1] = y + i;
			}
		case 3:
			pozicija[0][0] = x + 1; // najljevlji
			pozicija[0][1] = y;
			pozicija[1][0] = x + 1;
			pozicija[1][1] = y + 1;
			pozicija[2][0] = x; // srednji
			pozicija[2][1] = y + 1;
			break;
		case 4:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x - i;
				pozicija[i + 1][1] = y + 1;
			}
			break;
		case 5:

			pozicija[0][0] = x; // najljevlji
			pozicija[0][1] = y + 1;
			pozicija[1][0] = x - 1;
			pozicija[1][1] = y + 1;
			pozicija[2][0] = x - 1; // srednji
			pozicija[2][1] = y;
			break;
		case 6:
			for (int i = -1; i < 2; i++) {
				pozicija[i + 1][0] = x - 1;
				pozicija[i + 1][1] = y - i;
			}
			break;
		case 7:
			pozicija[0][0] = x - 1; // najljevlji
			pozicija[0][1] = y;
			pozicija[1][0] = x - 1;
			pozicija[1][1] = y - 1;
			pozicija[2][0] = x; // srednji
			pozicija[2][1] = y - 1;
			break;
		}

		for (int i = 0; i < 3; i++) {
			int px = pozicija[i][0];
			int py = pozicija[i][1];
			// System.out.print(px + ", " + py + "|");
			if (px > 0 && px < dimenzijaGrada - 1 && py > 0 && py < dimenzijaGrada - 1) 
				if (kucaX == px && kucaY == py)
					info[i] = false;
				else
						info[i] = mapa[px][py].provjeriZaBezopasne();
		}

		return info;
	}

	public synchronized void oslobodiZaUlazUKucuUOdgovarajucojMatrici(Ucesnik ucesnik, int x, int y) {
		if (Util.daLiJeLegalno(x, y)) {
			if (ucesnik instanceof RizicniUcesnik) {
				for (int i = x - 1; i <= x + 1; i++) {
					for (int j = y - 1; j <= y + 1; j++) {
						if (Util.daLiJeLegalno(i, j))
							mapa[i][j].oslobodiZaPunoljetnu();
					}
				}
				mapa[x][y].oslobodiZaBezopasne();

			} else if (ucesnik instanceof BezopasniUcesnik) {
				if (ucesnik instanceof Dijete) {
					mapa[x][y].oslobodiZaBezopasne();

				} else if (ucesnik instanceof AmbulantnoVozilo) {
					mapa[x][y].oslobodiZaBezopasne();
				}

			}
		}
	}

	public synchronized void zauzmiZaPunkt(int x, int y) {
		mapa[x][y].zauzmiZaPunkt();
	}

	public synchronized void zauzmiZaAmbulantu(int x, int y) {
		mapa[x][y].zauzmiZaAmbulantu();
	}

	public synchronized boolean pribaviInformacijeOPunktovima(int x, int y) {
		if(Util.daLiJeLegalno(x, y))
		return mapa[x][y].provjeriZaPunkt();
		return false;
	}

	public synchronized static boolean pribaviInformacijeOAmbulantama(int x, int y) {
		return mapa[x][y].provjeriZaAmbulantu();
	}

	public synchronized boolean daLiJeZauzeto(int x, int y) {
		return mapa[x][y].provjeriZaBezopasne();
	}

	public boolean daLiJeInficirano(int x, int y) {
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				if(mapa[x+i][y+j].provjeriZaPunoljetne()) return true;
		return false;
	}

	public static int getDimenzijaGrada() {
		return dimenzijaGrada;
	}

	public static void setDimenzijaGrada(int dimenzijaGrada) {
		Grad.dimenzijaGrada = dimenzijaGrada;
	}

	public static int getUkupnaPopulacija() {
		return ukupnaPopulacija;
	}

	public static void setUkupnaPopulacija(int ukupnaPopulacija) {
		Grad.ukupnaPopulacija = ukupnaPopulacija;
	}

	public static int getBrojKuca() {
		return brojKuca;
	}

	public static void setBrojKuca(int brojKuca) {
		Grad.brojKuca = brojKuca;
	}

	public static int getBrojStaraca() {
		return brojStaraca;
	}

	public static void setBrojStaraca(int brojStaraca) {
		Grad.brojStaraca = brojStaraca;
	}

	public static int getBrojOdraslih() {
		return brojOdraslih;
	}

	public static void setBrojOdraslih(int brojOdraslih) {
		Grad.brojOdraslih = brojOdraslih;
	}

	public static int getBrojDjece() {
		return brojDjece;
	}

	public static void setBrojDjece(int brojDjece) {
		Grad.brojDjece = brojDjece;
	}

	public static int getBrojPunktova() {
		return brojPunktova;
	}

	public static void setBrojPunktova(int brojPunktova) {
		Grad.brojPunktova = brojPunktova;
	}

	public static int getBrojOporavljenihLjudi() {
		return brojOporavljenihLjudi;
	}

	public static void setBrojOporavljenihLjudi(int brojOporavljenihLjudi) {
		Grad.brojOporavljenihLjudi = brojOporavljenihLjudi;
	}

	public static int getBrojZarazenihLjudi() {
		return brojZarazenihLjudi;
	}

	public static void setBrojZarazenihLjudi(int brojZarazenihLjudi) {
		Grad.brojZarazenihLjudi = brojZarazenihLjudi;
	}

	public static Polje[][] getMapa() {
		return mapa;
	}

	public static void setMapa(Polje[][] mapa) {
		Grad.mapa = mapa;
	}

	public static ArrayDeque<Stanovnik> getZarazeniNaCekanju() {
		return zarazeniNaCekanju;
	}

	public static void setZarazeniNaCekanju(ArrayDeque<Stanovnik> zarazeniNaCekanju) {
		Grad.zarazeniNaCekanju = zarazeniNaCekanju;
	}

	public static ArrayList<Ambulanta> getAmbulante() {
		return ambulante;
	}

	public static void setAmbulante(ArrayList<Ambulanta> ambulante) {
		Grad.ambulante = ambulante;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		out.writeInt(dimenzijaGrada);
		out.writeInt(ukupnaPopulacija);
		out.writeInt(brojKuca);
		out.writeInt(brojStaraca);
		out.writeInt(brojOdraslih);
		out.writeInt(brojDjece);
		out.writeInt(brojPunktova);
		out.writeInt(brojOporavljenihLjudi);
		out.writeInt(brojZarazenihLjudi);
		out.writeObject(mapa);
		out.writeObject(zarazeniNaCekanju);
		out.writeObject(ambulante);
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		dimenzijaGrada=in.readInt();
		ukupnaPopulacija=in.readInt();
		brojKuca=in.readInt();
		brojStaraca=in.readInt();
		brojOdraslih=in.readInt();
		brojDjece=in.readInt();
		brojPunktova=in.readInt();
		brojOporavljenihLjudi=in.readInt();
		brojZarazenihLjudi=in.readInt();
		mapa=(Polje[][])in.readObject();
		zarazeniNaCekanju=(ArrayDeque<Stanovnik>)in.readObject();
		ambulante=(ArrayList<Ambulanta>)in.readObject();
		
	}
	
	

}
