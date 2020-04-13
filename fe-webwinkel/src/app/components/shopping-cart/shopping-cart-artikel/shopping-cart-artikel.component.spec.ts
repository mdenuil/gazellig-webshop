import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShoppingCartArtikelComponent } from './shopping-cart-artikel.component';
import { MaterialModule } from '../../../material.module';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { ArtikelService } from '../../../services/artikel.service';
import { HttpClientModule } from '@angular/common/http';

describe('ShoppingCartArtikelComponent', () => {
	let component: ShoppingCartArtikelComponent;
	let fixture: ComponentFixture<ShoppingCartArtikelComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ShoppingCartArtikelComponent],
			providers: [ShoppingCartService, ArtikelService],
			imports: [MaterialModule, HttpClientModule]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ShoppingCartArtikelComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
