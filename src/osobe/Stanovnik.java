package osobe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ekran.Ekran;
import filewatcher.Watcher;
import grad.Grad;
import objekti.Ambulanta;
import objekti.KontrolniPunkt;
import objekti.Kuca;

public abstract class Stanovnik extends Thread implements Ucesnik,Externalizable{
	protected Grad grad;
	protected int id;
	protected boolean izasaoIzBolnice;
	protected String ime;
	protected String prezime;
	protected int godinaRodjenja;
	protected int starost;
	protected Pol pol;
	protected int idKuce;
	protected int tjelesnaTemperatura;
	protected int pozicijaX;
	protected int pozicijaY;
	protected int odredisteX;
	protected int odredisteY;
	protected Kuca kuca;
	protected volatile boolean stop;
	protected int smjer;
	protected volatile boolean naOporavku=false;
	protected Color boja = Color.BLACK;
	private volatile boolean uHitnoj = false;
	protected int gornjiLijeviCosakX;
	protected int sirinaKvadrata;
	protected int duzinaKvadrata;
	protected int gornjiLijeviCosakY;
	protected Stack<Integer> tabelaTemperatura;
	protected Ambulanta ambulantaUKojojLezi;
	protected volatile boolean zaustavi=false;
	private boolean karantin=false;
	private static FileHandler filehandler;

	static
	{
	try
	{
		filehandler=new FileHandler(System.getProperty("user.dir")+File.separatorChar+"logger"+File.separatorChar+"Stanovnik.log",true);
		SimpleFormatter format=new SimpleFormatter();
		filehandler.setFormatter(format);
	}
	catch(IOException ex)
	{
		ex.printStackTrace();
	}
	}


	

	public Ambulanta getAmbulantaUKojojLezi() {
		return ambulantaUKojojLezi;
	}

	public void setAmbulantaUKojojLezi(Ambulanta ambulantaUKojojLezi) {
		this.ambulantaUKojojLezi = ambulantaUKojojLezi;
	}

	public Stanovnik() 
	{
	this.id=new Random().nextInt(120);
	this.prezime="Prezime"+id;
	this.ime="Ime"+id;
	Pol polovi[]= {Pol.MUSKI,Pol.ZENSKI};
	this.pol=polovi[new Random().nextInt(2)];
	this.tabelaTemperatura=new Stack<>();
	this.izasaoIzBolnice=false;
	}

	public Stack<Integer> getTabelaTemperatura() {
		return tabelaTemperatura;
	}

	public void setTabelaTemperatura(Stack<Integer> tabelaTemperatura) {
		this.tabelaTemperatura = tabelaTemperatura;
	}

	public Stanovnik(String ime, String prezime, int godinaRodjenja, Pol pol, int idKuce, int id,Grad grad) {
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.godinaRodjenja = godinaRodjenja;
		this.pol = pol;
		this.idKuce = idKuce;
		this.grad=grad;
		this.tjelesnaTemperatura = new Random().nextInt(5) + 35;
		this.stop=false;
		this.naOporavku=false;
		this.tabelaTemperatura=new Stack<>();
		this.izasaoIzBolnice=false;
	}

	public void iscrtaj(Graphics graphic) {
		graphic.setColor(boja);
		graphic.fillOval(5 + pozicijaX * 20, 55 + pozicijaY * 20, 10, 10);
		//graphic.fillRect(5 + odredisteX * 20, 55 + odredisteY * 20, 10, 10);
		double[] pt = {pozicijaX, pozicijaY - 1};
		AffineTransform.getRotateInstance(Math.toRadians(smjer * 45), pozicijaX, pozicijaY)
		  .transform(pt, 0, pt, 0, 1); 
		graphic.drawLine(5 + pozicijaX * 20, 55 + pozicijaY * 20, 5 + (int)pt[0] * 20, 55 + (int)pt[1] * 20);
		
		 graphic.drawString(Integer.toString(tjelesnaTemperatura),5+pozicijaX*20+1,55+pozicijaY*20-1);
	}

	public void postaviNovoOdrediste() 
	{
		Random random = new Random();
		odredisteX = gornjiLijeviCosakX + random.nextInt(sirinaKvadrata);
		odredisteY = gornjiLijeviCosakY + random.nextInt(duzinaKvadrata);
	}

	protected synchronized void odrediSmjer() 
	{
		if(!zaustavi)
		{
		int udaljenostY = this.odredisteY - this.pozicijaY;
		if (udaljenostY != 0)
			udaljenostY /= Math.abs(udaljenostY);
		int udaljenostX = this.odredisteX - this.pozicijaX;
		if (udaljenostX != 0)
			udaljenostX /= Math.abs(udaljenostX);
		switch (3 * udaljenostY + udaljenostX) 
		{
		case -4:
			smjer = 7;
			break;
		case -3:
			smjer = 0;
			break;
		case -2:
			smjer = 1;
			break;
		case -1:
			smjer = 6;
			break;
		case 0:
			postaviNovoOdrediste();
			odrediSmjer();
			break;
		case 1:
			smjer = 2;
			break;
		case 2:
			smjer = 5;
			break;
		case 3:
			smjer = 4;
			break;
		case 4:
			smjer = 3;
			break;
		}
		}
	
	}


protected  abstract  boolean skreni();

	protected void idi() 
	{
		if(!zaustavi)
		{
		int stariX=this.pozicijaX;
		int stariY=this.pozicijaY;
		switch (smjer) {
		case 0:
			--pozicijaY; 	
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,0);
			break;
		case 1:
			++pozicijaX; 
			--pozicijaY;
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,1);
			break;
		case 2:
			++pozicijaX;
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,2);
			break;
		case 3:
			++pozicijaX;
			++pozicijaY;
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,3);
			break;
		case 4:
			++pozicijaY;
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,4);
			break;
		case 5:
			--pozicijaX;
			++pozicijaY;
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,5);
			break;
		case 6:
			--pozicijaX;
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,6);
			break;
		case 7:
			--pozicijaX;
			--pozicijaY;
			this.grad.zauzmiOblastUOdgovarajucojMatrici(this,this.pozicijaX,this.pozicijaY);
			this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this,stariX, stariY,7);
			break;
		}
		}
	};

	public void run() 
	{
		long pocetno=System.currentTimeMillis();
		long period=0;
		boolean vecPostavljenPutZaKuci=false;
		while (!zaustavi)
		{
			System.out.println("radim");
			Ekran.prikazNaTabli(this.trenutniPodaci());
			//Ekran.prikazStatistike(this.trenutniPodaci());
			if(tjelesnaTemperatura>37 && daLiJeUBliziniPunkt() && !zaustavi)
			{
				stop=true;
				pozoviHitnu();
				while(stop && !zaustavi)
				{
					//System.out.println("stop");
					if(uHitnoj) 
					{
					System.out.println("U hitnoj"); 
					}
					try
					{
						Thread.sleep(500);
					}
					catch (InterruptedException ex) 
					{
						log(ex);
					}
				}
			}
			if(this.naOporavku && !zaustavi)
			{
				
				long pocetnoVrijemeOporavka=System.currentTimeMillis();
				while(naOporavku && !zaustavi)
				{
					try
					{
						Ekran.prikazNaTabli(this.trenutniPodaci());
						Thread.sleep(1500);
					}
					catch(InterruptedException ex)
					{
						log(ex);
					}
					if(System.currentTimeMillis()-pocetnoVrijemeOporavka>5000 && !zaustavi)
					{
						System.out.println("promjenio sam temperaturu lijecim se");
						this.setTjelesnaTemperatura(new Random().nextInt(6)+35);
						this.tabelaTemperatura.push(this.tjelesnaTemperatura);
						System.out.println("najnovija temperatura"+this.tjelesnaTemperatura);
						pocetnoVrijemeOporavka=System.currentTimeMillis();
						if(this.tabelaTemperatura.size()>=3)
						{
							int prvaPomT=tabelaTemperatura.get(tabelaTemperatura.size()-1);
							int drugaPomT=tabelaTemperatura.get(tabelaTemperatura.size()-2);
							int trecaPomT=tabelaTemperatura.get(tabelaTemperatura.size()-3);
							double prosjekTemp=(prvaPomT+drugaPomT+trecaPomT)/3.0;
							System.out.println("Prosjek: "+prosjekTemp);
							if(prosjekTemp<37.0 && !zaustavi)
							{
								izadjiIzBolniceIdiKuci();
							}
						}
						
					}
				
				}
			}
			if(kuca.isAlarm() && !vecPostavljenPutZaKuci && !zaustavi)
			{
				odredisteX=kuca.getKoordinataX();
				odredisteY=kuca.getKoordinataY();
				vecPostavljenPutZaKuci=true;
			}
				int pomKX=kuca.getKoordinataX();
				int pomKY=kuca.getKoordinataY();
				if(kuca.isAlarm() && Math.abs(pomKX - pozicijaX) < 2 && Math.abs(pomKY - pozicijaY) < 2 && vecPostavljenPutZaKuci && !zaustavi)
				{
					grad.oslobodiZaUlazUKucuUOdgovarajucojMatrici(this,pozicijaX,pozicijaY);
					kuca.setBrojacUkucana(kuca.getBrojacUkucana()+1);
					if(kuca.isAlarm())
					{
						
						this.pozicijaX=-1;
						this.pozicijaY=-1;
						karantin=true;
						Ekran.prikazNaTabli(trenutniPodaci());
						return;
					}
				}
			
			odrediSmjer();
			period=System.currentTimeMillis()-pocetno;
			if(period>30000 && !zaustavi)
			{
				this.setTjelesnaTemperatura(new Random().nextInt(6)+35);
				pocetno=System.currentTimeMillis();
			}
			while(!skreni() && !zaustavi) 
			{
				period=System.currentTimeMillis()-pocetno;
				if(period>30000)
				{
					this.setTjelesnaTemperatura(new Random().nextInt(6)+35);
					pocetno=System.currentTimeMillis();
				}
				try {
					sleep(new Random().nextInt(20) * 100 +1000);
					} 
				catch (InterruptedException ex) 
					{
					log(ex);
					}
				
			}
			//idi();
			period=System.currentTimeMillis()-pocetno;
			if(period>30000 && !zaustavi)
			{
				this.setTjelesnaTemperatura(new Random().nextInt(6)+35);
				pocetno=System.currentTimeMillis();
			}
			try {
				sleep(2000);
			} catch (InterruptedException ex) {
				log(ex);
			}
			}
		System.out.println(this.ime+"zavrsio sa radom");
		
	}
	
	public boolean isZaustavi() {
		return zaustavi;
	}

	public void setZaustavi(boolean zaustavi) {
		this.zaustavi = zaustavi;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public boolean isNaOporavku() {
		return naOporavku;
	}

	public void setNaOporavku(boolean naOporavku) {
		this.naOporavku = naOporavku;
	}

	public boolean isuHitnoj() {
		return uHitnoj;
	}

	public void setuHitnoj(boolean uHitnoj) {
		this.uHitnoj = uHitnoj;
	}

	public synchronized boolean daLiJeUBliziniPunkt()
	{
		for (int i = pozicijaX - 1; i <= pozicijaX + 1; i++) {
			for (int j = pozicijaY - 1; j <= pozicijaY + 1; j++) {
				if(this.grad.pribaviInformacijeOPunktovima(i,j))
				return true;
			}
		}
		return false;
	}
	
	public synchronized void pozoviHitnu()
	{
		Grad.getZarazeniNaCekanju().add(this);
		Ekran.buttonPosaljiAmbulantnoVozilo.setEnabled(true);
		Ekran.buttonPosaljiAmbulantnoVozilo.setBackground(Color.RED);
	}
	public void izadjiIzBolniceIdiKuci()
	{
		while(true) {
		for (int i = this.ambulantaUKojojLezi.getKoordinataX()- 1; i <= this.ambulantaUKojojLezi.getKoordinataX() + 1; i++)
		{
			for (int j = this.ambulantaUKojojLezi.getKoordinataY() - 1; j <= this.ambulantaUKojojLezi.getKoordinataY() + 1; j++) 
			{
				if(i<Grad.getDimenzijaGrada()-1 && i>0 && j<Grad.getDimenzijaGrada()-1 && j>0 )
				{
					if(!this.grad.daLiJeZauzeto(i,j))
					{
						this.pozicijaX=i;
						this.pozicijaY=j;
						this.kuca.setAlarm(true);
						this.odredisteX=this.getKuca().getKoordinataX();
						this.odredisteY=this.getKuca().getKoordinataY();
						System.out.println("idem kuci "+this.odredisteX+":"+this.odredisteY);
						this.naOporavku=false;
						this.ambulantaUKojojLezi.otpustiPacijenta(this);
						System.out.println("idem kuci dosta je bilo");
						return;
					}
				}
			}
		}
		}
		
		
	}
	public String trenutniPodaci()
	{
		String pravac="";
		switch(this.smjer)
		{
		case 0:
			pravac="gore";
			break;
		case 1:
			pravac="gore-desno";
			break;
		case 2:
			pravac="desno";
			break;
		case 3:
			pravac="dole-desno";
			break;
		case 4:
			pravac="dole";
			break;
		case 5:
			pravac="dole-lijevo";
			break;
		case 6:
			pravac="lijevo";
			break;
		case 7:
			pravac="gore-lijevo";
			break;
		}
		if(naOporavku)
		{
			return "Stanovnik: "+this.ime+"-"+this.id+"na oporavku u bolnici\n";
		}
		if(karantin)
		{
			return "Stanovnik: "+this.ime+"-"+this.id+"u karantinu\n";
		}
		return "Stanovnik: "+this.ime+"-"+this.id+" Smjer: "+pravac+" Trenutna pozicija "+this.pozicijaX+":"+this.pozicijaY+"\n";
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		out.writeObject(grad);
		out.writeInt(id);
		out.writeBoolean(izasaoIzBolnice);
		out.writeObject(ime);
		out.writeObject(prezime);
		out.writeInt(godinaRodjenja);
		out.writeInt(starost);
		out.writeObject(pol);
		out.writeInt(idKuce);
		out.writeInt(tjelesnaTemperatura);
		out.writeInt(pozicijaX);
		out.writeInt(pozicijaY);
		out.writeInt(odredisteX);
		out.writeInt(odredisteY);
		out.writeObject(kuca);
		out.writeBoolean(stop);
		out.writeInt(smjer);
		out.writeBoolean(naOporavku);
		out.writeObject(boja);
		out.writeBoolean(uHitnoj);
		out.writeInt(gornjiLijeviCosakX);
		out.writeInt(gornjiLijeviCosakY);
		out.writeInt(sirinaKvadrata);
		out.writeInt(duzinaKvadrata);
		out.writeObject(tabelaTemperatura);
		out.writeObject(ambulantaUKojojLezi);
		out.writeBoolean(zaustavi);
		out.writeBoolean(karantin);
	
	}

	public boolean isKarantin() {
		return karantin;
	}

	public void setKarantin(boolean karantin) {
		this.karantin = karantin;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
	{
		grad=(Grad)in.readObject();
		id=in.readInt();
		izasaoIzBolnice=in.readBoolean();
		ime=(String)in.readObject();
		prezime=(String)in.readObject();
		godinaRodjenja=in.readInt();
		starost=in.readInt();
		pol=(Pol)in.readObject();
		idKuce=in.readInt();
		tjelesnaTemperatura=in.readInt();
		pozicijaX=in.readInt();
		pozicijaY=in.readInt();
		odredisteX=in.readInt();
		odredisteY=in.readInt();
		kuca=(Kuca)in.readObject();
		stop=in.readBoolean();
		smjer=in.readInt();
		naOporavku=in.readBoolean();
		boja=(Color)in.readObject();
		uHitnoj=in.readBoolean();
		gornjiLijeviCosakX=in.readInt();
		gornjiLijeviCosakY=in.readInt();
		sirinaKvadrata=in.readInt();
		duzinaKvadrata=in.readInt();
		tabelaTemperatura=(Stack<Integer>)in.readObject();
		ambulantaUKojojLezi=(Ambulanta)in.readObject();
		zaustavi=in.readBoolean();
		karantin=in.readBoolean();
	}

	public int getStarost() {
		return starost;
	}

	public void setStarost(int starost) {
		this.starost = starost;
	}

	public int getGornjiLijeviCosakX() {
		return gornjiLijeviCosakX;
	}

	public void setGornjiLijeviCosakX(int gornjiLijeviCosakX) {
		this.gornjiLijeviCosakX = gornjiLijeviCosakX;
	}

	public int getSirinaKvadrata() {
		return sirinaKvadrata;
	}

	public void setSirinaKvadrata(int sirinaKvadrata) {
		this.sirinaKvadrata = sirinaKvadrata;
	}

	public int getDuzinaKvadrata() {
		return duzinaKvadrata;
	}

	public void setDuzinaKvadrata(int duzinaKvadrata) {
		this.duzinaKvadrata = duzinaKvadrata;
	}

	public int getGornjiLijeviCosakY() {
		return gornjiLijeviCosakY;
	}

	public void setGornjiLijeviCosakY(int gornjiLijeviCosakY) {
		this.gornjiLijeviCosakY = gornjiLijeviCosakY;
	}

	public int getIdentifikator() {
		return id;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}

	public int getid() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public int getGodinaRodjenja() {
		return godinaRodjenja;
	}

	public void setGodinaRodjenja(int godinaRodjenja) {
		this.godinaRodjenja = godinaRodjenja;
	}

	public Pol getPol() {
		return pol;
	}

	public void setPol(Pol pol) {
		this.pol = pol;
	}

	public int getIdKuce() {
		return idKuce;
	}

	public void setIdKuce(int idKuce) {
		this.idKuce = idKuce;
	}

	public int getTjelesnaTemperatura() {
		return tjelesnaTemperatura;
	}

	public void setTjelesnaTemperatura(int tjelesnaTemperatura) {
		this.tjelesnaTemperatura = tjelesnaTemperatura;
	}

	public int getPozicijaX() {
		return pozicijaX;
	}

	public void setPozicijaX(int pozicijaX) {
		this.pozicijaX = pozicijaX;
	}

	public int getPozicijaY() {
		return pozicijaY;
	}

	public void setPozicijaY(int pozicijaY) {
		this.pozicijaY = pozicijaY;
	}

	public int getOdredisteX() {
		return odredisteX;
	}

	public void setOdredisteX(int odredisteX) {
		this.odredisteX = odredisteX;
	}

	public int getOdredisteY() {
		return odredisteY;
	}

	public void setOdredisteY(int odredisteY) {
		this.odredisteY = odredisteY;
	}

	public int getSmjer() {
		return smjer;
	}

	public void setSmjer(int smjer) {
		this.smjer = smjer;
	}

	public Color getBoja() {
		return boja;
	}

	public void setBoja(Color boja) {
		this.boja = boja;
	}
	public Kuca getKuca() {
		return kuca;
	}

	public void setKuca(Kuca kuca) {
		this.kuca = kuca;
	}

	public int getID() {
		return id;
	}
	public boolean isIzasaoIzBolnice() {
		return izasaoIzBolnice;
	}

	public void setIzasaoIzBolnice(boolean izasaoIzBolnice) {
		this.izasaoIzBolnice = izasaoIzBolnice;
	}
	public static void log(Exception ex)
	{
		Logger logger=Logger.getLogger(Stanovnik.class.getName());
		logger.addHandler(filehandler);
		logger.log(Level.WARNING,Stanovnik.class.getName(),ex);
	}

}
