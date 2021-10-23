package ekran;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import filewatcher.Watcher;
import grad.Grad;
import objekti.Ambulanta;
import objekti.KontrolniPunkt;
import objekti.Kuca;
import osobe.AmbulantnoVozilo;
import osobe.Dijete;
import osobe.OdraslaOsoba;
import osobe.Stanovnik;
import osobe.StaraOsoba;



public class Glavna extends Thread implements Externalizable
{
	private Ekran ekran;
	private static long pocetnoVrijeme=0;
	private static BufferStrategy buffer;
	private static Graphics graphic;
	private static int brojukupnePopulacije;
	private static int brojDjece=0;
	private static int brojOdrslih=0;
	private static int brojStaraca=0;
	private static int brojKuca=0;
	private static int brojPunktova=0;
	private static int brojAmbulantnihVozila=0;
	private static int brojAmbulanti;
	public static ArrayList <Stanovnik> stanovnici = new ArrayList<>();
	public static ArrayList <Kuca> kuce = new ArrayList<>();
	public static ArrayList <Ambulanta> ambulante = new ArrayList<>();
	public static ArrayList <AmbulantnoVozilo> vozila= new ArrayList<>();
	public static ArrayList <KontrolniPunkt> punktovi= new ArrayList<>();
	public static Grad grad;
	public volatile static boolean crtaj=false;
	private Watcher watcher;
	
	public Glavna()
	{
		
	}
	public Glavna(String naslov,int sirina,int visina,int format)
	{
		ekran=new Ekran(naslov,sirina,visina,format);
		ekran.glavna=this; //provjeri
		this.watcher=new Watcher(System.getProperty("user.dir")+File.separatorChar+"src"+File.separatorChar+"podaci");
		watcher.start();
	}
	
	public void iscrtaj()
	{
		
		buffer=ekran.getCanvas().getBufferStrategy();
		if(buffer==null)
		{
			ekran.getCanvas().createBufferStrategy(3);
			return;
		}
		int velicina=20*ekran.getFormat();
		int velicinaCelije=20;
		graphic=buffer.getDrawGraphics();
		graphic.clearRect(0,0,velicina,velicina);
		graphic.setColor(Color.WHITE);
		graphic.fillRect(0,50,velicina,velicina);
		
		int k;
	    int sirina =velicina;
	    int visina =velicina;
	    graphic.setColor(Color.black);
	    int htOfRow = velicinaCelije;
	    for (k = 0; k < ekran.getFormat(); k++) //horizontalne
	    {
	    	graphic.drawLine(0, (k * htOfRow)+50, sirina, (k * htOfRow)+50);
	    }
	    int wdOfRow = velicinaCelije; //vertikalne
	    for (k = 0; k < ekran.getFormat(); k++) 
	    {
	    	graphic.drawLine(k * wdOfRow,0, k * wdOfRow, visina+50);   
	    	
	    }
	    
	    
	    //graphic.setColor(Color.red);
	    for(Stanovnik stanovnik: stanovnici)
	    {
	    	if(!stanovnik.isNaOporavku() && !stanovnik.isuHitnoj())
	    	stanovnik.iscrtaj(graphic);
	    }
	    for(Kuca kuca: kuce)
	    {
	    	kuca.iscrataj(graphic);
	    }
	    for(Ambulanta ambulanta: ambulante)
	    {
			ambulanta.iscrtaj(graphic);
		}
	    for(AmbulantnoVozilo vozilo: vozila)
	    {
			if (vozilo.isAkcija())
				vozilo.iscrtaj(graphic);
		}
	    for(KontrolniPunkt punkt: punktovi)
	    {
	    	punkt.iscrtaj(graphic);
	    }
	    
	    
		buffer.show();
		graphic.dispose();
		}
	public void run()
	{
		while(true)
		{
			if(crtaj) {
				//System.out.println("crtaj");
			iscrtaj();}
			//else 
				//System.out.println("ne crtaj");
			
		}
			
	}
	public static void main(String args []) 
	{
		Glavna g=new Glavna("APLIKACIJA",500,500,Grad.getDimenzijaGrada());
		grad=new Grad(Grad.getDimenzijaGrada());
	    g.start();
	}

	public Ekran getEkran() {
		return ekran;
	}

	public void setEkran(Ekran ekran) {
		this.ekran = ekran;
	}

	public static BufferStrategy getBuffer() {
		return buffer;
	}

	public static void setBuffer(BufferStrategy buffer) {
		Glavna.buffer = buffer;
	}

	public static Graphics getGraphic() {
		return graphic;
	}

	public static void setGraphic(Graphics graphic) {
		Glavna.graphic = graphic;
	}

	public static int getBrojDjece() {
		return brojDjece;
	}

	public static void setBrojDjece(int brojDjece) {
		Glavna.brojDjece = brojDjece;
	}

	public static int getBrojOdrslih() {
		return brojOdrslih;
	}

	public static void setBrojOdrslih(int brojOdrslih) {
		Glavna.brojOdrslih = brojOdrslih;
	}

	public static int getBrojStaraca() {
		return brojStaraca;
	}

	public static void setBrojStaraca(int brojStaraca) {
		Glavna.brojStaraca = brojStaraca;
	}

	public static int getBrojKuca() {
		return brojKuca;
	}

	public static int getBrojAmbulanti() {
		return brojAmbulanti;
	}
	public static void setBrojAmbulanti(int brojAmbulanti) {
		Glavna.brojAmbulanti = brojAmbulanti;
	}
	public static void setBrojKuca(int brojKuca) {
		Glavna.brojKuca = brojKuca;
	}

	public static int getBrojPunktova() {
		return brojPunktova;
	}

	public static void setBrojPunktova(int brojPunktova) {
		Glavna.brojPunktova = brojPunktova;
	}

	public static int getBrojAmbulantnihVozila() {
		return brojAmbulantnihVozila;
	}

	public static void setBrojAmbulantnihVozila(int brojAmbulantnihVozila) {
		Glavna.brojAmbulantnihVozila = brojAmbulantnihVozila;
	}

	public static ArrayList<Stanovnik> getStanovnici() {
		return stanovnici;
	}

	public static void setStanovnici(ArrayList<Stanovnik> stanovnici) {
		Glavna.stanovnici = stanovnici;
	}

	public static ArrayList<Kuca> getKuce() {
		return kuce;
	}

	public static void setKuce(ArrayList<Kuca> kuce) {
		Glavna.kuce = kuce;
	}

	public static ArrayList<Ambulanta> getAmbulante() {
		return ambulante;
	}

	public static void setAmbulante(ArrayList<Ambulanta> ambulante) {
		Glavna.ambulante = ambulante;
	}

	public static ArrayList<AmbulantnoVozilo> getVozila() {
		return vozila;
	}

	public static void setVozila(ArrayList<AmbulantnoVozilo> vozila) {
		Glavna.vozila = vozila;
	}

	public static ArrayList<KontrolniPunkt> getPunktovi() {
		return punktovi;
	}

	public static void setPunktovi(ArrayList<KontrolniPunkt> punktovi) {
		Glavna.punktovi = punktovi;
	}

	public static Grad getGrad() {
		return grad;
	}

	public static void setGrad(Grad grad) {
		Glavna.grad = grad;
	}

	public static int getBrojukupnePopulacije() {
		return brojukupnePopulacije;
	}
	public static void setBrojukupnePopulacije(int brojukupnePopulacije) {
		Glavna.brojukupnePopulacije = brojukupnePopulacije;
	}
	public static boolean isCrtaj() {
		return crtaj;
	}

	public static void setCrtaj(boolean crtaj) {
		Glavna.crtaj = crtaj;
	}

	public Watcher getWatcher() {
		return watcher;
	}

	public void setWatcher(Watcher watcher) {
		this.watcher = watcher;
	}
	public static long getPocetnoVrijeme() {
		return pocetnoVrijeme;
	}
	public static void setPocetnoVrijeme(long pocetnoVrijeme)
	{
		Glavna.pocetnoVrijeme=pocetnoVrijeme;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		out.writeLong(pocetnoVrijeme);
		out.writeObject(grad);
		out.writeObject(kuce);
		out.writeObject(ambulante);
		out.writeObject(vozila);
		out.writeObject(punktovi);
		out.writeObject(stanovnici);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
	{
		pocetnoVrijeme=in.readLong();
		grad=(Grad)in.readObject();
		kuce=(ArrayList<Kuca>)in.readObject();
		ambulante=(ArrayList<Ambulanta>)in.readObject();
		vozila=(ArrayList<AmbulantnoVozilo>)in.readObject();
		punktovi=(ArrayList<KontrolniPunkt>)in.readObject();
		stanovnici=(ArrayList<Stanovnik>)in.readObject();
		
	}
	}

