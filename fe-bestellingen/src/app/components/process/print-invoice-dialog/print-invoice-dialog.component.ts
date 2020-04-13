import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Bestelling } from '../../../../app/models/bestelling.model';
import { BesteldArtikel } from '../../../models/besteldArtikel.model';

@Component({
	selector: 'app-print-invoice-dialog',
	templateUrl: './print-invoice-dialog.component.html',
	styleUrls: ['./print-invoice-dialog.component.scss']
})
export class PrintInvoiceDialogComponent implements OnInit {
	price = 0;

	constructor(
		private readonly dialogRef: MatDialogRef<PrintInvoiceDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: Bestelling
	) {
		dialogRef.disableClose = true;
	}

	ngOnInit() {
		this.price = this.data.artikelen
			.map((artikel: BesteldArtikel) => {
				return artikel.prijs * artikel.aantal;
			})
			.reduce((total, amount) => total + amount);
	}

	print() {
		this.dialogRef.close();
	}
}
