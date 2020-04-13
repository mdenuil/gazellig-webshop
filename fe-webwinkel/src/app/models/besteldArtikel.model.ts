import { Artikel } from './artikel.model';

export interface BesteldArtikel {
	artikelNummer: number;
	aantal: number;
	artikel?: Artikel;
}
