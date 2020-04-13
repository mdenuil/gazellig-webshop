import { Component, OnInit } from '@angular/core';
import { BestellingService } from '../../services/bestelling.service';
import { Bestelling } from '../../models/bestelling.model';
import { PrintInvoiceDialogComponent } from './print-invoice-dialog/print-invoice-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { PrintPakbonDialogComponent } from './print-pakbon-dialog/print-pakbon-dialog.component';

@Component({
    selector: 'app-process',
    templateUrl: './process.component.html',
    styleUrls: ['./process.component.scss']
})
export class ProcessComponent implements OnInit {
	bestelling: Bestelling;
	itemsCompleted = false;
	invoicePrinted = false;
	pakbonPrinted = false;
	finishBestellingLoading = false;

	constructor(private readonly bestellingService: BestellingService, private readonly dialog: MatDialog) { }

	ngOnInit() {
		this.loadNewBestelling();
	}

	openPrintPakbonDialog(): void {
		const dialogRef = this.dialog.open(PrintPakbonDialogComponent, {
			width: '900px',
			data: this.bestelling
		});

		dialogRef.afterClosed().subscribe(
			() => this.pakbonPrinted = true
		);
	}

	openPrintInvoiceDialog(): void {
		const dialogRef = this.dialog.open(PrintInvoiceDialogComponent, {
			width: '900px',
			data: this.bestelling
		});

		dialogRef.afterClosed().subscribe(
			() => this.invoicePrinted = true
		);
	}

	finishBestelling(): void {
		this.finishBestellingLoading = true;
		this.bestellingService.finishBestelling(this.bestelling.bestelNummer)
			.subscribe({
				error: () => this.finishBestellingLoading = false,
				complete: () => this.loadNewBestelling()
			});
	}

	private loadNewBestelling() {
		this.bestelling = null;

		this.itemsCompleted = false;
		this.invoicePrinted = false;
		this.pakbonPrinted = false;
		this.finishBestellingLoading = false;

		this.bestellingService.getNextBestelling().subscribe(
			bestelling => this.bestelling = bestelling
		);
	}
	
	setArtikelChecked(artikelNummer: number): void {
        this.bestellingService.setArtikelChecked(this.bestelling.bestelNummer, artikelNummer).subscribe();
    }
}
