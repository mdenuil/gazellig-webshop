import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessComponent } from './process.component';
import { MaterialModule } from 'src/app/material.module';
import { ArtikelListComponent } from './artikel-list/artikel-list.component';
import { BestellingService } from '../../services/bestelling.service';
import { HttpClientModule } from '@angular/common/http';

describe('ProcessComponent', () => {
	let component: ProcessComponent;
	let fixture: ComponentFixture<ProcessComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			imports: [MaterialModule, HttpClientModule],
			providers: [BestellingService],
			declarations: [ProcessComponent, ArtikelListComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ProcessComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
