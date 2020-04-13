import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavigationComponent } from './components/navigation/navigation.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoadingComponent } from './shared/loading/loading.component';
import { ErrorComponent } from './shared/error/error.component';
import { FinishedComponent } from './shared/finished/finished.component';
import { ArtikelComponent } from './components/artikel-overzicht/artikel/artikel.component';
import { ArtikelService } from './services/artikel.service';
import { ArtikelOverzichtComponent } from './components/artikel-overzicht/artikel-overzicht.component';
import { ShoppingCartService } from './services/shopping-cart.service';
import { ShoppingCartComponent } from './components/shopping-cart/shopping-cart.component';
import { ShoppingCartArtikelComponent } from './components/shopping-cart/shopping-cart-artikel/shopping-cart-artikel.component';
import { ShoppingCartFormComponent } from './components/shopping-cart/shopping-cart-form/shopping-cart-form.component';
import { BestellingService } from './services/bestelling.service';
import { FooterComponent } from './components/footer/footer.component';
import { LoginComponent } from './components/login/login.component';
import { ToastrModule } from 'ngx-toastr';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { RegisterComponent } from './components/register/register.component';
import { ErrorInterceptor } from './interceptors/error.interceptor';
import { ArtikelDetailsComponent } from './components/artikel-overzicht/artikel-details/artikel-details.component';
import { InStockPipe } from './pipes/in-stock.pipe';
import { SearchComponent } from './components/search/search.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { OrderStatusPipe } from './pipes/orderStatus.pipe';
import { AuthGuard } from './guards/auth.guard';

@NgModule({
	declarations: [
		AppComponent,
		NavigationComponent,
		LoadingComponent,
		ErrorComponent,
		FinishedComponent,
		ArtikelComponent,
		ArtikelOverzichtComponent,
		ShoppingCartComponent,
		ShoppingCartArtikelComponent,
		ShoppingCartFormComponent,
		FooterComponent,
		LoginComponent,
		RegisterComponent,
		ArtikelDetailsComponent,
		InStockPipe,
		OrderStatusPipe,
		SearchComponent,
		OrderHistoryComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		BrowserAnimationsModule,
		LayoutModule,
		MaterialModule,
		FormsModule,
		ReactiveFormsModule,
		HttpClientModule,
		ToastrModule.forRoot({ positionClass: 'toast-bottom-right', preventDuplicates: true }),
	],
	providers: [
		ArtikelService,
		ShoppingCartService,
		BestellingService,
		{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
		{ provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
		AuthGuard
	],
	bootstrap: [AppComponent],
	entryComponents: [ArtikelDetailsComponent]
})
export class AppModule { }
