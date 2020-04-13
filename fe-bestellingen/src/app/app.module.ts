import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavigationComponent } from './components/navigation/navigation.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LoadingComponent } from './shared/loading/loading.component';
import { ErrorComponent } from './shared/error/error.component';
import { FinishedComponent } from './shared/finished/finished.component';
import { ArtikelService } from './services/artikel.service';
import { ProcessComponent } from './components/process/process.component';
import { BestellingService } from './services/bestelling.service';
import { ArtikelListComponent } from './components/process/artikel-list/artikel-list.component';
import { PrintInvoiceDialogComponent } from './components/process/print-invoice-dialog/print-invoice-dialog.component';
import { NgxPrintModule } from 'ngx-print';
import { PrintPakbonDialogComponent } from './components/process/print-pakbon-dialog/print-pakbon-dialog.component';

@NgModule({
	declarations: [
		AppComponent,
		NavigationComponent,
		LoadingComponent,
		ErrorComponent,
		FinishedComponent,
		ProcessComponent,
		ArtikelListComponent,
		PrintInvoiceDialogComponent,
		PrintPakbonDialogComponent,
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
		NgxPrintModule
	],
	providers: [
		ArtikelService,
		BestellingService
	],
	bootstrap: [AppComponent],
	entryComponents: [
		PrintInvoiceDialogComponent,
		PrintPakbonDialogComponent
	]
})
export class AppModule { }
