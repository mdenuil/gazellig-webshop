import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArtikelOverzichtComponent } from './components/artikel-overzicht/artikel-overzicht.component';
import { ShoppingCartComponent } from './components/shopping-cart/shopping-cart.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
	{ path: '', component: ArtikelOverzichtComponent },
	{ path: 'shopping-cart', component: ShoppingCartComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'my-orders', component: OrderHistoryComponent, canActivate: [AuthGuard] }
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
