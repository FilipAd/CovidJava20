package objekti;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import grad.Grad;
import osobe.PunoljetnaOsoba;
import osobe.Stanovnik;
import osobe.StaraOsoba;

public class Kuca implements Serializable
{
	private boolean alarm;
	private int koordinataX;
	private int koordinataY;
	private boolean zabranaNaSnazi=false;
	private int identifikator;
	private int brojacUkucana;
	private ArrayList<Stanovnik> ukucani;
	private Color boja;
	private Grad grad;
	private boolean imaPunoljetnih = false;
	


public Kuca()
{
	ukucani=new ArrayList<>();
	identifikator=new Random().nextInt(1000);
	brojacUkucana=0;
}

public Kuca(int identifikator, int brojacUkucana,ArrayList<Stanovnik>ukucani)
{
	this.identifikator=identifikator;
	this.brojacUkucana=brojacUkucana;
	this.ukucani=ukucani;
}

public Kuca(int koordinataX,int koordinataY,Grad grad)
{
	ukucani=new ArrayList<>();
	identifikator=new Random().nextInt(1000);
	brojacUkucana=0;
	this.koordinataX=koordinataX;
	this.koordinataY=koordinataY;
	Grad.mapa[koordinataX][koordinataY].zauzmiBezopasnoj();
}
public void iscrataj(Graphics graphic)
{
		graphic.setColor(boja);
		graphic.fillRect(koordinataX*20, 50+koordinataY*20,20,20);
		graphic.drawString(Integer.toString(this.brojacUkucana),5+koordinataX*20+2,55+koordinataY*20-7);
}

public Color getBoja() {
	return boja;
}

public void setBoja(Color boja) {
	this.boja = boja;
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

public boolean isZabranaNaSnazi() {
	return zabranaNaSnazi;
}

public void setZabranaNaSnazi(boolean zabranaNaSnazi) {
	this.zabranaNaSnazi = zabranaNaSnazi;
}

public int getIdentifikator() {
	return identifikator;
}

public void setIdentifikator(int identifikator) {
	this.identifikator = identifikator;
}

public int getBrojacUkucana() {
	return brojacUkucana;
}

public void setBrojacUkucana(int brojacUkucana) {
	this.brojacUkucana = brojacUkucana;
}

public ArrayList<Stanovnik> getUkucani() {
	return ukucani;
}

public void setUkucani(ArrayList<Stanovnik> ukucani) {
	this.ukucani = ukucani;
}
public boolean isAlarm()
{
	return alarm;
}

public void setAlarm(boolean alarm) {
	this.alarm = alarm;
}
public Grad getGrad() {
	return grad;
}

public void setGrad(Grad grad) {
	this.grad = grad;
}

public void dodajUkucana(Stanovnik stanovnik) {
	ukucani.add(stanovnik);
	if(stanovnik instanceof PunoljetnaOsoba)
	imaPunoljetnih  = true;;
	
}

public boolean imaPunoljetnih() {
	return imaPunoljetnih;
}
public String toString()
{
	return "Kuca: "+this.koordinataX+":"+koordinataY;
}

}
