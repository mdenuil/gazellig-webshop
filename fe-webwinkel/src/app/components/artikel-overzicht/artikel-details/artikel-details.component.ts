import { Component, OnInit, Input, Inject } from '@angular/core';
import { Artikel } from '../../../models/artikel.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { BesteldArtikel } from '../../../models/besteldArtikel.model';

@Component({
	selector: 'app-artikel-details',
	templateUrl: './artikel-details.component.html',
	styleUrls: ['./artikel-details.component.scss']
})
export class ArtikelDetailsComponent implements OnInit {
	besteldArtikel: BesteldArtikel;
	inCart = false;
	aantal = 1;

	constructor(
		private readonly dialogRef: MatDialogRef<ArtikelDetailsComponent>,
		private readonly shoppingCart: ShoppingCartService,
		@Inject(MAT_DIALOG_DATA) public artikel: Artikel
	) { }

	ngOnInit() {
		this.checkShoppingCart();

		this.besteldArtikel = {
			aantal: this.aantal,
			artikelNummer: this.artikel.artikelNummer,
			artikel: this.artikel
		}
	}

	addToCart() {
		this.shoppingCart.addArtikel(this.besteldArtikel);
		this.checkShoppingCart();
	}

	subtractAmount() {
		if (this.besteldArtikel.aantal > 1) {
			this.besteldArtikel.aantal--;
		}
	}

	addAmount() {
		this.besteldArtikel.aantal++;
	}

	close() {
		this.dialogRef.close();
	}

	private checkShoppingCart() {
		this.inCart = this.shoppingCart.artikelInShoppingCart(this.artikel.artikelNummer);
	}
}
