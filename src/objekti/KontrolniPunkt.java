package objekti;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

import grad.Grad;

public class KontrolniPunkt implements Serializable
{
	private int koordinataX;
	private int koordinataY;
	private Color boja;
	private Grad grad;

public KontrolniPunkt()
{
	this.koordinataX=new Random().nextInt(Grad.getDimenzijaGrada()-1);
	this.koordinataY=new Random().nextInt(Grad.getDimenzijaGrada()-1);
	this.boja=Color.BLACK;
	this.grad.zauzmiZaPunkt(koordinataX, koordinataY);
	
}

public KontrolniPunkt(int koordinataX,int koordinataY,Grad grad)
{
	this.koordinataX=koordinataX;
	this.koordinataY=koordinataY;
	this.grad=grad;
	this.boja=Color.BLACK;
	this.grad.zauzmiZaPunkt(koordinataX, koordinataY);
}

public void iscrtaj(Graphics graphic)
{
	
		graphic.setColor(boja);
		graphic.fillRect(koordinataX*20, 50+koordinataY*20,20,20);
	
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



}
