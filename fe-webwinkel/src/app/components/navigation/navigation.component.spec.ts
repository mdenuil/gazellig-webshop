import { LayoutModule } from '@angular/cdk/layout';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { NavigationComponent } from './navigation.component';
import { MaterialModule } from '../../material.module';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { ArtikelService } from '../../services/artikel.service';
import { HttpClientModule } from '@angular/common/http';

describe('NavigationComponent', () => {
	let component: NavigationComponent;
	let fixture: ComponentFixture<NavigationComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [NavigationComponent],
			imports: [
				NoopAnimationsModule,
				LayoutModule,
				MaterialModule,
				HttpClientModule
			],
			providers: [
				ShoppingCartService,
				ArtikelService
			]
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(NavigationComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should compile', () => {
		expect(component).toBeTruthy();
	});

	it('should render title', () => {
		const fixture = TestBed.createComponent(NavigationComponent);
		fixture.detectChanges();
		const compiled = fixture.debugElement.nativeElement;
		expect(compiled.querySelector('#title').textContent).toContain('Kantilever');
	});
});
