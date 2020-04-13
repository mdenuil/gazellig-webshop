import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtikelDetailsComponent } from './artikel-details.component';
import { MaterialModule } from '../../../material.module';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { HttpClientModule } from '@angular/common/http';
import { ArtikelService } from '../../../services/artikel.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { InStockPipe } from '../../../pipes/in-stock.pipe';

describe('ArtikelDetailsComponent', () => {
	let component: ArtikelDetailsComponent;
	let fixture: ComponentFixture<ArtikelDetailsComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ArtikelDetailsComponent, InStockPipe],
			imports: [
				MaterialModule,
				NoopAnimationsModule,
				HttpClientModule
			],
			providers: [
				ShoppingCartService,
				ArtikelService,
				{ provide: MatDialogRef, useValue: {} }, { provide: MAT_DIALOG_DATA, useValue: {} }
			]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ArtikelDetailsComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
