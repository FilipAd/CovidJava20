package util;

import grad.Grad;

public class Util {
	public static int[] pomjeri(int x, int y, int smjer) {
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
		int[] ret = {x, y};
		return ret;
	}
	
	public static boolean daLiJeLegalno(int x, int y) {
		return x >= 0 && x < Grad.getDimenzijaGrada() && y >= 0 && y < Grad.getDimenzijaGrada();
	}

}
