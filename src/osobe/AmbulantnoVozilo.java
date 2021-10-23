package osobe;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Random;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ekran.Ekran;
import grad.Grad;
import objekti.Ambulanta;
import objekti.Kuca;
import util.Util;

public class AmbulantnoVozilo extends Thread implements BezopasniUcesnik,Externalizable 
{
	private int pozicijaX;
	private int pozicijaY;
	private int odredisteX;
	private int odredisteY;
	private Ambulanta ambulanta;
	private int smjer;
	private Stanovnik pacijent;
	private Grad grad;
	private boolean akcija = false;
	private boolean povratak;
	private volatile boolean zaustavi=false;
	private static FileHandler filehandler;


	static
	{
	try
	{
		filehandler=new FileHandler(System.getProperty("user.dir")+File.separatorChar+"logger"+File.separatorChar+"AmbulantnoVozilo.log",true);
		SimpleFormatter format=new SimpleFormatter();
		filehandler.setFormatter(format);
	}
	catch(IOException ex)
	{
		ex.printStackTrace();
	}
	}

	
	
	public void promijeniAkciju() {
		akcija = !akcija;
	}
	public boolean isAkcija() {
		return akcija;
	}

	public void setAkcija(boolean akcija) {
		this.akcija = akcija;
	}

	public AmbulantnoVozilo()
	{
		
	}
	
	public AmbulantnoVozilo(int pozicijaX,int pozicijaY,Ambulanta ambulanta, Grad grad)
	{
		this.pozicijaX=pozicijaX;
		this.pozicijaY=pozicijaY;
		this.ambulanta=ambulanta;
		this.grad=grad;
	}
	

	public void javiKoordinatePacijenta(Stanovnik pacijent)
	{
		this.pozicijaX=ambulanta.getKoordinataX();
		this.pozicijaY=ambulanta.getKoordinataY();
		odredisteX=pacijent.pozicijaX;
		odredisteY=pacijent.pozicijaY;
		this.setPacijent(pacijent);
	}
	
	protected boolean skreni()
	{
		boolean[] fizInfo = { true, true, true };
		fizInfo = grad.pribaviInfoZaKretanjeFizickih(pozicijaX, pozicijaY, smjer,-1,-1);
		if(pozicijaX != -2)
		System.out.println(pozicijaX + ", " + pozicijaY + "->" + smjer + "{" + fizInfo[0] + fizInfo[1] + fizInfo[2]);
			if (fizInfo[1]) {
				//System.out.println("[1] zauzeto");
				if (!fizInfo[2])
					smjer = (smjer + 1) % 8;
				else if (!fizInfo[0])
					smjer = (smjer + 7) % 8;
				else {
					smjer = (smjer + 2) % 8;
					return false;
			}
			
	}
			return true;
	}
	
	public void postaviNovoOdrediste() 
	{
		povratak = true;
		int x, y;
		for (int i = -1; i < 2; i++)
			for (int j = -1; j < 2; j++) {
				x = ambulanta.getKoordinataX() + i;
				y = ambulanta.getKoordinataY() + j;
				if (x > 0 && x < Grad.getDimenzijaGrada() - 1 && y > 0 && y < Grad.getDimenzijaGrada() - 1
						&& !grad.daLiJeZauzeto(x, y)) {
					odredisteX = ambulanta.getKoordinataX() + i;
					odredisteY = ambulanta.getKoordinataY() + j;
					System.out.println(":" + odredisteX + ", " + odredisteY);
					return;
				}
			}
		odredisteX = ambulanta.getKoordinataX();
		odredisteY = ambulanta.getKoordinataY();
		System.out.println(":" + odredisteX + ", " + odredisteY);
		

	}

	
	protected void odrediSmjer() {
		int udaljenostX=0, udaljenostY=0;
		if (povratak) {
			udaljenostY = this.odredisteY - this.pozicijaY;
			udaljenostX = this.odredisteX - this.pozicijaX;
			if (Math.abs(udaljenostX) < 2 && Math.abs(udaljenostY) < 2) {
				povratak = false;
				grad.oslobodiZaUlazUKucuUOdgovarajucojMatrici(this,this.getPozicijaX(),this.getPozicijaY());
				this.setPozicijaX(-2);
				this.setPozicijaY(-2);
				this.ambulanta.preuzmiPacijenta(this);
			}
		}

		else

		{
			udaljenostY = this.odredisteY - this.pozicijaY;
			udaljenostX = this.odredisteX - this.pozicijaX;
			if (Math.abs(udaljenostX) < 2 && Math.abs(udaljenostY) < 2) {
				postaviNovoOdrediste();
				grad.oslobodiZaUlazUKucuUOdgovarajucojMatrici(this.pacijent, this.pacijent.getPozicijaX(), this.pacijent.getPozicijaY());
				this.pacijent.setBoja(Color.BLACK);
				this.pacijent.setPozicijaX(-2);
				this.pacijent.setPozicijaY(-2);

			}
		}
		if (udaljenostY != 0)
			udaljenostY /= Math.abs(udaljenostY);
		if (udaljenostX != 0)
			udaljenostX /= Math.abs(udaljenostX);
		switch (3 * udaljenostY + udaljenostX) {
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
			smjer = 9;
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
	
	public void run()
	{
		while (!zaustavi) {
			System.out.println("run");
			//System.out.println("Da li je aktivan:"+akcija+"korodinata x:"+this.pozicijaX+":"+this.pozicijaY+"pacijent:"+this.pacijent);
			if (akcija) {
				System.out.println("akcija");
				odrediSmjer();
				while (!skreni() && !zaustavi) {
					try {
						sleep(new Random().nextInt(20) * 100 + 3000);
					} catch (InterruptedException ex) {
						log(ex);
					}

				}
				idi();
				try {
					sleep(2000);
				} catch (InterruptedException ex) {
						log(ex);
				}
			} else {
				try {
					synchronized(this) {
					    while (!akcija) { this.wait(); 
						System.out.println("!!!!!!!! ne spavam!!!!!!!!!!!!!");}
					}
				} catch (InterruptedException ex) {
					log(ex);
				}
			}
		}

	}

	protected void idi() {
		if(!zaustavi)
		{
		int pomK[] = Util.pomjeri(pozicijaX, pozicijaY, smjer);
		this.grad.zauzmiOblastUOdgovarajucojMatrici(this, pomK[0], pomK[1]);
		this.grad.oslobodiPrethodnuPozicijuUOdgovarajucojMatrici(this, pozicijaX, pozicijaY, smjer);
		pozicijaX = pomK[0];
		pozicijaY = pomK[1];
		}
	}
	
	public synchronized void iscrtaj(Graphics graphic)
	{
			graphic.setColor(Color.BLUE);
			graphic.fillOval(pozicijaX*20,50+pozicijaY*20,20,20);
			graphic.setColor(Color.WHITE);
		//	graphic.drawString(this.pozicijaX + ":" + this.pozicijaY,5+pozicijaX*20+2,55+pozicijaY*20-7);
			graphic.drawLine(pozicijaX*20,60+pozicijaY*20,pozicijaX*20+20,60+pozicijaY*20);
			graphic.drawLine(pozicijaX*20+10,50+pozicijaY*20,pozicijaX*20+10,70+pozicijaY*20);
			graphic.setColor(Color.RED);
			graphic.fillOval(5+odredisteX*20,55+odredisteY*20,10,10);
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
	public Ambulanta getAmbulanta() {
		return ambulanta;
	}
	public void setAmbulanta(Ambulanta ambulanta) {
		this.ambulanta = ambulanta;
	}
	public Stanovnik getPacijent() {
		return pacijent;
	}
	public void setPacijent(Stanovnik pacijent) {
		this.pacijent = pacijent;
	}
	public boolean isZaustavi() {
		return zaustavi;
	}
	public void setZaustavi(boolean zaustavi) {
		this.zaustavi = zaustavi;
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		out.writeObject(grad);
		out.writeInt(pozicijaX);
		out.writeInt(pozicijaY);
		out.writeInt(odredisteX);
		out.writeInt(odredisteY);
		out.writeObject(ambulanta);
		out.writeInt(smjer);
		out.writeObject(pacijent);
		out.writeBoolean(akcija);
		out.writeBoolean(povratak);
		out.writeBoolean(zaustavi);

		
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
	{
		grad=(Grad)in.readObject();
		pozicijaX=in.readInt();
		pozicijaY=in.readInt();
		odredisteX=in.readInt();
		odredisteY=in.readInt();
		ambulanta=(Ambulanta)in.readObject();
		smjer=in.readInt();
		pacijent=(Stanovnik)in.readObject();
		akcija=in.readBoolean();
		povratak=in.readBoolean();
		zaustavi=in.readBoolean();
		
		
	}
	public static void log(Exception ex)
	{
		Logger logger=Logger.getLogger(AmbulantnoVozilo.class.getName());
		logger.addHandler(filehandler);
		logger.log(Level.WARNING,AmbulantnoVozilo.class.getName(),ex);
	}
	
	
}
