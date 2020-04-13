import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import { MaterialModule } from '../../material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from '../../app-routing.module';
import { ArtikelOverzichtComponent } from '../artikel-overzicht/artikel-overzicht.component';
import { ShoppingCartComponent } from '../shopping-cart/shopping-cart.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from '../login/login.component';
import { OrderHistoryComponent } from '../order-history/order-history.component';
import { OrderStatusPipe } from '../../pipes/orderStatus.pipe';

describe('RegisterComponent', () => {
	let component: RegisterComponent;
	let fixture: ComponentFixture<RegisterComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [
				LoginComponent,
				RegisterComponent,
				ArtikelOverzichtComponent,
				ShoppingCartComponent,
				OrderHistoryComponent,
				OrderStatusPipe
			],
			imports: [
				BrowserAnimationsModule,
				MaterialModule,
				ReactiveFormsModule,
				HttpClientModule,
				ToastrModule.forRoot(),
				AppRoutingModule,
			],
			schemas: [NO_ERRORS_SCHEMA],
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(RegisterComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
