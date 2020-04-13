import { Component, OnInit } from '@angular/core';
import { Artikel } from '../../models/artikel.model';
import { ArtikelService } from '../../services/artikel.service';

@Component({
	selector: 'app-artikel-overzicht',
	templateUrl: './artikel-overzicht.component.html',
	styleUrls: ['./artikel-overzicht.component.scss']
})
export class ArtikelOverzichtComponent implements OnInit {
	artikelen: Artikel[];
	loading = true;

	constructor(private readonly artikelService: ArtikelService) { }

	ngOnInit(): void {
		this.getArtikelen();
	}

	getArtikelen(filter?: string): void {
		this.loading = true;

		this.artikelService.getArtikelen(filter)
			.subscribe({
				next: (artikelen) => {
					this.artikelen = artikelen;
					this.loading = false;
				},
				error: () => this.loading = false,
				complete: () => this.loading = false
			});
	}
}
