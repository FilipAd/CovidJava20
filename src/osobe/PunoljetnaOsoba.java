package osobe;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

import grad.Grad;
import util.Util;

public abstract class PunoljetnaOsoba extends Stanovnik implements RizicniUcesnik,Serializable {
	
	
	public PunoljetnaOsoba() {

	}

	public PunoljetnaOsoba(String ime, String prezime, int godinaRodjenja, Pol pol, int idKuce, int id, Grad grad) {
		super(ime, prezime, godinaRodjenja, pol, idKuce, id, grad);
		this.boja = Color.GRAY;
	}

	@Override
	public boolean skreni()
	{
		int[] pomKord = Util.pomjeri(pozicijaX, pozicijaY, smjer);
		if (Util.daLiJeLegalno(pomKord[0], pomKord[1])) {
			synchronized (Grad.mapa[pomKord[0]][pomKord[1]]) {
				boolean[] fizInfo = { true, true, true };
				fizInfo = grad.pribaviInfoZaKretanjeFizickih(pozicijaX, pozicijaY, smjer, this.kuca.getKoordinataX(),
						this.kuca.getKoordinataY());
				// System.out.println(smjer + "{" + fizInfo[0] + fizInfo[1] + fizInfo[2]);
				if (fizInfo[1]) {
					// System.out.println("[1] zauzeto");
					if (!fizInfo[2])
						smjer = (smjer + 1) % 8;
					else if (!fizInfo[0])
						smjer = (smjer + 7) % 8;
					else {
						smjer = (smjer + 2) % 8;
						return false;
					}
				}
				boolean[] info = { true, true, true, true };
				if (smjer % 2 == 0)
					info = grad.pribaviInfoZaKretanjePunoljetnihOrtogonalno(pozicijaX, pozicijaY, smjer);
				else
					info = grad.pribaviInfoZaKretanjePunoljetnihDijagonalno(pozicijaX, pozicijaY, smjer);
				// System.out.println(this.ime+"info:"+info[0]+"|"+info[1]+"|"+info[2]);
				if (info[1] || info[3]) {
					smjer = (smjer + 2) % 8; // ako je srednji zauzet skreni desno
					return false;
				}
				if (info[0] && info[2]) {
					smjer = (smjer + 4) % 8; // ako su i lijevi i desni zauzeti idi nazad
					return false;
				}
				if (info[0]) {
					smjer = (smjer + 2) % 8; // ako je lijevi zauzet idi desno
					return false;
				}
				if (info[2]) {
					smjer = (smjer + 6) % 8; // ako je desni zauzet idi lijevo
					return false;

				}
				if (vanGranica() && !izasaoIzBolnice) {
					smjer = (smjer + 1) % 8;
					return false;
				}
				idi();
				return true;
			}
		} else {
			smjer = (smjer + 1) % 8;
			return false;
		}
	}



	private boolean vanGranica() {
		int[] noveKord = Util.pomjeri(pozicijaX, pozicijaY, smjer);
		return gornjiLijeviCosakX > noveKord[0] || gornjiLijeviCosakX + sirinaKvadrata <= noveKord[0]
				|| gornjiLijeviCosakY > noveKord[1] || gornjiLijeviCosakY + duzinaKvadrata <= noveKord[1];
	}
}
