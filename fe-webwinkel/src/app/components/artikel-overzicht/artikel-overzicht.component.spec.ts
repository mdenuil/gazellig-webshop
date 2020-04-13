import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtikelOverzichtComponent } from './artikel-overzicht.component';
import { ArtikelComponent } from './artikel/artikel.component';
import { ArtikelService } from '../../services/artikel.service';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from '../../material.module';
import { InStockPipe } from '../../pipes/in-stock.pipe';
import { SearchComponent } from '../search/search.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('ArtikelOverzichtComponent', () => {
	let component: ArtikelOverzichtComponent;
	let fixture: ComponentFixture<ArtikelOverzichtComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [
				ArtikelOverzichtComponent,
				ArtikelComponent,
				SearchComponent,
				InStockPipe
			],
			imports: [
				BrowserAnimationsModule,
				HttpClientModule,
				MaterialModule,
				ReactiveFormsModule
			],
			providers: [ArtikelService]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ArtikelOverzichtComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
