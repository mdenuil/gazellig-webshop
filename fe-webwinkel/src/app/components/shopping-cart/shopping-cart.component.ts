import { Component, OnInit } from '@angular/core';
import { ShoppingCartService } from '../../../app/services/shopping-cart.service';
import { BesteldArtikel } from '../../models/besteldArtikel.model';
import { AccountService } from '../../services/account.service';

@Component({
	selector: 'app-shopping-cart',
	templateUrl: './shopping-cart.component.html',
	styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit {
	besteldeArtikelen: BesteldArtikel[] = [];
	totalAmount: number;
	totalAmountWithoutVAT: number;
	isLoggedIn = false;
	shoppingCartUrl = "/shopping-cart"

	constructor(private readonly shoppingCart: ShoppingCartService, private readonly accountService: AccountService) { }

	ngOnInit() {
		this.besteldeArtikelen = this.shoppingCart.getArtikelen();
		this.totalAmount = this.shoppingCart.calculateTotalAmount();
		this.totalAmountWithoutVAT = this.shoppingCart.calculateTotalAmountWithoutVAT();

		this.isLoggedIn = this.accountService.isLoggedIn;
		this.accountService.currentUser.subscribe(user => this.isLoggedIn = Boolean(user));
	}

	calculateTotalAmount() {
		this.totalAmount = this.shoppingCart.calculateTotalAmount();
		this.totalAmountWithoutVAT = this.shoppingCart.calculateTotalAmountWithoutVAT();
	}
}
