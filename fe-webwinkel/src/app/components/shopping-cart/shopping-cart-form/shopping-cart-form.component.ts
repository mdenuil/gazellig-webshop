import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BestellingService } from '../../../../app/services/bestelling.service';
import { Adres } from '../../../../app/models/adres.model';
import { Bestelling } from '../../../../app/models/bestelling.model';
import { ShoppingCartService } from '../../../../app/services/shopping-cart.service';
import { AccountService } from '../../../services/account.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
	selector: 'app-shopping-cart-form',
	templateUrl: './shopping-cart-form.component.html',
	styleUrls: ['./shopping-cart-form.component.scss']
})
export class ShoppingCartFormComponent implements OnInit {
	form: FormGroup;

	constructor(
		private readonly bestellingService: BestellingService,
		private readonly shoppingCartService: ShoppingCartService,
		private readonly accountService: AccountService,
		private readonly toastr: ToastrService,
		private readonly router: Router
	) { }

	ngOnInit() {
		this.form = new FormGroup({
			initialen: new FormControl(undefined, Validators.required),
			achternaam: new FormControl(undefined, Validators.required),
			email: new FormControl(undefined, Validators.required),
			straatnaam: new FormControl(undefined, Validators.required),
			huisnummer: new FormControl(undefined, Validators.required),
			postcode: new FormControl(undefined, [Validators.required, Validators.pattern("[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$")]),
			woonplaats: new FormControl(undefined, Validators.required)
		});

		this.accountService.currentUser.subscribe(user =>
			this.form.patchValue({
				initialen: user.initialen,
				achternaam: user.achternaam,
				email: user.email
			})
		);
	}

	submitForm() {
		const adres: Adres = {
			straatnaam: this.form.value.straatnaam,
			huisnummer: this.form.value.huisnummer,
			postcode: this.form.value.postcode,
			woonplaats: this.form.value.woonplaats
		};

		const bestelling: Bestelling = {
			initialen: this.form.value.initialen,
			achternaam: this.form.value.achternaam,
			email: this.form.value.email,
			status: null,
			afleverAdres: adres,
			factuurAdres: adres,
			artikelen: this.shoppingCartService.getArtikelen()
		};

		this.bestellingService.sendBestelling(bestelling)
			.subscribe({
				next: () => {
					this.shoppingCartService.emtpyShoppingCart();
					this.toastr.success('Order placed successfully', null, {
						timeOut: 6000
					});
					this.router.navigate(['/']);
				}
			});
	}
}
