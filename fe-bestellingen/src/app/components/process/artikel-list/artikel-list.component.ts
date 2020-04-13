import { Component, OnInit, Input, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel, SelectionChange } from '@angular/cdk/collections';
import { forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

import { BesteldArtikel } from '../../../models/besteldArtikel.model';
import { Artikel } from '../../../models/artikel.model';
import { ArtikelService } from '../../../services/artikel.service';
import { BestellingService } from 'src/app/services/bestelling.service';
import { element } from 'protractor';

@Component({
	selector: 'app-artikel-list',
	templateUrl: './artikel-list.component.html',
	styleUrls: ['./artikel-list.component.scss']
})
export class ArtikelListComponent implements OnInit {
	@Input() artikelen: BesteldArtikel[] = [];
	@Output() isCompleted = new EventEmitter();
	@Output() artikelChecked = new EventEmitter<number>();
	checkboxesDisabled = false;

	displayedColumns: string[] = ['select', 'naam', 'artikelNummer', 'aantal'];
	selection = new SelectionModel<BesteldArtikel>(true, []);
	dataSource;

	constructor(private readonly artikelService: ArtikelService, private changeDetectorRefs: ChangeDetectorRef) { }

	ngOnInit() {
		this.selection.changed.subscribe((value: SelectionChange<BesteldArtikel>) => {
			if (value.added.length > 0) {
				this.artikelChecked.next(value.added[0].artikelNummer);
			} else if (value.removed.length > 0) {
				this.artikelChecked.next(value.removed[0].artikelNummer);
			}

			if (this.selection.selected.length === this.artikelen.length) {
				this.checkboxesDisabled = true;
				this.isCompleted.next();
			}
		});

		const observableArray = this.artikelen.map((artikel: BesteldArtikel) => this.artikelService.getArtikel(artikel.artikelNummer));

		forkJoin(observableArray).pipe(
			map((artikelen: Artikel[]) => artikelen.forEach((artikel: Artikel, index: number) => {
				this.artikelen[index].naam = artikel.naam;
				this.artikelen[index].prijs = artikel.prijs;
			}))
		).subscribe();


		this.dataSource = new MatTableDataSource<BesteldArtikel>(this.artikelen);

		this.dataSource.data.forEach(row => {
			if (row.verwerkt) {
				this.selection.select(row);
			}
		});

		this.changeDetectorRefs.detectChanges();

	}

	checkArtikel(artikel: BesteldArtikel) {
		artikel.isVerwerkt = true;
	}
}
