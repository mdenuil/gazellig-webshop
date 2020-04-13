import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { MaterialModule } from '../../material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { ArtikelOverzichtComponent } from '../artikel-overzicht/artikel-overzicht.component';
import { ShoppingCartComponent } from '../shopping-cart/shopping-cart.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { RegisterComponent } from '../register/register.component';
import { AppRoutingModule } from '../../app-routing.module';
import { OrderHistoryComponent } from '../order-history/order-history.component';
import { OrderStatusPipe } from '../../pipes/orderStatus.pipe';

describe('LoginComponent', () => {
	let component: LoginComponent;
	let fixture: ComponentFixture<LoginComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [
				LoginComponent,
				ArtikelOverzichtComponent,
				ShoppingCartComponent,
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
				RouterTestingModule.withRoutes([])
			],
			schemas: [NO_ERRORS_SCHEMA],
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(LoginComponent);
		component = fixture.componentInstance;
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
