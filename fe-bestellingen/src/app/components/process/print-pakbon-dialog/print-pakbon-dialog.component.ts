import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Bestelling } from '../../../models/bestelling.model';

@Component({
	selector: 'app-print-pakbon-dialog',
	templateUrl: './print-pakbon-dialog.component.html',
	styleUrls: ['./print-pakbon-dialog.component.scss']
})
export class PrintPakbonDialogComponent implements OnInit {
	constructor(
		private readonly dialogRef: MatDialogRef<PrintPakbonDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: Bestelling
	) {
		dialogRef.disableClose = true;
	}

	ngOnInit() { }

	print() {
		this.dialogRef.close();
	}
}
