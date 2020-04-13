import { Component, OnInit } from '@angular/core';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
	breakpoint: number;
	colspan: number;

	constructor() { }

	ngOnInit() {
		this.colspan = (window.innerWidth <= 850) ? 1 : 3;
		this.breakpoint = (window.innerWidth <= 850) ? 1 : 3;
	}

	onResize(event) {
		this.colspan = (window.innerWidth <= 850) ? 1 : 3;
		this.breakpoint = (event.target.innerWidth <= 850) ? 1 : 3;
	}
}
