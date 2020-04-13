import { Component, OnInit } from '@angular/core';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { AccountService } from '../../services/account.service';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {
    numberOfArtikelen: number;
    isLoggedIn = false;

    constructor(private readonly shoppingCart: ShoppingCartService, private readonly accountService: AccountService) { }

    ngOnInit(): void {
        this.numberOfArtikelen = this.shoppingCart.getArtikelAmount();

        this.shoppingCart.numberOfArtikelen
            .subscribe(amount => this.numberOfArtikelen = amount);

        this.accountService.currentUser.subscribe(user => this.isLoggedIn = Boolean(user));
    }

    logout() {
        this.accountService.logout();
    }
}
