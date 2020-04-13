import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
	selector: 'app-finished',
	templateUrl: './finished.component.html',
	styleUrls: ['./finished.component.scss']
})
export class FinishedComponent implements OnInit {

	constructor(private readonly matSnackBar: MatSnackBar) { }

	ngOnInit() {
		this.openSnackBar('De auto is succesvol aangemeld.', 5000);
	}

	openSnackBar(message: string, duration: number) {
		this.matSnackBar.open(message, 'dismiss', {
			duration
		});
	}
}
