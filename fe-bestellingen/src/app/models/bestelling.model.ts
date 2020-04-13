import { BesteldArtikel } from './besteldArtikel.model';
import { Adres } from './adres.model';
import { Klant } from './klant.model';

export interface Bestelling {
	bestelNummer: number;

	klantGegevens: Klant;

	afleverAdres: Adres;
	factuurAdres: Adres;

	status: string;

	artikelen: BesteldArtikel[];
}
