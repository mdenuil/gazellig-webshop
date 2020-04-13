import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtikelComponent } from './artikel.component';
import { ArtikelService } from '../../../services/artikel.service';
import { HttpClientModule } from '@angular/common/http';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { MaterialModule } from '../../../material.module';
import { InStockPipe } from '../../../pipes/in-stock.pipe';

describe('ArtikelComponent', () => {
	let component: ArtikelComponent;
	let fixture: ComponentFixture<ArtikelComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ArtikelComponent, InStockPipe],
			imports: [HttpClientModule, MaterialModule],
			providers: [ArtikelService, ShoppingCartService]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ArtikelComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();

		component.artikel = {
			"artikelNummer": 1,
			"aantal": 8,
			"naam": "HL Road Frame - Black, 58",
			"beschrijving": "Our lightest and best quality aluminum frame made from the newest alloy; it is welded and heat- treated for strength.Our innovative design results in maximum comfort and performance.",
			"prijs": 1431.5000,
			"afbeeldingUrl": "bike_lock_small.gif",
			"leverbaarVanaf": "1998-06-01T00:00:00",
			"leverbaarTot": null,
			"leverancierCode": "FR-R92B-58",
			"leverancier": "Koga Miyata",
			"categorieen": ["Components", "Road Frames"]
		};
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
