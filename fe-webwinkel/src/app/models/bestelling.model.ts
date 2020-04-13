import { BesteldArtikel } from './besteldArtikel.model';
import { Adres } from './adres.model';

export interface Bestelling {
	initialen: string;
	achternaam: string;
	email: string;

	afleverAdres: Adres;
	factuurAdres: Adres;

	status: string;

	artikelen: BesteldArtikel[];

	bestelNummer?: number;
	totalAmount?: number;
}
