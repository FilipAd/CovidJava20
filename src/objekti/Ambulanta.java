package objekti;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Externalizable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ekran.Glavna;
import filewatcher.Watcher;
import grad.Grad;
import osobe.AmbulantnoVozilo;
import osobe.Pol;
import osobe.Stanovnik;
import util.Informacija;

public class Ambulanta implements Externalizable
{
public static int identifikator=1;
private static int brojSaniteta=0;
private static int brojZakapacitet;
private int koordinataX;
private int jedinstveniID;
private int koordinataY;
private int kapacitet;
private int zauzetiKreveti;
private int trenutnoSlobodno;
private Stack<AmbulantnoVozilo> vozila;
private ArrayList<Stanovnik>pacijenti;
private Grad grad;
private static FileHandler filehandler;


static
{
try
{
	filehandler=new FileHandler(System.getProperty("user.dir")+File.separatorChar+"logger"+File.separatorChar+"Ambulanta.log",true);
	SimpleFormatter format=new SimpleFormatter();
	filehandler.setFormatter(format);
}
catch(IOException ex)
{
	ex.printStackTrace();
}
}


public Ambulanta()
{
}

public Ambulanta(int x,int y,Grad grad)
{
	jedinstveniID=identifikator++;
	pacijenti=new ArrayList<>();
	vozila=new Stack<>();
	this.koordinataX=x;
	this.koordinataY=y;
	this.grad=grad;
	this.zauzetiKreveti = 0;
	this.kapacitet=brojZakapacitet+1;
	this.trenutnoSlobodno=kapacitet-zauzetiKreveti;
	this.grad.zauzmiZaAmbulantu(x, y);
	AmbulantnoVozilo vozilo = null;
	for(int i = 0; i < brojSaniteta; i++) {
		vozilo = new AmbulantnoVozilo(-1,-1, this, grad);
		vozila.push(vozilo);
		Glavna.vozila.add(vozilo);
	}

	
}

public synchronized boolean imaLiSlobodnihVozila()
{
	return !vozila.isEmpty();
}

public synchronized boolean imaLiSlobodnihKreveta()
{
	return zauzetiKreveti<kapacitet;
}


public int getKoordinataX() {
	return koordinataX;
}

public void setKoordinataX(int koordinataX) {
	this.koordinataX = koordinataX;
}

public int getKoordinataY() {
	return koordinataY;
}

public void setKoordinataY(int koordinataY) {
	this.koordinataY = koordinataY;
}

public int getKapacitet() {
	return kapacitet;
}

public void setKapacitet(int kapacitet) {
	this.kapacitet = kapacitet;
}

public Stack<AmbulantnoVozilo> getVozila() {
	return vozila;
}

public void setVozila(Stack<AmbulantnoVozilo> vozila) {
	this.vozila = vozila;
}

public ArrayList<Stanovnik> getPacijenti() {
	return pacijenti;
}

public void setPacijenti(ArrayList<Stanovnik> pacijenti) {
	this.pacijenti = pacijenti;
}

public Grad getGrad() {
	return grad;
}

public void setGrad(Grad grad) {
	this.grad = grad;
}

public synchronized void iscrtaj(Graphics graphic)
{
		graphic.setColor(Color.RED);
		graphic.fillRect(koordinataX*20, 50+koordinataY*20,20,20);
		graphic.setColor(Color.WHITE);
		graphic.drawLine(koordinataX*20,65+koordinataY*20,koordinataX*20+20,65+koordinataY*20);
		graphic.drawLine(koordinataX*20+10,50+koordinataY*20,koordinataX*20+10,70+koordinataY*20);
		graphic.drawString(Integer.toString(trenutnoSlobodno),3+koordinataX*20,55+koordinataY*20+5);
}

public void zauzmiKrevet() 
{
		++zauzetiKreveti;
	
}
public void smanjiKapacitet()
{
	--kapacitet;
}
public void povecajKapacitet()
{
	++kapacitet;
}
public void oslobodiKrevet()
{
	--zauzetiKreveti;
}

public static int getIdentifikator() {
	return identifikator;
}

public static void setIdentifikator(int identifikator) {
	Ambulanta.identifikator = identifikator;
}

public int getJedinstveniID() {
	return jedinstveniID;
}

public void setJedinstveniID(int jedinstveniID) {
	this.jedinstveniID = jedinstveniID;
}

public int getZauzetiKreveti() {
	return zauzetiKreveti;
}

public void setZauzetiKreveti(int zauzetiKreveti) {
	this.zauzetiKreveti = zauzetiKreveti;
}

public int getTrenutnoSlobodno() {
	return trenutnoSlobodno;
}
public void setTrenutnoSlobodno(int trenutnoSlobodno) {
	this.trenutnoSlobodno = trenutnoSlobodno;
}
public static void setBrojSaniteta(int brojSaniteta) {
	Ambulanta.brojSaniteta = brojSaniteta;
}
public static int getBrojSaniteta() {
	return brojSaniteta;
}

public static int getBrojZakapacitet() {
	return brojZakapacitet;
}

public static void setBrojZakapacitet(int brojZakapacitet) {
	Ambulanta.brojZakapacitet = brojZakapacitet;
}

public synchronized void posaljiVozilo(Stanovnik zarazen) {
	zauzmiKrevet();
	trenutnoSlobodno=kapacitet-zauzetiKreveti;
	AmbulantnoVozilo vozilo = vozila.pop();
	vozilo.javiKoordinatePacijenta(zarazen);
	vozilo.promijeniAkciju();
	if(!vozilo.isAlive()) vozilo.start();
	else 
		synchronized(vozilo) {
			vozilo.notify(); 
		}
	
}

public void preuzmiPacijenta(AmbulantnoVozilo vozilo) 
{
	
	vozilo.promijeniAkciju();
	vozilo.getPacijent().setNaOporavku(true);
	vozilo.getPacijent().setStop(false);
	vozilo.getPacijent().setuHitnoj(false);
	vozilo.getPacijent().setAmbulantaUKojojLezi(this);
	pacijenti.add(vozilo.getPacijent());
	vozila.push(vozilo);
	Grad.povecajBrojZarazenih();
	Informacija.azurirajInformacijePriUlasku(vozilo.getPacijent());
	zapisiURegistar();
}
public void otpustiPacijenta(Stanovnik pacijent)
{
	oslobodiKrevet();
	trenutnoSlobodno=kapacitet-zauzetiKreveti;
	pacijent.setIzasaoIzBolnice(true);
	pacijent.getTabelaTemperatura().clear();
	this.pacijenti.remove(pacijent);
	Grad.smanjiBrojZarazenih();
	Grad.povecajBrojOporavljenih();
	Informacija.azurirajInformacijePriIzlasku(pacijent);
	zapisiURegistar();
	
}
public void zapisiURegistar()
{
	File ff = new File(System.getProperty("user.dir")+File.separatorChar+"src"+File.separatorChar+"podaci"+File.separatorChar+"BrZarazeni.txt");
	synchronized(ff) {
	try
	{
	PrintWriter pisac=new PrintWriter(new FileWriter(ff));
	pisac.println(Grad.getBrojZarazenihLjudi()+":"+Grad.getBrojOporavljenihLjudi());
	pisac.close();
	}
	catch(IOException ex)
	{
		log(ex);
	}
	}
	
}
@Override
public void writeExternal(ObjectOutput out) throws IOException 
{
	out.writeInt(identifikator);
	out.writeInt(brojSaniteta);
	out.writeInt(koordinataX);
	out.writeInt(koordinataY);
	out.writeInt(jedinstveniID);
	out.writeInt(kapacitet);
	out.writeInt(zauzetiKreveti);
	out.writeObject(vozila);
	out.writeObject(pacijenti);
	out.writeInt(trenutnoSlobodno);
	out.writeInt(brojZakapacitet);
	out.writeObject(grad);
}

@Override
public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
{	
	identifikator=in.readInt();
	brojSaniteta=in.readInt();
	koordinataX=in.readInt();
	koordinataY=in.readInt();
	jedinstveniID=in.readInt();
	kapacitet=in.readInt();
	zauzetiKreveti=in.readInt();
	vozila=(Stack<AmbulantnoVozilo>)in.readObject();
	pacijenti=(ArrayList<Stanovnik>)in.readObject();
	trenutnoSlobodno=in.readInt();
	brojZakapacitet=in.readInt();
	grad=(Grad)in.readObject();
	
}
public static void log(Exception ex)
{
	Logger logger=Logger.getLogger(Ambulanta.class.getName());
	logger.addHandler(filehandler);
	logger.log(Level.WARNING,Ambulanta.class.getName(),ex);
}
}
