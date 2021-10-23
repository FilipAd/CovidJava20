package ekran;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


import grad.Grad;
import objekti.Ambulanta;
import objekti.KontrolniPunkt;
import objekti.Kuca;
import osobe.AmbulantnoVozilo;
import osobe.Dijete;
import osobe.OdraslaOsoba;
import osobe.Stanovnik;
import osobe.StaraOsoba;
import util.Informacija;


public class Ekran 
{
public Glavna glavna;
private JFrame frame;
private JFrame framePocetni;
private JFrame frameStanjeAmbulanti;
private JFrame frameStatistickiPodaci;
private String naslov;
private int sirina;
private int visina;
private Canvas canvas;
private JLabel labela;
private JButton dugme;
private JScrollPane scroll;
private JScrollPane scroll2;
private JScrollPane scroll3;
private JTextArea tekstualno;
private JButton buttonStart;
private JButton buttonOmoguciKretanje;
private JButton buttonDodajAmbulantu;
private JButton buttonSnimiStatistiku;
private JLabel brojZ;
private JLabel brojO;
private static JTextArea tekstPolje;
private static JTextArea tekstStanjaAmbulanti;
private static JTextArea tekstStatistika;
private static JTextField brojZarazenihStanovnika;
private static JTextField brojOporavljenihStanovnika;
public static JButton buttonPosaljiAmbulantnoVozilo;
private static BufferStrategy buffer;
private static Graphics graphic;
private int format;
private static FileHandler filehandler;

static
{
try
{
	filehandler=new FileHandler(System.getProperty("user.dir")+File.separatorChar+"logger"+File.separatorChar+"Ekran.log",true);
	SimpleFormatter format=new SimpleFormatter();
	filehandler.setFormatter(format);
}
catch(IOException ex)
{
	ex.printStackTrace();
}
}

public Ekran(String naslov,int sirina,int visina,int format)
{
	this.naslov=naslov;
	this.sirina=sirina;
	this.visina=visina;
	this.format=format;
	
	framePocetni=new JFrame("UNOS"); //postavljamo naslov na naslovnu liniju
	framePocetni.setSize(new Dimension(900,600));
	framePocetni.setResizable(true);
	framePocetni.setLocationRelativeTo(null);  //postavljanje na centar ekrana
	framePocetni.getContentPane().setBackground(new Color(20,200,220));
	framePocetni.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	buttonStart=new JButton();
	buttonStart.setText("START");
	buttonStart.setBounds(70,400,200,50);
	framePocetni.add(buttonStart);
	
	JTextField brojDjeceTekst=new JTextField();
	brojDjeceTekst.setBounds(250,80,50,20);
	framePocetni.getContentPane().add(brojDjeceTekst);
	brojDjeceTekst.setColumns(10);
	
	JTextField brojOdraslihTekst = new JTextField();
	brojOdraslihTekst.setBounds(250,120,50,20);
	framePocetni.getContentPane().add(brojOdraslihTekst);
	brojOdraslihTekst.setColumns(10);
	
	JTextField brojStarihTekst = new JTextField();
	brojStarihTekst.setBounds(250,160,50,20);
	framePocetni.getContentPane().add(brojStarihTekst);
	brojStarihTekst.setColumns(10);
	
	JTextField brojKuca = new JTextField();
	brojKuca.setBounds(250,200,50,20);
	framePocetni.getContentPane().add(brojKuca);
	brojKuca.setColumns(10);
	
	JTextField brojKontrolnihPunktova = new JTextField();
	brojKontrolnihPunktova.setBounds(250,240,50,20);
	framePocetni.getContentPane().add(brojKontrolnihPunktova);
	brojKontrolnihPunktova.setColumns(10);
	
	JTextField brojAmbulantnihVozila = new JTextField();
	brojAmbulantnihVozila.setBounds(250,280,50,20);
	framePocetni.getContentPane().add(brojAmbulantnihVozila);
	brojAmbulantnihVozila.setColumns(10);
	
	JLabel labelBrojDjece = new JLabel("BROJ DJECE:");
	labelBrojDjece.setBounds(50,80,130, 20);
	framePocetni.getContentPane().add(labelBrojDjece);
	
	
	JLabel labelBrojOdraslih = new JLabel("BROJ ODRASLIH:");
	labelBrojOdraslih.setBounds(50,120,130,20);
	framePocetni.getContentPane().add(labelBrojOdraslih);
	
	JLabel labelBrojStarih = new JLabel("BROJ STARIH:");
	labelBrojStarih.setBounds(50,160,130,20);
	framePocetni.getContentPane().add(labelBrojStarih);
	
	JLabel labelBrojKuca= new JLabel("BROJ KUCA:");
	labelBrojKuca.setBounds(50,200,180,20);
	framePocetni.getContentPane().add(labelBrojKuca);
	
	JLabel labelBrojKontrolnihPunktova = new JLabel("BROJ KONTROLNIH PUNKTOVA:");
	labelBrojKontrolnihPunktova.setBounds(50,240,180,20);
	framePocetni.getContentPane().add(labelBrojKontrolnihPunktova);
	
	JLabel labelBrojAVozila = new JLabel("BROJ AMBULANTNIH VOZILA:");
	labelBrojAVozila.setBounds(50,280,180,20);
	framePocetni.getContentPane().add(labelBrojAVozila);
	
	ImageIcon slika=new ImageIcon(System.getProperty("user.dir")+File.separatorChar+"slike"+File.separatorChar+"slika1.png");
	JLabel labelSlika=new JLabel(slika);
	labelSlika.setBounds(350,80,450,450);
	framePocetni.getContentPane().add(labelSlika);
	
	JLabel labelBrojpom = new JLabel();
	labelBrojpom.setBounds(20,250,130,20);
	framePocetni.getContentPane().add(labelBrojpom);
	
	
	
	
	
	
	
	frame=new JFrame(naslov); //postavljamo naslov na naslovnu liniju
	frame.setSize(new Dimension(sirina,visina));
	frame.setResizable(true);
	frame.setLocationRelativeTo(null);  //postavljanje na centar ekrana
	frame.getContentPane().setBackground(Color.BLACK);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pri zatvaranju prozora da se zatvori a ne minimizuje
	

	
	buttonOmoguciKretanje=new JButton();
	buttonOmoguciKretanje.setText("OMOGUCI KRETANJE");
	buttonOmoguciKretanje.setSize(100,50);
	buttonOmoguciKretanje.setBounds(650,50,320,50);
	buttonOmoguciKretanje.setVisible(true);
	
	buttonPosaljiAmbulantnoVozilo=new JButton();
	buttonPosaljiAmbulantnoVozilo.setText("POSALJI AMBULANTNO VOZILO");
	buttonPosaljiAmbulantnoVozilo.setSize(100,50);
	buttonPosaljiAmbulantnoVozilo.setBounds(650,120,320,50);
	buttonPosaljiAmbulantnoVozilo.setVisible(true);
	
	JButton buttonPregledajStanjeAmbulantni=new JButton();
	buttonPregledajStanjeAmbulantni.setText("POGLEDAJ STANJE AMBULANTI");
	buttonPregledajStanjeAmbulantni.setSize(100,50);
	buttonPregledajStanjeAmbulantni.setBounds(650,190,320,50);
	buttonPregledajStanjeAmbulantni.setVisible(true);
	
	JButton buttonPregledajStatistickePodatke=new JButton();
	buttonPregledajStatistickePodatke.setText("POGLEDAJ STATISTICKE PODATKE");
	buttonPregledajStatistickePodatke.setSize(100,50);
	buttonPregledajStatistickePodatke.setBounds(650,260,320,50);
	buttonPregledajStatistickePodatke.setVisible(true);
	
	JButton buttonZaustaviSimulaciju=new JButton();
	buttonZaustaviSimulaciju.setText("ZAUSTAVI SIMULACIJU");
	buttonZaustaviSimulaciju.setSize(100,50);
	buttonZaustaviSimulaciju.setBounds(650,330,320,50);
	buttonZaustaviSimulaciju.setVisible(true);
	
	JButton buttonPokreniSimulacijuPonovo=new JButton();
	buttonPokreniSimulacijuPonovo.setText("POKRENI SIMULACIJU PONOVO");
	buttonPokreniSimulacijuPonovo.setSize(100,50);
	buttonPokreniSimulacijuPonovo.setBounds(650,400,320,50);
	buttonPokreniSimulacijuPonovo.setVisible(true);
	
	JButton buttonZavrsiSimulaciju=new JButton();
	buttonZavrsiSimulaciju.setText("ZAVRSI SIMULACIJU");
	buttonZavrsiSimulaciju.setSize(100,50);
	buttonZavrsiSimulaciju.setBounds(650,470,320,50);
	buttonZavrsiSimulaciju.setVisible(true);
	
	brojZarazenihStanovnika=new JTextField("0");
	brojZarazenihStanovnika.setSize(50,20);
	brojZarazenihStanovnika.setBounds(150,20,50,20);
	brojZarazenihStanovnika.setEditable(false);
	brojZarazenihStanovnika.setVisible(true);
	
	brojZ=new JLabel("BROJ ZARAZENIH:");
	brojZ.setForeground(Color.WHITE);
	brojZ.setSize(120,20);
	brojZ.setBounds(20,20,120,20);
	brojZ.setVisible(true);
	
	brojOporavljenihStanovnika=new JTextField("0");
	brojOporavljenihStanovnika.setSize(50,20);
	brojOporavljenihStanovnika.setBounds(450,20,50,20);
	brojOporavljenihStanovnika.setEditable(false);
	brojOporavljenihStanovnika.setVisible(true);
	
	brojO=new JLabel("BROJ OPORAVLJENIH:");
	brojO.setForeground(Color.WHITE);
	brojO.setSize(150,20);
	brojO.setBounds(300,20,150,20);
	brojO.setVisible(true);
	
	tekstPolje=new JTextArea();
	tekstPolje.setLineWrap(true);
	tekstPolje.setWrapStyleWord(true);
	tekstPolje.setEditable(false);
	//JScrollPane scroll=new JScrollPane(tekstPolje);
	scroll=new JScrollPane();
	scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scroll.add(tekstPolje);
	scroll.setBounds(650,540,500,200);
	tekstPolje.setBounds(650,540,500,200);
	tekstPolje.setBorder(new LineBorder(Color.BLACK));
	scroll.getViewport().setBackground(Color.WHITE);
	scroll.getViewport().add(tekstPolje);
	
	
	this.frame.add(buttonOmoguciKretanje);
	this.frame.add(buttonPokreniSimulacijuPonovo);
	this.frame.add(buttonPosaljiAmbulantnoVozilo);
	this.frame.add(buttonPregledajStanjeAmbulantni);
	this.frame.add(buttonPregledajStatistickePodatke);
	this.frame.add(buttonZaustaviSimulaciju);
	this.frame.add(buttonZavrsiSimulaciju);
	this.frame.add(brojZarazenihStanovnika);
	this.frame.add(brojZ);
	this.frame.add(brojOporavljenihStanovnika);
	this.frame.add(brojO);
	this.frame.add(scroll);


	
	buttonStart.addActionListener(new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			frame.setBackground(Color.CYAN);
			boolean ispavanUnos=false;
			Glavna.setPocetnoVrijeme(System.currentTimeMillis());
			while(!ispavanUnos)
			{
			if(!provjeriDaLiJeBroj(brojKuca.getText()) || !provjeriDaLiJeBroj(brojKontrolnihPunktova.getText()) ||  !provjeriDaLiJeBroj(brojAmbulantnihVozila.getText()) || !provjeriDaLiJeBroj(brojDjeceTekst.getText()) || !provjeriDaLiJeBroj(brojOdraslihTekst.getText()) || !provjeriDaLiJeBroj(brojStarihTekst.getText())) // ovdje za sve provjeriti
			{
			 	JOptionPane.showMessageDialog(framePocetni,"NEISPRAVNA VRIJEDNOST!");
			 	break;
			}
			else
			{

				int brojKucaInt = Integer.parseInt(brojKuca.getText());
				int granica = (Grad.getDimenzijaGrada()-2) / 5;
				granica *= granica;
				brojKucaInt = brojKucaInt > granica? granica : brojKucaInt;
				Glavna.setBrojKuca(brojKucaInt);
				int dimX, dimY;
				dimX = dimY = (int)Math.floor(Math.sqrt(Glavna.getBrojKuca()));
				if(dimX * dimY < brojKucaInt) ++dimX;
				if(dimX * dimY < brojKucaInt) ++dimY;
				int korakX=(Grad.getDimenzijaGrada()-2)/dimX;
				int korakY=(Grad.getDimenzijaGrada()-2)/dimY;
				Random random = new Random();
				int x, y;
				Kuca kuca=null;
				for(int i=1 + (korakY-3)/2;i+2<Grad.getDimenzijaGrada()-1;i+=korakY)
				{
					for(int j= 1 + (korakX-3)/2;j+2<Grad.getDimenzijaGrada()-1 && brojKucaInt != 0;j+=korakX, brojKucaInt--)
					{
						kuca = new Kuca(j + random.nextInt(3), i + random.nextInt(3),Glavna.grad);
						Glavna.kuce.add(kuca);
						kuca.setBoja(Color.ORANGE);
					}
				}
				
				int brojKontrolnihPunktovaInt = Integer.parseInt(brojKontrolnihPunktova.getText());
				brojKontrolnihPunktovaInt = brojKontrolnihPunktovaInt > granica? granica : brojKontrolnihPunktovaInt;
				Glavna.setBrojPunktova(brojKontrolnihPunktovaInt);
				if(brojKontrolnihPunktovaInt!=0) {
				dimX = dimY = (int)Math.floor(Math.sqrt(brojKontrolnihPunktovaInt));
				if(dimX * dimY < brojKontrolnihPunktovaInt) ++dimX;
				if(dimX * dimY < brojKontrolnihPunktovaInt) ++dimY;
				korakX=(Grad.getDimenzijaGrada()-2)/dimX;
				korakY=(Grad.getDimenzijaGrada()-2)/dimY;
				
				for(int i=1 + (korakY-3)/2;i+2<Grad.getDimenzijaGrada()-1;i+=korakY)
				{
					for(int j= 1 + (korakX-3)/2;j+2<Grad.getDimenzijaGrada()-1 && brojKontrolnihPunktovaInt != 0;j+=korakX,brojKontrolnihPunktovaInt--)
					{
						do {
							x = j + random.nextInt(3);
							y = i + random.nextInt(3);
							}
							while(Glavna.grad.daLiJeZauzeto(x, y));
							KontrolniPunkt kp = new KontrolniPunkt(x, y, Glavna.grad);
							Glavna.punktovi.add(kp);
					}
				}
				}
				brojKucaInt = Glavna.kuce.size();

				Kuca k = null;
				int brojStarihInt = Integer.parseInt(brojStarihTekst.getText());;
				brojStarihInt = (brojStarihInt > brojKucaInt * 2) ? (brojKucaInt * 1) : brojStarihInt;
				Glavna.setBrojStaraca(brojStarihInt);// Podesi broj ljudi po kuci
				StaraOsoba s = null;
				for(int i = 0; i < brojStarihInt; i++) {
					k = Glavna.kuce.get(i % brojKucaInt);
					s = new StaraOsoba("stari" + i, k, Glavna.grad);
					k.dodajUkucana(s);
					Glavna.stanovnici.add(s);
				}
				
				int brojOdraslihInt = Integer.parseInt(brojOdraslihTekst.getText());;
				brojOdraslihInt = brojOdraslihInt > brojKucaInt * 2 ? brojKucaInt * 1 : brojOdraslihInt;// Podesi broj ljudi po kuci
				Glavna.setBrojOdrslih(brojOdraslihInt);
				OdraslaOsoba o = null;
				for(int i = 0; i < brojOdraslihInt; i++) {
					k = Glavna.kuce.get(i % brojKucaInt);
					o = new OdraslaOsoba("odrasli" + i, k, Glavna.grad);
					k.dodajUkucana(o);
					Glavna.stanovnici.add(o);
				}
				int brojDjeceInt = Integer.parseInt(brojDjeceTekst.getText());
				brojDjeceInt = (brojDjeceInt > brojKucaInt * 2) ? (brojKucaInt * 2) : brojDjeceInt; // Podesi broj ljudi po kuci
				Glavna.setBrojDjece(brojDjeceInt);
				Dijete d = null;
				for(int i = 0; i < brojDjeceInt; i++) {
					int pom = 0;
					do {
					k = Glavna.kuce.get((i + pom++)% brojKucaInt);
					}
					while(!k.imaPunoljetnih());
					d = new Dijete("dijete" + i, k, Glavna.grad);
					k.dodajUkucana(d);
					Glavna.stanovnici.add(d);
				}
				
				int brojSaniteta=Integer.parseInt(brojAmbulantnihVozila.getText());
				Glavna.setBrojAmbulantnihVozila(brojSaniteta);
				Glavna.setBrojukupnePopulacije(brojDjeceInt+brojOdraslihInt+brojStarihInt);
				Ambulanta.setBrojZakapacitet((int)Math.round(((Glavna.getBrojukupnePopulacije()*(new Random().nextInt(6)+10)))/100.00));
				Ambulanta.setBrojSaniteta(brojSaniteta);
				Ambulanta a1=new Ambulanta(0,0, Glavna.grad);
				Ambulanta a2=new Ambulanta(0,Grad.getDimenzijaGrada()-1, Glavna.grad);
				Ambulanta a3=new Ambulanta(Grad.getDimenzijaGrada()-1,0, Glavna.grad);
				Ambulanta a4=new Ambulanta(Grad.getDimenzijaGrada()-1,Grad.getDimenzijaGrada()-1, Glavna.grad);
				Grad.getAmbulante().add(a1);
				Grad.getAmbulante().add(a2);
				Grad.getAmbulante().add(a3);
				Grad.getAmbulante().add(a4);
				Glavna.getAmbulante().add(a1);
				Glavna.getAmbulante().add(a2);
				Glavna.getAmbulante().add(a3);
				Glavna.getAmbulante().add(a4);
				Glavna.setBrojAmbulanti(4);
				Glavna.crtaj=true;
				frame.setVisible(true);
				framePocetni.setVisible(false);
				break;
			}
			}
	
			
				
		}

		
	});


buttonOmoguciKretanje.addActionListener(new ActionListener() 
{
	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frameSudari=new JFrame();
		frameSudari.setSize(500,500);
		frameSudari.setLocationRelativeTo(null);
	    for(Stanovnik stanovnik: Glavna.stanovnici)
	    {
	    	stanovnik.start();
	    }
	    buttonOmoguciKretanje.setEnabled(false);
	    Glavna.setPocetnoVrijeme(System.currentTimeMillis());
		}

	
});

buttonPosaljiAmbulantnoVozilo.addActionListener(new ActionListener() 
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Grad.saljiAmbulantnaVozila();
		buttonPosaljiAmbulantnoVozilo.setEnabled(false);
	}

	
});

buttonPregledajStanjeAmbulantni.addActionListener(new ActionListener() 
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		frameStanjeAmbulanti = new JFrame();
		frameStanjeAmbulanti.setBounds(100, 100, 685, 444);
		frameStanjeAmbulanti.getContentPane().setLayout(null);
		
		tekstStanjaAmbulanti = new JTextArea();
		tekstStanjaAmbulanti.setBounds(10, 29, 649, 304);
		tekstStanjaAmbulanti.setLineWrap(true);
		tekstStanjaAmbulanti.setWrapStyleWord(true);
		tekstStanjaAmbulanti.setEditable(false);
		
		scroll2=new JScrollPane();
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll2.add(tekstStanjaAmbulanti);
		scroll2.setBounds(10, 29, 649, 304);
		tekstStanjaAmbulanti.setBorder(new LineBorder(Color.BLACK));
		scroll2.getViewport().setBackground(Color.WHITE);
		scroll2.getViewport().add(tekstStanjaAmbulanti);
	
		frameStanjeAmbulanti.getContentPane().add(scroll2);
		
		buttonDodajAmbulantu = new JButton("DODAJ AMBULANTU");
		buttonDodajAmbulantu.setBackground(Color.CYAN);
		buttonDodajAmbulantu.setBounds(266, 349, 165, 23);
		buttonDodajAmbulantu.setEnabled(false);
		frameStanjeAmbulanti.getContentPane().add(buttonDodajAmbulantu);
		frameStanjeAmbulanti.setVisible(true);
		
		for(Ambulanta aPom:Grad.getAmbulante())
		{                                                        
			if(aPom.getTrenutnoSlobodno()==0)
				buttonDodajAmbulantu.setEnabled(true);
			//buttonDodajAmbulantu.setBackground(Color.RED);
		}
		
		ispisPodatakaOAmbulantama();
		
		
		buttonDodajAmbulantu.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for(int i=0;i<=Grad.getDimenzijaGrada()-1;i++)
				{
					for(int j=0;j<=Grad.getDimenzijaGrada()-1;j++)
					{
					if((i==0 || i==Grad.getDimenzijaGrada()-1))
						{
							if(!Grad.pribaviInformacijeOAmbulantama(i,j))
							{
								Ambulanta a=new Ambulanta(i,j,Grad.getAmbulante().get(0).getGrad());
								Grad.getAmbulante().add(a);
								Grad.mapa[i][j].zauzmiZaAmbulantu();
								Glavna.ambulante.add(a);
								Glavna.setBrojAmbulanti(Glavna.getBrojAmbulanti()+1);
								return;
							}
						}
						else
						{
							if(!Grad.pribaviInformacijeOAmbulantama(i,0))
							{
								Ambulanta a=new Ambulanta(i,0,Grad.getAmbulante().get(0).getGrad());
								Grad.getAmbulante().add(a);
								Grad.mapa[i][0].zauzmiZaAmbulantu();
								Glavna.ambulante.add(a);
								Glavna.setBrojAmbulanti(Glavna.getBrojAmbulanti()+1);
								return;
							}
							else if(!Grad.pribaviInformacijeOAmbulantama(i,Grad.getDimenzijaGrada()-1))
							{
								Ambulanta a=new Ambulanta(i,Grad.getDimenzijaGrada()-1,Grad.getAmbulante().get(0).getGrad());
								Grad.getAmbulante().add(a);
								Grad.mapa[i][Grad.getDimenzijaGrada()-1].zauzmiZaAmbulantu();
								Glavna.ambulante.add(a);
								Glavna.setBrojAmbulanti(Glavna.getBrojAmbulanti()+1);
								return;
							}
							
						}
					}
					
				}
				
			}
		});
		
	}
	

		
});

buttonPregledajStatistickePodatke.addActionListener(new ActionListener() 
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		frameStatistickiPodaci = new JFrame();
		frameStatistickiPodaci.setBounds(100, 100, 1200, 444);
		frameStatistickiPodaci.getContentPane().setLayout(null);
		
		tekstStatistika = new JTextArea();
		tekstStatistika.setBounds(10, 29, 1100, 304);
		tekstStatistika.setLineWrap(true);
		tekstStatistika.setWrapStyleWord(true);
		tekstStatistika.setEditable(false);
		
		scroll3=new JScrollPane();
		scroll3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll3.add(tekstStatistika);
		scroll3.setBounds(10, 29, 1100, 304);
		tekstStatistika.setBorder(new LineBorder(Color.BLACK));
		scroll3.getViewport().setBackground(Color.WHITE);
		scroll3.getViewport().add(tekstStatistika);
	
		frameStatistickiPodaci.getContentPane().add(scroll3);
		
		buttonSnimiStatistiku = new JButton("SACUVAJ");
		buttonSnimiStatistiku.setBackground(Color.CYAN);
		buttonSnimiStatistiku.setBounds(266, 349, 165, 23);
		buttonSnimiStatistiku.setEnabled(true);
		frameStatistickiPodaci.getContentPane().add(buttonSnimiStatistiku);
		frameStatistickiPodaci.setVisible(true);
		
		
		int trenutnoZarazenihMuskaraca=Informacija.brojZarazenihMuskaracaTrenutno;
		int trenutnoZarazenihZena=Informacija.brojZarazenihZenaTrenutno;
		int trenutnoZarazeneDjece=Informacija.brojZarazeneDjeceTrenutno;
		int trenutnoZarazenihStaraca=Informacija.brojZarazenihStaracaTrenutno;
		int trenutnoZarazenihOdraslihOsoba=Informacija.brojZarazenihOdraslihOsobaTrenutno;
		int sumaTrenutnoZarazenih=Informacija.sumaTrenutnoZarazenih();
		
		int ukupnoZarazenihMuskaraca=Informacija.brojZarazenihMuskaraca;
		int ukupnoZarazenihZena=Informacija.brojZarazenihZena;
		int ukupnoZarazeneDjece=Informacija.brojZarazeneDjece;
		int ukupnoZarazenihStaraca=Informacija.brojZarazenihStaraca;
		int ukupnoZarazenihOdraslihOsoba=Informacija.brojZarazenihOdraslihOsoba;
		int sumaUkupnoZarazenih=Informacija.ukupanBrojZarazenih;
		
		int ukupnoOporavljenihMuskaraca=Informacija.brojOporavljenihMuskaraca;
		int ukupnoOporavljenihZena=Informacija.brojOporavljenihZena;
		int ukupnoOporavljeneDjece=Informacija.brojOporavljenihDjece;
		int ukupnoOporavljenihStaraca=Informacija.brojOporavljenihStaraca;
		int ukupnoOporavljenihOdraslihOsoba=Informacija.brojOporavljenihOdraslihOsoba;
		int sumaUkupnoOporavljenih=Informacija.ukupanBrojOporavljenih;
		
		double procenatZarazenihMuskaracaUkupno=Informacija.procenatZarazenihMuskarci();
		double procenatZarazenihZenaUkupno=Informacija.procenatZarazenihZene();
		double procenatZarazenihDjeceUkupno=Informacija.procenatZarazeneDjece();
		double procenatZarazenihStaracaUkupno=Informacija.procenatZarazenihStarih();
		double procenatZarazenihOdraslihOsobaUkupno=Informacija.procenatZarazenihOdraslihOsoba();
		
		double procenatOporavljenihMuskaracaUkupno=Informacija.procenatOporavljenihMuskarci();
		double procenatOporavljenihZenaUkupno=Informacija.procenatOporavljenihZena();
		double procenatOporavljenihDjeceUkupno=Informacija.procenatOpooravljeneDjece();
		double procenatOporavljenihStaracaUkupno=Informacija.procenatOporavljenihStaraca();
		double procenatOporavljenihOdraslihOsobaUkupno=Informacija.procenatOporavljenihOdraslihOsoba();
		
		tekstStatistika.append(String.format("%170s","STATISTIKA\n"));
		tekstStatistika.append(String.format("%265s","---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"));
		//tekstStatistika.append(String.format("%20s %30s %20s %20s %15s %20s %15s","|","Zarazenih trenutno","|","Zarazenih ukupno","|","Oporavljenih","|\n"));
		tekstStatistika.append(String.format("%20s %30s %20s %20s %15s %30s %15s %20s  %10s  %20s  %10s %20s %10s","|"," ","|","Zarazenih trenutno","|","Zarazenih ukupno (tip)","|","Oporavljenih","|","Ukupno zarazenih (%)","|","Ukupno oporavljenih (%)","|\n"));
		tekstStatistika.append(String.format("%160s","===============================================================================================================================================\n"));
		tekstStatistika.append(String.format("%20s %18s %20s %20s %30s %30s %32s %20s %20s %10s %.2f %35s %10s %.2f %35s","|","Muskog pola","|",Informacija.brojZarazenihMuskaracaTrenutno,"|",Informacija.brojZarazenihMuskaraca,"|",Informacija.brojOporavljenihMuskaraca,"|"," ",Informacija.procenatZarazenihMuskarci(),"|"," ",Informacija.procenatOporavljenihMuskarci(),"|\n"));
		tekstStatistika.append(String.format("%265s","---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"));
		tekstStatistika.append(String.format("%20s %19s %18s %20s %30s %30s %32s %20s %20s %10s %.2f %35s %10s %.2f %35s","|","Zenskog pola","|",Informacija.brojZarazenihZenaTrenutno,"|",Informacija.brojZarazenihZena,"|",Informacija.brojOporavljenihZena,"|"," ",Informacija.procenatZarazenihZene(),"|"," ",Informacija.procenatOporavljenihZena(),"|\n"));
		tekstStatistika.append(String.format("%265s","---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"));
		tekstStatistika.append(String.format("%20s %26s %18s %20s %30s %30s %32s %20s %20s %10s %.2f %35s %10s %.2f %35s","|","Djeca","|",Informacija.brojZarazeneDjeceTrenutno,"|",Informacija.brojZarazeneDjece,"|",Informacija.brojOporavljenihDjece,"|"," ",Informacija.procenatZarazeneDjece(),"|"," ",Informacija.procenatOpooravljeneDjece(),"|\n"));
		tekstStatistika.append(String.format("%265s","---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"));
		tekstStatistika.append(String.format("%20s %27s %18s %20s %30s %30s %32s %20s %20s %10s %.2f %35s %10s %.2f %35s","|","Starci","|",Informacija.brojZarazenihStaracaTrenutno,"|",Informacija.brojZarazenihStaraca,"|",Informacija.brojOporavljenihStaraca,"|"," ",Informacija.procenatZarazenihStarih(),"|"," ",Informacija.procenatOporavljenihStaraca(),"|\n"));
		tekstStatistika.append(String.format("%265s","---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"));
		tekstStatistika.append(String.format("%20s %25s %18s %20s %30s %30s %32s %20s %20s %10s %.2f %35s %10s %.2f %35s","|","Odrasli","|",Informacija.brojZarazenihOdraslihOsobaTrenutno,"|",Informacija.brojZarazenihOdraslihOsoba,"|",Informacija.brojOporavljenihOdraslihOsoba,"|"," ",Informacija.procenatZarazenihOdraslihOsoba(),"|"," ",Informacija.procenatOporavljenihOdraslihOsoba(),"|\n"));
		tekstStatistika.append(String.format("%265s","---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"));
		tekstStatistika.append(String.format("%20s %18s %18s %20s %30s %30s %32s %20s %20s %20s %35s %20s %35s","|","SUMARNO","|",Informacija.sumaTrenutnoZarazenih(),"|",Informacija.sumaUkupnoZarazenih(),"|",Informacija.sumaUkupnoOporavljenih(),"|"," ","|"," ","|\n"));
		tekstStatistika.append(String.format("%265s","---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"));
		
		
		
		buttonSnimiStatistiku.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try (PrintWriter pisacStatistike = new PrintWriter(new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir")+File.separatorChar+"statistika.csv"))))){
				      StringBuilder sb = new StringBuilder();
				      sb.append("TIP");
				      sb.append(',');
				      sb.append("Zarazenih trenutno");
				      sb.append(',');
				      sb.append("Zarazenih ukupno");
				      sb.append(',');
				      sb.append("Oporavljenih");
				      sb.append(',');
				      sb.append("Ukupno zarazenih (%)");
				      sb.append(',');
				      sb.append("Ukupno oporavljenih (%),");
				      sb.append('\n');
				  
				      sb.append("Muskog pola");
				      sb.append(',');
				      sb.append(trenutnoZarazenihMuskaraca);
				      sb.append(',');
				      sb.append(ukupnoZarazenihMuskaraca);
				      sb.append(',');
				      sb.append(ukupnoOporavljenihMuskaraca);
				      sb.append(',');
				      sb.append(procenatZarazenihMuskaracaUkupno);
				      sb.append(',');
				      sb.append(procenatOporavljenihMuskaracaUkupno);
				      sb.append('\n');
				      
				      sb.append("Zenskog pola");
				      sb.append(',');
				      sb.append(trenutnoZarazenihZena);
				      sb.append(',');
				      sb.append(ukupnoZarazenihZena);
				      sb.append(',');
				      sb.append(ukupnoOporavljenihZena);
				      sb.append(',');
				      sb.append(procenatZarazenihZenaUkupno);
				      sb.append(',');
				      sb.append(procenatOporavljenihZenaUkupno);
				      sb.append('\n');
				      
				      sb.append("Djeca");
				      sb.append(',');
				      sb.append(trenutnoZarazeneDjece);
				      sb.append(',');
				      sb.append(ukupnoZarazeneDjece);
				      sb.append(',');
				      sb.append(ukupnoOporavljeneDjece);
				      sb.append(',');
				      sb.append(procenatZarazenihDjeceUkupno);
				      sb.append(',');
				      sb.append(procenatOporavljenihDjeceUkupno);
				      sb.append('\n');
				      
				      sb.append("Starci");
				      sb.append(',');
				      sb.append(trenutnoZarazenihStaraca);
				      sb.append(',');
				      sb.append(ukupnoZarazenihStaraca);
				      sb.append(',');
				      sb.append(ukupnoOporavljenihStaraca);
				      sb.append(',');
				      sb.append(procenatZarazenihStaracaUkupno);
				      sb.append(',');
				      sb.append(procenatOporavljenihStaracaUkupno);
				      sb.append('\n');
				      
				      sb.append("Odrasli");
				      sb.append(',');
				      sb.append(trenutnoZarazenihOdraslihOsoba);
				      sb.append(',');
				      sb.append(ukupnoZarazenihOdraslihOsoba);
				      sb.append(',');
				      sb.append(ukupnoOporavljenihOdraslihOsoba);
				      sb.append(',');
				      sb.append(procenatZarazenihOdraslihOsobaUkupno);
				      sb.append(',');
				      sb.append(procenatOporavljenihOdraslihOsobaUkupno);
				      sb.append('\n');
				      
				      sb.append("SUMA");
				      sb.append(',');
				      sb.append(sumaTrenutnoZarazenih);
				      sb.append(',');
				      sb.append(sumaUkupnoZarazenih);
				      sb.append(',');
				      sb.append(sumaUkupnoOporavljenih);
				      sb.append('\n');
				      

				      pisacStatistike.write(sb.toString());

				      System.out.println("upisano!");

				    } 
				
				catch (IOException ex) 
					{
				      log(ex);
				    }
			}
		});
		
	}
	

	
});

buttonZaustaviSimulaciju.addActionListener(new ActionListener() 
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		for (Stanovnik s :Glavna.stanovnici)
		{
			s.setZaustavi(true);
			
		}
		for(AmbulantnoVozilo a: Glavna.vozila)
		{
			a.setZaustavi(true);
		}
		try 
		{	
			ObjectOutputStream pisacGlavne=new ObjectOutputStream(new FileOutputStream(new File(System.getProperty("user.dir")+File.separatorChar+"SerijalizovanaGlavna"+File.separatorChar+"glavna.ser")));
			pisacGlavne.writeObject(glavna);
			pisacGlavne.close();
	
		}
		catch(IOException ex)
		{
			log(ex);
		}
	}

	
});

buttonPokreniSimulacijuPonovo.addActionListener(new ActionListener() 
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			ObjectInputStream citacGlavne=new ObjectInputStream(new FileInputStream(new File(System.getProperty("user.dir")+File.separatorChar+"SerijalizovanaGlavna"+File.separatorChar+"glavna.ser")));
			glavna=(Glavna)citacGlavne.readObject();
			citacGlavne.close();
		
		for (Stanovnik s :Glavna.stanovnici)
		{
			s.setZaustavi(false);
		}
		for (AmbulantnoVozilo a :glavna.vozila)
		{
			a.setZaustavi(false);
		}
		for (Stanovnik s : glavna.stanovnici)
		{
			if(!s.isKarantin())
			s.start();
		}
		for (AmbulantnoVozilo v : glavna.vozila)
		{
			v.start();
		}
		}
		catch(IOException | ClassNotFoundException ex)
		{
			log(ex);
		}
	}
	
});

buttonZavrsiSimulaciju.addActionListener(new ActionListener() 
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		try {
			Date date=new Date();
			SimpleDateFormat format=new SimpleDateFormat("yy-MM-dd HH-mm-ss");
			File putanja=new File(System.getProperty("user.dir")+File.separatorChar+"PodaciNaKraju"+File.separatorChar+"SIM-JavaKov-20"+format.format(date)+".txt");
		PrintWriter pisackrajnjihPodataka=new PrintWriter(new BufferedWriter(new FileWriter(putanja)));
		StringBuilder konacniPodaci=new StringBuilder();
		konacniPodaci.append("Broj Djece:"+Glavna.getBrojDjece()+"\n");
		konacniPodaci.append("Broj Odraslih:"+Glavna.getBrojOdrslih()+"\n");
		konacniPodaci.append("Broj Staraca:"+Glavna.getBrojStaraca()+"\n");
		konacniPodaci.append("Broj Ambulanti:"+Glavna.getBrojAmbulanti()+"\n");
		konacniPodaci.append("Broj Ambualntnih vozila:"+Glavna.getBrojAmbulantnihVozila()+"\n");
		konacniPodaci.append("Broj Kontronih punktova:"+Glavna.getBrojPunktova()+"\n");
		
		konacniPodaci.append("Broj zarazene djece:"+Informacija.brojZarazeneDjece+"\n");
		konacniPodaci.append("Broj zarazenih starih lica:"+Informacija.brojZarazenihStaraca+"\n");
		konacniPodaci.append("Broj zarazenih odraslih lica:"+Informacija.brojZarazenihOdraslihOsoba+"\n");
		konacniPodaci.append("Broj zarazenih muskaraca:"+Informacija.brojZarazenihMuskaraca+"\n");
		konacniPodaci.append("Broj zarazenih zena:"+Informacija.brojZarazenihZena+"\n");
		
		konacniPodaci.append("Broj oporavljene djece:"+Informacija.brojOporavljenihDjece+"\n");
		konacniPodaci.append("Broj oporavljenih starih lica:"+Informacija.brojOporavljenihStaraca+"\n");
		konacniPodaci.append("Broj oporavljenih odraslih lica:"+Informacija.brojOporavljenihOdraslihOsoba+"\n");
		konacniPodaci.append("Broj oporavljenih muskaraca:"+Informacija.brojOporavljenihMuskaraca+"\n");
		konacniPodaci.append("Broj oporavljenih zena:"+Informacija.brojOporavljenihZena+"\n");
		
		
		konacniPodaci.append("Broj zarazenih ukupno:"+Informacija.sumaUkupnoZarazenih()+"\n");
		konacniPodaci.append("Broj oporavljenih ukupno:"+Informacija.sumaUkupnoOporavljenih()+"\n");
		
		konacniPodaci.append("VRIJEME TRAJANJA SIMULAICJE: "+(System.currentTimeMillis()-Glavna.getPocetnoVrijeme())/1000+"  sekundi");
		
		pisackrajnjihPodataka.println(konacniPodaci);
		pisackrajnjihPodataka.close();
		System.exit(0);
		
		}
		catch(IOException ex)
		{
			log(ex);
		}
	}

	
});
canvas=new Canvas();
canvas.setPreferredSize(new Dimension(sirina,visina));
canvas.setFocusable(false);
frame.add(canvas);
frame.pack();//da prozor bude sveden na velicinu canvasa
frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//framePocetni.setExtendedState(JFrame.MAXIMIZED_BOTH);
//framePocetni.pack();
framePocetni.setVisible(true);
//frame.setVisible(true);
}
public JFrame getFramePocetni() {
	return framePocetni;
}
public void setFramePocetni(JFrame framePocetni) {
	this.framePocetni = framePocetni;
}
public JButton getButtonStart() {
	return buttonStart;
}
public void setButtonStart(JButton buttonStart) {
	this.buttonStart = buttonStart;
}
public void iscrtaj()
{
	
	buffer=this.getCanvas().getBufferStrategy();
	if(buffer==null)
	{
		this.getCanvas().createBufferStrategy(3);
		return;
	}
	int velicina=20*format;
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
    for (k = 0; k < format; k++) //horizontalne
    {
    	graphic.drawLine(0, (k * htOfRow)+50, sirina, (k * htOfRow)+50);
    }
    int wdOfRow = velicinaCelije; //vertikalne
    for (k = 0; k < format; k++) 
    {
    	graphic.drawLine(k * wdOfRow,0, k * wdOfRow, visina+50);   
    }
	buffer.show();
	graphic.dispose();
}

public static synchronized void prikazNaTabli(String tekst)
{
	tekstPolje.append(tekst);	
}
public static synchronized void prikazStatistike(String tekst)
{
	tekstStanjaAmbulanti.append(tekst);	
}
public static void prikazBrojaZO(String putanja)
{
	try
	{
	File ff=new File(putanja);
	synchronized(ff)
	{
	BufferedReader citac=new BufferedReader(new FileReader(ff));
	String brojZarOp=citac.readLine();
	String el[]=brojZarOp.split(":");
	brojZarazenihStanovnika.setText(el[0]);
	brojOporavljenihStanovnika.setText(el[1]);
	citac.close();
	}
	}
	catch(Exception ex)
	{
		System.out.println("NA UPISU");
	}
	
}
public JFrame getFrame() {
	return frame;
}
public void setFrame(JFrame frame) {
	this.frame = frame;
}
public String getNaslov() {
	return naslov;
}
public void setNaslov(String naslov) {
	this.naslov = naslov;
}
public static BufferStrategy getBuffer() {
	return buffer;
}
public static void setBuffer(BufferStrategy buffer) {
	Ekran.buffer = buffer;
}
public static Graphics getGraphic() {
	return graphic;
}
public static void setGraphic(Graphics graphic) {
	Ekran.graphic = graphic;
}
public int getFormat() {
	return format;
}
public void setFormat(int format) {
	this.format = format;
}
public int getSirina() {
	return sirina;
}
public void setSirina(int sirina) {
	this.sirina = sirina;
}
public int getVisina() {
	return visina;
}
public void setVisina(int visina) {
	this.visina = visina;
}
public Canvas getCanvas() {
	return canvas;
}
public void setCanvas(Canvas canvas) {
	this.canvas = canvas;
}
public JLabel getLabela() {
	return labela;
}
public void setLabela(JLabel labela) {
	this.labela = labela;
}
public JButton getDugme() {
	return dugme;
}
public void setDugme(JButton dugme)
{
	this.dugme = dugme;
}

public void ispisPodatakaOAmbulantama()
{
	tekstStanjaAmbulanti.append(String.format("%100s","STANJE AMBULANTI\n"));
	tekstStanjaAmbulanti.append(String.format("%150s","----------------------------------------------------------------------------------------------------------------------------------\n"));
	tekstStanjaAmbulanti.append(String.format("%20s %30s %20s %20s %15s %20s %15s","|","NAZIV AMBULANTE","|","SLOBODNO","|","ZAUZETO","|\n"));
	tekstStanjaAmbulanti.append(String.format("%95s","==========================================================================\n"));
	for(Ambulanta a: Grad.getAmbulante())
	{
		tekstStanjaAmbulanti.append(String.format("%150s","----------------------------------------------------------------------------------------------------------------------------------\n"));
		tekstStanjaAmbulanti.append(String.format("%20s %29s %30s %20s %26s %20s %25s","|","Ambulanta"+a.getJedinstveniID(),"|",a.getTrenutnoSlobodno(),"|",a.getZauzetiKreveti(),"|\n"));
		tekstStanjaAmbulanti.append(String.format("%150s","----------------------------------------------------------------------------------------------------------------------------------\n"));
	}
}
public boolean provjeriDaLiJeBroj(String tekst)
{
	try
	{
		Integer.parseInt(tekst);
		
	}
	catch(Exception ex)
	{
		return false;
	}
	return true;
}
public JFrame getFrameStanjeAmbulanti() {
	return frameStanjeAmbulanti;
}
public void setFrameStanjeAmbulanti(JFrame frameStanjeAmbulanti) {
	this.frameStanjeAmbulanti = frameStanjeAmbulanti;
}
public JFrame getFrameStatistickiPodaci() {
	return frameStatistickiPodaci;
}
public void setFrameStatistickiPodaci(JFrame frameStatistickiPodaci) {
	this.frameStatistickiPodaci = frameStatistickiPodaci;
}
public JScrollPane getScroll() {
	return scroll;
}
public void setScroll(JScrollPane scroll) {
	this.scroll = scroll;
}
public JScrollPane getScroll2() {
	return scroll2;
}
public void setScroll2(JScrollPane scroll2) {
	this.scroll2 = scroll2;
}
public JScrollPane getScroll3() {
	return scroll3;
}
public void setScroll3(JScrollPane scroll3) {
	this.scroll3 = scroll3;
}
public JTextArea getTekstualno() {
	return tekstualno;
}
public void setTekstualno(JTextArea tekstualno) {
	this.tekstualno = tekstualno;
}
public JButton getButtonOmoguciKretanje() {
	return buttonOmoguciKretanje;
}
public void setButtonOmoguciKretanje(JButton buttonOmoguciKretanje) {
	this.buttonOmoguciKretanje = buttonOmoguciKretanje;
}
public JButton getButtonDodajAmbulantu() {
	return buttonDodajAmbulantu;
}
public void setButtonDodajAmbulantu(JButton buttonDodajAmbulantu) {
	this.buttonDodajAmbulantu = buttonDodajAmbulantu;
}
public JButton getButtonSnimiStatistiku() {
	return buttonSnimiStatistiku;
}
public void setButtonSnimiStatistiku(JButton buttonSnimiStatistiku) {
	this.buttonSnimiStatistiku = buttonSnimiStatistiku;
}
public JLabel getBrojZ() {
	return brojZ;
}
public void setBrojZ(JLabel brojZ) {
	this.brojZ = brojZ;
}
public JLabel getBrojO() {
	return brojO;
}
public void setBrojO(JLabel brojO) {
	this.brojO = brojO;
}
public static JTextArea getTekstPolje() {
	return tekstPolje;
}
public static void setTekstPolje(JTextArea tekstPolje) {
	Ekran.tekstPolje = tekstPolje;
}
public static JTextArea getTekstStanjaAmbulanti() {
	return tekstStanjaAmbulanti;
}
public static void setTekstStanjaAmbulanti(JTextArea tekstStanjaAmbulanti) {
	Ekran.tekstStanjaAmbulanti = tekstStanjaAmbulanti;
}
public static JTextArea getTekstStatistika() {
	return tekstStatistika;
}
public static void setTekstStatistika(JTextArea tekstStatistika) {
	Ekran.tekstStatistika = tekstStatistika;
}
public static JTextField getBrojZarazenihStanovnika() {
	return brojZarazenihStanovnika;
}
public static void setBrojZarazenihStanovnika(JTextField brojZarazenihStanovnika) {
	Ekran.brojZarazenihStanovnika = brojZarazenihStanovnika;
}
public static JTextField getBrojOporavljenihStanovnika() {
	return brojOporavljenihStanovnika;
}
public static void setBrojOporavljenihStanovnika(JTextField brojOporavljenihStanovnika) {
	Ekran.brojOporavljenihStanovnika = brojOporavljenihStanovnika;
}
public static JButton getButtonPosaljiAmbulantnoVozilo() {
	return buttonPosaljiAmbulantnoVozilo;
}
public static void setButtonPosaljiAmbulantnoVozilo(JButton buttonPosaljiAmbulantnoVozilo) {
	Ekran.buttonPosaljiAmbulantnoVozilo = buttonPosaljiAmbulantnoVozilo;
}
public static void log(Exception ex)
{
	Logger logger=Logger.getLogger(Ekran.class.getName());
	logger.addHandler(filehandler);
	logger.log(Level.WARNING,Ekran.class.getName(),ex);
}
}
