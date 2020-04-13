import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';

@Component({
	selector: 'app-search',
	templateUrl: './search.component.html',
	styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
	@Output() searchInput: EventEmitter<string> = new EventEmitter<string>();

	searchForm: FormGroup;

	constructor(private readonly formBuilder: FormBuilder) { }

	ngOnInit() {
		this.searchForm = this.formBuilder.group({
			input: [''],
		});

		this.f.input.valueChanges
			.pipe(
				debounceTime(500)
			).subscribe(value => this.searchInput.emit(value));
	}

	get f() { return this.searchForm.controls; }
}
