import { Injectable } from '@angular/core';
import { Subject, forkJoin } from 'rxjs';
import { BesteldArtikel } from '../models/besteldArtikel.model';

import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { debounceTime, map } from 'rxjs/operators';
import { AccountService } from './account.service';
import { Winkelmandje } from '../models/winkelmandje.model';
import { ArtikelService } from './artikel.service';
import { Artikel } from '../models/artikel.model';

@Injectable()
export class ShoppingCartService {
	public numberOfArtikelen: Subject<number> = new Subject();
	private readonly winkelmandje: Winkelmandje = { artikelen: [] };

	constructor(
		private readonly http: HttpClient,
		private readonly accountService: AccountService,
		private readonly artikelService: ArtikelService
	) {
		this.loadLocalstorage();
		this.updateArtikelenCounter();

		this.accountService.currentUser
			.subscribe({
				next: (user) => {
					if (user) {
						if (this.winkelmandje.artikelen.length === 0) {
							this.getWinkelmandjeFromBackend();
						} else if (this.winkelmandje.artikelen.length > 0) {
							this.sendToBackend();
						}
					}
				}
			});

		this.numberOfArtikelen
			.pipe(
				debounceTime(2000)
			).subscribe({
				next: () => {
					if (this.accountService.isLoggedIn) {
						this.sendToBackend();
					}
				}
			});
	}

	addArtikel(besteldArtikel: BesteldArtikel): void {
		this.winkelmandje.artikelen.push(besteldArtikel);
		this.updateArtikelenCounter();
		this.updateLocalstorage();
	}

	updateArtikel(besteldArtikel: BesteldArtikel): void {
		const index = this.winkelmandje.artikelen.findIndex(
			artikelInCart => artikelInCart.artikelNummer === besteldArtikel.artikelNummer
		);
		this.winkelmandje.artikelen[index] = besteldArtikel;

		this.updateArtikelenCounter();
		this.updateLocalstorage();
	}

	deleteArtikel(besteldArtikel: BesteldArtikel) {
		const index = this.winkelmandje.artikelen.findIndex(
			artikelInCart => artikelInCart.artikelNummer === besteldArtikel.artikelNummer
		);
		this.winkelmandje.artikelen.splice(index, 1);

		this.updateArtikelenCounter();
		this.updateLocalstorage();
	}

	artikelInShoppingCart(artikelNummer: number): boolean {
		if (this.winkelmandje.artikelen.find(artikelInCart => artikelInCart.artikelNummer === artikelNummer)) {
			return true;
		} else {
			return false;
		}
	}

	getArtikelAmount(): number {
		let total = 0;

		for (const besteldArtikel of this.winkelmandje.artikelen) {
			total += besteldArtikel.aantal;
		}

		return total;
	}

	getArtikelen(): BesteldArtikel[] {
		return this.winkelmandje.artikelen;
	}

	calculateTotalAmount(): number {
		let total = 0;

		this.winkelmandje.artikelen.forEach(besteldArtikel => {
			total += (besteldArtikel.artikel.prijs * besteldArtikel.aantal);
		});

		return total;
	}

	calculateTotalAmountWithoutVAT(): number {
		const total = this.calculateTotalAmount();

		const VAT = (total / 121) * 21;

		return total - VAT;
	}

	public emtpyShoppingCart(): void {
		this.winkelmandje.artikelen = [];
		this.updateArtikelenCounter();
		this.updateLocalstorage();
		this.sendToBackend();
	}

	private updateArtikelenCounter(): void {
		let total = 0;

		for (const besteldArtikel of this.winkelmandje.artikelen) {
			total += besteldArtikel.aantal;
		}

		this.numberOfArtikelen.next(total);
	}

	private sendToBackend() {
		const body: Winkelmandje = {
			artikelen: []
		};

		this.winkelmandje.artikelen.forEach(besteldArtikel =>
			body.artikelen.push({
				artikelNummer: besteldArtikel.artikelNummer,
				aantal: besteldArtikel.aantal
			})
		);

		this.http.post<BesteldArtikel[]>(`${environment.apiUrl}/winkelmandje`, body).subscribe();
	}

	private getWinkelmandjeFromBackend() {
		this.http.get<Winkelmandje>(`${environment.apiUrl}/winkelmandje`)
			.subscribe({
				next: (incomingWinkelmandje: Winkelmandje) => {
					if (incomingWinkelmandje && incomingWinkelmandje.artikelen.length > 0) {
						this.winkelmandje.artikelen = [];

						incomingWinkelmandje.artikelen.forEach((artikel: BesteldArtikel) => {
							this.winkelmandje.artikelen.push({
								aantal: artikel.aantal,
								artikelNummer: artikel.artikelNummer,
								artikel: null
							});
						});

						const observableArray = incomingWinkelmandje.artikelen
							.map((artikel: BesteldArtikel) => this.artikelService.getArtikel(artikel.artikelNummer));

						forkJoin(observableArray).pipe(
							map((artikelen: Artikel[]) => artikelen.forEach((artikel: Artikel) => {
								const besteldArtikelFound: BesteldArtikel = this.winkelmandje.artikelen
									.find((besteldArtikel: BesteldArtikel) => besteldArtikel.artikelNummer === artikel.artikelNummer);

								besteldArtikelFound.artikel = artikel;
							}))
						).subscribe({
							complete: () => {
								this.updateArtikelenCounter();
								this.updateLocalstorage();
							}
						});
					}
				}
			});
	}

	private loadLocalstorage(): void {
		if (localStorage.shoppingCart) {
			this.winkelmandje.artikelen = JSON.parse(localStorage.shoppingCart);
		}
	}

	private updateLocalstorage(): void {
		localStorage.shoppingCart = JSON.stringify(this.winkelmandje.artikelen);
	}
}
