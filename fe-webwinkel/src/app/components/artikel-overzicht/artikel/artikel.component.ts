import { Component, OnInit, Input } from '@angular/core';
import { Artikel } from '../../../models/artikel.model';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { BesteldArtikel } from '../../../models/besteldArtikel.model';
import { ArtikelDetailsComponent } from '../artikel-details/artikel-details.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
	selector: 'app-artikel',
	templateUrl: './artikel.component.html',
	styleUrls: ['./artikel.component.scss']
})
export class ArtikelComponent implements OnInit {
	@Input() artikel: Artikel;
	besteldArtikel: BesteldArtikel;
	loaded = false;
	inCart = false;

	aantal = 1;

	constructor(private readonly shoppingCart: ShoppingCartService, private readonly dialog: MatDialog) { }

	ngOnInit() {
		if (this.artikel) {
			this.loaded = true;
			this.checkShoppingCart();

			this.besteldArtikel = {
				aantal: this.aantal,
				artikelNummer: this.artikel.artikelNummer,
				artikel: this.artikel
			}
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

	openDetails() {
		this.dialog.open(ArtikelDetailsComponent, {
			width: '900px',
			data: this.artikel
		});
	}

	private checkShoppingCart() {
		this.inCart = this.shoppingCart.artikelInShoppingCart(this.artikel.artikelNummer);
	}
}
