export interface Artikel {
	artikelNummer: number;
	aantal: number;
	naam: string;
	beschrijving: string;
	prijs: number;
	afbeeldingUrl: string;
	leverbaarVanaf: string;
	leverbaarTot: string;
	leverancierCode: string;
	leverancier: string;
	categorieen: string[];
}
