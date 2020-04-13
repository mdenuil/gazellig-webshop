import { Component, OnInit } from '@angular/core';
import { BestellingService } from '../../services/bestelling.service';
import { Bestelling } from '../../models/bestelling.model';
import { BesteldArtikel } from '../../models/besteldArtikel.model';
import { Artikel } from '../../models/artikel.model';
import { ArtikelService } from '../../services/artikel.service';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
	selector: 'app-order-history',
	templateUrl: './order-history.component.html',
	styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent implements OnInit {
	bestellingen: Bestelling[];
	displayedColumns = ['orderNumber', 'orderStatus', 'totalAmount'];
	loading = true;

	constructor(
		private readonly bestellingService: BestellingService,
		private readonly artikelService: ArtikelService) { }

	ngOnInit() {
		this.bestellingService.getBestellingen()
			.subscribe(bestellingen => {
				this.bestellingen = bestellingen;

				const observableArray = this.bestellingen
					.map((bestelling: Bestelling) => this.calculateTotalAmount(bestelling));

				forkJoin(observableArray).subscribe({
					complete: () => this.loading = false
				});
			});
	}

	calculateTotalAmount(bestelling: Bestelling): Observable<any> {
		bestelling.totalAmount = 0;

		const observableArray = bestelling.artikelen
			.map((artikel: BesteldArtikel) => this.artikelService.getArtikel(artikel.artikelNummer));

		return forkJoin(observableArray).pipe(
			map((artikelen: Artikel[]) => artikelen.forEach((artikel: Artikel) => {
				const besteldArtikelFound: BesteldArtikel = bestelling.artikelen
					.find((besteldArtikel: BesteldArtikel) => besteldArtikel.artikelNummer === artikel.artikelNummer);

				const artikelPrijsWithoutVAT = artikel.prijs * besteldArtikelFound.aantal;
				const VAT = (artikelPrijsWithoutVAT / 121) * 21;
				const artikelTotal = artikelPrijsWithoutVAT + VAT;
				bestelling.totalAmount += artikelTotal;
			}))
		);
	}
}
